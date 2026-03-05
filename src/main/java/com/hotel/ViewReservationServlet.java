package com.hotel;

import com.hotel.db.DBConnection;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

@WebServlet("/view-reservation")
public class ViewReservationServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("text/plain");

        // session protection
        HttpSession session = req.getSession(false);
        if (session == null || session.getAttribute("username") == null) {
            resp.getWriter().println("❌ Please login first.");
            return;
        }

        String reservationNo = req.getParameter("reservationNo");
        if (reservationNo == null || reservationNo.isEmpty()) {
            resp.getWriter().println("❌ Reservation number required.");
            return;
        }

        String sql = "SELECT * FROM reservations WHERE reservation_no=?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, reservationNo);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                String output =
                        "✅ Reservation Found\n" +
                                "Reservation No: " + rs.getString("reservation_no") + "\n" +
                                "Guest Name: " + rs.getString("guest_name") + "\n" +
                                "Address: " + rs.getString("address") + "\n" +
                                "Contact: " + rs.getString("contact") + "\n" +
                                "Room Type: " + rs.getString("room_type") + "\n" +
                                "Check-in: " + rs.getDate("check_in") + "\n" +
                                "Check-out: " + rs.getDate("check_out");

                resp.getWriter().println(output);
            } else {
                resp.getWriter().println("❌ No reservation found for: " + reservationNo);
            }

        } catch (Exception e) {
            resp.getWriter().println("❌ Error: " + e.getMessage());
        }
    }
}