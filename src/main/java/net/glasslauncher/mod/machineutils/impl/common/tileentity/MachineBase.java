package net.glasslauncher.mod.machineutils.impl.common.tileentity;

import net.minecraft.entity.player.PlayerBase;
import net.minecraft.inventory.InventoryBase;
import net.minecraft.item.ItemInstance;
import net.minecraft.util.io.CompoundTag;
import net.minecraft.util.io.ListTag;



/**
 * Extend this when making custom machines.
 * This handles all saving, networking, rotation and wrench events for you for more simple custom machines.
 */
public abstract class MachineBase extends WrenchableMachineTileEntity
        implements InventoryBase {

    public ItemInstance[] inventory;

    public MachineBase(int i) {
        inventory = new ItemInstance[i];
    }

    @Override
    public int getInventorySize() {
        return inventory.length;
    }

    @Override
    public ItemInstance getInventoryItem(int i) {
        return inventory[i];
    }

    @Override
    public ItemInstance takeInventoryItem(int i, int j) {
        if (inventory[i] != null) {
            if (inventory[i].count <= j) {
                ItemInstance itemstack = inventory[i];
                inventory[i] = null;
                return itemstack;
            }
            ItemInstance itemstack1 = inventory[i].split(j);
            if (inventory[i].count == 0) {
                inventory[i] = null;
            }
            return itemstack1;
        } else {
            return null;
        }
    }

    @Override
    public void setInventoryItem(int i, ItemInstance itemstack) {
        inventory[i] = itemstack;
        if (itemstack != null && itemstack.count > getMaxItemCount()) {
            itemstack.count = getMaxItemCount();
        }
    }

    @Override
    public int getMaxItemCount() {
        return 64;
    }

    @Override
    public boolean canPlayerUse(PlayerBase entityplayer) {
        if (level.getTileEntity(x, y, z) != this) {
            return false;
        } else {
            return entityplayer.distanceTo((double) x + 0.5D, (double) y + 0.5D, (double) z + 0.5D) <= 64D;
        }
    }

    @Override
    public abstract String getContainerName();

    @Override
    public void readIdentifyingData(CompoundTag nbttagcompound) {
        super.readIdentifyingData(nbttagcompound);
        ListTag nbttaglist = nbttagcompound.getListTag("Items");
        inventory = new ItemInstance[getInventorySize()];
        for (int i = 0; i < nbttaglist.size(); i++) {
            CompoundTag nbttagcompound1 = (CompoundTag) nbttaglist.get(i);
            byte byte0 = nbttagcompound1.getByte("Slot");
            if (byte0 >= 0 && byte0 < inventory.length) {
                inventory[byte0] = new ItemInstance(nbttagcompound1);
            }
        }

    }

    @Override
    public void writeIdentifyingData(CompoundTag nbttagcompound) {
        super.writeIdentifyingData(nbttagcompound);
        ListTag nbttaglist = new ListTag();
        for (int i = 0; i < inventory.length; i++) {
            if (inventory[i] != null) {
                CompoundTag nbttagcompound1 = new CompoundTag();
                nbttagcompound1.put("Slot", (byte) i);
                inventory[i].toTag(nbttagcompound1);
                nbttaglist.add(nbttagcompound1);
            }
        }

        nbttagcompound.put("Items", nbttaglist);
    }
}
