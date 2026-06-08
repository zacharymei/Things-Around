package mod.zacharymei.impl.events.biomeevent;

import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;

import java.util.Map;

public class ServerBiomeEventManager {

    public static final ServerBiomeEventUpdater updater = new ServerBiomeEventUpdater();
    public static final ServerBiomeEventData data = new ServerBiomeEventData();

    private static final Map<ResourceKey<Level>, ObjectOpenHashSet<BiomeEventInstance>> levelsBiomeEvents = new Object2ObjectOpenHashMap<>();

    public static void init(){

    }

    public static void start(ResourceKey<Level> level, BiomeEvent biomeEvent){
        start(level, biomeEvent, BiomeEvent.DEFAULT_DURATION);
    }

    public static void start(ResourceKey<Level> level, BiomeEvent biomeEvent, int durations) {
        if (levelsBiomeEvents.containsKey(level) && levelsBiomeEvents.get(level).stream().anyMatch(i->i.getBiomeEvent()==biomeEvent)) return;

        BiomeEventInstance biomeEventInstance = biomeEvent.getOrCreateInstance();
        biomeEventInstance.setDurations(durations);
        register(level, biomeEventInstance);
    }

    public static void end(ResourceKey<Level> level, BiomeEvent biomeEvent){
        ObjectOpenHashSet<BiomeEventInstance> biomeEventsSet = levelsBiomeEvents.get(level);
        if(biomeEventsSet == null || biomeEventsSet.isEmpty()) return;
        biomeEventsSet.removeIf(i->i.isBiomeEvent(biomeEvent));
    }

    private static void register(ResourceKey<Level> level, BiomeEventInstance biomeEventInstance){
        if (biomeEventInstance.getDurations() <= 0) return;
        ObjectOpenHashSet<BiomeEventInstance> biomeEventsSet = levelsBiomeEvents.getOrDefault(level, new ObjectOpenHashSet<>());
        biomeEventsSet.add(biomeEventInstance);
        levelsBiomeEvents.put(level, biomeEventsSet);
    }

    protected static void clean(){
        // TODO throw exceptions for levels not being saved
        levelsBiomeEvents.clear();
    }

    public static ObjectOpenHashSet<BiomeEvent> tick(ResourceKey<Level> level){
        if(!levelsBiomeEvents.containsKey(level)) return new ObjectOpenHashSet<>();
        ObjectOpenHashSet<BiomeEventInstance> biomeEventsSet = levelsBiomeEvents.getOrDefault(level, new ObjectOpenHashSet<>());
        ObjectOpenHashSet<BiomeEvent> timeoutEventsSet = biomeEventsSet.stream()
                .filter(i->i.getDurations() <= 1)
                .map(BiomeEventInstance::getBiomeEvent)
                .collect(ObjectOpenHashSet::new, ObjectOpenHashSet::add, ObjectOpenHashSet::addAll);
        biomeEventsSet.removeIf(i->i.getDurations() <= 1);
        biomeEventsSet.forEach(BiomeEventInstance::tick);
        timeoutEventsSet.forEach(e->{
            boardcast(level, e, 0);
            e.endInstance();
        });
        return timeoutEventsSet;
    }

    private static void boardcast(ResourceKey<Level> level, BiomeEvent biomeEvent, int message){

    }

    public static int getDurations(ResourceKey<Level> level, BiomeEvent biomeEvent){
        return levelsBiomeEvents.getOrDefault(level, new ObjectOpenHashSet<>()).stream()
                .filter(i->i.getBiomeEvent()==biomeEvent)
                .mapToInt(BiomeEventInstance::getDurations)
                .sum();
    }

    public static ObjectOpenHashSet<BiomeEventInstance> getActiveBiomeEvents(ResourceKey<Level> level){
        return levelsBiomeEvents.getOrDefault(level, new ObjectOpenHashSet<>());
    }

}
