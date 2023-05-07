package aiss.gitminer.controller;

import aiss.gitminer.exception.IssueNotFoundException;
import aiss.gitminer.model.Issue;
import aiss.gitminer.repository.IssueRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("gitminer/issues")
public class IssueController {

    //TODO: Add more operations and exceptions
    @Autowired
    IssueRepository repository;

    @GetMapping
    public List<Issue> getAllIssues() {
        return repository.findAll();
    }

    @GetMapping("{/id}")
    public Issue findIssueById(@PathVariable String id) throws IssueNotFoundException {
        Optional<Issue> issue = repository.findById(id);
        if(!issue.isPresent()){
            throw new IssueNotFoundException();
        }
        return issue.get();
    }


    //TODO: Change this method in order to be a API filter using PathVariables (Goal: Something like this http://apipath?state=open)
    @GetMapping("{/state}")
    public List<Issue> findIssuesByState(String state) {
        List<Issue> issues = getAllIssues().stream().filter(x->x.getState().equals(state)).collect(Collectors.toList());
        return issues;

    }

}
