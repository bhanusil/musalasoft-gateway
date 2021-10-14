package com.test.gateway.repository;

import com.test.gateway.entity.Gateway;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GatewayRepository extends JpaRepository<Gateway, String> {
}
