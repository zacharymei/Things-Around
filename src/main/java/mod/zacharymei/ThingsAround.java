package mod.zacharymei;

import mod.zacharymei.base.block.ModBlocks;
import mod.zacharymei.base.component.ModComponents;
import mod.zacharymei.base.item.ModItems;
import mod.zacharymei.command.ModCommands;
import mod.zacharymei.event.ModEvents;
import mod.zacharymei.event.ServerLevelEvent;
import mod.zacharymei.impl.events.biomeevent.ServerBiomeEventManager;
import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.item.v1.ItemComponentTooltipProviderRegistry;
import net.minecraft.core.component.DataComponents;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ThingsAround implements ModInitializer {
	public static final String MOD_ID = "things-around";

	// This logger is used to write text to the console and the log file.
	// It is considered best practice to use your mod id as the logger's name.
	// That way, it's clear which mod wrote info, warnings, and errors.
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution.

		ModItems.initialize();
		ModBlocks.initialize();
		ModCommands.initialize();
		ModComponents.initialize();
		ModEvents.initialize();

		ItemComponentTooltipProviderRegistry.addAfter(DataComponents.ENCHANTMENTS, ModComponents.ENCHANTS_DURATION);
		ItemComponentTooltipProviderRegistry.addAfter(DataComponents.ENCHANTMENTS, ModComponents.ENCHANTMENTS_DURATIONS);

		ServerBiomeEventManager.init();

		LOGGER.info("Hello Fabric world!");
	}
}