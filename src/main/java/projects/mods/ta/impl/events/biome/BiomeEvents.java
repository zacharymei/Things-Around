package projects.mods.ta.impl.events.biome;

import com.google.common.collect.Maps;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;
import projects.mods.ta.impl.AroundsType;

import java.util.Map;

public class BiomeEvents {

    static Map<Identifier, BiomeEvent> biome_events = Maps.newHashMap();

    public static final ForestEvent HEALING_ARROW_EVENT = register(Identifier.of("things-around", "forest/healing_arrow"), new HealingArrowForestEvent());
    public static final ForestEvent FOREST_HARVEST_EVENT = register(Identifier.of("things-around", "forest/harvest"), new ForestHarvestEvent());
    public static final ForestEvent FOREST_RAINY_DAY = register(Identifier.of("things-around", "forest/rainy_day"), new RainyDayForestEvent());
    public static final ForestEvent FOREST_TOUGH_BARK_EVENT = register(Identifier.of("things-around", "forest/tough_bark"), new ToughBarkForestEvent());

    public static <E extends BiomeEvent> E register(Identifier id, E biome_event){
        biome_events.put(id, biome_event);
        return biome_event;
    }

    @Nullable
    public static <E extends BiomeEvent> Identifier getEventId(E biome_event){
        return biome_events.entrySet().stream().filter((e)-> e.getValue().is(biome_event)).findAny().map(Map.Entry::getKey).orElse(null);
    }

    public static BiomeEvent getEvent(Identifier event_id){
        return biome_events.get(event_id);
    }

    public static Map<Identifier, BiomeEvent> getAllAvailableEvents(){
        return biome_events;
    }

}
