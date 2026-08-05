package com.antonius.harmonics.item;

import com.antonius.harmonics.HarmonicsComponents;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.Tool;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.AABB;
import net.minecraft.util.RandomSource;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class ModWeaponItem extends Item {

    private final WeaponMaterial material;
    private final WeaponType type;
    
    // Lazy supplier for Tool component - avoids accessing tags during construction
    private final Supplier<Tool> toolSupplier;
    
    // Cached tool component (volatile for thread safety)
    private volatile Tool cachedTool;

    public ModWeaponItem(WeaponMaterial material, WeaponType type, Properties properties) {
        super(properties
                .durability(material.getDurability()));
        // NOTE: We do NOT set DataComponents.TOOL here because tags are not bound yet!
        // The Tool component will be applied lazily when first needed.
        this.material = material;
        this.type = type;
        
        // Create a lazy supplier that only resolves tags when actually called
        this.toolSupplier = () -> buildToolComponentForType(material, type);
    }

    // Registry in this version only exposes getTagOrEmpty(TagKey) -> Iterable<Holder<Block>>;
    // there's no getTag/getOrCreateTag/getOrThrow that hands back a HolderSet directly, so we
    // collect the iterable into a List and wrap it with HolderSet.direct(...) ourselves.
    private static HolderSet<Block> resolveTag(TagKey<Block> tag) {
        List<Holder<Block>> holders = new ArrayList<>();
        BuiltInRegistries.BLOCK.getTagOrEmpty(tag).forEach(holders::add);
        return HolderSet.direct(holders);
    }

    /**
     * Creates the Tool component with proper tag references using Tool.Rule.minesAndDrops().
     * This is called lazily AFTER tags are bound, avoiding the crash.
     * V4 FIX: Tool-specific behaviors per weapon type!
     */
    private static Tool buildToolComponentForType(WeaponMaterial material, WeaponType type) {
        List<Tool.Rule> rules;
        
        switch (type) {
            case PICKAXE:
                // Pickaxes are efficient at mining rock/ore blocks
                rules = List.of(
                    Tool.Rule.minesAndDrops(resolveTag(BlockTags.MINEABLE_WITH_PICKAXE), material.getMiningSpeed()),
                    Tool.Rule.minesAndDrops(resolveTag(BlockTags.MINEABLE_WITH_AXE), material.getMiningSpeed() * 0.3f),
                    Tool.Rule.minesAndDrops(resolveTag(BlockTags.MINEABLE_WITH_SHOVEL), material.getMiningSpeed() * 0.2f),
                    Tool.Rule.minesAndDrops(resolveTag(BlockTags.LEAVES), material.getMiningSpeed() * 0.1f)
                );
                break;
                
            case AXE:
                // Axes are efficient at chopping wood
                rules = List.of(
                    Tool.Rule.minesAndDrops(resolveTag(BlockTags.MINEABLE_WITH_AXE), material.getMiningSpeed() * 1.1f),
                    Tool.Rule.minesAndDrops(resolveTag(BlockTags.MINEABLE_WITH_PICKAXE), material.getMiningSpeed() * 0.3f),
                    Tool.Rule.minesAndDrops(resolveTag(BlockTags.MINEABLE_WITH_SHOVEL), material.getMiningSpeed() * 0.2f),
                    Tool.Rule.minesAndDrops(resolveTag(BlockTags.LEAVES), material.getMiningSpeed() * 0.8f)
                );
                break;
                
            case SHOVEL:
                // Shovels are good at dirt/sand/gravel
                rules = List.of(
                    Tool.Rule.minesAndDrops(resolveTag(BlockTags.MINEABLE_WITH_SHOVEL), material.getMiningSpeed() * 0.9f),
                    Tool.Rule.minesAndDrops(resolveTag(BlockTags.MINEABLE_WITH_PICKAXE), material.getMiningSpeed() * 0.1f),
                    Tool.Rule.minesAndDrops(resolveTag(BlockTags.MINEABLE_WITH_AXE), material.getMiningSpeed() * 0.1f)
                );
                break;
                
            case SWORD:
            default:
                // V17 FIX: Swords are weapons, NOT tools!
                // They should mine at the same speed as an empty hand (1.0f)
                // This prevents swords from being used as efficient miners
                rules = List.of(
                    Tool.Rule.minesAndDrops(resolveTag(BlockTags.MINEABLE_WITH_PICKAXE), 1.0f),
                    Tool.Rule.minesAndDrops(resolveTag(BlockTags.MINEABLE_WITH_AXE), 1.0f),
                    Tool.Rule.minesAndDrops(resolveTag(BlockTags.MINEABLE_WITH_SHOVEL), 1.0f),
                    Tool.Rule.minesAndDrops(resolveTag(BlockTags.LEAVES), 1.0f)
                );
                break;
        }
        
        // Mining level based on material tier (fixes "punching" display!)
        int miningLevel = getMiningLevelForMaterial(material);
        
        return new Tool(rules, 1.0f, miningLevel, true);
    }
    
    /**
     * Get appropriate mining level based on material tier.
     * Uses the material's built-in mining level property.
     */
    private static int getMiningLevelForMaterial(WeaponMaterial material) {
        return material.getMiningLevel();
    }

    /**
     * Ensures the Tool component is applied to the stack.
     * Called before any operation that needs tool properties.
     */
    private void ensureToolComponent(ItemStack stack) {
        if (cachedTool == null) {
            synchronized (this) {
                if (cachedTool == null) {
                    cachedTool = toolSupplier.get();
                }
            }
        }
        if (!stack.has(DataComponents.TOOL)) {
            stack.set(DataComponents.TOOL, cachedTool);
        }
    }

    @Override
    public float getDestroySpeed(ItemStack stack, net.minecraft.world.level.block.state.BlockState state) {
        ensureToolComponent(stack);
        return super.getDestroySpeed(stack, state);
    }

    public WeaponMaterial getWeaponMaterial() { return material; }
    public WeaponType getWeaponType() { return type; }

    public boolean isMasterwork(ItemStack stack) {
        return stack.has(HarmonicsComponents.MASTERWORK);
    }

    public float getAttackDamage() {
        return type.getEffectiveDamage(material);
    }

    public float getAttackSpeed() {
        return type.getEffectiveAttackSpeed(material);
    }

    @Override
    public void hurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        // Damage is now handled by the vanilla attribute system (attack_damage component).
        // hurtEnemy only handles special effects and durability.

        // Apply masterwork bonus damage (extra damage on top of base attribute)
        if (isMasterwork(stack)) {
            float extraDamage = getAttackDamage() * 0.5f;
            // Bypass invulnerability frames for masterwork bonus
            int prevInvul = target.invulnerableTime;
            target.invulnerableTime = 0;
            var bonusSource = attacker instanceof Player player
                    ? attacker.level().damageSources().playerAttack(player)
                    : attacker.level().damageSources().mobAttack(attacker);
            target.hurt(bonusSource, extraDamage);
            target.invulnerableTime = prevInvul;
        }

        if (!attacker.level().isClientSide()) {
            applySpecialEffect(stack, target, attacker);
        }

        if (stack.isDamageableItem()) {
            stack.hurtAndBreak(1, attacker,
                    attacker.getUsedItemHand() == InteractionHand.MAIN_HAND
                            ? EquipmentSlot.MAINHAND
                            : EquipmentSlot.OFFHAND);
        }
    }

    private void applySpecialEffect(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        Level level = attacker.level();
        boolean masterwork = isMasterwork(stack);
        float multiplier = masterwork ? 2.0f : 1.0f;

        switch (material.getSpecialEffect()) {
            case FIRE_ASPECT -> {
                int fireDuration = (int) (3.0f * multiplier);
                target.igniteForSeconds(fireDuration);
            }
            case INFERNO -> {
                target.igniteForSeconds((int) (5.0f * multiplier));
                if (target.onGround() && !(attacker.fallDistance > 0.0)) break;
                double radius = 2.0 * multiplier;
                level.explode(null, target.getX(), target.getY(), target.getZ(),
                        (float) radius * 0.3f, false, Level.ExplosionInteraction.NONE);
            }
            case SOUL_STEAL -> {
                if (attacker instanceof Player player) {
                    float healAmount = 1.0f * multiplier;
                    player.heal(healAmount);
                    player.addEffect(new MobEffectInstance(MobEffects.REGENERATION,
                            (int) (20.0f * multiplier), 1, true, true));
                }
                target.addEffect(new MobEffectInstance(MobEffects.SLOWNESS,
                        (int) (40.0f * multiplier), 1, false, true));
            }
            case VOID_TOUCH -> {
                target.hurt(level.damageSources().magic(), 2.0f * multiplier);
                double roll = Math.random();
                double chance = masterwork ? 0.4 : 0.2;
                if (roll < chance) {
                    double newX = target.getX() + (Math.random() - 0.5) * 6.0;
                    double newZ = target.getZ() + (Math.random() - 0.5) * 6.0;
                    target.teleportTo(newX, target.getY(), newZ);
                }
                target.addEffect(new MobEffectInstance(MobEffects.DARKNESS,
                        (int) (30.0f * multiplier), 0, false, true));
            }
            case WITHER_TOUCH -> {
                target.addEffect(new MobEffectInstance(MobEffects.WITHER,
                        (int) (60.0f * multiplier), masterwork ? 1 : 0, false, true));
                if (masterwork && attacker instanceof Player player) {
                    player.addEffect(new MobEffectInstance(MobEffects.STRENGTH,
                            (int) (100.0f * multiplier), 0, false, true));
                }
            }
            case PRISM -> {
                float damage = getAttackDamage() * 0.5f * multiplier;
                for (int i = 0; i < 3; i++) {
                    target.hurt(level.damageSources().magic(), damage);
                }
                target.addEffect(new MobEffectInstance(MobEffects.BLINDNESS,
                        (int) (30.0f * multiplier), 2, false, true));
                if (attacker instanceof Player player && masterwork) {
                    player.addEffect(new MobEffectInstance(MobEffects.GLOWING,
                            (int) (60.0f * multiplier), 0, false, true));
                }
            }
            case SHADOW_STEP -> {
                if (attacker instanceof Player player && masterwork) {
                    player.addEffect(new MobEffectInstance(MobEffects.INVISIBILITY,
                            (int) (40.0f * multiplier), 0, false, true));
                }
                target.addEffect(new MobEffectInstance(MobEffects.WEAKNESS,
                        (int) (50.0f * multiplier), 2, false, true));
                target.addEffect(new MobEffectInstance(MobEffects.SLOWNESS,
                        (int) (50.0f * multiplier), 2, false, true));
            }
            case BLINK -> {
                if (masterwork && attacker instanceof Player player) {
                    double newX = player.getX() + (Math.random() - 0.5) * 8.0;
                    double newZ = player.getZ() + (Math.random() - 0.5) * 8.0;
                    player.teleportTo(newX, player.getY(), newZ);
                }
                target.addEffect(new MobEffectInstance(MobEffects.LEVITATION,
                        (int) (20.0f * multiplier), 1, false, true));
                target.addEffect(new MobEffectInstance(MobEffects.BLINDNESS,
                        (int) (40.0f * multiplier), 0, false, true));
            }
            case FREEZE -> {
                target.addEffect(new MobEffectInstance(MobEffects.SLOWNESS,
                        (int) (60.0f * multiplier), 2, false, true));
                target.addEffect(new MobEffectInstance(MobEffects.WEAKNESS,
                        (int) (60.0f * multiplier), 1, false, true));
                if (masterwork && attacker instanceof Player player) {
                    player.addEffect(new MobEffectInstance(MobEffects.SLOW_FALLING,
                            (int) (80.0f * multiplier), 0, false, true));
                }
            }
            case LIFE_STEAL -> {
                if (attacker instanceof Player player) {
                    float healAmount = getAttackDamage() * 0.1f * multiplier;
                    player.heal(healAmount);
                    if (masterwork) {
                        player.addEffect(new MobEffectInstance(MobEffects.ABSORPTION,
                                (int) (60.0f * multiplier), 0, false, true));
                    }
                }
                target.addEffect(new MobEffectInstance(MobEffects.GLOWING,
                        (int) (20.0f * multiplier), 0, false, true));
            }
        }
    }

    @Override
    public InteractionResult use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);

        if (type == WeaponType.SWORD && !level.isClientSide() && !player.getCooldowns().isOnCooldown(stack)) {
            performSpecialAttack(stack, player, level);
            player.getCooldowns().addCooldown(stack, isMasterwork(stack) ? 40 : 60);

            if (stack.isDamageableItem()) {
                stack.hurtAndBreak(isMasterwork(stack) ? 1 : 2, player,
                        player.getUsedItemHand() == InteractionHand.MAIN_HAND
                                ? EquipmentSlot.MAINHAND
                                : EquipmentSlot.OFFHAND);
            }
            return InteractionResult.SUCCESS;
        }
        return super.use(level, player, hand);
    }

    private void performSpecialAttack(ItemStack stack, Player player, Level level) {
        AABB box = player.getBoundingBox().inflate(3.0);
        boolean masterwork = isMasterwork(stack);
        float damage = getAttackDamage() * (masterwork ? 1.5f : 1.0f);

        level.getEntitiesOfClass(LivingEntity.class, box, e -> e != player && e.isAlive())
                .forEach(entity -> {
                    entity.hurt(level.damageSources().playerAttack(player), damage);
                    applySpecialEffect(stack, entity, player);
                });

        level.playSound(null, player.blockPosition(), SoundEvents.PLAYER_ATTACK_SWEEP,
                SoundSource.PLAYERS, 1.0f, masterwork ? 0.8f : 1.0f);
    }

    public boolean isFireResistant() {
        return material == WeaponMaterial.LAVA || material == WeaponMaterial.NETHERITE_PLUS
                || material == WeaponMaterial.WITHER;
    }
}
