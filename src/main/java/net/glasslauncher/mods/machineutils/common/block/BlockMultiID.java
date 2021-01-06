package net.glasslauncher.mods.machineutils.common.block;

import net.glasslauncher.mods.machineutils.common.PlatformUtils;
import net.glasslauncher.mods.machineutils.common.StackUtil;
import net.glasslauncher.mods.machineutils.common.tileentity.WrenchableMachineTileEntity;
import net.minecraft.block.BlockSounds;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Living;
import net.minecraft.entity.player.PlayerBase;
import net.minecraft.inventory.InventoryBase;
import net.minecraft.item.ItemInstance;
import net.minecraft.level.Level;
import net.minecraft.level.TileView;
import net.minecraft.tileentity.TileEntityBase;
import net.minecraft.util.maths.MathHelper;
import net.modificationstation.stationapi.api.common.registry.Identifier;

import java.util.ArrayList;
import java.util.Iterator;



public abstract class BlockMultiID extends BlockWithEntity {

    public static final int[][] sideAndFacingToSpriteOffset = {
            {
                    3, 5, 0, 0, 0, 0
            }, {
            5, 3, 1, 1, 1, 1
    }, {
            2, 2, 3, 2, 5, 4
    }, {
            1, 0, 4, 3, 2, 5
    }, {
            4, 4, 5, 4, 3, 2
    }, {
            0, 1, 2, 5, 4, 3
    }
    };

    protected BlockMultiID(int i, Material material) {
        super(i, material);
    }

    public static boolean isActive(TileView iblockaccess, int i, int j, int k) {
        TileEntityBase tileentity = iblockaccess.getTileEntity(i, j, k);
        if (tileentity instanceof WrenchableMachineTileEntity) {
            return ((WrenchableMachineTileEntity) tileentity).getActive();
        } else {
            return false;
        }
    }

    @Override
    public BlockMultiID setHardness(float f) {
        return (BlockMultiID) super.setHardness(f);
    }

    @Override
    public BlockMultiID setBlastResistance(float f) {
        return (BlockMultiID) super.setBlastResistance(f);
    }

    @Override
    public BlockMultiID sounds(BlockSounds sounds) {
        return (BlockMultiID) super.sounds(sounds);
    }

    @Override
    public int getTextureForSide(TileView iblockaccess, int i, int j, int k, int l) {
        TileEntityBase tileentity = iblockaccess.getTileEntity(i, j, k);
        short word0 = (tileentity instanceof WrenchableMachineTileEntity) ? ((WrenchableMachineTileEntity) tileentity).getFacing() : 0;
        int i1 = iblockaccess.getTileMeta(i, j, k);
        if (isActive(iblockaccess, i, j, k)) {
            return (i1 + (sideAndFacingToSpriteOffset[l][word0] + 6) * 16);
        } else {
            return (i1 + sideAndFacingToSpriteOffset[l][word0] * 16);
        }
    }

    @Override
    public int getTextureForSide(int i, int j) {
        return j + sideAndFacingToSpriteOffset[i][3] * 16;
    }

    @Override
    public boolean canUse(Level world, int i, int j, int k, PlayerBase entityplayer) {
        if (entityplayer.method_1373()) {
            return false;
        }
        Identifier identifier = getGui(world, i, j, k, entityplayer);
        if (identifier == null) {
            return false;
        }
        if (!PlatformUtils.isSimulating()) {
            return true;
        } else {
            return PlatformUtils.launchGUI(entityplayer, world.getTileEntity(i, j, k), identifier);
        }
    }

    public abstract Identifier getGui(Level world, int i, int j, int k, PlayerBase entityplayer);

    public ArrayList getBlockDropped(Level world, int i, int j, int k, int l) {
        ArrayList arraylist = new ArrayList();
        for (int bruh = 0; bruh < this.getDropCount(world.rand); bruh++)
            arraylist.add(new ItemInstance(this.getDropId(l, world.rand), 1, this.droppedMeta(l)));
        TileEntityBase tileentity = world.getTileEntity(i, j, k);
        if (tileentity instanceof InventoryBase) {
            InventoryBase iinventory = (InventoryBase) tileentity;
            for (int i1 = 0; i1 < iinventory.getInventorySize(); i1++) {
                ItemInstance itemstack = iinventory.getInventoryItem(i1);
                if (itemstack != null) {
                    arraylist.add(itemstack);
                    iinventory.setInventoryItem(i1, null);
                }
            }

        }
        return arraylist;
    }

    @Override
    public WrenchableMachineTileEntity createTileEntity() {
        return null;
    }

    public abstract WrenchableMachineTileEntity getBlockEntity(int i);

    @Override
    public void onBlockPlaced(Level world, int i, int j, int k) {
        TileEntityBase te = this.getBlockEntity(world.getTileMeta(i, j, k));
        world.setTileEntity(i, j, k, te);
    }

    @Override
    public void onBlockRemoved(Level world, int i, int j, int k) {

        boolean flag = true;
        for (Iterator iterator = getBlockDropped(world, i, j, k, world.getTileMeta(i, j, k)).iterator(); iterator.hasNext(); ) {
            ItemInstance itemstack = (ItemInstance) iterator.next();
            if (flag) {
                flag = false;
            } else {
                StackUtil.dropAsEntity(world, i, j, k, itemstack);
            }
        }
        super.onBlockRemoved(world, i, j, k);
    }

    @Override
    public void afterPlaced(Level world, int i, int j, int k, Living entityliving) {
        if (!PlatformUtils.isSimulating()) {
            return;
        }
        WrenchableMachineTileEntity tileentityblock = (WrenchableMachineTileEntity) world.getTileEntity(i, j, k);
        if (entityliving == null) {
            tileentityblock.setFacing((short) 2);
        } else {
            int l = MathHelper.floor((double) ((entityliving.yaw * 4F) / 360F) + 0.5D) & 3;
            switch (l) {
                case 0: // '\0'
                    tileentityblock.setFacing((short) 2);
                    break;

                case 1: // '\001'
                    tileentityblock.setFacing((short) 5);
                    break;

                case 2: // '\002'
                    tileentityblock.setFacing((short) 3);
                    break;

                case 3: // '\003'
                    tileentityblock.setFacing((short) 4);
                    break;
            }
        }
    }

}
