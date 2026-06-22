package com.example.eventmanager.controller;

import com.example.eventmanager.model.EventGroup;
import com.example.eventmanager.service.EventGroupService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/event-groups")
@RequiredArgsConstructor
public class EventGroupController {

    private final EventGroupService eventGroupService;

    @GetMapping
    public String findAll(Model model) {
        model.addAttribute("eventGroups", eventGroupService.findAll());
        return "event-groups/list";
    }

    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("eventGroup", new EventGroup());
        return "event-groups/form";
    }

    @PostMapping
    public String create(@Valid @ModelAttribute EventGroup eventGroup, BindingResult result) {
        if (result.hasErrors()) return "event-groups/form";
        eventGroupService.save(eventGroup);
        return "redirect:/event-groups";
    }

    @GetMapping("/{id}/edit")
    public String showEditForm(@PathVariable Long id, Model model) {
        model.addAttribute("eventGroup", eventGroupService.findById(id));
        return "event-groups/form";
    }

    @PostMapping("/{id}")
    public String update(@PathVariable Long id, @Valid @ModelAttribute EventGroup eventGroup, BindingResult result) {
        if (result.hasErrors()) return "event-groups/form";
        eventGroupService.update(id, eventGroup);
        return "redirect:/event-groups";
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id) {
        eventGroupService.delete(id);
        return "redirect:/event-groups";
    }
}