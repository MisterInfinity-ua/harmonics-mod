package com.antonius.harmonics.item;

import com.antonius.harmonics.HarmonicsComponents;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;

import java.util.List;

/**
 * A single instrument = one InstrumentMaterial + one InstrumentType.
 * Playing it (right click / use) plays a sound and, for tiers that warrant it,
 * applies an area effect around the player -- calming (water), fire resistance (lava),
 * a slowness pulse for the obsidian horn on nearby hostiles, or a haste "bard" buff
 * for the netherite tier.
 *
 * A "masterwork" (fused) instrument doubles effective range and effect strength;
 * see HarmonicsComponents.MASTERWORK and FusionRecipe.
 */
public class InstrumentItem extends Item {

    private final InstrumentMaterial material;
    private final InstrumentType type;

    public InstrumentItem(InstrumentMaterial material, InstrumentType type, Properties properties) {
        super(properties.durability(material.getDurability()));
        this.material = material;
        this.type = type;
    }

    public InstrumentMaterial getMaterial() {
        return material;
    }

    public InstrumentType getType() {
        return type;
    }

    private boolean isMasterwork(ItemStack stack) {
        return stack.has(HarmonicsComponents.MASTERWORK);
    }

    private int effectiveRange(ItemStack stack) {
        int base = Math.round(material.getRange() * type.getRangeMultiplier());
        return isMasterwork(stack) ? base * 2 : base;
    }

    private float effectiveStrength(ItemStack stack) {
        float base = type.getEffectStrength();
        return isMasterwork(stack) ? base * 2f : base;
    }

    @Override
    public InteractionResult use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);

        if (level.isClientSide()) {
            return InteractionResult.PASS;
        }

        int range = effectiveRange(stack);
        float strength = effectiveStrength(stack);

        level.playSound(null, player.blockPosition(),
                SoundEvents.NOTE_BLOCK_HARP.value(), SoundSource.RECORDS,
                material.getVolume(), pitchFor(type));

        applyAreaEffect(level, player, range, strength);

        if (stack.isDamageableItem()) {
            stack.hurtAndBreak(1, player, hand == InteractionHand.MAIN_HAND
                    ? EquipmentSlot.MAINHAND
                    : EquipmentSlot.OFFHAND);
        }

        return InteractionResult.SUCCESS;
    }

    private float pitchFor(InstrumentType type) {
        return switch (type) {
            case FLUTE -> 1.4f;
            case DRUM -> 0.6f;
            case HORN -> 0.8f;
            case CHIMES -> 1.6f;
            case LUTE -> 1.0f;
        };
    }

    private void applyAreaEffect(Level level, Player player, int range, float strength) {
        AABB box = player.getBoundingBox().inflate(range);

        if (material.isElemental()) {
            List<Player> players = level.getEntitiesOfClass(Player.class, box, p -> true);
            for (Player p : players) {
                if (material == InstrumentMaterial.WATER) {
                    p.addEffect(new MobEffectInstance(MobEffects.REGENERATION,
                            Math.round(100 * strength), 0, true, true));
                } else if (material == InstrumentMaterial.LAVA) {
                    p.addEffect(new MobEffectInstance(MobEffects.FIRE_RESISTANCE,
                            Math.round(200 * strength), 0, true, true));
                }
            }
        } else if (material == InstrumentMaterial.OBSIDIAN && type == InstrumentType.HORN) {
            // Ominous obsidian horn: fear/slowness pulse on nearby hostiles.
            List<Monster> hostiles = level.getEntitiesOfClass(Monster.class, box, e -> true);
            for (Monster hostile : hostiles) {
                hostile.addEffect(new MobEffectInstance(MobEffects.SLOWNESS,
                        Math.round(60 * strength), 1, true, true));
            }
        } else if (material == InstrumentMaterial.NETHERITE) {
            // Bard buff: haste for nearby allies.
            List<Player> players = level.getEntitiesOfClass(Player.class, box, p -> true);
            for (Player p : players) {
                p.addEffect(new MobEffectInstance(MobEffects.HASTE,
                        Math.round(150 * strength), 0, true, true));
            }
        } else if (material == InstrumentMaterial.VOID) {
            // Void anthem: darkens and slows every hostile caught in the blast.
            List<Monster> hostiles = level.getEntitiesOfClass(Monster.class, box, e -> true);
            for (Monster hostile : hostiles) {
                hostile.addEffect(new MobEffectInstance(MobEffects.DARKNESS,
                        Math.round(100 * strength), 1, true, true));
                hostile.addEffect(new MobEffectInstance(MobEffects.SLOWNESS,
                        Math.round(80 * strength), 1, true, true));
            }
        }
    }
}
