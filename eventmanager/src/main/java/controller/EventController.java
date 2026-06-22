package com.example.eventmanager.controller;

import com.example.eventmanager.model.Event;
import com.example.eventmanager.service.CategoryService;
import com.example.eventmanager.service.EventGroupService;
import com.example.eventmanager.service.EventService;
import com.example.eventmanager.service.LocationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/events")
@RequiredArgsConstructor
public class EventController {

    private final EventService eventService;
    private final LocationService locationService;
    private final CategoryService categoryService;
    private final EventGroupService eventGroupService;

    @GetMapping
    public String findAll(Model model,
                          @RequestParam(defaultValue = "0") int page,
                          @RequestParam(defaultValue = "10") int size,
                          @RequestParam(defaultValue = "startDate") String sortBy,
                          @RequestParam(defaultValue = "asc") String direction) {
        Sort sort = direction.equals("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        model.addAttribute("events", eventService.findAll(PageRequest.of(page, size, sort)));
        model.addAttribute("sortBy", sortBy);
        model.addAttribute("direction", direction);
        return "events/list";
    }

    @GetMapping("/{id}")
    public String findById(@PathVariable Long id, Model model) {
        model.addAttribute("event", eventService.findById(id));
        return "events/detail";
    }

    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("event", new Event());
        model.addAttribute("locations", locationService.findAll());
        model.addAttribute("categories", categoryService.findAll());
        model.addAttribute("eventGroups", eventGroupService.findAll());
        return "events/form";
    }

    @PostMapping
    public String create(@Valid @ModelAttribute Event event, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("locations", locationService.findAll());
            model.addAttribute("categories", categoryService.findAll());
            model.addAttribute("eventGroups", eventGroupService.findAll());
            return "events/form";
        }
        eventService.save(event);
        return "redirect:/events";
    }

    @GetMapping("/{id}/edit")
    public String showEditForm(@PathVariable Long id, Model model) {
        model.addAttribute("event", eventService.findById(id));
        model.addAttribute("locations", locationService.findAll());
        model.addAttribute("categories", categoryService.findAll());
        model.addAttribute("eventGroups", eventGroupService.findAll());
        return "events/form";
    }

    @PostMapping("/{id}")
    public String update(@PathVariable Long id, @Valid @ModelAttribute Event event,
                         BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("locations", locationService.findAll());
            model.addAttribute("categories", categoryService.findAll());
            model.addAttribute("eventGroups", eventGroupService.findAll());
            return "events/form";
        }
        eventService.update(id, event);
        return "redirect:/events";
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id) {
        eventService.delete(id);
        return "redirect:/events";
    }
}