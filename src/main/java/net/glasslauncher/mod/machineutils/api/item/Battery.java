package net.glasslauncher.mod.machineutils.api.item;

import net.glasslauncher.mod.machineutils.api.energy.EnergyTier;
import net.glasslauncher.mod.machineutils.api.energy.ChargeableItem;
import net.minecraft.item.ItemInstance;
import net.modificationstation.stationapi.api.registry.Identifier;
import net.modificationstation.stationapi.api.template.item.TemplateItemBase;

public interface Battery extends ChargeableItem {

    default int getEnergyFrom(ItemInstance itemstack, int energyRequested, EnergyTier machineTier) {
        if (machineTier.worseThan(getTier()) || getEnergy(itemstack) <= 0) {
            return 0;
        }
        int removedEnergy = getEnergy(itemstack);
        if (energyRequested > removedEnergy) {
            energyRequested = removedEnergy;
        }
        int transfer = getTransfer();
        if (transfer != 0 && transfer < removedEnergy) {
            removedEnergy = Math.min(transfer, energyRequested);
        }
        addEnergy(itemstack, -removedEnergy);
        return removedEnergy;
    }
}
