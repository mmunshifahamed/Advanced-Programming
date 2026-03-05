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

        // ✅ Read + trim values
        String reservationNo = trim(req.getParameter("reservationNo"));
        String guestName     = trim(req.getParameter("guestName"));
        String address       = trim(req.getParameter("address"));
        String contact       = trim(req.getParameter("contact"));
        String roomType      = trim(req.getParameter("roomType"));
        String checkIn       = trim(req.getParameter("checkIn"));
        String checkOut      = trim(req.getParameter("checkOut"));

        // ✅ Basic validation
        if (reservationNo.isEmpty() || guestName.isEmpty() || address.isEmpty() ||
                contact.isEmpty() || roomType.isEmpty() || checkIn.isEmpty() || checkOut.isEmpty()) {
            resp.getWriter().println("❌ All fields are required.");
            return;
        }

        // ✅ Contact validation (10 digits)
        if (!contact.matches("\\d{10}")) {
            resp.getWriter().println("❌ Contact must be 10 digits (e.g., 0771234567).");
            return;
        }

        // ✅ Date validation (check-out must be after check-in)
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

        // ✅ DAO logic: duplicate check + insert
        try {
            if (dao.exists(reservationNo)) {
                resp.getWriter().println("❌ Reservation number already exists.");
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
            resp.getWriter().println(ok ? "✅ Reservation Saved!" : "❌ Save failed.");

        } catch (Exception e) {
            resp.getWriter().println("❌ Error: " + e.getMessage());
        }
    }

    private String trim(String s) {
        return (s == null) ? "" : s.trim();
    }
}