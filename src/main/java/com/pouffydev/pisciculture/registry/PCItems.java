package com.pouffydev.pisciculture.registry;

import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.SpawnEggItem;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import vectorwing.farmersdelight.FarmersDelight;
import vectorwing.farmersdelight.common.FoodValues;

import static com.pouffydev.pisciculture.Pisciculture.MODID;

public class PCItems {
    public static final DeferredRegister<Item> ITEMS;

    public static final RegistryObject<Item> codSpawnEgg;
    public static final RegistryObject<Item> WORM;

    //BlockItems
    public static final RegistryObject<Item> codRoe;

    public PCItems() {
    }
    public static Item.Properties foodItem(FoodProperties food) {
        return (new Item.Properties()).food(food).tab(CreativeModeTab.TAB_FOOD);
    }
    static {

        ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, MODID);
        WORM  = ITEMS.register("worm", () -> {
            return new Item(foodItem(new FoodProperties.Builder().nutrition(1).saturationMod(0.1f).build()));
        });
        codSpawnEgg = ITEMS.register("cod_spawn_egg", () -> {
            return new SpawnEggItem(PCEntities.COD.get(), 12691306, 15058059, (new Item.Properties()).tab(CreativeModeTab.TAB_MISC));
        });
        codRoe = ITEMS.register("cod_roe", () -> {
            return new BlockItem(PCBlocks.COD_ROE.get(), (new Item.Properties().tab(CreativeModeTab.TAB_MISC)));
        });
    }

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}
