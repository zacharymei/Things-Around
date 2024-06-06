package projects.mods.ta.render;

import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;
import net.minecraft.block.MapColor;
import net.minecraft.client.color.block.BlockColorProvider;
import net.minecraft.entity.boss.BossBar;
import net.minecraft.util.ColorCode;
import net.minecraft.util.Colors;
import projects.mods.ta.base.block.ModBlocks;

import java.awt.*;
import java.util.Random;

public class ModColorProvider{

    public static final BlockColorProvider LUCK_LEAVES_COLOR = (state, view, pos, tintIndex)->{

        Color base_color = Color.decode("0xe2bc46");

        int[] luck_colors = new int[]{
//                base_color.darker().darker().getRGB(),
//                base_color.darker().getRGB(),
//                base_color.getRGB(),
//                base_color.brighter().getRGB(),
//                base_color.brighter().brighter().getRGB(),
//                0xf7c457,
                0xf5cc45,
                0xf5cc45,
                0xf5cc45,
//                0xfabd55,
                0xf2c11f,
                0xf5b445
        };

        return luck_colors[new Random(pos.asLong()).nextInt(luck_colors.length)];

    };

    public static void register(){
        ColorProviderRegistry.BLOCK.register(LUCK_LEAVES_COLOR, ModBlocks.LUCK_LEAVES);
    }

}
