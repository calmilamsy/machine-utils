// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode 

package net.glasslauncher.mods.machineutils.common.item;
import net.glasslauncher.mods.machineutils.api.energy.IChargeableItem;
import net.glasslauncher.mods.machineutils.common.PlatformUtils;
import net.minecraft.block.BlockBase;
import net.minecraft.entity.Living;
import net.minecraft.entity.player.PlayerBase;
import net.minecraft.item.ItemInstance;
import net.minecraft.item.tool.ToolBase;
import net.minecraft.item.tool.ToolMaterial;
import net.modificationstation.stationloader.api.common.item.StrengthOnMeta;

// Referenced classes of package ic2.common:
//            IChargeableItem, ItemArmorBatpack

class ItemElectricTool extends ToolBase
    implements IChargeableItem, StrengthOnMeta
{

    public ItemElectricTool(int i, int j, ToolMaterial enumtoolmaterial, BlockBase ablock[], int k, int l, int i1)
    {
        super(i, j, enumtoolmaterial, ablock);
        tier = k;
        ratio = l;
        transfer = i1;
        mineables = ablock;
    }

    @Override
    public float getStrengthOnBlock(ItemInstance itemstack, BlockBase block, int i)
    {
        if(itemstack.getDamage() + 1 >= itemstack.method_723())
        {
            return 1.0F;
        }
        return super.getStrengthOnBlock(itemstack, block);
    }

    @Override
    public boolean isEffectiveOn(BlockBase block)
    {
        for(int i = 0; i < mineables.length; i++)
        {
            if(mineables[i] == block)
            {
                return true;
            }
        }

        return false;
    }

    @Override
    public boolean postHit(ItemInstance itemstack, Living entityliving, Living entityliving1)
    {
        return true;
    }

    @Override
    public boolean postMine(ItemInstance itemstack, int i, int j, int k, int l, Living entityliving)
    {
        return true;
    }

    public static boolean use(ItemInstance itemstack, int i, PlayerBase entityplayer)
    {
        chargeFromBatpack(itemstack, entityplayer);
        if(itemstack.getDamage() + i > itemstack.method_723() - 1)
        {
            return false;
        }
        if(PlatformUtils.isSimulating())
        {
            itemstack.setDamage(itemstack.getDamage() + i);
            chargeFromBatpack(itemstack, entityplayer);
        }
        return true;
    }

    public static void chargeFromBatpack(ItemInstance itemstack, PlayerBase entityplayer)
    {
        if(entityplayer == null || entityplayer.inventory.armour[2] == null || !(entityplayer.inventory.armour[2].getType() instanceof ItemArmorBatpack))
        {
            return;
        } else
        {
            ((ItemArmorBatpack)entityplayer.inventory.armour[2].getType()).useBatpackOn(itemstack, entityplayer.inventory.armour[2]);
            return;
        }
    }

    @Override
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

    @Override
    public int getRatio() {
        return ratio;
    }

    public String getTextureFile()
    {
        return "/net/glasslauncher/mods/ic2sl/sprites/item_0.png";
    }

    public int tier;
    public int ratio;
    public int transfer;
    public BlockBase mineables[];
}
