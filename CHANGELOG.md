# Changelog

All notable changes to this project are documented here.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.1.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [1.6.1] - Hotfix

### Fixed
- **Fabric crash on startup** (`Block id not set` NPE in `ModBlocks.registerOre`
  on MC 26.2): `BlockBehaviour.Properties` now calls `setId(...)` before the
  block is constructed.
- **Harmonics 32x** pack format corrected from the data-pack value (`107`) to
  the resource-pack value (`88`), so the pack validates and applies on 26.2.

## [Unreleased]

### Added
- **Ruby material**: new gemstone material set with weapons, full armor set,
  and equipment layers:
  - Tools: `ruby_sword`, `ruby_axe`, `ruby_shovel`, `ruby_pickaxe`
    (each with a `_masterwork` variant).
  - Armor: `ruby_helmet`, `ruby_chestplate`, `ruby_leggings`, `ruby_boots`.
  - Material item: `ruby` (gem).

### Changed
- Ruby, armor, masterwork, equipment-layer, and gem textures now match the
  pixel shape of the existing material sets (recolored from shared reference
  shapes).
- Ruby, armor, and equipment-layer textures replicated for all materials
  (`ruby` gem shape based on the vanilla emerald texture).

### Fixed
- Ruby weapon/armor/masterwork item shapes now identical to the 9 existing
  material sets.
- Entity armor layers for `stone` and `wooden` no longer read as iron or
  cardboard: stone now has mottled gray depth, wooden now has oak plank grain.

## [1.7.0] - Scheduled

> Content for this release is already in the codebase and will be tagged on
> its release date.

### Added
- Ruby material (weapons, armor, equipment, gem) — see Unreleased.
- Optional **Harmonics 32x** resource pack (2x upscaled textures) to pair with
  *Faithful 32x*. Bundled in the Fabric jar (toggled from the resource-pack
  screen) and provided as a standalone `.zip` in `packs/` for any loader.

## [1.8.0] - Scheduled

> Placeholder entry. See `git diff`/commit history for the exact content
> slated for this release.

## [1.9.0] - Scheduled

> Placeholder entry. See `git diff`/commit history for the exact content
> slated for this release.

## [2.0.0] - Scheduled

> Placeholder entry. See `git diff`/commit history for the exact content
> slated for this release.