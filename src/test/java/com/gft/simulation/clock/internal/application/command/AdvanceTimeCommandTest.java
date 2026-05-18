package com.gft.simulation.clock.internal.application.command;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class AdvanceTimeCommandTest {

    @Test
    void constructor_WhenGivenDays_ShouldExposeDays() {
        AdvanceTimeCommand command = new AdvanceTimeCommand(4);

        assertThat(command.days()).isEqualTo(4);
    }

    @Test
    void records_WhenGivenSameDays_ShouldBeEqual() {
        AdvanceTimeCommand firstCommand = new AdvanceTimeCommand(2);
        AdvanceTimeCommand secondCommand = new AdvanceTimeCommand(2);

        assertThat(firstCommand).isEqualTo(secondCommand);
        assertThat(firstCommand.hashCode()).isEqualTo(secondCommand.hashCode());
        assertThat(firstCommand).hasToString("AdvanceTimeCommand[days=2]");
    }
}
