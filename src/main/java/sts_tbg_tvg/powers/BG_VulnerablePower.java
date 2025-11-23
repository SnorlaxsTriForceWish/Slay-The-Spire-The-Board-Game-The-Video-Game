package sts_tbg_tvg.powers;

import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import sts_tbg_tvg.StsTbgTvgMod;

/**
 * Board Game version of Vulnerable.
 * - Doubles damage dealt to the target
 * - Removes 1 stack per hit
 * - Maximum 3 stacks per target
 * - Maximum 7 stacks globally across all targets
 */
public class BG_VulnerablePower extends BasePower {
    public static final String POWER_ID = StsTbgTvgMod.makeID("BG_Vulnerable");
    private static final int MAX_STACKS_PER_TARGET = 3;
    private static final int MAX_STACKS_GLOBAL = 7;

    public BG_VulnerablePower(AbstractCreature owner, AbstractCreature source, int amount) {
        super(POWER_ID, PowerType.DEBUFF, false, owner, source, amount, true, false);

        // Use the base game's vulnerable icon
        this.loadRegion("vulnerable");

        // Enforce per-target cap
        if (this.amount > MAX_STACKS_PER_TARGET) {
            this.amount = MAX_STACKS_PER_TARGET;
        }

        this.updateDescription();
    }

    @Override
    public void updateDescription() {
        if (this.amount == 1) {
            this.description = DESCRIPTIONS[0] + this.amount + DESCRIPTIONS[1];
        } else {
            this.description = DESCRIPTIONS[0] + this.amount + DESCRIPTIONS[2];
        }
    }

    @Override
    public float atDamageReceive(float damage, DamageInfo.DamageType damageType) {
        // Only double damage if it's an ATTACK type (not thorns, etc.)
        if (damageType == DamageInfo.DamageType.NORMAL) {
            return damage * 2.0f;
        }
        return damage;
    }

    @Override
    public int onAttacked(DamageInfo info, int damageAmount) {
        // Remove 1 stack after taking damage from an attack
        if (damageAmount > 0 && info.type == DamageInfo.DamageType.NORMAL) {
            this.flash();
            this.amount--;
            if (this.amount <= 0) {
                // Remove the power if we run out of stacks
                this.addToTop(new com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction(
                    this.owner, this.owner, this
                ));
            } else {
                this.updateDescription();
            }
        }
        return damageAmount;
    }

    @Override
    public void stackPower(int stackAmount) {
        // Calculate current global stacks
        int globalStacks = getGlobalBG_VulnerableStacks();

        // Calculate how many stacks we can actually add
        int remainingGlobalCapacity = MAX_STACKS_GLOBAL - globalStacks;
        int remainingTargetCapacity = MAX_STACKS_PER_TARGET - this.amount;

        // Take the minimum of what we want to add and what we can add
        int actualStacksToAdd = Math.min(stackAmount, Math.min(remainingGlobalCapacity, remainingTargetCapacity));

        if (actualStacksToAdd > 0) {
            this.fontScale = 8.0F;
            this.amount += actualStacksToAdd;
            this.updateDescription();
        }
    }

    /**
     * Counts the total number of BG_Vulnerable stacks across all targets in combat.
     */
    private int getGlobalBG_VulnerableStacks() {
        int totalStacks = 0;

        // Count stacks on all monsters
        if (AbstractDungeon.getCurrRoom() != null && AbstractDungeon.getCurrRoom().monsters != null) {
            for (AbstractMonster monster : AbstractDungeon.getCurrRoom().monsters.monsters) {
                if (monster != null && !monster.isDeadOrEscaped()) {
                    AbstractPower power = monster.getPower(POWER_ID);
                    if (power != null) {
                        totalStacks += power.amount;
                    }
                }
            }
        }

        return totalStacks;
    }
}
