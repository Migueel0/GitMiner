package aiss.gitminer.controller;

import aiss.gitminer.exception.ProjectNotFoundException;
import aiss.gitminer.exception.UserNotFoundException;
import aiss.gitminer.model.Project;
import aiss.gitminer.model.User;
import aiss.gitminer.repository.UserRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/api/gitminer/users")
public class UserController {

    //TODO: Complete user controller adding operations and exceptions

    UserRepository repository;
    @GetMapping("/{id}")
    public User findUserById (@PathVariable String id) throws UserNotFoundException {
        Optional<User> project = repository.findById(id);
        if(!project.isPresent()){
            throw new UserNotFoundException();
        }
        return project.get();
    }

    //TODO: POST PUT AND DELETE USER


}
