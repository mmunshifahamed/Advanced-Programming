package com.hotel;

import com.hotel.dao.ReservationDAO;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;

@WebServlet("/delete-reservation")
public class DeleteReservationServlet extends HttpServlet {

    private final ReservationDAO dao = new ReservationDAO();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        resp.setContentType("text/plain; charset=UTF-8");

        HttpSession session = req.getSession(false);
        if (session == null || session.getAttribute("username") == null) {
            resp.getWriter().println("❌ Please login first.");
            return;
        }

        String reservationNo = req.getParameter("reservationNo");

        if (reservationNo == null || reservationNo.trim().isEmpty()) {
            resp.getWriter().println("❌ Reservation number required.");
            return;
        }

        try {

            boolean deleted = dao.deleteReservation(reservationNo);

            if (deleted) {
                resp.getWriter().println("✅ Reservation deleted");
            } else {
                resp.getWriter().println("❌ Reservation not found");
            }

        } catch (Exception e) {
            resp.getWriter().println("❌ Error: " + e.getMessage());
        }
    }
}