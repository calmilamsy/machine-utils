package net.glasslauncher.mods.machineutils.event.ingame;

import net.fabricmc.api.EnvType;
import net.fabricmc.loader.api.FabricLoader;
import net.glasslauncher.mods.machineutils.api.network.INetworkItemEventListener;
import net.glasslauncher.mods.machineutils.api.network.INetworkTileEntityEventListener;
import net.glasslauncher.mods.machineutils.api.network.INetworkUpdateListener;
import net.glasslauncher.mods.machineutils.common.PlatformUtils;
import net.glasslauncher.mods.machineutils.common.tileentity.WrenchableMachineTileEntity;
import net.glasslauncher.mods.machineutils.event.init.MachineUtilsConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerBase;
import net.minecraft.item.ItemBase;
import net.minecraft.item.ItemInstance;
import net.minecraft.level.Level;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tileentity.TileEntityBase;
import net.modificationstation.stationapi.api.common.event.EventListener;
import net.modificationstation.stationapi.api.common.event.packet.MessageListenerRegister;
import net.modificationstation.stationapi.api.common.packet.Message;
import net.modificationstation.stationapi.api.common.packet.MessageListenerRegistry;
import net.modificationstation.stationapi.api.common.packet.PacketHelper;
import net.modificationstation.stationapi.api.common.registry.Identifier;
import net.modificationstation.stationapi.api.common.util.API;

import java.lang.reflect.Field;
import java.util.*;

import static net.glasslauncher.mods.machineutils.event.init.MachineUtilsConfig.MOD_ID;


public class NetworkManager
{
    private static final int updatePeriod = 2;
    private static final Set<TileEntityField> fieldsToUpdateSet;
    private static int ticksLeftToUpdate;

    static {
        fieldsToUpdateSet = new HashSet<>();
        NetworkManager.ticksLeftToUpdate = updatePeriod;
    }

    public NetworkManager()
    {
    }

    public static void onTick() {
        if (--NetworkManager.ticksLeftToUpdate == 0) {
            if (!NetworkManager.fieldsToUpdateSet.isEmpty()) {
                sendUpdatePacket();
            }
            NetworkManager.ticksLeftToUpdate = 2;
        }
    }

    public static void updateTileEntityField(TileEntityBase tileentity, String s)
    {
        if (FabricLoader.getInstance().getEnvironmentType() == EnvType.CLIENT) {
            if (tileentity instanceof INetworkUpdateListener) {
                ((INetworkUpdateListener) tileentity).onNetworkUpdate(s);
            }
        }
        else {
            NetworkManager.fieldsToUpdateSet.add(new TileEntityField(tileentity, s));
            if (NetworkManager.fieldsToUpdateSet.size() > 10000) {
                sendUpdatePacket();
            }
        }
    }

    @API
    public static void initiateTileEntityEvent(TileEntityBase tileentity, int i, boolean flag)
    {
        if (FabricLoader.getInstance().getEnvironmentType() == EnvType.CLIENT) {
            if (tileentity instanceof INetworkTileEntityEventListener) {
                ((INetworkTileEntityEventListener) tileentity).onNetworkEvent(i);
            }
        }
        else {
            final int j = flag ? 400 : ((MinecraftServer) FabricLoader.getInstance().getGameInstance()).serverPlayerConnectionManager.getViewRadiusInTiles();
            final Level world = tileentity.level;
            for (final Object obj : world.players) {
                final PlayerBase entityplayer = (PlayerBase) obj;
                final int k = tileentity.x - (int)entityplayer.x;
                final int l = tileentity.z - (int)entityplayer.z;
                int i2;
                if (flag) {
                    i2 = k * k + l * l;
                }
                else {
                    i2 = Math.max(Math.abs(k), Math.abs(l));
                }
                if (i2 <= j) {
                    final Message packet230modloader = new Message(Identifier.of("machineutils:initiateTileEntityEvent"));
                    packet230modloader.field_906 = true;
                    packet230modloader.put(new int[] {world.dimension.field_2179,
                            tileentity.x,
                            tileentity.y,
                            tileentity.z,
                            i});
                    PacketHelper.sendTo(entityplayer, packet230modloader);
                    System.out.println("Sent packet");
                }
            }
        }
    }

