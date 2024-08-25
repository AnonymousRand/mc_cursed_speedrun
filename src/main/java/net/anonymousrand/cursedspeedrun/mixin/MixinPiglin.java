package net.anonymousrand.cursedspeedrun.mixin;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.mob.PiglinEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PiglinEntity.class)
public abstract class MixinPiglin {

    @Inject(method = "<init>", at = @At("TAIL"))
    private void cantConvert(CallbackInfo info) { /**piglins are immune to zombification*/
        ((PiglinEntity)(Object)this).setImmuneToZombification(true);
    }
}
