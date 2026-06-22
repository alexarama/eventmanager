package com.example.eventmanager.controller;

import com.example.eventmanager.model.Location;
import com.example.eventmanager.service.LocationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/locations")
@RequiredArgsConstructor
public class LocationController {

    private final LocationService locationService;

    @GetMapping
    public String findAll(Model model) {
        model.addAttribute("locations", locationService.findAll());
        return "locations/list";
    }

    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("location", new Location());
        return "locations/form";
    }

    @PostMapping
    public String create(@Valid @ModelAttribute Location location, BindingResult result) {
        if (result.hasErrors()) return "locations/form";
        locationService.save(location);
        return "redirect:/locations";
    }

    @GetMapping("/{id}/edit")
    public String showEditForm(@PathVariable Long id, Model model) {
        model.addAttribute("location", locationService.findById(id));
        return "locations/form";
    }

    @PostMapping("/{id}")
    public String update(@PathVariable Long id, @Valid @ModelAttribute Location location, BindingResult result) {
        if (result.hasErrors()) return "locations/form";
        locationService.update(id, location);
        return "redirect:/locations";
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id) {
        locationService.delete(id);
        return "redirect:/locations";
    }
}