package net.mcreator.minigames.procedures;

import net.neoforged.neoforge.client.network.ClientPacketDistributor;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.bus.api.SubscribeEvent;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.resources.Identifier;
import net.minecraft.nbt.CompoundTag;

import net.mcreator.minigames.network.BoxingPunchMessage;
import net.mcreator.minigames.init.MinigamesModItems;

@EventBusSubscriber
public class BoxingMasteryHandler {
	private static final Identifier BOXING_DAMAGE_ID = Identifier.fromNamespaceAndPath("minigames", "boxing_mastery_damage");
	private static final Identifier BOXING_SPEED_ID = Identifier.fromNamespaceAndPath("minigames", "boxing_mastery_speed");
	private static final Identifier BOXING_RANGE_ID = Identifier.fromNamespaceAndPath("minigames", "boxing_mastery_entity_range");
	private static final Identifier BOXING_BLOCK_RANGE_ID = Identifier.fromNamespaceAndPath("minigames", "boxing_mastery_block_range");

	public static boolean isBoxing(Player player) {
		if (player == null || !player.getMainHandItem().isEmpty())
			return false;
		ItemStack relic = CheckRelicProcedure.getRelicStack(player, new ItemStack(MinigamesModItems.BOXING_MASTERY.get()));
		return !relic.isEmpty();
	}

	@SubscribeEvent
	public static void onPlayerTick(PlayerTickEvent.Post event) {
		Player player = event.getEntity();
		if (player == null)
			return;

		boolean boxing = isBoxing(player);

		AttributeInstance damageAttr = player.getAttribute(Attributes.ATTACK_DAMAGE);
		AttributeInstance speedAttr = player.getAttribute(Attributes.ATTACK_SPEED);
		AttributeInstance rangeAttr = player.getAttribute(Attributes.ENTITY_INTERACTION_RANGE);
		AttributeInstance blockRangeAttr = player.getAttribute(Attributes.BLOCK_INTERACTION_RANGE);

		if (boxing) {
			ItemStack relic = CheckRelicProcedure.getRelicStack(player, new ItemStack(MinigamesModItems.BOXING_MASTERY.get()));
			double damage = GetItemAttributeProcedure.execute(relic, "minecraft:attack_damage");
			if (damage <= 0)
				damage = 2.0;

			double speed = GetItemAttributeProcedure.execute(relic, "minecraft:attack_speed");
			if (speed == 0)
				speed = -1.0;

			double rangePenalty = -0.6;

			if (damageAttr != null && !damageAttr.hasModifier(BOXING_DAMAGE_ID)) {
				damageAttr.addTransientModifier(new AttributeModifier(BOXING_DAMAGE_ID, damage, AttributeModifier.Operation.ADD_VALUE));
			}
			if (speedAttr != null && !speedAttr.hasModifier(BOXING_SPEED_ID)) {
				speedAttr.addTransientModifier(new AttributeModifier(BOXING_SPEED_ID, speed, AttributeModifier.Operation.ADD_VALUE));
			}
			if (rangeAttr != null && !rangeAttr.hasModifier(BOXING_RANGE_ID)) {
				rangeAttr.addTransientModifier(new AttributeModifier(BOXING_RANGE_ID, rangePenalty, AttributeModifier.Operation.ADD_VALUE));
			}
			if (blockRangeAttr != null && !blockRangeAttr.hasModifier(BOXING_BLOCK_RANGE_ID)) {
				blockRangeAttr.addTransientModifier(new AttributeModifier(BOXING_BLOCK_RANGE_ID, rangePenalty, AttributeModifier.Operation.ADD_VALUE));
			}
		} else {
			if (damageAttr != null && damageAttr.hasModifier(BOXING_DAMAGE_ID)) {
				damageAttr.removeModifier(BOXING_DAMAGE_ID);
			}
			if (speedAttr != null && speedAttr.hasModifier(BOXING_SPEED_ID)) {
				speedAttr.removeModifier(BOXING_SPEED_ID);
			}
			if (rangeAttr != null && rangeAttr.hasModifier(BOXING_RANGE_ID)) {
				rangeAttr.removeModifier(BOXING_RANGE_ID);
			}
			if (blockRangeAttr != null && blockRangeAttr.hasModifier(BOXING_BLOCK_RANGE_ID)) {
				blockRangeAttr.removeModifier(BOXING_BLOCK_RANGE_ID);
			}
		}
	}

	public static void triggerPunch(Player player) {
		if (player == null || !isBoxing(player))
			return;

		CompoundTag data = player.getPersistentData();
		boolean isRight = !data.getBooleanOr("BoxingPunchHand", false);
		data.putBoolean("BoxingPunchHand", isRight);
		String anim = isRight ? "minigames:right_punch" : "minigames:left_punch";

		data.putString("PlayerCurrentAnimation", anim);
		data.putBoolean("OverrideCurrentAnimation", true);
		data.putBoolean("FirstPersonAnimation", true);
		ClientPacketDistributor.sendToServer(new BoxingPunchMessage(isRight));
	}
}
