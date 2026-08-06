#!/usr/bin/env python3
"""
Generate the "Harmonics 32x" companion resource pack.

Takes every texture under src/main/resources/assets/harmonics/textures and
produces a 2x nearest-neighbour (Faithful-style pixel-doubled) version into a
bundled resource pack. Because the upscale is a straight 2x, the pixel-shape
of every item/block/equipment texture is preserved exactly, matching how the
vanilla "Faithful 32x" pack doubles the base 16x art.

Outputs:
  - src/main/resources/resourcepacks/32x/   in-jar builtin pack (Fabric registers it)
  - packs/Harmonics32x/                     standalone pack folder (any loader)
  - packs/Harmonics32x.zip                  standalone pack archive

When enabled alongside "Faithful 32x" in the resource-pack screen, the mod's
own textures are upscaled to 32x to match -- and are otherwise untouched.

The pack.mcmeta uses the modern min_format/max_format schema used by 26.x.
"""
import json
import os
import shutil
import zipfile
from PIL import Image

ROOT = os.path.dirname(os.path.abspath(__file__))
SRC_TEX = os.path.join(ROOT, "src", "main", "resources", "assets", "harmonics", "textures")
JAR_PACK = os.path.join(ROOT, "src", "main", "resources", "resourcepacks", "32x")
STANDALONE = os.path.join(ROOT, "packs", "Harmonics32x")
ZIP_PATH = os.path.join(ROOT, "packs", "Harmonics32x.zip")

# Modern 1.9+/26.x range schema. 26.2 ships with resource pack format 88
# (data pack format is 107 -- resource packs must use the resource value).
MCMETA = {
    "pack": {
        "description": "Harmonics 32x - HD textures for the Faithful 32x setup",
        "min_format": [88, 0],
        "max_format": [88, 1],
    }
}


def upscale2x(img):
    """2x nearest-neighbour (Faithful-style pixel double) baseline."""
    return img.resize((img.width * 2, img.height * 2), Image.NEAREST)


def blend(a, b, w=0.5):
    """Weighted blend of two colours."""
    return (int(round(a[0] * w + b[0] * (1 - w))),
            int(round(a[1] * w + b[1] * (1 - w))),
            int(round(a[2] * w + b[2] * (1 - w))),
            255)


def enhanced_2x(img):
    """
    True 32x remaster rather than a straight pixel double.

    Starts from the exact 16x shape (nearest 2x2) then applies classic
    pixel-art corner anti-aliasing: wherever a right-angle step occurs in the
    32x grid, the corner pixel is blended with what sits diagonally across it.
    Diagonal contours (the repeated 45-degree steps in blades, armour edges,
    gem facets, etc.) get smooth intermediate pixels instead of chunky 4x4
    stairs, so the higher resolution is genuinely used -- the pack reads as an
    HD remaster, not a copy of the 16x art.
    """
    src = img.convert("RGBA")
    m = src.resize((src.width * 2, src.height * 2), Image.NEAREST)
    mw, mh = m.size
    mp = m.load()
    out = m.copy()
    op = out.load()

    def g(x, y):
        return mp[x, y]

    def is_corner(p, drift_orth_a, drift_orth_b, drift_diag):
        """p equals both orthogonal neighbours and differs diagonally = L step."""
        return (p[:3] == drift_orth_a[:3] and p[:3] == drift_orth_b[:3]
                and drift_diag[:3] != p[:3]
                and drift_diag[3] != 0 and drift_orth_a[3] != 0 and drift_orth_b[3] != 0)

    for y in range(1, mh - 1):
        for x in range(1, mw - 1):
            p = g(x, y)
            if p[3] == 0:
                continue
            # four diagonal neighbours
            nw = g(x - 1, y - 1)
            ne = g(x + 1, y - 1)
            sw = g(x - 1, y + 1)
            se = g(x + 1, y + 1)
            N = g(x, y - 1)
            S = g(x, y + 1)
            W = g(x - 1, y)
            E = g(x + 1, y)
            # four corner anti-aliasing cases (right-angle steps)
            if is_corner(p, E, S, sw):
                op[x, y] = blend(p, sw)
            elif is_corner(p, E, N, se):
                op[x, y] = blend(p, se)
            elif is_corner(p, W, S, nw):
                op[x, y] = blend(p, nw)
            elif is_corner(p, W, N, ne):
                op[x, y] = blend(p, ne)
    return out


def generate():
    for dest in (JAR_PACK, STANDALONE):
        if os.path.isdir(dest):
            shutil.rmtree(dest)
        os.makedirs(dest, exist_ok=True)

    n = 0
    for root, _dirs, files in os.walk(SRC_TEX):
        for name in files:
            if not name.lower().endswith(".png"):
                continue
            src = os.path.join(root, name)
            rel = os.path.relpath(src, SRC_TEX)
            with Image.open(src) as img:
                img = img.convert("RGBA")
                up = enhanced_2x(img)
                for dest in (JAR_PACK, STANDALONE):
                    out = os.path.join(dest, "assets", "harmonics", "textures", rel)
                    os.makedirs(os.path.dirname(out), exist_ok=True)
                    up.save(out)
            n += 1

    for dest in (JAR_PACK, STANDALONE):
        os.makedirs(dest, exist_ok=True)
        with open(os.path.join(dest, "pack.mcmeta"), "w") as f:
            json.dump(MCMETA, f, indent=2)

    if os.path.exists(ZIP_PATH):
        os.remove(ZIP_PATH)
    with zipfile.ZipFile(ZIP_PATH, "w", zipfile.ZIP_DEFLATED) as zf:
        for root, _dirs, files in os.walk(STANDALONE):
            for name in files:
                full = os.path.join(root, name)
                rel = os.path.relpath(full, STANDALONE)
                zf.write(full, rel)

    print(f"Generated {n} upscaled textures ->")
    print(f"  in-jar builtin pack: {JAR_PACK}")
    print(f"  standalone folder : {STANDALONE}")
    print(f"  standalone zip    : {ZIP_PATH}")


if __name__ == "__main__":
    generate()