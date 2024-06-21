package com.example.charter.controller;

import com.example.charter.model.*;
import com.example.charter.controller.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.core.Local;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.FluentQuery;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;


import javax.swing.*;
import java.time.LocalDate;
import java.time.chrono.ChronoLocalDate;
import java.time.chrono.ChronoLocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

@Controller

public class ReservationController{


    private static final Logger logger = LoggerFactory.getLogger(ReservationController.class);
    private final ReservationRepository reservationRepository;

    ReservationController(final ReservationRepository reservationRepository){
        this.reservationRepository = reservationRepository;
    }
    @Autowired
    CustomerController customerController;
    @Autowired
    CustomerRepository customerRepository;

    @Autowired
    YachtRepository yachtRepository;

    @Autowired
    YachtController yachtController;

    //SHOW ALL RESESRVATIONS LIST

    //SHOW ALL CUSTOMERS LIST
    @RequestMapping(method = RequestMethod.GET, path = "/api/reservations", params = {"!page", "!size", "!sort"})
    ResponseEntity<List<Reservation>>readAllReservations(){
        logger.warn("Exposing all the Reservations");
        return ResponseEntity.ok(reservationRepository.findAll());
    }

    @RequestMapping(method = RequestMethod.GET, path = "/reservations")
    public String showAllReservationsOnly(Reservation reservation, Model model){
        reservationRepository.findAll().stream().collect(Collectors.toList());

        model.addAttribute("reservations", reservationRepository.findAll());
        logger.warn("Exposing all the Reservations");

        return "reservations2";
    }

    //////main method that shows new reservation form. yachtRep, resRep and customerRep is needed

    @RequestMapping(method = RequestMethod.GET, path = "/reservations1")
    public String showAllReservations(Reservation reservation, Model model){
        reservationRepository.findAll().stream().collect(Collectors.toList());

        model.addAttribute("reservations", reservationRepository.findAll());
        logger.warn("Exposing all the Reservations");
        model.addAttribute("yachts", yachtRepository.findAll());
        model.addAttribute("customers", customerRepository.findAll());





        return "reservations2";
    }


    ///////CREATING NEW RESERVATIONS thymeleaf connected from html template
    @RequestMapping(method = RequestMethod.POST, path = "/reservations1")
    public String makeReservation(@ModelAttribute("reservation") Reservation reservation){

        reservationRepository.save(reservation);
///////here was placed modified return statement

        return "reservations2";

    }



    //SHOW ALL RESERVATIONS LIST
    @RequestMapping( method = RequestMethod.GET, path = "/showAllReservationsList")
    public String showAllReservationsList(Reservation reservation, Model model){
        reservationRepository.findAll().stream().collect(Collectors.toList());

        model.addAttribute("reservations", reservationRepository.findAll());
        logger.warn("Exposing all the Yachts List");

//have to make method reservationsInDate reading the served dates at localtime format
//try to show dates in types localdate and string - console will earn listed reservation dates when will show list of reservations
        //reservationsInDate(reservation, reservation.getReservationFrom(), reservation.getReservationTo());
        //reservationsInDate2(reservation, "2023-06-01" , "2023-06-30");

        return "view_reservations";
    }

    //SHOW DATE SELECT FORM
    @RequestMapping( method = RequestMethod.GET, path ="/showNewReservationForm2")
    public String selectReservationDate( Model model){


        Reservation reservation = new Reservation();
        model.addAttribute("reservation", reservation);

        return "reservation_date_select";
    }

    @RequestMapping( method = RequestMethod.GET, path ="/showNewReservationForm21")
    public String selectReservationDateONLY( Reservation reservation, Model model){

        model.addAttribute("reservation", reservation);
        model.addAttribute("customers", customerRepository.findAll());

        return "reservation_date_select2";
    }

    @RequestMapping( method = RequestMethod.POST, path ="/showNewReservationForm21")
    public String submitReservationDateOnly(@ModelAttribute("reservation") Reservation reservation){
        reservationRepository.save(reservation);

        String response = "redirect:/showNewReservationForm2/" + String.valueOf(reservation.getId());

        //redirect to path of PATHtoMETHOD, not html file
        //return "reservations2";
        return response;

    }

    public List<Yacht> findAvailableYachts(LocalDate dateStart, LocalDate dateEnd){

        List<Yacht> bookedYachts = yachtsBookedInDate(dateStart,  dateEnd);
        List<Yacht> allOfYachts = yachtRepository.findAll().stream().collect(Collectors.toList());
        System.out.println("booked yachts " + bookedYachts.toArray().toString());

        allOfYachts.removeAll(bookedYachts);

        return allOfYachts;
    }


