package vaevictis.game.turn.DiceResult;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;
import vaevictis.model.BaseEntity;

@Entity
@Getter
@Setter
@Table(name="diceresults")
public class DiceResult extends BaseEntity {
    private Integer dice0;

    private Integer dice1;

    private Integer dice2;

    public DiceResult() {}

    public DiceResult(List<Integer> diceResult) {
        this.dice0 = diceResult.get(0);
        this.dice1 = diceResult.get(1);
        this.dice2 = diceResult.get(2);
    }

    public List<Integer> getDiceResult() {
        List<Integer> diceResult = new ArrayList<Integer>();
        diceResult.add(this.dice0);
        diceResult.add(this.dice1);
        diceResult.add(this.dice2);

        return diceResult;
    }

    public void setDiceResult(Integer diceIndex, Integer value) {
        switch(diceIndex) {
            case 0:
                this.dice0 = value;
                break;
            case 1:
                this.dice1 = value;
                break;
            case 2:
                this.dice2 = value;
                break;
        }
    }

    /*
     * Se le pasa una matriz binaria para asignar el valor -1 (dado eliminado) a los resultados de los dados
     */
    public void removeDices(List<Integer> removedDices) {
        for(int i=0; i<removedDices.size(); i++) {
            if(removedDices.get(i) == 1) {
                this.setDiceResult(i, -1);
            }
        }
    }
}
