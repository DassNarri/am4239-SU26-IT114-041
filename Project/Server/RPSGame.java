package Project.Server;

import Project.Common.Move;

/** Server-side model tracking a single RPS match state between two players. */

// Date: July 20th   |    UCID: am4239
// Created file: 
//
// Created the server-side RPSGame state model to track game ID, participant IDs, and submitted moves.
// Implemented helper methods for move submission prevention, game completion checks, 
// participant lookups, and win/tie evaluation logic.

public class RPSGame {
    private static long nextGameId = 1;

    private final long gameId;
    private final long playerAId;
    private final long playerBId;
    private Move playerAMove;
    private Move playerBMove;

    public RPSGame(long playerAId, long playerBId) {
        this.gameId = synchronizedNextGameId();
        this.playerAId = playerAId;
        this.playerBId = playerBId;
    }

    private static synchronized long synchronizedNextGameId() {
        return nextGameId++;
    }

    public long getGameId() {
        return gameId;
    }

    public long getPlayerAId() {
        return playerAId;
    }

    public long getPlayerBId() {
        return playerBId;
    }

    public boolean isParticipant(long clientId) {
        return clientId == playerAId || clientId == playerBId;
    }

    public long getOpponentId(long playerId) {
        if (playerId == playerAId) return playerBId;
        if (playerId == playerBId) return playerAId;
        return -1;
    }

    public Move getOpponentMove(long playerId) {
        if (playerId == playerAId) return playerBMove;
        if (playerId == playerBId) return playerAMove;
        return null;
    }

    /**
     * Sets move for the given player. Rejects if player already made a move.
     * @return true if move was accepted, else false
     */
    public boolean updateMove(long playerId, Move move) {
        if (playerId == playerAId) {
            if (playerAMove != null) return false; // already submitted
            playerAMove = move;
            return true;
        } else if (playerId == playerBId) {
            if (playerBMove != null) return false; // already submitted
            playerBMove = move;
            return true;
        }
        return false;
    }

    /** Checks if both participants have submitted their moves. */
    public boolean isComplete() {
        return playerAMove != null && playerBMove != null;
    }

    /**
     * Win logic, called only after isComplete() returns true.
     * @return true if playerId won, false if tie or lost
     */
    public boolean playerWins(long playerId) {
        if (!isComplete()) return false;

        Move myMove = (playerId == playerAId) ? playerAMove : playerBMove;
        Move opponentMove = getOpponentMove(playerId);

        if (myMove == opponentMove) return false; // Tie

        return (myMove == Move.ROCK && opponentMove == Move.SCISSORS) ||
               (myMove == Move.PAPER && opponentMove == Move.ROCK) ||
               (myMove == Move.SCISSORS && opponentMove == Move.PAPER);
    }
}