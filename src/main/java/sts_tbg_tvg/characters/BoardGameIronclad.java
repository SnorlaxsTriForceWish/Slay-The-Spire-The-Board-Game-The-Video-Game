package sts_tbg_tvg.characters;

import basemod.BaseMod;
import basemod.abstracts.CustomPlayer;
import basemod.animations.AbstractAnimation;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.esotericsoftware.spine.AnimationState;
import com.evacipated.cardcrawl.modthespire.lib.SpireEnum;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.EnergyManager;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.cutscenes.CutscenePanel;
import com.megacrit.cardcrawl.events.city.Vampires;
import com.megacrit.cardcrawl.helpers.CardLibrary;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.helpers.ScreenShake;
import com.megacrit.cardcrawl.localization.CharacterStrings;
import com.megacrit.cardcrawl.screens.CharSelectInfo;
import com.megacrit.cardcrawl.unlock.UnlockTracker;
import sts_tbg_tvg.StsTbgTvgMod;

import java.util.ArrayList;

import static sts_tbg_tvg.StsTbgTvgMod.characterPath;
import static sts_tbg_tvg.StsTbgTvgMod.makeID;

public class BoardGameIronclad extends CustomPlayer {
    // Character ID and Card Color - must match
    public static class Enums {
        @SpireEnum
        public static AbstractPlayer.PlayerClass BOARD_GAME_IRONCLAD;
        @SpireEnum(name = "BOARD_GAME_RED")
        public static AbstractCard.CardColor BOARD_GAME_RED;
        @SpireEnum(name = "BOARD_GAME_RED")
        public static CardLibrary.LibraryType BOARD_GAME_LIBRARY_TYPE;
    }

    // Character-specific static values
    private static final int ENERGY_PER_TURN = 3;
    private static final int MAX_HP = 10;
    private static final int STARTING_GOLD = 99;
    private static final int CARD_DRAW = 5;
    private static final int ORB_SLOTS = 0;

    // Localization strings
    private static final String ID = makeID("BoardGameIronclad");
    private static CharacterStrings characterStrings;
    private static String[] NAMES;
    private static String[] TEXT;

    // Lazy load character strings when first needed
    private static void loadStrings() {
        if (characterStrings == null) {
            characterStrings = CardCrawlGame.languagePack.getCharacterString(ID);
            NAMES = characterStrings.NAMES;
            TEXT = characterStrings.TEXT;
        }
    }

    // Image paths - we'll use base game Ironclad assets
    private static final String SHOULDER_1 = "images/characters/ironclad/shoulder.png";
    private static final String SHOULDER_2 = "images/characters/ironclad/shoulder2.png";
    private static final String CORPSE = "images/characters/ironclad/corpse.png";
    private static final String SKELETON_ATLAS = "images/characters/ironclad/idle/skeleton.atlas";
    private static final String SKELETON_JSON = "images/characters/ironclad/idle/skeleton.json";

    // Character select screen assets
    private static final String BUTTON = "images/ui/charSelect/ironcladButton.png";
    private static final String PORTRAIT = "images/ui/charSelect/ironcladPortrait.jpg";

    // Card backgrounds - we'll use the base red color card backgrounds
    private static final String BG_ATTACK_512 = "images/512/bg_attack_red.png";
    private static final String BG_SKILL_512 = "images/512/bg_skill_red.png";
    private static final String BG_POWER_512 = "images/512/bg_power_red.png";
    private static final String BG_ENERGY_512 = "images/512/bg_energy_red.png";
    private static final String BG_ATTACK_1024 = "images/1024/bg_attack_red.png";
    private static final String BG_SKILL_1024 = "images/1024/bg_skill_red.png";
    private static final String BG_POWER_1024 = "images/1024/bg_power_red.png";
    private static final String BG_ENERGY_1024 = "images/1024/bg_energy_red.png";

    // Energy orb assets
    private static final String ORB_VFX = "images/ui/topPanel/energyOrbRed.png";

    public BoardGameIronclad(String name) {
        super(name, Enums.BOARD_GAME_IRONCLAD,
              new String[] { SHOULDER_1, SHOULDER_2, SHOULDER_1 },
              CORPSE,
              new AbstractAnimation() {
                  @Override
                  public Type type() {
                      return Type.NONE;
                  }

                  @Override
                  public void renderSprite(SpriteBatch sb, float x, float y) {
                      // Empty - will be handled by skeleton animation
                  }
              });

        // Load strings before creating loadout
        loadStrings();

        // Initialize class with all the character properties
        initializeClass(
                null, // Image not needed since we're using skeleton
                SHOULDER_2, SHOULDER_1,
                CORPSE,
                getLoadout(), // Now we can call instance method
                0.0F, 0.0F, 200.0F, 250.0F,
                new EnergyManager(ENERGY_PER_TURN)
        );

        // Load animation from Ironclad skeleton files
        loadAnimation(SKELETON_ATLAS, SKELETON_JSON, 1.0f);

        // Set up dialog position
        this.dialogX = (this.drawX + 0.0F * Settings.scale);
        this.dialogY = (this.drawY + 220.0F * Settings.scale);
    }

