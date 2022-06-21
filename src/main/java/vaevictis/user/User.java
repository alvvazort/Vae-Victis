package vaevictis.user;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.hibernate.envers.Audited;

import org.hibernate.envers.NotAudited;

import org.hibernate.validator.constraints.Length;

import lombok.Getter;
import lombok.Setter;
import vaevictis.statistics.Achievement;

@Getter
@Setter
@Audited
@Entity
@Table(name = "users")
public class User {
	@Id
	@Length(min = 3, max = 16)
	String username;
	
	@Length(min = 8, max = 16)
	@JsonIgnore
	String password;
	
	boolean enabled;
	
	@NotAudited
	private Integer wins;

	@NotAudited
	@ManyToMany(fetch = FetchType.EAGER)
	private Set<Achievement> achievements;
	
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "user")
	@OnDelete(action = OnDeleteAction.CASCADE)
	@JsonIgnore
	private Set<Authorities> authorities;

	public User() {

	}

	public User(String username, String password) {
		this.username = username;
		this.password = password;
		this.enabled = true;
		this.setWins(0);
		this.setAchievements(new HashSet<Achievement>() {
			
		});
	}

	public void setWins(Integer wins){
		this.wins = wins;
	}
}
