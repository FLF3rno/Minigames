package net.mcreator.minigames.mixin;

import net.minecraft.world.item.ItemCooldowns;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;
import java.util.Map;

@Mixin(ItemCooldowns.class)
public interface ItemCooldownsAccessor {
    @Accessor("cooldowns")
    Map<Identifier, ?> getCooldowns();

    @Accessor("tickCount")
    int getTickCount();

    @Invoker("getCooldownGroup")
    Identifier callGetCooldownGroup(ItemStack stack);
}
