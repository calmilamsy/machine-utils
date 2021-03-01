package net.glasslauncher.mods.machineutils.api.item;

import net.glasslauncher.mods.machineutils.api.energy.IChargeableItem;
import net.minecraft.item.ItemInstance;

public interface BatteryArmour extends IChargeableItem {

    default void useBatPackOn(ItemInstance itemToCharge, ItemInstance itemToDrain) {
        if (!(itemToCharge.getType() instanceof IChargeableItem)) {
            return;
        }
        IChargeableItem ichargeableitem = (IChargeableItem) itemToCharge.getType();
        int energyToTransfer = getTransfer(itemToDrain);
        if (energyToTransfer > 0) {
            energyToTransfer = ichargeableitem.giveEnergyToOther(itemToCharge, energyToTransfer, getTier(itemToDrain), true);
            ((BatteryArmour) itemToDrain.getType()).addEnergy(itemToDrain, -energyToTransfer);
        }
    }
}
