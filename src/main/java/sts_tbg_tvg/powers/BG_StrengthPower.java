package sts_tbg_tvg.powers;

import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import sts_tbg_tvg.StsTbgTvgMod;

/**
 * Board Game version of Strength.
 * - Each point adds 1 damage to each hit dealt by the target
 * - Maximum 8 stacks per target
 * - Token-based resource system:
 *   - 12 tokens worth 1 strength each (12 total from 1-tokens)
 *   - 1 token worth 5 strength (5 total from 5-token)
 *   - Maximum 17 strength globally across all targets
 * - When a target reaches 5+ strength, preferentially use the 5-token
 * - When a target drops below 5 strength, swap 5-token for 1-tokens
 */
public class BG_StrengthPower extends BasePower {
    public static final String POWER_ID = StsTbgTvgMod.makeID("BG_Strength");
    private static final int MAX_STACKS_PER_TARGET = 8;
    private static final int MAX_ONE_TOKENS = 12;
    private static final int FIVE_TOKEN_VALUE = 5;
    private static final int MAX_GLOBAL_STRENGTH = MAX_ONE_TOKENS + FIVE_TOKEN_VALUE; // 17

    // Track if this specific power instance is using the 5-token
    private boolean usingFiveToken = false;

    public BG_StrengthPower(AbstractCreature owner, AbstractCreature source, int amount) {
        super(POWER_ID, PowerType.BUFF, false, owner, source, amount, true, true);

        // Enforce per-target cap
        if (this.amount > MAX_STACKS_PER_TARGET) {
            this.amount = MAX_STACKS_PER_TARGET;
        }

        // Check if we should be using the 5-token
        optimizeTokenUsage();

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
    public float atDamageGive(float damage, DamageInfo.DamageType damageType) {
        // Add strength to outgoing damage from attacks
        if (damageType == DamageInfo.DamageType.NORMAL) {
            return damage + this.amount;
        }
        return damage;
    }

    @Override
    public void stackPower(int stackAmount) {
        if (stackAmount <= 0) {
            return;
        }

        // Calculate current token usage
        TokenUsage usage = calculateGlobalTokenUsage();

        // Calculate available strength from tokens
        int availableStrength = MAX_GLOBAL_STRENGTH - usage.totalStrengthUsed;

        // Calculate how much we can add to this target
        int remainingTargetCapacity = MAX_STACKS_PER_TARGET - this.amount;
        int actualStacksToAdd = Math.min(stackAmount, Math.min(availableStrength, remainingTargetCapacity));

        if (actualStacksToAdd > 0) {
            this.fontScale = 8.0F;
            this.amount += actualStacksToAdd;

            // Optimize token usage after adding strength
            optimizeTokenUsage();

            this.updateDescription();
        }
    }

    @Override
    public void reducePower(int reduceAmount) {
        this.fontScale = 8.0F;
        this.amount -= reduceAmount;

        if (this.amount <= 0) {
            this.addToTop(new com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction(
                this.owner, this.owner, this
            ));
        } else {
            // Optimize token usage after reducing strength
            optimizeTokenUsage();
            this.updateDescription();
        }
    }

    /**
     * Optimizes token usage for this power instance.
     * If this target has >= 5 strength and the 5-token is available, use it.
     * If this target has < 5 strength and is using the 5-token, swap to 1-tokens.
     */
    private void optimizeTokenUsage() {
        if (this.amount >= FIVE_TOKEN_VALUE) {
            // Try to use the 5-token if available
            if (!usingFiveToken) {
                BG_StrengthPower fiveTokenOwner = getFiveTokenOwner();
                if (fiveTokenOwner == null) {
                    // 5-token is available, claim it
                    usingFiveToken = true;
                } else if (fiveTokenOwner.amount < FIVE_TOKEN_VALUE) {
                    // Current owner dropped below 5, steal the token
                    fiveTokenOwner.usingFiveToken = false;
                    usingFiveToken = true;
                }
            }
        } else {
            // Below 5 strength, release the 5-token if we have it
            if (usingFiveToken) {
                usingFiveToken = false;

                // Check if another target with >= 5 strength can claim it
                redistributeFiveToken();
            }
        }
    }

    /**
     * After releasing the 5-token, check if another target can claim it.
     */
    private void redistributeFiveToken() {
        // Find a target with >= 5 strength that isn't using the 5-token
        for (AbstractMonster monster : getAllCreaturesInCombat()) {
            if (monster != null && !monster.isDeadOrEscaped() && monster != this.owner) {
                BG_StrengthPower power = (BG_StrengthPower) monster.getPower(POWER_ID);
                if (power != null && power.amount >= FIVE_TOKEN_VALUE && !power.usingFiveToken) {
                    power.usingFiveToken = true;
                    return;
                }
            }
        }

        // Check player
        if (AbstractDungeon.player != null && AbstractDungeon.player != this.owner) {
            BG_StrengthPower power = (BG_StrengthPower) AbstractDungeon.player.getPower(POWER_ID);
            if (power != null && power.amount >= FIVE_TOKEN_VALUE && !power.usingFiveToken) {
                power.usingFiveToken = true;
            }
        }
    }

    /**
     * Finds the creature that is currently using the 5-token, or null if it's available.
     */
    private BG_StrengthPower getFiveTokenOwner() {
        // Check all monsters
        for (AbstractMonster monster : getAllCreaturesInCombat()) {
            if (monster != null && !monster.isDeadOrEscaped()) {
                BG_StrengthPower power = (BG_StrengthPower) monster.getPower(POWER_ID);
                if (power != null && power.usingFiveToken) {
                    return power;
                }
            }
        }

        // Check player
        if (AbstractDungeon.player != null) {
            AbstractPower power = AbstractDungeon.player.getPower(POWER_ID);
            if (power instanceof BG_StrengthPower && ((BG_StrengthPower) power).usingFiveToken) {
                return (BG_StrengthPower) power;
            }
        }

        return null;
    }

    /**
     * Calculates global token usage across all creatures in combat.
     */
    private TokenUsage calculateGlobalTokenUsage() {
        TokenUsage usage = new TokenUsage();

        // Check all monsters
        for (AbstractMonster monster : getAllCreaturesInCombat()) {
            if (monster != null && !monster.isDeadOrEscaped()) {
                BG_StrengthPower power = (BG_StrengthPower) monster.getPower(POWER_ID);
                if (power != null) {
                    if (power.usingFiveToken) {
                        usage.fiveTokenUsed = true;
                        usage.totalStrengthUsed += power.amount;
                    } else {
                        usage.oneTokensUsed += power.amount;
                        usage.totalStrengthUsed += power.amount;
                    }
                }
            }
        }

        // Check player
        if (AbstractDungeon.player != null) {
            AbstractPower power = AbstractDungeon.player.getPower(POWER_ID);
            if (power instanceof BG_StrengthPower) {
                BG_StrengthPower bgPower = (BG_StrengthPower) power;
                if (bgPower.usingFiveToken) {
                    usage.fiveTokenUsed = true;
                    usage.totalStrengthUsed += bgPower.amount;
                } else {
                    usage.oneTokensUsed += bgPower.amount;
                    usage.totalStrengthUsed += bgPower.amount;
                }
            }
        }

        return usage;
    }

    /**
     * Gets all creatures currently in combat (monsters only, since we check player separately).
     */
    private Iterable<AbstractMonster> getAllCreaturesInCombat() {
        if (AbstractDungeon.getCurrRoom() != null && AbstractDungeon.getCurrRoom().monsters != null) {
            return AbstractDungeon.getCurrRoom().monsters.monsters;
        }
        return new java.util.ArrayList<>();
    }

    /**
     * Helper class to track token usage.
     */
    private static class TokenUsage {
        int oneTokensUsed = 0;
        boolean fiveTokenUsed = false;
        int totalStrengthUsed = 0;
    }
}
