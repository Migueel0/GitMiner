package aiss.gitminer.controller;

import aiss.gitminer.model.Comment;
import aiss.gitminer.repository.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("gitminer/comments")
public class CommentController {

    @Autowired
    CommentRepository repository;

    @GetMapping
    public List<Comment> getAll(){
        return repository.findAll();
    }

    @GetMapping("/{id}")
    public Comment findById(@PathVariable String id){
        Optional<Comment> comment = repository.findById(id);
        return comment.get();
    }
}
