package projects.mods.ta;

import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import projects.mods.ta.base.block.ModBlocks;
import projects.mods.ta.base.item.ModItems;
import projects.mods.ta.command.ModCommands;
import projects.mods.ta.impl.AroundsSetup;
import projects.mods.ta.impl.events.BiomeEventsUpdater;
import projects.mods.ta.testing.RightClickTest;


public class ThingsAround implements ModInitializer {
	// This logger is used to write text to the console and the log file.
	// It is considered best practice to use your mod id as the logger's name.
	// That way, it's clear which mod wrote info, warnings, and errors.
    public static final Logger LOGGER = LoggerFactory.getLogger("things-around");

	public static final String MOD_ID = "things-around";

	public static final BiomeEventsUpdater BIOME_EVENTS_UPDATER = new BiomeEventsUpdater();




	@Override
	public void onInitialize() {
		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution.

		LOGGER.info("Hello Fabric world!");


		ModBlocks.register();
		ModItems.register();
		ModCommands.register();

		new AroundsSetup();


		UseBlockCallback.EVENT.register(new RightClickTest());


	}
}