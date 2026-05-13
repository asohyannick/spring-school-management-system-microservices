package com.ecol.reviewService.controller;

import com.ecol.reviewService.service.ReviewService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/${api.version}/review")
@Tag ( name = "", description = "")
public class ReviewController {
   private final ReviewService reviewService;
}
