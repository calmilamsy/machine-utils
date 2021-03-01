package net.glasslauncher.mod.machineutils.mixin.common;

import net.minecraft.entity.EntityBase;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(EntityBase.class)
public interface EntityBaseAccessor {

    @Accessor
    float getFallDistance();

    @Accessor
    void setFallDistance(float f);
}
