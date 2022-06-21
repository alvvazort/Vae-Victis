package vaevictis.game.turn;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import vaevictis.game.Game;
import vaevictis.game.turn.ActionPhase.ActionPhaseOptions;
import vaevictis.game.turn.ActionPhase.ActionPhaseOptionsService;
import vaevictis.game.turn.Phases.Phase;
import vaevictis.game.turn.Phases.PhaseService;
import vaevictis.player.Player;

@Service
public class TurnService {
    
    @Autowired
    TurnRepository turnRepository;

    @Autowired
    PhaseService phaseService;

    @Autowired
    ActionPhaseOptionsService actionPhaseOptionsService;

    @Autowired
    public TurnService(TurnRepository turnRepository, ActionPhaseOptionsService actionPhaseOptionsService, PhaseService phaseService) {
        this.turnRepository = turnRepository;
        this.actionPhaseOptionsService = actionPhaseOptionsService;
        this.phaseService = phaseService;
    }

    public Turn createTurn(Turn turn) {
        return this.turnRepository.save(turn);
    }

    public Turn createTurn(Game game, Player player, Phase phase) {
        Turn turn = new Turn(game, player, phase);
        return this.createTurn(turn);
    }

    public Turn createTurn(Game game, Player player, Integer actionLevel, List<Integer> actions, Phase phase) {
        ActionPhaseOptions options = new ActionPhaseOptions(actions);
        this.actionPhaseOptionsService.create(options);

        Turn turn = new Turn(game, player, options, actionLevel, phase);

        return this.createTurn(turn);
    }

    public Player getNextPlayersTurn(List<Player> playersInGame, Player lastPlayerTurn) {
        Player nextPlayer = null;

        for(int i=0; i<playersInGame.size(); i++) {
            Player player = playersInGame.get(i);
            if(player.getUser().getUsername().equals(lastPlayerTurn.getUser().getUsername())) {
                nextPlayer = (i+1) < playersInGame.size() ? playersInGame.get(i+1) : playersInGame.get(0);
            }
        }

        return nextPlayer;
    }

    public void setGameToTurn(Game game, Turn turn) {
        this.turnRepository.setGameToTurn(game, turn);
    }

    public Turn getLastTurn(Player player, Game game) {
        List<Turn> turn = this.turnRepository.findByPlayerAndGame(player, game);
        return turn.size()>0 ? turn.get(turn.size() - 1) : null;
    }

    public Turn getLastActionTurn(Game game) {
        List<Turn> turn = this.turnRepository.findByGameAndPhase(game, "ActionFirst");
        return turn.size()>0 ? turn.get(turn.size() - 1) : null;
    }
}
