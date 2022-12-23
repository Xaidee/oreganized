package galena.oreganized.index;

import galena.oreganized.Oreganized;
import galena.oreganized.content.entity.ExposerBlockEntity;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class OBlockEntities {

    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITIES, Oreganized.MOD_ID);

    public static final RegistryObject<BlockEntityType<?>> EXPOSER = BLOCK_ENTITIES.register("exposer", () -> BlockEntityType.Builder.of(ExposerBlockEntity::new, OBlocks.EXPOSER.get()).build(null));

}
