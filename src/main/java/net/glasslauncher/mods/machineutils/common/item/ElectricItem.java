// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode 

package net.glasslauncher.mods.machineutils.common.item;

import net.glasslauncher.mods.machineutils.api.energy.IChargeableItem;
import net.glasslauncher.mods.machineutils.common.PlatformUtils;
import net.minecraft.entity.player.PlayerBase;
import net.minecraft.item.ItemBase;
import net.minecraft.item.ItemInstance;

// Referenced classes of package ic2.common:
//            ItemIC2, IChargeableItem, ItemElectricTool

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
    public int giveEnergyTo(ItemInstance itemstack, int i, int j, boolean flag) {
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
        ItemElectricTool.chargeFromBatpack(itemstack, entityplayer);
        if (itemstack.getDamage() + i > itemstack.method_723() - 1) {
            return false;
        }
        if (PlatformUtils.isSimulating()) {
            itemstack.setDamage(itemstack.getDamage() + i);
            ItemElectricTool.chargeFromBatpack(itemstack, entityplayer);
        }
        return true;
    }
}
