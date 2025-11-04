package com.example;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import Entity.CentroDeSalud;

import java.io.IOException;

@WebServlet("/init")
public class InitServlet extends HttpServlet {

    @PersistenceContext(unitName = "defaultPU")
    private EntityManager em;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        CentroDeSalud c = new CentroDeSalud();
        c.setNombre("Clinica Central");
        c.setDireccion("Av. Siempre Viva 123");
        c.setTelefono("123456");
        c.setTipoInstitucion("Privada");

        em.persist(c); // Forzamos a usar JPA

        resp.getWriter().println("Persisted Clinica!");
    }
}
