package vaevictis.game;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import vaevictis.game.boards.action.ActionBoard;
import vaevictis.game.boards.action.ActionBoardService;
import vaevictis.game.boards.war.GameBoard;
import vaevictis.game.boards.war.GameBoardService;
import vaevictis.game.boards.war.TypeGameBoard;
import vaevictis.game.components.card.Card;
import vaevictis.game.components.card.CardService;
import vaevictis.game.components.pieces.war.GamePiece;
import vaevictis.game.components.pieces.war.GamePieceService;
import vaevictis.game.turn.Turn;
import vaevictis.game.turn.TurnService;
import vaevictis.game.turn.ActionPhase.ActionPhaseOptionsService;
import vaevictis.game.turn.DiceResult.DiceResultService;
import vaevictis.game.turn.Phases.Phase;
import vaevictis.game.turn.Phases.PhaseService;
import vaevictis.game.turn.Phases.PhaseStateService;
import vaevictis.game.turn.Phases.Doom.DoomState;
import vaevictis.game.turn.ws.TurnOutput;
import vaevictis.game.turn.ws.TurnRequest;
import vaevictis.lobby.ws.lobby.LobbySearch;
import vaevictis.player.Player;
import vaevictis.player.PlayerService;
import vaevictis.user.User;
import vaevictis.user.UserService;

@Service
public class GameService {

    @Autowired
    private GameRepository gameRepository;

    @Autowired
    private PlayerService playerService;

    @Autowired
    private UserService userService;
    @Autowired
    private CardService cardService;

    @Autowired
    public TurnService turnService;

    @Autowired 
    GameBoardService gameBoardService;
    
    @Autowired 
    ActionBoardService actionBoardService;

    @Autowired 
    public DiceResultService diceResultService;

    @Autowired
    GamePieceService gamePieceService;

    @Autowired
    public PhaseService phaseService;

    @Autowired
    public PhaseStateService phaseStateService;

    @Autowired
    public ActionPhaseOptionsService actionPhaseOptionsService;

    @Autowired
    public GameService(GameRepository gameRepository, PlayerService playerService, UserService userService, TurnService turnService, DiceResultService diceResultService, GamePieceService gamePieceService, PhaseService phaseService, ActionPhaseOptionsService phaseOptionsService) {
        this.gameRepository = gameRepository;
        this.playerService = playerService;
        this.userService = userService;
        this.turnService = turnService;
        this.diceResultService = diceResultService;
        this.gamePieceService = gamePieceService;
        this.phaseService = phaseService;
        this.actionPhaseOptionsService = phaseOptionsService;
    }

    public Game createGame(LobbySearch lobby) {
        List<Player> playerList = new ArrayList<Player>();
        
        for(User user : lobby.getUsers()) {
            Player player = this.playerService.createPlayer(user);
            playerList.add(player);
        };
        GameBoard warBoard = gameBoardService.createGameBoard(TypeGameBoard.WARBOARD);
        GameBoard cityStateBoard = gameBoardService.createGameBoard(TypeGameBoard.CITYSTATEBOARD);
        ActionBoard actionBoard = actionBoardService.createActionBoard((4-lobby.getGameLevel())*playerList.size());
        //ActionBoard actionBoard = new ActionBoard();

        Player fPlayer = this.getRandomPlayer(playerList);

        Turn turn = new Turn(fPlayer);
        
        turn = this.turnService.createTurn(turn);


        Game game = this.gameRepository.save(new Game(playerList, turn,lobby.getLobbyAdmin(), warBoard, cityStateBoard, actionBoard));
        
        Phase phase = this.phaseService.create(new Phase(new DoomState(game, fPlayer)));

        turn.setPhase(phase);

        turn = this.turnService.createTurn(turn);

        this.turnService.setGameToTurn(game, turn);

        return game;
    }

    public List<Game> findGamesOrdered(){
        return this.gameRepository.findGamesOrdered();
    }

	private Player getRandomPlayer(List<Player> players) {
		Random rand = new Random();
		return players.get(rand.nextInt(players.size()));
	}

    public Optional<Game> getGameById(int id){
        return this.gameRepository.findById(id);
    }

