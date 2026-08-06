# Changelog

All notable changes to this project are documented here.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.1.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [1.6.2] - Hotfix

### Added
- Ruby ore blocks (`ruby_ore`, `deepslate_ruby_ore`) now appear in the
  Harmonics creative tab.
- Missing block translations for ruby ores in `en_us` and `uk_ua`.

### Fixed
- Ruby ore textures were rendering as the missing-texture checkerboard in
  some setups; assets verified present and valid in the built jars.

## [1.6.1] - Hotfix

### Fixed
- **Fabric crash on startup** (`Block id not set` NPE in `ModBlocks.registerOre`
  on MC 26.2): `BlockBehaviour.Properties` now calls `setId(...)` before the
  block is constructed.
- **Harmonics 32x** pack format corrected from the data-pack value (`107`) to
  the resource-pack value (`88`), so the pack validates and applies on 26.2.

## [1.7.1] - Hotfix

### Changed
- **Ruby ore worldgen is now actually findable**: vein size 7 (was 3), 6 veins
  per chunk (was 4), concentrated between y=-16 and y=96 (mid-to-deep) instead
  of randomly spread over the full y=-16..320 range.

## [1.7.0] - HD Textures & Fixes

### Added
- **Real 32x textures**: the Harmonics 32x pack now anti-aliases diagonal
  contours (corner-AA pixel-art remaster) instead of naively pixel-doubling, so
  the textures read as a genuine HD upgrade rather than resized 16x art. The
  pack is now also registered as a builtin resource pack on **NeoForge**.
- **uk_ua** translations completed (now 152 entries, matching `en_us`, with all
  weapon, instrument, armor, ore, and advancement strings).

### Fixed
- **Ruby ore purple-black texture**: the two ore blocks were missing their
  modern (`assets/harmonics/items/*.json`) item-model definitions; added
  `ruby_ore` and `deepslate_ruby_ore`.
- **No NeoForge mod icon**: `neoforge.mods.toml` used unsupported `iconFile`;
  renamed to `logoFile` so the badge shows in the mod list.
- **Advancement titles not translating**: advancement lang keys lived under
  `data/harmonics/lang` (never loaded); moved into `assets/harmonics/lang`.
- **Wrong item count**: docs claimed 130/36/44; corrected everywhere to the
  real 141 items (40 weapons / 50 instruments / 48 armor / ruby + 2 ores).
- Removed orphaned `copper` textures and unused 1.25 MB `modlogo.png` files
  (base, 32x pack, and standalone pack).

## [Unreleased]

- Ruby material, worldgen, village loot previously slated — shipped in 1.6.0.
  See below.

## [1.8.0] - Scheduled

> Placeholder entry. See `git diff`/commit history for the exact content
> slated for this release.

## [1.9.0] - Scheduled

> Placeholder entry. See `git diff`/commit history for the exact content
> slated for this release.

## [2.0.0] - Scheduled

> Placeholder entry. See `git diff`/commit history for the exact content
> slated for this release.