package net.glasslauncher.mod.machineutils.test;

import net.glasslauncher.mod.machineutils.api.energy.EnergyTier;
import net.glasslauncher.mod.machineutils.impl.common.MachineUtilsIdentifier;
import net.glasslauncher.mod.machineutils.impl.common.item.UsableChargeableItem;
import net.minecraft.block.BlockBase;
import net.minecraft.entity.Living;
import net.minecraft.entity.player.PlayerBase;
import net.minecraft.item.ItemInstance;
import net.minecraft.item.tool.Pickaxe;
import net.minecraft.item.tool.ToolMaterial;
import net.minecraft.level.Level;
import net.modificationstation.stationapi.api.item.nbt.StationNBT;
import net.modificationstation.stationapi.api.item.tool.ToolLevel;
import net.modificationstation.stationapi.api.registry.Identifier;
import net.modificationstation.stationapi.api.template.item.TemplateItemBase;
import net.modificationstation.stationapi.api.template.item.tool.TemplatePickaxe;

public class ElectricDrill extends TemplateItemBase implements UsableChargeableItem, ToolLevel {
    private final EnergyTier tier;
    private final int storageMultiplier;

    /**
     * @param identifier
     * @param tier
     * @param storageMultiplier storage is this *10000, so pick appropriately. Approximate to IC2's values.
     */
    public ElectricDrill(Identifier identifier, EnergyTier tier, int storageMultiplier) {
        super(identifier);
        this.tier = tier;
        this.storageMultiplier = storageMultiplier;
        maxStackSize = 1;
    }

    @Override
    public EnergyTier getTier() {
        return tier;
    }

    @Override
    public boolean postMine(ItemInstance arg, int i, int j, int k, int l, Living arg2) {
        return use(arg, 1000, arg2);
    }

    @Override
    public float getStrengthOnBlock(ItemInstance arg, BlockBase arg2) {
        if (getEnergy(arg) < 1000) {
            return 1.0F;
        }
        return getMaterial(arg).getMiningSpeed();
    }

    @Override
    public ItemInstance use(ItemInstance arg, Level arg2, PlayerBase arg3) {
        StationNBT.cast(arg).getStationNBT().put(MachineUtilsIdentifier.NBT_ENERGY.toString(), getMaxEnergy());
        return super.use(arg, arg2, arg3);
    }

    @Override
    public int getStorageMultiplier() {
        return storageMultiplier;
    }

    @Override
    public ToolMaterial getMaterial(ItemInstance itemInstance) {
        return ToolMaterial.field_1691;
    }
}
