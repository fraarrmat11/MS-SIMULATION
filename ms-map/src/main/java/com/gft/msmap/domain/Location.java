package com.gft.msmap.domain;


import com.gft.msmap.domain.exceptions.InvalidLocationException;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
public class Location {
    private int x;
    private int y;

    public Location(int x, int y) {
        if(x < 0 || y < 0){
            throw new InvalidLocationException(x,y);
        }
        this.x = x;
        this.y = y;
    }
}
