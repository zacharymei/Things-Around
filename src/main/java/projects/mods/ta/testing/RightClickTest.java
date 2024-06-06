package projects.mods.ta.testing;

import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import projects.mods.ta.base.block.ModBlocks;
import projects.mods.ta.impl.AroundsType;
import projects.mods.ta.impl.arounds.AroundsLevelTracker;
import projects.mods.ta.impl.events.biome.RainyDayForestEvent;
import projects.mods.ta.impl.util.BlockPlaceUtil;
import projects.mods.ta.util.ReturnTreeProperties;
import projects.mods.ta.util.TreesHelper;

import java.util.Optional;

public class RightClickTest implements UseBlockCallback {

    static ReturnTreeProperties TREE = null;

    @Override
    public ActionResult interact(PlayerEntity player, World world, Hand hand, BlockHitResult hitResult) {

        if(world.getBlockState(hitResult.getBlockPos()).isIn(BlockTags.LOGS)){
            TreesHelper helper = new TreesHelper(world);
            helper.getTree(hitResult.getBlockPos(), 1).ifPresent(t->{
                TREE = t;
                player.sendMessage(Text.literal(String.valueOf(t.height())));
                player.sendMessage(Text.literal(t.root().toString()));
                BlockPlaceUtil.replaceAll(world, t.leaves(), ModBlocks.LUCK_LEAVES);
            });

        }
        if(world.getBlockState(hitResult.getBlockPos()).isOf(Blocks.DIAMOND_BLOCK)){
            if(TREE != null){
                BlockPlaceUtil.placeAll(world, TREE.trunks());
                BlockPlaceUtil.placeAll(world, TREE.leaves());

            }
        }

        RegistryEntry<Biome> biome = world.getBiome(player.getBlockPos());
        AroundsType type = AroundsType.getType(biome);
        int around_level = AroundsLevelTracker.getPlayerLevel(player, type);

        player.sendMessage(Text.literal("Arounds Level: "+around_level));


        return ActionResult.PASS;
    }
}
