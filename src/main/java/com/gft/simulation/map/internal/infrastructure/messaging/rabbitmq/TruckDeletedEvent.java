package com.gft.simulation.map.internal.infrastructure.messaging.rabbitmq;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class TruckDeletedEvent {
    UUID truckId;
}
