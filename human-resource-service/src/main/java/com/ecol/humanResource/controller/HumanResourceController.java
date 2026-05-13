package com.ecol.humanResource.controller;

import com.ecol.humanResource.service.HumanResourceService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/${api.version}/resource")
@RequiredArgsConstructor
public class HumanResourceController {
 private final HumanResourceService  humanResourceService;
}
