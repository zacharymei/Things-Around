package projects.mods.ta.event;

import net.fabricmc.api.ModInitializer;
import projects.mods.ta.util.status_effect.BiomeStatusEffectUpdater;

public class EventInit implements ModInitializer {
    @Override
    public void onInitialize() {


        PlayerTickCallback.TICK_MOVEMENT.register(new BiomeStatusEffectUpdater());

        //ArrowEntityCallback.ENTITY_HIT_EVENT.register(HealingArrowForestEvent.getInstance());
    }
}
