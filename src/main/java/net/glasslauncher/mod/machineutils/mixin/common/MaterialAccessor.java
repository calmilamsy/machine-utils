package net.glasslauncher.mod.machineutils.mixin.common;

import net.minecraft.block.material.Material;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(Material.class)
public interface MaterialAccessor {

    @Invoker
    Material invokeSetOpaque();

    @Invoker
    Material invokeRequiresTool();

    @Invoker
    Material invokeBurnable();
}
