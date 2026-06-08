package mod.zacharymei.impl.events.biomeevent;

import mod.zacharymei.impl.events.biomeevent.RainyDayEvent;

public enum ModBiomeEvents {

    RainyDayEvent(new RainyDayEvent());

    private final BiomeEvent biomeEvent;

    private ModBiomeEvents(BiomeEvent biomeEvent) {
        this.biomeEvent = biomeEvent;
    }

    public BiomeEvent getBiomeEvent(){
        return this.biomeEvent;
    }

}
