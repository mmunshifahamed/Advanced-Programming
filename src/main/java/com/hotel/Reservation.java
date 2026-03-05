package com.hotel.model;

public class Reservation {
    private String reservationNo;
    private String guestName;
    private String address;
    private String contact;
    private String roomType;
    private String checkIn;
    private String checkOut;

    public Reservation(String reservationNo, String guestName, String address, String contact,
                       String roomType, String checkIn, String checkOut) {
        this.reservationNo = reservationNo;
        this.guestName = guestName;
        this.address = address;
        this.contact = contact;
        this.roomType = roomType;
        this.checkIn = checkIn;
        this.checkOut = checkOut;
    }

    public String getReservationNo() { return reservationNo; }
    public String getGuestName() { return guestName; }
    public String getAddress() { return address; }
    public String getContact() { return contact; }
    public String getRoomType() { return roomType; }
    public String getCheckIn() { return checkIn; }
    public String getCheckOut() { return checkOut; }
}