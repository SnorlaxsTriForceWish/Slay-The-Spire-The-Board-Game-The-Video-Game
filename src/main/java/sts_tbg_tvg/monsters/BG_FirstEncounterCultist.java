package sts_tbg_tvg.monsters;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.TalkAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.RollMoveAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import sts_tbg_tvg.StsTbgTvgMod;
import sts_tbg_tvg.powers.BG_StrengthPower;

/**
 * Board Game version of First Encounter Cultist.
 * - 9 HP
 * - Every turn: Attack for 1 base damage and gain 1 Strength
 * - Uses the same art and animations as the base game Cultist
 */
public class BG_FirstEncounterCultist extends AbstractMonster {
    public static final String ID = StsTbgTvgMod.makeID("BG_FirstEncounterCultist");
    private static final MonsterStrings monsterStrings = CardCrawlGame.languagePack.getMonsterStrings(ID);
    public static final String NAME = monsterStrings.NAME;
    public static final String[] MOVES = monsterStrings.MOVES;
    public static final String[] DIALOG = monsterStrings.DIALOG;

    // Stats
    private static final int HP = 9;
    private static final int BASE_DAMAGE = 1;
    private static final int STRENGTH_GAIN = 1;

    // Move byte
    private static final byte INCANTATION = 1;

    public BG_FirstEncounterCultist(float x, float y) {
        super(NAME, ID, HP, 0.0F, 0.0F, 230.0F, 240.0F, null, x, y);

        // Use the base game Cultist's atlas and JSON for animations
        // The Cultist enemy ID in the base game is "Cultist"
        this.loadAnimation("images/monsters/theBottom/cultist/skeleton.atlas",
                          "images/monsters/theBottom/cultist/skeleton.json", 1.0F);

        // Set up animation state (base game Cultist uses "waving" animation)
        com.esotericsoftware.spine.AnimationState.TrackEntry e = this.state.setAnimation(0, "waving", true);
        e.setTime(e.getEndTime() * AbstractDungeon.monsterHpRng.random());

        // Set damage
        this.damage.add(new DamageInfo(this, BASE_DAMAGE));
    }

    @Override
    public void takeTurn() {
        switch (this.nextMove) {
            case INCANTATION:
                // Attack the player
                AbstractDungeon.actionManager.addToBottom(
                    new DamageAction(
                        AbstractDungeon.player,
                        this.damage.get(0),
                        AbstractGameAction.AttackEffect.SLASH_HORIZONTAL
                    )
                );

                // Gain 1 Strength using BG_StrengthPower
                AbstractDungeon.actionManager.addToBottom(
                    new ApplyPowerAction(
                        this,
                        this,
                        new BG_StrengthPower(this, this, STRENGTH_GAIN),
                        STRENGTH_GAIN
                    )
                );
                break;
        }

        AbstractDungeon.actionManager.addToBottom(new RollMoveAction(this));
    }

    @Override
    protected void getMove(int num) {
        // Always do the same move: attack and gain strength
        setMove(INCANTATION, Intent.ATTACK_BUFF, BASE_DAMAGE);
    }

    @Override
    public void die() {
        super.die();
        // Could add custom death effects here if needed
    }

    @Override
    public void usePreBattleAction() {
        // Play the Cultist's "CAW" sound and show dialogue at the start of battle
        if (DIALOG.length > 0) {
            // Play the Cultist's signature "CAW" sound effect (randomly chosen from 3 variants)
            int roll = AbstractDungeon.monsterHpRng.random(2);
            String soundKey;
            if (roll == 0) {
                soundKey = "VO_CULTIST_1A";
            } else if (roll == 1) {
                soundKey = "VO_CULTIST_1B";
            } else {
                soundKey = "VO_CULTIST_1C";
            }
            CardCrawlGame.sound.play(soundKey);

            // Show "CAW CAAAW!" dialogue
            AbstractDungeon.actionManager.addToBottom(new TalkAction(this, DIALOG[0], 0.5F, 2.0F));
        }
    }
}
