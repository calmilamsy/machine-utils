package net.glasslauncher.mod.machineutils.api.energy;

import net.glasslauncher.mod.machineutils.api.item.ItemWithEnergy;
import net.glasslauncher.mod.machineutils.mixin.client.ItemRendererAccessor;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.TextRenderer;
import net.minecraft.client.render.entity.ItemRenderer;
import net.minecraft.client.texture.TextureManager;
import net.minecraft.item.ItemInstance;
import net.minecraft.util.io.CompoundTag;
import net.modificationstation.stationapi.api.client.gui.CustomItemOverlay;
import net.modificationstation.stationapi.api.client.gui.CustomTooltipProvider;
import net.modificationstation.stationapi.api.item.nbt.HasItemEntity;
import net.modificationstation.stationapi.api.item.nbt.ItemEntity;
import net.modificationstation.stationapi.api.item.nbt.ItemWithEntity;
import net.modificationstation.stationapi.api.util.Colours;
import org.lwjgl.opengl.GL11;

import java.util.function.Function;
import java.util.function.Supplier;

public interface IChargeableItem extends CustomTooltipProvider, ItemWithEntity, CustomItemOverlay {


    default int giveEnergyToOther(ItemInstance itemstack, int inputEnergy, int machineTier, boolean followTransferRate) {
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
        return getTier(itemInstance) * 32;
    }

    default void setEnergy(ItemInstance itemInstance, int newEnergy) {
        ((ItemWithEnergy) HasItemEntity.cast(itemInstance).getItemEntity()).energy = Math.min(newEnergy, getMaxEnergy(itemInstance));
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
    default String[] getTooltip(ItemInstance itemInstance, String originalTooltip) {
        return new String[]{
                originalTooltip,
                "" + Colours.RED + ((IChargeableItem) itemInstance.getType()).getEnergy(itemInstance) + Colours.WHITE + "/" + Colours.DARK_AQUA + ((IChargeableItem) itemInstance.getType()).getMaxEnergy(itemInstance) + Colours.WHITE + " EU stored"
        };
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
        int barLength = (int)Math.round((((double)getEnergy(itemInstance) / (double)getMaxEnergy(itemInstance)) * 13));
        int colourOffset = 255-(int)Math.round((((double)getEnergy(itemInstance) / (double)getMaxEnergy(itemInstance)) * 225));
        GL11.glDisable(2896);
        GL11.glDisable(2929);
        GL11.glDisable(3553);
        Tessellator var8 = Tessellator.INSTANCE;
        int barColour = Math.max((colourOffset/8) - 130, 100) << 16 | (233-colourOffset) << 8 | 255-(colourOffset/4);
        int backgroundColour = (255 - colourOffset) / 4 << 16 | 16128;
        int barOffset = itemInstance.isDamaged()? 2 : 0;
        ((ItemRendererAccessor) itemRenderer).invokeMethod_1485(var8, itemX + 2, itemY + 13 - barOffset, 13, 2, 0);
        ((ItemRendererAccessor) itemRenderer).invokeMethod_1485(var8, itemX + 2, itemY + 13 - barOffset, 12, 1, backgroundColour);
        ((ItemRendererAccessor) itemRenderer).invokeMethod_1485(var8, itemX + 2, itemY + 13 - barOffset, barLength, 1, barColour);
        GL11.glEnable(3553);
        GL11.glEnable(2896);
        GL11.glEnable(2929);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
    }
}
