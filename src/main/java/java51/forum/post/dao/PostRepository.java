package java51.forum.post.dao;

import java51.forum.post.model.Post;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.stream.Stream;

public interface PostRepository extends CrudRepository<Post, String> {


    Stream<Post> findPostByAuthorIgnoreCase(String author);
    Stream<Post> findPostsByTagsIgnoreCase(List<String> tags);
//    Stream<Post> findPostsByCreatedAtBetween(LocalDate startDate, LocalDate endDate);
}
