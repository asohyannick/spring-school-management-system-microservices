package com.ecol.courseService.controller;

import com.ecol.courseService.service.CourseService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/${api.version}/course")
@Tag ( name = "Course Service", description = "")
public class CourseController {
 private  final CourseService courseService;
 
}
