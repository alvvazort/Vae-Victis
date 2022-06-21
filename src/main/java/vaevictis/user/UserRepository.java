package vaevictis.user;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, String>{
    @Query("SELECT u FROM User u WHERE u.username LIKE ?1%")
	public Iterable<User> findLikeUsername(String username);

    @Transactional
    @Modifying
    @Query("delete from User u where u.username = ?1")
    public void deleteByUsername(String username);

    @Query("SELECT u FROM User u ORDER BY wins DESC")
	public Iterable<User> findUserOrderedByWins();

}
