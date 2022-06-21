package vaevictis.game.turn.Phases.ActionOptions;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;

import vaevictis.game.Game;
import vaevictis.game.GameService;
import vaevictis.game.turn.Turn;
import vaevictis.game.turn.Phases.Phase;
import vaevictis.game.turn.Phases.PhaseState;
import vaevictis.game.turn.Phases.Action.ActionSecondState;
import vaevictis.game.turn.ws.TurnOutput;
import vaevictis.game.turn.ws.TurnRequest;
import vaevictis.player.Player;

@Entity
public class LimitesFirstState extends PhaseState {

    public static final String phaseName = "LimitesFirst";

    public Game game;
    public Player player;

    
    public LimitesFirstState(Game game, Player player){
        this.game = game;
        this.player = player;
        super.setName(phaseName);

    }
    public LimitesFirstState(){}
    
    @Override
    public void next(Phase phase) {
        // TODO Auto-generated method stub
        List<Integer> electedActions = this.game.getPlayersTurn().getFirstAction().toArray();

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
                phase.setState(new LimitesSecondState(this.game, this.player));
                break;
            case 1:
                phase.setState(new AerariumFirstState(this.game, this.player));
                break;
            case 2:
                phase.setState(new DomusPalatinaFirstState(this.game, this.player));
                break;
            case 3:
                phase.setState(new ActionSecondState(this.game, this.player));
                break;
        }
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
        Turn turn = game.getPlayersTurn();
        List<Integer> options = turnRequest.getLimitesOption();
        if(options.size()>0&&options.size()<=4&&this.checkFirstLimitesAction(options)) {
            List<Integer> moves = new ArrayList<Integer>();

            for(int j=0; j<options.size(); j++) {
                Integer o = options.get(j);
                for(int k=0; k<o; k++) {
                    moves.add(j);
                }
            }
            turnOutput.cityStateEvent=gameService.movePieces(game, moves, new ArrayList<>());

            PhaseState phaseState = gameService.phaseStateService.create(this);

            Phase phase = turn.getPhase();

            phase.setState(phaseState);

            gameService.phaseService.save(phase);

            game.setPlayersTurn(turn);
            gameService.saveGame(game);

            phase.nextState();

            turnOutput.setPhase(phase);
            turnOutput.setPiecesGoForward(moves);
            turnOutput.setTurn(turn);
            turnOutput.setPlayersTurn(player);
            turnOutput.setPlayerCoins(player.getCoins());
            
        } else {
            turnOutput.setWarning("Primera acción de límites inválida");
        }
    }

    public boolean checkFirstLimitesAction(List<Integer> action) {
        boolean ok = true;
        int total = 0;

        for(Integer i : action) {
            if(i>2||i<0) {
                ok = false;
            }
            total += i;
        }

        ok = ok&&total>0;
        return ok;
    }

    @Override
    public String getPhaseName() {
        // TODO Auto-generated method stub
        return phaseName;
    }
    
}
