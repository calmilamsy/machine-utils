// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode 

package net.glasslauncher.mods.machineutils.common.tileentity;

import net.minecraft.entity.player.PlayerBase;
import net.minecraft.inventory.InventoryBase;
import net.minecraft.item.ItemInstance;
import net.minecraft.util.io.CompoundTag;
import net.minecraft.util.io.ListTag;

// Referenced classes of package ic2.common:
//            TileEntityBlock

/**
 * Extend this when making custom machines.
 * This handles all saving, networking, rotation and wrench events for you for more simple custom machines.
 */
public abstract class MachineBase extends WrenchableMachineTileEntity
    implements InventoryBase
{

    public MachineBase(int i)
    {
        inventory = new ItemInstance[i];
    }

    public int getInventorySize()
    {
        return inventory.length;
    }

    public ItemInstance getInventoryItem(int i)
    {
        return inventory[i];
    }

    public ItemInstance takeInventoryItem(int i, int j)
    {
        if(inventory[i] != null)
        {
            if(inventory[i].count <= j)
            {
                ItemInstance itemstack = inventory[i];
                inventory[i] = null;
                return itemstack;
            }
            ItemInstance itemstack1 = inventory[i].split(j);
            if(inventory[i].count == 0)
            {
                inventory[i] = null;
            }
            return itemstack1;
        } else
        {
            return null;
        }
    }

    public void setInventoryItem(int i, ItemInstance itemstack)
    {
        inventory[i] = itemstack;
        if(itemstack != null && itemstack.count > getMaxItemCount())
        {
            itemstack.count = getMaxItemCount();
        }
    }

    public int getMaxItemCount()
    {
        return 64;
    }

    public boolean canPlayerUse(PlayerBase entityplayer)
    {
        if(level.getTileEntity(x, y, z) != this)
        {
            return false;
        } else
        {
            return entityplayer.method_1350((double)x + 0.5D, (double)y + 0.5D, (double)z + 0.5D) <= 64D;
        }
    }

    public abstract String getContainerName();

    public void readIdentifyingData(CompoundTag nbttagcompound)
    {
        super.readIdentifyingData(nbttagcompound);
        ListTag nbttaglist = nbttagcompound.getListTag("Items");
        inventory = new ItemInstance[getInventorySize()];
        for(int i = 0; i < nbttaglist.size(); i++)
        {
            CompoundTag nbttagcompound1 = (CompoundTag)nbttaglist.get(i);
            byte byte0 = nbttagcompound1.getByte("Slot");
            if(byte0 >= 0 && byte0 < inventory.length)
            {
                inventory[byte0] = new ItemInstance(nbttagcompound1);
            }
        }

    }

    public void writeIdentifyingData(CompoundTag nbttagcompound)
    {
        super.writeIdentifyingData(nbttagcompound);
        ListTag nbttaglist = new ListTag();
        for(int i = 0; i < inventory.length; i++)
        {
            if(inventory[i] != null)
            {
                CompoundTag nbttagcompound1 = new CompoundTag();
                nbttagcompound1.put("Slot", (byte)i);
                inventory[i].toTag(nbttagcompound1);
                nbttaglist.add(nbttagcompound1);
            }
        }

        nbttagcompound.put("Items", nbttaglist);
    }

    public ItemInstance inventory[];
}
