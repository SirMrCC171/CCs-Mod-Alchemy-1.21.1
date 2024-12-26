package sirmrcc.alchemy.block;

import net.minecraft.block.*;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.intprovider.UniformIntProvider;
import sirmrcc.alchemy.CCsModAlchemy;
import sirmrcc.alchemy.block.custom.TransmutedEarthBlock;

public class ModBlocks
{
    public static final Block RAW_SULFUR_BLOCK = register("raw_sulfur_block",
            new Block(AbstractBlock.Settings.create().strength(5f).requiresTool().sounds(BlockSoundGroup.STONE)));
    public static final Block SULFUR_ORE = register("sulfur_ore",
            new ExperienceDroppingBlock(UniformIntProvider.create(2,5),AbstractBlock.Settings.create()
                    .strength(3f).requiresTool().sounds(BlockSoundGroup.STONE)));
    public static final Block SULFUR_DEEPSLATE_ORE = register("sulfur_deepslate_ore",
            new ExperienceDroppingBlock(UniformIntProvider.create(2,5),AbstractBlock.Settings.create()
                    .strength(4.5f).requiresTool().sounds(BlockSoundGroup.DEEPSLATE)));
    public static final Block CINNABAR_BLOCK = register("cinnabar_block",
            new Block(AbstractBlock.Settings.create().strength(5f).requiresTool().sounds(BlockSoundGroup.STONE)));
    public static final Block CINNABAR_ORE = register("cinnabar_ore",
            new Block(AbstractBlock.Settings.create().strength(3f).requiresTool().sounds(BlockSoundGroup.STONE)));
    public static final Block CINNABAR_DEEPSLATE_ORE = register("cinnabar_deepslate_ore",
            new Block(AbstractBlock.Settings.create().strength(4.5f).requiresTool().sounds(BlockSoundGroup.DEEPSLATE)));
    public static final Block LIMESTONE = register("limestone",
            new PillarBlock(AbstractBlock.Settings.create().strength(2f).requiresTool().sounds(BlockSoundGroup.DRIPSTONE_BLOCK)));
    public static final Block VOLATILE_TRANSMUTED_EARTH = register("volatile_transmuted_earth",
            new TransmutedEarthBlock(AbstractBlock.Settings.create().strength(1f).sounds(BlockSoundGroup.NETHERRACK)));
    public static final Block STABLE_TRANSMUTED_EARTH = register("stable_transmuted_earth",
            new MushroomBlock(AbstractBlock.Settings.create().strength(1f).sounds(BlockSoundGroup.NETHERRACK)));

    private static <T extends Block> T register(String name, T block)
    {
        Registry.register(Registries.BLOCK, Identifier.of(CCsModAlchemy.MOD_ID, name), block);
        Registry.register(Registries.ITEM, Identifier.of(CCsModAlchemy.MOD_ID, name), new BlockItem(block, new Item.Settings()));
        return block;
    }
    public static void registerModBlocks()
    {
        CCsModAlchemy.LOGGER.info("Registering Mod Blocks for " + CCsModAlchemy.MOD_ID);
    }
}
