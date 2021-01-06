package net.glasslauncher.mods.machineutils.mixin.client;

import net.minecraft.client.sound.SoundHelper;
import net.minecraft.client.sound.SoundMap;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(SoundHelper.class)
public interface SoundHelperAccessor {

    @Accessor
    SoundMap getSounds();
}
