package com.gft.mssimulation.application.simulationclock;

import com.gft.mssimulation.application.simulationclock.command.AdvanceTimeCommand;
import com.gft.mssimulation.application.simulationclock.result.TimeAdvancedResult;


public interface AdvanceTimeUseCase {

    TimeAdvancedResult advanceTime(AdvanceTimeCommand command);
}
