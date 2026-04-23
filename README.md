# G1axFixer

[![Discord](https://img.shields.io/discord/XPYnrMd39C?color=7289da&label=Discord&logo=discord&logoColor=white)](https://discord.gg/XPYnrMd39C)
[![License](https://img.shields.io/badge/License-MIT-green.svg)](LICENSE)
[![Minecraft](https://img.shields.io/badge/Minecraft-1.21.11-brightgreen.svg)](https://fabricmc.net/)
[![Fabric API](https://img.shields.io/badge/Fabric%20API-Required-orange.svg)](https://modrinth.com/mod/fabric-api)

**Heavy-duty performance optimization & crash prevention mod for Minecraft 1.21.11**

G1axFixer eliminates crashes from resource packs and delivers aggressive real-time optimizations across networking, packet handling, entity ticking, rendering, and memory management.

## ⚡ What It Does

| Category | Optimizations |
|----------|--------------|
| **Network** | Packet deduplication, position packet throttling, entity spawn burst limiter, distant particle/explosion/world event culling |
| **Ticking** | 3-tier distance-based entity tick scaling, per-type throttling (items 1/4, armor stands 1/8, item frames 1/20), boss/player protection |
| **Rendering** | Entity render distance culling, block entity render culling, particle cap per tick, memory pressure-aware culling, chunk rebuild throttling |
| **Memory** | JVM pressure monitoring, automatic aggressive culling under low memory, light update deduplication |
| **Stability** | Atlas overflow protection, corrupt ZIP guard, missing sprite/sound/model handling, metadata auto-repair |

## 📦 Install

1. Install **Fabric Loader** 0.19.2+
2. Install **Fabric API**
3. Drop `g1axfixer-1.1.0.jar` in `.minecraft/mods/`

## 🔧 Build

```bash
git clone https://github.com/AkaTriggered/G1axFixer.git
cd G1axFixer
./gradlew clean build
```

## 📞 Links

- **Discord:** [discord.gg/XPYnrMd39C](https://discord.gg/XPYnrMd39C)
- **GitHub:** [AkaTriggered](https://github.com/AkaTriggered)
- **Modrinth:** [G1axFixer](https://modrinth.com/mod/g1axfixer)

---

**Developed by AkaTriggered & G1ax** | MIT License
