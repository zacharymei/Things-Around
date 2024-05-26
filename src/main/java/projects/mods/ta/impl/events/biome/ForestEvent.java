package projects.mods.ta.impl.events.biome;

import net.minecraft.entity.Entity;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.tag.BiomeTags;
import net.minecraft.world.biome.Biome;
import projects.mods.ta.impl.AroundsType;
import projects.mods.ta.util.tags.CustomBiomeTags;

public abstract class ForestEvent extends BiomeEvent {

    final AroundsType type = AroundsType.FOREST;

    @Override
    public AroundsType getType(){
        return this.type;
    }

    public boolean isInBiome(RegistryEntry<Biome> biome){
        return biome.isIn(CustomBiomeTags.IS_FOREST_AROUND);
    }


}
