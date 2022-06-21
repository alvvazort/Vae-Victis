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
package vaevictis.user;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.userdetails.UserDetails;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import vaevictis.friends.FriendsService;
import vaevictis.lobby.Lobby;
import vaevictis.lobby.LobbyService;

/**
 * @author Juergen Hoeller
 * @author Ken Krebs
 * @author Arjen Poutsma
 * @author Michael Isvy
 */
@Controller
@RequestMapping("/users")
public class UserController {

	private static final String VIEWS_USER_CREATE_FORM = "users/createUserForm";
	@Autowired
	private final UserService userService;
	@Autowired
	private final FriendsService friendsService;
	@Autowired
	private final LobbyService lobbyService;

	@Autowired
	public UserController(UserService userService, FriendsService friendsService, LobbyService lobbyService) {
		this.userService = userService;
		this.friendsService = friendsService;
		this.lobbyService = lobbyService;
	}

	@Autowired
	@Qualifier("sessionRegistry")
	private SessionRegistry sessionRegistry;

	//@InitBinder
	//public void setAllowedFields(WebDataBinder dataBinder) {
	//}

	
	@GetMapping(value = "/page/{pag}")
	public String devuelvePaginacion(@PathVariable("pag") Integer pag,ModelMap modelmap) {
		modelmap.addAttribute("pags",this.userService.paginationOfUsers());
		modelmap.addAttribute("paginaActual",pag);
		return "users/usersList";
	}

	@GetMapping(value = "/new")
	public String initCreationForm(Map<String, Object> model) {
		User user = new User();
		model.put("user", user);
		return VIEWS_USER_CREATE_FORM;
	}

	@PostMapping(value = "/new")
	public String processCreationForm(@Valid User user, BindingResult result,RedirectAttributes redirectAttrs) {

		List<String> b=new ArrayList<>();

        if(this.userService.findUser(user.getUsername()).isPresent()){
            String a = "Error: nombre de usuario ya en uso";
            b.add(a);
        }
        if (result.hasErrors()) {

            for (FieldError x : result.getFieldErrors()){
                String a= "Error en el campo "+x.getField()+": "+x.getDefaultMessage();
                b.add(a);
            }
        }
        if(!b.isEmpty()){

            redirectAttrs.addFlashAttribute("errorMessage", b);
            return "redirect:/users/new";
        }
        else {
            //creating user, and authority
			user.setWins(0);
            this.userService.saveUser(user);
            return "redirect:/";
        }
	}

	@GetMapping(value = "/delete/{username}")
	public String deleteUser(@PathVariable("username") String username, Map<String, Object> model) {
		this.userService.deleteUser(username);
		return "redirect:/users?username=";
	}
	
	@GetMapping(value = "/find")
	public String initFindForm(Map<String, Object> model, Principal principal) {
		model.put("user", new User());
		if(principal != null){
			List<Lobby> lobbies = lobbyService.getUserInvitedLobbies(principal.getName());
			model.put("showUser", principal.getName());
			model.put("lobbies", lobbies);
			model.put("friends",this.friendsService.getPetitions(principal.getName()));
			model.put("friendsList",this.friendsService.getFriends(principal.getName()));
	
			List<Object> principals = sessionRegistry.getAllPrincipals();
	
			List<String> usersNamesList = new ArrayList<String>();
	
			for (Object p: principals) {
				if (p instanceof org.springframework.security.core.userdetails.User) {
					usersNamesList.add(((org.springframework.security.core.userdetails.User) p).getUsername());
				}
			}
	
			model.put("loggedUsers", usersNamesList);
		}
		return "users/findUsers";
	}

	@GetMapping()
	public String listadoUsuarios(ModelMap modelmap, User user, Principal principal){
		String vista= "users/usersList";
		Collection<User> results = (Collection<User>) this.userService.findUserByUsername(user.getUsername());

		if (user.getUsername()==null) {
			user.setUsername("");
		}
		if (results.isEmpty()) {
			// no owners found
			return "redirect:/users/find";
		}
		else if (results.size() == 1) {
			// 1 owner found
			
			return "redirect:/users/" + results.iterator().next().getUsername();
		}
		
		if(principal != null){
			List<Lobby> lobbies = lobbyService.getUserInvitedLobbies(principal.getName());
			modelmap.put("showUser", principal.getName());
			modelmap.put("lobbies", lobbies);
			modelmap.put("friends",this.friendsService.getPetitions(principal.getName()));
			modelmap.put("friendsList",this.friendsService.getFriends(principal.getName()));
	
			List<Object> principals = sessionRegistry.getAllPrincipals();
	
			List<String> usersNamesList = new ArrayList<String>();
	
			for (Object p: principals) {
				if (p instanceof org.springframework.security.core.userdetails.User) {
					usersNamesList.add(((org.springframework.security.core.userdetails.User) p).getUsername());
				}
			}
	
			modelmap.put("loggedUsers", usersNamesList);
		}

		modelmap.addAttribute("users",results);
		modelmap.addAttribute("pags",this.userService.paginationOfUsers());
		modelmap.addAttribute("paginaActual",1);
		
		return vista;
	}

