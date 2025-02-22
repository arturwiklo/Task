package com.betacom.recruitmentTask.repository;

import com.betacom.recruitmentTask.model.Item;
import com.betacom.recruitmentTask.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ItemRepository extends JpaRepository<Item, UUID> {

    List<Item> findByOwner(Users owner);
}

