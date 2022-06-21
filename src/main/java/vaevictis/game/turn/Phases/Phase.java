package vaevictis.game.turn.Phases;

import javax.persistence.Entity;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;
import vaevictis.game.GameService;
import vaevictis.game.turn.Phases.Doom.DoomState;
import vaevictis.game.turn.ws.TurnOutput;
import vaevictis.game.turn.ws.TurnRequest;
import vaevictis.model.BaseEntity;
@Entity
@Getter
@Setter
@Table(name="phases")
public class Phase extends BaseEntity {
    
    @OneToOne
    public PhaseState state;

    public Phase() {
        this.state = new DoomState();
    }

    public Phase(PhaseState phaseState){
        this.state = phaseState;
    }

    public void previousState(){
        state.prev(this);
    }

    public void nextState(){
        state.next(this);
    }

    public void printStatus(){
        state.printPhase();
    }

    public void phaseHandle(GameService gameService, TurnRequest turnRequest, TurnOutput turnOutput){
        state.phaseHandle(gameService, turnRequest, turnOutput);
    }

    public void setState(PhaseState phaseState) {
        this.state = phaseState;
    }
}
