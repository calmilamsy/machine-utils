package net.glasslauncher.mod.machineutils.common.tileentity;

import net.glasslauncher.mod.machineutils.api.block.IWrenchable;
import net.glasslauncher.mod.machineutils.api.network.INetworkUpdateListener;
import net.glasslauncher.mod.machineutils.event.ingame.NetworkManager;
import net.minecraft.entity.player.PlayerBase;
import net.minecraft.tileentity.TileEntityBase;
import net.minecraft.util.io.CompoundTag;

import java.util.List;
import java.util.Vector;



/**
 * Not recommended for extending unless you know what you are doing, or just want a simple machine without an inventory.
 */
public class WrenchableMachineTileEntity extends TileEntityBase
        implements INetworkUpdateListener, IWrenchable {

    public boolean prevActive;
    public short prevFacing;
    protected boolean created;
    private boolean active;
    private short facing;

    public WrenchableMachineTileEntity() {
        created = false;
        active = false;
        facing = 0;
        prevActive = false;
        prevFacing = 0;
    }

    public void onCreated() {
        NetworkManager.requestInitialTileEntityData(level, x, y, z);
    }

    @Override
    public void readIdentifyingData(CompoundTag nbttagcompound) {
        super.readIdentifyingData(nbttagcompound);
        prevFacing = facing = nbttagcompound.getShort("facing");
    }

    @Override
    public void writeIdentifyingData(CompoundTag nbttagcompound) {
        super.writeIdentifyingData(nbttagcompound);
        nbttagcompound.put("facing", facing);
    }

    @Override
    public void tick() {
        if (!created) {
            onCreated();
            created = true;
        }
    }

    public boolean getActive() {
        return active;
    }

    public void setActive(boolean flag) {
        active = flag;
        if (prevActive != flag) {
            NetworkManager.updateTileEntityField(this, "active");
        }
        prevActive = flag;
    }

    public void setActiveWithoutNotify(boolean flag) {
        active = flag;
        prevActive = flag;
    }

    public short getFacing() {
        return facing;
    }

    @Override
    public void setFacing(short word0) {
        facing = word0;
        if (prevFacing != word0) {
            NetworkManager.updateTileEntityField(this, "facing");
        }
        prevFacing = word0;
    }

    public List<String> getNetworkedFields() {
        Vector<String> vector = new Vector<>(2);
        vector.add("active");
        vector.add("facing");
        return vector;
    }

    @Override
    public void onNetworkUpdate(String s) {
        if (s.equals("active") && prevActive != active || s.equals("facing") && prevFacing != facing) {
            level.method_243(x, y, z);
            prevActive = active;
            prevFacing = facing;
        }
    }

    @Override
    public boolean wrenchSetFacing(PlayerBase entityplayer, int i) {
        return false;
    }

    @Override
    public boolean wrenchRemove(PlayerBase entityplayer) {
        return true;
    }

    @Override
    public float getWrenchDropRate() {
        return 1.0F;
    }
}
