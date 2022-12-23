package galena.oreganized.world.gen.structure;

import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.feature.StructureFeature;
import net.minecraft.world.level.levelgen.feature.configurations.JigsawConfiguration;
import net.minecraft.world.level.levelgen.structure.PoolElementStructurePiece;
import net.minecraft.world.level.levelgen.structure.pieces.PieceGeneratorSupplier;
import net.minecraft.world.level.levelgen.structure.pools.JigsawPlacement;

public class BoulderStructure extends StructureFeature<JigsawConfiguration> {

    public BoulderStructure(Codec<JigsawConfiguration> codec) {
        super(codec, (context) -> {
            JigsawConfiguration boulderConfig = new JigsawConfiguration(context.config().startPool(), 10);
            PieceGeneratorSupplier.Context<JigsawConfiguration> boulderContext = new PieceGeneratorSupplier.Context<>(context.chunkGenerator(), context.biomeSource(), context.seed(), context.chunkPos(), boulderConfig, context.heightAccessor(), context.validBiome(), context.structureManager(), context.registryAccess());
            BlockPos boulderPos = context.chunkPos().getMiddleBlockPosition(0);
            int topLandY = context.chunkGenerator().getFirstFreeHeight(boulderPos.getX(), boulderPos.getZ(), Heightmap.Types.WORLD_SURFACE_WG, context.heightAccessor());
            boulderPos = boulderPos.above(topLandY - 7);
            return JigsawPlacement.addPieces(boulderContext, PoolElementStructurePiece::new, boulderPos, true, false);
        });
    }

    /**
     *        : WARNING!!! DO NOT FORGET THIS METHOD!!!! :
     * If you do not override step method, your structure WILL crash the biome as it is being parsed!
     *
     * Generation step for when to generate the structure. there are 10 stages you can pick from!
     * This surface structure stage places the structure before plants and ores are generated.
     */
    @Override
    public GenerationStep.Decoration step() {
        return GenerationStep.Decoration.UNDERGROUND_DECORATION;
    }
}