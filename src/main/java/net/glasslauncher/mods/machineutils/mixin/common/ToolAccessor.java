package net.glasslauncher.mods.machineutils.mixin.common;

import net.minecraft.item.tool.ToolBase;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(ToolBase.class)
public interface ToolAccessor {

    @Accessor
    void setField_2713(float f);

    @Accessor
    void setField_2714(int i);
}
