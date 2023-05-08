package aiss.gitminer.controller;

import aiss.gitminer.exception.IssueNotFoundException;
import aiss.gitminer.model.Issue;
import aiss.gitminer.model.User;
import aiss.gitminer.repository.IssueRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api/gitminer/issues")
public class IssueController {

    //TODO: Add more operations and exceptions
    @Autowired
    IssueRepository repository;

    @GetMapping
    public List<Issue> getAllIssues() {
        return repository.findAll();
    }

    @GetMapping("/{id}")
    public Issue findIssueById(@PathVariable String id) throws IssueNotFoundException {
        Optional<Issue> issue = repository.findById(id);
        if(!issue.isPresent()){
            throw new IssueNotFoundException();
        }
        return issue.get();
    }

    @GetMapping
    public List<Issue> findIssuesByState(@RequestParam(defaultValue = "all") String state) {
        if(state.equals("all")){
             return getAllIssues();
        }else {
            List<Issue> issues = getAllIssues().stream().filter(x -> x.getState().equals(state)).collect(Collectors.toList());
            return issues;
        }
    }

    @GetMapping("/{id}/author")
    public User findIssueAuthor(@PathVariable String id) throws IssueNotFoundException {
        Optional<Issue> issue = repository.findById(id);
        if(!issue.isPresent()){
            throw new IssueNotFoundException();
        }
        User author = issue.get().getAuthor();
        return author;
    }
    @GetMapping("/{id}/assignee")
    public User findIssueAssignee(@PathVariable String id) throws IssueNotFoundException {
        Optional<Issue> issue = repository.findById(id);
        if(!issue.isPresent()){
            throw new IssueNotFoundException();
        }
        User assignee = issue.get().getAssignee();
        return assignee;
    }

}
