package com.gft.msmap.application.usecase;

import com.gft.msmap.application.port.out.TruckPositionPort;
import com.gft.msmap.application.service.MapStateHolder;
import com.gft.msmap.domain.Location;
import com.gft.msmap.domain.TruckPosition;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Slf4j
@Service
@AllArgsConstructor
public class RegisterTruckUseCase {

    private final MapStateHolder holder;
    private final TruckPositionPort truckPositionPort;

    public void execute(UUID truckId, Location location) {
        log.info("Registering truck: truckId={}", truckId);
        holder.get().registerTruck(truckId, location);
        truckPositionPort.save(new TruckPosition(truckId, location));
        log.info("Truck registered successfully: truckId={}", truckId);
    }
}
