package vaevictis.user;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManagerFactory;

import org.hibernate.envers.AuditReader;
import org.hibernate.envers.AuditReaderFactory;
import org.hibernate.envers.RevisionType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import vaevictis.auditing.UserRevEntity;
import vaevictis.friends.FriendsService;


@Service
public class UserService {
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private AuthoritiesService authoritiesService;

	@Autowired
	private FriendsService friendsService;

	@Autowired
	public UserService(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	@Autowired
	private EntityManagerFactory factory;

	@Transactional
	public void saveUser(User user) throws DataAccessException {
		user.setEnabled(true);
		userRepository.save(user);
		
		//creating authorities
		authoritiesService.saveAuthorities(user.getUsername(), "user");
	}

	@Transactional
	public void deleteUser(String username) throws DataAccessException {
		userRepository.deleteByUsername(username);
	}

	public Optional<User> findUser(String username) {
		return userRepository.findById(username);
	}

	
	@Transactional(readOnly = true)
	public Iterable<User> findUserByUsername(String username) throws DataAccessException{
		return userRepository.findLikeUsername(username);
	}
	

    public Iterable<User> findAll() {
        return userRepository.findAll();
    }
  
    public String createFriendPetition(String origin, User destiny) {
		User originUser = findUser(origin).get();

		return friendsService.createFriendPetition(originUser,destiny);
    }

	public boolean isFriend(String destiny) {

		boolean res = true;
		UserDetails principal =(UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if(principal.getUsername()==destiny|| !this.friendsService.findByUsername(principal.getUsername(), destiny).isEmpty())
		{
			res=false;
		}
		return res;
    }

	public void removeFriendPetition(String origin, String destiny) {
		 friendsService.removeFriendPetition(origin,destiny);
	}

	public boolean imDestiny(Principal principal, User origin){
		
		
		return this.friendsService.getPetitions(principal.getName()).contains(origin);
	}

	public List<String> getUserAudit(String username){
		
		AuditReader auditReader = AuditReaderFactory.get(factory.createEntityManager());
		List<Object[]> revisions = auditReader.createQuery().forRevisionsOfEntity(User.class, false, false).getResultList();

		List<String> revsUser = new ArrayList<String>();

		if(revisions.size()>0){
			//Creación del usuario
			for (Object[] data : revisions){
				//Usuario que ha sido creado
				User user = (User)data[0];
				
				if (user.getUsername().equals(username)){
					//UserRevEntity asociado. Contiene el user que lo creó y cuándo
					UserRevEntity ure = (UserRevEntity) data[1];

					String creator = ure.getUsername();
					if(ure.getUsername().equals("anonymousUser") || ure.getUsername().equals(username)) creator = "itself";

					//Tipo de modificación
					RevisionType rt = (RevisionType) data[2];

					//Nos interesa la creación
					if(rt.equals(RevisionType.ADD)){
						revsUser.add("(User created on " + ure.getRevisionDate().toString() + " by " + creator + ")");
					}
				}
			}
			
			if(revsUser.size()==0){
				revsUser.add("(User created by server)");
			}
			//Ediciones del usuario
			for (Object[] data : revisions){
				//Usuario que ha sido editado
				User user = (User)data[0];

				if (user.getUsername().equals(username)){
					//UserRevEntity asociado. Contiene el user que lo modificó y cuándo
					UserRevEntity ure = (UserRevEntity) data[1];

					String creator = ure.getUsername();
					if(ure.getUsername().equals("anonymousUser") || ure.getUsername().equals(username)) creator = "itself";

					//Tipo de modificación
					RevisionType rt = (RevisionType) data[2];

					//Nos interesan las ediciones
					if(!rt.equals(RevisionType.ADD)){
						revsUser.add("(User modified on " + ure.getRevisionDate().toString() + " by " + creator + ")");
					}
				}
			}
		}
		else{
			revsUser.add("(User created by server)");
		}
		
		return revsUser;
	}
	
	public List<List<String>> paginationOfUsers(){
		List<List<String>> res = new ArrayList<>();
		List<String> pag = new ArrayList<>();
		Iterable<User> allUsers = this.userRepository.findAll();
		Iterator<User> users = allUsers.iterator();
		
		while(users.hasNext()){
			if(pag.size() < 4){		// 5 es el indice de paginacion
				pag.add(users.next().getUsername());
			}
			else{
				pag.add(users.next().getUsername());
				res.add(pag);
				pag = new ArrayList<>();
			}
		}
		if(!pag.isEmpty())
			res.add(pag);
			
		return res; 
	}
}
