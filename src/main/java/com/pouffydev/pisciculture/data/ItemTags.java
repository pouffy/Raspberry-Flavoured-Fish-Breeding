package com.pouffydev.pisciculture.data;

import com.pouffydev.pisciculture.registry.PCItems;
import com.pouffydev.pisciculture.registry.PCTags;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.tags.BlockTagsProvider;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.world.entity.EntityType;
import net.minecraftforge.common.data.ExistingFileHelper;

import javax.annotation.Nullable;

public class ItemTags extends ItemTagsProvider {
    public ItemTags(DataGenerator generatorIn, BlockTagsProvider blockTagProvider, String modId, @Nullable ExistingFileHelper existingFileHelper) {
        super(generatorIn, blockTagProvider, modId, existingFileHelper);
    }
    protected void addTags() {
        this.registerModTags();
    }
    
    protected void registerModTags() {
        this.tag(PCTags.AllItemTags.COD_FOOD.tag).add(PCItems.WORM.get());
    }
}
