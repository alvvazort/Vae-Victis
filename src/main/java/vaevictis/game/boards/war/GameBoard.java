package vaevictis.game.boards.war;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import vaevictis.game.components.pieces.war.GamePiece;
import vaevictis.model.BaseEntity;

@Entity
@Getter
@Setter
@ToString
@Table(name = "Gameboards")
public class GameBoard extends BaseEntity {

    @NotNull
    private TypeGameBoard type;

    @OneToMany(fetch = FetchType.EAGER)
    private List<GamePiece> pieces;

    public GameBoard(){
        this.type=TypeGameBoard.WARBOARD;
    }
    public GameBoard(TypeGameBoard type){
        this.type=type;
    }
    
}
