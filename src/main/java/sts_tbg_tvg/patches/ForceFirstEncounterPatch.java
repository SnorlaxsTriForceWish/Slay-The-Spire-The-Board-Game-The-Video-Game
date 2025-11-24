package sts_tbg_tvg.patches;

import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.MonsterHelper;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.MonsterGroup;
import javassist.CtBehavior;
import sts_tbg_tvg.monsters.BG_FirstEncounterCultist;

/**
 * Patch to force the first combat encounter to always be BG_FirstEncounterCultist.
 *
 * This hooks into MonsterHelper.getEncounter() to intercept the encounter selection
 * BEFORE the game logs which monster was selected, ensuring clean logs.
 */
public class ForceFirstEncounterPatch {

    // Track whether we've had our first combat yet
    private static boolean hasHadFirstCombat = false;

    /**
     * Patch MonsterHelper.getEncounter() to return our custom encounter for the first fight.
     * This intercepts the encounter selection before logging occurs.
     */
    @SpirePatch(
            clz = MonsterHelper.class,
            method = "getEncounter",
            paramtypez = {String.class}
    )
    public static class InterceptEncounterSelection {
        @SpirePrefixPatch
        public static SpireReturn<MonsterGroup> Prefix(String key) {
            // Check if this is the first combat encounter in Act 1
            if (!hasHadFirstCombat &&
                AbstractDungeon.player != null &&
                AbstractDungeon.currMapNode != null &&
                AbstractDungeon.actNum == 1) {

                // Mark that we've had our first combat
                hasHadFirstCombat = true;

                // Return our custom encounter directly, bypassing the normal selection
                // Position the cultist in the center of the screen
                return SpireReturn.Return(new MonsterGroup(
                    new AbstractMonster[] {
                        new BG_FirstEncounterCultist(-50.0F, 0.0F)
                    }
                ));
            }

            // Not the first encounter, let the game proceed normally
            return SpireReturn.Continue();
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
