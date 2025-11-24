package sts_tbg_tvg.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import sts_tbg_tvg.StsTbgTvgMod;
import sts_tbg_tvg.util.DieRollManager;

/**
 * Patches to manage the per-turn die roll system.
 * Automatically rolls a die at the start of each player turn.
 */
public class DieRollPatch {

    /**
     * Patch AbstractPlayer.applyStartOfTurnRelics() to roll the die at the start of each turn.
     * This method is called at the beginning of the player's turn, before any other turn logic.
     */
    @SpirePatch(
            clz = AbstractPlayer.class,
            method = "applyStartOfTurnRelics"
    )
    public static class RollDieAtTurnStart {
        @SpirePostfixPatch
        public static void Postfix(AbstractPlayer __instance) {
            // Only roll if we're in combat and it's the player's turn
            if (AbstractDungeon.getCurrRoom() != null &&
                AbstractDungeon.getCurrRoom().monsters != null &&
                !AbstractDungeon.getCurrRoom().monsters.areMonstersBasicallyDead()) {

                int roll = DieRollManager.rollDie();
                StsTbgTvgMod.logger.info("Die rolled: " + roll);
            }
        }
    }

    /**
     * Reset the die roll when combat ends.
     */
    @SpirePatch(
            clz = AbstractDungeon.class,
            method = "reset"
    )
    public static class ResetDieOnRunStart {
        @SpirePostfixPatch
        public static void Postfix() {
            DieRollManager.reset();
        }
    }
}
