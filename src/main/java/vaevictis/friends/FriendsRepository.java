package vaevictis.friends;

import java.util.List;
import java.util.Optional;

import org.springframework.dao.DataAccessException;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;

import vaevictis.user.User;


public interface FriendsRepository extends Repository<Friends, Integer> {
    @Query("SELECT f FROM Friends f WHERE (f.origin.username=:loggedUser AND f.destiny.username=:searchUser) OR (f.destiny.username=:loggedUser AND f.origin.username=:searchUser)   ")
      public Optional<Friends> findByUsername(@Param("loggedUser") String loggedUser,@Param("searchUser") String searchUser);
   
    
    void save(Friends friends) throws DataAccessException;
    
    @Query("SELECT COUNT(1) from Friends where origin = ?1")
    public int haveNotification1(String name);
    @Query("SELECT COUNT(1) from Friends where destiny = ?1")
    public int haveNotification(String name);

    @Query("SELECT u.destiny from Friends u where u.origin.username = ?1")
    public List<User> getSentPetitions(String name);
    @Query("SELECT u.origin from Friends u where u.destiny.username = ?1 AND u.request=true")
    public List<User> getPetitions(String name);

    @Query("SELECT f FROM Friends f WHERE (f.origin=:loggedUser AND f.destiny=:searchUser) ")
    public Optional<Friends> findByUsername2(@Param("loggedUser") User loggedUser,@Param("searchUser") User searchUser);
   
    public void delete(Friends entity); 


    @Query("SELECT f.destiny FROM Friends f WHERE f.origin.username=:loggedUser AND f.request=false ")
    public List<User> getFriends(@Param("loggedUser") String loggedUser);
    
    @Query("SELECT f.origin FROM Friends f WHERE f.destiny.username=:loggedUser AND f.request=false ")
    public List<User> getFriends2(@Param("loggedUser") String loggedUser);
}
