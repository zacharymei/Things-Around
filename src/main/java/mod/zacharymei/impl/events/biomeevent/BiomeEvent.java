package mod.zacharymei.impl.events.biomeevent;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import mod.zacharymei.ThingsAround;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Util;

import java.util.Objects;

public abstract class BiomeEvent {

    public static int DEFAULT_DURATION = 20 * 20;

    private BiomeEventInstance activeInstance;

    protected boolean active = false;

    public static final Codec<BiomeEvent> CODEC = Identifier.CODEC.comapFlatMap(
            BiomeEvent::readIdentifier,
            BiomeEvent::getIdentifier
    );

    protected abstract void init();

    protected void cleanup(){};

    protected final BiomeEventInstance getOrCreateInstance(){
        if(activeInstance == null){
            activeInstance = new BiomeEventInstance(this);
        }
        active = true;
        return activeInstance;
    }

    public BiomeEventInstance getActiveInstance(){
        return activeInstance;
    }

    public void endInstance(){
        active = false;
        activeInstance = null;
    }

    public Identifier getIdentifier(){
        return Identifier.tryBuild(ThingsAround.MOD_ID, "biome_event." + getName());
    }

    private static DataResult<BiomeEvent> readIdentifier(Identifier id) {
        for (ModBiomeEvents entry : ModBiomeEvents.values()) {
            if (entry.getBiomeEvent().getIdentifier().equals(id)) {
                return DataResult.success(entry.getBiomeEvent());
            }
        }
        return DataResult.error(() -> "Unknown biome event: " + id);
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
