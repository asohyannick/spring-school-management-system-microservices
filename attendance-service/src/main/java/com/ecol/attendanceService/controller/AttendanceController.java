package com.ecol.attendanceService.controller;

import com.ecol.attendanceService.service.AttendanceService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
@RestController
@RequestMapping("/api/${api.version}/attend")
@RequiredArgsConstructor
@Tag ( name = "Attendance Service", description = "")
public class AttendanceController {
   private final AttendanceService attendanceService;
}
