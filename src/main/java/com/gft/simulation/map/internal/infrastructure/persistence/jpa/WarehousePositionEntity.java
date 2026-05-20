package com.gft.simulation.map.internal.infrastructure.persistence.jpa;

import com.gft.simulation.map.internal.domain.WarehouseType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Table(name = "warehouse_position")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class WarehousePositionEntity {
    @Id
    private String warehouseId;

    private String name;

    @Column(name = "x_edge")
    private int xEdge;

    @Column(name = "y_edge")
    private int yEdge;

    @Column(name = "warehouse_type")
    private String warehouseType;
}
