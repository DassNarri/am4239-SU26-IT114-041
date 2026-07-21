package Project.Common;

// Date: July 20th   |    UCID: am4239
// Created file: 
//
// Created the RPSPayload class extending Payload to hold game-specific data including 
// targetUser, move, and accepted status.

public class RPSPayload extends Payload {
    private long targetUser;
    private Move move;
    private boolean accepted;

    public long getTargetUser() {
        return targetUser;
    }

    public void setTargetUser(long targetUser) {
        this.targetUser = targetUser;
    }

    public Move getMove() {
        return move;
    }

    public void setMove(Move move) {
        this.move = move;
    }

    public boolean isAccepted() {
        return accepted;
    }

    public void setAccepted(boolean accepted) {
        this.accepted = accepted;
    }

    @Override
    public String toString() {
        return super.toString() +
                String.format(" TargetUser: [%d] Move: [%s] Accepted: [%b]",
                        getTargetUser(),getMove(),isAccepted());
    }
}