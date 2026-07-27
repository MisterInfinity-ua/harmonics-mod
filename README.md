# Harmonics — tiered instrument mod for Minecraft 26.2 (Fabric)

**Update:** this project now uses **Mojang's official mappings**, not Yarn. Yarn
was discontinued by Fabric starting with 26.1 — Minecraft became unobfuscated
that release, and Fabric decided not to keep maintaining a third-party mapping
set for unobfuscated code. If you're used to Yarn names (`Identifier.of`,
`PlayerEntity`, `ItemGroupEvents`, etc.), the big renames to know are:

| Yarn (old) | Mojang mappings (now) |
|---|---|
| `PlayerEntity` | `Player` |
| `World` | `Level` |
| `Hand` | `InteractionHand` |
| `Box` | `AABB` |
| `HostileEntity` | `Monster` |
| `StatusEffectInstance` / `StatusEffects` | `MobEffectInstance` / `MobEffects` |
| `SoundCategory` | `SoundSource` |
| `Item.Settings` | `Item.Properties` |
| `RegistryKey` | `ResourceKey` |
| `ItemGroupEvents` | `CreativeModeTabEvents` (new package: `creativetab.v1`) |
| `SpecialCraftingRecipe` | `CustomRecipe` |
| `SpecialRecipeSerializer` | `SimpleCraftingRecipeSerializer` |
| `RegistryWrapper.WrapperLookup` | `HolderLookup.Provider` |

`Identifier` is still called `Identifier` (that one didn't change), just
constructed with `Identifier.fromNamespaceAndPath(...)` instead of `.of(...)`.

Also: since Minecraft ships unobfuscated from 26.1 on, there's no remapping
step anymore — `build.gradle` uses plain `implementation` instead of
`modImplementation`, and there's no `mappings` block at all.

45 instruments: 9 materials (wood, stone, iron, gold, diamond, obsidian, netherite,
water, lava) x 5 types (flute, drum, horn, chimes, lute). Plus a fusion system:
craft 2 of the same base instrument together anywhere in a crafting grid to get
a "masterwork" version with double range/effect and a glowing texture.

## Before you build — things to double-check

1. Go to https://fabricmc.net/develop and select Minecraft **26.2**.
   Confirm `loader_version` and `fabric_api_version` in `gradle.properties`
   match what the site shows — I used `0.155.2+26.2`, the latest I could
   confirm, but check for anything newer.
2. **Highest-risk spot #1:** the `minecraft:condition` / `minecraft:has_component`
   schema in the generated `assets/harmonics/items/*.json` files — this item
   model predicate format has shifted release to release, so if items don't
   render/switch texture correctly, check the current schema on the Fabric
   docs' "Item Models" page.
3. **Highest-risk spot #2:** `SimpleCraftingRecipeSerializer` / `CustomRecipe`
   in `FusionRecipe.java` — this pattern (special, non-data-driven recipes)
   has existed in Mojang mappings for a long time and I'm fairly confident in
   it, but the docs mention data-driven recipe serializers were simplified to
   plain MapCodec/StreamCodec pairs in 26.1, so if this doesn't compile,
   check whether `CustomRecipe`'s special-recipe pattern changed too.

## What's implemented (Java, hand-written)

- `InstrumentMaterial` / `InstrumentType` — the two enums that define every
  tier's stats (range, durability, volume, elemental flag) and every
  instrument shape's behavior modifier. No Minecraft API references here, so
  nothing about these changed with the mappings switch.
- `InstrumentItem` — right-click to play: plays a sound, applies an area
  effect (water = regen aura, lava = fire resistance aura, obsidian horn =
  slowness pulse on nearby hostiles, netherite = haste buff — a "bard" effect),
  and damages the item like a tool.
- `HarmonicsComponents` — the custom `masterwork` data component used to flag
  fused instruments.
- `FusionRecipe` + `ModRecipes` — a special (code-driven) crafting recipe:
  any 2 identical, non-masterwork instruments anywhere in the grid → 1
  masterwork instrument. Works for all 45 items without 45 separate JSON
  recipes.
- `Harmonics` / `HarmonicsClient` — mod entrypoints; client one adds all 45
  items to the vanilla Tools & Utilities creative tab via `CreativeModeTabEvents`.

## What's generated (`generate_assets.py`)

Run once with `python3 generate_assets.py` (needs Pillow: `pip install
pillow`). It stamps out, for all 45 items:

- `assets/harmonics/items/<id>.json` — item model definition that switches
  texture based on whether the `masterwork` component is present.
- Base + masterwork models pointing at their textures.
- **Placeholder textures** (flat tinted squares) so the mod is playable
  immediately — swap the PNGs in `textures/item/` for real art whenever you
  want, same filenames.
- `lang/en_us.json` with all 90 display names (base + you'll want to add
  masterwork name overrides if you want "Masterwork Iron Flute" style names —
  currently the fused item shows the same name as the base one).
- Base crafting recipes: 2 material-item + 1 stick, shaped like a short tool.
  Water/lava use their buckets as the "material" ingredient — buckets return
  empty automatically via vanilla's recipe remainder system, so you don't
  lose them.

## Weapons system

Added on top of the instrument system: 4 materials (obsidian, lava, netherite+,
void) x 4 types (sword, axe, shovel, pickaxe) = 16 weapons, each with a unique
`SpecialEffect` on hit (fire aspect, inferno/explosion, soul steal, void
teleport-and-darkness), plus a sword right-click AoE special attack with a
cooldown. Masterwork (fused) weapons hit 1.5x harder and get 2x effect
strength, same fusion mechanic as the instruments.

**Bug that was fixed here:** the original `ModWeapons.register()` built an
`Item.Properties` and never called `.setId(...)` on it before constructing
the item — Minecraft 26.x requires the id to be set on `Properties` *before*
it reaches the `Item` constructor, since the constructor uses it to build the
item's default data components. Skipping that call crashes at construction
time. Fixed by mirroring `ModItems.register()`'s order: build the
`ResourceKey<Item>` first, call `.setId(key)` on the `Properties`, construct
the item, then register with that same key.

Weapon crafting recipes (2 material + stick, sword/axe/tool-shaped) already
existed as hand-written JSON in `data/harmonics/recipe/weapon/` and are
carried over unchanged. `generate_assets.py` now also stamps out item
definitions, models (using vanilla's `minecraft:item/handheld` parent, so
they render in-hand like real tools), and placeholder textures for all 16
weapons — these didn't exist before, so weapons would have shown as missing
textures even once the crash was fixed.

## Not yet done / ideas for next pass

- Sounds are currently reusing vanilla note block sounds. A custom sound
  per instrument type (via `sounds.json`) would sell it a lot better.
- No advancement/recipe-book unlock conditions yet (recipes are craftable
  from the start).
- Masterwork item display name currently doesn't change — could add a
  component-based name override so it visibly reads "Masterwork X" in
  the tooltip.
- The obsidian/netherite/elemental special effects are just a first pass —
  happy to tune numbers or add more tiers' unique effects once you've
  played with it in-game.

