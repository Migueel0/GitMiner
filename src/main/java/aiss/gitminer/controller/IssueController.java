package aiss.gitminer.controller;

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

    @Autowired
    IssueRepository repository;

    @GetMapping
    public List<Issue> getAllIssues() {
        return repository.findAll();
    }

    @GetMapping("{/id}")
    public Issue findIssueById(@PathVariable String id) {
        Optional<Issue> issue = repository.findById(id);
        return issue.get();
    }

    @GetMapping("{/state}")
    public List<Issue> findIssuesByState(String state) {
        List<Issue> issues = getAllIssues().stream().filter(x->x.getState().equals(state)).collect(Collectors.toList());
        return issues;

    }

}
