package net.glasslauncher.mod.machineutils.mixin.client;

import net.glasslauncher.mod.machineutils.event.ingame.TickManager;
import net.minecraft.client.Minecraft;
import net.minecraft.sortme.GameRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GameRenderer.class)
public class MixinGameRenderer {

    private long clock = 0L;

    @Shadow
    private Minecraft minecraft;

    @Inject(method = "method_1844", at = @At(value = "TAIL"))
    private void onTick(float delta, CallbackInfo ci) {
        long newClock = 0L;
        if (minecraft.level != null && minecraft.player != null) {
            newClock = minecraft.level.getLevelTime();
            if (newClock != clock) {
                TickManager.onTickInGame(minecraft.level.players);
            }
        }
        clock = newClock;
    }

}
