import { useData, withBase } from 'vitepress'
// @ts-ignore
import current from '../data/data.json'
// @ts-ignore
import v2601 from '../snapshots/v2601.json'
// @ts-ignore
import old from '../snapshots/old.json'
import { createApi, type Api, type Dataset } from './resourcestrees'

/**
 * The documented versions. Each lives under its own folder; the current one is the site root.
 * A new entry is only needed when the mod's content changes, not for every Minecraft port.
 */
export const VERSIONS = [
  { key: 'current', label: 'Minecraft 26.3', path: '/', api: createApi(current as unknown as Dataset) },
  { key: 'v2601', label: 'Minecraft 26.1 – 26.2', path: '/v2601/', api: createApi(v2601 as unknown as Dataset) },
  { key: 'old', label: 'Older versions', path: '/old/', api: createApi(old as unknown as Dataset) },
] as const

export type Version = (typeof VERSIONS)[number]

// The most specific folder wins, so the root only matches pages outside the other folders.
const byDepth = [...VERSIONS].sort((a, b) => b.path.length - a.path.length)

export function versionOf(relativePath: string): Version {
  return byDepth.find((v) => `/${relativePath}`.startsWith(v.path)) ?? VERSIONS[0]
}

/** The version of the page being rendered, with its data helpers and a way to link within that version. */
export function useRt(): Api & { version: Version; link: (path: string) => string } {
  const { page } = useData()
  const version = versionOf(page.value.relativePath)
  return { ...version.api, version, link: (path: string) => withBase(`${version.path}${path.replace(/^\//, '')}`) }
}
