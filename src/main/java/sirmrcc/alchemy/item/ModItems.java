package sirmrcc.alchemy.item;

import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import sirmrcc.alchemy.CCsModAlchemy;

public class ModItems
{
    public static final Item RAW_SULFUR = registerItem("raw_sulfur", new Item(new Item.Settings()));
    public static final Item SULFUR_POWDER = registerItem("sulfur_powder", new Item(new Item.Settings()));


    private  static Item registerItem(String name, Item item)
    {
        return Registry.register(Registries.ITEM, Identifier.of(CCsModAlchemy.MOD_ID, name), item);
    }

    public static void registerModItems()
    {
        CCsModAlchemy.LOGGER.info("Registering Mod Items for " + CCsModAlchemy.MOD_ID);

        ItemGroupEvents.modifyEntriesEvent(ItemGroups.INGREDIENTS).register(fabricItemGroupEntries ->
        {
            fabricItemGroupEntries.add(RAW_SULFUR);
            fabricItemGroupEntries.add(SULFUR_POWDER);
        });
    }
}
