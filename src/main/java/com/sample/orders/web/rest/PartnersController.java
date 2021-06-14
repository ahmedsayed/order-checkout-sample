package com.sample.orders.web.rest;

import com.sample.common.domains.Partner;
import com.sample.orders.unit.services.PartnersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/partners")
public class PartnersController {

    private final PartnersService partnersService;

    @Autowired
    public PartnersController(PartnersService partnersService) {
        this.partnersService = partnersService;
    }

    @GetMapping("/all")
    public ResponseEntity<List<Partner>> findAll() {
        return ResponseEntity.ok().body(partnersService.findPartners());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Partner> findItem(@PathVariable(value = "id") long id) {
        return ResponseEntity.ok().body(partnersService.findPartner(id));
    }
}
