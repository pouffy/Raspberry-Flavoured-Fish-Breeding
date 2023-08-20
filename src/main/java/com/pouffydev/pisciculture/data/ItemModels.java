package com.pouffydev.pisciculture.data;

import com.google.common.collect.Sets;
import com.pouffydev.pisciculture.Pisciculture;
import com.pouffydev.pisciculture.registry.PCItems;
import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.ForgeRegistries;
import vectorwing.farmersdelight.common.registry.ModItems;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class ItemModels extends ItemModelProvider {
    public static final String GENERATED = "item/generated";
    public static final String HANDHELD = "item/handheld";
    public static final String EGG = "item/template_spawn_egg";
    public static final ResourceLocation MUG = new ResourceLocation("pisciculture", "item/mug");

    public ItemModels(DataGenerator generator, ExistingFileHelper existingFileHelper) {
        super(generator, "pisciculture", existingFileHelper);
    }
    protected void registerModels() {
        Set<Item> items = ForgeRegistries.ITEMS.getValues().stream().filter(i -> Pisciculture.MODID.equals(ForgeRegistries.ITEMS.getKey(i).getNamespace()))
                .collect(Collectors.toSet());
        Set<Item> spawnEggItems = Sets.newHashSet((Item) PCItems.codSpawnEgg.get());
        takeAll(items, (Object[])((Item[])spawnEggItems.toArray(new Item[0]))).forEach((item) -> {
            this.itemSpawnEgg((Item) item);
        });
        this.itemGeneratedModel((Item)PCItems.codRoe.get(), this.resourceItem(this.itemName((Item)PCItems.codRoe.get())));
        items.remove(PCItems.codRoe.get());
        items.forEach(item -> itemGeneratedModel(item, resourceItem(itemName(item))));


    }
    public void itemGeneratedModel(Item item, ResourceLocation texture) {
        withExistingParent(itemName(item), GENERATED).texture("layer0", texture);
    }
    public void itemSpawnEgg(Item item) {
        withExistingParent(itemName(item), EGG);
    }
    public ResourceLocation resourceItem(String path) {
        return new ResourceLocation(Pisciculture.MODID, "item/" + path);
    }

    private String itemName(Item item) {
        return ForgeRegistries.ITEMS.getKey(item).getPath();
    }
    @SafeVarargs
    @SuppressWarnings("varargs")
    public static <T> Collection<T> takeAll(Set<? extends T> src, T... items) {
        List<T> ret = Arrays.asList(items);
        for (T item : items) {
            if (!src.contains(item)) {
                Pisciculture.LOGGER.warn("Item {} not found in set", item);
            }
        }
        if (!src.removeAll(ret)) {
            Pisciculture.LOGGER.warn("takeAll array didn't yield anything ({})", Arrays.toString(items));
        }
        return ret;
    }

    public static <T> Collection<T> takeAll(Set<T> src, Predicate<T> pred) {
        List<T> ret = new ArrayList<>();

        Iterator<T> iter = src.iterator();
        while (iter.hasNext()) {
            T item = iter.next();
            if (pred.test(item)) {
                iter.remove();
                ret.add(item);
            }
        }

        if (ret.isEmpty()) {
            Pisciculture.LOGGER.warn("takeAll predicate yielded nothing", new Throwable());
        }
        return ret;
    }
}
