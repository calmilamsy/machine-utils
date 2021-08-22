package net.glasslauncher.mod.machineutils.api.item;

import net.minecraft.util.io.CompoundTag;
import net.modificationstation.stationapi.api.item.nbt.ItemEntity;

public class ItemWithEnergy implements ItemEntity {
    public int energy;

    public ItemWithEnergy() {
        energy = 0;
    }

    public ItemWithEnergy(int energy) {
        this.energy = energy;
    }

    public ItemWithEnergy(CompoundTag compoundTag) {
        energy = compoundTag.getInt("machineutils:energy");
    }

    @Override
    public ItemEntity copy() {
        return new ItemWithEnergy(energy);
    }

    @Override
    public void writeToNBT(CompoundTag compoundTag) {
        compoundTag.put("machineutils:energy", energy);
    }
}
