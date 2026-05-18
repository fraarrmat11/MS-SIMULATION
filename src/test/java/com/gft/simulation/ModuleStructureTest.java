package com.gft.simulation;

import org.junit.jupiter.api.Test;
import org.springframework.modulith.core.ApplicationModules;

class ModuleStructureTest {

    @Test
    void modulithStructureIsValid() {
        ApplicationModules.of(SimulationApplication.class).verify();
    }
}
