package com.hotel;

import com.hotel.db.DBConnection;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;

@WebServlet("/view-all")
public class ViewAllServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        resp.setContentType("application/json");
        PrintWriter out = resp.getWriter();

        HttpSession session = req.getSession(false);
        if (session == null || session.getAttribute("username") == null) {
            out.print("[]");
            return;
        }

        String sql = "SELECT reservation_no, guest_name, room_type, check_in, check_out FROM reservations";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            StringBuilder json = new StringBuilder("[");
            boolean first = true;

            while (rs.next()) {
                if (!first) json.append(",");
                first = false;

                json.append("{")
                        .append("\"reservationNo\":\"").append(rs.getString("reservation_no")).append("\",")
                        .append("\"guestName\":\"").append(rs.getString("guest_name")).append("\",")
                        .append("\"roomType\":\"").append(rs.getString("room_type")).append("\",")
                        .append("\"checkIn\":\"").append(rs.getDate("check_in")).append("\",")
                        .append("\"checkOut\":\"").append(rs.getDate("check_out")).append("\"")
                        .append("}");
            }

            json.append("]");
            out.print(json);

        } catch (Exception e) {
            out.print("[]");
        }
    }
}