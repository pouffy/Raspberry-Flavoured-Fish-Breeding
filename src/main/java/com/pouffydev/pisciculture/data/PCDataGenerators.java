package com.pouffydev.pisciculture.data;

import net.minecraft.data.DataGenerator;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(
        modid = "pisciculture",
        bus = Mod.EventBusSubscriber.Bus.MOD
)
public class PCDataGenerators {
    public PCDataGenerators() {
    }

    @SubscribeEvent
    public static void gatherData(GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();
        ExistingFileHelper helper = event.getExistingFileHelper();
        if (event.includeServer()) {
            BlockTags blockTags = new BlockTags(generator, "pisciculture", helper);
            generator.addProvider(true, blockTags);
            generator.addProvider(true, new ItemTags(generator, blockTags, "pisciculture", helper));
            generator.addProvider(true, new EntityTags(generator, "pisciculture", helper));

        }

        if (event.includeClient()) {
            BlockStates blockStates = new BlockStates(generator, helper);
            generator.addProvider(true, blockStates);
            generator.addProvider(true, new ItemModels(generator, blockStates.models().existingFileHelper));
            generator.addProvider(true, new LangProvider(generator));
        }

    }
}
