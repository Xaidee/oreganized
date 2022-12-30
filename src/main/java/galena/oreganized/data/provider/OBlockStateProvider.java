package galena.oreganized.data.provider;

import galena.oreganized.Oreganized;
import galena.oreganized.content.block.*;
import net.minecraft.core.Direction;
import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.*;
import net.minecraftforge.client.model.generators.*;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.function.Supplier;

import static galena.oreganized.Oreganized.MOD_ID;
import static net.minecraftforge.client.model.generators.ModelProvider.BLOCK_FOLDER;

public abstract class OBlockStateProvider extends BlockStateProvider {

    public OBlockStateProvider(DataGenerator gen, ExistingFileHelper help) {
        super(gen, MOD_ID, help);
    }

    protected ResourceLocation texture(String name) {
        return modLoc("block/" + name);
    }

    protected String name(Block block) {
        return ForgeRegistries.BLOCKS.getKey(block).getPath();
    }

    protected String name(Supplier<? extends Block> block) {
        return name(block.get());
    }

    private String suffixByIndex(int index) {
        switch (index) {
            case CrystalGlassPaneBlock.ROTATED -> {
                return "_rot";
            }
            case CrystalGlassPaneBlock.INNER -> {
                return "_in";
            }
            case CrystalGlassPaneBlock.OUTER -> {
                return "_out";
            }
            default -> {
                return "";
            }
        }
    }

    public void simpleBlock(Supplier<? extends Block> block) {
        simpleBlock(block.get());
    }

    public void stairsBlock(Supplier<? extends StairBlock> block, Supplier<? extends Block> fullBlock) {
        stairsBlock(block.get(), texture(name(fullBlock)));
    }

    public void slabBlock(Supplier<? extends SlabBlock> block, Supplier<? extends Block> fullBlock) {
        slabBlock(block.get(), texture(name(fullBlock)), texture(name(fullBlock)));
    }

    public void wallBlock(Supplier<? extends WallBlock> wall, Supplier<? extends Block> fullBlock) {
        wallBlock(wall.get(), texture(name(fullBlock)));
    }

    public void waxedBlock(Supplier<? extends Block> block, Block origin) {
        simpleBlock(block.get(), cubeAll(origin));
    }

    public ModelFile cubeBottomTop(Supplier<? extends Block> block) {
        BlockModelBuilder model = models().getBuilder(name(block));
        model.parent(models().getExistingFile(new ResourceLocation("minecraft", "block" + "/cube_bottom_top")));
        model.texture("top", texture(name(block) + "_top"));
        model.texture("bottom", texture(name(block) + "_bottom"));
        model.texture("side", texture(name(block) + "_side"));
        return model;
    }

    public void axisBlock(Supplier<? extends Block> block) {
        axisBlock((RotatedPillarBlock)block.get());
    }

    public ModelFile directionalBlockModel(Supplier<? extends Block> block, String name, String side, String front, String back, String top) {
        return models().withExistingParent(name, BLOCK_FOLDER + "/observer")
                .texture("bottom", texture(back))
                .texture("side", texture(side))
                .texture("top", texture(top))
                .texture("front", texture(front))
                .texture("particle", texture(front));
    }

    public void exposer(Supplier<? extends Block> block) {
        getVariantBuilder(block.get()).forAllStates(state -> {
            // Annoyingly complicated method to convert the blockstate's levels (0,1,2,3,4,5,6,7) to 4 frames (0,1,2,3)
            int level = Math.round((ExposerBlock.TexturedFrames - 1) / (float)(state.getValue(ExposerBlock.LEVEL) + 1));
            Direction facing = state.getValue(ExposerBlock.FACING);
            int x = 0;
            int y = 0;
            //if (facing == Direction.NORTH) do nothing;
            if (facing == Direction.EAST) y = 90;
            if (facing == Direction.SOUTH) y = 180;
            if (facing == Direction.WEST) y = 270;
            if (facing == Direction.DOWN) x = 90;
            if (facing == Direction.UP) x = 270;

            String name = name(block) + "_level_" + level;
            String side = name(block) + "_side";
            String back = state.getValue(ExposerBlock.LEVEL) > 0 ? name(block) + "_back_on" : name(block) + "_back";
            String top = name(block) + "_top";

            return ConfiguredModel.builder().modelFile(directionalBlockModel(block, name + "_" + facing, side, name, back, top)).rotationX(x).rotationY(y).build();
        });
    }

    public ModelFile cauldronModel(Supplier<? extends Block> block, ResourceLocation texture, int age) {
        String name = name(block) + age;
        return models().withExistingParent(name, ModelProvider.BLOCK_FOLDER + "/template_cauldron_full")
                .texture("content", texture);
    }

