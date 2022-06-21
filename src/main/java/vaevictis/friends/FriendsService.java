/*
 * Copyright 2002-2013 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package vaevictis.friends;




import java.security.Principal;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import vaevictis.statistics.Achievement;
import vaevictis.statistics.AchievementService;
import vaevictis.user.User;
import vaevictis.user.UserRepository;
/**
 * Mostly used as a facade for all Petclinic controllers Also a placeholder
 * for @Transactional and @Cacheable annotations
 *
 * @author Michael Isvy
 */
@Service
public class FriendsService {

	private FriendsRepository friendsRepository;

	@Autowired
	private AchievementService achievementService;

	private UserRepository usersRepository;

	

	@Autowired
	public FriendsService(FriendsRepository friendsRepository,AchievementService achievementService, UserRepository usersRepository) {
		this.friendsRepository = friendsRepository;
		this.achievementService = achievementService;
		this.usersRepository = usersRepository;
	}
	
	@Transactional
    public String createFriendPetition(User origin, User destiny) throws DataAccessException {
		//Añadir excepciones
		String res = "redirect:/users/{username}";	
		
		Optional<Friends> friend = friendsRepository.findByUsername2(destiny, origin);

		if(!friend.isEmpty()){
			
			friend.get().setRequest(false);
			if(this.getFriends(origin.getUsername()).size() ==
			 this.achievementService.findById(3).getAchievementCondition()){
				Set<Achievement> achievements = origin.getAchievements();
				achievements.add(this.achievementService.findById(3));
			}
			
			if(this.getFriends(destiny.getUsername()).size() ==
			this.achievementService.findById(3).getAchievementCondition()){
			Set<Achievement> achievementsDestiny = destiny.getAchievements();
			achievementsDestiny.add(achievementService.findById(3));
			usersRepository.save(destiny);
			}
			
			res = "redirect:/users/profile";
			friend.get().setRequest(false);	

		}
		else{
		Friends friends = new Friends();
		friends.setDestiny(destiny);
		friends.setOrigin(origin);
		friends.setRequest(true);
		friendsRepository.save(friends);
		}
		return res;
    }

	public boolean haveNotification(Principal principal) {
		int a = this.friendsRepository.haveNotification(principal.getName()) + this.friendsRepository.haveNotification1(principal.getName());
		return (a!=0);
    }

	public List<User> getPetitions(String principal){
		return this.friendsRepository.getPetitions(principal);
		}
	public List<User> getSentPetitions(String principal){
		return this.friendsRepository.getSentPetitions(principal);
		}	

	public Optional<Friends> findByUsername(String user, String destiny){
		return this.friendsRepository.findByUsername(user,destiny);
		}

    public void removeFriendPetition(String originUser, String destinyUser) {

		Optional<Friends> entity = this.friendsRepository.findByUsername(originUser, destinyUser);
		if(entity.isPresent()){
				
			this.friendsRepository.delete(entity.get());
				
		}
    }	

	public List<User> getFriends(String user){

		List<User> res = this.friendsRepository.getFriends(user);
		res.addAll(this.friendsRepository.getFriends2(user));
		return res;
	}
}
