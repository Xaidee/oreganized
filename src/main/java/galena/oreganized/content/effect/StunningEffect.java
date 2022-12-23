package galena.oreganized.content.effect;

import galena.oreganized.index.OEffects;
import net.minecraft.client.player.Input;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.MovementInputUpdateEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.jetbrains.annotations.NotNull;

import galena.oreganized.Oreganized;

@Mod.EventBusSubscriber(modid = Oreganized.MOD_ID, value = Dist.CLIENT)
public class StunningEffect extends MobEffect {

    public static boolean flag = false; // Flag to check if entity should be paralysed
    public static int coolDown = 0;
    public StunningEffect() {
        super(MobEffectCategory.HARMFUL, 0x3B3B63);
    }

    @Override
    public void applyEffectTick(@NotNull LivingEntity entity, int amplifier) {
        if (coolDown <= 0) {
            // amplifier multiplies only the time frozen, making the relative gap larger the higher the level
            coolDown = entity.getLevel().getRandom().nextInt(120 * (flag ? amplifier + 1 : 1)) + 20;
            flag = !flag; // Toggle flag, if should be paralysed flag = true, else flag = false
        }
        coolDown--; // cool down is decremented every in game tick
    }

    @SubscribeEvent // applyStunned for Mobs
    public static void applyStunned(LivingEvent.LivingUpdateEvent event) {
        LivingEntity entity = (LivingEntity) event.getEntity();
        if ((!(entity instanceof Player)) && entity.hasEffect(OEffects.STUNNING.get()) && flag) {
            // Copied from LivingEntity.aiStep() when isImmobile() is true
            entity.setJumping(false);
            entity.xxa = 0.0F;
            entity.zza = 0.0F;
        }
    }

    @SubscribeEvent // applyStunned for Players
    public static void applyStunned(MovementInputUpdateEvent event) {
        Input input = event.getInput(); // Gets player movement input
        LivingEntity entity = (LivingEntity) event.getEntity();
        if (entity.hasEffect(OEffects.STUNNING.get()) && flag) {
            // Disable all movement related input by setting it to false or 0
            input.up = false;
            input.down = false;
            input.left = false;
            input.right = false;
            input.forwardImpulse = 0;
            input.leftImpulse = 0;
            input.jumping = false;
            input.shiftKeyDown = false;
        }
    }

    @Override
    public boolean isDurationEffectTick(int pDuration, int pAmplifier) {
        return this == OEffects.STUNNING.get();
    }
}