    @Override
    public ArrayList<String> getStartingDeck() {
        ArrayList<String> retVal = new ArrayList<>();

        // Standard Ironclad starting deck
        // 5 Strikes
        for (int i = 0; i < 5; i++) {
            retVal.add("Strike_R");
        }
        // 4 Defends
        for (int i = 0; i < 4; i++) {
            retVal.add("Defend_R");
        }
        // 1 Bash
        retVal.add("Bash");

        return retVal;
    }

    @Override
    public AbstractCard getStartCardForEvent() {
        return new com.megacrit.cardcrawl.cards.red.Strike_Red();
    }

    @Override
    public ArrayList<String> getStartingRelics() {
        ArrayList<String> retVal = new ArrayList<>();
        // We'll create a custom starting relic
        retVal.add(makeID("BoardGameStarterRelic"));
        UnlockTracker.markRelicAsSeen(makeID("BoardGameStarterRelic"));
        return retVal;
    }

    @Override
    public CharSelectInfo getLoadout() {
        loadStrings(); // Ensure strings are loaded before accessing NAMES/TEXT
        return new CharSelectInfo(
                NAMES[0], // Character name
                TEXT[0],  // Character description
                MAX_HP,   // Starting HP
                MAX_HP,   // Max HP
                ORB_SLOTS, // Orb slots
                STARTING_GOLD, // Starting gold
                CARD_DRAW, // Card draw per turn
                this,
                getStartingRelics(),
                getStartingDeck(),
                false
        );
    }

    @Override
    public String getTitle(PlayerClass playerClass) {
        loadStrings();
        return NAMES[1];
    }

    @Override
    public AbstractCard.CardColor getCardColor() {
        return Enums.BOARD_GAME_RED;
    }

    @Override
    public Color getCardRenderColor() {
        return Color.SCARLET;
    }

    @Override
    public String getAchievementKey() {
        return "BOARD_GAME_IRONCLAD";
    }

    @Override
    public ArrayList<AbstractCard> getCardPool(ArrayList<AbstractCard> tmpPool) {
        // For now, we'll return empty - eventually we'll add custom cards
        // Or we could include base Ironclad cards
        return new ArrayList<>();
    }

    @Override
    public Color getCardTrailColor() {
        return Color.RED.cpy();
    }

    @Override
    public String getLeaderboardCharacterName() {
        return "BOARD_GAME_IRONCLAD";
    }

    @Override
    public Texture getEnergyImage() {
        return ImageMaster.RED_ORB_FLASH_VFX;
    }

    @Override
    public int getAscensionMaxHPLoss() {
        return 4;
    }

    @Override
    public BitmapFont getEnergyNumFont() {
        return FontHelper.energyNumFontRed;
    }

    @Override
    public void doCharSelectScreenSelectEffect() {
        CardCrawlGame.sound.playA("ATTACK_HEAVY", MathUtils.random(-0.2F, 0.2F));
        CardCrawlGame.screenShake.shake(ScreenShake.ShakeIntensity.MED, ScreenShake.ShakeDur.SHORT, true);
    }

    @Override
    public String getCustomModeCharacterButtonSoundKey() {
        return "ATTACK_HEAVY";
    }

    @Override
    public String getLocalizedCharacterName() {
        loadStrings();
        return NAMES[0];
    }

    @Override
    public AbstractPlayer newInstance() {
        return new BoardGameIronclad(name);
    }

    @Override
    public String getSpireHeartText() {
        loadStrings();
        return TEXT[1];
    }

    @Override
    public Color getSlashAttackColor() {
        return Color.RED;
    }

    @Override
    public AbstractGameAction.AttackEffect[] getSpireHeartSlashEffect() {
        return new AbstractGameAction.AttackEffect[]{
                AbstractGameAction.AttackEffect.FIRE,
                AbstractGameAction.AttackEffect.BLUNT_HEAVY,
                AbstractGameAction.AttackEffect.FIRE
        };
    }

    @Override
    public String getVampireText() {
        loadStrings();
        return TEXT[2];
    }

    // Meta class for registration
    public static class Meta {
        public static void registerColor() {
            BaseMod.addColor(
                    Enums.BOARD_GAME_RED,
                    Color.RED,
                    Color.RED,
                    Color.RED,
                    Color.RED,
                    Color.RED,
                    Color.RED,
                    Color.RED,
                    BG_ATTACK_512, BG_SKILL_512, BG_POWER_512, BG_ENERGY_512,
                    BG_ATTACK_1024, BG_SKILL_1024, BG_POWER_1024, BG_ENERGY_1024,
                    ORB_VFX
            );
        }

        public static void registerCharacter() {
            loadStrings(); // Ensure strings are loaded before accessing NAMES
            BaseMod.addCharacter(
                    new BoardGameIronclad(NAMES[0]),
                    BUTTON,
                    PORTRAIT,
                    Enums.BOARD_GAME_IRONCLAD
            );
        }
    }
}
