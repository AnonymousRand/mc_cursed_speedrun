package net.anonymousrand.cursedspeedrun.mixin;

import net.minecraft.block.AbstractFireBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AbstractFireBlock.class)
public abstract class MixinAbstractFireBlock {

    private World world;
    private BlockPos pos;

    @ModifyVariable(method = "onBlockAdded", at = @At("HEAD"), ordinal = 0)
    private World getWorld(World world) {
        this.world = world;
        return world;
    }

    @ModifyVariable(method = "onBlockAdded", at = @At("HEAD"), ordinal = 0)
    private BlockPos getBlockPos(BlockPos pos) {
        this.pos = pos;
        return pos;
    }

    @Inject(method = "onBlockAdded", at = @At("TAIL"))
    private void noFireOnFlintAndSteel(CallbackInfo info) { //to make sure fire doesn't break artificial nether portals
        if (this.world.getBlockState(this.pos.down()).getBlock() == Blocks.OBSIDIAN) {
            this.world.setBlockState(this.pos, Blocks.NETHER_PORTAL.getDefaultState(), 18);
        }
    }
}
