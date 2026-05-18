package com.gft.simulation.clock.internal.application.usecase;

import com.gft.simulation.clock.internal.application.command.AdvanceTimeCommand;
import com.gft.simulation.clock.internal.application.result.TimeAdvancedResult;

public interface AdvanceTimeUseCase {

    TimeAdvancedResult advanceTime(AdvanceTimeCommand command);
}
