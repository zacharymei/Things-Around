package projects.mods.ta.impl.arounds;

import net.minecraft.entity.player.PlayerEntity;
import projects.mods.ta.impl.AroundsType;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class AroundsLevelTracker {

    private static final Map<UUID, PlayerEntry> PLAYER_MAP = new ConcurrentHashMap<>();

    public static PlayerEntry getPlayer(PlayerEntity player){
        UUID id = player.getUuid();
        if(!PLAYER_MAP.containsKey(id)) PLAYER_MAP.put(id, new PlayerEntry(id));
        return PLAYER_MAP.get(id);
    }

    public static int getPlayerLevel(PlayerEntity player, AroundsType type){
        if(PLAYER_MAP.containsKey(player.getUuid())){
            return PLAYER_MAP.get(player.getUuid()).addLevel(type, 0);
        }
        return 0;
    }

    public static class PlayerEntry{

        private final UUID player_id;
        private final Map<AroundsType, Integer> arounds_level = new HashMap<>();

        PlayerEntry(UUID id){
            this.player_id = id;
        }

        public int addLevel(AroundsType type, int amount){
            return this.arounds_level.compute(type, (k, v)->(v != null)? v+amount : amount);
        }

        public void setLevel(AroundsType type, int level){
            this.arounds_level.put(type, level);
        }






    }

}
