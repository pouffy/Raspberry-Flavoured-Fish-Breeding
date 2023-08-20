package com.pouffydev.pisciculture.registry;

import com.pouffydev.pisciculture.Pisciculture;
import com.pouffydev.pisciculture.fish.cod.CodEntity;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.Level;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import javax.annotation.Nullable;

import static com.pouffydev.pisciculture.Pisciculture.MODID;

public class PCEntities {
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES;
    public static final RegistryObject<EntityType<CodEntity>> COD;
    public PCEntities() {
    }

    static {
        ENTITY_TYPES = DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, Pisciculture.MODID);

        COD = ENTITY_TYPES.register("cod", () ->
            EntityType.Builder.of(CodEntity::new, MobCategory.WATER_AMBIENT)
                    .sized(0.5f, 0.3f)
                    .clientTrackingRange(4)
                    .build(new ResourceLocation(MODID, "cod").toString()));
    }
    public static void register(IEventBus eventBus) {
        ENTITY_TYPES.register(eventBus);
    }
}
