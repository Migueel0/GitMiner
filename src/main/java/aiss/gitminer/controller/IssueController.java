package aiss.gitminer.controller;

import aiss.gitminer.exception.issues.IssueNotFoundException;
import aiss.gitminer.model.Comment;
import aiss.gitminer.model.Issue;
import aiss.gitminer.model.User;
import aiss.gitminer.repository.IssueRepository;
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
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Tag(name="Issue", description="API for issue management")
@RestController
@RequestMapping("gitminer/v1/issues")
public class IssueController {
    @Autowired
    IssueRepository repository;
    @Operation(
            summary = "Retrieve an Issue by id",
            description = "Get an Issue object by specifying its id",
            tags = {"issue", "get"}
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200",
                    content = {@Content(schema=@Schema(implementation = Issue.class)
                            , mediaType = "application/json" )}),
            @ApiResponse(responseCode = "404",
                    content = {@Content(schema = @Schema())})
    })
    @GetMapping("/{id}")
    public Issue findIssueById(@Parameter(description = "id of de issue to be searched")@PathVariable String id) throws IssueNotFoundException {
        Optional<Issue> issue = repository.findById(id);
        if(!issue.isPresent()){
            throw new IssueNotFoundException();
        }
        return issue.get();
    }

    @Operation(
            summary = "Retrieve a List of Issues by state",
            description = "Get a List of issues by specifying its state, default value will retrieve all issues",
            tags = {"issues", "get"}
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200",
                    content = {@Content(schema=@Schema(implementation = Issue.class)
                            , mediaType = "application/json" )}),
            @ApiResponse(responseCode = "404",
                    content = {@Content(schema = @Schema())})
    })
    @GetMapping
    public List<Issue> findIssuesByState(@Parameter(description = "State of the issue to be searched") @RequestParam(required = false) String state,
                                         @RequestParam(defaultValue = "0") int page,
                                         @RequestParam(defaultValue = "10") int size,
                                         @RequestParam(required = false)String  order) {
        Pageable paging;
        Page<Issue> issuePage;

        if(order !=null){
            if(order.startsWith("-")){
                paging = PageRequest.of(page,size, Sort.by(order.substring(1)).descending());
            }else{
                paging = PageRequest.of(page,size,Sort.by(order).ascending());
            }
        }else{
            paging = PageRequest.of(page,size);
        }

        if(state != null){
            issuePage = repository.findByState(state,paging);
        }else {
            issuePage = repository.findAll(paging);
        }


        return issuePage.getContent();
    }

    @Operation(
            summary = "Find the author of an Issue",
            description = "Retrieve the author of an Issue by specifying its ID",
            tags = {"issues", "get"}
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = User.class))),
            @ApiResponse(responseCode = "404",
                    content = @Content(schema = @Schema()))
    })
    @GetMapping("/{id}/author")
    public User findIssueAuthor(@Parameter(description = "Id of the issue whose author is going to be searched")@PathVariable String id) throws IssueNotFoundException {
        Optional<Issue> issue = repository.findById(id);
        if(!issue.isPresent()){
            throw new IssueNotFoundException();
        }
        User author = issue.get().getAuthor();
        return author;
    }
    @Operation(
            summary = "Find the assignee of an Issue",
            description = "Retrieve the assignee of an Issue by specifying its ID",
            tags = {"issues", "get"}
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = User.class))),
            @ApiResponse(responseCode = "404",
                    content = @Content(schema = @Schema()))
    })
    @GetMapping("/{id}/assignee")
    public User findIssueAssignee(@Parameter(description = "Id of the issue whose author is going to be assigned")@PathVariable String id) throws IssueNotFoundException {
        Optional<Issue> issue = repository.findById(id);
        if(!issue.isPresent()){
            throw new IssueNotFoundException();
        }
        User assignee = issue.get().getAssignee();
        return assignee;
    }


    @GetMapping("/{id}/comments")
    public List<Comment> findIssueComments(@PathVariable String id) throws IssueNotFoundException{
        Optional<Issue> issue = repository.findById(id);
        if(!issue.isPresent()){
            throw  new IssueNotFoundException();
        }
        List<Comment> comments = issue.get().getComments();
        return comments;
    }

}
