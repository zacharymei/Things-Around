package projects.mods.ta.impl.events.biome;

import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.text.Text;
import net.minecraft.util.Util;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import projects.mods.ta.impl.AroundsType;

import java.util.Objects;

public abstract class BiomeEvent {

    private String translation_key;

    protected boolean isActive = false;
    protected int preset_duration = -1;


    public abstract AroundsType getType();


    public <E extends BiomeEvent> boolean is(E biome_event){
        return biome_event.getClass().isInstance(this);
    };

    public String getTranslationKey(){
        return this.loadTranslationKey();
    };

    String loadTranslationKey(){
        if(translation_key == null) translation_key = Util.createTranslationKey("biome_event", BiomeEvents.getEventId(this));
        return this.translation_key;
    }

    public BiomeEventInstance createEvent(World world){
        return new BiomeEventInstance(this, this.preset_duration, world.getTime());
    }

    protected void setActive(boolean bl){
        this.isActive = bl;
        if(!bl) cleanup();
    }

    protected void cleanup(){};


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BiomeEvent that)) return false;
        return is(that);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getTranslationKey());
    }
}
