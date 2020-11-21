package net.glasslauncher.mods.machineutils.client;

import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerBase;

public class Client {

    public static Minecraft getMinecraft() {
        return ((Minecraft) FabricLoader.getInstance().getGameInstance());
    }

    public static PlayerBase getPlayer() {
        return getMinecraft().player;
    }
}
