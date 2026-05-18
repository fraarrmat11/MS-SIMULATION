package com.gft.simulation.clock.internal.application.port.out;

import com.gft.simulation.clock.internal.domain.TimeAdvancedEvent;

public interface TimeAdvancedEventPublisher {
    void publish(TimeAdvancedEvent event);
}
