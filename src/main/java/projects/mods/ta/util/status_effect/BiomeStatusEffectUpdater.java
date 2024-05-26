package projects.mods.ta.util.status_effect;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.world.biome.Biome;
import projects.mods.ta.base.status_effects.ModStatusEffects;
import projects.mods.ta.event.PlayerTickCallback;
import projects.mods.ta.impl.AroundsType;
import projects.mods.ta.impl.events.biome.BiomeEvents;
import projects.mods.ta.impl.events.CurrentBiomeEventManager;
import projects.mods.ta.impl.events.biome.ForestEvent;

public class BiomeStatusEffectUpdater implements PlayerTickCallback.TickMovement {


    @Override
    public void tickMovement(PlayerEntity player) {
        RegistryEntry<Biome> biome = player.getWorld().getBiome(player.getBlockPos());
        if(AroundsType.getType(biome) == AroundsType.FOREST){
            if(!player.hasStatusEffect(ModStatusEffects.FOREST_STATUS_EFFECT)) player.addStatusEffect(new StatusEffectInstance(ModStatusEffects.FOREST_STATUS_EFFECT, -1, 1, false, false));



        }
        else{
            player.removeStatusEffect(ModStatusEffects.FOREST_STATUS_EFFECT);
        }
    }
}
