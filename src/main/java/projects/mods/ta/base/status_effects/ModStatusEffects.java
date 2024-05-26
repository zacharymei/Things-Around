package projects.mods.ta.base.status_effects;

import net.fabricmc.api.ModInitializer;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModStatusEffects implements ModInitializer {

    public static final StatusEffect FOREST_STATUS_EFFECT = new ForestStatusEffect();

    @Override
    public void onInitialize() {
        Registry.register(Registries.STATUS_EFFECT, new Identifier("things-around", "forest_around"), FOREST_STATUS_EFFECT);
    }
}
