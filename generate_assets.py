#!/usr/bin/env python3
"""
Stamps out the repetitive assets for all 45 (material x type) instruments:
- assets/harmonics/items/<id>.json        (item model definition, masterwork switch)
- assets/harmonics/models/item/<id>.json  (base model)
- assets/harmonics/models/item/<id>_masterwork.json (glowing variant model)
- assets/harmonics/textures/item/<id>.png            (placeholder, tinted by material)
- assets/harmonics/textures/item/<id>_masterwork.png (placeholder, brighter/glow-tinted)
- assets/harmonics/lang/en_us.json (all display names)
- data/harmonics/recipe/<id>.json (base crafting recipe)

Run this once from the mod root: python3 generate_assets.py
Re-run any time you tweak materials/types; it overwrites generated files but
never touches hand-written ones (fusion.json, Java sources).

NOTE ON TEXTURES: these are flat placeholder squares so the mod is playable/testable
immediately. Swap them for real art in the same file paths whenever you're ready --
the masterwork variant should look like an enchanted/glowing version of the base.
"""
import json
import os
from PIL import Image, ImageDraw

ROOT = os.path.dirname(os.path.abspath(__file__))
RES = os.path.join(ROOT, "src", "main", "resources")

MATERIALS = {
    # id: (display_name, crafting_ingredient, base_color, glow_color)
    "wood":      ("Wood",      "minecraft:stick",          (155, 118, 83),  (200, 160, 120)),
    "stone":     ("Stone",     "minecraft:cobblestone",    (128, 128, 128), (180, 180, 190)),
    "iron":      ("Iron",      "minecraft:iron_ingot",     (216, 216, 216), (255, 255, 255)),
    "gold":      ("Gold",      "minecraft:gold_ingot",     (255, 215, 60),  (255, 240, 150)),
    "diamond":   ("Diamond",   "minecraft:diamond",        (80, 220, 220),  (170, 255, 255)),
    "obsidian":  ("Obsidian",  "minecraft:obsidian",       (35, 20, 55),    (110, 60, 180)),
    "netherite": ("Netherite", "minecraft:netherite_ingot",(70, 60, 60),    (200, 90, 60)),
    "water":     ("Water",     "minecraft:water_bucket",   (60, 120, 220),  (140, 200, 255)),
    "lava":      ("Lava",      "minecraft:lava_bucket",    (230, 100, 20),  (255, 200, 60)),
}

TYPES = {
    # id: display_name
    "flute":  "Flute",
    "drum":   "Drum",
    "horn":   "Horn",
    "chimes": "Chimes",
    "lute":   "Lute",
}

lang = {}

def ensure_dir(path):
    os.makedirs(os.path.dirname(path), exist_ok=True)

def write_json(path, data):
    ensure_dir(path)
    with open(path, "w") as f:
        json.dump(data, f, indent=2)

def make_texture(path, color, glow=False):
    ensure_dir(path)
    img = Image.new("RGBA", (16, 16), (0, 0, 0, 0))
    d = ImageDraw.Draw(img)
    d.rectangle([2, 2, 13, 13], fill=color + (255,))
    d.rectangle([2, 2, 13, 13], outline=(0, 0, 0, 255))
    if glow:
        d.rectangle([4, 4, 11, 11], outline=(255, 255, 255, 180))
    img.save(path)

for mat_id, (mat_name, ingredient, color, glow_color) in MATERIALS.items():
    for type_id, type_name in TYPES.items():
        item_id = f"{mat_id}_{type_id}"

        # Item model definition -- switches texture based on the masterwork component.
        write_json(
            os.path.join(RES, "assets", "harmonics", "items", f"{item_id}.json"),
            {
                "model": {
                    "type": "minecraft:condition",
                    "property": "minecraft:has_component",
                    "component": "harmonics:masterwork",
                    "on_true": {"type": "minecraft:model", "model": f"harmonics:item/{item_id}_masterwork"},
                    "on_false": {"type": "minecraft:model", "model": f"harmonics:item/{item_id}"},
                }
            },
        )

        # Base + masterwork models (simple generated-item-model, like vanilla tools).
        write_json(
            os.path.join(RES, "assets", "harmonics", "models", "item", f"{item_id}.json"),
            {"parent": "minecraft:item/generated", "textures": {"layer0": f"harmonics:item/{item_id}"}},
        )
        write_json(
            os.path.join(RES, "assets", "harmonics", "models", "item", f"{item_id}_masterwork.json"),
            {"parent": "minecraft:item/generated", "textures": {"layer0": f"harmonics:item/{item_id}_masterwork"}},
        )

        # Placeholder textures.
        make_texture(os.path.join(RES, "assets", "harmonics", "textures", "item", f"{item_id}.png"), color)
        make_texture(os.path.join(RES, "assets", "harmonics", "textures", "item", f"{item_id}_masterwork.png"), glow_color, glow=True)

        # Lang entries.
        lang[f"item.harmonics.{item_id}"] = f"{mat_name} {type_name}"

        # Base crafting recipe: 2 material + 1 stick, shaped like a short tool.
        # Buckets (water/lava) auto-return empty buckets via vanilla remainder handling.
        write_json(
            os.path.join(RES, "data", "harmonics", "recipe", f"{item_id}.json"),
            {
                "type": "minecraft:crafting_shaped",
                "category": "equipment",
                "pattern": ["M", "M", "S"],
                "key": {
                    "M": {"item": ingredient},
                    "S": {"item": "minecraft:stick"},
                },
                "result": {"id": f"harmonics:{item_id}", "count": 1},
            },
        )

