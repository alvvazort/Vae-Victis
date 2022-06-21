package vaevictis.game;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.hibernate.validator.constraints.Range;

import lombok.Getter;
import lombok.Setter;
import vaevictis.game.boards.action.ActionBoard;
import vaevictis.game.boards.war.GameBoard;
import vaevictis.game.turn.Turn;
import vaevictis.model.BaseEntity;
import vaevictis.player.Player;
import vaevictis.user.User;

@Entity
@Getter
@Setter
@Table(name = "games")
public class Game extends BaseEntity {
    
	@OneToMany
	@OnDelete(action = OnDeleteAction.CASCADE)
	@JoinColumn(name="GameIdIn")
	private List<Player> playersIn;

	@OneToOne
	private GameBoard warBoard;

	@OneToOne
	private GameBoard cityStateBoard;

	@OneToOne
	private ActionBoard actionBoard;
	
	@OneToOne
	private User winner;

	@OneToOne
	@OnDelete(action = OnDeleteAction.CASCADE)
	private Turn playersTurn;

	@OneToOne
	private User lobbyAdmin;
	
	@Column(name="createdAt", columnDefinition="TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
	@Temporal(TemporalType.TIMESTAMP)
	private Date createdAt;

	@Temporal(TemporalType.TIMESTAMP)
	private Date removedAt;

	// Senado de curia
	@Column(name= "senate_left")
	@Range(min = 0,max=3)
	public int senateLeft;

	@Column(name= "senate_mid")
	@Range(min = 0,max=3)
	public int senateMid;

	@Column(name= "senate_right")
	@Range(min = 0,max=3)
	public int senateRight;
	
	public boolean hispaniaWin;

	public boolean galliaWin;

	public boolean britanniaWin;

	public boolean germaniaWin;

	public boolean matchLose;



	/* @OneToOne
	private Player winner; */

	public Game(){

		this.senateLeft=0;
		this.senateMid=0;
		this.senateRight=0;

		this.hispaniaWin=false;
		this.britanniaWin=false;
		this.galliaWin=false;
		this.germaniaWin=false;
		this.matchLose=false;
	}

    
	public Game(List<Player> players, Turn playersTurn) {
		this.playersIn = players;
		this.playersTurn = playersTurn;

		this.senateLeft=0;
		this.senateMid=0;
		this.senateRight=0;

		this.hispaniaWin=false;
		this.britanniaWin=false;
		this.galliaWin=false;
		this.germaniaWin=false;
		this.matchLose=false;

		//this.actionBoard = new ActionBoard();

	}
	public Game(List<Player> players, Turn playersTurn,User lobbyAdmin, GameBoard warBoard, GameBoard cityStateBoard, ActionBoard actionBoard) {
		this.playersIn = players;
		this.playersTurn = playersTurn;
		this.lobbyAdmin = lobbyAdmin;
		this.actionBoard = actionBoard;
		this.warBoard = warBoard;
		this.cityStateBoard = cityStateBoard;

		this.senateLeft=0;
		this.senateMid=0;
		this.senateRight=0;

		this.hispaniaWin=false;
		this.britanniaWin=false;
		this.galliaWin=false;
		this.germaniaWin=false;
		this.matchLose=false;

	}

	public boolean isPlayerIn(String username) {
		boolean isPlayerIn = false;

		for(int i=0; i<this.playersIn.size() && !isPlayerIn; i++) {
			isPlayerIn = this.playersIn.get(i).getUser().getUsername().equals(username);
		}

		return isPlayerIn;
	}

}
