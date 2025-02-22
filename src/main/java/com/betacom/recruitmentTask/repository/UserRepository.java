package com.betacom.recruitmentTask.repository;

import com.betacom.recruitmentTask.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<Users, UUID> {

    Optional<Users> findByLogin(String login);
}
