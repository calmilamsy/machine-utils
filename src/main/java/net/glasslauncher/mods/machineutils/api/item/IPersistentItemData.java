// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode 

package net.glasslauncher.mods.machineutils.api.item;

import java.nio.ByteBuffer;

public interface IPersistentItemData {

    void loadFromBuffer(short word0, short word1, ByteBuffer bytebuffer);

    ByteBuffer saveToBuffer();

    short getVersion();
}
