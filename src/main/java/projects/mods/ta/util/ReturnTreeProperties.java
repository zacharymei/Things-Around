package projects.mods.ta.util;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;

import java.util.Map;
import java.util.Set;

public record ReturnTreeProperties(BlockPos root, int height, Map<BlockPos, BlockState> trunks, Map<BlockPos, BlockState> leaves) {

}
