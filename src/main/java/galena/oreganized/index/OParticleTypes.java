package galena.oreganized.index;

import galena.oreganized.Oreganized;
import galena.oreganized.client.particle.CustomDrippingParticle;
import galena.oreganized.client.particle.LeadShrapnelParticle;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.ParticleEngine;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraftforge.client.event.ParticleFactoryRegisterEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

@Mod.EventBusSubscriber(modid = Oreganized.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class OParticleTypes {

    public static final DeferredRegister<ParticleType<?>> PARTICLES = DeferredRegister.create(ForgeRegistries.PARTICLE_TYPES, Oreganized.MOD_ID);

    public static final RegistryObject<SimpleParticleType> DRIPPING_LEAD = PARTICLES.register("dripping_lead", () -> new SimpleParticleType(true));
    public static final RegistryObject<SimpleParticleType> FALLING_LEAD = PARTICLES.register("falling_lead", () -> new SimpleParticleType(true));
    public static final RegistryObject<SimpleParticleType> LANDING_LEAD = PARTICLES.register("landing_lead", () -> new SimpleParticleType(true));
    public static final RegistryObject<SimpleParticleType> LEAD_SHRAPNEL = PARTICLES.register( "lead_shrapnel", () -> new SimpleParticleType(true));


    @SubscribeEvent
    public static void registerParticleFactories(ParticleFactoryRegisterEvent event) {
        ParticleEngine engine = Minecraft.getInstance().particleEngine;

        engine.register(DRIPPING_LEAD.get(), CustomDrippingParticle.LeadHangProvider::new);
        engine.register(FALLING_LEAD.get(), CustomDrippingParticle.LeadFallProvider::new);
        engine.register(LANDING_LEAD.get(), CustomDrippingParticle.LeadLandProvider::new);
        engine.register(LEAD_SHRAPNEL.get(), LeadShrapnelParticle.Provider::new);
    }
}
