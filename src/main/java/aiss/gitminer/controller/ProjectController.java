package aiss.gitminer.controller;
import aiss.gitminer.exception.projects.ProjectDoesNotCorrespondException;
import aiss.gitminer.exception.projects.ProjectNotFoundException;
import aiss.gitminer.model.Commit;
import aiss.gitminer.model.Issue;
import aiss.gitminer.model.Project;
import aiss.gitminer.repository.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("gitminer/v1/projects")
public class ProjectController {
    @Autowired
    ProjectRepository repository;
    @GetMapping
    public List<Project> getAllProjects(){
        return repository.findAll();
    }

    @GetMapping("/{id}")
    public Project findProjectById (@PathVariable String id) throws ProjectNotFoundException {
        Optional<Project> project = repository.findById(id);
        if(!project.isPresent()){
            throw new ProjectNotFoundException();
        }
        return project.get();
    }

    @GetMapping("/{id}/commits")
    public List<Commit> findCommitsByProjectId (@PathVariable String id) throws ProjectNotFoundException {
        Optional<Project> project = repository.findById(id);
        if(!project.isPresent()){
            throw new ProjectNotFoundException();
        }
        List<Commit> commits =  project.get().getCommits();
        return commits;
    }

    @GetMapping("/{id}/issues")
    public List<Issue> findIssuesByProjectId (@PathVariable String id) throws ProjectNotFoundException {
        Optional<Project> project = repository.findById(id);
        if(!project.isPresent()){
            throw new ProjectNotFoundException();
        }
        List<Issue> issues =  project.get().getIssues();
        return issues;
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public Project createNewProject(@RequestBody Project p){
        return repository.save(p);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PutMapping("/{id}")
    public void updateProject(@RequestBody @Valid Project updatedProject, @PathVariable String id) throws ProjectDoesNotCorrespondException {
        Optional<Project> projectData = repository.findById(id);
        Project p = projectData.get();
        if(!p.getId().equals(updatedProject.getId())){
            throw new ProjectDoesNotCorrespondException();
        }
        p.setName(updatedProject.getName());
        p.setCommits(updatedProject.getCommits());
        p.setIssues(updatedProject.getIssues());

    }
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    public void deleteProject(@PathVariable String id) throws ProjectNotFoundException {
        if(repository.existsById(id)){
            repository.deleteById(id);
        }else{
            throw new ProjectNotFoundException();
        }
    }


}
