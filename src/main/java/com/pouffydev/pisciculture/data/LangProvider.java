package com.pouffydev.pisciculture.data;

import com.pouffydev.pisciculture.Pisciculture;
import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.data.LanguageProvider;
import net.minecraftforge.registries.ForgeRegistries;
import org.apache.commons.lang3.text.WordUtils;

import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public class LangProvider extends LanguageProvider {
    public LangProvider(DataGenerator gen) {
        super(gen, Pisciculture.MODID, "en_us");
    }
    @Override
    protected void addTranslations() {
        Set<Item> items = ForgeRegistries.ITEMS.getValues().stream().filter(i -> Pisciculture.MODID.equals(Objects.requireNonNull(ForgeRegistries.ITEMS.getKey(i)).getNamespace()))
                .collect(Collectors.toSet());
        items.forEach(this::add);
    }
    private void add(Item item) {
        this.add(item, format(Objects.requireNonNull(ForgeRegistries.ITEMS.getKey(item))));
    }
    private void add(Block block) {
        this.add(block, format(Objects.requireNonNull(ForgeRegistries.BLOCKS.getKey(block))));
    }
    @SuppressWarnings("deprecation")
    private String format(ResourceLocation registryName) {
        return WordUtils.capitalizeFully(registryName.getPath().replace("_", " ")).replace("With", "with");
    }
}
