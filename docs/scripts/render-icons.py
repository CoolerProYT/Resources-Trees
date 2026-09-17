#!/usr/bin/env python3
"""
Renders the wiki's item icons for every documented version and uploads them to the texture bucket,
following the mod-texture-uploader convention:

  gs://coolerpromc/textures/resourcestrees/<name>.png   1024x1024, nearest-neighbour

  <type>_<tree>_sapling   stem texture with the tinted leaf layer on top
  <type>_<tree>_leaves    isometric block, like the vanilla leaves in the bucket
  <type>_leaf_fragment    tinted fragment texture
  <essence>               tinted essence texture
  tree_simulator          committed 1024px render in scripts/assets/

Colours and names come from the synced data and the version snapshots, so run `npm run sync` first.

Usage (from docs/):
  python scripts/render-icons.py                 # render into .vitepress/icons-out/ only
  python scripts/render-icons.py --upload        # render, then upload files not yet in the bucket

Requires Pillow and, for --upload, the gcloud CLI.
"""
import argparse
import json
import subprocess
import sys
from pathlib import Path

try:
    from PIL import Image
except ImportError:
    sys.exit("Missing dependency: run `pip install pillow` first.")

DOCS = Path(__file__).resolve().parent.parent
TEXTURES = DOCS.parent / "common/src/main/resources/assets/resourcestrees/textures"
OUT = DOCS / ".vitepress/icons-out"
BUCKET = "gs://coolerpromc/textures/resourcestrees"
SIZE = 1024


def rgb(color: str):
    return tuple(int(color[i:i + 2], 16) for i in (1, 3, 5))


def tint(image: Image.Image, color: str, shade: float = 1.0) -> Image.Image:
    """Multiplies every pixel by the colour and a face shade, the way the game tints grayscale textures."""
    r, g, b, a = image.convert("RGBA").split()
    tr, tg, tb = (c * shade for c in rgb(color))
    return Image.merge("RGBA", (r.point(lambda v: int(v * tr / 255)), g.point(lambda v: int(v * tg / 255)),
                                b.point(lambda v: int(v * tb / 255)), a))


def flat(*layers: Image.Image) -> Image.Image:
    """Stacks 16x16 layers and scales them up with nearest-neighbour."""
    canvas = Image.new("RGBA", layers[0].size)
    for layer in layers:
        canvas = Image.alpha_composite(canvas, layer)
    return canvas.resize((SIZE, SIZE), Image.NEAREST)


# Inventory view of a block, measured from the vanilla renders in the bucket.
TOP = (512, 10)
LEFT = (60, 236)
RIGHT = (964, 236)
CENTER = (512, 462)
HEIGHT = 555


def face(texture: Image.Image, origin, u_end, v_end) -> Image.Image:
    """Draws the texture on the parallelogram origin → u_end (texture x) and origin → v_end (texture y)."""
    w, h = texture.size
    ux, uy = (u_end[0] - origin[0]) / w, (u_end[1] - origin[1]) / w
    vx, vy = (v_end[0] - origin[0]) / h, (v_end[1] - origin[1]) / h
    det = ux * vy - vx * uy
    # PIL maps each output pixel back to the source, so this is the inverse transform.
    a, b = vy / det, -vx / det
    d, e = -uy / det, ux / det
    c = -(a * origin[0] + b * origin[1])
    f = -(d * origin[0] + e * origin[1])
    return texture.transform((SIZE, SIZE), Image.AFFINE, (a, b, c, d, e, f), Image.NEAREST)


def cube(texture: Image.Image, color: str) -> Image.Image:
    """Isometric block. The far faces go first so they show through the gaps in leaves, as in vanilla."""
    down = lambda p: (p[0], p[1] + HEIGHT)
    far = [
        (tint(texture, color, 0.5), down(LEFT), down(TOP), down(CENTER)),  # bottom
        (tint(texture, color, 0.6), LEFT, TOP, down(LEFT)),  # back left
        (tint(texture, color, 0.8), TOP, RIGHT, down(TOP)),  # back right
    ]
    near = [
        (tint(texture, color, 1.0), LEFT, TOP, CENTER),  # top
        (tint(texture, color, 0.8), LEFT, CENTER, down(LEFT)),  # front left
        (tint(texture, color, 0.6), CENTER, RIGHT, down(CENTER)),  # front right
    ]
    canvas = Image.new("RGBA", (SIZE, SIZE))
    for image, origin, u_end, v_end in far + near:
        canvas = Image.alpha_composite(canvas, face(image, origin, u_end, v_end))
    return canvas


def load_datasets():
    paths = [DOCS / ".vitepress/data/data.json", *sorted((DOCS / ".vitepress/snapshots").glob("*.json"))]
    return [json.loads(p.read_text(encoding="utf8")) for p in paths]


def main():
    ap = argparse.ArgumentParser(description=__doc__, formatter_class=argparse.RawDescriptionHelpFormatter)
    ap.add_argument("--upload", action="store_true", help="upload rendered icons that aren't in the bucket yet")
    args = ap.parse_args()

    types, trees, essences = {}, set(), {}
    for data in load_datasets():
        for t in data["resourceTypes"]:
            if types.setdefault(t["name"], t["color"]) != t["color"]:
                sys.exit(f"{t['name']} has different colours between versions; icons would be wrong for one of them.")
        trees.update(tree["name"] for tree in data["treeTypes"])
        essences.update(data["essences"])

    OUT.mkdir(parents=True, exist_ok=True)
    block = lambda name: Image.open(TEXTURES / "block" / f"{name}.png").convert("RGBA")
    item = lambda name: Image.open(TEXTURES / "item" / f"{name}.png").convert("RGBA")
    count = 0
    for tree in sorted(trees):
        stem, layer, leaves = block(f"resources_{tree}_sapling"), block(f"resources_{tree}_sapling_layer1"), block(f"resources_{tree}_leaves")
        for name, color in types.items():
            flat(stem, tint(layer, color)).save(OUT / f"{name}_{tree}_sapling.png")
            cube(leaves, color).save(OUT / f"{name}_{tree}_leaves.png")
            count += 2
    for name, color in types.items():
        flat(tint(item("leaf_fragment"), color)).save(OUT / f"{name}_leaf_fragment.png")
        count += 1
    for item_id, color in essences.items():
        flat(tint(item("essence"), color)).save(OUT / f"{item_id.split(':')[1]}.png")
        count += 1
    Image.open(DOCS / "scripts/assets/tree_simulator.png").convert("RGBA").save(OUT / "tree_simulator.png")
    count += 1
    print(f"Rendered {count} icons into {OUT}")

    if args.upload:
        # One call for the whole folder; --no-clobber skips icons already in the bucket.
        gcloud = "gcloud.cmd" if sys.platform == "win32" else "gcloud"
        subprocess.run([gcloud, "storage", "cp", "--no-clobber", str(OUT / "*.png"), f"{BUCKET}/"], check=True)


if __name__ == "__main__":
    main()
