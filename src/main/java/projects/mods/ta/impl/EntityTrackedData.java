package projects.mods.ta.impl;

import net.minecraft.entity.data.TrackedDataHandler;
import net.minecraft.util.Identifier;

import java.util.Objects;

public class EntityTrackedData<T> {

    private final Identifier identifier;
    private final TrackedDataHandler<T> handler;

    EntityTrackedData(Identifier identifier, TrackedDataHandler<T> handler){
        this.identifier = identifier;
        this.handler = handler;
    }

    public Identifier identifier(){
        return this.identifier;
    }



    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof EntityTrackedData<?> that)) return false;
        return identifier.equals(that.identifier);
    }

    @Override
    public int hashCode() {
        return identifier.hashCode();
    }
}
