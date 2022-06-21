package vaevictis.game.turn;

import javax.persistence.Entity;


import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import lombok.Getter;
import lombok.Setter;
import vaevictis.game.Game;
import vaevictis.game.turn.ActionPhase.ActionPhaseOptions;
import vaevictis.game.turn.DiceResult.DiceResult;
import vaevictis.game.turn.Phases.Phase;
import vaevictis.model.BaseEntity;
import vaevictis.player.Player;

import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;


@Entity
@Getter
@Setter
@Table(name = "turns")
public class Turn extends BaseEntity {

    @JsonIgnore
    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Game game;

    @OneToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Player player;

    @OneToOne
    private Phase phase;

    @OneToOne
    public DiceResult diceResult;

    @OneToOne
    public DiceResult limitesDiceResult;

    @OneToOne
    public DiceResult aerariumDiceResult;

    @OneToOne
    public ActionPhaseOptions firstAction;
    
    @OneToOne
    public ActionPhaseOptions secondAction;

    public Turn() {}

    // Este constructor se utiliza cuando se crea el primer turno de una partida y se actualiza Game una vez creado
    public Turn(Player player) {
        this.player = player;
    }

    public Turn(Game game, Player player, DiceResult diceResult, Phase phase) {
        this.diceResult = diceResult;
        this.player = player;
        this.game = game;
        this.phase = phase;
    }
    
    public Turn(Game game, Player player, Phase phase) {
        this.player = player;
        this.game = game;
        this.phase = phase;
    }

    public Turn(Game game, Player player, ActionPhaseOptions actions, Integer actionLevel, Phase phase) {
        this.secondAction = actions;
        this.phase = phase;
        this.player = player;
        this.game = game;
    }
    
}
