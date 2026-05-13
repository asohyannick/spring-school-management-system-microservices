package com.ecol.admissionService.controller;

import com.ecol.admissionService.service.AdmissionService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/${api.version}/admit")
@Tag ( name = "", description = "")
public class AdmissionController {
  private final AdmissionService admissionService;
}
