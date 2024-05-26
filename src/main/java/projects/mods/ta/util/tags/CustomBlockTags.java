package projects.mods.ta.util.tags;

import net.minecraft.block.Block;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;

public class CustomBlockTags {

    public static final TagKey<Block> STRIPPED_LOG = TagKey.of(RegistryKeys.BLOCK, new Identifier("things-around", "stripped_log"));

}
