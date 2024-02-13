package java51.forum.service;

import java51.forum.dto.AddPostDto;
import java51.forum.dto.PeriodDto;
import java51.forum.dto.PostDto;
import java51.forum.dto.UpdatePostDto;
import java51.forum.model.Post;

import java.util.List;
import java.util.Optional;

public interface PostService {

    Post addPost (AddPostDto addPostDto, String postId);

    PostDto findPostById (String id);

    Boolean addLike (String postId);

    List <PostDto> findPostsByAuthor(String user);

    PostDto addComment (String message, String postId, String user);

    Optional<PostDto> deletePost(String postId);

    List<PostDto> findPostsByTags (List<String> tags);

    List<PostDto> findPostsByPeriod (PeriodDto period);

    PostDto updatePost (UpdatePostDto updatePostDto, String postId);


}
