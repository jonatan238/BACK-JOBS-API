package com.uap.proiv.jobs.service.impl;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.InjectMocks;

import java.util.ArrayList;
import org.junit.jupiter.api.BeforeEach;


import com.uap.proiv.jobs.dto.AssignedResponse;
import com.uap.proiv.jobs.dto.Job;
import com.uap.proiv.jobs.dto.User;
import com.uap.proiv.jobs.dto.UserApiResponse;
import com.uap.proiv.jobs.dto.UserJobAssigned;
import com.uap.proiv.jobs.service.AssignedService;
import com.uap.proiv.jobs.service.JobService;
import com.uap.proiv.jobs.service.UserService;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Spy;

@ExtendWith(MockitoExtension.class)

public class UserJobAssignedServiceImplSpyTest {

    @Mock
    JobService jobService;
    
    @Mock
    UserService userService;

    @Spy
    AssignedService assignedService;

    @InjectMocks 
    UserJobAssignedServiceImpl serviceImpl;

    List<Job> jobs;
    List<User> users;
    List<AssignedResponse> assignedResponse;
    UserApiResponse userApiResponse;

    @BeforeEach
    void setup(){
         jobs= new ArrayList<>();
        Job job1 = new Job();
        job1.setId(1);
        job1.setName("Developer");
        job1.setSalary (5000);
        job1.setHours(2000);
        job1.setResources(3);
        jobs.add(job1);

        Job job2 = new Job();
        job2.setId(2);
        job2.setName("Designer");
        job2.setSalary(4500);
        job2.setHours(1500);
        job2.setResources(1);
        jobs.add(job2);

        users = new ArrayList<>();
        User user1 = new User();
        user1.setId(1);
        user1.setEmail("user1@example.com");
        user1.setAvatar("null");
        user1.setFirstName("Juan");
        user1.setLastName("Garcia");
        users.add(user1);

         User user2 = new User();
        user2.setId(2);
        user2.setEmail("user2@example.com");
        user2.setAvatar("null");
        user2.setFirstName("Diana");
        user2.setLastName("Diaz");
        users.add(user2);

        userApiResponse = new UserApiResponse();
        userApiResponse.setPage(1);
        userApiResponse.setPerPage(2);
        userApiResponse.setTotal(2);
        userApiResponse.setTotalPages(1);
        userApiResponse.setData(users);

        assignedResponse = new ArrayList<>();
        assignedResponse.add(new AssignedResponse(1, 2));
        assignedResponse.add(new AssignedResponse(2, 1));

    }

    @Test
    @DisplayName("Verifica que el método getAssignedJobsByUserId devuelva la lista de trabajos asignados para un usuario específico")
    void assign_succesOnePage() {
        when(jobService.getAllJobs()).thenReturn(jobs);
        when(userService.search(1)).thenReturn(userApiResponse);
        doReturn(assignedResponse).when(assignedService).create(any(), any());

        List<UserJobAssigned> result = serviceImpl.assign();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(2, result.get(0).getUsers().get(0).getId());

        verify(jobService, times(1)).getAllJobs();
        verify(userService, times(1)).search(1);
        verify(assignedService, times(1)).create(any(), any());  
    }
}
