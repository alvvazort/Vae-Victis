package vaevictis.game.turn.Phases.ActionOptions;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import javax.persistence.Entity;

import vaevictis.game.Game;
import vaevictis.game.GameService;
import vaevictis.game.turn.Turn;
import vaevictis.game.turn.DiceResult.DiceResult;
import vaevictis.game.turn.Phases.Phase;
import vaevictis.game.turn.Phases.PhaseState;
import vaevictis.game.turn.Phases.Action.ActionSecondState;
import vaevictis.game.turn.ws.TurnOutput;
import vaevictis.game.turn.ws.TurnRequest;
import vaevictis.player.Player;

@Entity
public class LimitesSecondState extends PhaseState {

    public static final String phaseName = "LimitesSecond";

    public Game game;
    public Player player;

    
    public LimitesSecondState(Game game, Player player){
        this.game = game;
        this.player = player;
        super.setName(phaseName);

    }
    public LimitesSecondState(){}
    
    @Override
    public void next(Phase phase) {
        // TODO Auto-generated method stub
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
            gameService.movePieces(game, moves, new ArrayList<>());

            List<Integer> diceResult = this.getDiceResult();
            DiceResult diceRes = gameService.diceResultService.create(new DiceResult(diceResult));

            PhaseState phaseState = gameService.phaseStateService.create(this);

            Phase phase = turn.getPhase();

            phase.setState(phaseState);

            gameService.phaseService.save(phase);

            turn.setLimitesDiceResult(diceRes);

            game.setPlayersTurn(turn);
            gameService.saveGame(game);

            phase.nextState();

            turnOutput.setPhase(phase);
            turnOutput.setPiecesGoForward(moves);
            turnOutput.setTurn(turn);
            turnOutput.setPlayersTurn(player);
            turnOutput.setDiceResult(diceResult);
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

    public void addLimitesTurn(Game game, Player player, List<Integer> diceResult, GameService gameService) {
        DiceResult diceRes = gameService.diceResultService.create(new DiceResult(diceResult));

        PhaseState phaseState = gameService.phaseStateService.create(this);

        Phase phase = game.getPlayersTurn().getPhase();
        phase.setState(phaseState);
        
        gameService.phaseService.save(phase);

        Turn turn = game.getPlayersTurn();
        turn.setDiceResult(diceRes);
        gameService.turnService.createTurn(turn);
        
        game.setPlayersTurn(turn);
        gameService.saveGame(game);
    }

    public List<Integer> getDiceResult(){
		List<Integer> diceResult = new ArrayList<Integer>();
		diceResult.add(ThreadLocalRandom.current().nextInt(0, 6));
		diceResult.add(ThreadLocalRandom.current().nextInt(0, 6));
		diceResult.add(ThreadLocalRandom.current().nextInt(0, 6));

		return diceResult;
	}
    
}
