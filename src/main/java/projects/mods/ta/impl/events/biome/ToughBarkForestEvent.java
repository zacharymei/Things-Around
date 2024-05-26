package projects.mods.ta.impl.events.biome;

import net.fabricmc.yarn.constants.MiningLevels;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.AxeItem;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.registry.tag.ItemTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import projects.mods.ta.event.BlockStripCallback;
import projects.mods.ta.event.PlayerEntityCallback;
import projects.mods.ta.item.ModItems;
import projects.mods.ta.util.tags.CustomBlockTags;

import java.util.Optional;
import java.util.Random;

public class ToughBarkForestEvent extends ForestEvent
        implements PlayerEntityCallback.BlockBreakingSpeed,
                    BlockStripCallback {

    ToughBarkForestEvent(){
        PlayerEntityCallback.BLOCK_BREAKING_SPEED.register(this);
        BlockStripCallback.STRIP.register(this);
    }


    @Override
    public float getBlockBreakingSpeed(PlayerEntity player, BlockState block, float original) {
        if(isActive){
            if(player.isCreative()) return original;

            if(block.isIn(BlockTags.LOGS) && !block.isIn(CustomBlockTags.STRIPPED_LOG)){

                ItemStack stack = player.getMainHandStack();
                if(!stack.isSuitableFor(block)) return original / 10f;

                if(stack.isIn(ItemTags.AXES)){
                    AxeItem axe = (AxeItem) stack.getItem();
                    int i = axe.getMaterial().getMiningLevel();
                    if(i < MiningLevels.IRON) return original/axe.getMaterial().getMiningSpeedMultiplier();
                }

                return original / 2f;
            }
        }
        return original;
    }


    @Override
    public void onTryStrip(World world, BlockPos pos, @Nullable PlayerEntity player, BlockState state, Optional<BlockState> state_after_strip) {
        if(isActive && player != null && !world.isClient()){
            if(isInBiome(world.getBiome(player.getBlockPos())) && state.isIn(BlockTags.LOGS)){

                if(new Random().nextInt(10) != 1) return;

                BlockPos entity_pos = pos.offset(player.getHorizontalFacing().getOpposite(), 1);
                ItemEntity bark_entity = new ItemEntity(world, entity_pos.getX(), entity_pos.getY(), entity_pos.getZ(), ModItems.TOUGH_BARK.getDefaultStack());
                world.spawnEntity(bark_entity);
            }
        }
    }
}
