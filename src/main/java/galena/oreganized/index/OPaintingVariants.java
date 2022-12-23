package galena.oreganized.index;

import galena.oreganized.Oreganized;
import net.minecraft.world.entity.decoration.Motive;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class OPaintingVariants {

    public static final DeferredRegister<Motive> PAINTINGS = DeferredRegister.create(ForgeRegistries.PAINTING_TYPES, Oreganized.MOD_ID);

    public static final RegistryObject<Motive> VINDICATING_BAD = PAINTINGS.register("vindicating_bad", () -> new Motive(32,48));
}
