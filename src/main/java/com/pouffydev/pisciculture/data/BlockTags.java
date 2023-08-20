package com.pouffydev.pisciculture.data;

import com.pouffydev.pisciculture.registry.PCTags;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.tags.BlockTagsProvider;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.common.data.ExistingFileHelper;

import javax.annotation.Nullable;

public class BlockTags extends BlockTagsProvider {
    public BlockTags(DataGenerator generatorIn, String modId, @Nullable ExistingFileHelper existingFileHelper) {
        super(generatorIn, modId, existingFileHelper);
    }
    protected void addTags() {
        this.registerModTags();
    }

    protected void registerModTags() {
        this.tag(PCTags.AllBlockTags.COD_ROE_REPLACEABLE.tag).add(new Block[]{((Blocks.SEAGRASS))});
    }
}
