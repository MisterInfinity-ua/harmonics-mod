# HARMONICS

> **Harmonics** is a Fabric mod for Minecraft that introduces a complete system of **Tiered Musical Instruments** and **Resonant Combat Weapons**, featuring a unique item-fusion mechanic.

![Harmonics Mod]

---

## Core Features

### Tiered Musical Instruments (45 Total)
* **9 Materials:** Wood, Stone, Iron, Gold, Diamond, Obsidian, Netherite, Water, and Lava.
* **5 Instrument Types:** Flute, Drum, Horn, Chimes, and Lute.
* **AoE Effects:** Right-click to trigger unique area-of-effect abilities (e.g., Regeneration aura, Fire Resistance, Slowness pulses, or Haste buffs).

### Resonant Weapons (16 Total)
* **4 Late-Game Tiers:** Obsidian, Lava, Netherite+, and Void.
* **Combat Types:** Swords, Axes, Shovels, and Pickaxes.
* **On-Hit & Active Abilities:** Includes Fire Aspect, Explosive Inferno, Soul Steal, Void Teleportation, and right-click AoE special attacks.

### Masterwork Fusion System
Craft **two identical base items** together in any crafting grid to create a **Masterwork** version:
* **Instruments:** Doubles effect range and potency with a glowing texture.
* **Weapons:** Deals **1.5x damage** and doubles special effect strength.

---

## Installation & Setup

1. Install **[Fabric Loader]** for Minecraft.
2. Download the latest **Harmonics** `.jar` from the **Releases** tab.
3. Place the file inside your `.minecraft/mods` folder.
4. Launch the game and find all items in the **Tools & Utilities** creative tab!

---

<details>
<summary>Developer & Building Information (Click to Expand)</summary>

### Technical Overview
* **Minecraft Version:** 26.2 (Fabric)
* **Mappings:** Official Mojang Mappings
* **Assets:** Generated via `generate_assets.py` (Requires `pip install pillow`)

### Asset Generation
To generate or regenerate item model JSONs, base textures, and language keys for all instruments and weapons:
```bash
python3 generate_assets.py
