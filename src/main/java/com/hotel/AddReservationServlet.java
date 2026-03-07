package com.hotel;

import com.hotel.dao.ReservationDAO;
import com.hotel.model.Reservation;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.time.LocalDate;

@WebServlet("/add-reservation")
public class AddReservationServlet extends HttpServlet {

    private final ReservationDAO dao = new ReservationDAO();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        resp.setContentType("text/plain; charset=UTF-8");

        // ✅ Session protection
        HttpSession session = req.getSession(false);
        if (session == null || session.getAttribute("username") == null) {
            resp.getWriter().println("❌ Please login first.");
            return;
        }

        // ✅ Read values
        String reservationNo = trim(req.getParameter("reservationNo"));
        String guestName     = trim(req.getParameter("guestName"));
        String address       = trim(req.getParameter("address"));
        String contact       = trim(req.getParameter("contact"));
        String roomType      = trim(req.getParameter("roomType"));
        String checkIn       = trim(req.getParameter("checkIn"));
        String checkOut      = trim(req.getParameter("checkOut"));

        // ✅ Format Reservation Number to R0001 style
        reservationNo = formatReservationNo(reservationNo);

        // ✅ Basic validation
        if (reservationNo.isEmpty() || guestName.isEmpty() || address.isEmpty() ||
                contact.isEmpty() || roomType.isEmpty() || checkIn.isEmpty() || checkOut.isEmpty()) {

            resp.getWriter().println("❌ All fields are required.");
            return;
        }

        // ✅ Contact validation
        if (!contact.matches("\\d{10}")) {
            resp.getWriter().println("❌ Contact must be 10 digits (e.g., 0771234567).");
            return;
        }

        // ✅ Date validation
        LocalDate inDate, outDate;
        try {
            inDate = LocalDate.parse(checkIn);
            outDate = LocalDate.parse(checkOut);
        } catch (Exception e) {
            resp.getWriter().println("❌ Invalid date format.");
            return;
        }

        if (!outDate.isAfter(inDate)) {
            resp.getWriter().println("❌ Check-out date must be after check-in date.");
            return;
        }

        // ✅ Check duplicate reservation number
        try {

            if (dao.exists(reservationNo)) {
                resp.getWriter().println("❌ This reservation number already exists.");
                return;
            }

            Reservation r = new Reservation(
                    reservationNo,
                    guestName,
                    address,
                    contact,
                    roomType,
                    checkIn,
                    checkOut
            );

            boolean ok = dao.addReservation(r);

            if (ok) {
                resp.getWriter().println("✅ Reservation Saved!");
            } else {
                resp.getWriter().println("❌ Save failed.");
            }

        } catch (Exception e) {
            resp.getWriter().println("❌ Error: " + e.getMessage());
        }
    }

    // ✅ Remove spaces
    private String trim(String s) {
        return (s == null) ? "" : s.trim();
    }

    // ✅ Convert number to R0001 format
    private String formatReservationNo(String input) {

        if (input == null || input.isEmpty())
            return "";

        // remove R if user typed it
        input = input.replaceAll("[^0-9]", "");

        try {
            int number = Integer.parseInt(input);
            return String.format("R%04d", number);
        } catch (Exception e) {
            return "";
        }
    }
}