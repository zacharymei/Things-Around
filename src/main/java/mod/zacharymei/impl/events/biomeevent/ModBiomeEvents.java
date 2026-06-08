package mod.zacharymei.impl.events.biomeevent;

import mod.zacharymei.impl.events.biomeevent.RainyDayEvent;

import java.util.stream.Stream;

public enum ModBiomeEvents {

    RainyDayEvent(new RainyDayEvent());

    private final BiomeEvent biomeEvent;

    private ModBiomeEvents(BiomeEvent biomeEvent) {
        this.biomeEvent = biomeEvent;
    }

    public BiomeEvent getBiomeEvent(){
        return this.biomeEvent;
    }

    public BiomeEvent getBiomeEvent(BiomeEvent biomeEvent){
        return Stream.of(values()).filter(e->e.biomeEvent == biomeEvent).findAny().get().getBiomeEvent();
    }

}
