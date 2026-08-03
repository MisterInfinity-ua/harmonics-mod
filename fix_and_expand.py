#!/usr/bin/env python3
"""
Fix crystal/shadow weapon textures and expand the mod with new armor/weapon sets.

1. Regenerate crystal/shadow weapon textures with proper distinct colors
2. Generate missing armor assets for crystal, shadow, astral
3. Add Ender armor set (new)
4. Add Frost armor set (new)
5. Add Ender & Frost weapon sets (textures, models, items, recipes, lang)
6. Update language file with all new entries
"""
import json
import os
import shutil
from PIL import Image, ImageDraw

ROOT = os.path.dirname(os.path.abspath(__file__))
RES = os.path.join(ROOT, "src", "main", "resources")
TEX_ITEM = os.path.join(RES, "assets", "harmonics", "textures", "item")
TEX_HUMANOID = os.path.join(RES, "assets", "harmonics", "textures", "entity", "equipment", "humanoid")
TEX_LEGGINGS = os.path.join(RES, "assets", "harmonics", "textures", "entity", "equipment", "humanoid_leggings")
MODELS = os.path.join(RES, "assets", "harmonics", "models", "item")
ITEMS = os.path.join(RES, "assets", "harmonics", "items")
EQUIPMENT = os.path.join(RES, "assets", "harmonics", "equipment")
LANG_PATH = os.path.join(RES, "assets", "harmonics", "lang", "en_us.json")
RECIPE_ARMOR = os.path.join(RES, "data", "harmonics", "recipe", "armor")
RECIPE_WEAPON = os.path.join(RES, "data", "harmonics", "recipe", "weapon")


def ensure_dir(path):
    os.makedirs(os.path.dirname(path), exist_ok=True)


def write_json(path, data):
    ensure_dir(path)
    with open(path, "w") as f:
        json.dump(data, f, indent=2)


# ═══════════════════════════════════════════════════════════════════════════
# Weapon texture generation (from weapon_texgen.py)
# ═══════════════════════════════════════════════════════════════════════════
CANVAS = 16
_HANDLE = [(1, 14), (2, 13), (2, 14), (3, 12), (3, 13), (4, 11), (4, 12)]
_POMMEL = [(1, 13), (0, 14), (1, 15)]


def _a4(c):
    return c if len(c) == 4 else c + (255,)


def _darken(c, amt=40):
    c = _a4(c)
    return tuple(max(0, v - amt) for v in c[:3]) + (c[3],)


def _lighten(c, amt=20):
    c = _a4(c)
    return tuple(min(255, v + amt) for v in c[:3]) + (c[3],)


def _set(img, x, y, color):
    if 0 <= x < CANVAS and 0 <= y < CANVAS:
        img.putpixel((x, y), color)


def _outline(img):
    src = img.copy()
    for y in range(CANVAS):
        for x in range(CANVAS):
            if src.getpixel((x, y))[3] == 0:
                for dx, dy in ((-1, 0), (1, 0), (0, -1), (0, 1)):
                    nx, ny = x + dx, y + dy
                    if 0 <= nx < CANVAS and 0 <= ny < CANVAS and src.getpixel((nx, ny))[3] > 0:
                        img.putpixel((x, y), (10, 10, 12, 255))
                        break
    return img


def _draw_handle(img, wood=(90, 60, 35, 255)):
    wood = _a4(wood)
    for (x, y) in _HANDLE:
        _set(img, x, y, wood)
    for (x, y) in _POMMEL:
        _set(img, x, y, _darken(wood, 25))


def _draw_sword(img, blade, edge, guard):
    steps = [(11, 1), (12, 1), (10, 2), (11, 2), (9, 3), (10, 3), (8, 4), (9, 4),
             (7, 5), (8, 5), (6, 6), (7, 6), (5, 7), (6, 7)]
    for i, (x, y) in enumerate(steps):
        _set(img, x, y, edge if i < 2 else blade)
    for (x, y) in [(3, 8), (4, 8), (5, 8), (4, 9)]:
        _set(img, x, y, guard)
    _draw_handle(img)


