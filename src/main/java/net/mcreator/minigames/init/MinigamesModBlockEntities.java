/*
*    MCreator note: This file will be REGENERATED on each build.
*/
package net.mcreator.minigames.init;

import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.items.wrapper.SidedInvWrapper;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.bus.api.SubscribeEvent;

import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.Block;
import net.minecraft.core.registries.BuiltInRegistries;

import net.mcreator.minigames.block.entity.SpreadingIceBlockEntity;
import net.mcreator.minigames.block.entity.InflatableWallBlockBlockEntity;
import net.mcreator.minigames.MinigamesMod;

@EventBusSubscriber
public class MinigamesModBlockEntities {
	public static final DeferredRegister<BlockEntityType<?>> REGISTRY = DeferredRegister.create(BuiltInRegistries.BLOCK_ENTITY_TYPE, MinigamesMod.MODID);
	public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<InflatableWallBlockBlockEntity>> INFLATABLE_WALL_BLOCK = register("inflatable_wall_block", MinigamesModBlocks.INFLATABLE_WALL_BLOCK, InflatableWallBlockBlockEntity::new);
	// Start of user code block custom block entities
	public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<SpreadingIceBlockEntity>> SPREADING_ICE = register("spreading_ice", MinigamesModBlocks.SPREADING_ICE, SpreadingIceBlockEntity::new);

	// End of user code block custom block entities
	private static <T extends BlockEntity> DeferredHolder<BlockEntityType<?>, BlockEntityType<T>> register(String registryname, DeferredHolder<Block, Block> block, BlockEntityType.BlockEntitySupplier<T> supplier) {
		return REGISTRY.register(registryname, () -> new BlockEntityType(supplier, block.get()));
	}

	@SubscribeEvent
	public static void registerCapabilities(RegisterCapabilitiesEvent event) {
		event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, INFLATABLE_WALL_BLOCK.get(), SidedInvWrapper::new);
	}
}