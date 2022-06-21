package vaevictis.lobby;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import vaevictis.friends.FriendsService;
import vaevictis.game.Game;
import vaevictis.game.GameService;
import vaevictis.lobby.ws.lobby.LobbyRequest;
import vaevictis.lobby.ws.lobby.LobbySearch;
import vaevictis.lobby.ws.lobby.OutputLobbyRequest;
import vaevictis.model.WebSocket.Message;
import vaevictis.model.WebSocket.OutputMessage;
import vaevictis.user.User;
import vaevictis.user.UserService;

import java.security.Principal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Controller
public class LobbyController {

    private final LobbyService lobbyService;
    @Autowired
    private final UserService userService;
    private final GameService gameService;
    private final FriendsService friendsService;
    private final List<LobbySearch> lobbySearchs;

    @Autowired
    public LobbyController(LobbyService lobbyService, UserService userService, FriendsService friendsService, GameService gameService) {
        this.lobbyService = lobbyService;
        this.userService = userService;
        this.friendsService = friendsService;
        this.gameService = gameService;
        this.lobbySearchs = new ArrayList<LobbySearch>();
    }

    @Autowired
	@Qualifier("sessionRegistry")
	private SessionRegistry sessionRegistry;

    @GetMapping(value="/lobby/{roomid}")    
    public String LobbyRoom(Map<String, Object> model, @PathVariable(value="roomid") int roomId, Principal principal) {
        String username = principal.getName();
        Lobby lobby = this.lobbyService.getLobby(roomId);

        if(lobby!=null && lobby.isUserInvited(username)){
            if(lobby.getUsersIn().size() < lobby.getPlayerNumber()){
                this.lobbyService.cleanUserLobbies(username);
                lobby.acceptInvitation(username);
                this.lobbyService.saveLobby(lobby);
            }
            else{
                model.put("errorMessage", "La sala está llena");
                return "error";
            }
        }
        
        if(lobby!=null && lobby.isUserIn(username)) {
            model.put("lobby", lobby);
            model.put("userLogged", username);
            model.put("showUser", username);
            String[] numRom = {"I", "II", "III", "IV", "V", "VI"};
            model.put("numerosRomanos", numRom);
            
            model.put("friends",this.friendsService.getPetitions(principal.getName()));
	        model.put("friendsList",this.friendsService.getFriends(principal.getName()));

            List<Object> principals = sessionRegistry.getAllPrincipals();

            List<String> usersNamesList = new ArrayList<String>();

            for (Object p: principals) {
                if (p instanceof org.springframework.security.core.userdetails.User) {
                    usersNamesList.add(((org.springframework.security.core.userdetails.User) p).getUsername());
                }
            }

            model.put("loggedUsers", usersNamesList);

            return "lobby/room";
        } else {
            return "404";
        }
    }

    @GetMapping(value = "/lobby/logout")
    public ModelAndView lobbyLogout(Principal principal) {
        String username = principal.getName();
        this.lobbyService.cleanUserLobbies(username);
        return new ModelAndView("redirect:/");
    } 

    @GetMapping(value = "/lobby/")
	public ModelAndView createLobby(Map<String, Object> model, Principal principal) {

        String view = "/404";

        Optional<User> userFound = this.userService.findUser(principal.getName());
        if(userFound.isPresent()) {
            User user = userFound.get();
            Lobby lobby = new Lobby(user);

            this.lobbyService.cleanUserLobbies(user.getUsername());
            lobby = this.lobbyService.saveLobby(lobby);

            view = "/lobby/"+lobby.getId().toString();
        }
        return new ModelAndView("redirect:"+view);
	}

    @PostMapping(value = "/lobby/invite/{roomid}")
    public ModelAndView inviteFriendToLobby(Map<String, Object> model, @PathVariable(value="roomid") int roomId, @RequestParam String username, Principal principal) {
        Lobby lobby = this.lobbyService.getLobby(roomId);

        Optional<User> userFound = this.userService.findUser(username);
        if(userFound.isPresent()) {
            User user = userFound.get();
            if(!user.getUsername().equals(principal.getName())) {
                lobby.addUserInvited(user);
                this.lobbyService.saveLobby(lobby);
            }
        }

		return new ModelAndView("redirect:/lobby/"+roomId);
	}
    
    @PostMapping(value = "/lobby/{roomid}")
    public ModelAndView changeConfigLobby(Map<String, Object> model, @PathVariable(value="roomid") int roomId, @RequestParam int userNumber, @RequestParam int gameLevel) {
        Lobby lobby = this.lobbyService.getLobby(roomId);
        lobby.setGameLevel(gameLevel);
        lobby.setPlayerNumber(userNumber);
        this.lobbyService.saveLobby(lobby);

		return new ModelAndView("redirect:/lobby/"+roomId);
	}


    @MessageMapping("/chat")
    @SendTo("/topic/messages")
    public OutputMessage send(Message message) throws Exception {
        String time = new SimpleDateFormat("HH:mm").format(new Date());
        return new OutputMessage(message.getFrom(), message.getText(), time);
    }

    @MessageMapping("/matchmaking")
    @SendTo("/lobbymatch/matchmaking")
    public OutputLobbyRequest send(LobbyRequest lobbyRequest, Principal principal) {
        Optional<User> lobbyAdmin = this.userService.findUser(principal.getName());
        LobbySearch search = null;

        if(lobbyAdmin.isPresent()) {
            User userAdmin = lobbyAdmin.get();
            Lobby lobby = this.lobbyService.getUserLobbyAdmin(userAdmin);

            if(lobbyRequest.getAction()==1) { // un usuario LobbyAdmin está buscando partida
                // combina si se pueden mezclar dos lobbies o crea un nuevo LobbySearch
                search = this.lobbyService.addLobbySearch(lobby, this.lobbySearchs);

                if(search.isSatified()) {
                    // se crea la partida y se añaden todos los jugadores del lobby
                    Game game = this.gameService.createGame(search);

                    // se borra la búsqueda completa antes de redirigir a la partida
                    this.lobbyService.removeLobbyRequest(search, this.lobbySearchs);


                    return new OutputLobbyRequest(game.getId().toString());
                }

            } else { // un usuario LobbyAdmin ha cancelado la búsqueda de partida
                search = this.lobbyService.removeLobbyRequest(lobby, this.lobbySearchs);
            }
        }

        return new OutputLobbyRequest(search);
    }
}