    @API
    public static void initiateItemEvent(PlayerBase entityplayer, ItemInstance itemstack, int i, boolean flag)
    {
        if (FabricLoader.getInstance().getEnvironmentType() == EnvType.CLIENT)
        {
            ItemBase item = itemstack.getType();
            if (item instanceof INetworkItemEventListener) {
                ((INetworkItemEventListener) item).onNetworkEvent(itemstack.getDamage(), entityplayer, i);
            }
        }
        else {

            final int j = flag ? 400 : ((MinecraftServer) FabricLoader.getInstance().getGameInstance()).serverPlayerConnectionManager.getViewRadiusInTiles();
            for (final Object obj : entityplayer.level.players) {
                final PlayerBase entityplayer2 = (PlayerBase)obj;
                final int k = (int)entityplayer.x - (int)entityplayer2.x;
                final int l = (int)entityplayer.z - (int)entityplayer2.z;
                int i2;
                if (flag) {
                    i2 = k * k + l * l;
                }
                else {
                    i2 = Math.max(Math.abs(k), Math.abs(l));
                }
                if (i2 <= j) {
                    final Message packet230modloader = new Message(Identifier.of("machineutils:initiateItemEvent"));
                    packet230modloader.field_906 = true;
                    packet230modloader.put(new int[] {itemstack.itemId,
                            itemstack.getDamage(),
                            i});
                    packet230modloader.put(new String[]{entityplayer.name});
                    PacketHelper.sendTo(entityplayer2, packet230modloader);
                }
            }
        }
    }

    @API
    public static void announceBlockUpdate(Level world, int i, int j, int k)
    {
        if (FabricLoader.getInstance().getEnvironmentType() == EnvType.CLIENT)
        {
            world.method_243(i, j, k);
        }
        else {
            for (final Object obj : world.players) {
                final PlayerBase entityplayer = (PlayerBase)obj;
                final int l = Math.min(Math.abs(i - (int)entityplayer.x), Math.abs(k - (int)entityplayer.z));
                if (l <= ((MinecraftServer) FabricLoader.getInstance().getGameInstance()).serverPlayerConnectionManager.getViewRadiusInTiles()) {
                    final Message packet230modloader = new Message(Identifier.of( "machineutils:announceBlockUpdate"));
                    packet230modloader.field_906 = true;
                    packet230modloader.put(new int[] {world.dimension.field_2179,
                            i,
                            j,
                            k});
                    PacketHelper.sendTo(entityplayer, packet230modloader);
                }
            }
        }
    }

    public static void requestInitialTileEntityData(Level world, int i, int j, int k)
    {
        Message customPacket = new Message(Identifier.of("machineutils:requestInitialTileEntityData"));
        customPacket.field_906 = true;
        customPacket.put(new int[] {world.dimension.field_2179,
                i,
                j,
                k});
        PacketHelper.send(customPacket);
    }

    @API
    public static void initiateClientItemEvent(ItemInstance itemstack, int i)
    {
        ItemBase item = itemstack.getType();
        if(PlatformUtils.isSimulating())
        {
            if(item instanceof INetworkItemEventListener)
            {
                ((INetworkItemEventListener)item).onNetworkEvent(itemstack.getDamage(), PlatformUtils.getPlayerInstance(), i);
            }
        } else
        {
            Message customPacket = new Message(Identifier.of("machineutils:initiateClientItemEvent"));
            customPacket.field_906 = true;
            customPacket.put(new int[] {itemstack.itemId,
                    itemstack.getDamage(),
                    i});
            PacketHelper.send(customPacket);
        }
    }

