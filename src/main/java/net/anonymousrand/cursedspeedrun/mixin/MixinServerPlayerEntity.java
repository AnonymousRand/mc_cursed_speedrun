package net.anonymousrand.cursedspeedrun.mixin;

import com.mojang.datafixers.util.Either;
import net.minecraft.advancement.criterion.Criteria;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Unit;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ServerPlayerEntity.class)
public abstract class MixinServerPlayerEntity {

    private PlayerEntity player = ((PlayerEntity)(Object)this);
    private BlockPos pos = null;
    private int sleepTime;

    @ModifyVariable(method = "trySleep", at = @At("HEAD"), ordinal = 0)
    private BlockPos getPos(BlockPos pos) { //to get the first and only parameter for trySleep()
        this.pos = pos;
        return pos;
    }

    @Inject(method = "trySleep", at = @At("HEAD"))
    private void sleepNether(CallbackInfoReturnable<Either<PlayerEntity.SleepFailureReason, Unit>> info) { /**can sleep in nether and end*/
        this.player.sleep(this.pos); //can't directly use PlayerEntity.trySleep() as  it causes stack overflow
        this.sleepTime = 0;
    }

    @Inject(method = "tick", at = @At("TAIL"))
    private void wakeUp(CallbackInfo info) { //make sure player wakes up after 5 secs
        if (this.player.isSleeping()) {
            this.sleepTime++;
        }

        if (this.sleepTime > 100 && this.player.isSleeping()) {
            this.player.wakeUp();
            this.sleepTime = 0;
        }
    }
}
