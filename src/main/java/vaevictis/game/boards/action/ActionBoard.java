package vaevictis.game.boards.action;

import javax.persistence.Entity;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;
import vaevictis.model.BaseEntity;

@Entity
@Getter
@Setter
@Table(name = "ActionBoards")
public class ActionBoard extends BaseEntity {

    public int treasury;
    public int maxtreasury;

    /* @Range(min=0, max=3)
    public int leftCuria;
    @Range(min=0, max=3)
    public int centerCuria;
    @Range(min=0, max=3)
    public int rightCuria; */


    public ActionBoard(){}
    public ActionBoard(int maxtreasury){
        this.maxtreasury = maxtreasury;
        this.treasury = 3;
    }
    public void addCoins(int i) {
        this.treasury = this.treasury+ i;
        if(this.treasury>this.maxtreasury) this.treasury=this.maxtreasury;
    }
}
