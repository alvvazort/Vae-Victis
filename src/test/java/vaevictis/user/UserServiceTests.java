package vaevictis.user;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Collection;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Service;

@DataJpaTest(includeFilters = @ComponentScan.Filter(Service.class))
class UserServiceTests {                
        
    @Autowired
	protected UserService userService;


    @Test
    void shouldFindUser() {

		User user = new User();
		user.setUsername("Testino");
		user.setPassword("contraseña123");
		this.userService.saveUser(user);

        assertThat(user).isNotNull();
		Optional<User> uOptional = this.userService.findUser("Testino");
        assertThat(uOptional.isPresent()).isTrue();
    }

	
    @Test
    void shouldFindUserLike() {

		Collection<User> iUser =(Collection<User>) this.userService.findUserByUsername("player");
		assertThat(iUser.size()).isEqualTo(6);
    }
	

    @Test
    void shouldCreateFriendPetition() {

		User user = new User();
		user.setUsername("Testino");
		user.setPassword("contraseña123");
		this.userService.saveUser(user);
		String res = "redirect:/users/{username}";		
		String res2 = this.userService.createFriendPetition("admin1", user);

		assertThat(res).isEqualTo(res2);
    }

	
    @Test
    void shouldAcceptFriendPetition() {

		User user = this.userService.findUser("owner1").get();
		String res = "redirect:/users/profile";		
		String res2 = this.userService.createFriendPetition("admin1", user);

		assertThat(res).isEqualTo(res2);
    }




	
}