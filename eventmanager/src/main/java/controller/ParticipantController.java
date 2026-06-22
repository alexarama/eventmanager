package com.example.eventmanager.controller;

import com.example.eventmanager.model.Participant;
import com.example.eventmanager.service.ParticipantService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/participants")
@RequiredArgsConstructor
public class ParticipantController {

    private final ParticipantService participantService;

    @GetMapping
    public String findAll(Model model,
                          @RequestParam(defaultValue = "0") int page,
                          @RequestParam(defaultValue = "10") int size,
                          @RequestParam(defaultValue = "lastName") String sortBy,
                          @RequestParam(defaultValue = "asc") String direction) {
        Sort sort = direction.equals("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        model.addAttribute("participants", participantService.findAll(PageRequest.of(page, size, sort)));
        model.addAttribute("sortBy", sortBy);
        model.addAttribute("direction", direction);
        return "participants/list";
    }

    @GetMapping("/{id}")
    public String findById(@PathVariable Long id, Model model) {
        model.addAttribute("participant", participantService.findById(id));
        return "participants/detail";
    }

    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("participant", new Participant());
        return "participants/form";
    }

    @PostMapping
    public String create(@Valid @ModelAttribute Participant participant, BindingResult result) {
        if (result.hasErrors()) return "participants/form";
        participantService.save(participant);
        return "redirect:/participants";
    }

    @GetMapping("/{id}/edit")
    public String showEditForm(@PathVariable Long id, Model model) {
        model.addAttribute("participant", participantService.findById(id));
        return "participants/form";
    }

    @PostMapping("/{id}")
    public String update(@PathVariable Long id, @Valid @ModelAttribute Participant participant,
                         BindingResult result) {
        if (result.hasErrors()) return "participants/form";
        participantService.update(id, participant);
        return "redirect:/participants";
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id) {
        participantService.delete(id);
        return "redirect:/participants";
    }
}