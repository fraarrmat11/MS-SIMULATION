package com.gft.mssimulation.infrastructure.messaging.rabbitmq.message;

import com.gft.mssimulation.domain.mapstate.Location;
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
