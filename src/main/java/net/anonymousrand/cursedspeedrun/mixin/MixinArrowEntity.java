package net.anonymousrand.cursedspeedrun.mixin;

import net.minecraft.entity.mob.PiglinEntity;
import net.minecraft.entity.projectile.ArrowEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PersistentProjectileEntity.class)
public abstract class MixinArrowEntity {

    private PersistentProjectileEntity arrow = ((PersistentProjectileEntity)(Object)this);

    @Inject(method = "tick", at = @At("TAIL"))
    private void setNoVelocity(CallbackInfo info) { /**arrows drop prematurely*/
        this.arrow.setVelocity(this.arrow.getVelocity().add(new Vec3d(0.0, -0.5, 0.0)));
    }
}
