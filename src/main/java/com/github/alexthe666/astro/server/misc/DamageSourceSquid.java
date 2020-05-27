package com.github.alexthe666.astro.server.misc;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntityDamageSource;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.Explosion;

import javax.annotation.Nullable;

public class DamageSourceSquid extends EntityDamageSource {

    public DamageSourceSquid(String damageTypeIn, LivingEntity entity) {
        super(damageTypeIn, entity);
    }

    public static DamageSource causeSquidDamage(@Nullable Explosion explosionIn) {
        return explosionIn != null && explosionIn.getExplosivePlacedBy() != null ? (new DamageSourceSquid("squidfall", explosionIn.getExplosivePlacedBy())).setDifficultyScaled().setExplosion() : (new DamageSourceSquid("squidfall", null)).setDifficultyScaled().setExplosion();
    }

    public ITextComponent getDeathMessage(LivingEntity entityLivingBaseIn) {
        LivingEntity livingentity = entityLivingBaseIn.getAttackingEntity();
        String s = "death.attack.squidfall_" + livingentity.getRNG().nextInt(3);
        return new TranslationTextComponent(s, entityLivingBaseIn.getDisplayName());
    }


}
