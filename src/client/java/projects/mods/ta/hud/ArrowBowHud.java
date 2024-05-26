package projects.mods.ta.hud;

import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BowItem;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.registry.Registries;
import net.minecraft.text.Text;
import net.minecraft.util.Colors;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.BlockPos;
import projects.mods.ta.impl.abilities.TreeEnhancingArcher;
import projects.mods.ta.util.TreesHelper;

import java.util.Set;

public class ArrowBowHud implements HudRenderCallback {

    @Override
    public void onHudRender(DrawContext drawContext, float tickDelta) {

        MinecraftClient mc = MinecraftClient.getInstance();

        Entity entity = mc.getCameraEntity();
        if(entity instanceof AbstractClientPlayerEntity player) {



            if(player.isUsingItem()) {
                if (player.getActiveItem().isOf(Items.BOW)) {

                    drawTreeCount(drawContext, player);
                    //drawForestEventDamage(drawContext);
                }
            }
        }

    }

    void drawForestEventDamage(DrawContext drawContext){

        MinecraftClient mc = MinecraftClient.getInstance();

        String str_sign = "+";
        String str = "3";

        int width = drawContext.getScaledWindowWidth();
        int height = drawContext.getScaledWindowHeight();

        int content_height = mc.inGameHud.getTextRenderer().fontHeight;
        int content_width = mc.inGameHud.getTextRenderer().getWidth(str);

        int x = (int) (width/2 - 1.8f * content_width);
        int y = (int)((height-content_height)/2 + 1.2f * content_height);

        int sign_color = 0x54ffffff;
        int color = Colors.WHITE;



        //drawContext.fillGradient((int) (x-0.4*content_width), (int) (y - 0.3*content_height), (int) (x+1.4*content_width), (int)(y+1.3*content_height), 0x14ffffff, 0x14ffffff);
        drawContext.drawText(mc.inGameHud.getTextRenderer(), Text.literal(str_sign), (int)(x-1.15*content_width), y, sign_color, false);
        drawContext.drawText(mc.inGameHud.getTextRenderer(), Text.literal(str).formatted(Formatting.BOLD), x, y, color, true);
    }

    void drawTreeCount(DrawContext drawContext, AbstractClientPlayerEntity player){

        int range = TreeEnhancingArcher.searchRange(BowItem.getPullProgress(player.getItemUseTime()));
        Set<BlockPos> roots = TreesHelper.getTrees(player.getBlockPos(), range, player.getWorld());

        MinecraftClient mc = MinecraftClient.getInstance();

        String str_count = String.valueOf(roots.size());

        int windowWidth = drawContext.getScaledWindowWidth();
        int windowHeight = drawContext.getScaledWindowHeight();

        int centerX = (int) (windowWidth * 0.37);
        int centerY = (int) (windowHeight / 2);
        int number_width = mc.inGameHud.getTextRenderer().getWidth("0");

        int itemWidth = (int) (number_width * 2.8), itemHeight = (int) (number_width * 2.8);

        int width = (int) (mc.inGameHud.getTextRenderer().getWidth(str_count) + number_width + itemWidth);
        int height = itemHeight;

        int itemX = centerX - width/2;
        int itemY = (int) (centerY - height/2);

        int countX = (int) (itemX + itemWidth + number_width);
        int countY = (int) (centerY - mc.inGameHud.getTextRenderer().fontHeight/2);

        //drawContext.drawHorizontalLine((int) (centerX-0.3*width), (int) (centerX+0.3*width), (int) (centerY-1*height), 0x14ffffff);
        drawContext.drawVerticalLine((int) (centerX + width*0.8), (int) (centerY-0.3*height), (int) (centerY+0.3*height), 0x14ffffff);
        drawContext.drawItem(Items.ARROW.getDefaultStack(), itemX, itemY);
        drawContext.drawText(mc.inGameHud.getTextRenderer(), Text.literal(str_count), countX, countY, Colors.WHITE, true);




        for(BlockPos r: roots){
            if(r.isWithinDistance(player.getBlockPos(), range)){
                player.getWorld().addParticle(ParticleTypes.GLOW, r.getX()+0.4+player.getWorld().random.nextDouble()*0.2, r.getY()-0.7+player.getWorld().random.nextDouble(), r.getZ()+0.4+player.getWorld().random.nextDouble()*0.2, 4, 2, 4);

            }
        }
    }

}