    @Transactional
    public Optional<Game> getUserPlayingGame(String username){
        Optional<User> u = this.userService.findUser(username);
        if(u.isPresent()) {
            User user = u.get();
            return this.gameRepository.getUserPlayingGame(user);
        } else {
            return null;
        }
    }

    @Transactional
    public Iterable<Game> findAll(){
        return this.gameRepository.findAll();
    }

    public boolean checkSecondLimitesAction(List<Integer> action) {
        boolean ok = true;

        for(Integer i : action) {
            if(i>3) {
                ok = false;
            }
        }
        return ok;
    }

    public void addFirstLimitesAction(List<Integer> action, Game game, Player player) {

        List<Integer> moves = new ArrayList<>();
        if(action.size()==3) {
            moves = action;
        } else if(action.size()==1) {
            moves = action;
            moves.add(action.get(0));
        }

        Phase phase = game.getPlayersTurn().getPhase();

        Turn turn = this.turnService.createTurn(new Turn(game, player, phase));
        game.setPlayersTurn(turn);

        this.movePieces(game, moves, new ArrayList<>());
    }

    
    
    public Game saveGame(Game game) {
        return this.gameRepository.save(game);
    }

    public void deleteGame(Game game){
        this.gameRepository.delete(game);
    }

    public void allLose2Coins(Game game){
        List<Player> players=game.getPlayersIn();

        for(int i=0; i<players.size();i++){
            Player player= players.get(i);
            player.setCoins(player.getCoins()-2);
        }
    }

    private void checkIfLose(Game game, GamePiece gamePiece){
        Integer numPlayers=game.getPlayersIn().size();

        if(numPlayers<=4 && gamePiece.getIndex()==4){ // Evento de derrota si hay 4 o menos jugadores
            game.matchLose=true;
        }else if(numPlayers<=5 && gamePiece.getIndex()==2){
            game.matchLose=true;
        }else if(gamePiece.getIndex()==0){
            game.matchLose=true;
        }
    }

    private String checkGameBoardEvents(Game game,Integer indexPiece){
        String eventCityStateBoard= null;
        List<GamePiece> cityPieces=game.getCityStateBoard().getPieces();
        GamePiece gamePiece= cityPieces.get(indexPiece-4);

        if(gamePiece.getIndex()==8){ // Evento del tablero "Haz descender otro contador"
            descendOtherCounter(game,indexPiece);
        }else if(gamePiece.getIndex()==7){
            switch (gamePiece.getType()){ // Identifica a que casilla ha caido exactamente
                case "DIVITIAE":
                    eventCityStateBoard="DISCARD1CARD";
                    break;
                case "SANITAS":
                    eventCityStateBoard="ALLLOSE2COINS";
                    allLose2Coins(game);
                    
                break;
                case "RAPINA":
                    eventCityStateBoard="YOULOSE1COIN";
                    payCoins(game, 1, game.getPlayersTurn().getPlayer());

            }
        }else if(gamePiece.getIndex()==5){
            switch (gamePiece.getType()){ // Identifica a que casilla ha caido exactamente
                case "DIVITIAE":
                    eventCityStateBoard="DISCARD2CARD";
                    break;
                case "SANITAS":
                    eventCityStateBoard="ALLLOSE2COINSANDWARS1";
                    allLose2Coins(game);

                    List<Integer> piecesMove= Arrays.asList(0,1,2,3);
                    move(game, piecesMove, false); //Mueve hacia atrás cada guerra;

                break;
                case "RAPINA":
                    eventCityStateBoard="YOULOSE2COIN";
                    payCoins(game, 2, game.getPlayersTurn().getPlayer());

            }
        }
        
        return eventCityStateBoard;
    }

