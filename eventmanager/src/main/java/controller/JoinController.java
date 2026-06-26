package com.example.eventmanager.controller;

import com.example.eventmanager.model.Participant;
import com.example.eventmanager.service.EmailService;
import com.example.eventmanager.service.EventService;
import com.example.eventmanager.service.ParticipantService;
import com.example.eventmanager.service.RegistrationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/join")
@RequiredArgsConstructor
public class JoinController {

    private final EventService eventService;
    private final ParticipantService participantService;
    private final RegistrationService registrationService;
    private final EmailService emailService;

    @GetMapping("/{token}")
    public String showJoinForm(@PathVariable String token, Model model) {
        return eventService.findByJoinToken(token)
                .map(event -> {
                    model.addAttribute("event", event);
                    model.addAttribute("participant", new Participant());
                    return "join/form";
                })
                .orElse("redirect:/events");
    }

    @PostMapping("/{token}")
    public String join(@PathVariable String token,
                       @Valid @ModelAttribute Participant participant,
                       BindingResult result,
                       Model model,
                       RedirectAttributes redirectAttributes) {

        return eventService.findByJoinToken(token)
                .map(event -> {
                    if (result.hasErrors()) {
                        model.addAttribute("event", event);
                        return "join/form";
                    }

                    Participant saved = participantService.save(participant);
                    registrationService.save(event.getId(), saved.getId());

                    String eventDate = event.getStartDate().toString();
                    String location = event.getLocation() != null ? event.getLocation().getName() : "TBD";
                    emailService.sendRegistrationConfirmation(
                            saved.getEmail(),
                            event.getName(),
                            eventDate,
                            location
                    );

                    redirectAttributes.addFlashAttribute("success",
                            "Te-ai înregistrat cu succes la evenimentul: " + event.getName());
                    return "redirect:/join/" + token + "/success";
                })
                .orElse("redirect:/events");
    }

    @GetMapping("/{token}/success")
    public String showSuccess(@PathVariable String token, Model model) {
        return eventService.findByJoinToken(token)
                .map(event -> {
                    model.addAttribute("event", event);
                    return "join/success";
                })
                .orElse("redirect:/events");
    }
}