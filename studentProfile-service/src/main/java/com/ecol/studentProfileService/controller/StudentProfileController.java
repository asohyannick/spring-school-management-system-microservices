package com.ecol.studentProfileService.controller;

import com.ecol.studentProfileService.service.StudentProfileService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/${api.version}/student")
@RequiredArgsConstructor
@Tag ( name = "", description = "")
public class StudentProfileController {
  private final StudentProfileService studentProfileService;
}
