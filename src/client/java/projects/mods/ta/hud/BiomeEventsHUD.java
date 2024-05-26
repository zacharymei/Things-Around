package projects.mods.ta.hud;

import com.google.common.collect.Lists;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.text.Text;
import net.minecraft.util.Colors;
import net.minecraft.util.Formatting;
import net.minecraft.world.biome.Biome;
import projects.mods.ta.impl.AroundsType;
import projects.mods.ta.impl.events.CurrentBiomeEventManager;
import projects.mods.ta.impl.events.biome.BiomeEventInstance;

import java.util.List;

public class BiomeEventsHUD implements HudRenderCallback {
    @Override
    public void onHudRender(DrawContext drawContext, float tickDelta) {

        MinecraftClient mc = MinecraftClient.getInstance();

        if(mc.getCameraEntity() instanceof AbstractClientPlayerEntity player) {
            CurrentBiomeEventManager manager = CurrentBiomeEventManager.getManager(player.getWorld());
            RegistryEntry<Biome> biome = player.getWorld().getBiome(player.getBlockPos());
            drawEventList(drawContext, manager.getOrCreateList(AroundsType.getType(biome)));
        }

    }

    void drawEventList(DrawContext drawContext, List<BiomeEventInstance> list){

        MinecraftClient mc = MinecraftClient.getInstance();

        int right_x = (int) (drawContext.getScaledWindowWidth() * 0.99);
        int top_y = (int) 64;

        int interval = (int) (mc.textRenderer.fontHeight * 0.2);

        int count = 0;
        int max_width = 0;



        for(BiomeEventInstance instance: Lists.newArrayList(list)){
            Text name = Text.translatable(instance.getEvent().getTranslationKey());
            int text_width = mc.inGameHud.getTextRenderer().getWidth(name);
            if(text_width > max_width) max_width = text_width;
            int instance_y = top_y + count * (mc.inGameHud.getTextRenderer().fontHeight + interval);
            drawContext.drawText(mc.inGameHud.getTextRenderer(), name, right_x-text_width, instance_y, Colors.WHITE, false);
            count ++;

        }

        int bottom_y = top_y - 6 * interval;
        int title_y = bottom_y - interval - mc.inGameHud.getTextRenderer().fontHeight;

        Text title = Text.literal("FOREST EVENTS");
        int title_width = mc.inGameHud.getTextRenderer().getWidth(title);
        if(title_width > max_width) max_width = title_width;
        drawContext.drawCenteredTextWithShadow(mc.inGameHud.getTextRenderer(), title, right_x-max_width/2, title_y, Formatting.DARK_GREEN.getColorValue());

        drawContext.drawHorizontalLine(right_x-max_width, right_x, bottom_y, Colors.WHITE);
    }

}
