package com.pouffydev.pisciculture.data;

import com.pouffydev.pisciculture.registry.PCEntities;
import com.pouffydev.pisciculture.registry.PCTags;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.tags.EntityTypeTagsProvider;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.world.entity.EntityType;
import net.minecraftforge.common.data.ExistingFileHelper;
import vectorwing.farmersdelight.common.tag.ModTags;

import javax.annotation.Nullable;

public class EntityTags extends EntityTypeTagsProvider {
    public EntityTags(DataGenerator generator, String modId, @Nullable ExistingFileHelper existingFileHelper) {
        super(generator, modId, existingFileHelper);
    }
    protected void addTags() {
        this.tag(PCTags.AllEntityTags.COD.tag).add(EntityType.COD, PCEntities.COD.get());
        this.tag(EntityTypeTags.AXOLOTL_HUNT_TARGETS).add(PCEntities.COD.get());
    }
}
