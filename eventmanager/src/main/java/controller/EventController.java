package com.example.eventmanager.controller;

import com.example.eventmanager.model.Event;
import com.example.eventmanager.service.CategoryService;
import com.example.eventmanager.service.EventGroupService;
import com.example.eventmanager.service.EventService;
import com.example.eventmanager.service.LocationService;
import com.example.eventmanager.service.ParticipantService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.io.PrintWriter;

@Controller
@RequestMapping("/events")
@RequiredArgsConstructor
public class EventController {

    private final EventService eventService;
    private final LocationService locationService;
    private final CategoryService categoryService;
    private final EventGroupService eventGroupService;
    private final ParticipantService participantService;

    @GetMapping
    public String findAll(Model model,
                          @RequestParam(defaultValue = "0") int page,
                          @RequestParam(defaultValue = "10") int size,
                          @RequestParam(defaultValue = "startDate") String sortBy,
                          @RequestParam(defaultValue = "asc") String direction,
                          @RequestParam(defaultValue = "") String search) {
        Sort sort = direction.equals("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        if (search.isEmpty()) {
            model.addAttribute("events", eventService.findAll(PageRequest.of(page, size, sort)));
        } else {
            model.addAttribute("events", eventService.search(search, PageRequest.of(page, size, sort)));
        }
        model.addAttribute("sortBy", sortBy);
        model.addAttribute("direction", direction);
        model.addAttribute("search", search);
        return "events/list";
    }

    @GetMapping("/{id}")
    public String findById(@PathVariable Long id, Model model) {
        model.addAttribute("event", eventService.findById(id));
        model.addAttribute("participants", participantService.findAll(PageRequest.of(0, 1000, Sort.by("lastName").ascending())).getContent());
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

    @GetMapping("/{id}/export")
    @org.springframework.security.access.annotation.Secured("ROLE_ADMIN")
    public void exportParticipants(@PathVariable Long id, HttpServletResponse response) throws IOException {
        Event event = eventService.findById(id);
        response.setContentType("text/csv");
        response.setHeader("Content-Disposition", "attachment; filename=\"participants-" + id + ".csv\"");

        PrintWriter writer = response.getWriter();
        writer.println("Nume,Prenume,Email,Telefon,Data inregistrarii,Status");

        event.getRegistrations().forEach(reg -> {
            writer.println(
                    reg.getParticipant().getLastName() + "," +
                            reg.getParticipant().getFirstName() + "," +
                            reg.getParticipant().getEmail() + "," +
                            (reg.getParticipant().getPhone() != null ? reg.getParticipant().getPhone() : "") + "," +
                            reg.getRegistrationDate() + "," +
                            reg.getStatus()
            );
        });
        writer.flush();
    }
}