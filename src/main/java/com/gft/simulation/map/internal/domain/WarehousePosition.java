package com.gft.simulation.map.internal.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class WarehousePosition {
    private String warehouseId;
    private String name;
    private Location location;
    private String type;
}
