package com.hotel;

import com.hotel.db.DBConnection;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("text/plain");

        String username = req.getParameter("username");
        String password = req.getParameter("password");

        if (username == null || password == null || username.isEmpty() || password.isEmpty()) {
            resp.getWriter().println("❌ Please enter username and password");
            return;
        }

        String sql = "SELECT * FROM users WHERE username=? AND password=?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, username);
            ps.setString(2, password);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                // create session
                HttpSession session = req.getSession();
                session.setAttribute("username", username);

                resp.getWriter().println("✅ Login successful!");
            } else {
                resp.getWriter().println("❌ Invalid username or password");
            }

        } catch (Exception e) {
            resp.getWriter().println("❌ Error: " + e.getMessage());
        }
    }
}