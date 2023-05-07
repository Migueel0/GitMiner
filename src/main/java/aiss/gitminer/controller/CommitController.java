package aiss.gitminer.controller;

import aiss.gitminer.model.Comment;
import aiss.gitminer.model.Commit;
import aiss.gitminer.repository.CommentRepository;
import aiss.gitminer.repository.CommitRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("gitminer/commits")
public class CommitController {

    @Autowired
    CommitRepository repository;

    @GetMapping
    public List<Commit> getAllCommits(){

        return repository.findAll();
    }

    @GetMapping("/{id}")
    public Commit findById(@PathVariable String id){
        Optional<Commit> commit = repository.findById(id);
        return commit.get();
    }
}
