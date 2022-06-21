package vaevictis.lobby;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Service;

import vaevictis.lobby.ws.lobby.LobbySearch;
import vaevictis.user.User;
import vaevictis.user.UserService;

@DataJpaTest(includeFilters = @ComponentScan.Filter(Service.class))
class LobbyServiceTests {                
        
    @Autowired
	protected LobbyService lobbyService;

    @Autowired
	protected UserService userService;

    @Test
    void shouldCreateLobbyAndDeleteUsers() {

		Lobby lobby = this.lobbyService.getLobby(1);
        assertThat(lobby).isNull();

        Optional<User> userFound = this.userService.findUser("admin1");
		User user = null;

        if(userFound.isPresent()) {
            user = userFound.get();
            lobby = new Lobby(user);

            this.lobbyService.saveLobby(lobby);
        }

        lobby = this.lobbyService.getLobby(1);

		assertThat(user).isNotNull();
        assertThat(lobby).isNotNull();
        assertThat(lobby.getUsersIn().size()).isEqualTo(1);
        assertThat(lobby.isUserIn("admin1")).isTrue();

		lobby = this.lobbyService.getUserLobbyAdmin(user);
		assertThat(lobby.getId()).isEqualTo(1);

		//borro al usuario de los lobbies
		this.lobbyService.cleanUserLobbies(user.getUsername());
		assertThat(lobby.getUsersIn().size()).isEqualTo(0);
    }

	@Test
    void shouldInviteFriends() {
        Optional<User> userFound = this.userService.findUser("admin1");
        Lobby lobby = null;
		User user = null;

        if(userFound.isPresent()) {
            user = userFound.get();
            lobby = new Lobby(user);
        }

		Optional<User> userFound2 = this.userService.findUser("dandianog");
		User user2 = null;
		if(userFound.isPresent()) {
            user2 = userFound2.get();
        }

		lobby.addUserInvited(user2);
		this.lobbyService.saveLobby(lobby);

		List<Lobby> listaLobbies = this.lobbyService.getUserInvitedLobbies(user2.getUsername());

		assertThat(listaLobbies.size()).isEqualTo(1);
    }

    @Test
    void shouldCreateLobbySearch() {
        // Guarda en memoria todas las búsquedas de partida
        List<LobbySearch> lobbySearchs = new ArrayList<LobbySearch>();

        // ALmacena una lista de usuarios
        List<User> lobbyUsers = new ArrayList<User>();
        for(int i=0; i<6; i++) {
            Optional<User> u = this.userService.findUser("player"+(i+1));
            if(u.isPresent()) {
                lobbyUsers.add(u.get());
            }
        }

        // Se crea el primer lobby con los 3 primeros usuarios para una partida de nivel medio de 6 jugadores
        Lobby lobby1 = new Lobby(lobbyUsers.get(0));
        lobby1.setGameLevel(1);
        lobby1.setPlayerNumber(3);
        
        lobby1 = this.lobbyService.saveLobby(lobby1);
        Lobby l = this.lobbyService.getLobby(lobby1.getId());
        assertNotNull(l, "El lobby no se guarda correctamente");

        for(int i=1; i<3; i++) {
            lobby1.addUserIn(lobbyUsers.get(i));
        }
        // Se ejecuta la lógica que implementa el websocket con un LobbySearch añadiendo la búsqueda y combinándola según los filtros de tipo de partida
        LobbySearch lobbySearch1 = this.lobbyService.addLobbySearch(lobby1, lobbySearchs);
        assertTrue(lobbySearch1.isSatified(), "La búsqueda de partida debería estar satisfecha");
        assertEquals(lobbySearch1.getPlayerNumber(), 3, "La partida debería ser de 3 jugadores");
        assertEquals(lobbySearch1.getGameLevel(), 1, "La búsqueda debería ser de una partida de nivel 1");
    }

