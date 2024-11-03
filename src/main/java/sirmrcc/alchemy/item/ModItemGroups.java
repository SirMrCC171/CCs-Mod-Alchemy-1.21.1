package sirmrcc.alchemy.item;

import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import sirmrcc.alchemy.CCsModAlchemy;
import sirmrcc.alchemy.block.ModBlocks;

public class ModItemGroups
{
    public static final ItemGroup ALCHEMY_CRAFT = Registry.register(Registries.ITEM_GROUP,
            Identifier.of(CCsModAlchemy.MOD_ID, "alchemy_craft"), FabricItemGroup
                    .builder().icon(() -> new ItemStack(ModItems.PHILOSOPHERS_STONE))
                    .displayName(Text.translatable("itemgroup.cc-mod-alchemy.alchemy_craft"))
                    .entries(((displayContext, entries) ->
                    {
                        entries.add(ModBlocks.SULFUR_ORE);
                        entries.add(ModBlocks.DEEPSLATE_SULFUR_ORE);
                        entries.add(ModBlocks.RAW_SULFUR_BLOCK);
                        entries.add(ModItems.RAW_SULFUR);
                        entries.add(ModItems.SULFUR_POWDER);
                        entries.add(ModItems.PHILOSOPHERS_STONE);
                    })).build());

    public static void registerItemGroups()
    {
        CCsModAlchemy.LOGGER.info("Registering Item Groups for " + CCsModAlchemy.MOD_ID);
    }
}
