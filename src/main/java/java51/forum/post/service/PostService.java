package java51.forum.post.service;

import java51.forum.post.dto.*;
import java51.forum.post.model.Post;

import java.util.List;

public interface PostService {

    Post addNewPost(NewPostDto newPostDto, String author);

    PostDto findPostById (String id);

    Boolean addLike (String id);

    Iterable <PostDto> findPostsByAuthor(String author);

    PostDto addComment (NewCommentDto newCommentDto, String author, String id);

    PostDto addComment(CommentDto newCommentDto, String id, String author);

    PostDto removePost(String postId);

    Iterable <PostDto> findPostsByTags (List<String> tags);

    Iterable <PostDto> findPostsByPeriod (DataPeriodDto period);

    PostDto updatePost(NewPostDto newPostDto, String id);
}