    public ModelFile cauldronModel(Supplier<? extends Block> cauldron, Supplier<? extends Block> content, int age) {
        return cauldronModel(cauldron, texture(name(content)), age);
    }

    public void moltenCauldron(Supplier<? extends Block> cauldron, Supplier<? extends Block> content) {
        getVariantBuilder(cauldron.get()).forAllStates(state -> {
            int age = state.getValue(MoltenLeadCauldronBlock.AGE);
            if (age == 0) {
                return ConfiguredModel.builder().modelFile(cauldronModel(cauldron, content, age)).build();
            } else {
                ResourceLocation texture = age == 3 ? texture("molten_" + name(content)) : texture(name(content) + "2");
                return ConfiguredModel.builder().modelFile(cauldronModel(cauldron, texture, age)).build();
            }
        });
    }
    public void crystalGlassBlock(Supplier<? extends Block> block) {
        getVariantBuilder(block.get()).partialState().with(CrystalGlassBlock.TYPE, CrystalGlassBlock.NORMAL).modelForState()
                .modelFile(cubeAll(block.get())).addModel().partialState().with(CrystalGlassBlock.TYPE, CrystalGlassBlock.ROTATED)
                .modelForState().modelFile(models().cubeAll(name(block) + "_rot",
                        Oreganized.modLoc("block/" + name(block) + "_rot"))).addModel()
                .partialState().with(CrystalGlassBlock.TYPE, CrystalGlassBlock.INNER)
                .modelForState().modelFile(models().cubeAll(name(block) + "_in",
                        Oreganized.modLoc("block/" + name(block) + "_in"))).addModel()
                .partialState().with(CrystalGlassBlock.TYPE, CrystalGlassBlock.OUTER)
                .modelForState().modelFile(models().cubeAll(name(block) + "_out",
                        Oreganized.modLoc("block/" + name(block) + "_out"))).addModel();
    }

    public void crystalGlassPaneBlock(Supplier<? extends Block> pane, Supplier<? extends Block> fullBlock) {
        String baseName = name(fullBlock);
        String paneName = name(pane);
        MultiPartBlockStateBuilder builder = getMultipartBuilder(pane.get());
        for (int i = 0; i < 4; i++) {
            int finalI = i;
            PipeBlock.PROPERTY_BY_DIRECTION.entrySet().forEach(e -> {
                Direction dir = e.getKey();
                if (dir.getAxis().isHorizontal()) {
                    boolean alt = dir == Direction.SOUTH;
                    builder.part().modelFile(models().panePost(paneName + "_post" + suffixByIndex(finalI), Oreganized.modLoc("block/" + baseName + suffixByIndex(finalI)), Oreganized.modLoc("block/" + paneName + "_top"))).addModel().condition(CrystalGlassPaneBlock.TYPE, finalI).end()
                            .part().modelFile(alt || dir == Direction.WEST ? models().paneSideAlt(paneName + "_side_alt" + suffixByIndex(finalI), Oreganized.modLoc("block/" + baseName + suffixByIndex(finalI)), Oreganized.modLoc("block/" + paneName + "_top")) :
                                    models().paneSide(paneName + "_side" + suffixByIndex(finalI), Oreganized.modLoc("block/" + baseName + suffixByIndex(finalI)), Oreganized.modLoc("block/" + paneName + "_top"))).rotationY(dir.getAxis() == Direction.Axis.X ? 90 : 0).addModel()
                            .condition(e.getValue(), true).condition(CrystalGlassPaneBlock.TYPE, finalI).end()
                            .part().modelFile(alt || dir == Direction.EAST ? models().paneNoSideAlt(paneName + "_noside_alt" + suffixByIndex(finalI), Oreganized.modLoc("block/" + baseName + suffixByIndex(finalI))) :
                                    models().paneNoSide(paneName + "_noside" + suffixByIndex(finalI), Oreganized.modLoc("block/" + baseName + suffixByIndex(finalI)))).rotationY(dir == Direction.WEST ? 270 : dir == Direction.SOUTH ? 90 : 0).addModel()
                            .condition(e.getValue(), false).condition(CrystalGlassPaneBlock.TYPE, finalI);
                }
            });
        }
    }

    public ModelFile engravedFace(Block block, Boolean engraved) {
        BlockModelBuilder model = models().getBuilder(name(block));
        ResourceLocation textureLoc = engraved ? texture("engraved/" + name(block)) : texture(name(block));
        model.parent(models().getExistingFile(new ResourceLocation("minecraft", "block" + "/template_single_face")));
        model.texture("texture", textureLoc);
        return model;
    }
}
