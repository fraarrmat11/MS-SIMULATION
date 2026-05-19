package com.gft.simulation.time.internal.application.usecase;

import com.gft.simulation.time.internal.application.command.AdvanceTimeCommand;
import com.gft.simulation.time.internal.application.result.TimeAdvancedResult;

public interface AdvanceTimeUseCase {

    TimeAdvancedResult advanceTime(AdvanceTimeCommand command);
}
