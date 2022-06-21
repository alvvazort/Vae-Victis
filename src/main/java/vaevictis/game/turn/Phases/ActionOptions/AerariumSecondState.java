package vaevictis.game.turn.Phases.ActionOptions;

import javax.persistence.Entity;

import vaevictis.game.Game;
import vaevictis.game.GameService;
import vaevictis.game.turn.Phases.Phase;
import vaevictis.game.turn.Phases.PhaseState;
import vaevictis.game.turn.Phases.Action.ActionSecondState;
import vaevictis.game.turn.ws.TurnOutput;
import vaevictis.game.turn.ws.TurnRequest;
import vaevictis.player.Player;

@Entity
public class AerariumSecondState extends PhaseState {

    public static final String phaseName = "AerariumSecond";

    public Game game;
    public Player player;

    public AerariumSecondState(Game game, Player player){
        this.game = game;
        this.player = player;
        super.setName(phaseName);
    }
    public AerariumSecondState(){}
    

    @Override
    public void next(Phase phase) {
        phase.setState(new ActionSecondState(this.game, this.player));  
    }

    @Override
    public void prev(Phase phase) {
        
    }

    @Override
    public void printPhase() {
        // TODO Auto-generated method stub
        System.out.println(phaseName);
        
    }

    @Override
    public void phaseHandle(GameService gameService, TurnRequest turnRequest, TurnOutput turnOutput) {
        // TODO Auto-generated method stub
        
        
    }

    @Override
    public String getPhaseName() {
        // TODO Auto-generated method stub
        return phaseName;
    }
    
}
