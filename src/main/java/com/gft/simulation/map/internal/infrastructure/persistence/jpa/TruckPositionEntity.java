package com.gft.simulation.map.internal.infrastructure.persistence.jpa;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Table(name = "truck_position")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class TruckPositionEntity {
    @Id
    private UUID truckId;

    @Column(name = "x_edge")
    private int xEdge;

    @Column(name = "y_edge")
    private int yEdge;
}
