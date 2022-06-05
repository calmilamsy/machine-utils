package net.glasslauncher.mod.machineutils.api.energy;

import net.glasslauncher.mod.machineutils.impl.common.MachineUtils;
import net.glasslauncher.mod.machineutils.impl.common.MachineUtilsIdentifier;
import net.glasslauncher.mod.machineutils.mixin.client.ItemRendererAccessor;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.TextRenderer;
import net.minecraft.client.render.entity.ItemRenderer;
import net.minecraft.client.texture.TextureManager;
import net.minecraft.item.ItemInstance;
import net.modificationstation.stationapi.api.client.gui.CustomItemOverlay;
import net.modificationstation.stationapi.api.client.gui.CustomTooltipProvider;
import net.modificationstation.stationapi.api.item.nbt.StationNBT;
import net.modificationstation.stationapi.api.util.Colours;
import org.lwjgl.opengl.GL11;

import java.awt.*;

public interface ChargeableItem extends CustomTooltipProvider, CustomItemOverlay {

    default int giveEnergyToOther(ItemInstance thisItem, EnergyTier machineTier, int inputEnergy, boolean followTransferRate) {
        if (machineTier.worseThan(getTier()) || getEnergy(thisItem) >= getMaxEnergy()) {
            return 0;
        }
        if (followTransferRate && getTransfer() != 0 && inputEnergy > getTransfer()) {
            inputEnergy = getTransfer();
        }
        addEnergy(thisItem, inputEnergy);
        return inputEnergy;
    }

    default int getEnergy(ItemInstance itemInstance) {
        return StationNBT.cast(itemInstance).getStationNBT().getInt(MachineUtilsIdentifier.NBT_ENERGY.toString());
    }

    default int getMaxEnergy() {
        return getStorageMultiplier() * 10000;
    }

    int getStorageMultiplier();

    EnergyTier getTier();

    /**
     * This exists purely to allow for custom values.
     * DO. NOT. OVERRIDE. UNLESS. YOU. MUST.
     * @return max energy transferable per tick.
     */
    default int getTransfer() {
        return getTier().transfer;
    }

    default void setEnergy(ItemInstance itemInstance, int newEnergy) {
        StationNBT.cast(itemInstance).getStationNBT().put(MachineUtilsIdentifier.NBT_ENERGY.toString(), Math.min(newEnergy, getMaxEnergy()));
        if (getEnergy(itemInstance) < 0) {
            setEnergy(itemInstance, 0);
        }
        else if (getEnergy(itemInstance) > getMaxEnergy()) {
            setEnergy(itemInstance, getMaxEnergy());
        }
    }

    default void addEnergy(ItemInstance itemInstance, int addedEnergy) {
        setEnergy(itemInstance, addedEnergy + getEnergy(itemInstance));
    }

    @Override
    default String[] getTooltip(ItemInstance itemInstance, String originalTooltip) {
        return new String[]{
                originalTooltip,
                "" + Colours.RED + ((ChargeableItem) itemInstance.getType()).getEnergy(itemInstance) + Colours.WHITE + "/" + Colours.DARK_AQUA + ((ChargeableItem) itemInstance.getType()).getMaxEnergy() + Colours.WHITE + " EU stored"
        };
    }

    @Override
    default void renderItemOverlay(ItemRenderer itemRenderer, int itemX, int itemY, ItemInstance itemInstance, TextRenderer textRenderer, TextureManager textureManager) {
        int barLength = (int)Math.round((((double)getEnergy(itemInstance) / (double)getMaxEnergy()) * 13));
        int colourOffset = 255-(int)Math.round((((double)getEnergy(itemInstance) / (double)getMaxEnergy()) * 225));
        GL11.glDisable(2896);
        GL11.glDisable(2929);
        GL11.glDisable(3553);
        Tessellator var8 = Tessellator.INSTANCE;
        int barColour = MachineUtils.getIntFromColour(new Color(255-Math.max((colourOffset/2) - 130, 100), 255-colourOffset, 255-(colourOffset/2)));
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
