package mod.zacharymei.event;

import mod.zacharymei.impl.enchanment.DETracker;

public class ModEvents {

    public static void initialize(){

        ItemStackEvent.END_INVENTORY_TICK.register(new DETracker());

    }


}
