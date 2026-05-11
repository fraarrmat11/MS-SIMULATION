package com.gft.mstime.application.usecase;

import com.gft.mstime.application.command.AdvanceTimeCommand;
import com.gft.mstime.application.result.TimeAdvancedResult;


public interface AdvanceTimeUseCase {

    TimeAdvancedResult advanceTime(AdvanceTimeCommand command);
}
