// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode 

package net.glasslauncher.mods.machineutils.api.energy;

import net.minecraft.item.ItemInstance;
import net.modificationstation.stationloader.api.client.gui.HasCustomTooltip;
import net.modificationstation.stationloader.api.client.gui.Colors;

import java.util.ArrayList;
import java.util.List;

public interface IChargeableItem extends HasCustomTooltip
{

    int giveEnergyTo(ItemInstance itemstack, int i, int j, boolean flag);

    @Override
    default List<String> getToolTip(String string, ItemInstance itemInstance) {
        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add(string);
        arrayList.add(Colors.RED + ((itemInstance.getType().getDurability()-itemInstance.getDamage())) + Colors.WHITE + "/" + Colors.DARK_AQUA + itemInstance.getType().getDurability() + Colors.WHITE + " EU" + Colors.WHITE + " stored");
        return arrayList;
    }
}
