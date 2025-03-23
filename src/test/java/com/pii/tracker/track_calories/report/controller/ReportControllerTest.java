package com.pii.tracker.track_calories.report.controller;

import com.pii.tracker.track_calories.exception.GlobalExceptionHandler;
import com.pii.tracker.track_calories.report.dto.CalorieLimitResponse;
import com.pii.tracker.track_calories.report.dto.DailyReportResponse;
import com.pii.tracker.track_calories.report.dto.MealHistoryResponse;
import com.pii.tracker.track_calories.report.service.ReportService;
import com.pii.tracker.track_calories.user.exception.UserNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
@WebMvcTest(controllers = ReportController.class)
@Import(GlobalExceptionHandler.class)
public class ReportControllerTest {

    @MockitoBean
    private ReportService reportService;
    @Autowired
    private MockMvc mockMvc;

    private LocalDate date = LocalDate.now();

    @Test
    void getDailyReport_ShouldReturnDailyReport() throws Exception {
        var response = new DailyReportResponse(1500, List.of());
        when(reportService.getDailyReport(1L, date)).thenReturn(response);

        mockMvc.perform(get("/api/users/1/reports/daily")
                        .param("date", date.toString())
                        .accept("application/json"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalCalories").value(1500));

        verify(reportService, times(1)).getDailyReport(1L, date);
    }

    @Test
    void getDailyReport_ShouldUseCurrentDateIfNotProvided() throws Exception {
        var response = new DailyReportResponse(1500, List.of());
        when(reportService.getDailyReport(eq(1L), isNull())).thenReturn(response);

        mockMvc.perform(get("/api/users/1/reports/daily")
                        .accept("application/json"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalCalories").value(1500));

        verify(reportService, times(1)).getDailyReport(eq(1L), isNull());
    }

    @Test
    void getDailyReport_WhenUserNotFound_ShouldReturnNotFound() throws Exception {
        when(reportService.getDailyReport(eq(1L), eq(date)))
                .thenThrow(new UserNotFoundException("Пользователь не найден id=1"));

        mockMvc.perform(get("/api/users/1/reports/daily")
                        .param("date", date.toString())
                        .accept("application/json"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("Пользователь не найден id=1"));

        verify(reportService, times(1)).getDailyReport(eq(1L), eq(date));
    }

    @Test
    void getDailyReport_WhenInvalidDateFormat_ShouldReturnBadRequest() throws Exception {
        mockMvc.perform(get("/api/users/1/reports/daily")
                        .param("date", "invalid-date")
                        .accept("application/json"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void checkCalorieLimit_ShouldReturnTrue_WhenWithinLimit() throws Exception {
        when(reportService.checkCalorieLimit(1L, date)).thenReturn(new CalorieLimitResponse(true));

        mockMvc.perform(get("/api/users/1/reports/is-within-limit")
                        .param("date", date.toString())
                        .accept("application/json"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.isWithinLimit").value(true));

        verify(reportService, times(1)).checkCalorieLimit(1L, date);
    }

    @Test
    void checkCalorieLimit_ShouldReturnFalse_WhenExceededLimit() throws Exception {
        when(reportService.checkCalorieLimit(1L, date)).thenReturn(new CalorieLimitResponse(false));

        mockMvc.perform(get("/api/users/1/reports/is-within-limit")
                        .param("date", date.toString())
                        .accept("application/json"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.isWithinLimit").value(false));

        verify(reportService, times(1)).checkCalorieLimit(1L, date);
    }

    @Test
    void getMealHistory_ShouldReturnHistory() throws Exception {
        var historyResponse = new MealHistoryResponse(date, 1200, List.of());
        when(reportService.getMealHistory(1L, date.minusDays(7), date)).thenReturn(List.of(historyResponse));

        mockMvc.perform(get("/api/users/1/reports/history")
                        .param("start", date.minusDays(7).toString())
                        .param("end", date.toString())
                        .accept("application/json"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1))
                .andExpect(jsonPath("$[0].totalCalories").value(1200));

        verify(reportService, times(1)).getMealHistory(1L, date.minusDays(7), date);
    }
}

