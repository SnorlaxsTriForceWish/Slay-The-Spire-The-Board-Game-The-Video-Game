# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

This is a Slay the Spire mod implementing "Slay the Spire: The Board Game: The Video Game" - a cooperative multiplayer adaptation of the board game version back into video game format. The mod transforms the single-player roguelike into a 2-4 player cooperative experience with simultaneous turn-based combat, row-based enemy targeting, and shared die roll mechanics.

**Mod ID**: `sts_tbg_tvg`
**Author**: degraffa
**Base Game**: Slay the Spire
**Dependencies**: ModTheSpire, BaseMod, StSLib, TogetherInSpire

## Build System

### Maven Commands

**Build and package the mod:**
```bash
mvn clean package
```

This command:
- Validates JSON files in `src/main/resources`
- Compiles Java sources (Java 1.8 target)
- Packages JAR file to `target/sts_tbg_tvg.jar`
- Automatically copies JAR to Steam mods folder

**Clean build artifacts:**
```bash
mvn clean
```

### Build Configuration

The build process uses Maven profiles for different operating systems. The active profile determines the Steam installation path:
- **Windows (default)**: `D:\SteamLibrary\steamapps`
- **Linux**: `~/.local/share/Steam/steamapps`
- **Mac**: `~/Library/Application Support/Steam/steamapps`

Update the `steam.windows` property in `pom.xml` if your Steam installation is elsewhere.

### JAR Dependencies

The mod depends on JAR files from Steam Workshop and the game installation:
- **Desktop-1.0.jar**: Base game (located in Steam/common/SlayTheSpire/)
- **ModTheSpire.jar**: Workshop ID 1605060445
- **BaseMod.jar**: Workshop ID 1605833019
- **StSLib.jar**: Workshop ID 1609158507
- **TogetherInSpire.jar**: Workshop ID 2384072973

These are referenced as system-scoped dependencies in `pom.xml`.

## Project Structure

### Source Organization

```
src/main/java/sts_tbg_tvg/
├── StsTbgTvgMod.java       # Main mod initializer
├── cards/
│   └── BaseCard.java        # Abstract base for custom cards
├── potions/
│   └── BasePotion.java      # Abstract base for custom potions
├── powers/
│   └── BasePower.java       # Abstract base for custom powers
├── relics/
│   └── BaseRelic.java       # Abstract base for custom relics
└── util/
    ├── CardStats.java       # Card data structure
    ├── GeneralUtils.java    # Utility functions
    ├── KeywordInfo.java     # Keyword definition structure
    ├── Sounds.java          # Audio resource paths
    ├── TextureLoader.java   # Image loading utility
    └── TriFunction.java     # Functional interface

src/main/resources/sts_tbg_tvg/
├── localization/eng/        # English localization JSON files
├── images/                  # Texture assets
│   ├── cards/              # Card art (attack/skill/power)
│   ├── powers/             # Power icons (32x32 and large)
│   └── relics/             # Relic images
└── audio/                  # Sound effects
```

### Key Classes

**StsTbgTvgMod** (`StsTbgTvgMod.java:35-274`)
- Main entry point marked with `@SpireInitializer`
- Implements BaseMod subscriber interfaces for localization, keywords, and audio
- Handles resource loading from `resources/sts_tbg_tvg/` folder
- Auto-detects mod ID from ModTheSpire metadata

**Base Classes** (in cards/, potions/, powers/, relics/)
- Extend Slay the Spire's abstract classes
- Provide simplified constructors for common mod patterns
- Standardize ID prefixing with `makeID()` helper

## Modding Framework Integration

### BaseMod Subscriber Pattern

The mod uses BaseMod's subscriber system to hook into game initialization:

```java
@SpireInitializer
public class StsTbgTvgMod implements
    EditStringsSubscriber,      // Custom text/localization
    EditKeywordsSubscriber,     // Custom keywords
    AddAudioSubscriber,         // Custom sounds
    PostInitializeSubscriber {  // Post-init setup
```

Each interface provides a callback method called at specific initialization phases.

### Resource Loading

All resources are loaded from `src/main/resources/sts_tbg_tvg/`:
- **Localization**: JSON files in `localization/<lang>/`
- **Images**: PNG files in `images/`
- **Audio**: OGG/WAV/MP3 files in `audio/`

The mod ID (`sts_tbg_tvg`) must match the resources folder name. This is enforced in `checkResourcesPath()` (`StsTbgTvgMod.java:229-253`).

### ID Prefixing

All mod content IDs should be prefixed using `makeID()` (`StsTbgTvgMod.java:48-50`):
```java
String cardID = StsTbgTvgMod.makeID("Strike");
// Returns: "sts_tbg_tvg:Strike"
```

This prevents ID conflicts with other mods.

## Reference Dependencies

### ModCopies Directory

