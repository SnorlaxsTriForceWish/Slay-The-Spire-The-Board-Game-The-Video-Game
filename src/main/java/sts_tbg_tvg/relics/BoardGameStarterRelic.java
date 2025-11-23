package sts_tbg_tvg.relics;

import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import sts_tbg_tvg.characters.BoardGameIronclad;

import static sts_tbg_tvg.StsTbgTvgMod.makeID;

public class BoardGameStarterRelic extends BaseRelic {
    private static final String NAME = "BoardGameStarterRelic";
    public static final String ID = makeID(NAME);

    // Use Burning Blood image from base game
    private static final String IMG_NAME = "burningBlood.png";

    private static final int HEAL_AMOUNT = 6;

    public BoardGameStarterRelic() {
        super(ID, IMG_NAME, BoardGameIronclad.Enums.RED, RelicTier.STARTER, LandingSound.MAGICAL);
    }

    @Override
    public void onVictory() {
        this.flash();
        this.addToTop(new RelicAboveCreatureAction(AbstractDungeon.player, this));
        AbstractDungeon.player.heal(HEAL_AMOUNT);
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }
}