write_json(os.path.join(RES, "assets", "harmonics", "lang", "en_us.json"), dict(sorted(lang.items())))

print(f"Generated {len(MATERIALS) * len(TYPES)} instruments "
      f"({len(MATERIALS)} materials x {len(TYPES)} types).")

# --- Weapons (WeaponMaterial x WeaponType) ---------------------------------
# NOTE ON TEXTURES: real pixel-art tool silhouettes (blade/head + guard + handle),
# not flat squares -- see weapon_texgen.py for the drawing code.
WEAPON_MATERIALS = {
    # id: (display_name, blade, edge, guard, glow)
    "obsidian":       ("Obsidian",   (35,20,55),  (150,70,210), (60,60,68),   (200,140,255)),
    "lava":           ("Lava",       (200,80,15), (255,200,60), (60,60,68),   (255,230,140)),
    "netherite_plus": ("Netherite+", (60,52,52),  (200,90,60),  (150,120,60), (255,150,120)),
    "void":           ("Void",       (22,10,35),  (170,70,255), (50,40,70),   (210,160,255)),
    "wither":         ("Wither",     (32,32,34),  (150,190,90), (210,205,190),(190,230,140)),
}
WEAPON_TYPES = {
    "sword": "Sword",
    "axe": "Axe",
    "shovel": "Shovel",
    "pickaxe": "Pickaxe",
}

from weapon_texgen import make_weapon_texture

weapon_lang = {}
for mat_id, (mat_name, blade, edge, guard, glow) in WEAPON_MATERIALS.items():
    for type_id, type_name in WEAPON_TYPES.items():
        item_id = f"{mat_id}_{type_id}"

        write_json(
            os.path.join(RES, "assets", "harmonics", "items", f"{item_id}.json"),
            {
                "model": {
                    "type": "minecraft:condition",
                    "property": "minecraft:has_component",
                    "component": "harmonics:masterwork",
                    "on_true": {"type": "minecraft:model", "model": f"harmonics:item/{item_id}_masterwork"},
                    "on_false": {"type": "minecraft:model", "model": f"harmonics:item/{item_id}"},
                }
            },
        )
        write_json(
            os.path.join(RES, "assets", "harmonics", "models", "item", f"{item_id}.json"),
            {"parent": "minecraft:item/handheld", "textures": {"layer0": f"harmonics:item/{item_id}"}},
        )
        write_json(
            os.path.join(RES, "assets", "harmonics", "models", "item", f"{item_id}_masterwork.json"),
            {"parent": "minecraft:item/handheld", "textures": {"layer0": f"harmonics:item/{item_id}_masterwork"}},
        )

        base_path = os.path.join(RES, "assets", "harmonics", "textures", "item", f"{item_id}.png")
        mw_path = os.path.join(RES, "assets", "harmonics", "textures", "item", f"{item_id}_masterwork.png")
        ensure_dir(base_path)
        make_weapon_texture(type_id, blade, edge, guard).save(base_path)
        make_weapon_texture(type_id, blade, edge, guard, masterwork=True, glow=glow).save(mw_path)

        weapon_lang[f"item.harmonics.{item_id}"] = f"{mat_name} {type_name}"

# Merge into the same lang file as the instruments.
lang_path = os.path.join(RES, "assets", "harmonics", "lang", "en_us.json")
with open(lang_path) as f:
    existing_lang = json.load(f)
existing_lang.update(weapon_lang)
write_json(lang_path, dict(sorted(existing_lang.items())))

print(f"Generated {len(WEAPON_MATERIALS) * len(WEAPON_TYPES)} weapons "
      f"({len(WEAPON_MATERIALS)} materials x {len(WEAPON_TYPES)} types). "
      f"(Crafting recipes for these were already hand-written in data/harmonics/recipe/weapon/.)")

