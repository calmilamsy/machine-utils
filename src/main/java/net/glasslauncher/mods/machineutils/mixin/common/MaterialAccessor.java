package net.glasslauncher.mods.machineutils.mixin.common;

import net.minecraft.block.material.Material;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(Material.class)
public interface MaterialAccessor {

    @Invoker
    Material invokeMethod_902();

    @Invoker
    Material invokeRequiresTool();

    @Invoker
    Material invokeBurnable();
}
