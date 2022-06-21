package vaevictis.game;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import vaevictis.lobby.Lobby;
import vaevictis.lobby.ws.lobby.LobbySearch;
import vaevictis.player.Player;
import vaevictis.player.PlayerService;
import vaevictis.user.User;
import vaevictis.user.UserService;

@DataJpaTest(includeFilters = @ComponentScan.Filter(Service.class))
class GameServiceTests {                
        
    @Autowired
	protected GameService gameService;
    
    @Autowired
	protected UserService userService;
    @Autowired
	protected PlayerService playerService;

    @Test
    void shouldCreateGame() {
        assertThat(gameService.getGameById(1).isPresent()).isFalse();
        User player1 = this.userService.findUser("player1").get();
        User player2 = this.userService.findUser("player2").get();
        User player3 = this.userService.findUser("player3").get();
        Lobby lobby = new Lobby(player1);
        lobby.addUserIn(player2);
        lobby.addUserIn(player3);
        LobbySearch lobbysearch = new LobbySearch(lobby);
        Game game= this.gameService.createGame(lobbysearch);

        assertThat(gameService.getGameById(game.getId()).isPresent()).isTrue();
    }

    @Test
    @Transactional
    void shouldCreateCard() {
        User user1 = this.userService.findUser("player6").get();
        Lobby lobby = new Lobby(user1);
        LobbySearch lobbysearch = new LobbySearch(lobby);
        this.gameService.createGame(lobbysearch);
        Player player1 = this.playerService.getPlayerByUser(user1).get();
        assertThat(this.playerService.getPlayerCards(player1)).isEmpty();
        this.gameService.drawCard(false, player1);
        assertThat(this.playerService.getPlayerCards(player1)).isNotEmpty();
        

    }

    @Test
    @Transactional
    @DisplayName("Should change the number of senators in a senate section (Curia)")
    void shouldSaveActionCuria() {
        User player1 = this.userService.findUser("player6").get();
        Lobby lobby = new Lobby(player1);

        LobbySearch lobbysearch = new LobbySearch(lobby);
        Game game = this.gameService.createGame(lobbysearch);

        Boolean plusOrMinus=true; // true means add a senator
        int section=2;

        this.gameService.saveActionCuria(game, plusOrMinus, section);

        assertThat(game.senateLeft==0).isTrue();
        assertThat(game.senateMid==1).isTrue();
        assertThat(game.senateRight==0).isTrue();

        this.gameService.deleteGame(game);
    }

    @Test
    @Transactional
    @DisplayName("Should change the number of senators in a senate section (Curia) two times")
    void shouldSaveActionCuriaTwoTimes() {
        User player1 = this.userService.findUser("player6").get();
        Lobby lobby = new Lobby(player1);

        LobbySearch lobbysearch = new LobbySearch(lobby);
        Game game = this.gameService.createGame(lobbysearch);

        Boolean plusOrMinus=true; // true means add a senator
        int section=2;

        this.gameService.saveActionCuria(game, plusOrMinus, section);

        assertThat(game.senateLeft==0).isTrue();
        assertThat(game.senateMid==1).isTrue();
        assertThat(game.senateRight==0).isTrue();

        plusOrMinus=false; // true means add a senator
        section=2;

        this.gameService.saveActionCuria(game, plusOrMinus, section);

        assertThat(game.senateLeft==0).isTrue();
        assertThat(game.senateMid==0).isTrue();
        assertThat(game.senateRight==0).isTrue();

        this.gameService.deleteGame(game);
    }

