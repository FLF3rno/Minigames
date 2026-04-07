package net.mcreator.minigames.procedures;

import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.bus.api.Event;

import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.effect.MobEffectInstance;

import net.mcreator.minigames.network.MinigamesModVariables;
import net.mcreator.minigames.init.MinigamesModMobEffects;
import net.mcreator.minigames.init.MinigamesModItems;

import javax.annotation.Nullable;

@EventBusSubscriber
public class BlockArmorStandDestroyProcedure {
	@SubscribeEvent
	public static void onPlayerTick(PlayerTickEvent.Post event) {
		execute(event, event.getEntity().level(), event.getEntity());
	}

	public static void execute(LevelAccessor world, Entity entity) {
		execute(null, world, entity);
	}

	private static void execute(@Nullable Event event, LevelAccessor world, Entity entity) {
		if (entity == null)
			return;
		if (!(entity instanceof Player _player))
			return;
		if (_player.level().isClientSide())
			return;
		if (!MinigamesModVariables.MapVariables.get(world).inGracePeriod)
			return;
		Vec3 _start = _player.getEyePosition(1.0F);
		Vec3 _view = _player.getViewVector(1.0F);
		Vec3 _end = _start.add(_view.scale(5.0D));
		AABB _aabb = _player.getBoundingBox().expandTowards(_view.scale(5.0D)).inflate(1.0D);
		HitResult _hit = ProjectileUtil.getEntityHitResult(_player, _start, _end, _aabb, e -> e instanceof ArmorStand, 25.0D);
		Entity _target = _hit instanceof EntityHitResult _ehr ? _ehr.getEntity() : null;
		if (!(_target instanceof ArmorStand))
			return;
		if ((_target instanceof LivingEntity _entGetArmor ? _entGetArmor.getItemBySlot(EquipmentSlot.HEAD) : ItemStack.EMPTY).getItem() == MinigamesModItems.CROWN_HELMET_HELMET.get()) {
			_player.addEffect(new MobEffectInstance(MinigamesModMobEffects.BLOCK_LEFT_CLICK, 2, 1, false, false));
		}
	}
}
