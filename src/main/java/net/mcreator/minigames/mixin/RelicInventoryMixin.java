package net.mcreator.minigames.mixin;

import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import net.mcreator.minigames.procedures.RelicEquippedProcedure;
import net.mcreator.minigames.procedures.RelicUnequippedProcedure;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Inventory.class)
public class RelicInventoryMixin {

    @Shadow
    @Final
    public Player player;

    private ItemStack minigames$last34 = ItemStack.EMPTY;
    private ItemStack minigames$last35 = ItemStack.EMPTY;
    private boolean minigames$initialized = false;
    private boolean minigames$updatingRelics = false;

    @Inject(method = "setChanged", at = @At("TAIL"))
    private void minigames$detectRelicChanges(CallbackInfo ci) {
        if (minigames$updatingRelics) {
            return;
        }

        Inventory inventory = (Inventory) (Object) this;

        ItemStack current34 = inventory.getItem(34).copy();
        ItemStack current35 = inventory.getItem(35).copy();

        if (!minigames$initialized) {
            minigames$last34 = current34.copy();
            minigames$last35 = current35.copy();
            minigames$initialized = true;
            return;
        }

        boolean slot34Changed = !ItemStack.matches(minigames$last34, current34);
        boolean slot35Changed = !ItemStack.matches(minigames$last35, current35);

        if (!slot34Changed && !slot35Changed) {
            return;
        }

        boolean movedBetweenRelicSlots =
                ItemStack.matches(minigames$last34, current35)
             && ItemStack.matches(minigames$last35, current34);

        if (!movedBetweenRelicSlots) {
            minigames$updatingRelics = true;
            try {
                ItemStack previous34 = minigames$last34.copy();
                ItemStack previous35 = minigames$last35.copy();

                // Update the cache before the callbacks run so any item rewrites they cause
                // don't get treated as a fresh equip/unequip event.
                minigames$last34 = current34.copy();
                minigames$last35 = current35.copy();

                if (slot34Changed) {

                    if (!previous34.isEmpty()) {
                        RelicUnequippedProcedure.execute(
                                player,
                                previous34.copy()
                        );
                    }

                    if (!current34.isEmpty()) {
                        RelicEquippedProcedure.execute(
                                player,
                                current34.copy()
                        );
                    }
                }

                if (slot35Changed) {

                    if (!previous35.isEmpty()) {
                        RelicUnequippedProcedure.execute(
                                player,
                                previous35.copy()
                        );
                    }

                    if (!current35.isEmpty()) {
                        RelicEquippedProcedure.execute(
                                player,
                                current35.copy()
                        );
                    }
                }
            } finally {
                minigames$updatingRelics = false;
            }
            return;
        }

        minigames$last34 = current34.copy();
        minigames$last35 = current35.copy();
    }
}
