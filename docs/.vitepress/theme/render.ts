// Draws the mod's tinted icons in the browser, the way the game does: grayscale texture × tint colour.
// Results are cached as data URLs, so a table full of the same sapling decodes each texture once.
import type { Icon } from './resourcestrees'

const images = new Map<string, Promise<HTMLImageElement>>()
const icons = new Map<string, Promise<string>>()

function load(src: string): Promise<HTMLImageElement> {
  let image = images.get(src)
  if (!image) {
    image = new Promise((resolve, reject) => {
      const element = new Image()
      element.onload = () => resolve(element)
      element.onerror = reject
      element.src = src
    })
    images.set(src, image)
  }
  return image
}

function rgb(color: string): [number, number, number] {
  const value = parseInt(color.slice(1), 16)
  return [(value >> 16) & 255, (value >> 8) & 255, value & 255]
}

/** A copy of the texture with every pixel multiplied by the tint and the face shade. */
function tinted(image: HTMLImageElement, tint?: string, shade = 1): HTMLCanvasElement {
  const canvas = document.createElement('canvas')
  canvas.width = image.naturalWidth
  canvas.height = image.naturalHeight
  const context = canvas.getContext('2d')!
  context.drawImage(image, 0, 0)
  if (!tint && shade === 1) return canvas
  const [r, g, b] = tint ? rgb(tint) : [255, 255, 255]
  const pixels = context.getImageData(0, 0, canvas.width, canvas.height)
  const d = pixels.data
  for (let i = 0; i < d.length; i += 4) {
    d[i] = (d[i] * r * shade) / 255
    d[i + 1] = (d[i + 1] * g * shade) / 255
    d[i + 2] = (d[i + 2] * b * shade) / 255
  }
  context.putImageData(pixels, 0, 0)
  return canvas
}

async function drawLayers(layers: { src: string; tint?: string }[]): Promise<string> {
  const loaded = await Promise.all(layers.map((layer) => load(layer.src)))
  const canvas = document.createElement('canvas')
  canvas.width = loaded[0].naturalWidth
  canvas.height = loaded[0].naturalHeight
  const context = canvas.getContext('2d')!
  loaded.forEach((image, i) => context.drawImage(tinted(image, layers[i].tint), 0, 0))
  return canvas.toDataURL()
}

/** Size of the cube render; the slot scales it down. */
const CUBE = 128

/**
 * Top, left and right faces in the inventory's isometric view, as texture-to-canvas transforms
 * on a 64-unit grid, with the game's face shading (top 1.0, north/south 0.8, east/west 0.6).
 */
const FACES = [
  { origin: [3, 16], u: [29, -14.5], v: [29, 14.5], shade: 1 },
  { origin: [3, 16], u: [29, 14.5], v: [0, 32.5], shade: 0.8 },
  { origin: [32, 30.5], u: [29, -14.5], v: [0, 32.5], shade: 0.6 },
]

async function drawCube(src: string, tint: string): Promise<string> {
  const image = await load(src)
  const canvas = document.createElement('canvas')
  canvas.width = CUBE
  canvas.height = CUBE
  const context = canvas.getContext('2d')!
  context.imageSmoothingEnabled = false
  const unit = CUBE / 64
  for (const face of FACES) {
    const texture = tinted(image, tint, face.shade)
    const perTexel = unit / texture.width
    context.setTransform(
      face.u[0] * perTexel,
      face.u[1] * perTexel,
      face.v[0] * perTexel,
      face.v[1] * perTexel,
      face.origin[0] * unit,
      face.origin[1] * unit,
    )
    context.drawImage(texture, 0, 0)
  }
  return canvas.toDataURL()
}

/** Data URL for a tinted icon. `resolve` turns site paths into URLs under the site base. */
export function renderIcon(icon: Exclude<Icon, { kind: 'image' }>, resolve: (path: string) => string): Promise<string> {
  const cacheKey = JSON.stringify(icon)
  let result = icons.get(cacheKey)
  if (!result) {
    result =
      icon.kind === 'cube'
        ? drawCube(resolve(icon.src), icon.tint)
        : drawLayers(icon.layers.map((layer) => ({ ...layer, src: resolve(layer.src) })))
    icons.set(cacheKey, result)
  }
  return result
}
