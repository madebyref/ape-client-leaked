package net.ape.mixins;

import net.minecraft.class_310;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin({class_310.class})
public interface MinecraftClientAccessor {
   @Accessor("attackCooldown")
   int getAttackCooldown();

   @Invoker("doAttack")
   boolean invokeDoAttack();
}
