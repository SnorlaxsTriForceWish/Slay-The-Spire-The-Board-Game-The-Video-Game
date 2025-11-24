package sts_tbg_tvg.patches;

import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.MonsterGroup;
import javassist.CtBehavior;
import sts_tbg_tvg.monsters.BG_FirstEncounterCultist;

/**
 * Patch to force the first combat encounter to always be BG_FirstEncounterCultist.
 *
 * This hooks into MonsterGroup initialization to detect the first combat and
 * replace the monster group with our custom cultist encounter.
 */
public class ForceFirstEncounterPatch {

    // Track whether we've had our first combat yet
    private static boolean hasHadFirstCombat = false;

    /**
     * Patch the MonsterGroup constructor to override the first encounter.
     */
    @SpirePatch(
            clz = MonsterGroup.class,
            method = SpirePatch.CONSTRUCTOR,
            paramtypez = {AbstractMonster[].class}
    )
    public static class OverrideFirstEncounter {
        @SpirePrefixPatch
        public static void Prefix(MonsterGroup __instance, @ByRef AbstractMonster[][] monsters) {
            // Check if this is the first combat encounter
            // Verify we have a player, we're in Act 1, and we haven't had first combat yet
            if (!hasHadFirstCombat &&
                AbstractDungeon.player != null &&
                AbstractDungeon.currMapNode != null &&
                AbstractDungeon.actNum == 1) {

                // Replace with our custom first encounter
                // Position the cultist in the center of the screen
                monsters[0] = new AbstractMonster[] {
                    new BG_FirstEncounterCultist(-50.0F, 0.0F)
                };

                // Mark that we've had our first combat
                hasHadFirstCombat = true;
            }
        }
    }

    /**
     * Reset the flag when starting a new run.
     */
    @SpirePatch(
            clz = AbstractDungeon.class,
            method = "reset"
    )
    public static class ResetFirstCombatFlag {
        @SpirePostfixPatch
        public static void Postfix() {
            hasHadFirstCombat = false;
        }
    }
}
