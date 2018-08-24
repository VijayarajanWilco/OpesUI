package com.wilco.opesui.mapmyplan;

import com.squareup.otto.Bus;

/**
 * Created by vijayarajan on 7/4/2018.
 */

public class BusProvider {

    private static final Bus BUS = new Bus();

    public static Bus getInstance(){
        return BUS;
    }

    public BusProvider(){}

}
