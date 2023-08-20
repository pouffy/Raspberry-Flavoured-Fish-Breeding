package com.pouffydev.pisciculture.fish.cod;

import com.pouffydev.pisciculture.fish.breeding.AbstractBreedableFish;
import com.pouffydev.pisciculture.fish.breeding.RoeBlock;
import com.pouffydev.pisciculture.registry.PCBlocks;
import com.pouffydev.pisciculture.registry.PCEntities;
import com.pouffydev.pisciculture.registry.PCTags;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.tags.BiomeTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.animal.Pufferfish;
import net.minecraft.world.entity.animal.Turtle;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraftforge.event.entity.EntityEvent;

import javax.annotation.Nullable;

@MethodsReturnNonnullByDefault
@SuppressWarnings({"DataFlowIssue", "ConstantValue"})
public class CodEntity extends AbstractBreedableFish {
    private static final EntityDataAccessor<Boolean> HAS_EGG = SynchedEntityData.defineId(CodEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> LAYING_EGG = SynchedEntityData.defineId(CodEntity.class, EntityDataSerializers.BOOLEAN);
    public static final Ingredient FOOD_ITEMS = Ingredient.of(PCTags.AllItemTags.COD_FOOD.tag);
    int layEggCounter;
    boolean isReachedTarget;
    public CodEntity(EntityType<? extends AbstractBreedableFish> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }
    public boolean hasEgg() {
        return this.entityData.get(HAS_EGG);
    }

    @SuppressWarnings("SameParameterValue")
    void setHasEgg(boolean pHasEgg) {
        this.entityData.set(HAS_EGG, pHasEgg);
    }

    public boolean isLayingEgg() {
        return this.entityData.get(LAYING_EGG);
    }
    void setLayingEgg(boolean pIsLayingEgg) {
        this.layEggCounter = pIsLayingEgg ? 1 : 0;
        this.entityData.set(LAYING_EGG, pIsLayingEgg);
    }
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(HAS_EGG, false);
        this.entityData.define(LAYING_EGG, false);
    }
    public void addAdditionalSaveData(CompoundTag pCompound) {
        super.addAdditionalSaveData(pCompound);
        pCompound.putBoolean("HasEgg", this.hasEgg());
    }
    protected void registerGoals() {
        this.goalSelector.addGoal(1, new FishBreedGoal(this, 1.0D));
        this.goalSelector.addGoal(2, new TemptGoal(this, 1.1D, FOOD_ITEMS, false));
        this.goalSelector.addGoal(1, new TravelToBottomAndLayEggs(this));
        this.goalSelector.addGoal(0, new PanicGoal(this, 1.25D));
        this.goalSelector.addGoal(2, new AvoidEntityGoal<>(this, Player.class, 8.0F, 1.6D, 1.4D, EntitySelector.NO_SPECTATORS::test));
        this.goalSelector.addGoal(4, new FishSwimGoal(this));
    }

    @Override
    protected SoundEvent getFlopSound() {
        return SoundEvents.COD_FLOP;
    }

    @Override
    public ItemStack getBucketItemStack() {
        return null;
    }
    public boolean isFood(ItemStack pStack) {
        return pStack.is(PCTags.AllItemTags.COD_FOOD.tag);
    }
    @Nullable
    public AgeableMob getBreedOffspring(ServerLevel pLevel, AgeableMob pOtherParent) {
        return PCEntities.COD.get().create(pLevel);
    }
    public InteractionResult mobInteract(Player pPlayer, InteractionHand pHand) {
        ItemStack itemstack = pPlayer.getItemInHand(pHand);
        if (this.isFood(itemstack)) {
            int i = this.getAge();
            if (!this.level.isClientSide && i == 0 && this.canFallInLove()) {
                this.usePlayerItem(pPlayer, pHand, itemstack);
                this.setInLove(pPlayer);
                return InteractionResult.SUCCESS;
            }

            if (this.isBaby()) {
                this.usePlayerItem(pPlayer, pHand, itemstack);
                this.ageUp(getSpeedUpSecondsWhenFeeding(-i), true);
                return InteractionResult.sidedSuccess(this.level.isClientSide);
            }

            if (this.level.isClientSide) {
                return InteractionResult.CONSUME;
            }
        }

        return super.mobInteract(pPlayer, pHand);
    }
    static class FishBreedGoal extends BreedGoal {
        private final CodEntity fish;

        FishBreedGoal(CodEntity pFish, double pSpeedModifier) {
            super(pFish, pSpeedModifier);
            this.fish = pFish;
        }

        /**
         * Returns whether execution should begin. You can also read and cache any state necessary for execution in this
         * method as well.
         */
        public boolean canUse() {
            return super.canUse() && !this.fish.hasEgg();
        }

