package com.antonius.harmonics.item;

public enum WeaponMaterial {
    OBSIDIAN(6.0f, 800, 14, -2.8f, 6.0f, 3, SpecialEffect.FIRE_ASPECT, 0xFF0E7A9A),
    LAVA(11.0f, 500, 10, -3.0f, 9.0f, 3, SpecialEffect.INFERNO, 0xFFFF4700),
    NETHERITE_PLUS(14.0f, 1200, 20, -2.6f, 10.0f, 4, SpecialEffect.SOUL_STEAL, 0xFFA96666),
    WITHER(15.0f, 1600, 25, -2.5f, 11.0f, 4, SpecialEffect.WITHER_TOUCH, 0xFF2B2B2E),
    CRYSTAL(12.0f, 900, 18, -2.7f, 7.0f, 3, SpecialEffect.PRISM, 0xFF66DDFF),
    SHADOW(13.0f, 1100, 16, -2.6f, 8.0f, 3, SpecialEffect.SHADOW_STEP, 0xFF2A1A3A),
    ENDER(18.0f, 1800, 22, -2.4f, 9.5f, 4, SpecialEffect.BLINK, 0xFF8A2BE2),
    FROST(13.0f, 700, 15, -2.6f, 7.0f, 3, SpecialEffect.FREEZE, 0xFF44CCFF),
    VOID(18.0f, 2000, 30, -2.4f, 12.0f, 5, SpecialEffect.VOID_TOUCH, 0xFF34123A),
    RUBY(15.0f, 1400, 24, -2.5f, 9.0f, 4, SpecialEffect.LIFE_STEAL, 0xFFC21E2B);

    private final float attackDamage;
    private final int durability;
    private final int enchantability;
    private final float attackSpeed;
    private final float miningSpeed;
    private final int miningLevel;
    private final SpecialEffect specialEffect;
    private final int color;

    WeaponMaterial(float attackDamage, int durability, int enchantability, float attackSpeed,
                   float miningSpeed, int miningLevel, SpecialEffect specialEffect, int color) {
        this.attackDamage = attackDamage;
        this.durability = durability;
        this.enchantability = enchantability;
        this.attackSpeed = attackSpeed;
        this.miningSpeed = miningSpeed;
        this.miningLevel = miningLevel;
        this.specialEffect = specialEffect;
        this.color = color;
    }

    public float getAttackDamage() { return attackDamage; }
    public int getDurability() { return durability; }
    public int getEnchantability() { return enchantability; }
    public float getAttackSpeed() { return attackSpeed; }
    public float getMiningSpeed() { return miningSpeed; }
    public int getMiningLevel() { return miningLevel; }
    public SpecialEffect getSpecialEffect() { return specialEffect; }
    public int getColor() { return color; }

    public String id() {
        return this.name().toLowerCase();
    }

    public enum SpecialEffect {
        FIRE_ASPECT,
        INFERNO,
        SOUL_STEAL,
        VOID_TOUCH,
        WITHER_TOUCH,
        PRISM,
        SHADOW_STEP,
        BLINK,
        FREEZE,
        LIFE_STEAL,
        NONE
    }
}