def _draw_axe(img, blade, edge, guard):
    head = {
        (10, 1): edge, (11, 1): edge, (12, 1): blade,
        (9, 2): edge, (10, 2): blade, (11, 2): blade, (12, 2): blade,
        (9, 3): blade, (10, 3): blade, (11, 3): blade,
        (8, 4): blade, (9, 4): blade, (10, 4): blade,
        (7, 5): blade, (8, 5): blade,
        (6, 6): blade, (7, 6): blade,
    }
    for (x, y), c in head.items():
        _set(img, x, y, c)
    for (x, y) in [(5, 7), (4, 8)]:
        _set(img, x, y, guard)
    _draw_handle(img)


def _draw_pickaxe(img, blade, edge, guard):
    head = {
        (9, 1): edge, (10, 1): blade, (11, 1): blade, (12, 1): blade, (13, 1): edge,
        (10, 2): blade, (11, 2): blade, (12, 2): blade,
        (10, 3): blade, (11, 3): blade,
        (9, 4): blade, (10, 4): blade,
        (8, 5): blade, (9, 5): blade,
        (7, 6): blade, (8, 6): blade,
    }
    for (x, y), c in head.items():
        _set(img, x, y, c)
    for (x, y) in [(6, 7), (5, 8)]:
        _set(img, x, y, guard)
    _draw_handle(img)


def _draw_shovel(img, blade, edge, guard):
    head = {
        (11, 1): edge, (12, 1): edge,
        (10, 2): blade, (11, 2): blade, (12, 2): blade,
        (10, 3): blade, (11, 3): blade,
        (9, 4): blade, (10, 4): blade,
        (9, 5): blade,
        (8, 6): blade,
    }
    for (x, y), c in head.items():
        _set(img, x, y, c)
    for (x, y) in [(7, 7), (6, 8)]:
        _set(img, x, y, guard)
    _draw_handle(img)


_DRAWERS = {"sword": _draw_sword, "axe": _draw_axe, "pickaxe": _draw_pickaxe, "shovel": _draw_shovel}
_GRIP_COLORS = {(90, 60, 35, 255), _darken((90, 60, 35, 255), 25)}


def make_weapon_texture(wtype, blade, edge, guard=(180, 180, 190), masterwork=False, glow=None):
    img = Image.new("RGBA", (CANVAS, CANVAS), (0, 0, 0, 0))
    blade, edge, guard = _a4(blade), _a4(edge), _a4(guard)
    _DRAWERS[wtype](img, blade, edge, guard)

    if masterwork:
        for y in range(CANVAS):
            for x in range(CANVAS):
                p = img.getpixel((x, y))
                if p[3] > 0 and p[:3] not in {c[:3] for c in _GRIP_COLORS} and p[:3] != guard[:3]:
                    img.putpixel((x, y), _lighten(p))
        if glow:
            glow = _a4(glow)
            for (x, y) in [(12, 0), (13, 3), (6, 5)]:
                _set(img, x, y, glow)

    return _outline(img)


