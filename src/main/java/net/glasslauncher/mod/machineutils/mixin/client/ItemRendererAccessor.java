package net.glasslauncher.mod.machineutils.mixin.client;

import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.entity.ItemRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(ItemRenderer.class)
public interface ItemRendererAccessor {

    @Invoker
    void invokeMethod_1485(Tessellator tessellator, int i, int j, int k, int l, int m);
}
