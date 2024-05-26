package projects.mods.ta.event;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;

public interface PlayerJumpCallback {

    Event<PlayerJumpCallback> EVENT = EventFactory.createArrayBacked(PlayerJumpCallback.class,
            (listeners)->(PlayerEntity player)->{

                for(PlayerJumpCallback listener: listeners){
                    listener.happen(player);
                }

            });


    void happen(PlayerEntity player);
}
