package com.gft.mssimulation.application.mapstate;

import com.gft.mssimulation.domain.mapstate.Location;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@AllArgsConstructor
public class RegisterTruckUseCase {

    private final MapStateHolder holder;

    public void execute(UUID truckId, Location location){
        holder.get().registerTruck(truckId, location);
    }
}
