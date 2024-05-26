package projects.mods.ta.mixin;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import projects.mods.ta.event.PlayerJumpCallback;

@Mixin(PlayerEntity.class)
public abstract class EnterTerrainMixin {

    @Shadow public abstract void sendMessage(Text message, boolean overlay);

    @Inject(method = "jump", at = @At("HEAD"))
    private void onEnter(final CallbackInfo info){


        PlayerJumpCallback.EVENT.invoker().happen((PlayerEntity) (Object) this);

    }


}
