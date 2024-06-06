package projects.mods.ta.base.block;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.LeavesBlock;
import net.minecraft.block.piston.PistonBehavior;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.Identifier;
import projects.mods.ta.ThingsAround;

import java.util.Random;

public class ModBlocks {

    public static final Block LUCK_LEAVES = Registry.register(Registries.BLOCK, new Identifier(ThingsAround.MOD_ID, "luck_leaves"),
            new LeavesBlock(
                    FabricBlockSettings.create().luminance(state->new Random().nextInt(3, 15))
                            .strength(0.2f).ticksRandomly().sounds(BlockSoundGroup.GRASS).nonOpaque().allowsSpawning(Blocks::canSpawnOnLeaves).suffocates(Blocks::never).blockVision(Blocks::never).burnable().pistonBehavior(PistonBehavior.DESTROY).solidBlock(Blocks::never)));


    public static void register(){

    }
}