The `ModCopies/` directory contains decompiled JAR sources for reference only. **Do not modify these files.** They include:

- **BaseMod**: Console commands and mod hooks framework
- **ModTheSpire**: Core modding loader infrastructure
- **SlayTheSpireSource**: Full base game source code
- **StSLib**: Shared library providing keywords, mechanics, custom actions
- **TogetherInSpire**: P2P multiplayer networking implementation

### TogetherInSpire Architecture

The multiplayer implementation uses:
- **Event-driven state replication**: Full game state synchronized across clients
- **Dual network backends**: P2P (Steam) and PF (TCP sockets)
- **P2PManager**: Central registry of connected players and game state
- **NetworkMessage protocol**: Serialized message passing for state updates
- **P2PCallbacks**: 45+ event handlers for state synchronization

Key architectural details are documented in `.llm/stsogether_architecture_analysis.md`.

## Board Game Adaptation Design

The mod implements mechanics from the physical board game that differ significantly from the video game:

### Core Differences
- **Simultaneous turn-based combat** (not asynchronous like original)
- **Row-based enemy targeting** (4 rows, one per player)
- **Shared die roll system** (single d6 roll affects all players/enemies)
- **Token caps** (Strength max 8, Block max 20, Poison max 30 global)
- **Energy resets to 3 each turn** (not progressive)
- **Block resets at start of turn** (not end)

### Multiplayer Features to Implement
1. Multi-character control system (solo mode with 1-4 characters)
2. Hotseat mode (asymmetric player/character distribution)
3. Disconnect recovery (control transfer between players)
4. Potion trading between players
5. Gold pooling at merchants
6. Shared map progression (party moves together)

Full design specifications are in `.llm/board_game_rule_context.md`.

## Development Guidelines

### Adding New Content

**Cards**: Extend `BaseCard` and place in `cards/` package
- Define card stats using `CardStats` constructor
- Implement `use()` method for card effects
- Add localization in `localization/eng/CardStrings.json`
- Add card art to `images/cards/<type>/`

**Relics**: Extend `BaseRelic` and place in `relics/` package
- Implement relic trigger methods (e.g., `atBattleStart()`)
- Add localization in `RelicStrings.json`
- Add images: small (32x32) and large versions

**Powers**: Extend `BasePower` and place in `powers/` package
- Implement power effects (e.g., `atEndOfTurn()`)
- Add localization in `PowerStrings.json`
- Add power icon (32x32) to `images/powers/`

### Testing the Mod

1. Build with `mvn clean package`
2. JAR is auto-copied to Steam mods folder
3. Launch Slay the Spire via ModTheSpire
4. Check console/logs for errors (Log4j logger available via `StsTbgTvgMod.logger`)

### Common Patterns

**Accessing game state:**
```java
AbstractDungeon.player           // Current player
AbstractDungeon.getCurrRoom()    // Current room
AbstractDungeon.actionManager    // Action queue
```

**Using SpirePatch for hooks:**
ModTheSpire uses Javassist for bytecode patching. Patches go in separate classes with `@SpirePatch` annotation (see StSLib for examples).

**Networking with TogetherInSpire:**
Use `P2PMessageSender.Send_*()` methods to broadcast state changes and `P2PCallbacks.On*()` to handle incoming events.

## Important Notes

- **Java version**: Source and target compatibility is Java 1.8
- **Resource folder naming**: Must match package name (`sts_tbg_tvg`)
- **Maven filtering**: JSON files have variable substitution enabled (use `${modID}`, `${project.version}`)
- **PSD files**: Excluded from JAR packaging (only PNG files included)
- **Windows paths**: Update `steam.windows` property in `pom.xml` for your system

## Architecture Context

This mod builds on TogetherInSpire's multiplayer framework but adds board game-specific mechanics:

### Planned Extensions
- **Turn phase state machine** (Planning → Ready Check → Resolution → Enemy Turn → Cleanup)
- **Action queueing system** (players plan cards before simultaneous resolution)
- **Row assignment manager** (track which player is in which row)
- **Die roll coordinator** (host rolls, broadcasts to all clients)
- **Token cap enforcement** (server-authoritative limits on Strength/Block/Poison)

### Integration Strategy
Add a `BoardGameCombatManager` that wraps TogetherInSpire's networking layer while implementing board game rules. Toggle with a `GameSettings.boardGameMode` flag to preserve compatibility with normal multiplayer mode.

## Localization

All text is stored in JSON files under `localization/<language>/`:
- `CardStrings.json` - Card names and descriptions
- `PowerStrings.json` - Power names and descriptions
- `RelicStrings.json` - Relic names and descriptions
- `Keywords.json` - Custom keywords with tooltips
- `UIStrings.json` - Interface text

Default language is English (`eng`). The game falls back to English for any missing translations in other languages.
