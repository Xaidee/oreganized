package galena.oreganized.data.provider;

import galena.oreganized.Oreganized;
import galena.oreganized.compat.ColorCompat;
import galena.oreganized.content.block.BulbBlock;
import galena.oreganized.content.block.CrystalGlassBlock;
import galena.oreganized.content.block.CrystalGlassPaneBlock;
import galena.oreganized.content.block.GargoyleBlock;
import galena.oreganized.content.block.IMeltableBlock;
import galena.oreganized.content.block.LeadDoorBlock;
import galena.oreganized.content.block.MoltenLeadCauldronBlock;
import net.minecraft.core.Direction;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.CrossCollisionBlock;
import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.level.block.IronBarsBlock;
import net.minecraft.world.level.block.PipeBlock;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.block.TrapDoorBlock;
import net.minecraft.world.level.block.WallBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DoorHingeSide;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.level.block.state.properties.Half;
import net.minecraftforge.client.model.generators.BlockModelBuilder;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.client.model.generators.ConfiguredModel;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.client.model.generators.MultiPartBlockStateBuilder;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Arrays;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;

import static galena.oreganized.Oreganized.MOD_ID;
import static net.minecraftforge.client.model.generators.ModelProvider.BLOCK_FOLDER;

public abstract class OBlockStateProvider extends BlockStateProvider {

    public OBlockStateProvider(PackOutput output, ExistingFileHelper help) {
        super(output, MOD_ID, help);
    }

