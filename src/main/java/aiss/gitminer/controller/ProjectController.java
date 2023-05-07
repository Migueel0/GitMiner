package aiss.gitminer.controller;


import aiss.gitminer.model.Project;
import aiss.gitminer.repository.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("gitminer/projects")
public class ProjectController {

    @Autowired
    ProjectRepository repository;
    @GetMapping
    public List<Project> getAllProjects(){
        return repository.findAll();
    }

    @GetMapping("/{id}")
    public Project findProjectById (@PathVariable String id){
        Optional<Project> project = repository.findById(id);
        return project.get();
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public Project createNewProject(@RequestBody Project p){
        return repository.save(p);
    }


}
