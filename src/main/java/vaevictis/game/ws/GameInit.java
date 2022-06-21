package vaevictis.game.ws;

import java.util.ArrayList;
import java.util.List;

import vaevictis.game.Game;
import vaevictis.game.boards.action.ActionBoard;
import vaevictis.game.turn.Turn;
import vaevictis.game.turn.ActionPhase.ActionPhaseOptions;
import vaevictis.game.turn.DiceResult.DiceResult;
import vaevictis.game.turn.Phases.Phase;
import vaevictis.player.Player;

public class GameInit {
    public List<Player> playersIn;
    public Turn playersTurn;
    public ActionBoard actionBoard;
    public Phase phase;
    public List<Integer> diceResult;

    public List<Integer> firstActions;
    public List<Integer> secondActions;

    public GameInit(Game game) {
        this.playersIn = game.getPlayersIn();
        this.playersIn.size();

        this.actionBoard = game.getActionBoard();
        this.playersTurn = game.getPlayersTurn();

        this.phase = game.getPlayersTurn().getPhase();

        DiceResult dice = game.getPlayersTurn().diceResult;
        this.diceResult = dice != null ? dice.getDiceResult() : new ArrayList<>();

        this.firstActions = new ArrayList<Integer>();

        ActionPhaseOptions phase = game.getPlayersTurn().getFirstAction();
        if(phase!=null) {
            this.firstActions.add(phase.getAction0());
            this.firstActions.add(phase.getAction1());
            this.firstActions.add(phase.getAction2());
        }
    }
}
