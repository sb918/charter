

package com.example.charter.controller;

import com.example.charter.model.Customer;
import com.example.charter.model.Reservation;
import com.example.charter.model.Yacht;
import com.example.charter.model.YachtRepository;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.boot.autoconfigure.data.web.SpringDataWebProperties;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.FluentQuery;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

//import java.util.logging.Logger;

@Controller
class YachtController{
    private static final Logger logger = LoggerFactory.getLogger(YachtController.class);
    private final YachtRepository yachtRepository;

    YachtController(final YachtRepository yachtRepository){
        this.yachtRepository = yachtRepository;
    }



    @RequestMapping(method = RequestMethod.GET, path = "/yachts", params = {"!page", "!size", "!sort"})
    ResponseEntity<List<Yacht>>readAllYachts(){
        logger.warn("Exposing all the yachts");

        return ResponseEntity.ok(yachtRepository.findAll());

    }



    @RequestMapping(method = RequestMethod.GET, path = "/yachts")
    ResponseEntity<?> readAllYachts(Pageable page){
        logger.info("Customised pageable");
        return ResponseEntity.ok(yachtRepository.findAll(page));
    }

    @RequestMapping(method = RequestMethod.POST, path = "/saveYacht")
    public String saveYacht(@ModelAttribute("yacht") Yacht yacht){

        yachtRepository.save(yacht);

        //redirect to path of showAllYachtList, not html file
        return "redirect:/showAllYachtsList";
    }

    //SHOW ALL YACHT LIST
    @RequestMapping( method = RequestMethod.GET, path = "/showAllYachtsList")
    public String showAllYachtsList(Yacht yacht, Model model){
        yachtRepository.findAll().stream().collect(Collectors.toList());

        model.addAttribute("yachts", yachtRepository.findAll());
        logger.warn("Exposing all the Yachts List");

        return "view_yachts";
    }

    @ModelAttribute("listedYacht")
    public List<Yacht> listedYacht(Yacht yacht, Model model){
        List<Yacht> list = yachtRepository.findAll().stream().collect(Collectors.toList());

        return list;
    }

    //DELETE YACHT ON ALL YACHT LIST
    @RequestMapping(method = RequestMethod.GET, path = "/deleteYacht/{yachtId}")//yachtId not id!!!!!!!!!
    public String deleteYacht(@PathVariable(value = "yachtId") int id){
        Yacht yacht = yachtRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid yacht Id:" + id));

        yachtRepository.deleteById(yacht.getId());

        return "redirect:/showAllYachtsList";
    }

    //SHOW CUSTOMER EDIT FORM
    @RequestMapping(method = RequestMethod.GET, path = "/updateYacht/{yachtId}")//yachtId not id!!!!!!!!!
    public String updateYacht(@PathVariable(value = "yachtId") int id, Model model){
        Yacht yacht = yachtRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid yacht Id:" + id));

        //model.addAttribute("yacht", yachtRepository.findById(id));
        model.addAttribute("yacht", yacht);



        return "update_yacht";
    }

    //SHOW YACHT ADD FORM
    @RequestMapping(method = RequestMethod.GET, path = "/addYacht")
    public String addYacht( Model model){
        Yacht yacht = new Yacht();
        model.addAttribute("yacht", yacht);

        return "add_yacht";
    }

    //SHOW reservation/YACHT DETAILS
    @RequestMapping(method = RequestMethod.GET, path = "/yachtDetails/{yachtId}")//yachtId not id!!!!!!!!!
    public String yachtDetails(@PathVariable(value = "yachtId") int id, Model model){
        Yacht yacht = yachtRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid yacht Id:" + id));

        //model.addAttribute("yacht", yachtRepository.findById(id));
        model.addAttribute("yacht", yacht);

        return "reservation_yacht_details";
    }







}
