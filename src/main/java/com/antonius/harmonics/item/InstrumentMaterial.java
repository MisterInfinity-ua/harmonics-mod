package com.antonius.harmonics.item;

/**
 * Defines every material an instrument can be crafted from, mirroring vanilla's
 * tool tier progression but extending it with materials vanilla never gave a tool line:
 * obsidian, water and lava.
 *
 * range         - how far (in blocks) the instrument's sound/effect reaches
 * durability    - uses before breaking
 * volume        - relative loudness/pitch clarity, used for client-side sound scaling
 * elemental     - true for water/lava; these get elemental status effects instead of pure range/volume scaling
 */
public enum InstrumentMaterial {
    WOOD(8, 32, 0.6f, false),
    STONE(12, 64, 0.7f, false),
    IRON(18, 128, 0.85f, false),
    GOLD(16, 96, 1.1f, false),      // gold: louder/prettier tone, but fragile like gold tools
    DIAMOND(28, 256, 1.0f, false),
    OBSIDIAN(24, 512, 0.75f, false), // obsidian: heavy, ominous, near-unbreakable, nether-safe
    NETHERITE(36, 640, 1.0f, false),
    WATER(20, 200, 0.9f, true),     // elemental: regen aura when played
    LAVA(20, 200, 0.9f, true),      // elemental: fire resistance aura when played
    VOID(44, 1000, 1.2f, false);    // void: ominous, far-reaching, near-unbreakable; darkens hostiles nearby

    private final int range;
    private final int durability;
    private final float volume;
    private final boolean elemental;

    InstrumentMaterial(int range, int durability, float volume, boolean elemental) {
        this.range = range;
        this.durability = durability;
        this.volume = volume;
        this.elemental = elemental;
    }

    public int getRange() {
        return range;
    }

    public int getDurability() {
        return durability;
    }

    public float getVolume() {
        return volume;
    }

    public boolean isElemental() {
        return elemental;
    }

    /** Lowercase id fragment used for registry names, e.g. "netherite_flute". */
    public String id() {
        return this.name().toLowerCase();
    }
}
