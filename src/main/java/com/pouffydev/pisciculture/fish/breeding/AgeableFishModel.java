package com.pouffydev.pisciculture.fish.breeding;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;

import java.util.function.Function;

public abstract class AgeableFishModel<T extends Entity> extends HierarchicalModel<T> {
    private float babyBodyScale;
    private float bodyYOffset;
    private boolean scaleBody;
    protected AgeableFishModel(boolean pScaleBody, float pBabyBodyScale, float pBodyYOffset) {
        this(RenderType::entityCutoutNoCull, pScaleBody, pBabyBodyScale, pBodyYOffset);
    }

    protected AgeableFishModel(Function<ResourceLocation, RenderType> pRenderType, boolean pScaleBody,float pBabyBodyScale, float pBodyYOffset) {
        super(pRenderType);
        this.scaleBody = pScaleBody;
        this.babyBodyScale = pBabyBodyScale;
        this.bodyYOffset = pBodyYOffset;
    }
    protected AgeableFishModel() {
        this(false, 5.0F, 2.0F);
    }

    public void renderToBuffer(PoseStack pPoseStack, VertexConsumer pBuffer, int pPackedLight, int pPackedOverlay, float pRed, float pGreen, float pBlue, float pAlpha) {
        if (this.young) {
            pPoseStack.pushPose();
            if (this.scaleBody) {
                float f = 1.5F / this.babyBodyScale;
                pPoseStack.scale(f, f, f);
            }

            pPoseStack.popPose();
            pPoseStack.pushPose();
            float f1 = 1.0F / this.babyBodyScale;
            pPoseStack.scale(f1, f1, f1);
            pPoseStack.translate(0.0D, (double)(this.bodyYOffset / 16.0F), 0.0D);
            this.bodyParts().forEach((p_102071_) -> {
                p_102071_.render(pPoseStack, pBuffer, pPackedLight, pPackedOverlay, pRed, pGreen, pBlue, pAlpha);
            });
            pPoseStack.popPose();
        } else {
            this.bodyParts().forEach((p_102051_) -> {
                p_102051_.render(pPoseStack, pBuffer, pPackedLight, pPackedOverlay, pRed, pGreen, pBlue, pAlpha);
            });
        }

    }

    @Override
    public ModelPart root() {
        return null;
    }

    protected abstract Iterable<ModelPart> bodyParts();

}
