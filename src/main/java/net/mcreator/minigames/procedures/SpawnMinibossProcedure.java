package net.mcreator.minigames.procedures;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.LevelAccessor;

public class SpawnMinibossProcedure {
	public static void spawn(LevelAccessor world, double x, double y, double z, float yaw, float pitch) {

		TagKey<EntityType<?>> buriedTag = TagKey.create(Registries.ENTITY_TYPE, Identifier.parse("minigames:miniboss"));
		var types = BuiltInRegistries.ENTITY_TYPE.stream().filter(entityType -> entityType.builtInRegistryHolder().is(buriedTag)).toList();
		if (!types.isEmpty()) {
			EntityType<?> entityType = types.get(world.getRandom().nextInt(types.size()));
			Identifier id = BuiltInRegistries.ENTITY_TYPE.getKey(entityType);

			SummonMinionProcedure.execute(world, x + 0.5D, y, z + 0.5D, null, yaw * -1, pitch * -1, 0, 0, 0, id.toString());

		}
	}
}