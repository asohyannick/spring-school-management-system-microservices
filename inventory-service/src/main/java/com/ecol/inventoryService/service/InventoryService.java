package com.ecol.inventoryService.service;

import com.ecol.inventoryService.mapper.InventoryMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class InventoryService {
  private final InventoryService inventoryService;
  private final InventoryMapper inventoryMapper;
}