    @Test
    void shouldSatisfyLobbySearch() {

        // Guarda en memoria todas las búsquedas de partida
        List<LobbySearch> lobbySearchs = new ArrayList<LobbySearch>();

        // ALmacena una lista de usuarios
        List<User> lobbyUsers = new ArrayList<User>();
        for(int i=0; i<6; i++) {
            Optional<User> u = this.userService.findUser("player"+(i+1));
            if(u.isPresent()) {
                lobbyUsers.add(u.get());
            }
        }

        // Se crea el primer lobby con los 3 primeros usuarios para una partida de nivel medio de 6 jugadores
        Lobby lobby1 = new Lobby(lobbyUsers.get(0));
        lobby1.setGameLevel(1);
        lobby1.setPlayerNumber(6);
        for(int i=1; i<3; i++) {
            lobby1.addUserIn(lobbyUsers.get(i));
        }
        // Se ejecuta la lógica que implementa el websocket con un LobbySearch añadiendo la búsqueda y combinándola según los filtros de tipo de partida
        LobbySearch lobbySearch1 = this.lobbyService.addLobbySearch(lobby1, lobbySearchs);
        assertFalse(lobbySearch1.isSatified(), "La búsqueda de partida no debería estar satisfecha");
        assertEquals(lobbySearch1.getPlayerNumber(), 6, "La partida debería ser de 3 jugadores");
        assertEquals(lobbySearch1.getGameLevel(), 1, "La búsqueda debería ser de una partida de nivel 1");
        assertFalse(lobbySearch1.isSatified(), "La búsqueda de partida no debería estar satisfecha");

        // Se crea un segundo lobby con las mismas configuraciones
        Lobby lobby2 = new Lobby(lobbyUsers.get(3));
        lobby2.setGameLevel(1);
        lobby2.setPlayerNumber(6);
        for(int i=4; i<6; i++) {
            lobby2.addUserIn(lobbyUsers.get(i));
        }
        // Se deberían combinar los lobbies porque tienen misma configuración y les faltan los usuarios
        LobbySearch lobbySearch2 = this.lobbyService.addLobbySearch(lobby2, lobbySearchs);
        assertTrue(lobbySearch2.isSatified(), "La búsqueda de partida debería estar satisfecha");
    }

    @Test
    void shouldNotSatisfyLobbySearch() {
        // Guarda en memoria todas las búsquedas de partida
        List<LobbySearch> lobbySearchs = new ArrayList<LobbySearch>();

        // ALmacena una lista de usuarios
        List<User> lobbyUsers = new ArrayList<User>();
        for(int i=0; i<6; i++) {
            Optional<User> u = this.userService.findUser("player"+(i+1));
            if(u.isPresent()) {
                lobbyUsers.add(u.get());
            }
        }

        // Se crea el primer lobby con los 3 primeros usuarios para una partida de nivel medio de 6 jugadores
        Lobby lobby1 = new Lobby(lobbyUsers.get(0));
        lobby1.setGameLevel(1);
        lobby1.setPlayerNumber(6);
        for(int i=1; i<3; i++) {
            lobby1.addUserIn(lobbyUsers.get(i));
        }
        // Se ejecuta la lógica que implementa el websocket con un LobbySearch añadiendo la búsqueda y combinándola según los filtros de tipo de partida
        LobbySearch lobbySearch1 = this.lobbyService.addLobbySearch(lobby1, lobbySearchs);
        assertEquals(lobbySearch1.getPlayerNumber(), 6, "La partida debería ser de 3 jugadores");
        assertEquals(lobbySearch1.getGameLevel(), 1, "La búsqueda debería ser de una partida de nivel 1");
        assertFalse(lobbySearch1.isSatified(), "La búsqueda de partida no debería estar satisfecha");
        
        Lobby lobby2 = new Lobby(lobbyUsers.get(3));
        lobby2.setGameLevel(2);
        lobby2.setPlayerNumber(4);
        for(int i=4; i<6; i++) {
            lobby2.addUserIn(lobbyUsers.get(i));
        }

        LobbySearch lobbySearch2 = this.lobbyService.addLobbySearch(lobby2, lobbySearchs);
        assertFalse(lobbySearch2.isSatified(), "No se deberían combinar las búsquedas");
    }


}