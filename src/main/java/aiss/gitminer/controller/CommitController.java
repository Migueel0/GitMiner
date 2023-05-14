package aiss.gitminer.controller;

import aiss.gitminer.exception.commits.CommitNotFoundException;
import aiss.gitminer.model.Comment;
import aiss.gitminer.model.Commit;
import aiss.gitminer.repository.CommitRepository;
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

@Tag(name="Commit", description="API for managing code commits")
@RestController
@RequestMapping("gitminer/v1/commits")
public class CommitController {


    @Autowired
    CommitRepository repository;

    @Operation(summary = "Retrieve a List of commits",
            description = "Get a list of commits",
            tags = {"commits", "get"})
    @ApiResponses({
            @ApiResponse(responseCode = "200",
                    content = {@Content(schema=@Schema(implementation = Commit.class)
                            , mediaType = "application/json" )})
    })
    @GetMapping
    public List<Commit> getAllCommits(@Parameter(description = "selected page")@RequestParam(defaultValue = "0") int page,
                                      @Parameter(description = "page size")@RequestParam(defaultValue = "10") int size,
                                      @Parameter(description = "Property to order the response, if it starts with -, it will order descending")@RequestParam(required = false) String order){
        Pageable paging;
        Page<Commit> commitPage;
        if(order != null){
            if(order.startsWith("-")){
                paging = PageRequest.of(page, size, Sort.by(order.substring(1)).descending());
            }else{
                paging = PageRequest.of(page, size,Sort.by(order).ascending());
            }
        }else{
            paging = PageRequest.of(page,size);
        }
        commitPage = repository.findAll(paging);
        return commitPage.getContent();
    }

    @Operation(
            summary = "Retrieve a Commit by id",
            description = "Get a Commit object by specifying its id",
            tags = {"commits", "get"}
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200",
                    content = {@Content(schema=@Schema(implementation = Commit.class)
                            , mediaType = "application/json" )}),
            @ApiResponse(responseCode = "404",
                    content = {@Content(schema = @Schema())})
    })
    @GetMapping("/{id}")
    public Commit findById(@Parameter(description = "id of de commit to be searched") @PathVariable String id) throws CommitNotFoundException {
        Optional<Commit> commit = repository.findById(id);
        if(!commit.isPresent()){
            throw new CommitNotFoundException();
        }
        return commit.get();
    }

}