# ═══════════════════════════════════════════════════════════════════════════
# Armor item texture generation (16x16 inventory icons)
# ═══════════════════════════════════════════════════════════════════════════
def make_armor_item_texture(piece, primary, secondary, highlight):
    """Generate a 16x16 armor item icon matching the style of existing armor textures."""
    img = Image.new("RGBA", (16, 16), (0, 0, 0, 0))
    d = ImageDraw.Draw(img)
    prim4 = primary + (255,) if len(primary) == 3 else primary
    sec4 = secondary + (255,) if len(secondary) == 3 else secondary
    hi4 = highlight + (255,) if len(highlight) == 3 else highlight
    dark = tuple(max(0, v - 30) for v in prim4[:3]) + (255,)

    if piece == "helmet":
        # Helmet shape: rounded top, open bottom face
        d.rectangle([4, 2, 11, 3], fill=hi4)      # top ridge
        d.rectangle([3, 4, 12, 8], fill=prim4)     # main body
        d.rectangle([4, 9, 7, 9], fill=sec4)       # nose guard
        d.rectangle([5, 5, 7, 7], fill=sec4)       # face opening
        d.rectangle([3, 2, 12, 9], outline=dark)   # outline
    elif piece == "chestplate":
        # Chestplate: broad shoulders, torso
        d.rectangle([2, 2, 13, 3], fill=hi4)       # shoulder yoke
        d.rectangle([1, 4, 14, 10], fill=prim4)    # main body
        d.rectangle([4, 4, 6, 6], fill=sec4)       # left shoulder
        d.rectangle([9, 4, 11, 6], fill=sec4)      # right shoulder
        d.rectangle([6, 11, 9, 12], fill=sec4)     # bottom trim
        d.rectangle([1, 2, 14, 12], outline=dark)
    elif piece == "leggings":
        # Leggings: waistband + two legs
        d.rectangle([2, 2, 13, 3], fill=hi4)       # waistband
        d.rectangle([3, 4, 12, 7], fill=prim4)     # hip area
        d.rectangle([3, 8, 6, 12], fill=prim4)     # left leg
        d.rectangle([9, 8, 12, 12], fill=prim4)    # right leg
        d.rectangle([3, 8, 6, 12], outline=sec4)   # left leg trim
        d.rectangle([9, 8, 12, 12], outline=sec4)  # right leg trim
        d.rectangle([2, 2, 13, 12], outline=dark)
    elif piece == "boots":
        # Boots: two shoe shapes
        d.rectangle([1, 3, 6, 8], fill=prim4)      # left boot
        d.rectangle([1, 8, 7, 10], fill=sec4)      # left sole
        d.rectangle([9, 3, 14, 8], fill=prim4)     # right boot
        d.rectangle([8, 8, 14, 10], fill=sec4)     # right sole
        d.rectangle([1, 3, 6, 10], outline=dark)
        d.rectangle([9, 3, 14, 10], outline=dark)
        d.point((3, 4), fill=hi4)                   # left highlight
        d.point((11, 4), fill=hi4)                  # right highlight

    return img


# ═══════════════════════════════════════════════════════════════════════════
# Armor entity equipment texture generation (64x64 worn armor)
# ═══════════════════════════════════════════════════════════════════════════
def make_equipment_texture(primary, secondary, highlight, is_leggings=False):
    """Generate a 64x64 equipment texture for worn armor.
    For humanoid (helmet/chestplate/boots) or humanoid_leggings layer."""
    img = Image.new("RGBA", (64, 64), (0, 0, 0, 0))
    d = ImageDraw.Draw(img)
    prim4 = primary + (255,) if len(primary) == 3 else primary
    sec4 = secondary + (255,) if len(secondary) == 3 else secondary
    hi4 = highlight + (255,) if len(highlight) == 3 else highlight
    dark = tuple(max(0, v - 40) for v in prim4[:3]) + (255,)

    if is_leggings:
        # Leggings layout in MC 64x64 equipment texture:
        # Right leg: (0,32)-(31,63), Left leg: (32,32)-(63,63)
        # Waist belt: (16,48)-(48,51)
        # Right leg
        d.rectangle([4, 36, 27, 60], fill=prim4)
        d.rectangle([4, 36, 27, 60], outline=dark)
        d.rectangle([6, 38, 10, 42], fill=hi4)  # knee highlight
        # Left leg
        d.rectangle([36, 36, 59, 60], fill=prim4)
        d.rectangle([36, 36, 59, 60], outline=dark)
        d.rectangle([38, 38, 42, 42], fill=hi4)
        # Belt/waist area
        d.rectangle([16, 48, 47, 51], fill=sec4)
        d.rectangle([16, 48, 47, 51], outline=dark)
    else:
        # Humanoid layout in MC 64x64 equipment texture:
        # Right arm: (40,32)-(55,47), Left arm: (48,48)-(63,63) -- wait, MC format:
        # Actually MC 1.21+ equipment texture layout:
        # Head: (32,0)-(63,31) -- not used for body armor
        # Body: (16,16)-(39,31)  Right arm: (40,16)-(55,31)  Left arm: (16,32)-(31,47)
        # Right leg: (0,16)-(15,31)  Left leg: (0,32)-(15,47) -- for boots
        # But simpler approach: just fill the relevant areas

        # Body (chestplate core)
        d.rectangle([20, 18, 35, 30], fill=prim4)
        d.rectangle([20, 18, 35, 30], outline=dark)
        d.rectangle([24, 20, 31, 24], fill=sec4)  # chest detail

        # Right arm
        d.rectangle([42, 18, 53, 30], fill=prim4)
        d.rectangle([42, 18, 53, 30], outline=dark)

        # Left arm
        d.rectangle([18, 34, 29, 46], fill=prim4)
        d.rectangle([18, 34, 29, 46], outline=dark)

        # Shoulder highlights
        d.rectangle([20, 18, 23, 19], fill=hi4)
        d.rectangle([32, 18, 35, 19], fill=hi4)

    return img


