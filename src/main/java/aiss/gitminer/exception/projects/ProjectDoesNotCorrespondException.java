package aiss.gitminer.exception.projects;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.INTERNAL_SERVER_ERROR, reason = "You cannot update a project with a different project data")
public class ProjectDoesNotCorrespondException extends Exception{
}
