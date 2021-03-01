package net.glasslauncher.mod.machineutils.common.item;

import net.glasslauncher.mod.machineutils.api.energy.IChargeableItem;
import net.glasslauncher.mod.machineutils.common.MachineUtils;
import net.glasslauncher.mod.machineutils.common.PlatformUtils;
import net.minecraft.entity.player.PlayerBase;
import net.minecraft.item.ItemBase;
import net.minecraft.item.ItemInstance;



public abstract class ElectricItem extends ItemBase
        implements IChargeableItem {

    public int tier;
    public int ratio;
    public int transfer;

    public ElectricItem(int i, int j, int k, int l, int i1) {
        super(i);
        setTexturePosition(j);
        tier = k;
        ratio = l;
        transfer = i1;
    }

    @Override
    public int giveEnergyToOther(ItemInstance itemstack, int i, int j, boolean flag) {
        if (j < tier || itemstack.getDamage() == 1) {
            return 0;
        }
        int k = (itemstack.getDamage() - 1) * ratio;
        if (!flag && transfer != 0 && i > transfer) {
            i = transfer;
        }
        if (k < i) {
            i = k;
        }
        for (; i % ratio != 0; i--) {
        }
        itemstack.setDamage(itemstack.getDamage() - i / ratio);
        return i;
    }

    public boolean use(ItemInstance itemstack, int i, PlayerBase entityplayer) {
        MachineUtils.chargeFromBatPack(itemstack, entityplayer);
        if (itemstack.getDamage() + i > itemstack.getDurability() - 1) {
            return false;
        }
        if (PlatformUtils.isSimulating()) {
            itemstack.setDamage(itemstack.getDamage() + i);
            MachineUtils.chargeFromBatPack(itemstack, entityplayer);
        }
        return true;
    }

    @Override
    public int getMaxEnergy(ItemInstance itemInstance) {
        return ratio * 10000;
    }

    @Override
    public int getTier(ItemInstance itemInstance) {
        return tier;
    }
}
