package com.gft.mssimulation.infrastructure.messaging.rabbitmq.message;

import com.gft.mssimulation.domain.mapstate.Location;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;

@AllArgsConstructor
@Getter
public class TruckRegisteredEvent {
    UUID truckId;
    Location location;



}
