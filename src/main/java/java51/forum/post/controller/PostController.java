package java51.forum.post.controller;

import java51.forum.post.dto.NewCommentDto;
import java51.forum.post.dto.NewPostDto;
import java51.forum.post.dto.DataPeriodDto;
import java51.forum.post.dto.PostDto;
import java51.forum.post.model.Post;
import java51.forum.post.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping ("/forum")
public class PostController  {
    final PostService postService;

    @PostMapping ("/post/{user}")
    public Post addPost(@RequestBody NewPostDto newPostDto, @PathVariable ("user") String author) {
        return postService.addNewPost(newPostDto, author);
    }

    @GetMapping ("/post/{postId}")
    public PostDto findPostById(@PathVariable String id) {
        return postService.findPostById(id);
    }

    @PutMapping ("/post/{postId}/like")
    @ResponseStatus(HttpStatus.NO_CONTENT)          //??????? TODO WAS IS DAS
    public Boolean addLike(@PathVariable String postId) {
        return postService.addLike(postId);
    }

    @GetMapping ("/posts/author/{author}")
    public Iterable <PostDto> findPostsByAuthor(@PathVariable String author) {
        return postService.findPostsByAuthor(author);
    }

    @PutMapping ("/post/{postId}/comment/{user}")
    public PostDto addComment(@RequestBody NewCommentDto newCommentDto, @PathVariable String postId, @PathVariable String user) {
        return postService.addComment (newCommentDto, postId, user);
    }

    @DeleteMapping ("/post/{postId}")
    public PostDto deletePost(@PathVariable String postId) {
        return postService.removePost(postId);
    }

    @PostMapping ("/posts/tags")
    public Iterable <PostDto> findPostsByTags(@RequestBody List<String> tags) {
        return postService.findPostsByTags(tags);
    }

    @PostMapping("/posts/period")
    public Iterable <PostDto> findPostsByPeriod(@RequestBody DataPeriodDto period) {
        return postService.findPostsByPeriod(period);
    }

    @PutMapping ("/post/{postId}")
    public PostDto updatePost(@RequestBody NewPostDto newPostDto, @PathVariable String postId) {
        return postService.updatePost(newPostDto, postId);
    }
}
