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
public class ForumFirstState extends PhaseState {

    public static final String phaseName = "ForumFirst";

    public Game game;
    public Player player;

    public ForumFirstState(Game game, Player player){
        this.game = game;
        this.player = player;
        super.setName(phaseName);
    }
    public ForumFirstState(){}
    

    @Override
    public void next(Phase phase) {
        // TODO Auto-generated method stub
        List<Integer> electedActions = this.game.getPlayersTurn().getSecondAction().toArray();

        int indexSecondOption = 3;
        for(int index = 1; index < electedActions.size(); index++)   {
            Integer option = electedActions.get(index);
            if (option > 0) 
            {
                if((index == 1 && option == 2) || index > 1)  { 
                    indexSecondOption = index;
                    break;
                }
            }
        }

        switch(indexSecondOption)    {
            case 1:
                phase.setState(new ForumSecondState(this.game, this.player));
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
