package com.antonius.harmonics.item;

public enum WeaponMaterial {
    OBSIDIAN(6.0f, 800, 14, -2.8f, 6.0f, 3, SpecialEffect.FIRE_ASPECT, 0xFF0E7A9A /*placeholder*/),
    LAVA(11.0f, 500, 10, -3.0f, 9.0f, 3, SpecialEffect.INFERNO, 0xFFFF4700),
    NETHERITE_PLUS(14.0f, 1200, 20, -2.6f, 10.0f, 4, SpecialEffect.SOUL_STEAL, 0xFFA96666),
    VOID(18.0f, 2000, 30, -2.4f, 12.0f, 5, SpecialEffect.VOID_TOUCH, 0xFF34123A);

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
        NONE
    }
}
