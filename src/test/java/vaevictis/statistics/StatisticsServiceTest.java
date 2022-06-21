package vaevictis.statistics;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Date;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Service;

import vaevictis.game.Game;
import vaevictis.game.GameService;
import vaevictis.game.components.card.Card;
import vaevictis.game.turn.Turn;
import vaevictis.player.Player;
import vaevictis.user.User;
import vaevictis.user.UserService;

@DataJpaTest(includeFilters = @ComponentScan.Filter(Service.class))
class StatisticsServiceTest {                
        
    @Autowired
	protected StatisticsService statisticsService;

    @Autowired
    protected UserService userService;

    @Autowired
    protected GameService gameService;

    @Test
    void shouldCreateIterableUserOrdered() {
        assertThat(this.statisticsService.findUserOrderedByWins()).isNotEmpty();
    }
    @Test
    void shouldGetGamesDuration() {
        User u1 = new User("test1", "gonzalo123");
        Player p1 = new Player(u1);
        User u2 = new User("test2", "gonzalo123");
        Player p2 = new Player(u2);
        User u3 = new User("test3", "gonzalo123");
        Player p3 = new Player(u3);
        List<Player> lp = List.of(p1,p2,p3);
        Turn t = new Turn(p1);
        Game g = new Game(lp,t);
        g.setCreatedAt(new Date());
        g.setRemovedAt(new Date());
        //assertThat(this.statisticsService.getGamesDuration().get(0)).isNotEmpty(); 
        //assertThat(this.statisticsService.getGamesDuration().get(1)).isNotEmpty(); 
        //assertThat(this.statisticsService.getGamesDuration().get(2)).isNotEmpty(); 
    }

    @Test
    void shouldntCreateGamesOfUser() {
        User u1 = new User("test","gonzalo123");
        assertThat(this.statisticsService.findGamesOfUser(u1)).isEmpty();
    }

    @Test
    void shouldGetWinnerAndLoser() {
        User u1 = new User("winner", "gonzalo123");
        Player p1 = new Player(u1);
        User u2 = new User("loser", "gonzalo123");
        Player p2 = new Player(u2);
        User u3 = new User("test3", "gonzalo123");
        Player p3 = new Player(u3);
        List<Player> lp = List.of(p1,p2,p3);
        Turn t = new Turn(p1);
        Game g = new Game(lp,t);
        g.setWinner(u1);
        //assertThat(this.statisticsService.numberOfWinsOfUser(u1).equals(1));
        //assertThat(this.statisticsService.numberOfWinsOfUser(u2).equals(0));
    }

    @Test
    void shouldGetNumberOfPlayer() {
        User u1 = new User("test1", "gonzalo123");
        Player p1 = new Player(u1);
        User u2 = new User("test2", "gonzalo123");
        Player p2 = new Player(u2);
        User u3 = new User("test3", "gonzalo123");
        Player p3 = new Player(u3);
        User u4 = new User("test4", "gonzalo123");
        Player p4 = new Player(u4);

        List<Player> lp = List.of(p1,p2,p3,p4);
        Turn t = new Turn(p1);
        Game g = new Game(lp,t);
        List<Game> lg = List.of(g);

        assertThat(this.statisticsService.numberOfPlayer(lg).equals(4));
    }

    @Test
    void shouldGetMostPlayedCardUser() {
        User u1 = new User("test1", "gonzalo123");
        Player p1 = new Player(u1);
        User u2 = new User("test2", "gonzalo123");
        Player p2 = new Player(u2);
        User u3 = new User("test3", "gonzalo123");
        Player p3 = new Player(u3);
        User u4 = new User("test4", "gonzalo123");
        Player p4 = new Player(u4);
        
        Card c1 = new Card(1);
        Card c3 = new Card(2);
        Card c4 = new Card(7);
        List<Card> lc = List.of(c1,c1,c3,c4);
        p3.setAllCards(lc);

        List<Player> lp = List.of(p1,p2,p3,p4);
        Turn t = new Turn(p1);
        Game g = new Game(lp,t);
        List<Game> lg = List.of(g);

        assertThat(this.statisticsService.mostPlayedCardUser(lg,u3).equals(c1));
    }

    @Test
    void shouldGetMostPlayedCard() {
        User u1 = new User("test1", "gonzalo123");
        Player p1 = new Player(u1);
        User u2 = new User("test2", "gonzalo123");
        Player p2 = new Player(u2);
        User u3 = new User("test3", "gonzalo123");
        Player p3 = new Player(u3);
        User u4 = new User("test4", "gonzalo123");
        Player p4 = new Player(u4);
        
        Card c1 = new Card(1);
        Card c3 = new Card(2);
        Card c4 = new Card(7);
        List<Card> lc = List.of(c1,c1,c3,c4);
        List<Card> lc2 = List.of(c4,c4,c4,c4);
        p3.setAllCards(lc);
        p4.setAllCards(lc2);

        List<Player> lp = List.of(p1,p2,p3,p4);
        Turn t = new Turn(p1);
        Game g = new Game(lp,t);
        List<Game> lg = List.of(g);

        assertThat(this.statisticsService.mostPlayedCard(lg).equals(c4));
    }

    
    


    

    
    

}