# ═══════════════════════════════════════════════════════════════════════════
# Color definitions for all materials
# ═══════════════════════════════════════════════════════════════════════════

# Weapon material colors: (display_name, blade, edge, guard, glow)
WEAPON_COLORS = {
    "crystal": {
        "name": "Crystal",
        "blade": (102, 221, 255),   # bright cyan - PRISM color
        "edge": (180, 240, 255),    # light ice edge
        "guard": (80, 180, 210),    # teal guard
        "glow": (220, 255, 255),    # white-cyan sparkle
    },
    "shadow": {
        "name": "Shadow",
        "blade": (58, 36, 78),      # deep purple (NOT obsidian dark)
        "edge": (130, 80, 170),     # purple edge
        "guard": (70, 55, 90),      # dark violet guard
        "glow": (180, 120, 220),    # bright purple sparkle
    },
    "ender": {
        "name": "Ender",
        "blade": (100, 40, 180),    # ender purple
        "edge": (160, 100, 240),    # bright purple edge
        "guard": (60, 30, 100),     # dark purple guard
        "glow": (180, 140, 255),    # light purple sparkle
    },
    "frost": {
        "name": "Frost",
        "blade": (140, 200, 240),   # ice blue
        "edge": (200, 230, 255),    # frost white edge
        "guard": (100, 160, 200),   # steel blue guard
        "glow": (220, 245, 255),    # white frost sparkle
    },
}

# Armor material colors: (primary, secondary, highlight, crafting_ingredient)
ARMOR_COLORS = {
    "crystal": {
        "name": "Crystal",
        "primary": (102, 221, 255),
        "secondary": (60, 160, 200),
        "highlight": (180, 240, 255),
        "ingredient": "minecraft:amethyst_shard",
        "fire_resistant": True,
    },
    "shadow": {
        "name": "Shadow",
        "primary": (58, 36, 78),
        "secondary": (90, 60, 120),
        "highlight": (130, 80, 170),
        "ingredient": "minecraft:ender_pearl",
        "fire_resistant": False,
    },
    "astral": {
        "name": "Astral",
        "primary": (200, 180, 255),
        "secondary": (160, 140, 200),
        "highlight": (240, 220, 255),
        "ingredient": "minecraft:nether_star",
        "fire_resistant": True,
    },
    "ender": {
        "name": "Ender",
        "primary": (100, 40, 180),
        "secondary": (70, 20, 130),
        "highlight": (160, 100, 240),
        "ingredient": "minecraft:ender_pearl",
        "fire_resistant": False,
    },
    "frost": {
        "name": "Frost",
        "primary": (140, 200, 240),
        "secondary": (100, 160, 200),
        "highlight": (200, 230, 255),
        "ingredient": "minecraft:ice",
        "fire_resistant": False,
    },
}

