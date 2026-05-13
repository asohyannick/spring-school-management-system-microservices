package com.ecol.inventoryService.controller;

import com.ecol.inventoryService.service.InventoryService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/${api.version}/inventory")
@RequiredArgsConstructor
@Tag ( name = "", description = "")
public class InventoryController {
  private final InventoryService inventoryService;
}