	@GetMapping(value = "/{username}/edit")
	public String initUpdateOnerForm(@PathVariable("username") String username, Model model) {
		User user = this.userService.findUser(username).get();
		model.addAttribute(user);
		return VIEWS_USER_CREATE_FORM;
	}

	@GetMapping(value = "/profile/changePassword")
	public String initUpdateOnerForm(Authentication authentication, Model model) {
		User user = this.userService.findUser(authentication.getName()).get();
		model.addAttribute(user);
		return "users/changePasswordForm";
	}

	@PostMapping(value = "/{username}/edit")
	public String processUpdateUserForm(@Valid User user, RedirectAttributes redirectAttrs, BindingResult result, @PathVariable("username") String username) {

		if (result.hasErrors()) {
			return VIEWS_USER_CREATE_FORM;
		}
		List<String> b=new ArrayList<>();
        if (result.hasErrors()) {

            for (FieldError x : result.getFieldErrors()){
                String a= "Error en el campo "+x.getField()+": "+x.getDefaultMessage();
                b.add(a);
            }
        }
        if(!b.isEmpty()){

            redirectAttrs.addFlashAttribute("errorMessage", b);
            return "redirect:/{username}/edit";
        }
		else {
			this.userService.saveUser(user);
			return "redirect:/users/";
		}
	}

	@PostMapping(value = "/profile/changePassword")
	public String processUpdateUserForm(@Valid User user, BindingResult result) {
		if (result.hasErrors()) {
			return "users/changePasswordForm";
		}
		else {
			this.userService.saveUser(user);
			return "redirect:/users/profile";
		}
	}


	@GetMapping("/{username}")
	public ModelAndView showUser(ModelMap modelmap, Principal principal, @PathVariable("username") String username) {
		String a;
		List<Lobby> lobbies = lobbyService.getUserInvitedLobbies(principal.getName());
		modelmap.put("lobbies", lobbies);
		modelmap.addAttribute("friends",this.friendsService.getPetitions(principal.getName()));
		modelmap.addAttribute("friendsList",this.friendsService.getFriends(principal.getName()));
		if (username.equals(principal.getName())){
			modelmap.addAttribute("showUser", username);
			 a = "users/profileDetails";
		}
		else{
			modelmap.addAttribute("showUser", principal.getName());
			modelmap.addAttribute("ocultarboton",this.userService.isFriend(username));
			modelmap.addAttribute("soyDestino",this.userService.imDestiny(principal,this.userService.findUser(username).get()));
			 a = "users/userDetails";
			}
		ModelAndView mav = new ModelAndView(a);
		mav.addObject(this.userService.findUser(username).get());
		
		List<Object> principals = sessionRegistry.getAllPrincipals();

		List<String> usersNamesList = new ArrayList<String>();

		for (Object p: principals) {
			if (p instanceof org.springframework.security.core.userdetails.User) {
				usersNamesList.add(((org.springframework.security.core.userdetails.User) p).getUsername());
			}
		}

		modelmap.put("loggedUsers", usersNamesList);
		
		modelmap.addAttribute("revisions", this.userService.getUserAudit(username));
		return mav;
	}

	@GetMapping("/profile")
	public ModelAndView profile(Principal principal, ModelMap model, Authentication authentication) {
		ModelAndView mav = new ModelAndView("users/profileDetails");
		
		if(principal != null){
			List<Lobby> lobbies = lobbyService.getUserInvitedLobbies(principal.getName());
			model.put("showUser", principal.getName());
			model.put("lobbies", lobbies);
			model.put("friends",this.friendsService.getPetitions(principal.getName()));
			model.put("friendsList",this.friendsService.getFriends(principal.getName()));
	
			List<Object> principals = sessionRegistry.getAllPrincipals();
	
			List<String> usersNamesList = new ArrayList<String>();
	
			for (Object p: principals) {
				if (p instanceof org.springframework.security.core.userdetails.User) {
					usersNamesList.add(((org.springframework.security.core.userdetails.User) p).getUsername());
				}
			}
	
			model.put("loggedUsers", usersNamesList);
		}
		model.put("revisions", this.userService.getUserAudit(principal.getName()));
		mav.addObject(this.userService.findUser(authentication.getName()).get());
		return mav;
	}
	
	@GetMapping("/{username}/addFriend")
	public String addFriend(@PathVariable("username") String username, RedirectAttributes redirectAttrs, HttpServletRequest request) {
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		String userUsername = ((UserDetails) principal).getUsername();
		
		Optional<User> optional =  this.userService.findUser(username);
		if ( !optional.isPresent()) {
			final String errorMessage  ="Usuario no existente";
			redirectAttrs.addFlashAttribute("errorMessage", errorMessage);
			
			return "redirect:/error/";
		}
		else{
		this.userService.createFriendPetition(userUsername,optional.get());
		String referer = request.getHeader("Referer");
		return  "redirect:"+referer;}
	}

	@GetMapping("/{username}/deleteFriend")
	public String deleteFriendRequest(@PathVariable("username") String username, HttpServletRequest request) {
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		String userUsername = ((UserDetails) principal).getUsername();
		this.userService.removeFriendPetition(userUsername,username);

		String referer = request.getHeader("Referer");
		System.out.println(referer);


		return  "redirect:"+referer;
	}


}
