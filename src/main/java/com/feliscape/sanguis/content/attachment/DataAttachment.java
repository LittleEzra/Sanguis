package com.feliscape.sanguis.content.attachment;

import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.neoforged.neoforge.common.util.INBTSerializable;

public class DataAttachment implements INBTSerializable<CompoundTag> {

    protected void load(CompoundTag tag){

    }
    protected void save(CompoundTag tag){

    }

    @Override
    public CompoundTag serializeNBT(HolderLookup.Provider provider) {
        CompoundTag tag = new CompoundTag();
        save(tag);
        return tag;
    }

    @Override
    public void deserializeNBT(HolderLookup.Provider provider, CompoundTag tag) {
        load(tag);
    }
}
