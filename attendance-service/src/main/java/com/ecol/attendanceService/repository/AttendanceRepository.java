package com.ecol.attendanceService.repository;

import com.ecol.attendanceService.entity.Attendance;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface AttendanceRepository extends JpaRepository < Attendance, UUID > {

}
