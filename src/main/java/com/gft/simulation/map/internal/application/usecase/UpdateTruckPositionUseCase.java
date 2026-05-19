package com.gft.simulation.map.internal.application.usecase;

import com.gft.simulation.map.internal.application.port.out.TruckPositionPort;
import com.gft.simulation.map.internal.application.service.MapStateHolder;
import com.gft.simulation.map.internal.domain.Location;
import com.gft.simulation.map.internal.domain.TruckPosition;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Slf4j
@Service
@AllArgsConstructor
public class UpdateTruckPositionUseCase {

    private final MapStateHolder holder;
    private final TruckPositionPort truckPositionPort;

    @Transactional
    public void execute(UUID truckId, Location location) {
        log.info("Updating truck position: truckId={}", truckId);
        truckPositionPort.save(new TruckPosition(truckId, location));
        holder.get().updateTruckPosition(truckId, location);
        log.info("Truck position updated successfully: truckId={}", truckId);
    }
}
