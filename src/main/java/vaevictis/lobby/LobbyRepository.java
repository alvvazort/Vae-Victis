package vaevictis.lobby;
import java.util.List;

import org.springframework.dao.DataAccessException;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import vaevictis.user.User;

public interface LobbyRepository extends CrudRepository<Lobby, Integer> {
    @Transactional
	Lobby save(Lobby lobby) throws DataAccessException;

	List<Lobby> findByUsersIn_Username(String username);

	List<Lobby> findByUsersInvited_Username(String username);

	Lobby findByLobbyAdmin(User user);

	Lobby findById(int id);

	@Transactional
	@Modifying
	@Query("DELETE FROM Lobby lobby WHERE NOT EXISTS elements(lobby.usersIn)")
	void deleteEmptyLobbies();

	void delete(Lobby lobby);
}
