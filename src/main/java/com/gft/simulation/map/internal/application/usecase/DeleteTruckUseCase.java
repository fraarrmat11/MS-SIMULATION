package com.gft.simulation.map.internal.application.usecase;

import com.gft.simulation.map.internal.application.port.out.TruckPositionPort;
import com.gft.simulation.map.internal.application.service.MapStateHolder;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Slf4j
@Service
@AllArgsConstructor
public class DeleteTruckUseCase {

    private final MapStateHolder holder;
    private final TruckPositionPort port;

    @Transactional
    public void execute(UUID truckId){
        log.info("Deleting truck: truckId={}", truckId);
        port.delete(truckId);
        holder.get().deleteTruck(truckId);
        log.info("Truck deleted successfully: truckId={}", truckId);
    }
}
