package net.glasslauncher.mod.machineutils.impl.common.item;

import net.glasslauncher.mod.machineutils.api.energy.ChargeableItem;
import net.glasslauncher.mod.machineutils.api.energy.EnergyTier;
import net.glasslauncher.mod.machineutils.impl.common.MachineUtils;
import net.glasslauncher.mod.machineutils.impl.common.MachineUtilsIdentifier;
import net.glasslauncher.mod.machineutils.impl.common.PlatformUtils;
import net.minecraft.entity.EntityBase;
import net.minecraft.entity.player.PlayerBase;
import net.minecraft.item.ItemInstance;
import net.minecraft.util.io.CompoundTag;
import net.modificationstation.stationapi.api.item.nbt.StationNBT;
import net.modificationstation.stationapi.api.registry.Identifier;


public interface UsableChargeableItem extends ChargeableItem {

    default boolean use(ItemInstance itemstack, int energyUse, EntityBase wieldingEntity) {
        CompoundTag compoundTag = StationNBT.cast(itemstack).getStationNBT();
        if (compoundTag.getInt(MachineUtilsIdentifier.NBT_ENERGY.toString()) < energyUse) {
            return false;
        }
        if (PlatformUtils.isSimulating() && wieldingEntity instanceof PlayerBase) {
            compoundTag.put(MachineUtilsIdentifier.NBT_ENERGY.toString(), compoundTag.getInt(MachineUtilsIdentifier.NBT_ENERGY.toString()) - energyUse);
            MachineUtils.chargeFromBatPack(itemstack, (PlayerBase) wieldingEntity);
        }
        return true;
    }
}
