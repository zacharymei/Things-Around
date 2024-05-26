package projects.mods.ta.util.tags;

import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.BiomeTags;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;
import net.minecraft.world.biome.Biome;

public class CustomBiomeTags {

    public static final TagKey<Biome> IS_FOREST_AROUND = TagKey.of(RegistryKeys.BIOME, new Identifier("things-around", "is_forest_around"));

}
