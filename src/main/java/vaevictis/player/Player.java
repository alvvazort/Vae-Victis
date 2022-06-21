package vaevictis.player;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import lombok.Getter;
import lombok.Setter;
import vaevictis.enums.Goals;
import vaevictis.game.components.card.Card;
import vaevictis.model.BaseEntity;
import vaevictis.user.User;

@Entity
@Getter
@Setter
@Table(name = "players")
public class Player extends BaseEntity {
    
	@OneToOne
	@OnDelete(action = OnDeleteAction.CASCADE)
	private User user;

	private String warGoal;

	private String curiaGoal;

	private Integer coins;
	
	@OneToMany
	@JsonIgnore
	private List<Card> allCards;

    @OneToMany(fetch = FetchType.EAGER)
	@JoinColumn(name = "player_Id", nullable = true)
	Set<Card> cards;

	public Player() {}
	
	public Player(User user) {
		this.user = user;
		this.warGoal = Goals.getWarGoals();
		this.curiaGoal = Goals.getCuriaGoal();
		this.allCards = new ArrayList<Card>();
		this.cards = new HashSet<Card>();
	}
}
