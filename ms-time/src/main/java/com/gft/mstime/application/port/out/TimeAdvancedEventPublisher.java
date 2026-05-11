package com.gft.mstime.application.port.out;

import com.gft.mstime.domain.TimeAdvancedEvent;

public interface TimeAdvancedEventPublisher {
    void publish (TimeAdvancedEvent event);
}
