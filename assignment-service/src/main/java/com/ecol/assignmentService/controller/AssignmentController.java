package com.ecol.assignmentService.controller;

import com.ecol.assignmentService.service.AssignmentService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/${api.version}/assignment")
@Tag ( name = "Assignment Service", description = "")
@RequiredArgsConstructor
public class AssignmentController {
	private final AssignmentService assignmentService;

}
