package net.glasslauncher.mods.machineutils.api.energy;

import net.glasslauncher.mods.machineutils.api.item.ItemWithEnergy;
import net.glasslauncher.mods.machineutils.mixin.client.ItemRendererAccessor;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.TextRenderer;
import net.minecraft.client.render.entity.ItemRenderer;
import net.minecraft.client.texture.TextureManager;
import net.minecraft.item.ItemInstance;
import net.minecraft.util.io.CompoundTag;
import net.modificationstation.stationapi.api.client.gui.Colors;
import net.modificationstation.stationapi.api.client.gui.HasCustomTooltip;
import net.modificationstation.stationapi.api.client.item.CustomItemOverlay;
import net.modificationstation.stationapi.api.common.item.HasItemEntity;
import net.modificationstation.stationapi.api.common.item.ItemEntity;
import net.modificationstation.stationapi.api.common.item.ItemWithEntity;
import net.modificationstation.stationapi.api.common.preset.item.ItemBase;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;

public interface IChargeableItem extends HasCustomTooltip, ItemWithEntity, CustomItemOverlay {


    default int giveEnergyTo(ItemInstance itemstack, int inputEnergy, int machineTier, boolean followTransferRate) {
        if (machineTier < getTier(itemstack) || getEnergy(itemstack) >= getMaxEnergy(itemstack)) {
            return 0;
        }
        if (followTransferRate && getTransfer(itemstack) != 0 && inputEnergy > getTransfer(itemstack)) {
            inputEnergy = getTransfer(itemstack);
        }
        addEnergy(itemstack, inputEnergy);
        return inputEnergy;
    }

    default int getEnergy(ItemInstance itemInstance) {
        return ((ItemWithEnergy) HasItemEntity.cast(itemInstance).getItemEntity()).energy;
    }

    int getMaxEnergy(ItemInstance itemInstance);

    int getTier(ItemInstance itemInstance);

    default int getTransfer(ItemInstance itemInstance) {
        return getMaxEnergy(itemInstance) / 600;
    }

    default void setEnergy(ItemInstance itemInstance, int newEnergy) {
        System.out.println("adding " + newEnergy);
        ((ItemWithEnergy) HasItemEntity.cast(itemInstance).getItemEntity()).energy = Math.min(newEnergy, getMaxEnergy(itemInstance));
        System.out.println(((ItemWithEnergy) HasItemEntity.cast(itemInstance).getItemEntity()).energy);
        if (getEnergy(itemInstance) < 0) {
            setEnergy(itemInstance, 0);
        }
        else if (getEnergy(itemInstance) > getMaxEnergy(itemInstance)) {
            setEnergy(itemInstance, getMaxEnergy(itemInstance));
        }
    }

    default void addEnergy(ItemInstance itemInstance, int addedEnergy) {
        setEnergy(itemInstance, addedEnergy + getEnergy(itemInstance));
    }

    @Override
    default List<String> getToolTip(String originalTooltip, ItemInstance itemInstance) {
        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add(originalTooltip);
        arrayList.add(Colors.RED + ((IChargeableItem) itemInstance.getType()).getEnergy(itemInstance) + Colors.WHITE + "/" + Colors.DARK_AQUA + ((IChargeableItem) itemInstance.getType()).getMaxEnergy(itemInstance) + Colors.WHITE + " EU stored");
        return arrayList;
    }

    @Override
    default Supplier<ItemEntity> getItemEntityFactory() {
        return ItemWithEnergy::new;
    }

    @Override
    default Function<CompoundTag, ItemEntity> getItemEntityNBTFactory() {
        return ItemWithEnergy::new;
    }

    @Override
    default void renderItemOverlay(ItemRenderer itemRenderer, int itemX, int itemY, ItemInstance itemInstance, TextRenderer textRenderer, TextureManager textureManager) {
        int var11 = (int)Math.round(13.0D - (double)getMaxEnergy(itemInstance) * 13.0D / (double)getEnergy(itemInstance));
        System.out.println(var11 + " ===");
        int var7 = (int)Math.round(255.0D - (double)getMaxEnergy(itemInstance) * 255.0D / (double)getEnergy(itemInstance));
        System.out.println(var7);
        GL11.glDisable(2896);
        GL11.glDisable(2929);
        GL11.glDisable(3553);
        Tessellator var8 = Tessellator.INSTANCE;
        int var9 = 255 - var7 << 16 | var7 << 8;
        int var10 = (255 - var7) / 4 << 16 | 16128;
        ((ItemRendererAccessor) itemRenderer).invokeMethod_1485(var8, itemX + 2, itemY + 11, 13, 2, 0);
        ((ItemRendererAccessor) itemRenderer).invokeMethod_1485(var8, itemX + 2, itemY + 11, 12, 1, var10);
        ((ItemRendererAccessor) itemRenderer).invokeMethod_1485(var8, itemX + 2, itemY + 11, var11, 1, var9);
        GL11.glEnable(3553);
        GL11.glEnable(2896);
        GL11.glEnable(2929);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
    }
}
