package sts_tbg_tvg.cards.boardgame;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import sts_tbg_tvg.util.CardStats;

/**
 * Board Game version of Strike.
 * Deals 1 damage (much weaker than base game's 6 damage).
 */
public class BG_Strike extends BoardGameCard {
    public static final String ID = makeID("BG_Strike");

    private static final CardStats info = new CardStats(
            AbstractCard.CardColor.RED,
            AbstractCard.CardType.ATTACK,
            AbstractCard.CardRarity.BASIC,
            AbstractCard.CardTarget.ENEMY,
            1  // Cost: 1 energy
    );

    public BG_Strike() {
        super(ID, "Strike_R", info);
        setDamage(1, 1);  // 1 damage, +1 on upgrade (becomes 2)

        // Mark as starter card
        tags.add(AbstractCard.CardTags.STARTER_STRIKE);
        tags.add(AbstractCard.CardTags.STRIKE);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new DamageAction(m, new DamageInfo(p, damage, damageTypeForTurn),
                AbstractGameAction.AttackEffect.BLUNT_LIGHT));
    }

    @Override
    public AbstractCard makeCopy() {
        return new BG_Strike();
    }
}
