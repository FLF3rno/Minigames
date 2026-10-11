package net.mcreator.minigames.procedures;

import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Entity;
import net.minecraft.tags.TagKey;
import net.minecraft.resources.Identifier;
import net.minecraft.core.registries.Registries;

import net.mcreator.minigames.entity.CandleheadEntity;
import net.mcreator.minigames.entity.ChurchSharpenerEntity;
import net.mcreator.minigames.entity.CryptLurkerEntity;
import net.mcreator.minigames.entity.DemonEntity;
import net.mcreator.minigames.entity.GravediggerEntity;
import net.mcreator.minigames.entity.GravediggerMinibossEntity;
import net.mcreator.minigames.entity.LaserStatueEntity;
import net.mcreator.minigames.entity.PreacherEntity;
import net.mcreator.minigames.entity.SculklingEntity;
import net.mcreator.minigames.entity.ShieldAngelEntity;
import net.mcreator.minigames.entity.WorshipperEntity;
import net.mcreator.minigames.network.MinigamesModVariables;

import java.util.Comparator;

public class PotionExplosionRightclickedProcedure {
	private static final TagKey<EntityType<?>> DUNGEON_TAG = TagKey.create(Registries.ENTITY_TYPE, Identifier.parse("minigames:dungeon"));

	public static void execute(LevelAccessor world, double x, double y, double z, Entity entity, ItemStack itemstack) {
		if (entity == null)
			return;
		{
			final Vec3 _center = new Vec3(x, y, z);
			boolean exploded = false;
			for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(30 / 2d), e -> true).stream().sorted(Comparator.comparingDouble(_entcnd -> _entcnd.distanceToSqr(_center))).toList()) {
				if (!isMonster(entityiterator))
					continue;
				if (!isInSameRoom(world, entity, entityiterator))
					continue;
				if (!ReturnAllPotionEffectsProcedure.execute(entityiterator).isEmpty()) {
					ExplodeProcedure.execute(world, entityiterator.getX(), entityiterator.getY(), entityiterator.getZ(), entity, false, true, GetItemAttributeProcedure.execute(itemstack, "minigames:explosion_damage"), 0, 3, "normal");
					exploded = true;
				}
			}
			if (exploded) {
				ApplyCooldownProcedure.execute(entity, itemstack, GetItemAttributeProcedure.execute(itemstack, "minigames:ability_cooldown"));
			}
		}
	}

	public static boolean isMonster(Entity entity) {
		if (entity == null || entity instanceof Player) {
			return false;
		}
		return entity instanceof Enemy || entity instanceof Monster || entity.is(DUNGEON_TAG);
	}

	public static boolean isInSameRoom(LevelAccessor world, Entity player, Entity target) {
		if (player == null || target == null) {
			return false;
		}
		boolean isDungeon = MinigamesModVariables.MapVariables.get(world).playingDungeons
				|| (world instanceof Level lvl && lvl.dimension().identifier().toString().equals("minigames:dungeon_dimension"));
		if (!isDungeon) {
			return true;
		}
		int playerRoomId = (int) MinigamesModVariables.MapVariables.get(world).currentRoomID;
		int targetRoomId = getEntityRoomId(target);
		if (playerRoomId > 0 && targetRoomId > 0) {
			return playerRoomId == targetRoomId;
		}
		if (playerRoomId == 0 && targetRoomId > 0) {
			return false;
		}
		int playerGridX = (int) Math.floor(player.getX() / 29.0);
		int playerGridZ = (int) Math.floor(player.getZ() / 29.0);
		int targetGridX = (int) Math.floor(target.getX() / 29.0);
		int targetGridZ = (int) Math.floor(target.getZ() / 29.0);
		return playerGridX == targetGridX && playerGridZ == targetGridZ;
	}

	public static int getEntityRoomId(Entity entity) {
		if (entity == null) {
			return 0;
		}
		if (entity instanceof CandleheadEntity e) {
			return e.getEntityData().get(CandleheadEntity.DATA_ID);
		}
		if (entity instanceof ChurchSharpenerEntity e) {
			return e.getRoomID();
		}
		if (entity instanceof CryptLurkerEntity e) {
			return e.getRoomID();
		}
		if (entity instanceof DemonEntity e) {
			return e.getEntityData().get(DemonEntity.DATA_ID);
		}
		if (entity instanceof GravediggerEntity e) {
			return e.getRoomID();
		}
		if (entity instanceof GravediggerMinibossEntity e) {
			return e.getRoomID();
		}
		if (entity instanceof LaserStatueEntity e) {
			return e.getRoomID();
		}
		if (entity instanceof PreacherEntity e) {
			return e.getRoomID();
		}
		if (entity instanceof SculklingEntity e) {
			return e.getEntityData().get(SculklingEntity.DATA_ID);
		}
		if (entity instanceof ShieldAngelEntity e) {
			return e.getEntityData().get(ShieldAngelEntity.DATA_ID);
		}
		if (entity instanceof WorshipperEntity e) {
			return e.getRoomID();
		}
		int id = entity.getPersistentData().getIntOr("DataID", 0);
		if (id != 0) {
			return id;
		}
		return (int) entity.getPersistentData().getDoubleOr("DataID", 0);
	}
}