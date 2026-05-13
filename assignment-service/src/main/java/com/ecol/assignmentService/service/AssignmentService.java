package com.ecol.assignmentService.service;

import com.ecol.assignmentService.mapper.AssignmentMapper;
import com.ecol.assignmentService.repository.AssignmentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class AssignmentService {
	 private final AssignmentRepository assignmentRepository;
	 private final AssignmentMapper assignmentMapper;
}
