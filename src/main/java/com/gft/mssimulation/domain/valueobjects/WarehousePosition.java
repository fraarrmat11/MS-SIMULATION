package com.gft.mssimulation.domain.valueobjects;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class WarehousePosition {
    private UUID id;
    private String name;
    private Location location;
    private WarehouseType type;


}
