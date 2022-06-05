package net.glasslauncher.mod.machineutils.api.item;

import net.glasslauncher.mod.machineutils.api.energy.ChargeableItem;
import net.glasslauncher.mod.machineutils.api.energy.EnergyTier;
import net.minecraft.item.ItemInstance;
import net.modificationstation.stationapi.api.registry.Identifier;
import net.modificationstation.stationapi.api.template.item.armour.TemplateArmour;

public interface BatteryArmour extends ChargeableItem {

    default void useBatPackOn(ItemInstance itemToCharge, ItemInstance itemToDrain) {
        if (!(itemToCharge.getType() instanceof ChargeableItem interchangeable)) {
            return;
        }
        int energyToTransfer = getTransfer();
        if (energyToTransfer > 0) {
            energyToTransfer = interchangeable.giveEnergyToOther(itemToCharge, getTier(), energyToTransfer, true);
            ((BatteryArmour) itemToDrain.getType()).addEnergy(itemToDrain, -energyToTransfer);
        }
    }
}
