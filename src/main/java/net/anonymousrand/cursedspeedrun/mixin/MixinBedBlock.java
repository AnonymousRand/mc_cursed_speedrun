package net.anonymousrand.cursedspeedrun.mixin;

import net.minecraft.block.BedBlock;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BedBlock.class)
public abstract class MixinBedBlock {
    @Inject(method = "isOverworld", at = @At("RETURN"), cancellable = true)
    private static void noExplosion(CallbackInfoReturnable<Boolean> bool) { /**beds do not explode in the nether or end*/
        bool.setReturnValue(true);
    }
}
