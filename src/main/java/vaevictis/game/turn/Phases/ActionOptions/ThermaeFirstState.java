package vaevictis.game.turn.Phases.ActionOptions;

import java.util.List;

import javax.persistence.Entity;

import vaevictis.game.Game;
import vaevictis.game.GameService;
import vaevictis.game.turn.Turn;
import vaevictis.game.turn.Phases.Phase;
import vaevictis.game.turn.Phases.PhaseState;
import vaevictis.game.turn.ws.TurnOutput;
import vaevictis.game.turn.ws.TurnRequest;
import vaevictis.player.Player;

@Entity
public class ThermaeFirstState extends PhaseState {

    public static final String phaseName = "ThermaeFirst";

    public Game game;
    public Player player;

    public ThermaeFirstState(Game game, Player player){
        this.game = game;
        this.player = player;
        super.setName(phaseName);
    }
    public ThermaeFirstState(){}
    

    @Override
    public void next(Phase phase) {
        // TODO Auto-generated method stub
        List<Integer> electedActions = this.game.getPlayersTurn().getSecondAction().toArray();

        int indexSecondOption = 3;
        for(int index = 0; index < electedActions.size(); index++)   {
            Integer option = electedActions.get(index);
            if (option > 0) 
            {
                if((index == 0 && option == 2) || index > 0)  { 
                    indexSecondOption = index;
                    break;
                }
            }
        }

        switch(indexSecondOption) {
            case 0:
                phase.setState(new ThermaeSecondState(this.game, this.player));
                break;
            case 1:
                phase.setState(new ForumFirstState(this.game, this.player));
                break;
            case 2:
                phase.setState(new CuriaFirstState(this.game, this.player));
                break;
            case 3:
                phase.setState(new EndTurnState(this.game, this.player));
                break;
        }
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
        
        gameService.drawCard( false, player);
        turnOutput.setPlayersTurn(player);

        PhaseState phaseState = gameService.phaseStateService.create(this);
        Turn turn = game.getPlayersTurn();
        Phase phase = turn.getPhase();

        phase.setState(phaseState);

        gameService.phaseService.save(phase);

        game.setPlayersTurn(turn);
        gameService.saveGame(game);

        phase.nextState();

        turnOutput.setPhase(phase);
        turnOutput.setTurn(turn);
        turnOutput.setPlayersTurn(player);
    }

    @Override
    public String getPhaseName() {
        // TODO Auto-generated method stub
        return phaseName;
    }
    
}
