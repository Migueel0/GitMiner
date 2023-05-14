package aiss.gitminer.controller;

import aiss.gitminer.exception.comments.CommentNotFoundException;
import aiss.gitminer.model.Comment;
import aiss.gitminer.repository.CommentRepository;
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

@Tag(name="Comment", description="API for managing and organizing comments, feedback, and user interactions")
@RestController
@RequestMapping("gitminer/v1/comments")
public class CommentController {

    @Autowired
    CommentRepository repository;

    @Operation(summary = "Retrieve a List of comments",
    description = "Get a list of comments",
    tags = {"comments", "get"})
    @ApiResponses({
         @ApiResponse(responseCode = "200",
                 content = {@Content(schema=@Schema(implementation = Comment.class)
                         , mediaType = "application/json" )})
    })
    @GetMapping
    public List<Comment> getAllComments(@RequestParam(defaultValue = "0")int page,
                                        @RequestParam(defaultValue = "10") int size,
                                        @RequestParam(required = false) String order){

        Page<Comment> commentPage;
        Pageable paging;
        if(order !=null){
            if(order.startsWith("-")){
                paging = PageRequest.of(page, size, Sort.by(order.substring(1)).descending());
            }else{
                paging = PageRequest.of(page,size,Sort.by(order).ascending());
            }
        }else{
            paging = PageRequest.of(page,size);
        }
        commentPage = repository.findAll(paging);
        return commentPage.getContent();
    }

    @Operation(
            summary = "Retrieve a Comment by id",
            description = "Get a Comment object by specifying its id",
            tags = {"comments", "get"}
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200",
                    content = {@Content(schema=@Schema(implementation = Comment.class)
                            , mediaType = "application/json" )}),
            @ApiResponse(responseCode = "404",
            content = {@Content(schema = @Schema())})
    })
    @GetMapping("/{id}")
    public Comment findCommentById(@Parameter(description = "id of de comment to be searched") @PathVariable String id) throws CommentNotFoundException {
        Optional<Comment> comment = repository.findById(id);
        if(!comment.isPresent()){
            throw new CommentNotFoundException();
        }
        return comment.get();
    }
}
