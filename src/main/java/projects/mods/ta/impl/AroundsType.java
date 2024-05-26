package projects.mods.ta.impl;


import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.world.biome.Biome;
import projects.mods.ta.util.tags.CustomBiomeTags;

public enum AroundsType {

    FOREST("forest", CustomBiomeTags.IS_FOREST_AROUND);

    public final String name;
    public final TagKey<Biome> biome_tag;

    AroundsType(String name, TagKey<Biome> tag) {
        this.name = name;
        this.biome_tag = tag;
    }

    public static AroundsType getType(String name){
        for(AroundsType type: values()){
            if(name.equals(type.name)) return type;
        }
        return null;
    }

    public static AroundsType getType(RegistryEntry<Biome> biome){
        for(AroundsType type: values()){
            if(biome.isIn(type.biome_tag)) return type;
        }
        return null;
    }

    @Override
    public String toString(){
        return this.name;
    }





}