    @Test
    @Transactional
    @DisplayName("Should do not change the number of senators in a senate section if it would be out of range")
    void shouldSaveActionCuriaInTheLimitOfSenators() {
        User player1 = this.userService.findUser("player6").get();
        Lobby lobby = new Lobby(player1);

        LobbySearch lobbysearch = new LobbySearch(lobby);
        Game game = this.gameService.createGame(lobbysearch);

        Boolean plusOrMinus=false; // true means add a senator
        int section=1;

        this.gameService.saveActionCuria(game, plusOrMinus, section);

        assertThat(game.senateLeft==0).isTrue();
        assertThat(game.senateMid==0).isTrue();
        assertThat(game.senateRight==0).isTrue();

        this.gameService.deleteGame(game);
    }
    @Test
    @Transactional
    @DisplayName("Should change index of Pieces to move it in the gameboard")
    void shouldChangeIndexOfPieces(){
        User player1 = this.userService.findUser("player5").get();
        Lobby lobby = new Lobby(player1);

        LobbySearch lobbysearch = new LobbySearch(lobby);
        Game game = this.gameService.createGame(lobbysearch);

        List<Integer> piecesMove= Arrays.asList(1);
        Boolean forwardOrBack=true;

        this.gameService.move(game, piecesMove, forwardOrBack);

        assertThat(game.getWarBoard().getPieces().get(1).getIndex()==5).isTrue();

        this.gameService.deleteGame(game);
    }

    @Test
    @Transactional
    @DisplayName("Should change index of Pieces two times to move it in the gameboard")
    void shouldChangeIndexOfPiecesTwoTimes(){
        User player1 = this.userService.findUser("player5").get();
        Lobby lobby = new Lobby(player1);

        LobbySearch lobbysearch = new LobbySearch(lobby);
        Game game = this.gameService.createGame(lobbysearch);

        List<Integer> piecesMove= Arrays.asList(1,2,2);
        Boolean forwardOrBack=true;

        this.gameService.move(game, piecesMove, forwardOrBack);

        assertThat(game.getWarBoard().getPieces().get(1).getIndex()==5).isTrue();
        assertThat(game.getWarBoard().getPieces().get(2).getIndex()==6).isTrue();

        this.gameService.deleteGame(game);
    }

    @Test
    @Transactional
    @DisplayName("Should change index from 4 to 3")
    void shouldMovePiecesToBack(){
        User player1 = this.userService.findUser("player5").get();
        Lobby lobby = new Lobby(player1);

        LobbySearch lobbysearch = new LobbySearch(lobby);
        Game game = this.gameService.createGame(lobbysearch);

        List<Integer> piecesMove= Arrays.asList(1);
        Boolean forwardOrBack=false;

        this.gameService.move(game, piecesMove, forwardOrBack);

        assertThat(game.getWarBoard().getPieces().get(1).getIndex()==3).isTrue();

        this.gameService.deleteGame(game);
    }

    @Test
    @Transactional
    @DisplayName("Should lose a match by losing a war")
    void shouldLoseAMatchLosingAWar(){
        User player1 = this.userService.findUser("player5").get();
        Lobby lobby = new Lobby(player1);

        LobbySearch lobbysearch = new LobbySearch(lobby);
        Game game = this.gameService.createGame(lobbysearch);

        List<Integer> piecesMove= Arrays.asList(1,1,1,1);
        Boolean forwardOrBack=false;

        this.gameService.move(game, piecesMove, forwardOrBack);

        assertThat(game.matchLose).isTrue();

        this.gameService.deleteGame(game);
    }

    @Test
    @Transactional
    @DisplayName("Should win the war of Hispania")
    void shouldWindHispaniaWar(){
        User player1 = this.userService.findUser("player5").get();
        Lobby lobby = new Lobby(player1);

        LobbySearch lobbysearch = new LobbySearch(lobby);
        Game game = this.gameService.createGame(lobbysearch);

        List<Integer> piecesMove= Arrays.asList(0,0,0,0,0,0,0,0);
        Boolean forwardOrBack=true;

        this.gameService.move(game, piecesMove, forwardOrBack);
        assertThat(game.getWarBoard().getPieces().get(0).getIndex()==12).isTrue();
        assertThat(game.hispaniaWin).isTrue();

        this.gameService.deleteGame(game);

    }

    @Test
    @Transactional
    @DisplayName("Should not move back")
    void shouldNotMoveBackIftheWarIsWon(){
        User player1 = this.userService.findUser("player5").get();
        Lobby lobby = new Lobby(player1);

        LobbySearch lobbysearch = new LobbySearch(lobby);
        Game game = this.gameService.createGame(lobbysearch);

        List<Integer> piecesMove= Arrays.asList(0,0,0,0,0,0,0,0);
        Boolean forwardOrBack=true;

        this.gameService.move(game, piecesMove, forwardOrBack);
        piecesMove= Arrays.asList(0,0);
        forwardOrBack=false;

        assertThat(game.getWarBoard().getPieces().get(0).getIndex()==12).isTrue();
        assertThat(game.hispaniaWin).isTrue();

        this.gameService.deleteGame(game);

    }

