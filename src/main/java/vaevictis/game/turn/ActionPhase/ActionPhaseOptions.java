package vaevictis.game.turn.ActionPhase;

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
@Table(name="actionphaseoptions")
public class ActionPhaseOptions extends BaseEntity {
    Integer action0;
    Integer action1;
    Integer action2;

    public ActionPhaseOptions() {
        this.action0 = 0;
        this.action1 = 0;
        this.action2 = 0;
    }
    
    public ActionPhaseOptions(List<Integer> actions) {
        this.action0 = actions.get(0);
        this.action1 = actions.get(1);
        this.action2 = actions.get(2);
    }

    public List<Integer> toArray() {
        List<Integer> ret = new ArrayList<Integer>();
        ret.add(this.getAction0());
        ret.add(this.getAction1());
        ret.add(this.getAction2());
        return ret;
    }
}
