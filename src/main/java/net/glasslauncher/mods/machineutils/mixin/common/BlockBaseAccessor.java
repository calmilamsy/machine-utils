package net.glasslauncher.mods.machineutils.mixin.common;

import net.minecraft.block.BlockBase;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(BlockBase.class)
public interface BlockBaseAccessor {
    @Invoker
    BlockBase invokeSetBlastResistance(float f);

    @Invoker
    int invokeDroppedMeta(int i);
}
