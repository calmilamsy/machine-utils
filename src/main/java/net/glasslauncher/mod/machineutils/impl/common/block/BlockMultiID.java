package net.glasslauncher.mod.machineutils.impl.common.block;

import net.glasslauncher.mod.machineutils.impl.common.PlatformUtils;
import net.glasslauncher.mod.machineutils.impl.common.StackUtil;
import net.glasslauncher.mod.machineutils.impl.common.tileentity.WrenchableMachineTileEntity;
import net.minecraft.block.BlockSounds;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Living;
import net.minecraft.entity.player.PlayerBase;
import net.minecraft.inventory.InventoryBase;
import net.minecraft.item.ItemInstance;
import net.minecraft.level.BlockView;
import net.minecraft.level.Level;
import net.minecraft.tileentity.TileEntityBase;
import net.minecraft.util.maths.MathHelper;
import net.modificationstation.stationapi.api.registry.Identifier;

import java.util.ArrayList;

public abstract class BlockMultiID extends BlockWithEntity {
    public Identifier texture;

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

    public static boolean isActive(BlockView iblockaccess, int i, int j, int k) {
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
    public BlockMultiID setSounds(BlockSounds sounds) {
        return (BlockMultiID) super.setSounds(sounds);
    }

    /**
     *
     * @param texture Texture to use. Uses an identifier.
     * @return Current instance
     */
    public BlockMultiID setTexture(Identifier texture) {
        this.texture = texture;
        return this;
    }

    @Override
    public int getTextureForSide(BlockView iblockaccess, int i, int j, int k, int l) {
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

    public ArrayList<ItemInstance> getBlockDropped(Level world, int i, int j, int k, int l) {
        ArrayList<ItemInstance> arraylist = new ArrayList<>();
        for (int bruh = 0; bruh < this.getDropCount(world.rand); bruh++)
            arraylist.add(new ItemInstance(this.getDropId(l, world.rand), 1, this.droppedMeta(l)));
        TileEntityBase tileentity = world.getTileEntity(i, j, k);
        if (tileentity instanceof InventoryBase iinventory) {
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
        for (ItemInstance itemstack : getBlockDropped(world, i, j, k, world.getTileMeta(i, j, k))) {
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
                case 0 -> // '\0'
                        tileentityblock.setFacing((short) 2);
                case 1 -> // '\001'
                        tileentityblock.setFacing((short) 5);
                case 2 -> // '\002'
                        tileentityblock.setFacing((short) 3);
                case 3 -> // '\003'
                        tileentityblock.setFacing((short) 4);
            }
        }
    }

}
