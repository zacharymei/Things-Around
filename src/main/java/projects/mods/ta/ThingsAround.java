package projects.mods.ta;

import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Identifier;
import net.minecraft.world.LightType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import projects.mods.ta.command.ModCommands;
import projects.mods.ta.impl.EntityTrackedData;
import projects.mods.ta.impl.events.BiomeEventsUpdater;
import projects.mods.ta.impl.util.BlockPlaceUtil;
import projects.mods.ta.item.ModItem;


public class ThingsAround implements ModInitializer {
	// This logger is used to write text to the console and the log file.
	// It is considered best practice to use your mod id as the logger's name.
	// That way, it's clear which mod wrote info, warnings, and errors.
    public static final Logger LOGGER = LoggerFactory.getLogger("things-around");

	public static final String MOD_ID = "things-around";

	public static final Item custom_item1 = Registry.register(Registries.ITEM, new Identifier("ta", "custom1"),
			new ModItem(new Item.Settings()));

	public static final BiomeEventsUpdater BIOME_EVENTS_UPDATER = new BiomeEventsUpdater();




	@Override
	public void onInitialize() {
		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution.

		LOGGER.info("Hello Fabric world!");


		ItemGroupEvents.modifyEntriesEvent(ItemGroups.INGREDIENTS).register(content-> content.add(custom_item1));

		ModCommands.register();

		UseBlockCallback.EVENT.register(((player, world, hand, hitResult) -> {
			if(!player.getMainHandStack().isEmpty()) return ActionResult.PASS;
			boolean bl = BlockPlaceUtil.isPuddleShape(world, hitResult.getBlockPos());
			if(!world.isClient()) player.sendMessage(Text.literal(String.valueOf(bl)));
			if(!world.isClient()) player.sendMessage(Text.literal(String.valueOf(world.getLightLevel(LightType.SKY, hitResult.getBlockPos().up()))));
			return ActionResult.PASS;
		}));




	}
}