package com.gft.mssimulation.domain.mapstate;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class TruckPosition {
    private UUID truckId;
    private Location location;


}
