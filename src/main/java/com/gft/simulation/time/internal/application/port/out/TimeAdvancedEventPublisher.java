package com.gft.simulation.time.internal.application.port.out;

import com.gft.simulation.time.internal.domain.TimeAdvancedEvent;

public interface TimeAdvancedEventPublisher {
    void publish(TimeAdvancedEvent event);
}
