package vaevictis.game.turn.Phases.Action;

import java.util.List;

import javax.persistence.Entity;

import vaevictis.game.Game;
import vaevictis.game.GameService;
import vaevictis.game.turn.Turn;
import vaevictis.game.turn.ActionPhase.ActionPhaseOptions;
import vaevictis.game.turn.Phases.Phase;
import vaevictis.game.turn.Phases.PhaseState;
import vaevictis.game.turn.Phases.ActionOptions.AerariumFirstState;
import vaevictis.game.turn.Phases.ActionOptions.DomusPalatinaFirstState;
import vaevictis.game.turn.Phases.ActionOptions.LimitesFirstState;
import vaevictis.game.turn.ws.TurnOutput;
import vaevictis.game.turn.ws.TurnRequest;
import vaevictis.player.Player;

@Entity
public class ActionFirstState extends PhaseState {

    public static final String phaseName = "ActionFirst";

    public Game game;
    public Player player;

    public ActionFirstState(Game game, Player player){
        this.game = game;
        this.player = player;
        super.setName(phaseName);

    }
    public ActionFirstState(){}
    

    @Override
    public void next(Phase phase) {
        // TODO Auto-generated method stub
        List<Integer> electedActions = this.game.getPlayersTurn().getFirstAction().toArray();
        int indexFirstOption;
        for(indexFirstOption = 0; indexFirstOption < electedActions.size(); indexFirstOption++)   {
            if (electedActions.get(indexFirstOption) > 0) break;
        }

        switch(indexFirstOption) {
            case 0:
                phase.setState(new LimitesFirstState(this.game, this.player));
                break;
            case 1:
                phase.setState(new AerariumFirstState(this.game, this.player));
                break;
            case 2:
                phase.setState(new DomusPalatinaFirstState(this.game, this.player));
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

        this.addActionFirst(actions, game, player, turnOutput, gameService);
        turnOutput.setActions(actions);
        turnOutput.setTurn(game.getPlayersTurn());
    }

    // Guarda el turno de la elección de la primera fase de acciones
    public void addActionFirst(List<Integer> actions, Game game, Player player, TurnOutput ret, GameService gameService) {
        if(actions!=null&&this.checkActions(actions)) {
            PhaseState phaseState = gameService.phaseStateService.create(this);

            Phase phase = game.getPlayersTurn().getPhase();
            phase.setState(phaseState);
            gameService.phaseService.save(phase);
            
            Turn turn = game.getPlayersTurn(); //.createTurn(game, player, 0, actions, phase);

            ActionPhaseOptions options = new ActionPhaseOptions(actions);
            gameService.actionPhaseOptionsService.create(options);
            turn.setFirstAction(options);
            gameService.turnService.createTurn(turn);

            game.setPlayersTurn(turn);
            gameService.saveGame(game);

            ret.setPhase(turn.getPhase());
            ret.setPlayersTurn(player);
            turn.getPhase().nextState();
            ret.setTurn(turn);
            ret.setActions(actions);
        } else {
            ret.setWarning("Acción inválida");
        }
    }

    private boolean checkActions(List<Integer> actions) {
        Integer count = 0;
        for(Integer a : actions) {
            count += a;
        }
        return count>0 && count<3;
    }

    @Override
    public String getPhaseName() {
        // TODO Auto-generated method stub
        return phaseName;
    }
    
}
