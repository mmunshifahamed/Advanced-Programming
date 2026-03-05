package com.hotel;

import com.hotel.db.DBConnection;
import com.hotel.strategy.RateFactory;
import com.hotel.strategy.RateStrategy;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.sql.*;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@WebServlet("/bill")
public class BillServlet extends HttpServlet {

    private String lkr(long amount) {
        return "Rs. " + String.format("%,d", amount);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("text/plain; charset=UTF-8");

        // ✅ Session protection
        HttpSession session = req.getSession(false);
        if (session == null || session.getAttribute("username") == null) {
            resp.getWriter().println("❌ Please login first.");
            return;
        }

        // ✅ Input validation
        String reservationNo = req.getParameter("reservationNo");
        if (reservationNo == null || reservationNo.trim().isEmpty()) {
            resp.getWriter().println("❌ Reservation number required.");
            return;
        }
        reservationNo = reservationNo.trim();

        String sql = "SELECT guest_name, room_type, check_in, check_out " +
                "FROM reservations WHERE reservation_no=?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, reservationNo);

            try (ResultSet rs = ps.executeQuery()) {

                if (!rs.next()) {
                    resp.getWriter().println("❌ No reservation found for: " + reservationNo);
                    return;
                }

                String guestName = rs.getString("guest_name");
                String roomType  = rs.getString("room_type");
                Date checkInDate = rs.getDate("check_in");
                Date checkOutDate = rs.getDate("check_out");

                if (checkInDate == null || checkOutDate == null) {
                    resp.getWriter().println("❌ Reservation dates are missing.");
                    return;
                }

                LocalDate in  = checkInDate.toLocalDate();
                LocalDate out = checkOutDate.toLocalDate();

                long nights = ChronoUnit.DAYS.between(in, out);
                if (nights <= 0) nights = 1; // safety fallback

                // ✅ Strategy + Factory for room rate
                RateStrategy strategy = RateFactory.getStrategy(roomType);
                if (strategy == null) {
                    resp.getWriter().println("❌ Invalid room type: " + roomType);
                    return;
                }

                int rate = strategy.getRatePerNight();
                long total = (long) rate * nights;

                String bill =
                        "========== HOTEL BILL ==========\n" +
                                "Reservation No: " + reservationNo + "\n" +
                                "Guest Name: " + guestName + "\n" +
                                "Room Type: " + roomType + "\n" +
                                "Check-in: " + in + "\n" +
                                "Check-out: " + out + "\n" +
                                "Nights: " + nights + "\n" +
                                "Rate per Night: " + lkr(rate) + "\n" +
                                "--------------------------------\n" +
                                "TOTAL: " + lkr(total) + "\n" +
                                "================================";

                resp.getWriter().println(bill);
            }

        } catch (Exception e) {
            resp.getWriter().println("❌ Error: " + e.getMessage());
        }
    }
}