package org.continuouspoker.dealer.game;

import org.continuouspoker.dealer.GameLogger;
import org.continuouspoker.dealer.data.Player;
import org.continuouspoker.dealer.data.Table;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class BetDecision {
    private static final String ALL_IN_TEXT = "Player %s goes all in with %s.";

    private final long gameId;
    private final GameLogger logger;

    /* package */ BetDecision(final long gameId, final GameLogger logger) {
        this.gameId = gameId;
        this.logger = logger;
    }

    /* package */ Action performAction(final Table table, final Player player, final int bet) {
        if (noPlayerHasBetYet(table)) {
            if (bet >= table.getMinimumBet()) {
                return bet(table, player, bet);
            } else {
                return check(table, player);
            }
        } else {
            if (bet >= table.getMinimumRaise()) {
                return raise(table, player, bet);
            } else if (bet >= table.getMinimumBet()) {
                if (bet == player.getCurrentBet()) {
                    return check(table, player);
                } else {
                    return call(table, player);
                }
            } else {
                return fold(table, player);
            }
        }
    }

    private boolean playerCanPayIt(final Table table, final Player player, final int bet) {
        if (player.isGoingAllIn(bet)) {
            player.bet(bet);
            logger.log(gameId, table.getTableId(), table.getRound(), ALL_IN_TEXT, player.getName(), player.getCurrentBet());
            return false;
        }
        return true;
    }

    private Action raise(final Table table, final Player player, final int bet) {
        if (playerCanPayIt(table, player, bet)) {
            logger.log(gameId, table.getTableId(), table.getRound(), "Player %s raises to %s.", player.getName(), bet);
            player.bet(bet);
        }
        return Action.RAISE;
    }

    private Action call(final Table table, final Player player) {
        if (playerCanPayIt(table, player, table.getMinimumBet())) {
            logger.log(gameId, table.getTableId(), table.getRound(), "Player %s calls the bet of %s.", player.getName(),
                    table.getMinimumBet());
            player.bet(table.getMinimumBet());
        }
        return Action.CALL;
    }

    private Action bet(final Table table, final Player player, final int bet) {
        if (playerCanPayIt(table, player, bet)) {
            logger.log(gameId, table.getTableId(), table.getRound(), "Player %s bets %s.", player.getName(), bet);
            player.bet(bet);
        }
        return Action.BET;
    }

    private Action fold(final Table table, final Player player) {
        logger.log(gameId, table.getTableId(), table.getRound(), "Player %s folds.", player.getName());
        player.fold();
        return Action.FOLD;
    }

    private Action check(final Table table, final Player player) {
        logger.log(gameId, table.getTableId(), table.getRound(), "Player %s checks.", player.getName());
        return Action.CHECK;
    }

    private boolean noPlayerHasBetYet(final Table table) {
        return table.getPlayers().stream().mapToInt(Player::getCurrentBet).sum() == 0;
    }

}