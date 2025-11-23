package sts_tbg_tvg.patches;

import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.CardLibrary;
import javassist.CtBehavior;
import sts_tbg_tvg.characters.BoardGameIronclad;

/**
 * Patch to ensure Board Game Ironclad receives RED card rewards.
 *
 * The issue: AbstractDungeon.getRewardCards() filters cards by player class,
 * but Board Game Ironclad uses a custom PlayerClass with base game RED CardColor.
 * This patch ensures RED cards are available as rewards for Board Game Ironclad.
 */
@SpirePatch(
        clz = AbstractDungeon.class,
        method = "getRewardCards"
)
public class BoardGameIroncladCardRewardsPatch {

    @SpireInsertPatch(
            locator = Locator.class
    )
    public static void Insert(CardGroup ___srcCommonCardPool,
                            CardGroup ___srcUncommonCardPool,
                            CardGroup ___srcRareCardPool) {
        // If playing as Board Game Ironclad, populate card pools with RED cards
        if (AbstractDungeon.player != null &&
            AbstractDungeon.player.chosenClass == BoardGameIronclad.Enums.BOARD_GAME_IRONCLAD) {

            // Only populate if pools are empty (to avoid duplicates)
            if (___srcCommonCardPool.isEmpty() &&
                ___srcUncommonCardPool.isEmpty() &&
                ___srcRareCardPool.isEmpty()) {

                // Add all RED cards (both base game and custom Board Game cards) to appropriate rarity pools
                for (AbstractCard card : CardLibrary.getAllCards()) {
                    // Include base game RED cards and custom Board Game cards
                    boolean isBaseGameRedCard = card.color == AbstractCard.CardColor.RED;
                    boolean isCustomBoardGameCard = card.cardID != null && card.cardID.startsWith("sts_tbg_tvg:");

                    if ((isBaseGameRedCard || isCustomBoardGameCard) &&
                        card.rarity != AbstractCard.CardRarity.BASIC &&
                        card.rarity != AbstractCard.CardRarity.SPECIAL &&
                        card.rarity != AbstractCard.CardRarity.CURSE) {

                        switch (card.rarity) {
                            case COMMON:
                                ___srcCommonCardPool.addToBottom(card);
                                break;
                            case UNCOMMON:
                                ___srcUncommonCardPool.addToBottom(card);
                                break;
                            case RARE:
                                ___srcRareCardPool.addToBottom(card);
                                break;
                        }
                    }
                }
            }
        }
    }

    private static class Locator extends SpireInsertLocator {
        @Override
        public int[] Locate(CtBehavior ctMethodToPatch) throws Exception {
            // Insert at the very beginning of the method
            return new int[]{0};
        }
    }
}
