package net.mcreator.minigames.procedures;

import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.tags.BlockTags;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.network.chat.Component;
import net.minecraft.core.BlockPos;

import net.mcreator.minigames.network.MinigamesModVariables;

public class SpawnFloorProcedure {
	public static void execute(LevelAccessor world) {
		if (world instanceof ServerLevel _level) {
			_level.getServer().getPlayerList().broadcastSystemMessage(Component.literal("spawn the dungeoooon"), false);
		}
		if ((world.getBlockState(BlockPos.containing(MinigamesModVariables.MapVariables.get(world).dungeonStartLocation.x(), 300, MinigamesModVariables.MapVariables.get(world).dungeonStartLocation.z() - 1)))
				.is(BlockTags.create(ResourceLocation.parse("minigames:room")))) {
			if (world instanceof ServerLevel _serverworld) {
				StructureTemplate template = _serverworld.getStructureManager().getOrCreate(ResourceLocation.fromNamespaceAndPath("minigames", "dungeon_start_generic"));
				if (template != null) {
					template.placeInWorld(_serverworld, BlockPos.containing(MinigamesModVariables.MapVariables.get(world).dungeonStartLocation.x(), 100, MinigamesModVariables.MapVariables.get(world).dungeonStartLocation.z()),
							BlockPos.containing(MinigamesModVariables.MapVariables.get(world).dungeonStartLocation.x(), 100, MinigamesModVariables.MapVariables.get(world).dungeonStartLocation.z()),
							new StructurePlaceSettings().setRotation(Rotation.NONE).setMirror(Mirror.NONE).setIgnoreEntities(false), _serverworld.random, 3);
				}
			}
		} else if ((world.getBlockState(BlockPos.containing(MinigamesModVariables.MapVariables.get(world).dungeonStartLocation.x() + 1, 300, MinigamesModVariables.MapVariables.get(world).dungeonStartLocation.z())))
				.is(BlockTags.create(ResourceLocation.parse("minigames:room")))) {
			if (world instanceof ServerLevel _serverworld) {
				StructureTemplate template = _serverworld.getStructureManager().getOrCreate(ResourceLocation.fromNamespaceAndPath("minigames", "dungeon_start_generic"));
				if (template != null) {
					template.placeInWorld(_serverworld, BlockPos.containing(MinigamesModVariables.MapVariables.get(world).dungeonStartLocation.x() + 28, 100, MinigamesModVariables.MapVariables.get(world).dungeonStartLocation.z()),
							BlockPos.containing(MinigamesModVariables.MapVariables.get(world).dungeonStartLocation.x() + 28, 100, MinigamesModVariables.MapVariables.get(world).dungeonStartLocation.z()),
							new StructurePlaceSettings().setRotation(Rotation.CLOCKWISE_90).setMirror(Mirror.NONE).setIgnoreEntities(false), _serverworld.random, 3);
				}
			}
		} else if ((world.getBlockState(BlockPos.containing(MinigamesModVariables.MapVariables.get(world).dungeonStartLocation.x(), 300, MinigamesModVariables.MapVariables.get(world).dungeonStartLocation.z())))
				.is(BlockTags.create(ResourceLocation.parse("minigames:room")))) {
			if (world instanceof ServerLevel _serverworld) {
				StructureTemplate template = _serverworld.getStructureManager().getOrCreate(ResourceLocation.fromNamespaceAndPath("minigames", "dungeon_start_generic"));
				if (template != null) {
					template.placeInWorld(_serverworld, BlockPos.containing(MinigamesModVariables.MapVariables.get(world).dungeonStartLocation.x(), 100, MinigamesModVariables.MapVariables.get(world).dungeonStartLocation.z() + 28),
							BlockPos.containing(MinigamesModVariables.MapVariables.get(world).dungeonStartLocation.x(), 100, MinigamesModVariables.MapVariables.get(world).dungeonStartLocation.z() + 28),
							new StructurePlaceSettings().setRotation(Rotation.COUNTERCLOCKWISE_90).setMirror(Mirror.NONE).setIgnoreEntities(false), _serverworld.random, 3);
				}
			}
		}
	}
}