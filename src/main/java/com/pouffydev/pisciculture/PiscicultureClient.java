package com.pouffydev.pisciculture;

import com.pouffydev.pisciculture.fish.cod.CodRenderer;
import com.pouffydev.pisciculture.registry.PCBlocks;
import com.pouffydev.pisciculture.registry.PCEntities;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.entity.animal.Cod;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

public class PiscicultureClient {
    public PiscicultureClient() {
    }

    public static void init(FMLClientSetupEvent event) {
       registerRenderers();
    }
    public static void registerRenderers() {
        EntityRenderersEvent.RegisterRenderers event = new EntityRenderersEvent.RegisterRenderers();
        event.registerEntityRenderer(PCEntities.COD.get(), CodRenderer::new);
    }
}
