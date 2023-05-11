package aiss.gitminer.controller;
import aiss.gitminer.exception.projects.ProjectDoesNotCorrespondException;
import aiss.gitminer.exception.projects.ProjectNotFoundException;
import aiss.gitminer.model.*;
import aiss.gitminer.repository.ProjectRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@Tag(name= "Project", description = "Project management API")
@RestController
@RequestMapping("gitminer/v1/projects")
public class ProjectController {
    @Autowired
    ProjectRepository repository;

    @Operation(summary = "Retrieve a List of projects",
            description = "Get a list of projects",
            tags = {"projects", "get"})
    @ApiResponses({
            @ApiResponse(responseCode = "200",
                    content = {@Content(schema=@Schema(implementation = Project.class)
                            , mediaType = "application/json" )})
    })
    @GetMapping
    public List<Project> getAllProjects(){
        return repository.findAll();
    }

    @Operation(
            summary = "Retrieve a Project by id",
            description = "Get a Project object by specifying its id",
            tags = {"projects", "get"}
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200",
                    content = {@Content(schema=@Schema(implementation = Project.class)
                            , mediaType = "application/json" )}),
            @ApiResponse(responseCode = "404",
                    content = {@Content(schema = @Schema())})
    })
    @GetMapping("/{id}")
    public Project findProjectById (@Parameter(description = "id of the project to be searched") @PathVariable String id) throws ProjectNotFoundException {
        Optional<Project> project = repository.findById(id);
        if(!project.isPresent()){
            throw new ProjectNotFoundException();
        }
        return project.get();
    }


    @Operation(
            summary = "Retrieve Commits by Project ID",
            description = "Get a list of commits by specifying the Project ID",
            tags = {"commits", "get"}
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Commit.class))),
            @ApiResponse(responseCode = "404",
                    content = @Content(schema = @Schema()))
    })
    @GetMapping("/{id}/commits")
    public List<Commit> findCommitsByProjectId (@Parameter(description = "id of the project whose comments are going to be searched")@PathVariable String id) throws ProjectNotFoundException {
        Optional<Project> project = repository.findById(id);
        if(!project.isPresent()){
            throw new ProjectNotFoundException();
        }
        List<Commit> commits =  project.get().getCommits();
        return commits;
    }

    @Operation(
            summary = "Retrieve Issues by Project ID",
            description = "Get a list of issues by specifying the Project ID",
            tags = {"issues", "get"}
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Issue.class))),
            @ApiResponse(responseCode = "404",
                    content = @Content(schema = @Schema()))
    })
    @GetMapping("/{id}/issues")
    public List<Issue> findIssuesByProjectId (@Parameter(description = "id of the project whose issues are going to be searched")@PathVariable String id) throws ProjectNotFoundException {
        Optional<Project> project = repository.findById(id);
        if(!project.isPresent()){
            throw new ProjectNotFoundException();
        }
        List<Issue> issues =  project.get().getIssues();
        return issues;
    }
    @Operation(summary = "Insert a Project",
            description = "Add a new project whose data is passed in the body of the request in JSON format",
            tags= {"projects", "post"})
    @ApiResponses({
            @ApiResponse(responseCode = "201", content = {@Content(schema = @Schema(implementation= Project.class), mediaType = "application/json")}),
            @ApiResponse(responseCode = "400", content = {@Content(schema = @Schema())})
    })
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public Project createNewProject(@RequestBody Project p){

        return repository.save(p);
    }

    @Operation(
            summary = "Update a Project",
            description = "Update a project by specifying its ID",
            tags = {"projects", "update"}
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Project updated successfully"),
            @ApiResponse(responseCode = "404", description = "Project not found")
    })
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
    @Operation(
            summary = "Delete a Project",
            description = "Delete a project object by specifying its id",
            tags = {"users", "delete"}
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", content = {@Content(schema = @Schema())}),
            @ApiResponse(responseCode = "400", content = {@Content(schema = @Schema())}),
            @ApiResponse(responseCode = "404", content = {@Content(schema = @Schema())})
    })
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
