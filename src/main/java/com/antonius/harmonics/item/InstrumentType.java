package com.antonius.harmonics.item;

/**
 * The five instrument "shapes" available in every tier, similar to how every
 * tool material gets a sword/axe/pickaxe/shovel/hoe.
 */
public enum InstrumentType {
    FLUTE("flute", 1.0f, 0.0f),   // melody: short range, high pitch, no passive effect
    DRUM("drum", 0.6f, 1.0f),     // rhythm: shorter range, strong area pulse (buff/effect radius)
    HORN("horn", 1.6f, 0.4f),     // long range signaling, mob-alerting/scaring at high tiers
    CHIMES("chimes", 0.8f, 0.6f), // passive ambient, can be placed and left playing
    LUTE("lute", 1.1f, 0.2f);     // harmony/chords, used for the "bard buff" song mechanic

    private final String id;
    private final float rangeMultiplier;
    private final float effectStrength;

    InstrumentType(String id, float rangeMultiplier, float effectStrength) {
        this.id = id;
        this.rangeMultiplier = rangeMultiplier;
        this.effectStrength = effectStrength;
    }

    public String id() {
        return id;
    }

    public float getRangeMultiplier() {
        return rangeMultiplier;
    }

    public float getEffectStrength() {
        return effectStrength;
    }
}
