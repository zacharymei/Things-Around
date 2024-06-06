package projects.mods.ta.impl.events.biome;

import net.minecraft.entity.Entity;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.tag.BiomeTags;
import net.minecraft.world.biome.Biome;
import projects.mods.ta.impl.AroundsType;
import projects.mods.ta.util.tags.CustomBiomeTags;

public interface ForestEvent extends BiomeEvent{

    AroundsType event_type = AroundsType.FOREST;

    @Override
    default AroundsType getType() {
        return event_type;
    }
}
