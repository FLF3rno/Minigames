package net.mcreator.minigames.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(targets = "net.minecraft.world.item.ItemCooldowns$CooldownInstance")
public interface CooldownInstanceAccessor {
    @Accessor("startTime")
    int getStartTime();

    @Accessor("endTime")
    int getEndTime();

    @Accessor("startTime")
    @Mutable
    void setStartTime(int startTime);

    @Accessor("endTime")
    @Mutable
    void setEndTime(int endTime);
}