WEAPON_TYPES = ["sword", "axe", "shovel", "pickaxe"]
ARMOR_PIECES = ["helmet", "chestplate", "leggings", "boots"]


# ═══════════════════════════════════════════════════════════════════════════
# MAIN
# ═══════════════════════════════════════════════════════════════════════════

def main():
    # Load existing lang file
    with open(LANG_PATH) as f:
        lang = json.load(f)

    # ─── STEP 1: Fix crystal/shadow weapon textures ────────────────────────
    print("=== Step 1: Fixing crystal & shadow weapon textures ===")
    for mat_id in ["crystal", "shadow"]:
        mc = WEAPON_COLORS[mat_id]
        for wtype in WEAPON_TYPES:
            item_id = f"{mat_id}_{wtype}"

            # Regenerate base texture
            base_path = os.path.join(TEX_ITEM, f"{item_id}.png")
            ensure_dir(base_path)
            make_weapon_texture(wtype, mc["blade"], mc["edge"], mc["guard"]).save(base_path)

            # Regenerate masterwork texture
            mw_path = os.path.join(TEX_ITEM, f"{item_id}_masterwork.png")
            make_weapon_texture(wtype, mc["blade"], mc["edge"], mc["guard"],
                                masterwork=True, glow=mc["glow"]).save(mw_path)

            print(f"  Regenerated: {item_id}.png + {item_id}_masterwork.png")

    # ─── STEP 2: Generate missing armor assets for crystal/shadow/astral ───
    print("\n=== Step 2: Generating missing armor assets (crystal, shadow, astral) ===")
    for mat_id in ["crystal", "shadow", "astral"]:
        mc = ARMOR_COLORS[mat_id]
        _generate_armor_set(mat_id, mc, lang)

    # ─── STEP 3: Add Ender armor set ───────────────────────────────────────
    print("\n=== Step 3: Adding Ender armor set ===")
    _generate_armor_set("ender", ARMOR_COLORS["ender"], lang)

    # ─── STEP 4: Add Frost armor set ───────────────────────────────────────
    print("\n=== Step 4: Adding Frost armor set ===")
    _generate_armor_set("frost", ARMOR_COLORS["frost"], lang)

    # ─── STEP 5: Add Ender & Frost weapon sets ─────────────────────────────
    print("\n=== Step 5: Adding Ender & Frost weapon asset sets ===")
    for mat_id in ["ender", "frost"]:
        mc = WEAPON_COLORS[mat_id]
        for wtype in WEAPON_TYPES:
            item_id = f"{mat_id}_{wtype}"

            # Item definition JSON (masterwork condition switch)
            write_json(
                os.path.join(ITEMS, f"{item_id}.json"),
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

            # Base model
            write_json(
                os.path.join(MODELS, f"{item_id}.json"),
                {"parent": "minecraft:item/handheld", "textures": {"layer0": f"harmonics:item/{item_id}"}},
            )

            # Masterwork model
            write_json(
                os.path.join(MODELS, f"{item_id}_masterwork.json"),
                {"parent": "minecraft:item/handheld", "textures": {"layer0": f"harmonics:item/{item_id}_masterwork"}},
            )

            # Base texture
            base_path = os.path.join(TEX_ITEM, f"{item_id}.png")
            ensure_dir(base_path)
            make_weapon_texture(wtype, mc["blade"], mc["edge"], mc["guard"]).save(base_path)

            # Masterwork texture
            mw_path = os.path.join(TEX_ITEM, f"{item_id}_masterwork.png")
            make_weapon_texture(wtype, mc["blade"], mc["edge"], mc["guard"],
                                masterwork=True, glow=mc["glow"]).save(mw_path)

            # Crafting recipe
            ingredient = "minecraft:ender_pearl" if mat_id == "ender" else "minecraft:ice"
            write_json(
                os.path.join(RECIPE_WEAPON, f"{item_id}.json"),
                {
                    "type": "minecraft:crafting_shaped",
                    "category": "equipment",
                    "pattern": ["MM", "MS", " S"] if wtype in ("sword", "axe") else [" M", " S", " S"],
                    "key": {
                        "M": {"item": ingredient},
                        "S": {"item": "minecraft:stick"},
                    },
                    "result": {"id": f"harmonics:{item_id}", "count": 1},
                },
            )

            # Lang entry
            lang[f"item.harmonics.{item_id}"] = f"{mc['name']} {wtype.capitalize()}"

            print(f"  Created full asset set for: {item_id}")

    # ─── STEP 6: Write updated language file ───────────────────────────────
    write_json(LANG_PATH, dict(sorted(lang.items())))
    print(f"\n=== Language file updated with {len(lang)} total entries ===")

    print("\n✅ All done! Run the Java updates next.")


def _generate_armor_set(mat_id, mc, lang):
    """Generate all assets for one armor set."""
    for piece in ARMOR_PIECES:
        item_id = f"{mat_id}_{piece}"

        # Item texture (16x16 inventory icon)
        tex_path = os.path.join(TEX_ITEM, f"{item_id}.png")
        ensure_dir(tex_path)
        make_armor_item_texture(piece, mc["primary"], mc["secondary"], mc["highlight"]).save(tex_path)

        # Item definition JSON (no masterwork for armor)
        write_json(
            os.path.join(ITEMS, f"{item_id}.json"),
            {
                "model": {
                    "type": "minecraft:model",
                    "model": f"harmonics:item/{item_id}"
                }
            },
        )

        # Item model JSON
        write_json(
            os.path.join(MODELS, f"{item_id}.json"),
            {"parent": "minecraft:item/generated", "textures": {"layer0": f"harmonics:item/{item_id}"}},
        )

        # Crafting recipe
        _write_armor_recipe(mat_id, piece, mc["ingredient"])

        # Lang entry
        lang[f"item.harmonics.{item_id}"] = f"{mc['name']} {piece.capitalize()}"

        print(f"  Created: {item_id} (texture, item def, model, recipe, lang)")

    # Equipment JSON (worn armor texture mapping)
    write_json(
        os.path.join(EQUIPMENT, f"{mat_id}.json"),
        {
            "layers": {
                "humanoid": [{"texture": f"harmonics:{mat_id}"}],
                "humanoid_leggings": [{"texture": f"harmonics:{mat_id}"}]
            }
        },
    )

    # Entity equipment textures (64x64 worn armor)
    humanoid_path = os.path.join(TEX_HUMANOID, f"{mat_id}.png")
    leggings_path = os.path.join(TEX_LEGGINGS, f"{mat_id}.png")
    ensure_dir(humanoid_path)
    ensure_dir(leggings_path)
    make_equipment_texture(mc["primary"], mc["secondary"], mc["highlight"], is_leggings=False).save(humanoid_path)
    make_equipment_texture(mc["primary"], mc["secondary"], mc["highlight"], is_leggings=True).save(leggings_path)

    print(f"  Created equipment textures for: {mat_id}")


def _write_armor_recipe(mat_id, piece, ingredient):
    """Write a shaped crafting recipe for an armor piece."""
    patterns = {
        "helmet":    ["MMM", "M M"],
        "chestplate": ["M M", "MMM", "MMM"],
        "leggings":  ["MMM", "M M", "M M"],
        "boots":     ["M M", "M M"],
    }
    write_json(
        os.path.join(RECIPE_ARMOR, f"{mat_id}_{piece}.json"),
        {
            "type": "minecraft:crafting_shaped",
            "category": "equipment",
            "pattern": patterns[piece],
            "key": {
                "M": {"item": ingredient},
            },
            "result": {"id": f"harmonics:{mat_id}_{piece}", "count": 1},
        },
    )


if __name__ == "__main__":
    main()
