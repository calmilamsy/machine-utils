package net.glasslauncher.mod.machineutils.impl.common.block;

import net.glasslauncher.mod.machineutils.mixin.common.MaterialAccessor;
import net.minecraft.block.BlockSounds;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColour;

public class MachineMaterial extends Material {

    public static final Material MACHINE_BASIC = new MachineMaterial(MaterialColour.IRON).requiresTool();
    public static final BlockSounds MACHINE_SOUNDS = new BlockSounds("stone", 1, 1.2f);

    public MachineMaterial(MaterialColour arg) {
        super(arg);
    }

    public MachineMaterial requiresTool() {
        return (MachineMaterial) ((MaterialAccessor) this).invokeRequiresTool();
    }

    public MachineMaterial burnable() {
        return (MachineMaterial) ((MaterialAccessor) this).invokeBurnable();
    }

    public MachineMaterial setOpaque() {
        return (MachineMaterial) ((MaterialAccessor) this).invokeSetOpaque();
    }

    public MachineMaterial breaksWhenPushed() {
        return (MachineMaterial) super.breaksWhenPushed();
    }

    public MachineMaterial unpushable() {
        return (MachineMaterial) super.unpushable();
    }
}
