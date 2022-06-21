package vaevictis.lobby;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import vaevictis.model.BaseEntity;
import vaevictis.user.User;

@Entity
@Table(name = "lobbies")
public class Lobby extends BaseEntity {

	private int playerNumber;
	
	private int gameLevel;

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinColumn(name="LobbyIdIn")
	@OnDelete(action = OnDeleteAction.CASCADE)
	private List<User> usersIn;

	@ManyToMany(cascade = CascadeType.ALL)
	private List<User> usersInvited;

	@OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@OnDelete(action = OnDeleteAction.CASCADE)
	private User lobbyAdmin;

	public Lobby() {}

	public Lobby(User user) {
		/* Opciones por defecto */
		this.setPlayerNumber(3);
		this.setGameLevel(1);
		this.usersIn = new ArrayList<User>();
		this.addUserIn(user);
		this.setLobbyAdmin(user);
		this.usersInvited = new ArrayList<User>();
	}

	public void setPlayerNumber(int playerNumber) {
		this.playerNumber = playerNumber;
	}

	public int getPlayerNumber() {
		return this.playerNumber;
	}

	public void setGameLevel(int gameLevel) {
		this.gameLevel = gameLevel;
	}

	public int getGameLevel() {
		return this.gameLevel;
	}

	public List<User> getUsersIn() {
		return this.usersIn;
	}

	public void addUserIn(User user) {
		this.usersIn.add(user);
	}

	public void removeUserIn(String username) {
		this.usersIn = this.usersIn.stream().filter(user -> !user.getUsername().equals(username)).collect(Collectors.toList());
	}

	public boolean isUserIn(String username) {
		boolean isUserIn = false;

		for(int i=0; i<this.usersIn.size() && !isUserIn; i++) {
			isUserIn = this.usersIn.get(i).getUsername().equals(username);
		}

		return isUserIn;
	}

	public boolean isEmpty() {
		return this.usersIn.size()>0;
	}

	public void addUserInvited(User user) {
		this.usersInvited.add(user);
	}

	public void removeUserInvited(String username) {
		this.usersInvited = this.usersInvited.stream().filter(user -> !user.getUsername().equals(username)).collect(Collectors.toList());
	}

	public boolean isUserInvited(String username) {
		boolean isUserInvited = false;

		for(int i=0; i<this.usersInvited.size() && !isUserInvited; i++) {
			isUserInvited = this.usersInvited.get(i).getUsername().equals(username);
		}

		return isUserInvited;
	}

	public void acceptInvitation(String username) {
		User user;
		boolean done = false;
		for(int i=0; i<this.usersInvited.size() && !done; i++) {
			if(this.usersInvited.get(i).getUsername().equals(username)){ 
				user = this.usersInvited.get(i);
				this.addUserIn(user);
				this.usersInvited.remove(user);
				done = true;
			}
		}
	}

	public User getLobbyAdmin() {
		return this.lobbyAdmin;
	}

	public void setLobbyAdmin(User lobbyAdmin) {
		this.lobbyAdmin = lobbyAdmin;
	}

}
