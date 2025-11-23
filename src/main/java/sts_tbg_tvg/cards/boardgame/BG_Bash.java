package sts_tbg_tvg.cards.boardgame;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import sts_tbg_tvg.powers.BG_VulnerablePower;
import sts_tbg_tvg.util.CardStats;

/**
 * Board Game version of Bash.
 * Deals 2 damage and applies 2 Vulnerable.
 * (Base game version deals 8 damage and applies 2 Vulnerable)
 */
public class BG_Bash extends BoardGameCard {
    public static final String ID = makeID("BG_Bash");

    private static final int VULNERABLE_AMOUNT = 1;

    private static final CardStats info = new CardStats(
            AbstractCard.CardColor.RED,
            AbstractCard.CardType.ATTACK,
            AbstractCard.CardRarity.BASIC,
            AbstractCard.CardTarget.ENEMY,
            2  // Cost: 2 energy
    );

    public BG_Bash() {
        super(ID, "Bash", info);
        setDamage(2, 1);  // 2 damage, +1 on upgrade (becomes 3)
        setMagic(VULNERABLE_AMOUNT, 1);  // 2 vulnerable, +1 on upgrade (becomes 3)
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        // Deal damage
        addToBot(new DamageAction(m, new DamageInfo(p, damage, damageTypeForTurn),
                AbstractGameAction.AttackEffect.BLUNT_HEAVY));

        // Apply BG_Vulnerable
        addToBot(new ApplyPowerAction(m, p, new BG_VulnerablePower(m, p, magicNumber), magicNumber));
    }

    @Override
    public AbstractCard makeCopy() {
        return new BG_Bash();
    }
}
