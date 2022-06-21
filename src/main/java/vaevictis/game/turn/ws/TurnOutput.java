package vaevictis.game.turn.ws;

import java.util.List;

import lombok.Getter;
import lombok.Setter;
import vaevictis.game.components.card.Card;
import vaevictis.game.turn.Turn;
import vaevictis.game.turn.Phases.Phase;
import vaevictis.player.Player;
import vaevictis.user.User;

@Getter
@Setter
public class TurnOutput {

    public Player playersTurn;

    public Turn turn;

    public Integer playerCoins;
    public Integer cardUsedId;
    public Integer cardUsedIndex;

    public List<Integer> diceResult;
    public List<Integer> Card0Selected;

    public List<Integer> limitesDiceResult;

    public List<Integer> aerariumDiceResult;

    public List<Integer> previousResult;

    public List<Integer> piecesGoForward;

    public List<Integer> piecesGoBack;

    public Phase phase;

    public String warning;

    public List<Integer> actions;

    public List<Card> playerCards;

    public Integer sectionCuria;

    public Boolean plusOrMinus;

    public Integer treasury;

    public String cityStateEvent;

    public Boolean discard;

    public User winner;


    public TurnOutput() {
        
    }

    public TurnOutput(List<Integer> diceResult) {
        this.diceResult = diceResult;
        this.warning = "";
    }

    public void setDiceResult(List<Integer> diceResult) {
        this.diceResult = diceResult;
    }

    public void setPhase(Phase phase) {
        this.phase = phase;
    }

    public void setPlayersTurn(Player player) {
        this.playersTurn = player;
    }

    public void setWarning(String warning) {
        this.warning = warning;
    }

}