package vaevictis.statistics;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import vaevictis.game.Game;
import vaevictis.game.GameRepository;
import vaevictis.game.components.card.Card;
import vaevictis.player.Player;
import vaevictis.user.User;
import vaevictis.user.UserRepository;

@Service
public class StatisticsService {

    @Autowired 
    GameRepository gameRepository;

    @Autowired 
    UserRepository userRepository;
    
	public Iterable<User> findUserOrderedByWins() throws DataAccessException{
		return userRepository.findUserOrderedByWins();
	}

    public List<Integer> getGamesDuration(Iterable<Game> partidas){
        List<Integer> res = new ArrayList<Integer>();
        if(!partidas.iterator().hasNext()){
            return List.of(0,0,0);
        }
        Integer min = 0;
        Integer promedio = 0;
        Integer max = 0;
        Integer numPartidas = 1;            //Este metodo funciona pero como no hemos podido implementar el final de una partida no se pueden llamar 
        /*Integer numPartidas = 0;
        for (Game partida : partidas) {
            Date dateRemoved = partida.getRemovedAt();                
            Date dateCreated = partida.getCreatedAt();
            if(!dateRemoved.equals(null)){       
                Long diff = dateRemoved.getTime() - dateCreated.getTime();
                diff = diff/60000;
                if(diff < min){
                    min = Math.toIntExact(diff);
                }
                if(diff > max){
                    max = Math.toIntExact(diff);
                }
                promedio += Math.toIntExact(diff);
                }
            numPartidas ++;
        }*/

        res.add(min);
        res.add(promedio/numPartidas);
        res.add(max);

        return res;
    }

    public List<Game> findGamesOfUser(User user){
		List<Game> res = new ArrayList<Game>();
		Iterator<Game> iteratorPartidas = gameRepository.findAll().iterator();
	
		while(iteratorPartidas.hasNext()){
			Game partida = iteratorPartidas.next();
			List<Player> jugadores = partida.getPlayersIn(); 
			for (int j = 0; j < jugadores.size(); j++){
                
				if(user.getUsername().equals(jugadores.iterator().next().getUser().getUsername())){
					res.add(partida);
				}
			}
		}
		
		return res;
	}

    public List<Game> findGamesOrdered(User user){

        List<Game> partidas = this.gameRepository.findGamesOrdered();
        List<Game> res = new ArrayList<Game>();

        if(user.getAuthorities().iterator().next().getAuthority().equals("admin")){
            return partidas;
        }
        else{
            for(Game partida:partidas){
                List<Player> jugadores = partida.getPlayersIn();
                for(Player player:jugadores){
                    if(user.getUsername().equals(player.getUser().getUsername())){
                        res.add(partida);
                    }
                }
            }
        }
        return res;
    }
    
    public Integer numberOfWinsOfUser(User user){
		Integer wins = 0;
		List<Game> gamesPlayerByUser = this.findGamesOfUser(user);
        if(!gamesPlayerByUser.iterator().hasNext()){
            return 0;
        }
		for (Game juego : gamesPlayerByUser){
			
            // //Este metodo funciona pero como no hemos podido implementar el final de una partida no se pueden llamar
            /*if(juego.getWinner().getUsername().equals(user.getUsername())){
				wins++;
		    }*/
        }
		return wins;
	}

    public Integer numberOfPlayer(Iterable<Game> partidas){
        
        if(!partidas.iterator().hasNext()){
            return 0;
        }
        Integer numPlayer = 0;
        Integer numGames = 0;
        for(Game partida : partidas){
            numPlayer += partida.getPlayersIn().size();
            numGames ++;
        }
        Integer promedio = Math.round(numPlayer/numGames);
        return promedio;
    }

    public Card mostPlayedCardUser(List<Game> games, User user){
        
        if(!games.iterator().hasNext()){
            return null;
        }
        Map<Card,Integer> res = new HashMap<Card,Integer>();
        for(Game game : games){
            List<Player> players = game.getPlayersIn();
            for (Player player: players){
                if(user.getUsername().equals(player.getUser().getUsername())){
                    List<Card> cards = player.getAllCards();
                    for(Card card : cards){
                        if(res.containsKey(card)){
                            Integer i = res.get(card)+1;
                            res.put(card,i);
                        }
                        else{
                            res.put(card,1);
                        }
                    }
                }
            }     
        }
        Integer max = 0;
        Card carta = new Card();
        for (Card card : res.keySet()){
            Integer numCartas = res.get(card);
            if(numCartas > max){
                max = numCartas;
                carta = card;
            }
        }
        return carta;
    }
    
    public Card mostPlayedCard(Iterable<Game> games){
        
        if(!games.iterator().hasNext()){
            return new Card(1);
        }
        Map<Card,Integer> res = new HashMap<Card,Integer>();
        for(Game game : games){
            List<Player> players = game.getPlayersIn();
            for (Player player: players){
                    List<Card> cards = player.getAllCards();
                    for(Card card : cards){
                        if(res.containsKey(card)){
                            Integer i = res.get(card)+1;
                            res.put(card,i);
                        }
                        else{
                            res.put(card,1);
                        }
                    }
                }
            }     
        
        Integer max = 0;
        Card carta = new Card();
        for (Card card : res.keySet()){
            Integer numCartas = res.get(card);
            if(numCartas > max){
                max = numCartas;
                carta = card;
            }
        }
        return carta;
    }
    
}
