// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode 

package net.glasslauncher.mods.machineutils.common.item;

import net.glasslauncher.mods.machineutils.api.energy.IChargeableItem;
import net.minecraft.item.ItemBase;
import net.minecraft.item.ItemInstance;
import net.minecraft.item.armour.Armour;
import net.modificationstation.stationloader.api.client.item.ArmorTextureProvider;

// Referenced classes of package ic2.common:
//            ItemArmorBackpack, IChargeableItem

public class ItemArmorBatpack extends Armour
    implements IChargeableItem, ArmorTextureProvider
{

    String texture;

    @Override
    public String getChestplateModelTexture(ItemInstance itemInstance) {
        return "/assets/ic2/armor/" + texture + "_2.png";
    }

    @Override
    public String getOtherModelTexture(ItemInstance itemInstance) {
        return "/assets/ic2/armor/" + texture + "_1.png";
    }

    public ItemArmorBatpack(int i, int j, String texture)
    {
        super(i, j, 0, 1);
        this.texture = texture;
        tier = 1;
        sendTier = 1;
        ratio = 2;
        transfer = 100;
        setDurability(30002);
    }

    public void useBatpackOn(ItemInstance itemstack, ItemInstance itemstack1)
    {
        if(!(ItemBase.byId[itemstack.itemId] instanceof IChargeableItem))
        {
            return;
        }
        IChargeableItem ichargeableitem = (IChargeableItem)ItemBase.byId[itemstack.itemId];
        int i = (itemstack1.method_723() - itemstack1.getDamage() - 1) * ratio;
        if(i <= 0)
        {
            return;
        } else
        {
            i -= ichargeableitem.giveEnergyTo(itemstack, i, sendTier, true);
            itemstack1.setDamage(itemstack1.method_723() - 1 - i / ratio);
            return;
        }
    }

    public int giveEnergyTo(ItemInstance itemstack, int i, int j, boolean flag)
    {
        if(j < tier || itemstack.getDamage() == 1)
        {
            return 0;
        }
        int k = (itemstack.getDamage() - 1) * ratio;
        if(!flag && transfer != 0 && i > transfer)
        {
            i = transfer;
        }
        if(k < i)
        {
            i = k;
        }
        for(; i % ratio != 0; i--) { }
        itemstack.setDamage(itemstack.getDamage() - i / ratio);
        return i;
    }

    public int tier;
    public int sendTier;
    public int ratio;
    public int transfer;
}
