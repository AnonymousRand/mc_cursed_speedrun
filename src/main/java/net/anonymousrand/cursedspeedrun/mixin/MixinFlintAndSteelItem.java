package net.anonymousrand.cursedspeedrun.mixin;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.item.FlintAndSteelItem;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(FlintAndSteelItem.class)
public abstract class MixinFlintAndSteelItem {

    private World world;
    private BlockPos pos;

    @ModifyVariable(method = "useOnBlock", at = @At("HEAD"), ordinal = 0)
    private ItemUsageContext getContext(ItemUsageContext context) { /**nether portals can be created anywhere*/
        this.world = context.getWorld();
        this.pos = context.getBlockPos().up();
        return context;
    }

    @Inject(method = "useOnBlock", at = @At("RETURN"))
    private void createPortal(CallbackInfoReturnable<ActionResult> info) {
        if (info.getReturnValue() == ActionResult.SUCCESS) {
            this.world.setBlockState(this.pos, Blocks.NETHER_PORTAL.getDefaultState(), 18);
            this.world.setBlockState(this.pos.up(), Blocks.NETHER_PORTAL.getDefaultState(), 18);
            this.world.setBlockState(this.pos.west(), Blocks.NETHER_PORTAL.getDefaultState(), 18);
            this.world.setBlockState(this.pos.up().west(), Blocks.NETHER_PORTAL.getDefaultState(), 18);
        }
    }
}
