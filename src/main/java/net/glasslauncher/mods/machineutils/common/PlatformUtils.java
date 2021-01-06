package net.glasslauncher.mods.machineutils.common;

import net.fabricmc.api.EnvType;
import net.fabricmc.loader.api.FabricLoader;
import net.glasslauncher.mods.machineutils.api.block.IHasGuiContainer;
import net.glasslauncher.mods.machineutils.client.Client;
import net.minecraft.block.BlockBase;
import net.minecraft.container.ContainerBase;
import net.minecraft.container.slot.Slot;
import net.minecraft.entity.EntityBase;
import net.minecraft.entity.player.PlayerBase;
import net.minecraft.inventory.InventoryBase;
import net.minecraft.item.ItemInstance;
import net.minecraft.level.TileView;
import net.minecraft.level.chunk.Chunk;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntityBase;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.maths.Box;
import net.minecraft.util.maths.Vec3f;
import net.modificationstation.stationapi.api.common.gui.GuiHelper;
import net.modificationstation.stationapi.api.common.registry.Identifier;
import org.lwjgl.input.Keyboard;

import java.io.File;
import java.util.List;



/**
 * A side-independent utility class which unifies some client/server only methods so you dont have to.
 */
public class PlatformUtils {

    public static File getMinecraftDir() {
        if (FabricLoader.getInstance().getEnvironmentType() == EnvType.CLIENT) {
            return Client.getMinecraft().getGameDirectory();
        }
        return new File(".");
    }

    public static Chunk getOrLoadChunk(net.minecraft.level.Level world, int i, int j) {
        return world.getCache().getChunk(i, j);
    }

    public static PlayerBase getPlayerInstance() {
        if (FabricLoader.getInstance().getEnvironmentType() == EnvType.CLIENT) {
            return Client.getPlayer();
        }
        return null;
    }

    public static boolean isSimulating() {
        if (FabricLoader.getInstance().getEnvironmentType() == EnvType.CLIENT) {
            return !Client.getMinecraft().level.isClient;
        }
        return true;
    }

    public static boolean launchGUI(PlayerBase entityplayer, TileEntityBase tileentity, Identifier identifier) {
        if (identifier.getId().equals("ic2sl:nogui4u") && (tileentity instanceof InventoryBase)) {
            entityplayer.openChestScreen((InventoryBase) tileentity);
            return true;
        }
        if ((tileentity instanceof InventoryBase) && (tileentity instanceof IHasGuiContainer)) {
            GuiHelper.INSTANCE.openGUI(entityplayer, identifier, (InventoryBase) tileentity, ((IHasGuiContainer) tileentity).getGuiContainer(entityplayer.inventory));
            return true;
        } else {
            return false;
        }
    }

    public static boolean isBlockOpaqueCube(TileView iblockaccess, int i, int j, int k) {
        return iblockaccess.isFullOpaque(i, j, k);
    }

    public static void playSoundSp(String s, float f, float f1) {
        if (FabricLoader.getInstance().getEnvironmentType() == EnvType.CLIENT) {
            Client.getMinecraft().level.playSound(Client.getPlayer(), s, f, f1);
        }
    }

    public static void resetPlayerInAirTime(PlayerBase entityplayer) {
    }

    public static void messagePlayer(PlayerBase entityplayer, String s) {
        if (FabricLoader.getInstance().getEnvironmentType() == EnvType.CLIENT) {
            Client.getMinecraft().overlay.addChatMessage(s);
        }
    }

    public static boolean isKeyDownForward(PlayerBase entityplayer) {
        if (FabricLoader.getInstance().getEnvironmentType() == EnvType.CLIENT) {
            return Keyboard.isKeyDown(Client.getMinecraft().options.forwardKey.key);
        }
        return false;
    }

    public static String getItemNameIS(ItemInstance itemstack) {
        return itemstack.getType().getTranslationKey(itemstack);
    }

    public static void teleportTo(EntityBase entity, double d, double d1, double d2, float f,
                                  float f1) {
        entity.method_1338(d, d1, d2, f, f1);
    }

    public static String translateBlockName(BlockBase block) {
        return block.getTranslatedName();
    }

    public static double worldUntranslatedFunction2(net.minecraft.level.Level world, Vec3f vec3d, Box axisalignedbb) {
        return world.method_163(vec3d, axisalignedbb);
    }

    public static HitResult axisalignedbbUntranslatedFunction1(Box axisalignedbb, Vec3f vec3d, Vec3f vec3d1) {
        return axisalignedbb.method_89(vec3d, vec3d1);
    }

    @SuppressWarnings("unchecked")
    public static List<Slot> getContainerSlots(ContainerBase container) {
        return container.slots;
    }

    public static boolean isPlayerOp(PlayerBase entityplayer) {
        return isSimulating() || ((MinecraftServer) FabricLoader.getInstance().getGameInstance()).serverPlayerConnectionManager.isOp(entityplayer.name);
    }
}
