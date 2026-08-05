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
        "description": "Harmonics 32x - 2x textures for the Faithful 32x setup",
        "min_format": [88, 0],
        "max_format": [88, 1],
    }
}


def upscale2x(img):
    return img.resize((img.width * 2, img.height * 2), Image.NEAREST)


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
                up = upscale2x(img)
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