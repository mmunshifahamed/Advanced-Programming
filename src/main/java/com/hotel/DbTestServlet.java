package com.hotel;

import com.hotel.db.DBConnection;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.Connection;

@WebServlet("/dbtest")
public class DbTestServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("text/plain");

        try (Connection con = DBConnection.getConnection()) {
            resp.getWriter().println("✅ DB Connected Successfully!");
        } catch (Exception e) {
            resp.getWriter().println("❌ DB Connection Failed!");
            resp.getWriter().println(e.getMessage());
        }
    }
}