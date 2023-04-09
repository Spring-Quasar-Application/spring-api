package com.spring_api.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = StudentControllerTest.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class StudentControllerTest {

    @Autowired
    private WebApplicationContext context;

    @Autowired
    FilterChainProxy securityFilterChain;

    private MockMvc mockMvc;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity(securityFilterChain))
                .build();
    }

   /*  @Test
    public void submissionsWhenUserIsSpeakerReturnsListOfSubmissions() throws Exception {
        ConferenceUser joe = new ConferenceUser();
        joe.setUsername("Joe");
        joe.setSubmissions(List.of("Getting Started with Spring Authorization Server"));
        joe.setSpeaker(true);
        this.mockMvc.perform(get("/submissions").with(user(new ConferenceUserService.ConferenceUserDetails(joe))))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$", hasSize(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0]", is("Getting Started with Spring Authorization Server")));
    } */

/*     @Test
    public void submissionsWhenUserIsNotSpeakerReturns403() throws Exception {
        this.mockMvc.perform(get("/students").with(csrf()))
                .andExpect(status().isOk());
    }


    @Test
    public void getStudentsWithAdminUser() throws Exception {
        this.mockMvc.perform(get("/students")
                        .with(csrf())
                        .with(user("admin").roles("ADMIN"))
                )
                .andExpect(status().isOk());
    }

    @Test
    public void postAboutWhenUnauthenticatedUserReturns401() throws Exception {
        this.mockMvc.perform(post("/about")
                        .content("Join us online September 11-12!")
                        .with(csrf())
                )
                .andExpect(status().isUnauthorized());
    } */

    @Test
    public void postAboutWithoutCsrfThenReturns403() throws Exception {
        this.mockMvc.perform(post("/students"))
                .andExpect(status().isForbidden());
    }

    @Test
    public void getAboutWithoutCsrfThenReturns403() throws Exception {
        this.mockMvc.perform(get("/students"))
                .andExpect(status().isForbidden());
    }
}
