package galena.oreganized.mixin;

import galena.oreganized.world.ScaredOfGargoyleGoal;
import net.minecraft.world.entity.Mob;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Mob.class)
public class MobMixin {

    @Inject(
            method = "<init>",
            at = @At("RETURN")
    )
    private void addScaredOfGargoyleGoal(CallbackInfo ci) {
        var self = (Mob) (Object) this;
        if(self.level() == null || self.level().isClientSide()) return;
        ScaredOfGargoyleGoal.addGoal(self);
    }
}
