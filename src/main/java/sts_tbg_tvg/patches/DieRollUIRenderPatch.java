package sts_tbg_tvg.patches;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.ui.panels.TopPanel;
import sts_tbg_tvg.util.DieRollManager;

/**
 * Patch to render the current die roll in the top bar of the screen.
 * Displays "Die: X" where X is the current die roll (1-6).
 */
public class DieRollUIRenderPatch {

    /**
     * Patch TopPanel.render() to draw the die roll value.
     */
    @SpirePatch(
            clz = TopPanel.class,
            method = "render"
    )
    public static class RenderDieValue {
        @SpirePostfixPatch
        public static void Postfix(TopPanel __instance, SpriteBatch sb) {
            // Only render during combat
            if (AbstractDungeon.getCurrRoom() != null &&
                AbstractDungeon.getCurrRoom().monsters != null &&
                !AbstractDungeon.getCurrRoom().monsters.areMonstersBasicallyDead()) {

                int currentRoll = DieRollManager.getCurrentRoll();

                // Only render if a die has been rolled (not 0)
                if (currentRoll > 0) {
                    // Position the text using screen ratios for consistent placement
                    // x: 75% of screen width (right side)
                    // y: 97% of screen height (near the top with 3% margin)
                    float x = Settings.WIDTH * 0.60F;
                    float y = Settings.HEIGHT * 0.97F;

                    String text = "Die: " + currentRoll;

                    // Render the text with a white color
                    FontHelper.renderFontLeftTopAligned(
                            sb,
                            FontHelper.topPanelInfoFont,
                            text,
                            x,
                            y,
                            Color.WHITE
                    );
                }
            }
        }
    }
}
