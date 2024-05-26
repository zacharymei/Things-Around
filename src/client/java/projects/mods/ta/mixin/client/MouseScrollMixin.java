package projects.mods.ta.mixin.client;

import net.minecraft.client.Mouse;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import projects.mods.ta.event.MouseScrollCallback;

@Mixin(Mouse.class)
public class MouseScrollMixin {

    @Inject(method = "onMouseScroll", at = @At("HEAD"), cancellable = true)
    private void onScroll(long window, double horizontal, double vertical, CallbackInfo info){
        Boolean holding = MouseScrollCallback.EVENT.invoker().on_scroll(window, horizontal, vertical);

        if(holding) info.cancel();
    }

}
