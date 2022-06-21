package vaevictis.web;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import vaevictis.friends.FriendsService;
import vaevictis.lobby.Lobby;
import vaevictis.lobby.LobbyService;
import vaevictis.model.Person;

@Controller
public class WelcomeController {
	
	private final LobbyService lobbyService;
	private final FriendsService friendsService;

    @Autowired
    public WelcomeController(LobbyService lobbyService, FriendsService friendsService) {
        this.lobbyService = lobbyService;
		this.friendsService = friendsService;
    }

	@Autowired
	@Qualifier("sessionRegistry")
	private SessionRegistry sessionRegistry;

	@GetMapping({"/","/welcome"})
	public String welcome(Map<String, Object> model, Principal principal) {
	
	if(principal != null){
		List<Lobby> lobbies = lobbyService.getUserInvitedLobbies(principal.getName());
		model.put("showUser", principal.getName());
		model.put("lobbies", lobbies);
		model.put("friends",this.friendsService.getPetitions(principal.getName()));
		model.put("friendsList",this.friendsService.getFriends(principal.getName()));

		List<Object> principals = sessionRegistry.getAllPrincipals();

		List<String> usersNamesList = new ArrayList<String>();

		for (Object p: principals) {
			if (p instanceof User) {
				usersNamesList.add(((User) p).getUsername());
			}
		}

		model.put("loggedUsers", usersNamesList);
	}
	
	String[][] people = {{"Daniel ", "Díaz Nogales"}, {"Gonzalo ", "Martínez Martínez"}, {"Álvaro ", "Miranda Pozo"}, {"Pedro ", "Parrilla Bascón"}, {"Luis ", "Rodríguez Vidosa"}, {"Álvaro ", "Vázquez Ortiz"}};

	List<Person> members = new ArrayList<>();

	for(int i=0; i < people.length; i++) {
		Person member = new Person();

		member.setFirstName(people[i][0]);
		member.setLastName(people[i][1]);
		members.add(member);
	}
	
	model.put("members", members);
	model.put("title", "Vae Victis");
	model.put("group", "G4-5");

	
	return "welcome";
	}
}
