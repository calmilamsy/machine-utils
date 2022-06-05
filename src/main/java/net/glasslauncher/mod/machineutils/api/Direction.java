package net.glasslauncher.mod.machineutils.api;

import net.minecraft.tileentity.TileEntityBase;

public enum Direction {

    XN(0),
    XP(1),
    YN(2),
    YP(3),
    ZN(4),
    ZP(5);

    private final int dir;

    Direction(int j) {
        dir = j;
    }

    public TileEntityBase applyToTileEntity(TileEntityBase tileentity) {
        int[] ai = {
                tileentity.x, tileentity.y, tileentity.z
        };
        ai[dir / 2] += getSign();
        if (tileentity.level != null && tileentity.level.isTileLoaded(ai[0], ai[1], ai[2])) {
            return tileentity.level.getTileEntity(ai[0], ai[1], ai[2]);
        } else {
            return null;
        }
    }

    public Direction getInverse() {
        int i = dir - getSign();
        Direction[] adirection = values();
        int j = adirection.length;
        for (Direction direction : adirection) {
            if (direction.dir == i) {
                return direction;
            }
        }

        return this;
    }

    public int toSideValue() {
        return (dir + 4) % 6;
    }

    private int getSign() {
        return (dir % 2) * 2 - 1;
    }
}
