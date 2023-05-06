package aiss.gitminer.repository;

import aiss.gitminer.model.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.ArrayList;
import java.util.List;

public interface CommentRepository extends JpaRepository<Comment,String> {

}
