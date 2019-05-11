package Controllers;
import Models.Project;
import Models.User;
import Repositories.UserRepository;
import Services.ProjectService;
import Services.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

@CrossOrigin (origins = "*", allowedHeaders = "*")
@RestController

public class Search {

	@RequestMapping (value = "/userSearch", method = RequestMethod.GET)
	public ResponseEntity userSearch(HttpServletRequest req) {
		String name = req.getParameter("search-field");
		List<User> users = UserService.findUserWithName(name);
		if(users == null)
			return new ResponseEntity<>("No user found with this name!", HttpStatus.NOT_FOUND);
		else
			return ResponseEntity.ok(users);

	}

	@RequestMapping (value = "/projectSearch", method = RequestMethod.GET)
	public ResponseEntity projectSearch(HttpServletRequest req) {
		String searchField = req.getParameter("search-field");

		List<Project> projects = ProjectService.findProjectsWithTitle(searchField, req.getHeader("user-token"));
		projects.addAll(ProjectService.findProjectsWithDesc(searchField, req.getHeader("user-token")));

		Set<Project> temp = new LinkedHashSet<>(projects);
		projects.clear();
		projects.addAll(temp);
		if(projects == null)
			return new ResponseEntity<>("Couldn't fetch projects list from database!", HttpStatus.INTERNAL_SERVER_ERROR);
		else
			return ResponseEntity.ok(projects);
	}
}
