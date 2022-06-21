package vaevictis.game;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.socket.messaging.SessionSubscribeEvent;

import vaevictis.game.boards.action.ActionBoard;
import vaevictis.game.boards.war.GameBoard;
import vaevictis.game.boards.war.GameBoardService;
import vaevictis.game.components.card.Card;
import vaevictis.game.components.card.CardService;
import vaevictis.game.components.pieces.war.GamePieceService;
import vaevictis.game.turn.TurnService;
import vaevictis.game.turn.ws.TurnOutput;
import vaevictis.game.turn.ws.TurnRequest;
import vaevictis.game.ws.GameInit;
import vaevictis.player.Player;
import vaevictis.player.PlayerService;
import vaevictis.user.User;
import vaevictis.user.UserService;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;

@Controller
public class GameController {

	@Autowired
	GameService gameService;

	@Autowired
	GameBoardService gameBoardService;

	@Autowired
	GamePieceService gamePieceService;
	@Autowired
	CardService cardService;

	@Autowired
	PlayerService playerService;

	@Autowired
	TurnService turnService;

	@Autowired
	UserService userService;

	@Autowired
	public GameController(GameService gameService, PlayerService playerService, TurnService turnService) {
		this.turnService = turnService;
		this.gameService = gameService;
	}
	
    @GetMapping(value="/game/{gameid}")    
    public String GameRoom(Map<String, Object> model, @PathVariable(value="gameid") String roomId, Principal principal) {
		
		Optional<Game> gameFound = this.gameService.getGameById(Integer.valueOf(roomId));
		Game game = null;
		if(gameFound.isPresent()){ 
			game = gameFound.get();
		} else{
			return "404";
		}
		model.put("game", game);

		//Obtención de tableros
		GameBoard warBoard = game.getWarBoard();
		GameBoard cityStateBoard = game.getCityStateBoard();
		ActionBoard actionBoard = game.getActionBoard();
		model.put("warBoard", warBoard);
		model.put("cityStateBoard", cityStateBoard);
		model.put("actionBoard", actionBoard);


		Optional<User> userFound = null;
		User user = null;
		String username = "";
		Player currentPlayer = null;

		if(principal != null){ //comprueba que esté logueado
			userFound = this.userService.findUser(principal.getName());

			if(userFound.isPresent()) {
				user = userFound.get();
				username = user.getUsername();

				if(game.isPlayerIn(username)){//comprueba que el usuario esté en la partida
					List<Player> listPlayers= game.getPlayersIn();
					for(Player p : listPlayers) {
						if(p.getUser() == user) currentPlayer = p;
					}
				}
				else{
					model.put("errorMessage", "No tienes permiso para acceder a la partida");
					return "error";
				}
				

				String warGoal = currentPlayer.getWarGoal();
				List<String> war = Arrays.asList(warGoal);
				String curiaGoal = currentPlayer.getCuriaGoal();
				model.put("warGoal", war);
				model.put("curiaGoal", curiaGoal);
				model.put("gameId", roomId);
			}
		}
		else{
			model.put("errorMessage", "No tienes permiso para acceder a la partida");
			return "error";
		}
		
		//String warGoals = null, curiaGoals = null;
		
		//Optional<Player> player = this.playerService.getPlayerByUser(user);
		//if(player.isPresent())
		//{
		//	warGoals = player.get().getWarGoal().toString();
		//	curiaGoals = player.get().getCuriaGoal().toString();
		//}
		
		model.put("user", user);
		model.put("username", username);

        return "game/room";
    }

	
	@Autowired
	private SimpMessagingTemplate template;

	@Autowired
    public void SubscribeListener(SimpMessagingTemplate template) {
        this.template = template;
    }

	public List<Integer> getDiceResult(){
		List<Integer> diceResult = new ArrayList<Integer>();
		diceResult.add(ThreadLocalRandom.current().nextInt(0, 6));
		diceResult.add(ThreadLocalRandom.current().nextInt(0, 6));
		diceResult.add(ThreadLocalRandom.current().nextInt(0, 6));

		return diceResult;
	}
	
	@MessageMapping("/game")
    @SendTo("/game/turn")
    public TurnOutput TurnController(TurnRequest turnRequest, Principal principal) {
		TurnOutput ret = new TurnOutput();
		Optional<User> usersTurn = this.userService.findUser(principal.getName());

		

		if(usersTurn.isPresent()) {
			User user = usersTurn.get();
			Optional<Player> playersTurn = this.playerService.getPlayerByUser(user);
			if(playersTurn.isPresent()) {
				Player player = playersTurn.get();
				Optional<Game> playersGame = this.gameService.getUserPlayingGame(player.getUser().getUsername());

				if(playersGame.isPresent()) {
					Game game = playersGame.get();

					User winner= this.gameService.checkWinner(game);
					if(winner!=null){ //Comprueba si se ha ganado o perdido la partida 
						game.setWinner(winner);	
						ret.setWinner(winner);
						return ret;
					}

					if(turnRequest.getCardId()!=null){
						Card card = this.cardService.getCardById(turnRequest.getCardId()).get();
						ret.setCardUsedId(card.getId());
						ret.setCardUsedIndex(card.getIndex());
						ret.setPlayersTurn(player);
						ret.setDiscard(turnRequest.getDiscard());
						if(turnRequest.getDiscard()!=null){ // Si está descartando la carta
							this.gameService.deleteCard(turnRequest.cardId);
						}else{ // Si la está usando
							this.gameService.useCard(game,player, ret,turnRequest);
						}
						
						return ret;
					}
					else{
					
					if(game.getPlayersTurn().getPlayer().getId()==player.getId()) {
						game.getPlayersTurn().getPhase().nextState();
						this.gameService.setTurnPhase(game, turnRequest, ret);
					} else {
						ret.setWarning("No es tu turno");
					}

					return ret;
				}}
			}
		}

		return ret;
	}


	
	@EventListener
    public void GameLoader(SessionSubscribeEvent event) {
		String username = event.getUser().getName();

		Optional<Game> g = this.gameService.getUserPlayingGame(username);
		if(g.isPresent()) {
			Game game = g.get();
			System.out.println(game.getPlayersTurn().getPhase().getState().getName());
			game.getPlayersTurn().getPhase().nextState();
        	this.template.convertAndSendToUser(username, "/game/load", new GameInit(game));
		}
    }
}
