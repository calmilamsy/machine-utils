package net.glasslauncher.mod.machineutils.api.item;

import net.glasslauncher.mod.machineutils.api.energy.IChargeableItem;
import net.minecraft.item.ItemInstance;

public interface Battery extends IChargeableItem {

    default int getEnergyFrom(ItemInstance itemstack, int energyRequested, int machineTier) {
        if (machineTier < getTier(itemstack) || getEnergy(itemstack) <= 0) {
            return 0;
        }
        int removedEnergy = getEnergy(itemstack);
        if (energyRequested > removedEnergy) {
            energyRequested = removedEnergy;
        }
        int transfer = getTransfer(itemstack);
        if (transfer != 0 && transfer < removedEnergy) {
            removedEnergy = Math.min(transfer, energyRequested);
        }
        addEnergy(itemstack, -removedEnergy);
        return removedEnergy;
    }
}
