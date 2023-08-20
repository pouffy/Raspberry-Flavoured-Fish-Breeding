package com.pouffydev.pisciculture.registry;

import com.pouffydev.pisciculture.fish.breeding.RoeBlock;
import com.pouffydev.pisciculture.fish.cod.CodRoeBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import static com.pouffydev.pisciculture.Pisciculture.MODID;

public class PCBlocks {
    public static final DeferredRegister<Block> BLOCKS;

    public static final RegistryObject<Block> COD_ROE;
    public PCBlocks() {
    }
    static {
        BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, MODID);
        COD_ROE = BLOCKS.register("cod_roe", () -> {
            return new CodRoeBlock(BlockBehaviour.Properties.copy(Blocks.SLIME_BLOCK).noCollission());
        });
    }
    public static void register(IEventBus eventBus) {
        BLOCKS.register(eventBus);
    }
}
