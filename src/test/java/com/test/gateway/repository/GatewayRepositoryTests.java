package com.test.gateway.repository;

import com.test.gateway.entity.Gateway;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.ArrayList;
import java.util.Date;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DataJpaTest
class GatewayRepositoryTests {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private GatewayRepository gatewayRepository;

    @Test
    void testFindByIdWhenExistent() {
        Gateway gateway = entityManager.persist(getDefaultGateway());

        Optional<Gateway> gatewayOptional = gatewayRepository.findById(gateway.getSerialNumber());

        assertThat(gatewayOptional).isPresent();
        assertThat(gatewayOptional.get()).isEqualTo(gateway);
    }

    @Test
    void testFindByIdWhenNonExistent() {
        Optional<Gateway> gatewayOptional = gatewayRepository.findById("1234");
        assertThat(gatewayOptional).isNotPresent();
    }

    private Gateway getDefaultGateway() {
        Gateway gateway = new Gateway("123456",
                "testDevice",
                "192.168.0.0",
                new ArrayList<>(),
                new Date(),
                new Date()
        );
        return gateway;
    }
}
