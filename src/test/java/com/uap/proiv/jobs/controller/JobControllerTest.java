package com.uap.proiv.jobs.controller;

import com.uap.proiv.jobs.service.JobService;
import com.uap.proiv.jobs.service.UserJobAssignedService;
import com.uap.proiv.jobs.service.UserService;
import com.uap.proiv.jobs.dto.AssignRequest;
import com.uap.proiv.jobs.dto.UserApiResponse;

import com.uap.proiv.jobs.dto.User;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import com.fasterxml.jackson.databind.ObjectMapper;
@ExtendWith(MockitoExtension.class)

public class JobControllerTest{

    @Mock
    UserService userService;

    @Mock
    JobService jobService;

    @Mock
    UserJobAssignedService userJobAssignedService;

    @InjectMocks
    JobController jobController;

    private MockMvc mockMvc;    

    private UserApiResponse userApiResponse;
    private List<User> users;
    private final ObjectMapper objectMapper = new ObjectMapper();
    

    @BeforeEach
    void setup() {
        
        mockMvc = MockMvcBuilders.standaloneSetup(jobController).build();
                


        users = new ArrayList<>();
        User user1 = new User();
        user1.setId(1);
        user1.setEmail("ejemplo@as.com");
        user1.setAvatar("null");
        user1.setFirstName("juan");
        user1.setLastName("Garcia");
        users.add(user1);

        User user2 = new User();
        user2.setId(2);
        user2.setEmail("ejemplo2@as.com");
        user2.setAvatar("null");
        user2.setFirstName("diane");
        user2.setLastName("perez");
        users.add(user2);

        


        userApiResponse = new UserApiResponse();
        userApiResponse.setPage(1);
        userApiResponse.setPerPage(2);
        userApiResponse.setTotal(2);
        userApiResponse.setTotalPages(1);
        userApiResponse.setData(users);

    }

        @Test 
        @DisplayName("GET api /api/job/users/{page} retorna usuarios")
        

        void getUsers_success() throws Exception {  
        when(userService.search(1))
        .thenReturn(userApiResponse)
        .thenThrow(new RuntimeException("MSG"))
            .thenReturn(userApiResponse);

        mockMvc.perform(get("/api/job/users/1")).andExpect((status().isOk()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data.length()").value(2))
                .andExpect(jsonPath("$.page").value(1))
                .andExpect(jsonPath("$.total").value(2));
        }       
    
    @DisplayName("GET api /api/job/users/{page} - excepcion retornada por el service")
    void getUsers_excepciton() throws Exception {
        when(userService.search(2)).thenThrow(new RuntimeException("service error"));

        mockMvc.perform(get("/api/job/users/2"))
                .andExpect(status().is5xxServerError())
                .andExpect(content().string("Service Error"));
            
    }
    @Test
    @DisplayName("POST /api/job/assign")    
    void postAssign_success() throws Exception {
        AssignRequest assignRequest = new AssignRequest();
        assignRequest.setRequestNumber(123);
        assignRequest.setClientName("Name");

        mockMvc.perform(post("/api/job/assign")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(assignRequest)))
                .andExpect(status().isOk());
    }
     
}   