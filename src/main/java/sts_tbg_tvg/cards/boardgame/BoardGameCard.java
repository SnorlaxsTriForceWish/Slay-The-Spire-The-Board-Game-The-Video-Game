package sts_tbg_tvg.cards.boardgame;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.helpers.CardLibrary;
import sts_tbg_tvg.cards.BaseCard;
import sts_tbg_tvg.util.CardStats;

/**
 * Abstract base class for Board Game Ironclad cards.
 * These cards reuse existing base game Ironclad card art but have different stats and effects.
 *
 * Usage example:
 * <pre>
 * public class BG_HeavyBlow extends BoardGameCard {
 *     public static final String ID = makeID("BG_HeavyBlow");
 *     private static final CardStats info = new CardStats(
 *         CardColor.RED, CardType.ATTACK, CardRarity.COMMON, CardTarget.ENEMY, 2
 *     );
 *
 *     public BG_HeavyBlow() {
 *         super(ID, "Heavy Blade", info);
 *         setDamage(12, 4);
 *     }
 *
 *     @Override
 *     public void use(AbstractPlayer p, AbstractMonster m) {
 *         addToBot(new DamageAction(m, new DamageInfo(p, damage)));
 *         addToBot(new DrawCardAction(1));
 *     }
 * }
 * </pre>
 */
public abstract class BoardGameCard extends BaseCard {

    /**
     * Creates a Board Game card that reuses art from a base game card.
     *
     * @param cardID The unique ID for this custom card (e.g., "sts_tbg_tvg:BG_HeavyBlow")
     * @param referenceCardID The base game card ID to copy art from (e.g., "Strike_R", "Defend_R", "Bash", "Heavy Blade")
     * @param info Card statistics (cost, type, rarity, target, color)
     */
    public BoardGameCard(String cardID, String referenceCardID, CardStats info) {
        super(cardID, info);

        // Get the portrait from the base game card
        TextureAtlas.AtlasRegion baseCardPortrait = getBaseGameCardPortrait(referenceCardID);
        if (baseCardPortrait != null) {
            this.portrait = baseCardPortrait;
            this.jokePortrait = baseCardPortrait; // Also set joke portrait
        }
    }

    /**
     * Gets the portrait (texture) from a base game Ironclad card.
     *
     * @param referenceCardID The base game card ID (e.g., "Strike_R", "Defend_R", "Bash", "Heavy Blade")
     * @return The TextureAtlas.AtlasRegion from the reference card, or null if not found
     */
    private static TextureAtlas.AtlasRegion getBaseGameCardPortrait(String referenceCardID) {
        // Try to get the card from CardLibrary
        AbstractCard referenceCard = CardLibrary.getCard(referenceCardID);

        if (referenceCard != null && referenceCard.portrait != null) {
            return referenceCard.portrait;
        }

        // If not found, try with common Ironclad suffixes
        if (!referenceCardID.endsWith("_R")) {
            referenceCard = CardLibrary.getCard(referenceCardID + "_R");
            if (referenceCard != null && referenceCard.portrait != null) {
                return referenceCard.portrait;
            }
        }

        // Log warning if we couldn't find the reference card
        if (referenceCard == null) {
            System.err.println("WARNING: Could not find base game card '" + referenceCardID + "' to copy portrait from.");
        }

        return null;
    }
}