    public static void handlePacket(PlayerBase player, Message customPacket, int packetType)
    {
        if (FabricLoader.getInstance().getEnvironmentType() == EnvType.CLIENT){
            label0:
            {
                if (packetType == 0 && customPacket.ints().length > 0) {
                    Level world = ((Minecraft) FabricLoader.getInstance().getGameInstance()).level;
                    if (world.dimension.field_2179 != customPacket.ints()[0]) {
                        return;
                    }
                    int i = 0;
                    int j = 0;
                    for (int k = 1; k + 3 < customPacket.ints().length; k += 4) {
                        TileEntityBase tileentity1 = world.getTileEntity(customPacket.ints()[k], customPacket.ints()[k + 1], customPacket.ints()[k + 2]);
                        String s = customPacket.strings()[j];
                        j++;
                        Field field = null;
                        try {
                            if (tileentity1 != null) {
                                Class<?> class1 = tileentity1.getClass();
                                do {
                                    try {
                                        field = class1.getDeclaredField(s);
                                    } catch (NoSuchFieldException nosuchfieldexception) {
                                        class1 = class1.getSuperclass();
                                    }
                                } while (field == null && class1 != null);
                                if (field == null) {
                                    throw new NoSuchFieldException(s);
                                }
                                field.setAccessible(true);
                            }
                            switch (customPacket.ints()[k + 3]) {
                                case 0: // '\0'
                                    if (tileentity1 != null) {
                                        field.setFloat(tileentity1, customPacket.floats()[i]);
                                    }
                                    i++;
                                    break;

                                case 1: // '\001'
                                    if (tileentity1 != null) {
                                        field.setInt(tileentity1, customPacket.ints()[k + 4]);
                                    }
                                    k++;
                                    break;

                                case 2: // '\002'
                                    if (tileentity1 != null) {
                                        field.set(tileentity1, customPacket.strings()[j]);
                                    }
                                    j++;
                                    break;

                                case 3: // '\003'
                                    if (tileentity1 != null) {
                                        field.setBoolean(tileentity1, customPacket.ints()[k + 4] != 0);
                                    }
                                    k++;
                                    break;

                                case 4: // '\004'
                                    if (tileentity1 != null) {
                                        field.setByte(tileentity1, (byte) customPacket.ints()[k + 4]);
                                    }
                                    k++;
                                    break;

                                case 5: // '\005'
                                    if (tileentity1 != null) {
                                        field.setShort(tileentity1, (short) customPacket.ints()[k + 4]);
                                    }
                                    k++;
                                    break;

                                default:
                                    throw new RuntimeException("Invalid field type index: " + customPacket.ints()[k + 3]);
                            }
                        } catch (Exception exception) {
                            throw new RuntimeException(exception);
                        }
                        if (tileentity1 instanceof INetworkUpdateListener) {
                            ((INetworkUpdateListener) tileentity1).onNetworkUpdate(s);
                        }
                    }

                    break label0;
                }
                if (packetType == 1 && customPacket.ints().length == 5) {
                    Level world1 = ((Minecraft) FabricLoader.getInstance().getGameInstance()).level;
                    if (world1.dimension.field_2179 != customPacket.ints()[0]) {
                        return;
                    }
                    TileEntityBase tileentity = world1.getTileEntity(customPacket.ints()[1], customPacket.ints()[2], customPacket.ints()[3]);
                    if (tileentity instanceof INetworkTileEntityEventListener) {
                        ((INetworkTileEntityEventListener) tileentity).onNetworkEvent(customPacket.ints()[4]);
                    }
                    break label0;
                }
                if (packetType == 2 && customPacket.ints().length == 3) {
                    Level world2 = ((Minecraft) FabricLoader.getInstance().getGameInstance()).level;
                    Iterator<?> iterator = world2.players.iterator();
                    PlayerBase entityplayer;
                    do {
                        if (!iterator.hasNext()) {
                            break label0;
                        }
                        Object obj = iterator.next();
                        entityplayer = (PlayerBase) obj;
                    } while (!entityplayer.name.equals(customPacket.strings()[0]));
                    ItemBase item = ItemBase.byId[customPacket.ints()[0]];
                    if (item instanceof INetworkItemEventListener) {
                        ((INetworkItemEventListener) item).onNetworkEvent(customPacket.ints()[1], entityplayer, customPacket.ints()[2]);
                    }
                } else if (packetType == 3 && customPacket.ints().length == 4) {
                    Level world3 = ((Minecraft) FabricLoader.getInstance().getGameInstance()).level;
                    if (world3.dimension.field_2179 != customPacket.ints()[0]) {
                        return;
                    }
                    world3.method_243(customPacket.ints()[1], customPacket.ints()[2], customPacket.ints()[3]);
                }
            }
        }
        else {
            if (packetType == 0 && customPacket.ints().length == 4) {
                final List<ServerLevel> worldList = Arrays.asList(((MinecraftServer) FabricLoader.getInstance().getGameInstance()).levels);
                final ServerLevel[] aServerLevelHelper = new ServerLevel[worldList.size()];
                final ServerLevel[] aServerLevel = worldList.toArray(aServerLevelHelper);
                final int i = aServerLevel.length;
                int j = 0;
                while (j < i) {
                    final ServerLevel ServerLevel = aServerLevel[j];
                    if (customPacket.ints()[0] == (ServerLevel).dimension.field_2179) {
                        final TileEntityBase tileentity = ServerLevel.getTileEntity(customPacket.ints()[1], customPacket.ints()[2], customPacket.ints()[3]);
                        if (tileentity instanceof WrenchableMachineTileEntity) {
                            for (String s : ((WrenchableMachineTileEntity)tileentity).getNetworkedFields()) {
                                NetworkManager.fieldsToUpdateSet.add(new TileEntityField(tileentity, s, player));
                                if (NetworkManager.fieldsToUpdateSet.size() > 10000) {
                                    sendUpdatePacket();
                                }
                            }
                            sendUpdatePacket();
                            break;
                        }
                        break;
                    }
                    else {
                        ++j;
                    }
                }
            }
            else if (packetType == 1 && customPacket.ints().length == 3) {
                final ItemBase item = ItemBase.byId[customPacket.ints()[0]];
                if (item instanceof INetworkItemEventListener) {
                    ((INetworkItemEventListener)item).onNetworkEvent(customPacket.ints()[1], player, customPacket.ints()[2]);
                }
            }
        }
    }

