// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode 

package net.glasslauncher.mods.machineutils.api;

import net.minecraft.tileentity.TileEntityBase;

public enum Direction
{
	
    XN(0),
    XP(1),
    YN(2),
    YP(3),
    ZN(4),
    ZP(5);

    private Direction(int j)
    {
        dir = j;
    }

    public TileEntityBase applyToTileEntity(TileEntityBase tileentity)
    {
        int ai[] = {
            tileentity.x, tileentity.y, tileentity.z
        };
        ai[dir / 2] += getSign();
        if(tileentity.level != null && tileentity.level.isTileLoaded(ai[0], ai[1], ai[2]))
        {
            return tileentity.level.getTileEntity(ai[0], ai[1], ai[2]);
        } else
        {
            return null;
        }
    }

    public Direction getInverse()
    {
        int i = dir - getSign();
        Direction adirection[] = values();
        int j = adirection.length;
        for(int k = 0; k < j; k++)
        {
            Direction direction = adirection[k];
            if(direction.dir == i)
            {
                return direction;
            }
        }

        return this;
    }

    public int toSideValue()
    {
        return (dir + 4) % 6;
    }

    private int getSign()
    {
        return (dir % 2) * 2 - 1;
    }

    private int dir;
}
