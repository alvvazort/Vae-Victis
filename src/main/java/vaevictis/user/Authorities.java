package vaevictis.user;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.Size;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.hibernate.envers.Audited;

import lombok.Getter;
import lombok.Setter;
import vaevictis.model.BaseEntity;

@Getter
@Setter
@Audited
@Entity
@Table(name = "authorities")
public class Authorities extends BaseEntity{
	
	@ManyToOne
	@JoinColumn(name = "username")
	@OnDelete(action = OnDeleteAction.CASCADE)
	User user;
	
	@Size(min = 3, max = 50)
	String authority;
	
	
}