    private static void sendUpdatePacket() {
        MachineUtilsConfig.LOGGER.debug("Sending update packet.");
        List<ServerLevel> worldList = Arrays.asList(((MinecraftServer) FabricLoader.getInstance().getGameInstance()).levels);
        ServerLevel[] aServerLevelHelper = new ServerLevel[worldList.size()];
        ServerLevel[] aServerLevel = worldList.toArray(aServerLevelHelper);
        int i = aServerLevel.length;

        for (net.minecraft.server.level.ServerLevel ServerLevel : aServerLevel) {

            //noinspection unchecked
            for (PlayerBase obj : (Iterable<PlayerBase>) ServerLevel.players) {
                Message packet230modloader = new Message(Identifier.of("machineutils:updatePacket"));
                packet230modloader.field_906 = true;
                Vector<Float> vector = new Vector<>();
                Vector<Integer> vector1 = new Vector<>();
                Vector<String> vector2 = new Vector<>();
                vector1.add(ServerLevel.dimension.field_2179);

                for (TileEntityField tileentityfield : fieldsToUpdateSet) {
                    if ((!tileentityfield.te.isInvalid()) && (tileentityfield.te.level == ServerLevel) && ((tileentityfield.target == null) || (tileentityfield.target == obj))) {
                        int l = Math.min(Math.abs(tileentityfield.te.x - (int) obj.x), Math.abs(tileentityfield.te.z - (int) obj.z));
                        if (l <= ((MinecraftServer) FabricLoader.getInstance().getGameInstance()).serverPlayerConnectionManager.getViewRadiusInTiles()) {
                            vector1.add(tileentityfield.te.x);
                            vector1.add(tileentityfield.te.y);
                            vector1.add(tileentityfield.te.z);
                            vector2.add(tileentityfield.field);
                            Field field = null;
                            try {
                                Class<? extends TileEntityBase> class1 = tileentityfield.te.getClass();
                                do {
                                    try {
                                        field = class1.getDeclaredField(tileentityfield.field);
                                    } catch (NoSuchFieldException nosuchfieldexception) {
                                        //noinspection unchecked
                                        class1 = (Class<? extends TileEntityBase>) class1.getSuperclass();
                                    }
                                } while ((field == null) && (class1 != null));
                                if (field == null) {
                                    throw new NoSuchFieldException(tileentityfield.field);
                                }
                                field.setAccessible(true);
                                Class<?> class2 = field.getType();
                                if (class2 == Float.TYPE) {
                                    vector1.add(0);
                                    vector.add(field.getFloat(tileentityfield.te));
                                } else if (class2 == Integer.TYPE) {
                                    vector1.add(1);
                                    vector1.add(field.getInt(tileentityfield.te));
                                } else if (class2 == String.class) {
                                    vector1.add(2);
                                    vector2.add((String) field.get(tileentityfield.te));
                                } else if (class2 == Boolean.TYPE) {
                                    vector1.add(3);
                                    vector1.add(field.getBoolean(tileentityfield.te) ? 1 : 0);
                                } else if (class2 == Byte.TYPE) {
                                    vector1.add(4);
                                    vector1.add((int) field.getByte(tileentityfield.te));
                                } else if (class2 == Short.TYPE) {
                                    vector1.add(5);
                                    vector1.add((int) field.getShort(tileentityfield.te));
                                } else {
                                    throw new RuntimeException("Invalid field type: " + field.getType());
                                }
                            } catch (Exception exception) {
                                throw new RuntimeException(exception);
                            }
                        }
                    }
                }
                if (vector1.size() > 1) {
                    int[] ai = new int[vector1.size()];
                    int k = 0;
                    for (Integer integer : vector1) {
                        ai[(k++)] = integer;
                    }

                    float[] af = new float[vector.size()];
                    k = 0;
                    for (Float float1 : vector) {
                        af[(k++)] = float1;
                    }

                    packet230modloader.put(ai);
                    packet230modloader.put(af);
                    packet230modloader.put(vector2.toArray(new String[0]));
                    PacketHelper.sendTo(obj, packet230modloader);
                    MachineUtilsConfig.LOGGER.debug("Update packet sent.");
                }
            }
        }
        fieldsToUpdateSet.clear();
    }

