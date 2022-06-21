package vaevictis.game.turn.Phases.Doom;

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
import vaevictis.game.turn.ws.TurnOutput;
import vaevictis.game.turn.ws.TurnRequest;
import vaevictis.player.Player;

@Entity
public class DoomDiceThrowState extends PhaseState {

    public static final String phaseName = "DoomDiceThrow";

    public Game game;
    public Player player;

    public DoomDiceThrowState(Game game, Player player) {
        this.game = game;
        this.player = player;
        this.setName(phaseName);
    }

    public DoomDiceThrowState() {}

    @Override
    public void next(Phase phase) {
        // TODO Auto-generated method stub
        phase.state = new DoomDiceDeleteState(game, player);
    }

    @Override
    public void prev(Phase phase) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void printPhase() {
        System.out.println(phaseName);
        
    }

    @Override
    public void phaseHandle(GameService gameService, TurnRequest turnRequest, TurnOutput turnOutput) {
        List<Integer> diceResult = this.getDiceResult(); // Devuelve números randoms

        this.addDiceTurn(this.game, player, diceResult, gameService);
        this.game.getPlayersTurn().getPhase().nextState();

        turnOutput.setTurn(this.game.getPlayersTurn());
        turnOutput.setDiceResult(diceResult);
        turnOutput.setPlayersTurn(this.player);
    }

    public void addDiceTurn(Game game, Player player, List<Integer> diceResult, GameService gameService) {
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

    @Override
    public String getPhaseName() {
        // TODO Auto-generated method stub
        return phaseName;
    }
    
    public List<Integer> getDiceResult(){
		List<Integer> diceResult = new ArrayList<Integer>();
		diceResult.add(ThreadLocalRandom.current().nextInt(0, 6));
		diceResult.add(ThreadLocalRandom.current().nextInt(0, 6));
		diceResult.add(ThreadLocalRandom.current().nextInt(0, 6));

		return diceResult;
	}

}
