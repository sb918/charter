

package com.example.charter.controller;

import com.example.charter.model.Reservation;
import com.example.charter.model.Customer;
import com.example.charter.model.CustomerRepository;
import com.example.charter.model.Yacht;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.boot.autoconfigure.data.web.SpringDataWebProperties;
import org.springframework.data.domain.Pageable;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;



@Controller
public class CustomerController{
    private static final Logger logger = LoggerFactory.getLogger(CustomerController.class);
    private final CustomerRepository customerRepository;

    CustomerController(final CustomerRepository customerRepository){
        this.customerRepository = customerRepository;
    }


    @RequestMapping(method = RequestMethod.GET, path = "/customers", params = {"!page", "!size", "!sort"})
    ResponseEntity<List<Customer>>readAllCustomers(){
        logger.warn("Exposing all the Customers");
        return ResponseEntity.ok(customerRepository.findAll());
    }

    @RequestMapping(method = RequestMethod.GET, path = "/customers")
    ResponseEntity<?> readAllCustomers(Pageable page){
        logger.info("Customised pageable");
        return ResponseEntity.ok(customerRepository.findAll(page));
    }


    @RequestMapping(method = RequestMethod.POST, path = "/saveCustomer")
    public String saveCustomer(@ModelAttribute("customer") Customer customer){

        customerRepository.save(customer);

        return "redirect:/showAllCustomersList";
    }

    //return view_customers shows string only, not a html
    //  <form action="#" th:action="@{/showAllCustomers}" th:object="${customer}" method="post"> customers to /showAllCustomers
    //compiling, running but still string only
    //changed @RestController to Controller, postman still working, showAllCustomers mapping leads to error 500
    //well, it's thymeleaf's error, fields were out of scope


    //SHOW ALL CUSTOMERS LIST
    @RequestMapping( method = RequestMethod.GET, path = "/showAllCustomersList")
    public String showAllCustomersList(Customer customer, Model model){
        customerRepository.findAll().stream().collect(Collectors.toList());
        model.addAttribute("customers", customerRepository.findAll());
        logger.warn("Exposing all the Customers");

        return "view_customers";
    }

    //DELETE CUSTOMER ON ALL CUSTOMERS LIST
    @RequestMapping(method = RequestMethod.GET, path = "/deleteCustomer/{customerId}")//customerId not id!!!!!!!!!
    public String deleteCustomer(@PathVariable(value = "customerId") int id){
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid customer Id:" + id));

        customerRepository.deleteById(customer.getCustomerId());

        return "redirect:/showAllCustomersList";
    }

    //SHOW CUSTOMER ADD FORM
    @RequestMapping(method = RequestMethod.GET, path = "/addCustomer")
    public String addCustomer( Model model){
        Customer customer = new Customer();
        model.addAttribute("customer", customer);

        return "add_customer";
    }





    //SHOW CUSTOMER EDIT FORM
    @RequestMapping(method = RequestMethod.GET, path = "/updateCustomer/{customerId}")//customerId not id!!!!!!!!!
    public String updateCustomer(@PathVariable(value = "customerId") int id, Model model){
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid customer Id:" + id));

        model.addAttribute("customer", customerRepository.findById(id));
        model.addAttribute("customer", customer);

        return "update_customer";
    }

    //FIND CUSTOMER EDIT FORM
    @RequestMapping(method = RequestMethod.GET, path = "/findCustomer/{customerId}")//yachtId not id!!!!!!!!!
    public String findCustomer(@PathVariable(value = "customerId") int id, Model model){
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid yacht Id:" + id));

        //model.addAttribute("yacht", yachtRepository.findById(id));
        model.addAttribute("customer", customer);



        return "reservations1";
    }

    //SHOW reservation/CUSTOMER DETAILS
    @RequestMapping(method = RequestMethod.GET, path = "/customerDetails/{customerId}")//customerId not id!!!!!!!!!
    public String customerDetails(@PathVariable(value = "customerId") int id, Model model){
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid customer Id:" + id));

        //model.addAttribute("customer", customerRepository.findById(id));
        model.addAttribute("customer", customer);


        return "reservation_customer_details";
    }





}
