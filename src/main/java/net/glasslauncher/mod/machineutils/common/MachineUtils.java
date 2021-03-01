package net.glasslauncher.mod.machineutils.common;

import net.glasslauncher.mod.machineutils.api.energy.IChargeableItem;
import net.glasslauncher.mod.machineutils.api.item.BatteryArmour;
import net.glasslauncher.mod.machineutils.mixin.common.LivingAccessor;
import net.glasslauncher.mod.machineutils.api.item.Jetpack;
import net.glasslauncher.mod.machineutils.mixin.common.EntityBaseAccessor;
import net.minecraft.entity.EntityBase;
import net.minecraft.entity.Living;
import net.minecraft.entity.player.PlayerBase;
import net.minecraft.item.ItemInstance;

import java.util.HashMap;
import java.util.Map;


public class MachineUtils {

    public static boolean use(ItemInstance itemstack, int removedEnergy, PlayerBase entityplayer) {
        if (((IChargeableItem) itemstack.getType()).getEnergy(itemstack) <= 0) {
            return false;
        }
        if (PlatformUtils.isSimulating()) {
            ((IChargeableItem) itemstack.getType()).addEnergy(itemstack, -removedEnergy);
            chargeFromBatPack(itemstack, entityplayer);
        }
        return true;
    }

    public static void chargeFromBatPack(ItemInstance itemstack, PlayerBase entityplayer) {
        if (entityplayer == null) {
            return;
        }
        for (ItemInstance itemInstance : entityplayer.inventory.armour) {
            if (itemInstance != null && itemInstance.getType() instanceof BatteryArmour) {
                ((BatteryArmour) itemInstance.getType()).useBatPackOn(itemstack, itemInstance);
            }
            return;
        }
    }

    public static boolean useJetpack(PlayerBase entityplayer) {
        ItemInstance itemstack = entityplayer.inventory.armour[2];
        if (itemstack.getDamage() + 1 >= itemstack.getDurability()) {
            return false;
        }
        boolean flag = itemstack.getType() instanceof Jetpack;
        float f = 1.0F;
        float f1 = 0.2F;
        if (flag) {
            f = 0.7F;
            f1 = 0.05F;
        }
        if ((float) itemstack.getDamage() + (float) (itemstack.getDurability() - 1) * f1 >= (float) (itemstack.getDurability() - 1)) {
            float f2 = itemstack.getDurability() - 1 - itemstack.getDamage();
            f *= f2 / ((float) (itemstack.getDurability() - 1) * f1);
        }
        if (PlatformUtils.isKeyDownForward(entityplayer)) {
            float f3 = 0.15F;
            if (getHoverMode(entityplayer)) {
                f3 = 0.5F;
            }
            if (flag) {
                f3 += 0.15F;
            }
            float f4 = f * f3 * 2.0F;
            if (f4 > 0.0F) {
                entityplayer.movementInputToVelocity(0.0F, 0.4F * f4, 0.02F);
            }
        }
        int i = flag ? 100 : 128;
        double d = entityplayer.y;
        if (d > (double) (i - 25)) {
            if (d > (double) i) {
                d = i;
            }
            f = (float) ((double) f * (((double) i - d) / 25D));
        }
        double d1 = entityplayer.velocityY;
        entityplayer.velocityY = Math.min(entityplayer.velocityY + (double) (f * 0.2F), 0.60000002384185791D);
        if (getHoverMode(entityplayer)) {
            float f5 = -0.1F;
            if (flag && getJumping(entityplayer)) {
                f5 = 0.1F;
            }
            if (entityplayer.velocityY > (double) f5) {
                entityplayer.velocityY = f5;
                if (d1 > entityplayer.velocityY) {
                    entityplayer.velocityY = d1;
                }
            }
        }
        int j = 9;
        if (getHoverMode(entityplayer)) {
            j = 6;
        }
        if (flag) {
            j -= 2;
        }
        itemstack.setDamage(Math.min(itemstack.getDurability() - 1, itemstack.getDamage() + j));
        if (entityplayer.velocityY > -0.34999999403953552D) {
            multiplyFall(entityplayer, 0.0F);
        }
        entityplayer.field_1635 = 0.0F;
        PlatformUtils.resetPlayerInAirTime(entityplayer);
        return true;
    }

    public static float fallStorage = 0.0F;
    public static void absorbFalling(PlayerBase entityplayer) {
        float f = getFallDistance(entityplayer);
        if (f < 1.0F && fallStorage == 0.0F) {
            return;
        }
        if (fallStorage > 0.0F && (entityplayer.method_1334() || entityplayer.method_932())) {
            fallStorage = 0.0F;
        }
        if (f >= 1.0F) {
            f--;
            ((EntityBaseAccessor) entityplayer).setFallDistance(f);
            fallStorage++;
        }
        if (entityplayer.onGround) {
            if (fallStorage < 3F) {
                fallStorage = 0.0F;
            } else {
                int i = (int) Math.ceil(fallStorage - 3F);
                i = (i + 1) / 2;
                ItemInstance itemstack = entityplayer.inventory.armour[0];
                itemstack.applyDamage(i, entityplayer);
                if (itemstack.count <= 0) {
                    entityplayer.inventory.armour[0] = null;
                }
                if (i >= 4) {
                    entityplayer.damage(null, i / 4);
                }
                fallStorage = 0.0F;
            }
        }
    }

    public static void multiplyFall(EntityBase entity, float f) {
        float f1 = ((EntityBaseAccessor) entity).getFallDistance();
        f1 *= f;
        ((EntityBaseAccessor) entity).setFallDistance(f1);
    }

    public static boolean getJumping(Living entityplayer) {
        return ((LivingAccessor) entityplayer).isJumping();
    }

    public static float getFallDistance(EntityBase entityplayer) {
        return ((EntityBaseAccessor) entityplayer).getFallDistance();
    }

    public static Map<PlayerBase, Boolean> playerIsjumping = new HashMap<>();
    public static Map<PlayerBase, Boolean> hoverMode = new HashMap<>();
    public static boolean getHoverMode(PlayerBase entityplayer) {
        return hoverMode.getOrDefault(entityplayer, false);
    }
}
