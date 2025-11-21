# Slay the Spire Board Game - Video Game Adaptation Design Document

## Executive Summary
This document outlines the high-level design for adapting the Slay the Spire board game back into the video game format. The primary focus is creating a **cooperative multiplayer experience** (2-4 players) that faithfully recreates the board game's mechanics while leveraging the advantages of a digital medium.

## Core Design Pillars

### 1. Cooperative Multiplayer (2-4 Players)
- **Shared Adventure**: All players progress through the same map together
- **Shared Victory/Defeat**: If any player dies, the entire party loses
- **Independent Decks**: Each player builds and manages their own deck
- **Resource Sharing**: Limited (potions tradeable, gold poolable at merchants)

### 2. Simultaneous Turn-Based Combat
- All players take their turn simultaneously during the "Player Turn" phase
- Enemies act during the "Enemy Turn" phase
- Single shared die roll affects all players and enemies each round

---

## Major System Changes from Base Game

### Map & Progression System

#### Shared Map Navigation
- **Linear Progression**: Players move together as a party up the map
- **No Branching Paths**: Unlike the original game, everyone follows the same route
- **Visual Indicator**: A "boot" meeple/marker shows current position on map
- **Room Types**: Encounter, Elite, Boss, Event, Campfire, Merchant, Treasure
- **Row Assignment**: Players can switch rows between combats (affects enemy targeting)

