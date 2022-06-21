package vaevictis.game.turn.Phases;

import javax.persistence.Entity;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;
import vaevictis.game.Game;
import vaevictis.game.GameService;
import vaevictis.game.turn.ws.TurnOutput;
import vaevictis.game.turn.ws.TurnRequest;
import vaevictis.model.NamedEntity;
import vaevictis.player.Player;

@Entity
@Getter
@Setter
@Table(name="phasestates")
public abstract class PhaseState extends NamedEntity {
    @OneToOne
    public Game game;
    @OneToOne
    public Player player;
    
    public void next(Phase phase){}
    public void prev(Phase phase){}
    public void printPhase(){} //Debugging purposes
    public void phaseHandle(GameService gameService, TurnRequest turnRequest, TurnOutput turnOutput){} //Does things from the phase
    public String getPhaseName()   {return this.getName();}
}
