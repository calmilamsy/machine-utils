package net.glasslauncher.mods.machineutils.common;

import net.glasslauncher.mods.machineutils.api.Direction;
import net.minecraft.entity.Item;
import net.minecraft.inventory.DoubleChest;
import net.minecraft.inventory.InventoryBase;
import net.minecraft.item.ItemInstance;
import net.minecraft.level.Level;
import net.minecraft.tileentity.TileEntityBase;
import net.minecraft.tileentity.TileEntityChest;

import java.util.Iterator;
import java.util.List;

public class StackUtil {

    public static void distributeDrop(TileEntityBase tileentity, List list) {
        Direction[] adirection = Direction.values();
        int i = adirection.length;
        label0:
        for (int j = 0; j < i; j++) {
            Direction direction = adirection[j];
            if (list.isEmpty()) {
                break;
            }
            TileEntityBase tileentity1 = direction.applyToTileEntity(tileentity);
            if (!(tileentity1 instanceof InventoryBase)) {
                continue;
            }
            Object obj = tileentity1;
            if (((InventoryBase) (obj)).getInventorySize() < 18) {
                continue;
            }
            if (tileentity1 instanceof TileEntityChest) {
                Direction[] adirection1 = Direction.values();
                int k = adirection1.length;
                for (int l = 0; l < k; l++) {
                    Direction direction1 = adirection1[l];
                    if (direction1 == Direction.YN || direction1 == Direction.YP) {
                        continue;
                    }
                    TileEntityBase tileentity2 = direction1.applyToTileEntity(tileentity1);
                    if (!(tileentity2 instanceof TileEntityChest)) {
                        continue;
                    }
                    obj = new DoubleChest("", ((InventoryBase) (obj)), (InventoryBase) tileentity2);
                    break;
                }

            }
            Iterator iterator1 = list.iterator();
            do {
                ItemInstance itemstack1;
                do {
                    do {
                        if (!iterator1.hasNext()) {
                            continue label0;
                        }
                        itemstack1 = (ItemInstance) iterator1.next();
                    } while (itemstack1 == null);
                    putInInventory(((InventoryBase) (obj)), itemstack1);
                } while (itemstack1.count != 0);
                iterator1.remove();
            } while (true);
        }

        ItemInstance itemstack;
        for (Iterator iterator = list.iterator(); iterator.hasNext(); dropAsEntity(tileentity.level, tileentity.x, tileentity.y, tileentity.z, itemstack)) {
            itemstack = (ItemInstance) iterator.next();
        }

        list.clear();
    }

    public static void getFromInventory(InventoryBase iinventory, ItemInstance itemstack) {
        int i = itemstack.count;
        itemstack.count = 0;
        for (int j = 0; j < iinventory.getInventorySize(); j++) {
            ItemInstance itemstack1 = iinventory.getInventoryItem(j);
            if (itemstack1 == null || !itemstack1.isDamageAndIDIdentical(itemstack)) {
                continue;
            }
            int k = Math.min(i, itemstack1.count);
            i -= k;
            itemstack1.count -= k;
            itemstack.count += k;
            if (itemstack1.count == 0) {
                iinventory.setInventoryItem(j, null);
            }
            if (i == 0) {
                return;
            }
        }

    }

    public static void putInInventory(InventoryBase iinventory, ItemInstance itemstack) {
        for (int i = 0; i < iinventory.getInventorySize(); i++) {
            ItemInstance itemstack1 = iinventory.getInventoryItem(i);
            if (itemstack1 == null || !itemstack1.isDamageAndIDIdentical(itemstack)) {
                continue;
            }
            int k = Math.min(itemstack.count, itemstack1.getMaxStackSize() - itemstack1.count);
            itemstack1.count += k;
            itemstack.count -= k;
            if (itemstack.count == 0) {
                return;
            }
        }

        for (int j = 0; j < iinventory.getInventorySize(); j++) {
            ItemInstance itemstack2 = iinventory.getInventoryItem(j);
            if (itemstack2 != null) {
                continue;
            }
            int l = Math.min(itemstack.count, itemstack.getMaxStackSize());
            iinventory.setInventoryItem(j, new ItemInstance(itemstack.itemId, l, itemstack.getDamage()));
            itemstack.count -= l;
            if (itemstack.count == 0) {
                return;
            }
        }

    }

    public static void dropAsEntity(Level world, int i, int j, int k, ItemInstance itemstack) {
        if (itemstack == null) {
            return;
        } else {
            double d = 0.69999999999999996D;
            double d1 = (double) world.rand.nextFloat() * d + (1.0D - d) * 0.5D;
            double d2 = (double) world.rand.nextFloat() * d + (1.0D - d) * 0.5D;
            double d3 = (double) world.rand.nextFloat() * d + (1.0D - d) * 0.5D;
            Item entityitem = new Item(world, (double) i + d1, (double) j + d2, (double) k + d3, itemstack);
            entityitem.pickupDelay = 10;
            world.spawnEntity(entityitem);
            return;
        }
    }
}
