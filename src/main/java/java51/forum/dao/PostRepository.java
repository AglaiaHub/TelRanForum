package java51.forum.dao;

import java51.forum.dto.AddPostDto;
import java51.forum.dto.PostDto;
import java51.forum.model.Post;
import org.springframework.data.repository.CrudRepository;

public interface PostRepository
        extends CrudRepository<Post, String>
{
}
