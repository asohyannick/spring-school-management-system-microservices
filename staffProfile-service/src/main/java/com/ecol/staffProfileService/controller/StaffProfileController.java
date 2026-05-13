package com.ecol.staffProfileService.controller;

import com.ecol.staffProfileService.service.StaffProfileService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/${api.version}/staff")
@Tag ( name = "", description = "")
public class StaffProfileController {
   private final StaffProfileService staffProfileService;
}
