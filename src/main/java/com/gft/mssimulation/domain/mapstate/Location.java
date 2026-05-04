package com.gft.mssimulation.domain.mapstate;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class Location {
    private int x;
    private int y;

    public Location(int x, int y) {
        if(x < 0 || y < 0){
            throw new IllegalArgumentException("Location edges can't be null");
        }
        this.x = x;
        this.y = y;
    }
}
