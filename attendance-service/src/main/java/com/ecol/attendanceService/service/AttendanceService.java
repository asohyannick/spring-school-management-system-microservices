package com.ecol.attendanceService.service;

import com.ecol.attendanceService.mapper.AttendanceMapper;
import com.ecol.attendanceService.repository.AttendanceRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class AttendanceService {
	 private final AttendanceRepository attendanceRepository;
	 private final AttendanceMapper attendanceMapper;
}
