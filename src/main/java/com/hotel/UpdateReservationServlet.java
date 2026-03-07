package com.hotel;

import com.hotel.dao.ReservationDAO;
import com.hotel.model.Reservation;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;

@WebServlet("/update-reservation")
public class UpdateReservationServlet extends HttpServlet {

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
        String guestName = req.getParameter("guestName");
        String address = req.getParameter("address");
        String contact = req.getParameter("contact");
        String roomType = req.getParameter("roomType");
        String checkIn = req.getParameter("checkIn");
        String checkOut = req.getParameter("checkOut");

        try {

            Reservation r = new Reservation(
                    reservationNo,
                    guestName,
                    address,
                    contact,
                    roomType,
                    checkIn,
                    checkOut
            );

            boolean updated = dao.updateReservation(r);

            if (updated) {
                resp.getWriter().println("✅ Reservation updated");
            } else {
                resp.getWriter().println("❌ Update failed");
            }

        } catch (Exception e) {
            resp.getWriter().println("❌ Error: " + e.getMessage());
        }
    }
}