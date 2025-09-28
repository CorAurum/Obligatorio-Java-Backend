package Service;

import Class.pdi.pdiResponse;
import jakarta.ejb.Stateless;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.w3c.dom.*;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

@ApplicationScoped
public class pdiService {

    private static final String SOAP_ENDPOINT = "https://pdi.enbondi.xyz/ws"; // real service URL

    public pdiResponse callObtPersonaPorDoc(String org, String password, String doc, String tipoDoc) {
        // 1. Craft SOAP request**
        String soapRequest =
                "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
                        "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" " +
                        "xmlns:dnic=\"http://agesic.gub.uy/dnic\">" +
                        "<soapenv:Header/>" +
                        "<soapenv:Body>" +
                        "<dnic:ObtPersonaPorDocRequest>" +
                        "<dnic:organizacion>" + org + "</dnic:organizacion>" +
                        "<dnic:passwordEntidad>" + password + "</dnic:passwordEntidad>" +
                        "<dnic:nroDocumento>" + doc + "</dnic:nroDocumento>" +
                        "<dnic:tipoDocumento>" + tipoDoc + "</dnic:tipoDocumento>" +
                        "</dnic:ObtPersonaPorDocRequest>" +
                        "</soapenv:Body>" +
                        "</soapenv:Envelope>";

        // 2. Send request
        Client client = ClientBuilder.newClient();
        Response response = client.target(SOAP_ENDPOINT)
                .request(MediaType.TEXT_XML)
                .header("Content-Type", "text/xml;charset=UTF-8")
                .post(Entity.entity(soapRequest, MediaType.TEXT_XML));

        String rawXml = response.readEntity(String.class);
        response.close();
        client.close();

        // 3. Parse response
        return parseResponse(rawXml);
    }

    private pdiResponse parseResponse(String xml) {
        pdiResponse result = new pdiResponse();

        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setNamespaceAware(true);
            Document doc = factory.newDocumentBuilder()
                    .parse(new ByteArrayInputStream(xml.getBytes(StandardCharsets.UTF_8)));

            Node body = doc.getElementsByTagNameNS("http://schemas.xmlsoap.org/soap/envelope/", "Body").item(0);
            if (body == null) return result;

            // Check for errores
            NodeList errores = doc.getElementsByTagNameNS("http://agesic.gub.uy/dnic", "errores");
            if (errores.getLength() > 0) {
                Node msg = doc.getElementsByTagNameNS("http://agesic.gub.uy/dnic", "mensaje").item(0);
                if (msg != null) {
                    String code = getTextContent(doc, "CodMensaje");
                    String desc = getTextContent(doc, "Descripcion");
                    result.setError(code, desc);
                }
                return result;
            }

            // Check for objPersona
            NodeList personas = doc.getElementsByTagNameNS("http://agesic.gub.uy/dnic", "objPersona");
            if (personas.getLength() > 0) {
                result.setCodTipoDocumento(getTextContent(doc, "codTipoDocumento"));
                result.setNroDocumento(getTextContent(doc, "nroDocumento"));
                result.setNombre1(getTextContent(doc, "nombre1"));
                result.setNombre2(getTextContent(doc, "nombre2"));
                result.setApellido1(getTextContent(doc, "apellido1"));
                result.setApellido2(getTextContent(doc, "apellido2"));
                result.setSexo(getTextContent(doc, "sexo"));
                result.setFechaNacimiento(getTextContent(doc, "fechaNacimiento"));
                result.setCodNacionalidad(getTextContent(doc, "codNacionalidad"));
                result.setNombreEnCedula(getTextContent(doc, "nombreEnCedula"));
            }

            // Check for warnings
            NodeList warnings = doc.getElementsByTagNameNS("http://agesic.gub.uy/dnic", "warnings");
            if (warnings.getLength() > 0) {
                String code = getTextContent(doc, "CodMensaje");
                String desc = getTextContent(doc, "Descripcion");
                result.addWarning(code, desc);
            }

        } catch (Exception e) {
            result.setError("9999", "Parsing error: " + e.getMessage());
        }

        return result;
    }

    private String getTextContent(Document doc, String tag) {
        NodeList nodes = doc.getElementsByTagNameNS("http://agesic.gub.uy/dnic", tag);
        if (nodes.getLength() > 0) {
            return nodes.item(0).getTextContent();
        }
        return null;
    }
}

