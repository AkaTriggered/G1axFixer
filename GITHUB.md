# G1axFixer

[![Discord](https://img.shields.io/discord/XPYnrMd39C?color=7289da&label=Discord&logo=discord&logoColor=white)](https://discord.gg/XPYnrMd39C)
[![License](https://img.shields.io/badge/License-MIT-green.svg)](LICENSE)
[![Minecraft](https://img.shields.io/badge/Minecraft-1.21.11-brightgreen.svg)](https://fabricmc.net/)
[![Fabric API](https://img.shields.io/badge/Fabric%20API-Required-orange.svg)](https://modrinth.com/mod/fabric-api)

**Heavy-duty performance optimization & crash prevention mod for Minecraft 1.21.11**

G1axFixer is a client-side Fabric mod that eliminates crashes from resource packs and delivers aggressive real-time optimizations across networking, packet handling, entity ticking, rendering, chunk building, and memory management — all configurable.

## 🏗️ Architecture

```
g1axfixer/
├── G1axFixer.java                    # Main entrypoint (ClientModInitializer)
├── config/
│   └── G1axConfig.java               # 40+ configurable options via Gson
├── error/
│   ├── ErrorCategory.java            # Error classification enum
│   ├── G1axErrorHandler.java         # Central error bus + deduplication
│   ├── G1axCrashReport.java          # Crash report enrichment
│   └── SafetyWrapper.java            # lambda-based exception wrapper
├── fix/
│   ├── ResourcePackFixer.java        # Coordinator for all 14 modules
│   ├── ZipGuard.java                 # Corrupt ZIP protection
│   ├── SpriteValidator.java          # Texture validation
│   ├── AtlasStitchFixer.java         # Atlas overflow prevention
│   ├── SoundEventFixer.java          # Sound error suppression
│   ├── ModelLoadFixer.java           # Model loading fallback
│   ├── LanguageFileFixer.java        # Translation file sanitizer
│   └── MetadataSanitizer.java        # pack.mcmeta auto-repair
├── optimization/
│   ├── NetworkOptimizer.java         # Network optimization flags
│   ├── RenderOptimizer.java          # Render optimization flags
│   ├── TickOptimizer.java            # Tick optimization flags
│   ├── ChunkRebuildThrottler.java    # Per-frame chunk rebuild cap
│   ├── MemoryOptimizer.java          # JVM pressure monitoring
│   ├── ItemRenderCache.java          # Item render decision cache
│   └── LightUpdateBatcher.java       # Light update deduplication
├── stats/
│   └── G1axStatsTracker.java         # Thread-safe atomic counters
├── compat/
│   ├── CompatibilityChecker.java     # Mod compatibility validation
│   └── EarlyCompatCheck.java         # Pre-launch compatibility
└── mixin/
    ├── ClientConnectionMixin.java    # Packet dedup + exception handling
    ├── ClientPlayNetworkMixin.java   # Spawn burst, particle, explosion, XP, health throttle
    ├── ClientPlayerMixin.java        # Position packet throttling
    ├── ClientWorldMixin.java         # 3-tier entity tick throttling
    ├── ParticleManagerMixin.java     # Particle cap + distance culling
    ├── EntityRenderDispatcherMixin.java # Per-type entity render culling
    ├── BlockEntityRenderMixin.java   # Block entity render distance
    ├── WorldRendererMixin.java       # Frame timing + per-frame flushes
    ├── GameRendererMixin.java        # Memory pressure polling
    ├── ChunkBuilderMixin.java        # Chunk rebuild throttling
    ├── ZipResourcePackMixin.java     # ZIP open protection
    ├── SpriteAtlasMixin.java         # Atlas upload protection
    ├── SoundManagerMixin.java        # Missing sound cancellation
    ├── ModelLoaderMixin.java         # Model reload protection
    ├── LanguageManagerMixin.java     # Language reload protection
    ├── ResourcePackProfileMixin.java # Pack profile tracking
    └── ReloadableResourceManagerMixin.java # Reload lifecycle hooks
```

## ⚡ Optimization Details

### Network & Packet (4 Mixins)

| Optimization | What It Does | Config Key |
|-------------|-------------|-----------|
| Packet Deduplication | Drops duplicate particle/sound/world event packets within 50ms window | `deduplicatePackets` |
| Position Packet Throttle | Skips outbound movement packets when player hasn't moved significantly | `throttlePositionPackets` |
| Entity Spawn Burst Limit | Caps entity spawns to 50/sec to prevent lag spikes | `optimizeNetwork` |
| Distant Particle Culling | Drops server particle packets beyond cull distance | `cullDistantParticles` |
| Distant Explosion Culling | Ignores explosion packets >256 blocks away | `optimizeNetwork` |
| World Event Culling | Skips world events >180 blocks away | `optimizeNetwork` |
| XP/Health Dedup | Deduplicates rapid XP/health update packets | `deduplicatePackets` |

### Entity Ticking (1 Mixin, 6 Strategies)

| Strategy | Behavior | Config |
|---------|----------|--------|
| Distance Tier 1 (near-mid) | Tick every 2nd tick | `entityTickRange` |
| Distance Tier 2 (mid-far) | Tick every 4th tick | `entityTickRange` |
| Distance Tier 3 (far) | Tick every 8th tick | `entityTickRange` |
| Item Entity | Tick every Nth tick | `itemEntityTickRate` |
| Armor Stand | Tick every 8th tick (no passengers) | `throttleArmorStandTick` |
| Item Frame | Tick every 20th tick | `optimizeEntityTick` |
| **Protected:** Players, Ender Dragon, Wither, named mobs, projectiles | Never throttled | — |

### Rendering (4 Mixins)

| Optimization | Distance | Config |
|-------------|----------|--------|
| Item entity render skip | >64 blocks | `skipHiddenEntityRender` |
| Item frame render skip | >45 blocks | `skipHiddenEntityRender` |
| Armor stand render skip | >90 blocks | `skipHiddenEntityRender` |
| XP orb render skip | >32 blocks | `skipHiddenEntityRender` |
| Generic entity render cap | Configurable | `entityRenderDistanceSq` |
| Block entity render cap | Configurable | `blockEntityRenderDistance` |
| Particle per-tick cap | 1000/tick default | `maxParticlesPerTick` |
| Particle distance cull | Configurable | `particleCullDistance` |
| Chunk rebuild throttle | 4/frame default | `maxChunkRebuildsPerFrame` |

### Memory & System (2 Mixins)

| Feature | Behavior | Config |
|---------|----------|--------|
| JVM Pressure Monitor | Polls free memory every 5s | `optimizeMemory` |
| Aggressive Mode | Halves cull distances when free memory <15% | `memoryPressureThreshold` |
| Light Update Dedup | Deduplicates light recalcs for same position per tick | `batchLightUpdates` |

## 🔧 Technical Details

| Component | Version |
|-----------|---------|
| Minecraft | 1.21.11 |
| Fabric Loader | 0.19.2+ |
| Fabric API | 0.141.3+ |
| MixinExtras | 0.5.4 (bundled) |
| Java | 21+ |
| Gradle | 9.3.1 |
| Fabric Loom | 1.13-SNAPSHOT |
| Yarn Mappings | 1.21.11+build.1 |

## 🚀 Build

```bash
git clone https://github.com/AkaTriggered/G1axFixer.git
cd G1axFixer
./gradlew clean build
```

Output: `build/libs/g1axfixer-1.1.0.jar`

## 🤝 Contributing

1. Fork → Branch → Code → Test → PR

## 📞 Support

- **Discord:** [Join Server](https://discord.gg/XPYnrMd39C)
- **Issues:** [GitHub Issues](https://github.com/AkaTriggered/G1axFixer/issues)

---

**Developed by AkaTriggered & G1ax** | MIT License
