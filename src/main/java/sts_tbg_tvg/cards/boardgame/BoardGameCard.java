package sts_tbg_tvg.cards.boardgame;

import com.megacrit.cardcrawl.cards.AbstractCard;
import sts_tbg_tvg.cards.BaseCard;
import sts_tbg_tvg.util.CardStats;

import java.util.Locale;

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
 *         super(ID, "Heavy_Blade", info);
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
     * @param referenceCardID The base game card ID to copy art from (e.g., "Strike_R", "Heavy_Blade")
     * @param info Card statistics (cost, type, rarity, target, color)
     */
    public BoardGameCard(String cardID, String referenceCardID, CardStats info) {
        super(cardID, info, getBaseGameCardTexture(referenceCardID, info.cardType));
    }

    /**
     * Generates the texture path for a base game Ironclad card.
     *
     * Base game card images are stored at: images/1024/cards/red/[type]/[cardname].png
     *
     * @param referenceCardID The base game card ID (e.g., "Strike_R", "Heavy_Blade", "Bash")
     * @param cardType The card type (ATTACK, SKILL, POWER)
     * @return Full path to the base game card texture
     */
    private static String getBaseGameCardTexture(String referenceCardID, AbstractCard.CardType cardType) {
        // Remove common suffixes and convert to texture name
        String textureName = referenceCardID
            .replace("_R", "")      // Remove Ironclad suffix (Strike_R -> Strike)
            .replace("_Red", "")    // Remove alternate suffix
            .toLowerCase(Locale.ROOT);

        // Convert CamelCase or PascalCase to snake_case
        // e.g., "HeavyBlade" -> "heavy_blade"
        textureName = textureName.replaceAll("([a-z])([A-Z])", "$1_$2").toLowerCase(Locale.ROOT);

        // Determine subdirectory based on card type
        String typeDir;
        switch (cardType) {
            case ATTACK:
                typeDir = "attack";
                break;
            case POWER:
                typeDir = "power";
                break;
            case SKILL:
            default:
                typeDir = "skill";
                break;
        }

        // Construct the full path to base game card image
        // Format: images/1024/cards/red/[type]/[name].png
        return String.format("images/1024/cards/red/%s/%s.png", typeDir, textureName);
    }
}
