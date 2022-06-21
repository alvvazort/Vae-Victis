package vaevictis.game.turn.Phases.ActionOptions;

import java.util.List;

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
public class DomusPalatinaSecondState extends PhaseState {

    public static final String phaseName = "DomusSecond";

    public Game game;
    public Player player;


    public DomusPalatinaSecondState(Game game, Player player){
        this.game = game;
        this.player = player;
        super.setName(phaseName);
    }
    public DomusPalatinaSecondState() {}
    

    @Override
    public void next(Phase phase) {
        // TODO Auto-generated method stub
       phase.setState(new ActionSecondState(this.game, this.player));
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

        if(actions.get(2)==1){
            firstActionCuria(turnRequest.selectedCuria, turnOutput, gameService);
        }
        if(actions.get(0)==1){
            firstActionThermae(player, turnOutput, gameService);
        }
        if(actions.get(1)==1){
            firstActionForum(player, turnOutput, gameService);
        }
        
        System.out.println("ESTAMOS EN THIRDOPTIONS");

        System.out.println(actions.toString());
        
    }

    public void firstActionCuria(Integer selectedCuria, TurnOutput turnOutput, GameService gameService){

        Boolean plusOrMinus=selectedCuria%2==0;
        int section=0;
        if(selectedCuria<=2){
            section=1;
        }else if(selectedCuria<=4){
            section=2;
        }else{
            section=3;
        }

        turnOutput.setSectionCuria(section);
        turnOutput.setPlusOrMinus(plusOrMinus);

        gameService.saveActionCuria(this.game, plusOrMinus, section);
    }


    public void firstActionThermae(Player player, TurnOutput turnOutput, GameService gameService){
        gameService.drawCard( false, player);
        turnOutput.setPlayersTurn(player);
    }
    public void firstActionForum(Player player, TurnOutput turnOutput, GameService gameService){
        gameService.drawCard( true, player);
        turnOutput.setPlayersTurn(player);
    }

    @Override
    public String getPhaseName() {
        // TODO Auto-generated method stub
        return phaseName;
    }
    
}
