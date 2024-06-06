package projects.mods.ta.impl.events.biome;

import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Util;
import net.minecraft.world.biome.Biome;
import projects.mods.ta.impl.AroundsType;

import java.util.Objects;

public abstract class BiomeEventImpl {

    private String translation_key;

    protected boolean isActive = false;
    protected int preset_duration = -1;



    protected String loadTranslationKey(){
        if(translation_key == null) translation_key = Util.createTranslationKey("biome_event", BiomeEvents.getEventId((BiomeEvent) this));
        return this.translation_key;
    }

    protected abstract void init();

    protected void cleanup(){};


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BiomeEvent that)) return false;
        return ((BiomeEvent) this).is(that);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(((BiomeEvent) this).getTranslationKey());
    }
}
