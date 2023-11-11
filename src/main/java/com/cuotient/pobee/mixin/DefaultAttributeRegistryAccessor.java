package com.cuotient.pobee.mixin;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.DefaultAttributeRegistry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.Map;

@Mixin(DefaultAttributeRegistry.class)
public interface DefaultAttributeRegistryAccessor {
    @Accessor
    public static Map<EntityType<? extends LivingEntity>, DefaultAttributeContainer> getDEFAULT_ATTRIBUTE_REGISTRY () { return null; };
}
