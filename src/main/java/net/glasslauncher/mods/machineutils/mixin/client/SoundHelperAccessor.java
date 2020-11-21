package net.glasslauncher.mods.machineutils.mixin.client;

import net.minecraft.client.sound.SoundHelper;
import net.minecraft.util.SoundMap;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(SoundHelper.class)
public interface SoundHelperAccessor {

    @Accessor("soundMapMusic")
    SoundMap getSoundMapSounds();
}
