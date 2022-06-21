package vaevictis.statistics;

import javax.persistence.Entity;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;
import vaevictis.model.BaseEntity;

@Entity
@Getter
@Setter
@Table(name = "achievements")
public class Achievement extends BaseEntity {
    
	private String name;
    private String description;
    private Integer achievementCondition;
     
    public Achievement(){}

    public Achievement(String name,String description, Integer achievementCondition){
        this.name = name;
        this.description = description;
        this.achievementCondition = achievementCondition;
    }
}

