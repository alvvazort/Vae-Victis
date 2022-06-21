package vaevictis.friends;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import lombok.Getter;
import lombok.Setter;
import vaevictis.user.User;

@Getter
@Setter
@Entity
@IdClass(FriendsId.class)
@Table(name = "Friends")
public class Friends implements Serializable{
    @Id
    @ManyToOne()
    @OnDelete(action = OnDeleteAction.CASCADE)
	private User origin;

    @Id
    @ManyToOne()
    @OnDelete(action = OnDeleteAction.CASCADE)
	private User destiny;
    
    private boolean request;
    //request = true when is pending. request = false when accepted. if refused is deleted.
    
}
