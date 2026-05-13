package net.mcreator.minigames.mixin;

import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Final;

import net.minecraft.world.damagesource.CombatTracker;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextColor;

import net.mcreator.minigames.network.MinigamesModVariables;

@Mixin(CombatTracker.class)
public class CombatTrackerDeathMessageMixin {
	@Shadow
	@Final
	private LivingEntity mob;

	@Inject(method = "getDeathMessage", at = @At("RETURN"), cancellable = true)
	private void minigames$recolorDeathMessage(CallbackInfoReturnable<Component> cir) {
		LivingEntity victim = this.mob;
		if (!(victim instanceof Player victimPlayer))
			return;
		String name = victimPlayer.getGameProfile().getName();
		TextColor color = resolvePlayerColor(victimPlayer);
		if (color == null)
			return;
		Component coloredVictimName = Component.literal(name).setStyle(Style.EMPTY.withColor(color));
		Component original = cir.getReturnValue();
		if (original == null) {
			cir.setReturnValue(Component.empty().append(coloredVictimName).append(Component.literal(" died")));
			return;
		}
		String raw = original.getString();
		String suffix = raw.startsWith(name) ? raw.substring(name.length()) : " died";
		cir.setReturnValue(Component.empty().append(coloredVictimName).append(Component.literal(suffix)));
	}

	private TextColor resolvePlayerColor(Player player) {
		MinigamesModVariables.PlayerVariables vars = player.getData(MinigamesModVariables.PLAYER_VARIABLES);
		if (vars == null || vars.color == null || vars.color.isEmpty())
			return null;
		String hex = vars.color.startsWith("#") ? vars.color : "#" + vars.color;
		return TextColor.parseColor(hex).result().orElse(null);
	}
}