    public String move(Game game, List<Integer> piecesMove, Boolean forwardOrBack) { //Mueve hacia delante o hacia atrás
        String eventCityStateBoard=null;

		GameBoard warBoard = game.getWarBoard();
		GameBoard cityStateBoard = game.getCityStateBoard();
		List<GamePiece> piecesWar= warBoard.getPieces();
		List<GamePiece> piecesCity= cityStateBoard.getPieces();
		Integer addIndex=0;

		if(forwardOrBack){
			addIndex=1;
		}else{
			addIndex=-1;
		}
		for(int i=0; i<piecesMove.size();i++){ //Asigna nuevos index
			GamePiece gamePiece =null;
            Integer indexPiece=piecesMove.get(i);
            int numPiecesWarBoard=4;
            int numPiecesCityStateBoard=3;
            int numPieces=numPiecesWarBoard+numPiecesCityStateBoard;

			if(indexPiece<numPiecesWarBoard){ // Si el indice va de 0 a 3, es del tablero de guerra

                int deadZone=0;
                int winZone=12;
				gamePiece= piecesWar.get(indexPiece);
                if(gamePiece.getIndex()<winZone){ // Si todavia no se ha ganado la guerra entonces:

                    gamePiece.setIndex(gamePiece.getIndex()+addIndex);

                    if(gamePiece.getIndex()==deadZone){
                        game.matchLose=true;
                    }else if(gamePiece.getIndex()==winZone){ //Si se ha conquistado un frente se guarda en game
                        switch(indexPiece){
                            case 0:
                            game.hispaniaWin=true;
                            break;
                            case 1:
                            game.galliaWin=true;
                            break;
                            case 2:
                            game.britanniaWin=true;
                            break;
                            case 3:
                            game.germaniaWin=true;
                        }
                    }
                }
			}else if(indexPiece<numPieces){ // Si el indice va de 4 a 7, es del tablero de estado de ciudad
				gamePiece= piecesCity.get(indexPiece-4);
				gamePiece.setIndex(gamePiece.getIndex()+addIndex);

                eventCityStateBoard= checkGameBoardEvents(game,indexPiece);
                checkIfLose(game,gamePiece);
                

			}
			
			gamePieceService.savePiece(gamePiece); // Guardar pieza
		}
        return eventCityStateBoard;
	}

	public String movePieces(Game game, List<Integer> piecesGoForward, List<Integer> piecesGoBack) { //Devuelve también un evento del tablero
        String eventCityStateBoard=null;  
		if(!piecesGoForward.isEmpty()){
			eventCityStateBoard=move(game, piecesGoForward, true);
		}
		if(!piecesGoBack.isEmpty()){
			eventCityStateBoard=move(game, piecesGoBack, false);
		}
        

		
		this.saveGame(game);// Guardar partida

        return eventCityStateBoard;
	}

    private void descendOtherCounter(Game game,Integer index){ // Regla del juego de haz descender otro contador
        List<GamePiece> cityPieces= game.getCityStateBoard().getPieces();

        GamePiece gamePieceToChange=cityPieces.get((index-4+1)%cityPieces.size());
        gamePieceToChange.setIndex(gamePieceToChange.getIndex()-1);

        gamePieceService.savePiece(gamePieceToChange);
        checkIfLose(game,gamePieceToChange);
    }
    
    public void payCoins(Game game, int coins, Player player){
        
        this.playerService.setCoins(player,coins);
        game.getActionBoard().addCoins(coins);
        actionBoardService.savePiece(game.getActionBoard());
        this.saveGame(game);
    }
    public void drawCard(boolean tipo, Player player){
        
        this.playerService.drawCard(tipo, player);
    }


   

    public void saveActionCuria(Game game, Boolean plusOrMinus, Integer section){
        int add=-1;
        if(plusOrMinus){
            add=1;
        }

        switch(section){
            case 1:
            game.senateLeft+=add;
            if(game.senateLeft>3 || game.senateLeft<0)
                game.senateLeft-=add;
            break;
            case 2:
            game.senateMid+=add;
            if(game.senateMid>3 || game.senateMid<0)
                game.senateMid-=add;
            break;
            case 3:
            game.senateRight+=add;
            if(game.senateRight>3 || game.senateRight<0)
                game.senateRight-=add;

        }
        saveGame(game);
    }