    //SHOW DATE SELECT FORM
    @RequestMapping( method = RequestMethod.GET, path = "/showNewReservationForm2/{id}")
    public String selectReservationDateAddCustomerAndYacht(@ModelAttribute("reservation") Reservation reservation,
                                                  @PathVariable(value = "id") int id,
                                                  Model model){

        List<Yacht> emptyAvailable = new ArrayList<>();
        emptyAvailable.clear();
        model.addAttribute("availableYachts", emptyAvailable);

         reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid reservation Id:" + id));
        LocalDate dateSt = reservation.getReservationFrom();
        LocalDate dateE = reservation.getReservationTo();
        System.out.println(dateSt + " " + dateE);

        model.addAttribute("availableYachts", findAvailableYachts(dateSt, dateE));
        model.addAttribute("customers", customerRepository.findAll());
        model.addAttribute("reservations", reservationRepository.findAll());
        model.addAttribute("reservation", reservation);

        logger.warn("Date select form shown and will render available yachts view");



        return "reservation_yacht_select";
    }

    @RequestMapping(method = RequestMethod.POST, path = "/showNewReservationForm22")
    public String selectReservationDateProcessing(@ModelAttribute("reservation") Reservation reservation){

        reservationRepository.save(reservation);


        //redirect to path of showAllYachtList, not html file
        return "reservations2";
    }



    //DELETE RESERVATION ON ALL YACHT LIST
    @RequestMapping(method = RequestMethod.GET, path = "/deleteReservation/{reservationId}")//reservationId not id!!!!!!!!!
    public String deleteReservation(@PathVariable(value = "reservationId") int id){
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid yacht Id:" + id));

        reservationRepository.deleteById(reservation.getId());

        return "redirect:/showAllReservationsList";
    }

    //SHOW RESERVATION EDIT FORM
    @RequestMapping(method = RequestMethod.GET, path = "/updateReservation/{reservationId}")//yachtId not id!!!!!!!!!
    public String updateReservation(@PathVariable(value = "reservationId") int id, Model model){
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid reservation Id:" + id));

        model.addAttribute("reservation", reservation);
        model.addAttribute("yachts", yachtRepository.findAll());
        model.addAttribute("customers", customerRepository.findAll());

        return "reservations2";
    }

    @RequestMapping(method = RequestMethod.POST, path = "/saveReservation")
    public String saveReservation(@ModelAttribute("reservation") Reservation reservation){

        reservationRepository.save(reservation);


        //redirect to path of showAllYachtList, not html file
        return "redirect:/showAllReservationsList";
    }

    @RequestMapping(method = RequestMethod.GET, path = "/addReservationProperties")
    public String saveReservation1(@ModelAttribute("reservation") Reservation reservation, Model model){

        System.out.println(reservation.getReservationFrom() + " " + reservation.getReservationTo());

        System.out.println(confirmationMail(reservation));



        reservationRepository.save(reservation);


        //redirect to path of showAllYachtList, not html file
        return "redirect:/showAllReservationsList";
    }


    public List<Reservation> reservationsInDate(LocalDate selectedDateStart, LocalDate selectedDateEnd) {

        List<Reservation> listedReservations = reservationRepository.findAll().stream().collect(Collectors.toList());

        List<Reservation> foundList = new ArrayList<>();


        for(Reservation r: listedReservations){
            if(((r.getReservationFrom().isAfter(selectedDateStart)) || (r.getReservationFrom().isEqual(selectedDateStart)))
                            &&
                            ((r.getReservationTo().isBefore(selectedDateEnd)) || (r.getReservationTo().isEqual(selectedDateEnd))) ) {

                foundList.add(r);
            }
        }
        if(foundList.isEmpty())
            logger.warn("Reservations in date -> foundList is empty");

        return foundList;

    }

    public List<Reservation> reservationsInDateDebugging(LocalDate selectedDateStart, LocalDate selectedDateEnd) {

        System.out.println("reservations in date " + selectedDateStart.toString()  + " " + selectedDateEnd.toString());
        List<Reservation> listedReservations = reservationRepository.findAll().stream().collect(Collectors.toList());

        List<Reservation> foundList = new ArrayList<>();


        for(Reservation r: listedReservations){
            if(((r.getReservationFrom().isAfter(selectedDateStart)) || (r.getReservationFrom().isEqual(selectedDateStart)))
                    &&
                    ((r.getReservationTo().isBefore(selectedDateEnd)) || (r.getReservationTo().isEqual(selectedDateEnd))) ) {


                System.out.println(((r.getReservationFrom().isAfter(selectedDateStart)) || (r.getReservationFrom().isEqual(selectedDateStart)) && ((r.getReservationTo().isBefore(selectedDateEnd)) || (r.getReservationTo().isEqual(selectedDateEnd))) ));
                System.out.println(((r.getReservationFrom().isAfter(selectedDateStart)) + " " + (r.getReservationFrom().isEqual(selectedDateStart)) + " " + ((r.getReservationTo().isBefore(selectedDateEnd)) + " " + (r.getReservationTo().isEqual(selectedDateEnd))) ));

                foundList.add(r);
                System.out.println(r.getReservationFrom() + " " + r.getReservationTo() + " are into range " + selectedDateStart + " " + selectedDateEnd);
                System.out.println(r.getReservationFrom() + " - " + r.getReservationTo() + "\n" +
                        "get res from is after selected date start " + r.getReservationFrom().isAfter(selectedDateStart) +
                        "\n OR get res from is equal selected date start " + r.getReservationFrom().isEqual(selectedDateStart) +
                        "\n get res to is before selected date end " + r.getReservationTo().isBefore(selectedDateEnd) +
                        "\n get res to is equal selected date end " +    r.getReservationTo().isEqual(selectedDateEnd) + "\n\n");
            }

        }
        if(foundList.isEmpty())
            logger.warn("Reservations in date -> foundList is empty");


        System.out.println("foundlist below" + foundList.toString());

        for(Reservation l:  foundList){
            System.out.println(l.getId() + " id, customer id: " + l.getReservedCustomerId() + " dates " + l.getReservationFrom() + " " + l.getReservationTo() );
        }

        return foundList;

    }


