package vaevictis.game.turn.Phases.ActionOptions;

import java.util.List;

import javax.persistence.Entity;

import vaevictis.game.Game;
import vaevictis.game.GameService;
import vaevictis.game.turn.Phases.Phase;
import vaevictis.game.turn.Phases.PhaseState;
import vaevictis.game.turn.ws.TurnOutput;
import vaevictis.game.turn.ws.TurnRequest;
import vaevictis.player.Player;

@Entity
public class ThermaeSecondState extends PhaseState {

    public static final String phaseName = "ThermaeSecond";

    public Game game;
    public Player player;

    public ThermaeSecondState(Game game, Player player){
        this.game = game;
        this.player = player;
        super.setName(phaseName);
    }
    public ThermaeSecondState(){}
    

    @Override
    public void next(Phase phase) {
        // TODO Auto-generated method stub
    }

    @Override
    public void prev(Phase phase) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void printPhase() {
        // TODO Auto-generated method stub
        System.out.println(phaseName);
        
    }

    @Override
    public void phaseHandle(GameService gameService, TurnRequest turnRequest, TurnOutput turnOutput) {
        // TODO Auto-generated method stub
        List<Integer> actions = turnRequest.actions;

        // Recibimos una respuesta tal que así:
        // [1, 1, 0]
        // que interpreta cada índice de cada parte del tablero de arriba

        System.out.println(actions.toString());
        
    }

    @Override
    public String getPhaseName() {
        // TODO Auto-generated method stub
        return phaseName;
    }
    
}
