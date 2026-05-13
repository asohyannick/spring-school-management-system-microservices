package com.ecol.humanResource.repository;

import com.ecol.humanResource.entity.HumanResource;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface HumanResourceRepository extends JpaRepository < HumanResource, UUID > {

}
