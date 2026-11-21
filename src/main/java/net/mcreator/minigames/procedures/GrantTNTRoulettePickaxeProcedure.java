package net.mcreator.minigames.procedures;

import net.mcreator.minigames.block.RouletteGridBlock;
import net.mcreator.minigames.init.MinigamesModBlocks;
import net.minecraft.advancements.predicates.BlockPredicate;
import net.minecraft.advancements.predicates.StatePropertiesPredicate;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.AdventureModePredicate;
import net.minecraft.world.item.component.TooltipDisplay;
import net.neoforged.neoforge.transfer.transaction.Transaction;
import net.neoforged.neoforge.transfer.item.ItemResource;
import net.neoforged.neoforge.transfer.ResourceHandler;
import net.neoforged.neoforge.capabilities.Capabilities;

import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.Entity;

import net.mcreator.minigames.init.MinigamesModItems;

import java.util.ArrayList;
import java.util.List;

public class GrantTNTRoulettePickaxeProcedure {
	public static void execute(LevelAccessor world) {
		if (!world.isClientSide() && world.getServer() != null) {
			for (Entity entityiterator : new ArrayList<>(world.getServer().getPlayerList().getPlayers())) {
				ItemStack pick = new ItemStack(MinigamesModItems.TNT_ROULETTE_PICKAXE.get()).copy();
				BlockPredicate nonePred = BlockPredicate.Builder.block().of(world.registryAccess().lookupOrThrow(Registries.BLOCK), MinigamesModBlocks.ROULETTE_GRID.get()).setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(RouletteGridBlock.GRIDSTATE, "none")).build();
				BlockPredicate tntPred = BlockPredicate.Builder.block().of(world.registryAccess().lookupOrThrow(Registries.BLOCK), MinigamesModBlocks.ROULETTE_GRID.get()).setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(RouletteGridBlock.GRIDSTATE, "tnt")).build();
				BlockPredicate powerupPred = BlockPredicate.Builder.block().of(world.registryAccess().lookupOrThrow(Registries.BLOCK), MinigamesModBlocks.ROULETTE_GRID.get()).setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(RouletteGridBlock.GRIDSTATE, "powerup")).build();
				AdventureModePredicate adventureModePredicate = new AdventureModePredicate(List.of(nonePred, tntPred, powerupPred));
				pick.set(DataComponents.CAN_BREAK, adventureModePredicate );
				pick.set(DataComponents.TOOLTIP_DISPLAY, TooltipDisplay.DEFAULT.withHidden(DataComponents.CAN_BREAK, true));
				if (entityiterator.getCapability(Capabilities.Item.ENTITY, null) instanceof ResourceHandler<ItemResource> _resourceHandler) {
					setStackInSlot(_resourceHandler, 0, ItemResource.of(pick), 1);
				}
			}
		}
	}

	private static void setStackInSlot(ResourceHandler<ItemResource> handler, int index, ItemResource resource, int amount) {
		try (var tx = Transaction.openRoot()) {
			if (!handler.getResource(index).isEmpty())
				handler.extract(index, handler.getResource(index), handler.getAmountAsInt(index), tx);
			if (!resource.isEmpty() && amount > 0)
				handler.insert(index, resource, amount, tx);
			tx.commit();
		}
	}
}