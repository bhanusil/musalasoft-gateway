package com.test.gateway.repository;

import com.test.gateway.entity.Device;
import com.test.gateway.entity.Gateway;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.ArrayList;
import java.util.Date;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DataJpaTest
class DeviceRepositoryTests {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private DeviceRepository deviceRepository;

    @Test
    void testFindByIdWhenExistent() {
        Device device = entityManager.persist(getDefaultDevice());

        Optional<Device> deviceOptional = deviceRepository.findById(device.getUid());

        assertThat(deviceOptional).isPresent();
        assertThat(deviceOptional.get()).isEqualTo(device);
    }

    @Test
    void testFindByIdWhenNonExistent() {
        Optional<Device> deviceOptional = deviceRepository.findById("1234");
        assertThat(deviceOptional).isNotPresent();
    }

    @Test
    void testCountAllByGatewaySerialNumber() {
        entityManager.persist(getDefaultGateway());
        Device device = entityManager.persist(getDefaultDevice());

        Long deviceCount = deviceRepository.countAllByGatewaySerialNumber(device.getGatewaySerialNumber());

        assertThat(deviceCount).isEqualTo(1);
    }

    @Test
    void testCountAllByGatewaySerialNumberWhenWrongSerialNumber() {
        Device device = entityManager.persist(getDefaultDevice());

        Exception exception = assertThrows(DataIntegrityViolationException.class, () -> {
            deviceRepository.countAllByGatewaySerialNumber(device.getGatewaySerialNumber());
        });

        String expectedMessage = "ConstraintViolationException";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    private Device getDefaultDevice() {
        Device device = new Device("123456",
                "testDevice",
                "192.168.0.0",
                "123456",
                new Date(),
                new Date()
        );
        return device;
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