    @EventListener
    public void registerMessageListeners(MessageListenerRegister event) {
        MessageListenerRegistry registry = event.registry;
        // Client to server
        registry.registerValue(Identifier.of(MOD_ID, "requestInitialTileEntityData"), ((playerBase, customData) -> handlePacket(playerBase, customData, 0)));
        registry.registerValue(Identifier.of(MOD_ID, "initiateClientItemEvent"), ((playerBase, customData) -> handlePacket(playerBase, customData, 1)));
        // Server to client
        registry.registerValue(Identifier.of(MOD_ID, "updatePacket"), ((playerBase, customData) -> handlePacket(playerBase, customData, 0)));
        registry.registerValue(Identifier.of(MOD_ID, "initiateTileEntityEvent"), ((playerBase, customData) -> handlePacket(playerBase, customData, 1)));
        registry.registerValue(Identifier.of(MOD_ID, "initiateItemEvent"), ((playerBase, customData) -> handlePacket(playerBase, customData, 2)));
        registry.registerValue(Identifier.of(MOD_ID, "announceBlockUpdate"), ((playerBase, customData) -> handlePacket(playerBase, customData, 3)));
    }

    static class TileEntityField
    {
        TileEntityBase te;
        String field;
        PlayerBase target;

        @Override
        public boolean equals(final Object obj) {
            if (obj instanceof TileEntityField) {
                final TileEntityField tileentityfield = (TileEntityField)obj;
                return tileentityfield.te == this.te && tileentityfield.field.equals(this.field);
            }
            return false;
        }

        @Override
        public int hashCode() {
            return this.te.hashCode() * 31 ^ this.field.hashCode();
        }

        TileEntityField(final TileEntityBase tileentity, final String s) {
            this.target = null;
            this.te = tileentity;
            this.field = s;
        }

        TileEntityField(final TileEntityBase tileentity, final String s, final PlayerBase entityplayer) {
            this.target = null;
            this.te = tileentity;
            this.field = s;
            this.target = entityplayer;
        }
    }
}
