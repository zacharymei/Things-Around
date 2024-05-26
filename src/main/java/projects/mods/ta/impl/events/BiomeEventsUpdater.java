package projects.mods.ta.impl.events;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.World;
import net.minecraft.world.level.LevelProperties;
import projects.mods.ta.event.ServerWorldCallback;
import projects.mods.ta.impl.events.biome.BiomeEvents;

public class BiomeEventsUpdater implements ServerWorldCallback.RainState {

    public BiomeEventsUpdater(){
        ServerWorldCallback.RAINING_STATE.register(this);
    }


    @Override
    public void onRainStateChange(ServerWorld world, boolean is_raining) {
        CurrentBiomeEventManager manager = CurrentBiomeEventManager.getManager(world);
        if(is_raining){
            manager.register(BiomeEvents.FOREST_RAINY_DAY);
        }else{
            manager.timeout(BiomeEvents.FOREST_RAINY_DAY);
        }
    }
}
