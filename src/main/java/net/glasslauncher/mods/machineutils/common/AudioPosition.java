// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode 

package net.glasslauncher.mods.machineutils.common;

import net.minecraft.entity.EntityBase;
import net.minecraft.tileentity.TileEntityBase;

// Referenced classes of package ic2.common:
//            PositionSpec

public class AudioPosition {

    public float x;
    public float y;
    public float z;
    public AudioPosition(float f, float f1, float f2) {
        x = f;
        y = f1;
        z = f2;
    }

    public static AudioPosition getFrom(Object obj, PositionSpec positionspec) {
        if (obj instanceof AudioPosition) {
            return (AudioPosition) obj;
        }
        if (obj instanceof EntityBase) {
            EntityBase entity = (EntityBase) obj;
            return new AudioPosition((float) entity.x, (float) entity.y, (float) entity.z);
        }
        if (obj instanceof TileEntityBase) {
            TileEntityBase tileentity = (TileEntityBase) obj;
            return new AudioPosition((float) tileentity.x + 0.5F, (float) tileentity.y + 0.5F, (float) tileentity.z + 0.5F);
        } else {
            return null;
        }
    }
}
