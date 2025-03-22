package sirmrcc.alchemy.item;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.Rarity;
import sirmrcc.alchemy.CCsModAlchemy;
import sirmrcc.alchemy.item.custom.BridgerStoneTestItem;

import java.util.List;

public class ModItems
{
    public static final Item SULFUR = registerItem("sulfur", new Item(new Item.Settings()));
    public static final Item SULFUR_POWDER = registerItem("sulfur_powder", new Item(new Item.Settings()));
    public static final Item PHILOSOPHERS_STONE = registerItem("philosophers_stone", new Item(new Item.Settings().maxCount(1).rarity(Rarity.RARE)));
    public static final Item PHILOSOPHERS_STONE_POLISHED = registerItem("philosophers_stone_polished", new Item(new Item.Settings().maxCount(1).rarity(Rarity.RARE)));
    public static final Item CINNABAR = registerItem("cinnabar", new Item(new Item.Settings()));
    public static final Item MERCURY_BOTTLE = registerItem("mercury_bottle", new Item(new Item.Settings().maxCount(16)));
    public static final Item NATRON = registerItem("natron", new Item(new Item.Settings()));
    public static final Item SALT = registerItem("salt", new Item(new Item.Settings()));
    public static final Item SULFURIC_ACID_BOTTLE = registerItem("sulfuric_acid_bottle", new Item(new Item.Settings().maxCount(16)));
    public static final Item PHILOSOPHERS_STONE_POLISHED_BROKEN = registerItem("philosophers_stone_polished_broken", new Item(new Item.Settings().maxCount(1).rarity(Rarity.RARE)));

    //test list
    public static final Item TEST = registerItem("test", new BridgerStoneTestItem
                                                (new Item.Settings().rarity(Rarity.EPIC).maxDamage(512))
    {
        @Override
        public void appendTooltip(ItemStack stack, TooltipContext context, List<Text> tooltip, TooltipType type)
        {
            tooltip.add(Text.translatable("tooltip.cc-mod-alchemy.test.tooltip"));
            super.appendTooltip(stack, context, tooltip, type);
        }
    });


    private  static Item registerItem(String name, Item item)
    {
        return Registry.register(Registries.ITEM, Identifier.of(CCsModAlchemy.MOD_ID, name), item);
    }

    public static void registerModItems()
    {
        CCsModAlchemy.LOGGER.info("Registering Mod Items for " + CCsModAlchemy.MOD_ID);
    }
}
