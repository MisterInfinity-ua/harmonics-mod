package com.antonius.harmonics.item;

public enum WeaponType {
    SWORD("sword", 4.0f, -2.4f, true),
    AXE("axe", 5.0f, -3.2f, true),
    SHOVEL("shovel", 3.5f, -2.0f, false),
    PICKAXE("pickaxe", 3.0f, -1.8f, false);

    private final String id;
    private final float baseDamageBonus;
    private final float baseAttackSpeed;
    private final boolean isWeapon;

    WeaponType(String id, float baseDamageBonus, float baseAttackSpeed, boolean isWeapon) {
        this.id = id;
        this.baseDamageBonus = baseDamageBonus;
        this.baseAttackSpeed = baseAttackSpeed;
        this.isWeapon = isWeapon;
    }

    public String id() { return id; }
    public float getBaseDamageBonus() { return baseDamageBonus; }
    public float getBaseAttackSpeed() { return baseAttackSpeed; }
    public boolean isWeapon() { return isWeapon; }

    public float getEffectiveAttackSpeed(WeaponMaterial material) {
        return baseAttackSpeed + material.getAttackSpeed() * 0.15f;
    }

    public float getEffectiveDamage(WeaponMaterial material) {
        return baseDamageBonus + material.getAttackDamage();
    }
}
