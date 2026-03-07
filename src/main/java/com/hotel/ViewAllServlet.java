package com.hotel;

import com.hotel.dao.ReservationDAO;
import com.hotel.model.Reservation;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@WebServlet("/view-all")
public class ViewAllServlet extends HttpServlet {

    private final ReservationDAO dao = new ReservationDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        resp.setContentType("application/json; charset=UTF-8");
        PrintWriter out = resp.getWriter();

        HttpSession session = req.getSession(false);
        if (session == null || session.getAttribute("username") == null) {
            out.print("[]");
            return;
        }

        try {

            List<Reservation> list = dao.findAll();

            StringBuilder json = new StringBuilder("[");
            boolean first = true;

            for (Reservation r : list) {

                if (!first) json.append(",");
                first = false;

                json.append("{")
                        .append("\"reservationNo\":\"").append(r.getReservationNo()).append("\",")
                        .append("\"guestName\":\"").append(r.getGuestName()).append("\",")
                        .append("\"roomType\":\"").append(r.getRoomType()).append("\",")
                        .append("\"checkIn\":\"").append(r.getCheckIn()).append("\",")
                        .append("\"checkOut\":\"").append(r.getCheckOut()).append("\"")
                        .append("}");
            }

            json.append("]");
            out.print(json);

        } catch (Exception e) {
            out.print("[]");
        }
    }
}