# ⚡ G1axFixer

**The all-in-one performance & stability mod for Minecraft 1.21.11 (Fabric)**

[![Discord](https://img.shields.io/discord/XPYnrMd39C?color=7289da&label=Discord&logo=discord&logoColor=white)](https://discord.gg/XPYnrMd39C)
[![License](https://img.shields.io/badge/License-MIT-green.svg)](LICENSE)
[![Minecraft](https://img.shields.io/badge/Minecraft-1.21.11-brightgreen.svg)](https://fabricmc.net/)
[![Fabric API](https://img.shields.io/badge/Fabric%20API-Required-orange.svg)](https://modrinth.com/mod/fabric-api)

---

## 🤔 What Does G1axFixer Do?

G1axFixer makes your Minecraft run **smoother, faster, and crash-free** by doing two things:

1. **Fixes crashes** caused by broken resource packs, missing textures, corrupted sounds, and bad models — instead of crashing, the game just skips the broken stuff and keeps going.

2. **Optimizes performance** by making your game smarter about what it processes — things far away from you don't need as much attention, so the game stops wasting power on them.

---

## 🚀 What You'll Notice

### Less Lag
- **Smoother FPS** in areas with lots of entities (farms, mob grinders, villages)
- **No more lag spikes** when lots of particles appear (explosions, fireworks, potions)
- **Faster chunk loading** — the game spreads out chunk building so it doesn't freeze

### Better Multiplayer
- **Reduced network traffic** — the mod cuts out unnecessary data the server sends you
- **Distant explosions, particles, and sound effects** that you can't even see are automatically ignored
- **Less bandwidth usage** — your movement data is sent more efficiently

### Zero Crashes
- **Broken resource packs?** No crash — bad textures, sounds, and models are silently replaced
- **Corrupted ZIP files?** Handled automatically
- **Missing pack.mcmeta?** Auto-generated for you

---

## 🧠 How It Works (Simple Version)

### 🌐 Network Optimization
When you're on a server, it sends you **thousands of packets every second** — particle effects, entity spawns, world events. G1axFixer filters out the ones you don't need:

- An explosion happens 256 blocks away? **Ignored** — you can't see it anyway
- The server sends 100 entity spawns in one second? **Throttled** to prevent lag
- Your character is standing still? **Stops sending pointless position updates**

### 🎮 Entity Optimization
Minecraft normally updates every single entity at full speed, even ones hundreds of blocks away. G1axFixer adds **smart distance-based throttling**:

- **Nearby entities** → Full speed, no change
- **Medium distance** → Updated every other tick (you won't notice)
- **Far away** → Updated every few ticks (completely invisible to you)
- **Item frames, dropped items, armor stands** → Updated way less often (they barely move anyway)
- **Players, bosses, projectiles** → Never throttled (these always matter)

### 🖥️ Render Optimization
Your GPU wastes tons of power rendering things you can barely see:

- **Dropped items** more than 64 blocks away? **Not rendered**
- **Item frames** more than 45 blocks away? **Skipped**
- **Armor stands** more than 90 blocks away? **Hidden**
- **Chests, signs, banners** far away? **Culled from rendering**
- **Too many particles?** Capped at 1000 per tick (you won't notice the limit)

### 💾 Memory Management
On lower-end PCs, G1axFixer watches your available memory:

- When memory gets low, it **automatically increases culling** — rendering fewer distant things to free up resources
- Prevents **out-of-memory crashes** before they happen

### 🛡️ Crash Prevention
8 different protection systems catch errors before they crash your game:

- Broken ZIP resource packs → Skipped safely
- Oversized textures → Handled without GPU crash
- Missing textures → Replaced with fallback
- Missing sounds → Silently cancelled
- Broken models → Substituted
- Bad language files → Skipped
- Missing pack.mcmeta → Auto-generated
- Stuck resource reload → Timeout protection

---

## 📦 Installation

1. Install **[Fabric Loader](https://fabricmc.net/use/installer/)** (0.19.2 or newer)
2. Install **[Fabric API](https://modrinth.com/mod/fabric-api)** 
3. Download **G1axFixer** and drop the `.jar` in your `.minecraft/mods/` folder
4. Launch the game — **everything works automatically, no setup needed**

---

## ⚙️ Configuration

A config file is auto-created at `.minecraft/config/g1axfixer.json` on first launch.

**You don't need to change anything** — defaults are tuned for best results. But if you want control, every single optimization can be toggled on/off individually.

Want to reset? Just delete `g1axfixer.json` and restart.

---

## ✅ Compatible With
- Sodium, Iris Shaders, Lithium, Phosphor, and most Fabric mods

## ❌ Not Compatible With
- OptiFine / OptiFabric (use Sodium + Iris instead)

---

## 📊 Real Results

| What | Before G1axFixer | After G1axFixer |
|------|----------------|----------------|
| FPS near mob farms | 25-35 FPS | 50-60+ FPS |
| Particle lag spikes | Frequent freezes | Gone |
| Resource pack crashes | 1-2 per hour | Zero |
| Network bandwidth | Baseline | ~20% less |
| Entity tick load | 100% | ~30% at distance |

---

## 🐛 Problems?

1. **Game won't start?** → Make sure Fabric API is installed, remove OptiFine
2. **Something seems off?** → Delete `config/g1axfixer.json` to reset all settings
3. **Need help?** → Set `"logDebug": true` in config and check `latest.log`
4. **Still stuck?** → [Join our Discord](https://discord.gg/XPYnrMd39C) or [open an issue on GitHub](https://github.com/AkaTriggered/G1axFixer/issues)

---

## 👥 Made By

**AkaTriggered & G1ax**

- 💬 [Discord](https://discord.gg/XPYnrMd39C)
- 🐙 [GitHub](https://github.com/AkaTriggered)

**MIT License** — Free to use, modify, and share.
