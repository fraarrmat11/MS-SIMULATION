package com.gft.msmap.application.service;

import com.gft.msmap.domain.MapState;

public interface MapStateHolder {

    MapState get();

    void set(MapState mapState);
}
