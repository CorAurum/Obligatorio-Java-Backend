package com.example;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import javax.sql.DataSource;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;

@WebServlet("/test-ds")
public class TestServlet extends jakarta.servlet.http.HttpServlet {

    @Override
    protected void doGet(jakarta.servlet.http.HttpServletRequest req, jakarta.servlet.http.HttpServletResponse resp)
            throws ServletException, IOException {

        resp.setContentType("text/plain");
        PrintWriter out = resp.getWriter();

        try {
            InitialContext ctx = new InitialContext();
            // Use your configured JNDI name
            DataSource ds = (DataSource) ctx.lookup("java:/PostgresDS");

            try (Connection conn = ds.getConnection()) {
                out.println("✅ Successfully connected to PostgreSQL using java:/PostgresDS!");
            }

        } catch (NamingException | SQLException e) {
            out.println("❌ Connection failed: " + e.getMessage());
            e.printStackTrace(out);
        }
    }
}
