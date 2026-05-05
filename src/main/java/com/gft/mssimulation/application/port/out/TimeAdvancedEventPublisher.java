package com.gft.mssimulation.application.port.out;

import com.gft.mssimulation.domain.simulationclock.TimeAdvancedEvent;

public interface TimeAdvancedEventPublisher {
    void publish (TimeAdvancedEvent event);
}
