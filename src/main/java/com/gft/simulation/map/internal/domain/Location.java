package com.gft.simulation.map.internal.domain;

import com.gft.simulation.map.internal.domain.exceptions.InvalidLocationException;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode
public class Location {
    private final int x;
    private final int y;

    public Location(int x, int y) {
        if (x < 0 || y < 0) {
            throw new InvalidLocationException(x, y);
        }
        this.x = x;
        this.y = y;
    }
}
