package com.example.eventmanager.integration;

import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.example.eventmanager.model.User;
import com.example.eventmanager.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@ActiveProfiles("test")
class EventControllerIntegrationTest {

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(SecurityMockMvcConfigurers.springSecurity())
                .build();

        if (userRepository.findByUsername("testadmin").isEmpty()) {
            User admin = new User();
            admin.setUsername("testadmin");
            admin.setPassword(passwordEncoder.encode("admin123"));
            admin.setRole(User.Role.ADMIN);
            admin.setEnabled(true);
            userRepository.save(admin);
        }
    }

    @Test
    void eventsPage_unauthenticated_redirectsToLogin() throws Exception {
        mockMvc.perform(get("/events"))
                .andExpect(status().is3xxRedirection());
    }

    @Test
    @WithMockUser(username = "testadmin", roles = "ADMIN")
    void eventsPage_authenticated_returnsOk() throws Exception {
        mockMvc.perform(get("/events"))
                .andExpect(status().isOk())
                .andExpect(view().name("events/list"));
    }

    @Test
    @WithMockUser(username = "testadmin", roles = "ADMIN")
    void newEventPage_admin_returnsOk() throws Exception {
        mockMvc.perform(get("/events/new"))
                .andExpect(status().isOk())
                .andExpect(view().name("events/form"));
    }

    @Test
    @WithMockUser(username = "testuser", roles = "USER")
    void newEventPage_user_returnsForbidden() throws Exception {
        mockMvc.perform(get("/events/new"))
                .andExpect(status().isForbidden());
    }

    @Test
    void loginPage_unauthenticated_returnsOk() throws Exception {
        mockMvc.perform(get("/login"))
                .andExpect(status().isOk());
    }
}