package sirmrcc.alchemy;

import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.fabricmc.fabric.api.registry.FuelRegistry;
import net.minecraft.item.ItemGroups;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sirmrcc.alchemy.block.ModBlocks;
import sirmrcc.alchemy.item.ModItemGroups;
import sirmrcc.alchemy.item.ModItems;

public class CCsModAlchemy implements ModInitializer
{
	public static final String MOD_ID = "cc-mod-alchemy";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize()
	{
		ModItemGroups.registerItemGroups();
		ModItems.registerModItems();
		ModBlocks.registerModBlocks();

		//fuel
		FuelRegistry.INSTANCE.add(ModItems.RAW_SULFUR, 800);
	}
}