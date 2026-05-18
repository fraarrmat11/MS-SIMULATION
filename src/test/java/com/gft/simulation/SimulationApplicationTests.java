package com.gft.simulation;

import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.mockito.Mockito.mockStatic;

@SpringBootTest
@ActiveProfiles("test")
class SimulationApplicationTests {

    @Test
    void contextLoads() {
    }

    @Test
    void mainStartsSpringApplication() {
        String[] args = {"--spring.main.web-application-type=none"};

        try (MockedStatic<SpringApplication> springApplication = mockStatic(SpringApplication.class)) {
            SimulationApplication.main(args);

            springApplication.verify(() -> SpringApplication.run(SimulationApplication.class, args));
        }
    }
}
