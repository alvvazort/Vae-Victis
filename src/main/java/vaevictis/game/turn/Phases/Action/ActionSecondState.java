package vaevictis.game.turn.Phases.Action;

import java.util.List;

import javax.persistence.Entity;

import vaevictis.game.Game;
import vaevictis.game.GameService;
import vaevictis.game.turn.Turn;
import vaevictis.game.turn.ActionPhase.ActionPhaseOptions;
import vaevictis.game.turn.Phases.Phase;
import vaevictis.game.turn.Phases.PhaseState;
import vaevictis.game.turn.Phases.ActionOptions.CuriaFirstState;
import vaevictis.game.turn.Phases.ActionOptions.ForumFirstState;
import vaevictis.game.turn.Phases.ActionOptions.ThermaeFirstState;
import vaevictis.game.turn.ws.TurnOutput;
import vaevictis.game.turn.ws.TurnRequest;
import vaevictis.player.Player;

@Entity
public class ActionSecondState extends PhaseState {

    public static final String phaseName = "ActionSecond";

    public Game game;
    public Player player;

    public ActionSecondState(Game game, Player player){
        this.game = game;
        this.player = player;
        super.setName("ActionSecond");
    }
    public ActionSecondState(){}
    

    @Override
    public void next(Phase phase) {
        // TODO Auto-generated method stub
        List<Integer> electedActions = this.game.getPlayersTurn().getSecondAction().toArray();
        int indexFirstOption;
        for(indexFirstOption = 0; indexFirstOption < electedActions.size(); indexFirstOption++)   {
            if (electedActions.get(indexFirstOption) > 0) break;
        }

        switch(indexFirstOption)    {
            case 0:
                phase.setState(new ThermaeFirstState(this.game, this.player));
                break;
            case 1:
                phase.setState(new ForumFirstState(this.game, this.player));
                break;
            case 2:
                phase.setState(new CuriaFirstState(this.game, this.player));
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
        // TODO Auto-generated method stub
        List<Integer> actions = turnRequest.actions;
        this.addActionSecond(actions, game, player, turnOutput, gameService);
        turnOutput.setActions(actions);
        turnOutput.setTurn(game.getPlayersTurn());
        
    }

    // Guarda el turno de la elección de la primera fase de acciones
    public void addActionSecond(List<Integer> actions, Game game, Player player, TurnOutput turnOutput, GameService gameService) {
        // TODO: se debería comprobar que la información sea la adecuada
        if(actions != null && this.checkActions(actions)) {
            if (this.checkEnoughCoin(actions, this.player.getCoins())) {
                PhaseState phaseState = gameService.phaseStateService.create(this);

                Phase phase = game.getPlayersTurn().getPhase();
                phase.setState(phaseState);
                gameService.phaseService.save(phase);
                
                Turn turn = game.getPlayersTurn(); //.createTurn(game, player, 0, actions, phase);

                ActionPhaseOptions options = new ActionPhaseOptions(actions);
                gameService.actionPhaseOptionsService.create(options);
                turn.setSecondAction(options);
                gameService.turnService.createTurn(turn);

                game.setPlayersTurn(turn);
                gameService.saveGame(game);

                turnOutput.setPhase(turn.getPhase());
                turnOutput.setPlayersTurn(player);
                turn.getPhase().nextState();
                turnOutput.setTurn(turn);
                turnOutput.setActions(actions);


            } else {
                turnOutput.setWarning("No tienes monedas suficientes");
            }
        } else {
            turnOutput.setWarning("Acción inválida");
        }

    }

    @Override
    public String getPhaseName() {
        // TODO Auto-generated method stub
        return phaseName;
    }
    
    private boolean checkActions(List<Integer> actions) {
        Integer count = 0;
        for(Integer a : actions) {
            count += a;
        }
        return count>0 && count<3;
    }

    private boolean checkEnoughCoin(List<Integer> actions, Integer playerCoins) {        
        long count = actions.stream().filter(a -> a > 0).count();

        if (count > 1 ) { 
            return playerCoins > 3;
        } else if (count == 1 && playerCoins >= 1) {
            return true;
        } else {
            return false;
        }
    }

}
