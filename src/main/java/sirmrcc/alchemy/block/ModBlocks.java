package sirmrcc.alchemy.block;

import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.Identifier;
import sirmrcc.alchemy.CCsModAlchemy;

public class ModBlocks
{
    private static final Block RAW_SULFUR_BLOCK = registerBlock("raw_sulfur_block",
            new Block(AbstractBlock.Settings.create().strength(5f).requiresTool().sounds(BlockSoundGroup.STONE)));
    private static final Block SULFUR_ORE = registerBlock("sulfur_ore",
            new Block(AbstractBlock.Settings.create().strength(3f).requiresTool().sounds(BlockSoundGroup.STONE)));
    private static final Block DEEPSLATE_SULFUR_ORE = registerBlock("deepslate_sulfur_ore",
            new Block(AbstractBlock.Settings.create().strength(4.5f).requiresTool().sounds(BlockSoundGroup.DEEPSLATE)));

    private static Block registerBlock(String name, Block block)
    {
        registerBlockItem(name, block);
        return Registry.register(Registries.BLOCK, Identifier.of(CCsModAlchemy.MOD_ID, name), block);
    }

    private static void registerBlockItem(String name, Block block)
    {
        Registry.register(Registries.ITEM, Identifier.of(CCsModAlchemy.MOD_ID, name),
                new BlockItem(block, new Item.Settings()));
    }

    public static void registerModBlocks()
    {
        CCsModAlchemy.LOGGER.info("Registering Mod Blocks for " + CCsModAlchemy.MOD_ID);

        ItemGroupEvents.modifyEntriesEvent(ItemGroups.NATURAL).register(fabricItemGroupEntries ->
                fabricItemGroupEntries.add(ModBlocks.SULFUR_ORE));
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.NATURAL).register(fabricItemGroupEntries ->
                fabricItemGroupEntries.add(ModBlocks.DEEPSLATE_SULFUR_ORE));
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.NATURAL).register(fabricItemGroupEntries ->
                fabricItemGroupEntries.add(ModBlocks.RAW_SULFUR_BLOCK));
    }
}
