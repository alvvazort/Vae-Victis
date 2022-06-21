package vaevictis.friends;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Service;

import vaevictis.user.User;
import vaevictis.user.UserService;


@DataJpaTest(includeFilters = @ComponentScan.Filter(Service.class))
class FriendsServiceTests {                
        
    @Autowired
	protected FriendsService friendsService;
	@Autowired
	protected UserService userService;		


    @Test
    void shouldCreateFriendPetition() {

		
		Optional<Friends> friend = this.friendsService.findByUsername("admin1", "Testino");
		assertThat(friend.isPresent()).isFalse();

		User user = new User();
		user.setUsername("Testino");
		user.setPassword("contraseña123");
		this.userService.saveUser(user);
		User admin1 = this.userService.findUser("admin1").get();
		this.friendsService.createFriendPetition(admin1, user);

		friend = this.friendsService.findByUsername("admin1", "Testino");
		
		assertThat(friend.isPresent()).isTrue();
		
		assertThat(friend.get().isRequest()).isTrue();
    }

    @Test
    void shouldAcceptFriendPetition() {

		
		Optional<Friends> friend = this.friendsService.findByUsername("admin1", "owner1");
		assertThat(friend.get().isRequest()).isTrue();

		User owner1 = this.userService.findUser("owner1").get();
		User admin1 = this.userService.findUser("admin1").get();
		this.friendsService.createFriendPetition(admin1, owner1);
		friend = this.friendsService.findByUsername("admin1", "owner1");
		
		assertThat(friend.get().isRequest()).isFalse();
    }

    @Test
    void shouldHavePetitions() {

		List<User> lista = this.friendsService.getPetitions("player1");
		
		assertThat(lista.size()).isEqualTo(5);
    }

	
    @Test
    void shouldNotHavePetitions() {

		List<User> lista = this.friendsService.getPetitions("player2");
		
		assertThat(lista.size()).isEqualTo(0);
    }


    @Test
    void shouldHaveSentPetitions() {

		List<User> lista = this.friendsService.getSentPetitions("luirodvid");
		
		assertThat(lista.size()).isEqualTo(2);
    }
	

	@Test
	void shouldFindBothWays(){
		//Probamos que el método findByUsername devuelve el mismo resultado independientemente del orden de los parámetros
		Optional<Friends> friend = this.friendsService.findByUsername("admin1", "player1");
		Optional<Friends> friend2 = this.friendsService.findByUsername("player1", "admin1");

		assertThat(friend.isPresent()).isFalse();
		assertThat(friend).isEqualTo(friend2);
		//Enviamos una petición de amistad
		User player1 = this.userService.findUser("player1").get();
		User admin1 = this.userService.findUser("admin1").get();
		this.friendsService.createFriendPetition(admin1, player1);

		//Probamos que la petición ahora si existe y sigue devolviendo el mismo resultado
		friend = this.friendsService.findByUsername("admin1", "player1");
		friend2 = this.friendsService.findByUsername("player1", "admin1");

		assertThat(friend.isPresent()).isTrue();
		assertThat(friend).isEqualTo(friend2);

	}

	@Test
	void shouldDeleteFriends(){
		
		Optional<Friends> friend = this.friendsService.findByUsername("admin1", "owner1");
		assertThat(friend.isPresent()).isTrue();

		this.friendsService.removeFriendPetition("owner1", "admin1");		
		friend = this.friendsService.findByUsername("admin1", "owner1");
		assertThat(friend.isPresent()).isFalse();

	}



	@Test
	void shouldGetFriends(){
		List<User> list = this.friendsService.getFriends("admin1");
		
		assertThat(list.size()).isEqualTo(0);
		
		User owner1 = this.userService.findUser("owner1").get();
		User admin1 = this.userService.findUser("admin1").get();
		this.friendsService.createFriendPetition(admin1, owner1);

		list = this.friendsService.getFriends("admin1");
		
		assertThat(list.size()).isEqualTo(1);

	}
}