    public List<Yacht> yachtsBookedInDate(LocalDate dateStart, LocalDate dateEnd){
        List<Reservation> reservationsListInDate = reservationsInDate(dateStart, dateEnd);
        List<Yacht> bookedYachts = new ArrayList<>();


        //loop that prints reservation params
        //for(Reservation r:reservationsListInDate){
        //    System.out.println(r.getId() + "res id, res from " + r.getReservationFrom() + ", res to " + r.getReservationTo() +  ", yachtId " + r.getReservedYachtId());
        //}
        if(reservationsListInDate.isEmpty()){
            logger.warn("Reservations list in selected date is empty. Cannot find reserved yachts");

        }   else {


            for (Reservation r : reservationsListInDate) {
                Yacht found = yachtRepository.findById(r.getReservedYachtId())
                        .orElseThrow(() -> new IllegalArgumentException("Invalid yacht reserved Id: " + r.getReservedYachtId() + " reservationId " + r.getId()));

                bookedYachts.add(found);
            }
        }

        return bookedYachts;
    }


    //@ModelAttribute("reservationsInDate")
    public List<Reservation> reservationsInDate2(Reservation reservation, String selectedDateStart, String selectedDateEnd) {
        List<Reservation> listedReservations = reservationRepository.findAll().stream().collect(Collectors.toList());
        List<Reservation> foundList = new ArrayList<>();

        for (Reservation r : listedReservations) {
            System.out.println(r.getReservationFrom() + " " + r.getReservationTo());

        }

        return listedReservations;

    }



    /*
    @ModelAttribute("listedYacht")
    public List<Yacht> listedYacht(Yacht yacht, Model model){
        List<Yacht> listYacht = yachtRepository.findAll().stream().collect(Collectors.toList());

        return listYacht;
    }

    @ModelAttribute("listedCustomer")
    public List<Customer> listedCustomer(Customer customer, Model model){
        List<Customer> listCustomer = customerRepository.findAll().stream().collect(Collectors.toList());

        return listCustomer;
    }

    //SHOW ALL YACHT LIST
    @ModelAttribute("listedYachtRes")
    public List<Yacht> listedYachtRes(Yacht yacht, Model model){
        List<Yacht> list = yachtRepository.findAll().stream().collect(Collectors.toList());

        return list;
    }

    */

    @RequestMapping(method = RequestMethod.GET, path = "/home")
    public String showAllReservationsOnly(){


        return "home";
    }

    public String confirmationMail(Reservation reservation){

        int customerId = reservation.getReservedCustomerId();
        int yachtId = reservation.getReservedYachtId();

        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid customer Id:" + customerId));
        Yacht yacht = yachtRepository.findById(yachtId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid yacht Id:" + yachtId));

        StringBuilder confirmationMailTemplate = new StringBuilder();

        confirmationMailTemplate.append("Witaj, " + customer.getFirstName() + "!\n");
        confirmationMailTemplate.append("Z przyjemnością informuję o utworzeniu rezerwacji jachtu ").append(yacht.getName()).append(" ");
        confirmationMailTemplate.append(" w terminie ").append(reservation.getReservationFrom().toString()).append(" - ").append(reservation.getReservationTo().toString()).append(".\n");
        confirmationMailTemplate.append("Płatność za czarter odbywa się przelewem na rachunek \n17 1140 2004 0000 4667 9066\nCena to ////////TUTAJ PODAJ CENĘ CZARTERU\n");
        confirmationMailTemplate.append("Przelew należy wykonać w przeciągu siedmiu dni od otrzymania tej wiadomości, przeciwnie rezerwacja zostaje anulowana.\n\nZ żeglarskimi pozdrowieniami,\nPremiumYachtCharterPolska");
        confirmationMailTemplate.append("\n\n\nWiadomość tę nadaj na adres ").append(customer.getEmail());

        return confirmationMailTemplate.toString();
    }





}





