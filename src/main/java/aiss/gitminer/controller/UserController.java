package aiss.gitminer.controller;

import aiss.gitminer.exception.users.UserNotFoundException;
import aiss.gitminer.model.Comment;
import aiss.gitminer.model.User;
import aiss.gitminer.repository.UserRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@Tag(name="User", description = "API for managing user-related operations")
@RestController
@RequestMapping("gitminer/v1/users")
public class UserController {
    @Autowired
    UserRepository repository;

    @Operation(summary = "Retrieve a List of Users",
            description = "Get a list of users",
            tags = {"users", "get"})
    @ApiResponses({
            @ApiResponse(responseCode = "200",
                    content = {@Content(schema=@Schema(implementation = Comment.class)
                            , mediaType = "application/json" )})
    })
    @GetMapping
    public List<User> findAllUsers(@RequestParam(defaultValue = "0") int page,
                                   @RequestParam(defaultValue = "10") int size,
                                   @RequestParam(required = false) String username,
                                   @RequestParam(required = false) String order){
        Page<User> userPage;
        Pageable paging;

        if(order !=null){
            if(order.startsWith("-")){
                paging = PageRequest.of(page,size, Sort.by(order.substring(1)).descending());
            }else{
                paging = PageRequest.of(page,size,Sort.by(order).ascending());
            }
        }else{
            paging = PageRequest.of(page,size);
        }
        if(username != null){
            userPage = repository.findByUsername(username,paging);
        }else {
            userPage = repository.findAll(paging);
        }
        return userPage.getContent();
    }


    @Operation(
            summary = "Retrieve an User by id",
            description = "Get an User object by specifying its id",
            tags = {"users", "get"}
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200",
                    content = {@Content(schema=@Schema(implementation = Comment.class)
                            , mediaType = "application/json" )}),
            @ApiResponse(responseCode = "404",
                    content = {@Content(schema = @Schema())})
    })
    @GetMapping("/{id}")
    public User findUserById (@Parameter(description = "id of the user to be searched") @PathVariable String id) throws UserNotFoundException {
        Optional<User> project = repository.findById(id);
        if(!project.isPresent()){
            throw new UserNotFoundException();
        }
        return project.get();
    }

    @Operation(
            summary = "Delete an user",
            description = "Delete an user object by specifying its id",
            tags = {"users", "delete"}
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", content = {@Content(schema = @Schema())}),
            @ApiResponse(responseCode = "400", content = {@Content(schema = @Schema())}),
            @ApiResponse(responseCode = "404", content = {@Content(schema = @Schema())})
    })
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    public void deleteUser(@Parameter(description = "id of the User to be deleted")@PathVariable String id) throws UserNotFoundException{
        Optional <User> user = repository.findById(id);
        if (!user.isPresent()) {
            throw new UserNotFoundException();
        }else {
            repository.deleteById(id);
        }
    }
    @Operation(summary = "Insert an User",
    description = "Add a new album whose data is passed in the body of the request in JSON format",
    tags= {"users", "post"})
    @ApiResponses({
            @ApiResponse(responseCode = "201", content = {@Content(schema = @Schema(implementation= User.class), mediaType = "application/json")}),
            @ApiResponse(responseCode = "400", content = {@Content(schema = @Schema())})
    })
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping()
    public User createAnUser (@Valid @RequestBody User newUser){
        return repository.save(newUser);
    }


}