    @Test
    @Transactional
    @DisplayName("Should move down other game piece when one game piece is in index 8")
    void shouldDescendOtherCounter(){
        User player1 = this.userService.findUser("player5").get();
        Lobby lobby = new Lobby(player1);

        LobbySearch lobbysearch = new LobbySearch(lobby);
        Game game = this.gameService.createGame(lobbysearch);

        List<Integer> piecesMove= Arrays.asList(4);
        Boolean forwardOrBack=false;

        this.gameService.move(game, piecesMove, forwardOrBack);


        assertThat(game.getCityStateBoard().getPieces().get(0).getIndex()==8).isTrue();
        assertThat(game.getCityStateBoard().getPieces().get(1).getIndex()==8||game.getCityStateBoard().getPieces().get(2).getIndex()==8).isTrue();


        this.gameService.deleteGame(game);

    }

    @Test
    @Transactional
    void shouldDescendOtherCounterTwo(){
        User player1 = this.userService.findUser("player5").get();
        Lobby lobby = new Lobby(player1);

        LobbySearch lobbysearch = new LobbySearch(lobby);
        Game game = this.gameService.createGame(lobbysearch);

        List<Integer> piecesMove= Arrays.asList(5);
        Boolean forwardOrBack=false;

        this.gameService.move(game, piecesMove, forwardOrBack);


        assertThat(game.getCityStateBoard().getPieces().get(1).getIndex()==8).isTrue(); //Otra pieza distinta a la anterior
        assertThat(game.getCityStateBoard().getPieces().get(0).getIndex()==8||game.getCityStateBoard().getPieces().get(2).getIndex()==8).isTrue();


        this.gameService.deleteGame(game);
    }

    @Test
    @Transactional
    @DisplayName("Test if lose when money game piece descend to 4 index and there are less than 4 players")
    void shouldLoseIfMoneyDescendTo4IndexAndLessThan4Players(){
        User player1 = this.userService.findUser("player5").get();
        Lobby lobby = new Lobby(player1);

        LobbySearch lobbysearch = new LobbySearch(lobby);
        Game game = this.gameService.createGame(lobbysearch);

        List<Integer> piecesMove= Arrays.asList(4,4,4,4,4);
        Boolean forwardOrBack=false;

        this.gameService.move(game, piecesMove, forwardOrBack);

        assertThat(game.getCityStateBoard().getPieces().get(0).getIndex()== 4).isTrue();
        assertThat(game.matchLose).isTrue();

        this.gameService.deleteGame(game);
    }

    @Test
    @Transactional
    @DisplayName("Test if every player lose 2 coins")
    void shouldLose2CoinsEveryPlayer(){
        User player1 = this.userService.findUser("player5").get();
        User player2 = this.userService.findUser("player4").get();

        Lobby lobby = new Lobby(player1);
        lobby.addUserIn(player2);

        LobbySearch lobbysearch = new LobbySearch(lobby);
        Game game = this.gameService.createGame(lobbysearch);

        assertThat(game.getPlayersIn().get(0).getCoins()==8).isTrue();
        assertThat(game.getPlayersIn().get(1).getCoins()==8).isTrue();

        this.gameService.allLose2Coins(game);

        assertThat(game.getPlayersIn().get(0).getCoins()==6).isTrue();
        assertThat(game.getPlayersIn().get(1).getCoins()==6).isTrue();

        this.gameService.deleteGame(game);
    }
    @Test
    @Transactional
    @DisplayName("Test if every player lose 2 coins when Sanitas is in index 7")
    void shouldLose2CoinsEveryPlayerWhenSanitasIsInIndex7(){
        User player1 = this.userService.findUser("player3").get();
        User player2 = this.userService.findUser("player4").get();

        Lobby lobby = new Lobby(player1);
        lobby.addUserIn(player2);

        LobbySearch lobbysearch = new LobbySearch(lobby);
        Game game = this.gameService.createGame(lobbysearch);

        assertThat(game.getPlayersIn().get(0).getCoins()==8).isTrue();
        assertThat(game.getPlayersIn().get(1).getCoins()==8).isTrue();

        List<Integer> piecesMove= Arrays.asList(5,5);
        Boolean forwardOrBack=false;

        this.gameService.move(game, piecesMove, forwardOrBack);

        assertThat(game.getPlayersIn().get(0).getCoins()==6).isTrue();
        assertThat(game.getPlayersIn().get(1).getCoins()==6).isTrue();

        this.gameService.deleteGame(game);
    }

