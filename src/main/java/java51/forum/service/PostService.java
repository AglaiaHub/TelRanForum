package java51.forum.service;

import java51.forum.dto.AddPostDto;
import java51.forum.dto.PeriodDto;
import java51.forum.dto.PostDto;
import java51.forum.dto.UpdatePostDto;

import java.util.List;
import java.util.Optional;

public interface PostService {

    PostDto addPost (AddPostDto addPostDto, Integer postId);

    PostDto findPostById (Integer id);

    Boolean addLike (Integer postId);

    List <PostDto> findPostsByAuthor(String user);

    PostDto addComment (String message, Integer postId, String user);

    Optional<PostDto> deletePost(Integer postId);

    List<PostDto> findPostsByTags (List<String> tags);

    List<PostDto> findPostsByPeriod (PeriodDto period);

    PostDto updatePost (UpdatePostDto updatePostDto, Integer postId);


}