    protected ResourceLocation texture(String name) {
        return modLoc(BLOCK_FOLDER + "/" + name);
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

    public ModelFile directionalBlockModel(Supplier<? extends Block> block, String name, String side, String front, String back, String top) {
        return models().withExistingParent(name, BLOCK_FOLDER + "/observer")
                .texture("bottom", texture(back))
                .texture("side", texture(side))
                .texture("top", texture(top))
                .texture("front", texture(front))
                .texture("particle", texture(front));
    }

    public ModelFile cauldronModel(Supplier<? extends Block> block, ResourceLocation texture, int age) {
        String name = name(block) + age;
        return models().withExistingParent(name, BLOCK_FOLDER + "/template_cauldron_full")
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

    public void crystalGlassPaneBlock(DyeColor color, Supplier<? extends Block> pane, Supplier<? extends Block> fullBlock) {
        String baseName = name(fullBlock);
        String paneName = name(pane);
        MultiPartBlockStateBuilder builder = getMultipartBuilder(pane.get());
        for (int i = 0; i < 4; i++) {
            int finalI = i;
            PipeBlock.PROPERTY_BY_DIRECTION.entrySet().forEach(e -> {
                Direction dir = e.getKey();
                if (dir.getAxis().isHorizontal()) {
                    boolean alt = dir == Direction.SOUTH;
                    var topTexture = new ResourceLocation(ColorCompat.getNamespace(color), "block/" + color.getSerializedName() + "_stained_glass_pane_top");
                    builder.part().modelFile(models().panePost(paneName + "_post" + suffixByIndex(finalI), Oreganized.modLoc("block/" + baseName + suffixByIndex(finalI)), topTexture)).addModel().condition(CrystalGlassPaneBlock.TYPE, finalI).end()
                            .part().modelFile(alt || dir == Direction.WEST ? models().paneSideAlt(paneName + "_side_alt" + suffixByIndex(finalI), Oreganized.modLoc("block/" + baseName + suffixByIndex(finalI)), topTexture) :
                                    models().paneSide(paneName + "_side" + suffixByIndex(finalI), Oreganized.modLoc("block/" + baseName + suffixByIndex(finalI)), topTexture)).rotationY(dir.getAxis() == Direction.Axis.X ? 90 : 0).addModel()
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

    public <T extends Block & IMeltableBlock> void meltableBlock(Supplier<T> block, BiFunction<String, ResourceLocation, ModelFile> modelBuilder) {
        meltableBlock(block, modelBuilder, (s, it) -> it);
    }

    public <T extends Block & IMeltableBlock> void meltableBlock(Supplier<T> block, BiFunction<String, ResourceLocation, ModelFile> modelBuilder, BiFunction<BlockState, ConfiguredModel.Builder<?>, ConfiguredModel.Builder<?>> modelModifier) {
        var prefixes = List.of("", "goopy_", "red_hot_");
        var redHotModel = models().cubeAll("red_hot_lead", modLoc(BLOCK_FOLDER + "/red_hot_lead"));
        getVariantBuilder(block.get()).forAllStates(state -> {
            int goopyness = block.get().getGoopyness(state);
            var name = prefixes.get(goopyness) + name(block);
            var texture = texture(name);
            var isRedHot = goopyness == 2;
            var model = ConfiguredModel.builder().modelFile(isRedHot ? redHotModel : modelBuilder.apply(name, texture));
            if (isRedHot) return model.build();
            return modelModifier.apply(state, model).build();
        });
    }

    public <T extends Block & IMeltableBlock> void bulb(Supplier<T> block) {
        var prefixes = List.of("", "dimmer_", "goopy_", "red_hot_");
        var redHotModel = models().cubeAll("red_hot_lead", modLoc(BLOCK_FOLDER + "/red_hot_lead"));
        getVariantBuilder(block.get()).forAllStates(state -> {
            int goopyness = state.getValue(BulbBlock.GOOPYNESS_4);
            var name = prefixes.get(goopyness) + name(block);
            var texture = texture(name);
            var model = goopyness < 3 ? models().cubeAll(name, texture) : redHotModel;

            return ConfiguredModel.builder().modelFile(model).build();
        });
    }

    public <T extends RotatedPillarBlock & IMeltableBlock> void meltablePillar(Supplier<T> block) {
        meltableBlock(
                block,
                (n, t) -> models().cubeColumn(n, t.withSuffix("_side"), t.withSuffix("_top")),
                (s, it) -> switch (s.getValue(RotatedPillarBlock.AXIS)) {
                    case X -> it.rotationX(90).rotationY(90);
                    case Y -> it;
                    case Z -> it.rotationX(90);
                }
        );
    }

    public <T extends TrapDoorBlock & IMeltableBlock> void meltableTrapdoor(Supplier<T> block) {
        var baseName = name(block);
        var prefixes = List.of("", "goopy_", "red_hot_");

        getVariantBuilder(block.get()).forAllStatesExcept(state -> {
            int goopyness = block.get().getGoopyness(state);
            var name = prefixes.get(goopyness) + baseName;
            var texture = texture(goopyness < 2 ? name : "red_hot_lead");

            var bottom = models().trapdoorOrientableBottom(name + "_bottom", texture);
            var top = models().trapdoorOrientableTop(name + "_top", texture);
            var open = models().trapdoorOrientableOpen(name + "_open", texture);

            int xRot = 0;
            int yRot = ((int) state.getValue(TrapDoorBlock.FACING).toYRot()) + 180;
            boolean isOpen = state.getValue(TrapDoorBlock.OPEN);
            if (isOpen && state.getValue(TrapDoorBlock.HALF) == Half.TOP) {
                xRot += 180;
                yRot += 180;
            }
            yRot %= 360;
            return ConfiguredModel.builder().modelFile(isOpen ? open : state.getValue(TrapDoorBlock.HALF) == Half.TOP ? top : bottom)
                    .rotationX(xRot)
                    .rotationY(yRot)
                    .build();
        }, TrapDoorBlock.POWERED, TrapDoorBlock.WATERLOGGED);
    }

    public <T extends DoorBlock & IMeltableBlock> void meltableDoor(Supplier<T> block) {
        var baseName = name(block);
        var prefixes = List.of("", "goopy_", "red_hot_");

        getVariantBuilder(block.get()).forAllStatesExcept((state) -> {
            boolean animated = state.hasProperty(LeadDoorBlock.ANIMATED) && state.getValue(LeadDoorBlock.ANIMATED);
            boolean right = state.getValue(DoorBlock.HINGE) == DoorHingeSide.RIGHT;
            boolean open = state.getValue(DoorBlock.OPEN);
            boolean lower = state.getValue(DoorBlock.HALF) == DoubleBlockHalf.LOWER;

            int goopyness = block.get().getGoopyness(state);
            var name = prefixes.get(goopyness) + baseName;
            var textureSuffix = (open
                    ? animated ? "_closing" : "_open"
                    : animated ? "_opening" : "");
            var bottom = texture(goopyness < 2 ? (name + "_bottom" + textureSuffix) : "red_hot_lead");
            var top = texture(goopyness < 2 ? (name + "_top" + textureSuffix) : "red_hot_lead");

            var bottomLeft = this.models().doorBottomLeft(name + "_bottom_left" + textureSuffix, bottom, top);
            var bottomRight = this.models().doorBottomRight(name + "_bottom_right" + textureSuffix, bottom, top);
            var topLeft = this.models().doorTopLeft(name + "_top_left" + textureSuffix, bottom, top);
            var topRight = this.models().doorTopRight(name + "_top_right" + textureSuffix, bottom, top);

            int yRot = (int) state.getValue(DoorBlock.FACING).toYRot() + 90;
            if (open) {
                yRot += 90;
            }

            if (right && open) {
                yRot += 180;
            }

            yRot %= 360;
            ModelFile model = null;

            if (lower) {
                if (right) {
                    model = bottomRight;
                } else {
                    model = bottomLeft;
                }
            } else {
                if (right) {
                    model = topRight;
                } else {
                    model = topLeft;
                }
            }

            return ConfiguredModel.builder().modelFile(model).rotationY(yRot).build();
        }, DoorBlock.POWERED);
    }

    public <T extends IronBarsBlock & IMeltableBlock> void meltableBars(Supplier<T> block) {
        var baseName = name(block);
        var prefixes = List.of("", "goopy_", "red_hot_");

        var builder = getMultipartBuilder(block.get());
        var property = block.get().getGoopynessProperty();

        property.getPossibleValues().forEach(goopyness -> {
            var name = prefixes.get(goopyness) + baseName;
            var texture = texture(name);

            Function<String, ModelFile> createModel = suffix -> models()
                    .withExistingParent(name + suffix, mcLoc(BLOCK_FOLDER + "/iron_bars" + suffix))
                    .texture("bars", texture)
                    .texture("edge", texture)
                    .texture("particle", texture);

            builder.part().modelFile(createModel.apply("_post_ends")).addModel()
                    .condition(property, goopyness);

            builder.part()
                    .modelFile(createModel.apply("_post")).addModel()
                    .condition(property, goopyness)
                    .condition(IronBarsBlock.NORTH, false)
                    .condition(IronBarsBlock.EAST, false)
                    .condition(IronBarsBlock.SOUTH, false)
                    .condition(IronBarsBlock.WEST, false);

            Arrays.stream(Direction.values()).filter(it -> it.getAxis().isHorizontal()).forEach(direction -> {
                var suffix = switch (direction) {
                    case SOUTH, WEST -> "_alt";
                    default -> "";
                };

                var yRotation = switch (direction) {
                    case EAST, WEST -> 90;
                    default -> 0;
                };

                builder.part()
                        .modelFile(createModel.apply("_cap" + suffix)).rotationY(yRotation).addModel()
                        .condition(property, goopyness)
                        .condition(CrossCollisionBlock.NORTH, direction == Direction.NORTH)
                        .condition(CrossCollisionBlock.EAST, direction == Direction.EAST)
                        .condition(CrossCollisionBlock.SOUTH, direction == Direction.SOUTH)
                        .condition(CrossCollisionBlock.WEST, direction == Direction.WEST);

                builder.part()
                        .modelFile(createModel.apply("_side" + suffix)).rotationY(yRotation).addModel()
                        .condition(property, goopyness)
                        .condition(PipeBlock.PROPERTY_BY_DIRECTION.get(direction), true);
            });
        });
    }

    public void gargoyleBlock(Supplier<? extends Block> block) {
        var name = name(block);
        var floorModel = models().getExistingFile(texture(name));
        var wallModel = models().getExistingFile(texture(name + "_side"));

        getVariantBuilder(block.get()).forAllStatesExcept(state -> {
            var facing = state.getValue(BlockStateProperties.HORIZONTAL_FACING);
            var attachment = state.getValue(GargoyleBlock.ATTACHMENT);

            return ConfiguredModel.builder()
                    .rotationY((int) (facing.toYRot() % 360))
                    .modelFile(attachment == GargoyleBlock.AttachmentType.WALL ? wallModel : floorModel)
                    .build();
        });
    }

    public void crate(Supplier<? extends Block> block) {
        var name = name(block);
        simpleBlock(block.get(), models().cubeBottomTop(
                name,
                texture(name).withSuffix("_side"),
                texture(name).withSuffix("_bottom"),
                texture(name).withSuffix("_top")
        ));
    }
}
