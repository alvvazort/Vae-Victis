package vaevictis.statistics;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import vaevictis.friends.FriendsService;
import vaevictis.game.Game;
import vaevictis.game.GameService;
import vaevictis.game.components.card.Card;
import vaevictis.lobby.Lobby;
import vaevictis.lobby.LobbyService;
import vaevictis.user.User;
import vaevictis.user.UserService;

@Controller
public class StatisticsController {
    
  @Autowired
  private StatisticsService estadisticasService;

  @Autowired
  private UserService userService;

  @Autowired
  private GameService gameService;

  @Autowired
  private AchievementService achievementService;

  @Autowired
  private LobbyService lobbyService;

  @Autowired
  private FriendsService friendsService;

  @Autowired
  @Qualifier("sessionRegistry")
  private SessionRegistry sessionRegistry;


  @GetMapping({"/estadisticas/"})
  public String Estadisticas(Map<String, Object> model,Principal principal) {	 
      
    Iterable<Game> partidas = gameService.findAll();
    
    Iterable<User> usuarioActual = userService.findUserByUsername(principal.getName());
    User userActual = usuarioActual.iterator().next();

    Iterable<User> userByWins = estadisticasService.findUserOrderedByWins();

    List<Game> partidasUsuario = estadisticasService.findGamesOfUser(userActual);

    Integer userWins = estadisticasService.numberOfWinsOfUser(userActual);

    List<Integer> durationGames = estadisticasService.getGamesDuration(partidas);
    List<Integer> durationUserGames = estadisticasService.getGamesDuration(partidasUsuario);
    
    Integer numberOfPlayers = estadisticasService.numberOfPlayer(partidas);
    Integer numberOfPlayerUser = estadisticasService.numberOfPlayer(partidasUsuario);
    
    Card mostPlayedCardUser = estadisticasService.mostPlayedCardUser(partidasUsuario, userActual);
    Card mostPlayedCard = estadisticasService.mostPlayedCard(partidas);


    model.put("partidas", partidas);
    model.put("usuarioActual", userActual);
    model.put("usuariosGanadores",userByWins );
    model.put("partidasUsuarioActual", partidasUsuario);
    model.put("victoriasUsuario", userWins);
    model.put("FechaGlobal",durationGames);
    model.put("FechaPersonal",durationUserGames);
    model.put("PromedioUsuarios", numberOfPlayers);
    model.put("PromedioUsuariosUser",numberOfPlayerUser);
    model.put("cartaMasJugadaUser", mostPlayedCardUser);
    model.put("cartaMasJugada", mostPlayedCard);
    
    Iterable<Achievement> logros = achievementService.getAllAchievements();
    Set<Achievement> logrosUser = userActual.getAchievements();

    model.put("logros", logros);
    model.put("logrosUser",logrosUser);

    if(principal != null){
      List<Lobby> lobbies = lobbyService.getUserInvitedLobbies(principal.getName());
      model.put("showUser", principal.getName());
      model.put("lobbies", lobbies);
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
    }
    return "estadisticas";
  }

    @GetMapping({"/estadisticas/listOfGames"})
	  public String listOfGames(Map<String, Object> model,Principal principal) {	 

      Iterable<User> usuarioActual = userService.findUserByUsername(principal.getName());
      User userActual = usuarioActual.iterator().next();
      List<Game> partidas = estadisticasService.findGamesOrdered(userActual);

      model.put("partidas", partidas);

      return "listOfGames";
	  }

    @GetMapping({"/estadisticas/editLogro"})
	  public String editLogro(Map<String, Object> model,Principal principal) {	 

      model.put("logros", this.achievementService.getAllAchievements());
      return "updateAchievement";
	  }

    @PostMapping({"/estadisticas/editLogro/"})
	  public String editarLogro(@Valid Achievement a, BindingResult result,Map<String, Object> model) {	 
      
      if(result.hasErrors()){
        model.put("logros", this.achievementService.getAllAchievements());
        return "updateAchievement";
      }
      else{
        Achievement nuevoLogro = this.achievementService.findByName(a.getName());
        nuevoLogro.setAchievementCondition(a.getAchievementCondition());
        this.achievementService.save(nuevoLogro);
      }
      
      return "redirect:/";
	  }
}