#### Map Generation
- Randomly select one of multiple possible maps per Act
- Place randomized tokens on dark/light spaces
- Boss is randomly selected and placed face-down until revealed
- Map tokens reveal room types (players know what's ahead)

### Combat System Redesign

#### Row-Based Enemy Targeting
- **Player Rows**: Each player occupies a row (Row 1-4)
- **Enemy Placement**:
  - Regular Encounters: One enemy per player row
  - Elites: Single Elite in bottom row with Summons in each player row
  - Bosses: Treated as being in all rows
- **Targeting Rules**:
  - Enemies target the player in their row
  - Area-of-effect enemy actions (� symbol) target all players
  - Players can target any enemy
  - Player AoE effects (�) target all enemies in one row + Boss

#### Simultaneous Player Turns
**Start of Turn Phase** (All players simultaneously):
1. Reset Energy to 3, Block to 0
2. Draw 5 cards (no hand size limit)
3. **Shared Die Roll**: One player rolls, result applies to everyone
4. Trigger "Start of Turn" abilities and die-based relic effects

**Play Phase** (Players can discuss strategy):
- Players play cards, use potions, activate abilities in any order
- Can coordinate who attacks which enemies
- Can announce status effects (Weak, Vulnerable) to help coordination
- Play cards until all players are ready to end turn

**End of Turn Phase**:
1. Trigger "End of Turn" abilities
2. All players discard remaining hand
3. Proceed to Enemy Turn

#### Enemy Turn
1. **Remove Enemy Block**: All enemies lose Block tokens
2. **Enemy Actions**: Enemies act top row to bottom, left to right, Boss acts last
3. **Move Cube Actions**: For cube-based enemies, advance their cube tracker
4. Return to Player Turn

#### Enemy Action Patterns
Three types of enemy behavior:
1. **Single Action**: Same action every turn
2. **Die Action**: Action based on shared die result (1-6)
3. **Cube Action**: Sequential actions on a track, cube moves down each turn

#### Combat End Conditions
- **Victory**: All enemies defeated
- **Defeat**: Any player reaches 0 HP
- **Cleanup**:
  - Powers return to deck
  - Status/Daze cards removed from decks
  - Block/Energy reset
  - Character-specific resets (Orbs, Stances, tokens)

### Resource System Changes

#### Energy System
- **Fixed Reset**: Always reset to 3 Energy at start of turn (not progressive)
- **Maximum**: 6 Energy cap
- **Character Icons**: Each character has unique Energy symbol (all functionally identical)
- **Miracles/Energy Gain**: Can exceed 6 temporarily if immediately spent

#### Block System
- **Maximum**: 20 Block cap per player
- **Reset Timing**: Player Block resets to 0 at START of Player Turn (not end)
- **Enemy Block**: Resets at start of Enemy Turn
- **No Carry-Over**: Unlike some video game strategies, Block never persists between turns

#### Hit Points
- **Character-Specific Starting HP**: Each character has different max HP
- **Damage Prevention**: Block prevents damage from hits
- **HP Loss Effects**: "Lose X HP" effects bypass Block
- **Healing Between Acts**: Full heal at start of Act II, III, IV
- **Death**: 0 HP = immediate party loss

### Character-Specific Mechanics

#### Ironclad (Red)
- **Starting HP**: Highest of all characters
- **Strength Cap**: Maximum 8 Strength tokens
- **Focus**: Exhaust synergies
- **Starting Ability**: Unique board ability

#### Silent (Green)
- **Poison Cap**: Maximum 30 Poison combined across all enemies
- **Poison Timing**: Enemies lose 1 HP per Poison at end of turn (bypasses Block)
- **Shiv Tokens**: Maximum 5, usable anytime during Play phase as 1 damage Attacks
- **Starting Ability**: Unique board ability

#### Defect (Blue)
- **Orb System Changes**:
  - Can place Orbs in any slot (not left-to-right)
  - Can Evoke any Orb (not just rightmost)
  - Orbs trigger at End of Turn (Lightning/Frost passive effects)
  - No Orb slot limit increase from other sources
- **Orb Types**: Lightning (yellow cubes), Frost (blue cubes), Dark (purple cubes)
- **Starting Ability**: Unique board ability

#### Watcher (Purple)
- **Stance System**:
  - Start each combat in Neutral Stance
  - Cannot enter a Stance already in
  - Calm: Gain 2 Energy when leaving
  - Wrath: +1 damage all hits, take 1 damage at end of turn (if still in Wrath)
  - Neutral: No effects
- **Miracle Tokens**: Maximum 5, usable anytime for 1 Energy (can exceed Energy cap if immediately spent)
- **Starting Ability**: Unique board ability

### Deck Building & Card Systems

#### Card Rewards
- **Personal Decks**: Each player has their own Card Rewards deck and Rare Rewards deck
- **Reward Structure**:
  - Reveal top 3 cards from Card Rewards deck
  - Choose 1 or skip
  - Unselected cards go to bottom of deck
- **Golden Ticket**: Special card in rewards deck that reveals a Rare card option
- **Upgraded Rewards**: Some rewards upgrade the chosen card immediately
- **Rare Rewards**: Reveal 3 from Rare deck, choose 1 or skip
- **Skipping**: Can always skip adding cards to deck

#### Card Rarities & Types
- **Starter** (gray border): Starting deck cards
- **Common** (gray banner): 2 copies exist in rewards deck
- **Uncommon** (blue banner)
- **Rare** (yellow banner)
- **Colorless**: Separate deck, can be unlocked
- **Curse**: Negative cards added from various sources
- **Status/Daze**: Temporary debuff cards, removed after combat

#### Card Mechanics
- **X-Cost Cards**: Can spend 0+ Energy, effect based on X
- **Exhaust**: Card removed from deck until end of combat
- **Ethereal**: Exhausts if in hand at end of turn
- **Retain**: Not discarded at end of turn
- **Unplayable**: Cannot be played (Status/Curse cards)
- **Powers**: Stay in play until end of combat
- **No Memory**: Cards forget previous states when leaving hand (cost changes, Retain, etc.)

#### Deck Management
- **Remove**: Permanently remove a card (keep in removed pile)
- **Upgrade**: Flip card to upgraded side (green text)
- **Transform**: Remove a card, add random card from rewards deck

### Item Systems

#### Potions
- **Limit**: 3 potions per player (2 in Ascension 4+)
- **Trading**: Only item type that can be traded between players
- **Single-Use**: Discarded after use
- **Outside Combat**: Some potions (Gambler's Brew, Entropic Brew, Blood Potion, Fairy in a Bottle) usable outside combat
- **Deck Management**: Put on bottom of deck when used/discarded/skipped

#### Relics
- **Personal Collection**: Each player has their own relics
- **Die Relics**: Trigger on specific die results (1-6)
- **Once Per Combat**: Flip face-down when used, flip up after combat
- **Once Per Room**: Can be used at Events, flip when used
- **Loaded Die (Solo)**: Special relic for solo play, can trigger own ability or another die relic

#### Boss Relics
- **Party Reward**: Reveal (# players + 1) Boss relics [3 if solo]
- **Each Player Chooses**: Each player may take one or skip
- **High-Risk High-Reward**: Often have drawbacks

### Event & Encounter Systems

#### Events
- **Individual Choices**: Each player chooses their own option from bracketed [yellow] text
- **Payment Requirements**: Must have resources to choose "Pay" or "Give" options
- **Die-Based**: Some events use die rolls
- **Return to Bottom**: Event cards return to bottom of deck after use

#### Campfire
- **Two Options** (each player chooses independently):
  - **Rest**: Heal 3 HP
  - **Smith**: Upgrade a card

#### Merchant
- **Party Shopping**: All players visit together
- **Gold Pooling**: Players can pay for each other's purchases
- **Available Items**:
  - 3 Relics (top-left on sale for -1 gold)
  - 3 Potions
  - Each player's top 3 Card Rewards (Common/Uncommon/Rare pricing)
- **Card Remove**: Pay gold to remove a card (once per player per merchant)
- **Pricing**: Common (cheap), Uncommon (medium), Rare (expensive)

#### Treasure Rooms
- Each player gains a Relic
- Optional: "Choose Your Relic" variant (reveal # players relics, each chooses one)

### Summons System
- **Green Bar Icon**: Indicates enemy summons others at start of combat
- **Summons Deck**: Separate deck of summoned enemies
- **Placement**: Summons placed in rows (left of summoner for encounters, in player rows for Elites)
- **No Fleeing**: Unlike video game, Summons stay even if summoner dies
- **Random Selection**: When multiple Summons share a name, select randomly

### Status Effects & Tokens

#### Strength
- **Player Cap**: Maximum 8 tokens
- **Effect**: +1 damage per hit per Strength token
- **Applies to**: Each individual hit in multi-hit attacks
- **Enemy Strength**: No cap, same +1 per hit effect

#### Vulnerable
- **Cap**: Maximum 3 tokens (per player or enemy)
- **Effect**: Double damage from each hit in an attack
- **Timing**: Apply Strength bonuses before doubling
- **Token Removal**: Remove 1 token after entire attack resolves
- **Multi-Hit**: Still only removes 1 token even with multiple hits

#### Weak
- **Cap**: Maximum 3 tokens (per player or enemy)
- **Effect**: -1 damage per hit in an attack
- **Token Removal**: Remove 1 token after entire attack resolves
- **Multi-Hit**: Still only removes 1 token even with multiple hits
- **Interaction**: Weak vs Vulnerable cancels both out, removes tokens from both

#### Poison (Silent-specific)
- **Global Cap**: 30 Poison total across ALL enemies
- **Effect**: Enemy loses 1 HP per Poison token at end of turn
- **Bypasses Block**: HP loss, not damage
- **Persistence**: Stays on enemy until enemy dies
- **No Removal**: Tokens don't reduce over time

### Multi-Hit Attack System
- **Same Target**: All hits target the same enemy (unless stated)
- **Same Bonuses**: All hits affected equally by Strength, Vulnerable, Weak
- **Token Removal**: Only 1 Vulnerable/Weak token removed per complete multi-hit
- **Examples**: Twin Strike (2 hits), Finisher (variable hits), Barrage (variable hits)

### Die System
- **Single Roll Per Round**: One player rolls at start of Player Turn
- **Shared Result**: Same die result applies to all players and enemies
- **No Re-rolls**: Die result locked for the round (unless ability modifies it before use)
- **Triggers**:
  - Enemy die actions
  - Die-based relic abilities
  - Event outcomes
- **Modification Window**: Abilities that change die result must be used immediately after roll

### Act Structure & Progression

#### Act Setup
- **Act I**:
  - Neow's Blessing (each player draws one, gains red reward + choice of 3 blue rewards)
  - First Encounter (special "1st Encounter" cards)
  - Board game exclusive starting scenario
- **Act II & III**:
  - Full HP heal for all players
  - Shuffle Card Rewards deck (include any skipped cards)
  - New board with new map
  - New Enemy/Elite/Event/Summons decks
- **Act IV** (Unlockable):
  - Requires 3 Keys (Ruby, Emerald, Sapphire)
  - Special Boss (Corrupt Heart)
  - Special Elites

#### Act IV Key System
- **Ruby Key**: All players take no action at a Campfire
- **Sapphire Key**: All players skip a Relic at Treasure/Elite
- **Emerald Key**: Defeat a Burning Elite (before combat, shuffle Burn cards into decks)

#### Boss Fights
- **Boss in All Rows**: Treated as being in every row
- **Rewards**: All players gain Boss rewards
- **Post-Boss Options**: Save & continue later, proceed to next Act, or end game
- **Boss Relics**: Party reward system

### Victory & Defeat Conditions

#### Victory
- Defeat the final Boss of chosen Act (can stop at Act I, II, or III)
- With Act IV unlocked: Defeat Corrupt Heart

#### Defeat
- Any player reaches 0 HP = immediate party loss
- Optional "Last Stand" rule: In Boss fights only, other players can continue if one dies

### Ascension System
- **Progressive Difficulty**: 13 Ascension levels
- **Unlock Condition**: Defeat Act II Boss (Act III for solo) while playing on all previous Ascension modifiers
- **Modifiers Include**:
  - Harder Elites/Encounters/Bosses/Events
  - Lower max HP, starting damaged
  - Reduced potion limit (2 instead of 3)
  - Starting Curse (Ascender's Bane)
  - Reduced healing between Acts (4 HP instead of full)
  - Higher card remove cost
  - Double Act III Boss

### Unlock System
- **Character Unlocks**: Check boxes per Boss defeated
- **Unlock Rewards**: New cards added to appropriate decks
- **Colorless Cards**: Unlockable deck of character-neutral cards
- **Act IV**: Unlockable after sufficient progression

### Daily Climb & Custom Runs
- **Daily Climb**: Roll die twice for random modifiers from two categories
- **Custom Run**: Choose any modifiers
- **Modifier Examples**:
  - All Star (start with 5 random colorless)
  - Shiny (start with 5 random rares)
  - Prismatic Shard (cross-class card rewards)
  - Terminal (lose 1 HP at end of combat)
  - Cursed (start with 2 random Curses)
  - Uncertain Future (hidden map tokens)

### Achievement System
Examples include:
- Jaxxed: Hit Strength limit (8)
- Catalyst: Hit Poison limit (30)
- Ninja: Play 7 Shivs in one turn
- Perfect: Beat Boss with all players at full HP
- Minimalist: Beat Act III with 5-card deck
- Character-specific: Beat Act III with each character

---

## User Experience & Interface Design Needs

### Lobby & Game Setup
- **Player Selection**: 1-4 players (local co-op, online multiplayer, or solo)
- **Character Selection**: Each player picks Ironclad, Silent, Defect, or Watcher
- **Ascension Selection**: Choose Ascension level (must have unlocked)
- **Quick Start Option**: Start at Act II or III with preset rewards
- **Daily Climb**: Special mode with random modifiers
- **Custom Run**: Choose specific modifiers

### In-Game HUD Requirements
- **Player-Specific UI**:
  - Personal hand of cards
  - Personal deck/discard/exhaust pile indicators
  - Energy tracker (0-6)
  - Block tracker (0-20)
  - HP tracker (character-specific max)
  - Character-specific resources (Strength, Orbs, Shivs, Miracles, Stances, Poison)
  - Personal relics, potions, Powers in play
  - Gold count
- **Shared UI**:
  - Map view with current position
  - Die result (large, visible to all)
  - Enemy board (rows 1-4, Boss area)
  - Enemy HP, Block, tokens
  - Turn phase indicator (Start of Turn, Play Phase, End of Turn, Enemy Turn)
  - Other players' HP, Energy, Block at a glance
- **Communication**:
  - Chat system
  - Quick pings/emotes for strategy
  - "Ready to end turn" indicator for each player

### Map Screen
- **Visual Layout**: Vertical map showing path from bottom (start) to top (Boss)
- **Room Types**: Clear icons for Encounter, Elite, Event, Campfire, Merchant, Treasure, Boss
- **Party Position**: Boot meeple or party indicator
- **Token Visibility**: Room contents revealed/hidden based on progression
- **Row Indicators**: Show which player is in which row
- **Row Switching**: Allow players to switch rows between combats

### Combat Screen
- **Row Layout**:
  - 4 horizontal rows (one per possible player)
  - Enemies on right side of each row
  - Player characters/indicators on left
  - Boss area separate, visually connected to all rows
- **Turn Flow Visualization**:
  - Clear phase transitions
  - All players' actions visible simultaneously
  - Enemy action preview (show what enemies will do)
  - Animation queue for simultaneous actions
- **Targeting System**:
  - Players can click any enemy
  - AoE highlighting (show which enemies/players affected)
  - Row highlighting for row-based effects
- **Die Roll**:
  - Prominent animation
  - Result displayed clearly
  - Highlight affected enemy actions/relics

### Card Play Interface
- **Energy Display**: Clear cost vs. available Energy
- **Targeting**: Click to select target for non-AoE, automatic for AoE
- **Upgrade Preview**: Hover to see upgraded version before choosing
- **Multi-Player Coordination**:
  - See other players' planned actions (optional setting)
  - Undo/reset before confirming "End Turn"
  - Wait for all players before proceeding to Enemy Turn

### Merchant Interface
- **Shared Shopping**: All players see same merchant
- **Gold Pooling**: UI to contribute gold to another player's purchase
- **Simultaneous Browsing**: All players can browse simultaneously
- **Purchase Confirmation**: Clear indicators of who's buying what
- **Card Previews**: Each player sees their own 3 revealed cards

### Event Interface
- **Individual Choices**: Each player selects independently
- **Choice Visibility**: See what others chose (after committing)
- **Die Roll Integration**: For events that use die
- **Sequential Resolution**: If event requires order, clear turn order

### Campfire Interface
- **Simultaneous Choice**: All players choose Rest or Smith at once
- **Upgrade Preview**: For Smith, show upgrade before committing
- **Heal Display**: Show current/max HP for Rest decision

---

## Technical Implementation Priorities

### Core Systems (Must-Have)
1. **Multiplayer Framework**: Networking, turn synchronization, state management
2. **Row-Based Combat**: Enemy placement, targeting rules, simultaneous turns
3. **Die System**: Shared die roll, enemy action selection, relic triggers
4. **Player Turn Flow**: Simultaneous card play, "ready to end turn" system
5. **Enemy Turn Flow**: Sequential enemy actions with proper ordering
6. **Character-Specific Mechanics**: Orbs, Stances, Poison, Shivs, Miracles
7. **Token Systems**: Strength, Block, Vulnerable, Weak with caps
8. **Deck Management**: Personal decks, Card Rewards decks, Rare Rewards decks

### Secondary Systems (Important)
1. **Map Generation**: Random map selection, token placement, Boss selection
2. **Event System**: Individual player choices, die integration
3. **Merchant System**: Shared shopping, gold pooling
4. **Summons System**: Combat start summoning, dynamic enemy addition
5. **Status Cards**: Daze, Status, Curse management and removal
6. **Potion Trading**: UI for transferring potions between players
7. **Row Switching**: Between-combat row assignment

### Polish Systems (Nice-to-Have)
1. **Ascension**: Progressive difficulty unlocks
2. **Unlock System**: Boss defeat tracking, card unlocks
3. **Achievement System**: Tracking and rewards
4. **Daily Climb**: Random modifier generation
5. **Custom Runs**: Modifier selection UI
6. **Act IV**: Keys system, special encounters
7. **Optional Rules**: Last Stand, Sequential Turns, Choose Your Relic
8. **Quick Start**: Mid-Act entry with preset rewards

---

## Card Implementation Notes

### All Cards Must Be Re-Implemented
- **Board Game Versions**: Different numbers, effects, or costs from video game
- **Multiplayer Considerations**: Many cards need adjustments for co-op
- **Examples of Changes**:
  - AoE cards work differently (row-based)
  - Some cards reference die rolls
  - Cards may have different upgrade paths
  - Token caps affect balance (Strength, Poison, etc.)

### New Card Mechanics to Implement
- **Row Targeting**: Cards that specify "any row"
- **Die Synergies**: Cards that interact with die results
- **Multiplayer Buffs**: Cards that can target allies
- **Summoning**: Cards that work with Summons system

---

## Relic Implementation Notes

### Die-Based Relics
- Trigger on specific die results (1-6)
- Must check every turn during Start of Turn phase
- Can be modified by "once per room" relics that change die result

### Once Per Combat/Room Relics
- Need visual indicator of used/available state
- Flip face-down when used
- Reset timing (combat end vs. room end vs. Event end)

### Loaded Die (Solo-Specific)
- Special relic for solo play
- Can trigger itself or another die relic
- Needs special UI

---

## Enemy Implementation Notes

### Action Patterns
- **Single Action**: Simplest, same every turn
- **Die Action**: Needs to read shared die result
- **Cube Action**: Needs state tracking, movement logic, loop detection

### Summons
- Must check for Summon icon at combat start
- Search Summons deck
- Place in correct row positions
- Handle random selection for duplicate names
- Don't flee when summoner dies (unlike video game)

### Special Abilities
- Always active (yellow text)
- Conditional triggers (on death, start of combat, at certain HP)
- Turn-based triggers (Gremlin Nob's Enraged on turn 2+)

### Boss Modes
- Some Bosses switch between modes (Attack/Defensive for Guardian)
- Cube tracker resets to first action of new mode
- Only use actions/abilities from current mode

---

## Multiplayer-Specific Features

### Communication Systems
- **Text Chat**: For strategy discussion
- **Quick Pings**: Target pings, danger warnings
- **Emotes**: Fast communication
- **Status Announcements**: Auto-announce Weak/Vulnerable

### Synchronization Points
- **Simultaneous Actions**: All players play cards at same time
- **Ready Check**: "End Turn" ready indicator for each player
- **Combat Start**: All players ready before starting
- **Rewards**: All players confirm rewards before leaving room
- **Merchant**: All players ready before leaving shop

### Player Drop Handling
- **In Combat**: If player disconnects during combat, offer surrender or AI takeover
- **Between Rooms**: Can wait for reconnect or replace with AI
- **Save State**: Allow saving mid-run for async play

### Difficulty Scaling
- Enemy HP scales by player count (1/2/3/4 player values)
- Summons placement depends on player count
- Boss relic reveal count based on player count
- Rewards balanced for progression rate

---

## Quality of Life Features

### Deck Management
- **Quick View**: See full deck, discard, exhaust piles anytime
- **Upgrade Reference**: Check what upgrades do before selecting
- **Remove/Transform Preview**: See what's being removed
- **Curse Warning**: Highlight when gaining Curses

### Combat
- **Enemy Intent Preview**: Show what enemies will do based on current die result
- **Damage Calculation**: Show final damage accounting for Strength, Vulnerable, Weak
- **Block Projection**: Show Block after playing cards in hand
- **Energy Cost Highlighting**: Show playable cards clearly
- **Undo**: Allow undoing card plays before ending turn (multiplayer consideration)

### Map
- **Path Planning**: Show future rooms
- **Key Progress**: For Act IV, show which keys obtained
- **Act Progress**: Show current room / total rooms

### Accessibility
- **Colorblind Mode**: Distinguish card types/rarities without color
- **Scalable UI**: Adjust for different screen sizes
- **Keyboard Shortcuts**: Fast card play, targeting
- **Turn Timer Option**: Optional time limit for Play phase (prevent griefing)

---

## Known Differences from Video Game (Summary)

### Core Gameplay
- Cooperative multiplayer vs. solo
- Simultaneous turn-based vs. asynchronous
- Row-based enemy targeting vs. open targeting
- Shared die roll system (new mechanic)
- Linear map progression vs. branching paths
- Energy resets to 3 (not progressive gain)
- Block resets to 0 at start of turn

### Combat
- Token caps (Strength 8, Block 20, Poison 30, etc.)
- Multi-hit attacks work differently with Vulnerable/Weak
- Enemy action patterns (Single/Die/Cube)
- Summons don't flee
- Boss relics revealed to party

### Character Mechanics
- Defect Orbs: any slot, any evoke order
- Watcher Stance: can't re-enter same Stance
- Silent Poison: global 30 cap
- Ironclad Strength: 8 cap

### Resources
- Potions tradeable between players
- Gold poolable at merchants
- 3 potion limit (2 in Ascension)
- No Energy gain between turns

### Progression
- Personal Card Rewards decks
- Rare Rewards separate deck
- Status/Daze removed after combat
- Full heal between Acts (or 4 HP in high Ascension)

---

## Multi-Character Control System

### Core Concept: Hotseat Mode
**Any player can control multiple characters simultaneously.** This serves multiple purposes:
- **Solo Play**: One player can play all 4 characters for full party experience
- **Playtesting**: Developers/testers can control all characters to test interactions
- **Disconnect Recovery**: Other players can temporarily control disconnected player's character
- **Asymmetric Groups**: 2 players can each control 2 characters, or 3 players with 1 controlling 2
- **Learning**: New players can start by controlling just 1 character while experienced player handles others

### Character Ownership & Control
- **Primary Owner**: Character has a primary owner (the player who selected them)
- **Active Controller**: Who currently controls the character (can differ from owner)
- **Control Transfer**: Any player can request to control an uncontrolled/disconnected character
- **Reclaim Control**: Primary owner can reclaim control when they reconnect
- **Visual Indicators**: Clear color-coding and icons show who owns and who's controlling each character

### Hotkey System for Quick Switching
- **[1] [2] [3] [4]**: Switch active character view
- **[Tab]**: Cycle through characters you control
- **[Shift+1/2/3/4]**: "Peek" at character (view without switching active)
- **[Ctrl+Click]**: Play card from inactive character (if you control them)
- **[Space]**: "Ready" toggle for current character

### Multi-Character Play Modes

#### Solo Commander Mode (1 player, 1-4 characters)
- Full control over all characters
- Quick-switch between character perspectives
- Can play cards from any character without switching views
- Single "End Turn" confirms all characters ready

#### Hotseat Mode (multiple players, some controlling multiple characters)
- Player 1 might control Characters 1 & 2
- Player 2 might control Characters 3 & 4
- Each player manages their assigned characters
- "Ready" indicator per character

#### Full Multiplayer Mode (1 player per character)
- Each player controls only their character
- Can "peek" at ally hands for coordination
- Can request control if ally disconnects

---

## UX Solutions for 4-Player Display

### Primary Layout: Compact Row View

**Vertical Row Layout (Top to Bottom):**
```
+------------------------------------------------------------------+
|  [Phase: Player Turn] [Die: 4] [Boss: The Guardian - 78 HP]     |
+------------------------------------------------------------------+
|                        BOSS AREA                                 |
|              [Boss Enemy - large display]                        |
+------------------------------------------------------------------+
| Row 4: [P4 Icon][HP][Energy][Block] ← [Enemy][Enemy]  [Actions] |
+------------------------------------------------------------------+
| Row 3: [P3 Icon][HP][Energy][Block] ← [Enemy]         [Actions] |
+------------------------------------------------------------------+
| Row 2: [P2 Icon][HP][Energy][Block] ← [Enemy][Enemy]  [Actions] |
+------------------------------------------------------------------+
| Row 1: [P1 Icon][HP][Energy][Block] ← [Enemy]         [Actions] |
+------------------------------------------------------------------+
|                    ACTIVE PLAYER HAND AREA                       |
|        [Card] [Card] [Card] [Card] [Card] [Card] [Card]         |
+------------------------------------------------------------------+
| [P1][P2][P3][P4] Character Tabs | [End Turn: 2/4 Ready] [Chat]  |
+------------------------------------------------------------------+
```

### Row Display Components

Each row shows (left to right):
1. **Player Character Mini**: Small animated character sprite
2. **Quick Stats**: HP (❤️15/20) Energy (⚡3) Block (🛡️5)
3. **Status Icons**: Small icons for Strength, Vulnerable, Weak, character-specific (Wrath, Orbs, etc.)
4. **Arrow Indicator**: Shows enemy targeting this player
5. **Enemies**: Enemy cards with HP, Block, intent icons
6. **Action Preview**: Hover-over shows full enemy action details

**Row Height**: ~80-100px per row (compact but readable)

### Active Character Hand Area (Bottom ~250px)

- **Full Hand Display**: Current character's hand shown large and playable
- **Energy Cost Highlighting**: Green = playable, Red = too expensive, Gray = unplayable
- **Quick Actions**: Potion icons, starting ability button
- **Deck Counters**: Draw pile (15), Discard (8), Exhaust (2) - click to view

### Character Tabs (Bottom Bar)

Each tab shows:
- **Character Icon**: Color-coded (Red/Green/Blue/Purple)
- **Ready Indicator**: ✓ if player marked ready to end turn
- **Quick Stats**: HP/Energy at a glance
- **Control Indicator**: Border shows who controls this character
  - Your characters: Solid gold border
  - Disconnected: Red dashed border
  - Other players: Their player color border

**Click tab to switch character view**

### Hand Visibility System

#### Option A: Full Transparency (Recommended for Co-op)
- **Default**: All players can see all hands
- **Hover**: Hover over character tab to see their full hand in tooltip
- **Click Tab**: Switch to that character's perspective (if you control them)
- **Peek Mode**: Hold [Shift] + Hover over character shows hand overlay
- **Rationale**: Board game is cooperative, full information helps coordination

#### Option B: Partial Visibility
- **Card Count**: Always visible (e.g., "P2: 7 cards")
- **Playable Info**: Show "3 attacks, 2 skills, 1 power, 1 unplayable"
- **Peek Request**: Player can request to see another's hand (requires consent)
- **Rationale**: Preserves some individual agency, requires more communication

**Decision: Use Option A (Full Transparency)** - The board game encourages discussion, and strategic coordination requires knowing what allies can do.

### Compact Resource Display

#### Player Board Minimized View (in row)
```
[Ironclad Icon] ❤️18/20 ⚡3 🛡️5 💪2 [Ready: ✗]
```

#### Player Board Expanded View (when active character)
```
+--------------------------------+
| IRONCLAD (Player 1: Alice)     |
| HP: ❤️❤️❤️❤️❤️❤️❤️❤️❤️❤️ 18/20 |
| Energy: ⚡⚡⚡ 3/3              |
| Block: 🛡️🛡️🛡️🛡️🛡️ 5          |
| Strength: 💪💪 2/8             |
| Gold: 45                       |
| Potions: [🧪][🧪][ ]           |
| Relics: [R][R][R][R][R] (5)    |
+--------------------------------+
```

#### Character-Specific Resource Indicators

**Defect (Orbs):**
```
Orbs: [⚡Lightning][❄️Frost][⚫Dark][ empty ]
```

**Watcher (Stance):**
```
Stance: [WRATH] Miracles: 🎫🎫🎫 (3/5)
```

**Silent (Poison & Shivs):**
```
Poison Pool: ☠️☠️☠️☠️☠️ 18/30  Shivs: 🗡️🗡️🗡️ (3/5)
```

### Enemy Display Optimization

#### Compact Enemy Card
```
+------------------+
| Cultist      ❤️12 |
| Intent: ⚔️ 6     |
| 🛡️3 💢1         |
+------------------+
```

**Hover for details:**
- Full card text
- Special abilities
- Action pattern (Single/Die/Cube)

#### Enemy Action Preview
- **Always Show**: Current intent icon
- **Die Result Highlight**: If die = 4, highlight the "4" action
- **Color Coding**:
  - 🔴 Red: Attack incoming
  - 🟡 Yellow: Buff/debuff
  - 🔵 Blue: Defensive action
  - 🟢 Green: Unknown/special

### Boss Area (Top Center)
- **Large Enemy Card**: Boss prominently displayed
- **HP Bar**: Large, visible to all
- **Mode Indicator**: If Boss has modes (e.g., "ATTACK MODE" vs "DEFENSIVE MODE")
- **Action Preview**: Shows Boss's next action based on die
- **AoE Indicator**: Always visible reminder that Boss targets all rows

---

## Disconnect & Reconnect Handling

### Disconnect Detection
- **Network Timeout**: 30 seconds of no response = disconnected
- **Graceful Disconnect**: Player clicks "Leave" or closes game
- **Mid-Combat**: Game pauses, show disconnect notification
- **Between Rooms**: Game waits at current room

### Disconnect UI Flow

#### 1. Disconnect Notification (Overlay)
```
+----------------------------------------+
|    Player 2 (Silent) Disconnected!     |
|                                        |
| [Wait 60s for reconnect]               |
| [Take Control of Silent]               |
| [Vote to Continue with AI]             |
| [Surrender Run]                        |
+----------------------------------------+
```

#### 2. Temporary Control Transfer
- Any player can click **"Take Control"**
- Disconnected character's border turns red/dashed
- Controller plays both their character and disconnected character
- Use hotkeys [1][2][3][4] to switch between them

#### 3. Reconnection
```
+----------------------------------------+
|    Player 2 (Silent) Reconnected!      |
|                                        |
| Currently controlled by Player 1       |
|                                        |
| [Reclaim Control]                      |
| [Let Player 1 keep control]            |
+----------------------------------------+
```

- **Reclaim**: Player 2 takes back control of Silent
- **Keep Control**: Player 1 continues controlling Silent (useful if in middle of planning)

#### 4. AI Takeover (Optional Vote)
- If no player wants to control disconnected character
- Simple AI: Plays highest-cost playable cards, attacks lowest HP enemy
- Vote required (majority of remaining players)
- AI indicator shows on character tab

### Reconnect Grace Period
- **Combat**: Pause until reconnect or 60s timeout
- **Map**: Wait indefinitely (no timeout)
- **Events**: Wait indefinitely
- **Merchant**: Wait indefinitely

### Connection Status Indicators
```
Player Tabs:
[P1: Alice ✓ 🟢] [P2: Bob ✓ 🟡] [P3: Carol ✗ 🔴] [P4: Dave ✓ 🟢]

🟢 Green: Connected, good ping
🟡 Yellow: Connected, high latency
🔴 Red: Disconnected
```

---

## Screen Space Optimization Techniques

### 1. Collapsible Panels
- **Relics Panel**: Shows count, hover/click to expand
- **Potions Panel**: Always visible (max 3), compact icons
- **Powers Panel**: Minimized icons, hover for details, expand if >3
- **Deck View**: Overlay modal (not always visible)

### 2. Smart Zoom/Focus
- **Combat Zoom**: Click enemy to zoom/focus on it for details
- **Hand Zoom**: Hover card to enlarge (like original STS)
- **Row Highlight**: Hover row to highlight all enemies in that row
- **AoE Preview**: Hover AoE card to highlight affected enemies

### 3. Overlay Modals (Non-Blocking)
- **Deck View**: Transparent overlay, click outside to close
- **Relic Details**: Tooltip on hover, click for full compendium
- **Combat Log**: Side panel, collapsible, shows history of actions
- **Chat**: Collapsible side panel, notification badge for new messages

### 4. Contextual UI
- **In Combat**: Show combat-relevant UI (hands, enemies, HP/Energy/Block)
- **Map Screen**: Show map, party status, keys (if Act IV)
- **Merchant**: Full-screen merchant UI
- **Events**: Full-screen event UI with story text
- **Campfire**: Simple choice screen

### 5. Resolution Scaling
- **Minimum Resolution**: 1920x1080 (1080p)
- **Recommended**: 2560x1440 (1440p) for comfortable 4-player view
- **Ultrawide Support**: Use extra horizontal space for chat, combat log, expanded relics
- **Vertical Space Priority**: Rows stack vertically, critical for 4-player

### 6. Compact Mode Toggle
- **Option**: "Compact UI" setting
- **Effect**: Reduces row height, smaller fonts, icon-only resources
- **Use Case**: Lower resolutions, more screen space for hand/enemies

---

## Specific UI Layout Examples

### Example 1: Solo Player Controlling All 4 Characters

**Layout:**
- All 4 rows visible (compact mode)
- **Large hand area** at bottom shows active character
- **Character tabs** at bottom with [1][2][3][4] hotkeys
- **Quick-switch**: Press [2] to switch to Player 2's perspective instantly
- **All hands visible**: Small hand preview above each row (click to switch)

**Flow:**
1. Player starts turn, die rolls
2. Presses [1] to view Ironclad's hand
3. Plays 2 cards from Ironclad
4. Presses [2] to switch to Silent
5. Plays 3 cards from Silent
6. Presses [3] for Defect, plays cards
7. Presses [4] for Watcher, plays cards
8. Presses [Space] 4 times (or once if "ready all" option) to end turn

**UX Benefit**: Rapid switching, full control, no waiting for other players

### Example 2: 4 Players, One Disconnects

**Before Disconnect:**
- P1 (Alice) controls Ironclad
- P2 (Bob) controls Silent
- P3 (Carol) controls Defect
- P4 (Dave) controls Watcher

**After P2 Disconnects:**
1. Game pauses, notification appears
2. Alice clicks "Take Control of Silent"
3. Alice now controls Ironclad (Row 1) and Silent (Row 3)
4. Alice's character tabs show: [1: Ironclad (You)] [2: Silent (Temp)] [3: Defect (Carol)] [4: Watcher (Dave)]
5. Alice presses [1] and [2] to switch between her two characters
6. Bob reconnects, clicks "Reclaim Control"
7. Alice's tabs revert to normal

**UX Benefit**: Minimal disruption, clear ownership, easy control transfer

### Example 3: 2 Players, Each Controlling 2 Characters (Hotseat)

**Setup:**
- Alice controls Ironclad + Silent (Rows 1 & 2)
- Bob controls Defect + Watcher (Rows 3 & 4)

**Layout:**
- Alice's view: Rows 1-4 visible, Alice's character tabs highlighted
- Bob's view: Rows 1-4 visible, Bob's character tabs highlighted
- Each player can peek at other's hands for coordination

**Flow:**
1. Turn starts, Alice rolls die (or Bob, alternating)
2. Alice plays cards for Ironclad, presses [2], plays cards for Silent
3. Bob simultaneously plays cards for Defect, presses [4], plays cards for Watcher
4. Both click "Ready" when done
5. Enemy turn begins

**UX Benefit**: Flexible party size, full board game experience with just 2 people

---

## Additional UX Features

### Smart Ready System
- **Individual Ready**: Each character can be marked ready independently
- **Ready All (Hotkey: Shift+Space)**: If controlling multiple characters, ready all at once
- **Unready**: Can un-ready to play more cards (if any player unreadies, turn continues)
- **Ready Counter**: "2/4 Ready" shown prominently
- **Auto-Ready Option**: Setting to auto-ready when no playable cards and no other actions available

### Visual Clarity Enhancements
- **Color-Coding**: Each character has persistent color (Red/Green/Blue/Purple)
- **Player Names**: Always visible on character tabs and row displays
- **Turn Phase Banner**: Large banner at top shows "PLAYER TURN" / "ENEMY TURN" / "START OF TURN"
- **Die Result Display**: LARGE die result shown prominently after roll
- **Animation Queueing**: Smooth animation of simultaneous actions (cards played by multiple players)

### Accessibility
- **Colorblind Mode**: Patterns/icons in addition to colors for all statuses
- **Text Scaling**: Adjustable text size
- **High Contrast Mode**: Optional high-contrast outlines for enemies/cards
- **Screen Reader Support**: For visually impaired players (read card text, enemy intents, etc.)
- **Keyboard-Only Play**: Full game playable without mouse
- **Rebindable Hotkeys**: All hotkeys customizable

### Combat Log
- **Side Panel**: Collapsible log on right side
- **Action History**: "Alice (Ironclad) played Bash on Cultist for 8 damage, applied Vulnerable"
- **Filter Options**: Show all / only my characters / only enemies
- **Scroll**: Unlimited history for current combat
- **Export**: Copy log to clipboard for bug reports

### Communication Tools
- **Text Chat**: Standard chat box
- **Quick Pings**: Right-click enemy to ping "Attack this!"
- **Emotes**: Character-specific emotes ("Need help!" / "I've got this!" / "Watch out!")
- **Card Sharing**: Shift+Click card to show in chat ("I'm planning to play this")
- **Voice Chat Integration**: Optional Discord/Steam voice chat integration

---

## Resolved Design Questions

### ✅ Gameplay
1. **Turn timer?** Optional setting, default OFF for casual, optional ON for competitive/timed modes
2. **Disconnects?** Control transfer system + AI fallback option
3. **Practice mode?** Yes - solo mode with 1 character is essentially practice mode
4. **Hand visibility?** Full transparency (cooperative game, strategic coordination required)

### ✅ UI/UX
1. **4 players visualization?** Vertical row layout, ~80-100px per row, compact stats in-row, expanded when active
2. **Enemy action preview?** Always-on icons, hover for details, highlight based on die result
3. **Screen space for 4 rows?** 1080p minimum, rows stack vertically, active character hand at bottom
4. **Row orientation?** Vertical rows (top to bottom), matches board game map layout

### ✅ Multi-Character Control
1. **Solo multi-character?** YES - core feature, hotkey system [1][2][3][4] for switching
2. **Disconnect handling?** Any player can take temporary control, primary owner can reclaim
3. **Asymmetric groups?** YES - any distribution (1 player/4 chars, 2 players/2 chars each, 3 players/2-1-1, etc.)
4. **Control indicators?** Color-coded borders, owner/controller shown on tabs, clear visual distinction

### ⚠️ Still Open Design Questions

### Balance
1. Should solo mode use board game rules or be balanced differently?
2. How to handle balancing when players have very different skill levels?
3. Should there be catch-up mechanics if one player falls behind?

### Technical
1. Online multiplayer, local co-op, or both?
2. Real-time or asynchronous (play-by-mail style) multiplayer?
3. Cross-platform support?
4. Save system for mid-run saves?

---

## Success Metrics

### Core Experience
- Players can complete a full run (Act I-III) cooperatively
- All 4 characters playable with unique mechanics working correctly
- Combat feels strategic and engaging with simultaneous turns
- Multiplayer coordination feels rewarding

### Technical
- Stable multiplayer synchronization (no desyncs)
- Responsive UI (no lag in card play)
- Proper state management (all tokens, effects tracked correctly)

### Content
- All board game cards implemented
- All board game relics implemented
- All board game enemies/Bosses implemented
- All events functional
- Ascension system complete

---

## Development Phases (Suggested)

### Phase 1: Core Combat (MVP)
- Single-player combat with board game rules
- Row system, die rolls, enemy actions
- One character fully functional (Ironclad recommended)
- Basic card set, basic relics
- Win/loss conditions

### Phase 2: Multiplayer Foundation
- 2-player multiplayer combat
- Synchronization system
- Simultaneous turn management
- Basic communication (chat)

### Phase 3: Character Completion
- All 4 characters implemented
- All character-specific mechanics
- Full card sets for each character
- Full relic sets

### Phase 4: Map & Progression
- Map generation and navigation
- Event system
- Merchant system
- Campfire, Treasure rooms
- Reward system
- Between-combat persistence

### Phase 5: Full Multiplayer
- 4-player support
- Player drop handling
- Enhanced communication tools
- Potion trading
- Gold pooling

### Phase 6: Content Completion
- All cards implemented
- All relics implemented
- All enemies/Bosses
- All events
- Summons system

### Phase 7: Progression Systems
- Unlock system
- Ascension mode
- Achievement tracking
- Daily Climb
- Custom Runs

### Phase 8: Polish & Act IV
- Act IV content
- Keys system
- Optional rules (Last Stand, etc.)
- UI/UX polish
- Balance tuning
- Bug fixing

---

## Technical Architecture Notes

### State Management
- **Game State**: Centralized state for entire run
- **Combat State**: Current combat, all players, all enemies
- **Player State**: Individual deck, HP, Energy, Block, resources, relics, potions
- **Enemy State**: HP, Block, tokens, action track, Summons
- **Map State**: Current position, revealed tokens, keys obtained

### Synchronization Requirements
- **Input Synchronization**: All players' card plays must sync
- **RNG Synchronization**: Shared die roll, card shuffles must match
- **State Updates**: All clients see same game state
- **Animation Queueing**: Handle simultaneous actions elegantly

### Data Models
- **Card**: Cost, effects, targets, upgrades, rarity, character
- **Relic**: Effects, triggers, once-per usage tracking
- **Enemy**: HP, actions (Single/Die/Cube), special abilities, Summons
- **Token**: Type (Strength, Block, Vulnerable, etc.), count, owner
- **Player**: Character, deck, HP, resources, position (row)

---

## Conclusion

This mod fundamentally transforms Slay the Spire from a single-player roguelike into a cooperative multiplayer experience. The core challenge is maintaining the strategic depth of the original while introducing meaningful cooperation through the row system, simultaneous turns, and shared die mechanics. Success requires careful attention to both technical multiplayer implementation and faithful recreation of the board game's unique mechanics and balance.
