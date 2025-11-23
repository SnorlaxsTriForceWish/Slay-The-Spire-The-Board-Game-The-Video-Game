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
- Runs all unit tests (build fails if tests fail)
- Validates JSON files in `src/main/resources`
- Compiles Java sources (Java 1.8 target)
- Packages JAR file to `target/sts_tbg_tvg.jar`
- Automatically copies JAR to Steam mods folder

**Run tests only:**
```bash
mvn test
```

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
    PostInitializeSubscriber {}   // Post-init setup
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
z2. /clHotseat mode (asymmetric player/character distribution)
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
- **Testing**: Extract damage calculations or complex logic to utility methods and test them

**Relics**: Extend `BaseRelic` and place in `relics/` package
- Implement relic trigger methods (e.g., `atBattleStart()`)
- Add localization in `RelicStrings.json`
- Add images: small (32x32) and large versions
- **Testing**: If relic has complex logic, extract to utility methods and add tests

**Powers**: Extend `BasePower` and place in `powers/` package
- Implement power effects (e.g., `atEndOfTurn()`)
- Add localization in `PowerStrings.json`
- Add power icon (32x32) to `images/powers/`
- **Testing**: Extract any calculations or logic to testable utility methods

**Utility Classes**: Place in `util/` package for shared logic
- **Always create corresponding test files** in `src/test/java/sts_tbg_tvg/util/`
- Test all public methods with comprehensive test cases
- This is where most of your unit tests should live

### Building and Testing the Mod

**CRITICAL: After making any code changes, you MUST build the mod to validate your changes. This will run unit tests AND check for compilation errors.**


### Unit Testing

The project uses JUnit 4 for unit testing. Tests are automatically run during the Maven build process.

**Test Structure:**
```
src/test/java/sts_tbg_tvg/
├── StsTbgTvgModTest.java           # Framework-dependent tests (marked @Ignore)
├── characters/
│   └── BoardGameIroncladTest.java  # Character tests (marked @Ignore)
└── util/
    └── GeneralUtilsTest.java       # Utility class tests (executable)
```

**Test File Categories:**
1. **Executable Tests** (GeneralUtilsTest): Test pure utility logic without game dependencies
2. **Documentation Tests** (StsTbgTvgModTest, BoardGameIroncladTest): Marked with `@Ignore`, these document what would be tested if code were refactored for testability

**Running Tests:**
- Tests run automatically with `build.bat` or `mvn clean package`
- Run tests only: `mvn test`
- Skip tests: `mvn clean package -DskipTests`

**Writing Tests:**
1. Create test classes in `src/test/java/` mirroring the package structure of `src/main/java/`
2. Test class names should end with `Test` (e.g., `GeneralUtilsTest`)
3. Use JUnit 4 annotations: `@Test`, `@Before`, `@After`
4. Import assertions: `import static org.junit.Assert.*;`

**Example Test:**
```java
package sts_tbg_tvg.util;

import org.junit.Test;
import static org.junit.Assert.*;

public class GeneralUtilsTest {
    @Test
    public void testArrToString_withMultipleElements() {
        Object[] elements = new Object[]{"Alice", "Bob", "Charlie"};
        assertEquals("Alice, Bob, Charlie", GeneralUtils.arrToString(elements));
    }
}
```

**Testing Guidelines:**
- **Focus on testable code**: Test utility classes and pure logic that doesn't require Slay the Spire game initialization
- **Framework dependencies**: Classes that depend on `AbstractDungeon`, `BaseMod`, `CardCrawlGame`, or `Gdx` cannot be tested in standard unit tests
  - Static initializers that load game resources will fail without the game running
  - Use `@Ignore` annotation to document what would be tested if code were refactored
- **Refactoring for testability**: Extract game logic into static utility methods that accept dependencies as parameters
  - Example: Move starting deck composition from instance methods to static utility methods
  - Example: Extract constants to configuration classes that don't depend on game framework
- **Test edge cases**: null inputs, empty collections, boundary conditions
- **Keep tests fast and isolated**: Each test should be independent and run quickly

### Development Workflow with Tests

**IMPORTANT: Always consider testing when adding or modifying code.** Follow this workflow:

**When Adding New Functionality:**
1. **Check if the logic is testable**: Can it be written without game framework dependencies?
2. **Write tests first (if testable)**: Define expected behavior in a test before implementing
3. **If not directly testable**:
   - Extract testable logic into utility methods/classes
   - Add `@Ignore` tests documenting what should be tested if refactored
   - Consider adding helper methods that can be tested independently
4. **Implement the functionality**
5. **Run tests**: `mvn test` or `build.bat` to verify all tests pass
6. **Update existing tests**: If behavior changes affect existing tests, update them

**When Modifying Existing Code:**
1. **Check for existing tests**: Look for corresponding test files in `src/test/java/`
2. **Update tests first**: Modify tests to reflect the new expected behavior
3. **Modify the implementation**
4. **Run tests**: Verify all tests pass with the new changes
5. **Add new test cases**: If the modification adds new edge cases or scenarios

**When Adding Utility Classes:**
1. **Always create a test file**: Utility classes are perfect candidates for unit testing
2. **Test all public methods**: Cover happy path, edge cases, and error conditions
3. **Example**: When creating `util/MyHelper.java`, also create `src/test/java/sts_tbg_tvg/util/MyHelperTest.java`

**When Adding Game Framework Code (Cards, Relics, Powers, Characters):**
1. **Extract testable logic**: Move calculations, validation, and business logic to utility methods
2. **Test the utilities**: Write tests for the extracted logic
3. **Document with @Ignore tests**: Create test files showing what would be tested if possible
4. **Add integration notes**: Document in comments what manual testing is needed in-game

**Test Maintenance:**
- Keep test files in sync with source code structure
- Remove tests for deleted functionality
- Update test assertions when behavior intentionally changes
- Don't commit broken tests - either fix them or mark them `@Ignore` with a TODO comment

**Example Workflow - Adding a New Utility Method:**
```java
// 1. Write test first (src/test/java/sts_tbg_tvg/util/CombatUtilsTest.java)
@Test
public void testCalculateDamageWithStrength_positiveStrength() {
    assertEquals(15, CombatUtils.calculateDamageWithStrength(10, 5));
}

// 2. Implement method (src/main/java/sts_tbg_tvg/util/CombatUtils.java)
public static int calculateDamageWithStrength(int baseDamage, int strength) {
    return baseDamage + strength;
}

// 3. Run: mvn test
// 4. Test passes ✓
```

### Common Patterns

**Accessing game state:**
```
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
