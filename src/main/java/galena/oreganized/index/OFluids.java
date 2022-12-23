package galena.oreganized.index;

import galena.oreganized.Oreganized;
import galena.oreganized.content.fluid.MoltenLeadFluid;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.fluids.FluidAttributes;
import net.minecraftforge.fluids.ForgeFlowingFluid;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class OFluids {

    public static final DeferredRegister<Fluid> FLUIDS = DeferredRegister.create(ForgeRegistries.FLUIDS, Oreganized.MOD_ID);

    public static final FluidAttributes.Builder MOLTEN_LEAD_TYPE = FluidAttributes.builder(
            new ResourceLocation(Oreganized.MOD_ID, "fluid/molten_lead"),
            new ResourceLocation(Oreganized.MOD_ID, "fluid/molten_lead_flowing")
    ).luminosity(8);
    public static final RegistryObject<FlowingFluid> MOLTEN_LEAD = FLUIDS.register("molten_lead", () -> new MoltenLeadFluid(OFluids.MOLTEN_LEAD_PROPERTIES));

    public static final ForgeFlowingFluid.Properties MOLTEN_LEAD_PROPERTIES = new ForgeFlowingFluid.Properties(MOLTEN_LEAD, MOLTEN_LEAD, MOLTEN_LEAD_TYPE).bucket(OItems.MOLTEN_LEAD_BUCKET).block(OBlocks.MOLTEN_LEAD);
}
