package com.ecol.inventoryService.repository;

import com.ecol.inventoryService.entity.Inventory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface InventoryRepository extends JpaRepository < Inventory, UUID > {

}
