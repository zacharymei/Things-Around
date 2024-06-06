package projects.mods.ta;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import projects.mods.ta.event.ItemStackGetTooltipEvents;
import projects.mods.ta.hud.ArrowBowHud;
import projects.mods.ta.hud.BiomeEventsHUD;
import projects.mods.ta.network.ModNetworks;
import projects.mods.ta.render.ModColorProvider;

public class ThingsAroundClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		// This entrypoint is suitable for setting up client-specific logic, such as rendering.

		HudRenderCallback.EVENT.register(new ArrowBowHud());
		HudRenderCallback.EVENT.register(new BiomeEventsHUD());
		ModNetworks.init();

		ModColorProvider.register();

	}
}