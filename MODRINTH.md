# ⚡ G1axFixer

**Heavy-duty performance optimization & crash prevention for Minecraft 1.21.11**

G1axFixer eliminates crashes from resource packs and delivers aggressive real-time optimizations across networking, packet handling, entity ticking, rendering, chunk building, and memory management.

[![Discord](https://img.shields.io/discord/XPYnrMd39C?color=7289da&label=Discord&logo=discord&logoColor=white)](https://discord.gg/XPYnrMd39C)
[![GitHub](https://img.shields.io/badge/GitHub-AkaTriggered-181717?logo=github)](https://github.com/AkaTriggered)

---

## 🔥 What Makes G1axFixer Different

Most "optimization" mods touch one or two systems. G1axFixer runs **17 real-time mixins** across **6 game systems** simultaneously:

| System | Mixins | What Happens |
|--------|--------|-------------|
| **Network** | 3 | Packet deduplication, position throttle, spawn burst limiter |
| **Ticking** | 1 | 3-tier distance scaling, per-type throttling (items, armor stands, frames) |
| **Rendering** | 4 | Entity/block entity culling, particle cap, chunk rebuild throttle |
| **Memory** | 1 | JVM pressure monitoring, auto-aggressive culling mode |
| **Packets** | 1 | Distant particle/explosion/world event culling, XP/health dedup |
| **Stability** | 7 | Atlas, ZIP, sound, model, language, metadata, reload protection |

Every optimization is **configurable** and **safe** — wrapped in `try-catch` so a bug in the mod never crashes your game.

---

## ⚡ Network & Packet Optimization

The heaviest area. G1axFixer intercepts packets at multiple levels:

- **Packet Deduplication** — Duplicate particle, sound, and world event packets within 50ms are dropped
- **Position Packet Throttle** — Outbound movement packets skipped when you haven't moved significantly (saves bandwidth)
- **Entity Spawn Burst Limiter** — Caps entity spawns to 50/second, prevents lag spikes when entering loaded chunks
- **Distant Particle Culling** — Server particle packets beyond your cull distance are dropped before processing
- **Distant Explosion Culling** — Explosions >256 blocks away are silently ignored
- **World Event Culling** — Block break/place effects >180 blocks away are skipped
- **XP & Health Dedup** — Rapid XP/health update packets are deduplicated (100ms/50ms windows)

---

## 🎯 Entity Tick Optimization

Smart tick throttling that never breaks gameplay:

**Distance-Based (3 Tiers):**
- Near-mid range: Tick every **2nd** tick (50% reduction)
- Mid-far range: Tick every **4th** tick (75% reduction)
- Far range: Tick every **8th** tick (87.5% reduction)

**Per-Type Throttling:**
- Item entities: Tick every **4th** tick
- Armor stands (no passengers): Tick every **8th** tick
- Item frames: Tick every **20th** tick

**Never Throttled:** Players, Ender Dragon, Wither, named mobs, projectiles

---

## 🖥️ Render Optimization

Per-type entity render culling with smart distances:

| Entity Type | Cull Distance |
|------------|--------------|
| Item entities | 64 blocks |
| Item frames | 45 blocks |
| Glow item frames | 45 blocks |
| Armor stands | 90 blocks |
| XP orbs | 32 blocks |
| Block entities (chests, signs, etc.) | 64 blocks (configurable) |
| Generic entities | 128 blocks (configurable) |

Plus:
- **Particle cap**: 1000/tick max (configurable)
- **Chunk rebuild throttle**: 4 rebuilds/frame max (prevents lag from mass block changes)
- **Memory-aware culling**: When free memory drops below 15%, cull distances are halved automatically

---

## 🛡️ Crash Prevention

8 resource pack fixers that silently handle errors instead of crashing:

- **Corrupt ZIP Guard** — Handles broken .zip resource packs
- **Atlas Overflow Fix** — Prevents GPU texture limit crashes
- **Missing Sprite Handler** — Replaces missing textures with fallback
- **Sound Event Fixer** — Cancels playback of missing/null sounds
- **Model Load Protection** — Catches model bake/reload failures
- **Language File Sanitizer** — Handles malformed translation files
- **Metadata Auto-Repair** — Fixes missing pack.mcmeta
- **Reload Hang Protection** — Timeout for stuck resource reloads

---

## 📦 Installation

1. Install **Fabric Loader** (0.19.2+)
2. Install **Fabric API**
3. Drop `g1axfixer-1.1.0.jar` in `.minecraft/mods/`
4. Launch — everything works out of the box

---

## ⚙️ Configuration

Config file: `.minecraft/config/g1axfixer.json`

Every optimization can be toggled independently. Delete the file to regenerate defaults.

### Network
```json
{
  "optimizeNetwork": true,
  "deduplicatePackets": true,
  "packetDedupeWindowMs": 50,
  "throttlePositionPackets": true,
  "positionPacketMinIntervalMs": 25,
  "cullDistantParticles": true,
  "particleCullDistance": 64
}
```

### Ticking
```json
{
  "optimizeTicking": true,
  "optimizeEntityTick": true,
  "entityTickRange": 128,
  "throttleItemEntityTick": true,
  "itemEntityTickRate": 4,
  "throttleArmorStandTick": true
}
```

### Rendering
```json
{
  "optimizeRendering": true,
  "optimizeEntityRender": true,
  "skipHiddenEntityRender": true,
  "entityRenderDistanceSq": 16384,
  "reduceParticleCount": true,
  "maxParticlesPerTick": 1000,
  "optimizeBlockEntityRendering": true,
  "blockEntityRenderDistance": 64,
  "optimizeChunkRebuilds": true,
  "maxChunkRebuildsPerFrame": 4
}
```

### Memory
```json
{
  "optimizeMemory": true,
  "memoryPressureThreshold": 15.0,
  "batchLightUpdates": true
}
```

---

## 🔄 Compatibility

### ✅ Compatible With
- Fabric API, Sodium, Iris Shaders, Lithium, Phosphor
- Most Fabric mods

### ❌ Incompatible With
- OptiFabric / OptiFine (use Sodium + Iris instead)

---

## 📊 Performance Impact

**Typical results on mid-range hardware:**

| Metric | Before | After |
|--------|--------|-------|
| FPS in crowded areas | 30-40 | 55-60+ |
| Entity tick overhead | 100% | ~30% at distance |
| Particle lag spikes | Frequent | Eliminated |
| Network packet volume | Baseline | 15-25% reduced |
| Resource pack crashes | 1-2/hour | 0 |

---

## 🐛 Troubleshooting

- **Game won't launch:** Check Fabric API is installed. Remove OptiFine.
- **Config not working:** Delete `config/g1axfixer.json` to regenerate defaults.
- **Need more info:** Set `"logDebug": true` and check `latest.log` for `[G1axFixer]` entries.
- **Still having issues:** [Discord](https://discord.gg/XPYnrMd39C) or [GitHub Issues](https://github.com/AkaTriggered/G1axFixer/issues)

---

## 👥 Credits

**Developers:** AkaTriggered, G1ax
**Discord:** [Join our community](https://discord.gg/XPYnrMd39C)
**GitHub:** [AkaTriggered](https://github.com/AkaTriggered)

---

## 📜 License

MIT License — Free to use, modify, and distribute.

---

**Made with ⚡ for the Minecraft community**
