package com.gft.simulation.map.internal.infrastructure.messaging.rabbitmq;

import com.gft.simulation.map.internal.domain.Location;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class TruckPositionUpdatedEvent {
    private UUID truckId;
    private Location location;
}
