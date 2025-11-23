package sts_tbg_tvg.cards.boardgame;

import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import sts_tbg_tvg.util.CardStats;

/**
 * Board Game version of Defend.
 * Gains 1 Block (much weaker than base game's 5 block).
 */
public class BG_Defend extends BoardGameCard {
    public static final String ID = makeID("BG_Defend");

    private static final CardStats info = new CardStats(
            AbstractCard.CardColor.RED,
            AbstractCard.CardType.SKILL,
            AbstractCard.CardRarity.BASIC,
            AbstractCard.CardTarget.SELF,
            1  // Cost: 1 energy
    );

    public BG_Defend() {
        super(ID, "Defend_R", info);
        setBlock(1, 1);  // 1 block, +1 on upgrade (becomes 2)

        // Mark as starter card
        tags.add(AbstractCard.CardTags.STARTER_DEFEND);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new GainBlockAction(p, p, block));
    }

    @Override
    public AbstractCard makeCopy() {
        return new BG_Defend();
    }
}