    @Test
    @Transactional
    @DisplayName("Test if the player of the turn lose one coin when Rapina is in index 7")
    void shouldLose1CoinsThePlayerWhenRapinaIsInIndex7(){
        User player1 = this.userService.findUser("player1").get();
        User player2 = this.userService.findUser("player2").get();

        Lobby lobby = new Lobby(player1);
        lobby.addUserIn(player2);

        LobbySearch lobbysearch = new LobbySearch(lobby);
        Game game = this.gameService.createGame(lobbysearch);

        assertThat(game.getPlayersIn().get(0).getCoins()==8).isTrue();
        assertThat(game.getPlayersIn().get(1).getCoins()==8).isTrue();

        List<Integer> piecesMove= Arrays.asList(6,6);
        Boolean forwardOrBack=false;

        this.gameService.move(game, piecesMove, forwardOrBack);

        assertThat(game.getPlayersTurn().getPlayer().getCoins()==7).isTrue();


        this.gameService.deleteGame(game);
    }

    @Test
    @Transactional
    @DisplayName("Test if the player of the turn lose one coin and later other two coins when Rapina is in index 5")
    void shouldLose1CoinsThePlayerWhenRapinaIsInIndex5(){
        User player1 = this.userService.findUser("player5").get();
        User player2 = this.userService.findUser("player6").get();

        Lobby lobby = new Lobby(player1);
        lobby.addUserIn(player2);

        LobbySearch lobbysearch = new LobbySearch(lobby);
        Game game = this.gameService.createGame(lobbysearch);

        assertThat(game.getPlayersIn().get(0).getCoins()==8).isTrue();
        assertThat(game.getPlayersIn().get(1).getCoins()==8).isTrue();

        List<Integer> piecesMove= Arrays.asList(6,6);
        Boolean forwardOrBack=false;

        this.gameService.move(game, piecesMove, forwardOrBack);

        assertThat(game.getPlayersTurn().getPlayer().getCoins()==7).isTrue();

        this.gameService.move(game, piecesMove, forwardOrBack);

        assertThat(game.getPlayersTurn().getPlayer().getCoins()==5).isTrue();

        this.gameService.deleteGame(game);
    }

    @Test
    @Transactional
    @DisplayName("Test if every player lose 2 coins and later other 2 coins when Sanitas is in index 5")
    void shouldLose2CoinsEveryPlayerWhenSanitasIsInIndex5(){
        User player1 = this.userService.findUser("player2").get();
        User player2 = this.userService.findUser("player4").get();

        Lobby lobby = new Lobby(player1);
        lobby.addUserIn(player2);

        LobbySearch lobbysearch = new LobbySearch(lobby);
        Game game = this.gameService.createGame(lobbysearch);

        assertThat(game.getPlayersIn().get(0).getCoins()==8).isTrue();
        assertThat(game.getPlayersIn().get(1).getCoins()==8).isTrue();

        List<Integer> piecesMove= Arrays.asList(5,5);
        Boolean forwardOrBack=false;

        this.gameService.move(game, piecesMove, forwardOrBack);

        assertThat(game.getPlayersIn().get(0).getCoins()==6).isTrue();
        assertThat(game.getPlayersIn().get(1).getCoins()==6).isTrue();

        this.gameService.move(game, piecesMove, forwardOrBack);

        assertThat(game.getPlayersIn().get(0).getCoins()==4).isTrue();
        assertThat(game.getPlayersIn().get(1).getCoins()==4).isTrue();

        this.gameService.deleteGame(game);
    }

}