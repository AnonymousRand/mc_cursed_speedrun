package net.anonymousrand.cursedspeedrun.mixin;

import net.minecraft.block.BlockState;
import net.minecraft.block.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.BucketItem;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.tag.FluidTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BucketItem.class)
public abstract class MixinBucketItem {

    @Nullable
    private PlayerEntity player;
    private World world;
    private BlockPos pos;

    @Shadow
    private Fluid fluid;

    @ModifyVariable(method = "placeFluid", at = @At("HEAD"), index = 1)
    private PlayerEntity getPlayer(PlayerEntity player) {
        this.player = player;
        return player;
    }

    @ModifyVariable(method = "placeFluid", at = @At("HEAD"), index = 2)
    private World getWorld(World world) {
        this.world = world;
        return world;
    }

    @ModifyVariable(method = "placeFluid", at = @At("HEAD"), index = 3)
    private BlockPos getPos(BlockPos pos) {
        this.pos = pos;
        return pos;
    }

    @Inject(method = "placeFluid", at = @At("HEAD"), cancellable = true)
    private void placeWaterInNether(CallbackInfoReturnable<Boolean> info) { /**water can be placed in nether*/
        if (this.world.getDimension().isUltrawarm() && this.fluid.isIn(FluidTags.WATER)) {
            BlockState blockState = world.getBlockState(pos);
            Material material = blockState.getMaterial();
            boolean bl = blockState.canBucketPlace(this.fluid);

            if (!this.world.isClient && bl && !material.isLiquid()) {
                this.world.breakBlock(this.pos, true);
            }

            if (!this.world.setBlockState(this.pos, this.fluid.getDefaultState().getBlockState(), 11) && !blockState.getFluidState().isStill()) {
                info.setReturnValue(false);
            } else {
                this.playEmptyingSound(this.player, this.world, this.pos);
                info.setReturnValue(true);
            }
        }
    }

    @Inject(method = "placeFluid", at = @At("RETURN"), cancellable = true)
    private void makeSureItStillWorks(CallbackInfoReturnable<Boolean> info) {
        info.setReturnValue(true);
    }

    public void playEmptyingSound(@Nullable PlayerEntity player, WorldAccess world, BlockPos pos) {
        SoundEvent soundEvent = this.fluid.isIn(FluidTags.LAVA) ? SoundEvents.ITEM_BUCKET_EMPTY_LAVA : SoundEvents.ITEM_BUCKET_EMPTY;
        world.playSound(player, pos, soundEvent, SoundCategory.BLOCKS, 1.0F, 1.0F);
    }
}
