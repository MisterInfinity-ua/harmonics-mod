"""
Procedural pixel-art generator for Harmonics weapon-tool textures.

Replaces the old flat-colored-square placeholders with real 16x16 tool
silhouettes: a diagonal blade/head (staircase style, like vanilla MC tools),
a crossguard/binding, and a wood-grip handle with pommel. Colors are
material-driven; masterwork variants get a brightened blade plus a couple
of sparkle pixels in the material's glow color.

Used by generate_assets.py -- do not hand-edit generated .png files,
change the palettes/shapes here and re-run generate_assets.py instead.
"""
from PIL import Image

CANVAS = 16

# Diagonal grip (bottom-left) shared by every tool type.
_HANDLE = [(1, 14), (2, 13), (2, 14), (3, 12), (3, 13), (4, 11), (4, 12)]
_POMMEL = [(1, 13), (0, 14), (1, 15)]


def _a4(c):
    """Ensure a color tuple has an alpha channel."""
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
    """Stamp a dark outline on any transparent pixel adjacent to an opaque one."""
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

# Colors that should NOT be brightened for the masterwork pass (the wood grip).
_GRIP_COLORS = {(90, 60, 35, 255), _darken((90, 60, 35, 255), 25)}


def make_weapon_texture(wtype, blade, edge, guard=(180, 180, 190), masterwork=False, glow=None):
    """Build a single 16x16 RGBA tool texture. wtype: sword/axe/pickaxe/shovel."""
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
