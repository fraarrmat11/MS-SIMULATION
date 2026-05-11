package com.gft.msmap.infraestructure.persistence.jpa;

import com.gft.msmap.domain.WarehouseType;
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
    private UUID warehouseId;

    private String name;

    @Column(name = "x_edge")
    private int xEdge;

    @Column(name = "y_edge")
    private int yEdge;

    @Enumerated(EnumType.STRING)
    @Column(name = "warehouse_type")
    private WarehouseType warehouseType;
}
