package com.pii.tracker.track_calories.report.controller;

import com.pii.tracker.track_calories.report.dto.CalorieLimitResponse;
import com.pii.tracker.track_calories.report.dto.DailyReportResponse;
import com.pii.tracker.track_calories.report.dto.MealHistoryResponse;
import com.pii.tracker.track_calories.report.service.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/users/{userId}/reports")
@RequiredArgsConstructor
public class ReportController {

    private final ReportService reportService;

    @GetMapping("/daily")
    public ResponseEntity<DailyReportResponse> getDailyReport(
            @PathVariable Long userId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date
    ) {
        var response = reportService.getDailyReport(userId, date);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/is-within-limit")
    public ResponseEntity<CalorieLimitResponse> checkCalorieLimit(
            @PathVariable Long userId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date
    ) {
        return ResponseEntity.ok(reportService.checkCalorieLimit(userId, date));
    }

    @GetMapping("/history")
    public ResponseEntity<List<MealHistoryResponse>> getMealHistory(
            @PathVariable Long userId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate end
    ) {
        return ResponseEntity.ok(reportService.getMealHistory(userId, start, end));
    }
}
