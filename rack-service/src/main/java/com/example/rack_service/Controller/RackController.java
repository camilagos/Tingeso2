package com.example.rack_service.Controller;

import com.example.rack_service.Model.Reserva;
import com.example.rack_service.Service.RackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/rack")
@CrossOrigin("*")
public class RackController {

    @Autowired
    RackService rackService;

    @GetMapping("/")
    public List<Reserva> getReservas() {
        return rackService.obtenerReservas();
    }
}
