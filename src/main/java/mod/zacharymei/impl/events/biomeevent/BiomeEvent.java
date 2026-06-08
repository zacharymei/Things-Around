package mod.zacharymei.impl.events.biomeevent;

import mod.zacharymei.ThingsAround;
import net.minecraft.util.Util;

import java.util.Objects;

public abstract class BiomeEvent {

    public static int DEFAULT_DURATION = 20 * 20;

    private BiomeEventInstance activeInstance;

    protected boolean active = false;



    protected abstract void init();

    protected void cleanup(){};

    protected final BiomeEventInstance getOrCreateInstance(){
        active = true;
        return activeInstance == null ? new BiomeEventInstance(this) : activeInstance;
    }

    public BiomeEventInstance getActiveInstance(){
        return activeInstance;
    }

    public void endInstance(){
        active = false;
        activeInstance = null;
    }

    public abstract String getName();

    public final String getTranslationKey(){
        return String.join(".", ThingsAround.MOD_ID, "biome_event", getName());
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof IBiomeEvent that)) return false;
        //return ((IBiomeEvent) this).is(that);
        return true; //TODO more
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(this.getTranslationKey());
    }
}
