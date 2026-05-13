package com.ecol.quizService.controller;

import com.ecol.quizService.service.QuizService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/${api.version}/quiz")
@RequiredArgsConstructor
@Tag ( name = "", description = "")
public class QuizController {
 private final QuizService quizService;
}
