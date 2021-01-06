package net.glasslauncher.mods.machineutils.api.item;

import java.nio.ByteBuffer;

public interface IPersistentItemData {

    void loadFromBuffer(short word0, short word1, ByteBuffer bytebuffer);

    ByteBuffer saveToBuffer();

    short getVersion();
}
