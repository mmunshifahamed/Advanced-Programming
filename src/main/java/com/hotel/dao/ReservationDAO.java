package com.hotel.dao;

import com.hotel.db.DBConnection;
import com.hotel.model.Reservation;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class ReservationDAO {

    // ---------------------------------------------------
    // Check if reservation exists
    // ---------------------------------------------------
    public boolean exists(String reservationNo) throws Exception {

        String sql = "SELECT reservation_no FROM reservations WHERE reservation_no=?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, reservationNo);

            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }

    // ---------------------------------------------------
    // Add reservation
    // ---------------------------------------------------
    public boolean addReservation(Reservation r) throws Exception {

        String sql = "INSERT INTO reservations(reservation_no, guest_name, address, contact, room_type, check_in, check_out) VALUES(?,?,?,?,?,?,?)";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, r.getReservationNo());
            ps.setString(2, r.getGuestName());
            ps.setString(3, r.getAddress());
            ps.setString(4, r.getContact());
            ps.setString(5, r.getRoomType());
            ps.setString(6, r.getCheckIn());
            ps.setString(7, r.getCheckOut());

            return ps.executeUpdate() > 0;
        }
    }

    // ---------------------------------------------------
    // Find reservation by ID
    // ---------------------------------------------------
    public Reservation findById(String reservationNo) throws Exception {

        String sql = "SELECT * FROM reservations WHERE reservation_no=?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, reservationNo);

            try (ResultSet rs = ps.executeQuery()) {

                if (!rs.next()) return null;

                return new Reservation(
                        rs.getString("reservation_no"),
                        rs.getString("guest_name"),
                        rs.getString("address"),
                        rs.getString("contact"),
                        rs.getString("room_type"),
                        rs.getString("check_in"),
                        rs.getString("check_out")
                );
            }
        }
    }

    // ---------------------------------------------------
    // Get all reservations
    // ---------------------------------------------------
    public List<Reservation> findAll() throws Exception {

        List<Reservation> list = new ArrayList<>();

        String sql = "SELECT * FROM reservations ORDER BY reservation_no";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {

                Reservation r = new Reservation(
                        rs.getString("reservation_no"),
                        rs.getString("guest_name"),
                        rs.getString("address"),
                        rs.getString("contact"),
                        rs.getString("room_type"),
                        rs.getString("check_in"),
                        rs.getString("check_out")
                );

                list.add(r);
            }
        }

        return list;
    }

    // ---------------------------------------------------
    // Update reservation
    // ---------------------------------------------------
    public boolean updateReservation(Reservation r) throws Exception {

        String sql = "UPDATE reservations SET guest_name=?, address=?, contact=?, room_type=?, check_in=?, check_out=? WHERE reservation_no=?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, r.getGuestName());
            ps.setString(2, r.getAddress());
            ps.setString(3, r.getContact());
            ps.setString(4, r.getRoomType());
            ps.setString(5, r.getCheckIn());
            ps.setString(6, r.getCheckOut());
            ps.setString(7, r.getReservationNo());

            return ps.executeUpdate() > 0;
        }
    }

    // ---------------------------------------------------
    // Delete reservation
    // ---------------------------------------------------
    public boolean deleteReservation(String reservationNo) throws Exception {

        String sql = "DELETE FROM reservations WHERE reservation_no=?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, reservationNo);

            return ps.executeUpdate() > 0;
        }
    }
}