        /**
         * Spawns a baby animal of the same type.
         */
        protected void breed() {
            ServerPlayer serverplayer = this.animal.getLoveCause();
            if (serverplayer == null && this.partner.getLoveCause() != null) {
                serverplayer = this.partner.getLoveCause();
            }

            if (serverplayer != null) {
                serverplayer.awardStat(Stats.ANIMALS_BRED);
                CriteriaTriggers.BRED_ANIMALS.trigger(serverplayer, this.animal, this.partner, (AgeableMob)null);
            }

            this.fish.setHasEgg(true);
            this.animal.resetLove();
            this.partner.resetLove();
            RandomSource randomsource = this.animal.getRandom();
            if (this.level.getGameRules().getBoolean(GameRules.RULE_DOMOBLOOT)) {
                this.level.addFreshEntity(new ExperienceOrb(this.level, this.animal.getX(), this.animal.getY(), this.animal.getZ(), randomsource.nextInt(7) + 1));
            }

        }
    }
    static class FishSwimGoal extends RandomSwimmingGoal {
        private final CodEntity fish;

        public FishSwimGoal(CodEntity pFish) {
            super(pFish, 1.0D, 40);
            this.fish = pFish;
        }

        /**
         * Returns whether execution should begin. You can also read and cache any state necessary for execution in this
         * method as well.
         */
        public boolean canUse() {
            return this.fish.canRandomSwim() && super.canUse();
        }
    }
    static class TravelToBottomAndLayEggs extends MoveToBlockGoal {
        private final CodEntity cod;
        public TravelToBottomAndLayEggs(CodEntity cod) {
            super(cod, 1.0D, 16);
            this.cod = cod;
        }

        public boolean canUse() {
            return this.cod.hasEgg() && super.canUse();
        }

        /**
         * Returns whether an in-progress EntityAIBase should continue executing
         */
        public boolean canContinueToUse() {
            return super.canContinueToUse() && this.cod.hasEgg();
        }

        @Override
        public void start() {
            BlockPos bottomBlock = findBottomBlock(cod.blockPosition());

            if (bottomBlock != null && this.cod.hasEgg()) {
                cod.getNavigation().moveTo(bottomBlock.getX(), bottomBlock.getY(), bottomBlock.getZ(), 1.0D);
            }
        }

        protected boolean isValidTarget(LevelReader pLevel, BlockPos pPos) {
            return pLevel.isWaterAt(pPos.above()) && RoeBlock.isFloor(pLevel, pPos);
        }

        public void tick() {
            Level world = cod.getLevel();
            super.tick();
            BlockPos blockpos = this.cod.blockPosition();
            if (this.cod.isInWater() && this.cod.isReachedTarget) {
                if (this.cod.layEggCounter < 1) {
                    this.cod.setLayingEgg(true);
                } else if (this.cod.layEggCounter > this.adjustedTickDelay(200)) {
                    Level level = this.cod.level;
                    if (world.getBlockState(this.blockPos).is(Blocks.SEAGRASS)) {
                        level.playSound(null, blockpos, SoundEvents.TURTLE_LAY_EGG, SoundSource.BLOCKS, 0.3F, 0.9F + level.random.nextFloat() * 0.2F);
                        level.setBlock(this.blockPos, PCBlocks.COD_ROE.get().defaultBlockState().setValue(RoeBlock.EGGS, this.cod.random.nextInt(4) + 1), 3);
                        this.cod.setHasEgg(false);
                        this.cod.setLayingEgg(false);
                        this.cod.setInLoveTime(600);
                    }
                }

                if (this.cod.isLayingEgg()) {
                    ++this.cod.layEggCounter;
                }
            }

        }
        private BlockPos findBottomBlock(BlockPos startPos) {
            Level world = cod.getLevel();
            LevelChunk chunk = world.getChunkAt(startPos);

            int lowestY = -1; // Initialize with an invalid Y value
            BlockPos lowestBlockPos = null;

            for (int localX = 0; localX < 16; localX++) {
                for (int localZ = 0; localZ < 16; localZ++) {
                    BlockPos globalPos = chunk.getPos().getBlockAt(localX, 0, localZ);

                    int y = chunk.getHeight(Heightmap.Types.MOTION_BLOCKING, localX, localZ);

                    if (lowestY == -1 || y < lowestY) {
                        BlockState blockState = world.getBlockState(globalPos);

                        if (!blockState.getMaterial().isLiquid() && blockState.getBlock() == Blocks.SEAGRASS) {
                            lowestY = y;
                            lowestBlockPos = globalPos;
                        }
                    }
                }
            }

            this.cod.isReachedTarget = true;
            return lowestBlockPos;
        }
    }

}
