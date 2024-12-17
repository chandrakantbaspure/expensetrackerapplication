package com.nitor.expensetrackerapplication.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nitor.expensetrackerapplication.dto.ExpenseDto;
import com.nitor.expensetrackerapplication.repo.ExpenseRepository;
import com.nitor.expensetrackerapplication.service.ExpenseService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringJUnitConfig
@ExtendWith(MockitoExtension.class)
class ExpenseControllerTest {
    @InjectMocks
    private ExpenseController expenseController;
    @Mock
    private ExpenseService expenseService;
    @Mock
    private ExpenseRepository expenseRepository;
    private ModelMapper mapper;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        expenseController = new ExpenseController(expenseService, mapper);
        expenseRepository = mock(ExpenseRepository.class);
        mockMvc = MockMvcBuilders.standaloneSetup(expenseController).build();

    }


    @Test
    void getAllExpenses() throws Exception {
        String username = "testUser";
        ExpenseDto expenseDto = new ExpenseDto();
        expenseDto.setAmount(100.0);
        expenseDto.setDate(LocalDate.of(2023, 7, 10));
        expenseDto.setCategory("Food");
        expenseDto.setTitle("Test Expense");
        expenseDto.setUserName("testUser");
        List<ExpenseDto> list = new ArrayList<>();
        when(expenseService.getAllExpenses("testUser")).thenReturn(list);
        ResultActions resultActions = mockMvc.perform(get("/api/expenses/all/{username}", username)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        resultActions.andExpect(content().json(asJsonString(list)));

        verify(expenseService).getAllExpenses("testUser");

    }

    @Test
    void getExpensesById() throws Exception {
        Long id = 1L;
        ExpenseDto expenseDto = new ExpenseDto();
        expenseDto.setAmount(100.0);
        expenseDto.setDate(LocalDate.of(2023, 7, 10));
        expenseDto.setCategory("Food");
        expenseDto.setTitle("Test Expense");
        expenseDto.setUserName("testUser");
        mockMvc.perform(get("/api/expenses/{expenseId}", id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void deleteExpense() throws Exception {
        mockMvc.perform(delete("/api/expenses/{id}", 1L)).andExpect(status().isOk());
    }

    @Test
    void getExpensesForUser() throws Exception {
        String username = "testUser";
        ExpenseDto expenseDto = new ExpenseDto();
        expenseDto.setAmount(100.0);
        expenseDto.setDate(LocalDate.of(2023, 7, 10));
        expenseDto.setCategory("Food");
        expenseDto.setTitle("Test Expense");
        expenseDto.setUserName("testUser");
        List<ExpenseDto> list = new ArrayList<>();
        when(expenseService.getExpensesForUser("testUser")).thenReturn(list);
        ResultActions resultActions = mockMvc.perform(get("/api/expenses/user/{username}", username)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        resultActions.andExpect(content().json(asJsonString(list)));
    }

    private static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}