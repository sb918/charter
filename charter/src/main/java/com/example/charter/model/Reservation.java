package com.example.charter.model;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Entity
@Table(name = "reservations")
public class Reservation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;


    @NotNull(message = "Yacht's ID must not be empty")
    private int reservedYachtId;
    //@Column(name = "reserved_customer_id")
    @NotNull(message = "Customer's ID must not be empty")
    private int reservedCustomerId;
    //@Column(name = "reservation_from")
    @NotNull(message = "Date from must not be empty")
    @DateTimeFormat(pattern = "yyyy-MM-dd")

    private LocalDate reservationFrom;
    //@Column(name = "reservation_to")
    @NotNull(message = "Date to must not be empty")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate reservationTo;


    public Reservation(){
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getReservedYachtId() {
        return reservedYachtId;
    }

    public void setReservedYachtId(int reservedYachtId) {
        this.reservedYachtId = reservedYachtId;
    }

    public int getReservedCustomerId() {
        return reservedCustomerId;
    }

    public void setReservedCustomerId(int reservedCustomerId) {
        this.reservedCustomerId = reservedCustomerId;
    }

    public LocalDate getReservationFrom() {
        return reservationFrom;
    }

    public void setReservationFrom(LocalDate reservationFrom) {
        this.reservationFrom = reservationFrom;
    }

    public LocalDate getReservationTo() {
        return reservationTo;
    }

    public void setReservationTo(LocalDate reservationTo) {
        this.reservationTo = reservationTo;
    }
}
