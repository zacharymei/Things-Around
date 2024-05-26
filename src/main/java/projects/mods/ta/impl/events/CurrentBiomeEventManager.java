package projects.mods.ta.impl.events;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.World;

import projects.mods.ta.impl.AroundsType;
import projects.mods.ta.impl.events.biome.BiomeEvent;
import projects.mods.ta.impl.events.biome.BiomeEventInstance;
import projects.mods.ta.network.BiomeEventsNetwork;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class CurrentBiomeEventManager {

    World world;
    static List<CurrentBiomeEventManager> world_biome_event_managers = Lists.newArrayList();
    static BiomeEventsNetwork network = new BiomeEventsNetwork();

    int max_events_number = 3;
    protected Map<AroundsType, List<BiomeEventInstance>> current_events = Maps.newConcurrentMap();

    private CurrentBiomeEventManager(World world){
        this.world = world;
        world_biome_event_managers.add(this);
    }


    public static CurrentBiomeEventManager getManager(World world){
        Optional<CurrentBiomeEventManager> manager = world_biome_event_managers.stream().filter(e->e.world.getRegistryKey().getValue().equals(world.getRegistryKey().getValue()) && e.world.isClient() == world.isClient()).findFirst();
        return manager.orElseGet(() -> new CurrentBiomeEventManager(world));
    }

    public CurrentBiomeEventManager max_events(int number){
        if(!world.isClient()){
            this.max_events_number = number;
            // tbw sync
        }
        return this;
    }


    public boolean register(BiomeEvent event){

        if(!world.isClient()){
            if(isHappening(event)) return false;

            if(getOrCreateList(event.getType()).size() == max_events_number) return false;

            BiomeEventInstance event_instance = event.createEvent(world).setEventActive(true);
            boolean bl = getOrCreateList(event.getType()).add(event_instance);
            if(bl) notifyChange(event.getType(), getOrCreateList(event.getType()));
            return bl;
        }
        return false;

    }

    public void timeout(BiomeEvent event){
        if(!world.isClient() && isHappening(event)){
            List<BiomeEventInstance> list = getOrCreateList(event.getType());

            boolean removed = false;
            for(BiomeEventInstance in: list){
                if(in.getEvent().is(event)){
                    in.setEventActive(false);
                    list.remove(in);
                    removed = true;
                    break;
                }
            }
            if(removed) notifyChange(event.getType(), list);


        }

    }

    public List<BiomeEventInstance> getOrCreateList(AroundsType type){
        if(type == null) return Lists.newArrayList();
        if(!current_events.containsKey(type)){
            current_events.put(type, Lists.newArrayList());
        }
        return current_events.get(type);
    }

    public boolean isHappening(BiomeEvent event){
        for(BiomeEventInstance instance: getOrCreateList(event.getType())){
            if(instance.getEvent().is(event)) return true;
        }
        return false;
    }

    public void update(AroundsType type, List<BiomeEventInstance> list){
        if(world.isClient()){
            current_events.put(type, list);
        }
    }

    void notifyChange(AroundsType type, List<BiomeEventInstance> list){
        if(!world.isClient()){
            network.updateS2CCurrentBiomeEvents((ServerWorld) world, type, list);
        }

    }



}
