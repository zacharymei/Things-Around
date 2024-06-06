package projects.mods.ta.impl.events.biome;

import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Util;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import projects.mods.ta.impl.AroundsType;

public interface BiomeEvent {

    default BiomeEventInstance createEvent(World world){
        return new BiomeEventInstance(this, world.getTime());
    }

    default void setActive(boolean active){
        BiomeEventImpl impl = (BiomeEventImpl) this;
        if(!active) impl.cleanup();
        impl.isActive = active;
    }

    default <E extends BiomeEvent> boolean is(E biome_event){
        return biome_event.getClass().isInstance(this);
    };

    default boolean isInBiome(RegistryEntry<Biome> biome){
        return AroundsType.getType(biome) == getType();
    }

    AroundsType getType();


    default String getTranslationKey(){
        return ((BiomeEventImpl) this).loadTranslationKey();
    };


    default int getDuration(){
        return ((BiomeEventImpl) this).preset_duration;
    };


}
