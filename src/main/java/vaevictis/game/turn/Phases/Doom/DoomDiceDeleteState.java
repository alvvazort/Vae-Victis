package vaevictis.game.turn.Phases.Doom;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;

import vaevictis.game.Game;
import vaevictis.game.GameService;
import vaevictis.game.turn.Turn;
import vaevictis.game.turn.TurnService;
import vaevictis.game.turn.DiceResult.DiceResult;
import vaevictis.game.turn.DiceResult.DiceResultService;
import vaevictis.game.turn.Phases.Phase;
import vaevictis.game.turn.Phases.PhaseState;
import vaevictis.game.turn.Phases.Action.ActionFirstState;
import vaevictis.game.turn.ws.TurnOutput;
import vaevictis.game.turn.ws.TurnRequest;
import vaevictis.player.Player;

@Entity
public class DoomDiceDeleteState extends PhaseState {
    public static final String phaseName = "DoomDiceDelete";

    public Game game;
    public Player player;

    public DoomDiceDeleteState(Game game, Player player) {
        this.game = game;
        this.player = player;
        super.setName(phaseName);
    }

    public DoomDiceDeleteState() {}

    @Override
    public void next(Phase phase) {
        // TODO Auto-generated method stub
        phase.state = new ActionFirstState(this.game, this.player);
        
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
        if(turnRequest.removeDices.stream().anyMatch(x->(x==1))) {
            List<Integer> moves = this.moveDoomDices(game, gameService, turnRequest);

            Integer coinsToSpend = 2-moves.size();
            if(player.getCoins()>coinsToSpend) {
                this.addDeleteDicesTurn(game, player, turnRequest.removeDices, gameService, moves, turnOutput);

                gameService.payCoins(game, coinsToSpend, player);
                //ret.setPlayerCoins(player.getCoins()-(2-moves.size()));

                this.game.getPlayersTurn().getPhase().nextState();
                turnOutput.setTurn(this.game.getPlayersTurn());
                turnOutput.setPlayerCoins(player.getCoins());
                turnOutput.setPlayersTurn(player);
                turnOutput.setPiecesGoBack(moves);
            } else {
                turnOutput.setWarning("No tienes monedas suficientes");
            }
        } else {
            turnOutput.setWarning("Tienes que quitar al menos un dado");
        }
    
    }

    public void addDeleteDicesTurn(Game game, Player player, List<Integer> removeDices, GameService gameService, List<Integer> moves, TurnOutput turnOutput) {
        TurnService turnService = gameService.turnService;
        DiceResultService diceResultService = gameService.diceResultService; 

        Turn lastTurn = turnService.getLastTurn(player, game);

        DiceResult diceRes = lastTurn.getDiceResult();
        diceRes.removeDices(removeDices);
        diceResultService.create(diceRes);

        PhaseState phaseState = gameService.phaseStateService.create(this);

        Phase phase = game.getPlayersTurn().getPhase();
        phase.setState(phaseState);
        gameService.phaseService.save(phase);

        Turn turn = game.getPlayersTurn();
        turn.setDiceResult(diceRes);

        game.setPlayersTurn(turn);
        
        turnOutput.cityStateEvent=gameService.movePieces(game, new ArrayList<>(), moves); //Mueve piezas y además guarda el evento de la casilla en la que haya caido.

        gameService.saveGame(game);
    }

    public List<Integer> moveDoomDices(Game game, GameService gameService, TurnRequest request) {

        List<Integer> diceRes = game.getPlayersTurn().getDiceResult().getDiceResult();
        List<Integer> diceRem = request.getDicesToRemove();

        List<Integer> moves = new ArrayList<>();
        for(int i=0; i<diceRes.size(); i++) {
            if(diceRem.get(i)==0) {
                moves.add(diceRes.get(i));
            }
        }

        return moves;
    }

    @Override
    public String getPhaseName() {
        // TODO Auto-generated method stub
        return phaseName;
    }
    
    
}