    public void setTurnPhase(Game game, TurnRequest turnRequest, TurnOutput turnOutput) {
        game.getPlayersTurn().getPhase().phaseHandle(this, turnRequest, turnOutput);
    }

    
    public void useCard(Game game, Player player, TurnOutput ret,TurnRequest turnRequest) {
        Card card = this.cardService.getCardById(turnRequest.getCardId()).get();
        switch(card.getIndex()){
            case 0:
                this.card0(game,player,ret);
            break;
            case 5:
                List<Integer> a = turnRequest.getSelectedCityState();
                List<Integer> piecesGoForward= new ArrayList<Integer>();
                List<Integer> piecesGoBack=new ArrayList<Integer>();
                for (int pieceBack : a){
                    switch(pieceBack){
                        case 1: piecesGoBack.add(4); break;
                        case 2: piecesGoForward.add(4); break;
                        case 3: piecesGoBack.add(5); break;
                        case 4: piecesGoForward.add(5); break;
                        case 5: piecesGoBack.add(6); break;
                        case 6: piecesGoForward.add(6); break;
                    }}
                ret.setPiecesGoForward(piecesGoForward);
                ret.setPiecesGoBack(piecesGoBack);
                this.movePieces(game, piecesGoForward, piecesGoBack);
            break;
            case 6:
                if(game.getActionBoard().treasury>1){
                    game.getActionBoard().treasury-=2;
                    this.playerService.setCoins(player, -2);
                    saveGame(game);
                    
                }
                else if(game.getActionBoard().treasury==1){
                    game.getActionBoard().treasury-=1;
                    this.playerService.setCoins(player, -1);
                    saveGame(game);
                }
            break;
        }
        player.getAllCards().add(card);
        this.playerService.save(player);

        this.cardService.deleteCard(card);
    }
    public void deleteCard(Integer cardId){
        Card card = this.cardService.getCardById(cardId).get();
        this.cardService.deleteCard(card);
    }


    private void card0(Game game, Player player,TurnOutput ret) {
        List<Player> g = this.gameRepository.findPlayersByGameId(game.getId());
        List<Integer> a = new ArrayList<Integer>();
        for (Player player2 : g){
            if(player.getId()!=player2.getId() && !player2.getCards().isEmpty()){
                Card card = player2.getCards().iterator().next();
                a.add(card.getId());
                this.cardService.deleteCard(card);
            }
        }
        ret.setCard0Selected(a);
    }

    public User checkWinner(Game game){
        List<Player> players=game.getPlayersIn();
        for(int i=0; i<players.size(); i++){
            if(checkPlayerWinner(game,players.get(i))) //Comprueba jugador a jugador si ha ganado
                return players.get(i).getUser();
        }
        return null;
    }

    private Boolean checkPlayerWinner(Game game,Player player){
        Boolean res=false;
        String curiaGoal= player.getCuriaGoal();
        String warGoal=player.getWarGoal();

        Boolean curiaWin=false;
        switch(curiaGoal){
            case "LEFT":
                if(game.senateLeft> game.senateMid && game.senateLeft> game.senateRight)
                    curiaWin=true;
                break;
            case "MIDDLE":
                if(game.senateMid> game.senateLeft && game.senateMid> game.senateRight)
                    curiaWin=true;
                break;
            case "RIGHT":
                if(game.senateRight> game.senateMid && game.senateRight> game.senateLeft)
                    curiaWin=true;
                break;
            case "LEFTRIGHT":
                if(game.senateLeft> 0 && game.senateRight> 0 && game.senateLeft==game.senateRight)
                    curiaWin=true;
                break;
            case "LEFTCENTER": 
                if(game.senateLeft> 0 && game.senateMid> 0 && game.senateLeft==game.senateMid)
                    curiaWin=true;
                break;
            case "RIGHTCENTER":
                if(game.senateRight> 0 && game.senateMid> 0 && game.senateRight==game.senateMid)
                    curiaWin=true;

        }

        Boolean warWin=false;
        switch(warGoal){
            case "BritanniavsGermania":
                if(game.britanniaWin && game.germaniaWin)
                    warWin=true;
                break;
            case "GalliavsBritania":
                if(game.galliaWin && game.britanniaWin)
                    warWin=true;
                break;
            case "GalliavsGermania":
                if(game.galliaWin && game.germaniaWin)
                    warWin=true;
                break;
            case "HispaniavsBritannia":
                if(game.hispaniaWin && game.britanniaWin)
                    warWin=true;
                break;
            case "HispaniavsGallia": 
                if(game.hispaniaWin && game.galliaWin)
                    warWin=true;
                break;
            case "HispaniavsGermania":
                if(game.hispaniaWin && game.germaniaWin)
                    warWin=true;
        }

        res=curiaWin&&warWin;

        return res;
    }



}
