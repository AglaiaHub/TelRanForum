package java51.forum.controller;

import java51.forum.dto.AddPostDto;
import java51.forum.dto.PeriodDto;
import java51.forum.dto.PostDto;
import java51.forum.dto.UpdatePostDto;
import java51.forum.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class PostController  {
    final PostService postService;

    @PostMapping ("/post/{postId}")
    public PostDto addPost(@RequestBody AddPostDto addPostDto, @PathVariable Integer postId) {
        return postService.addPost(addPostDto, postId);
    }

    @GetMapping ("/post/{postId}")
    public PostDto findPostById(@PathVariable Integer id) {
        return postService.findPostById(id);
    }

    @PutMapping ("/post/{postId}/like")
    public Boolean addLike(@PathVariable Integer postId) {
        return postService.addLike(postId);
    }

    @GetMapping ("/posts/author/{user}")
    public List<PostDto> findPostsByAuthor(@PathVariable String user) {
        return postService.findPostsByAuthor(user);
    }

    @PutMapping ("/post/{postId}/comment/{user}")
    public PostDto addComment(@RequestBody String message, @PathVariable Integer postId, @PathVariable String user) {
        return postService.addComment (message, postId, user);
    }

    @DeleteMapping ("/post/{postId}")
    public Optional<PostDto> deletePost(@PathVariable Integer postId) {
        return postService.deletePost(postId);
    }

    @PostMapping ("/posts/tags")
    public List<PostDto> findPostsByTags(@RequestBody List<String> tags) {
        return postService.findPostsByTags(tags);
    }

    @PostMapping("/posts/period")
    public List<PostDto> findPostsByPeriod(@RequestBody PeriodDto period) {
        return postService.findPostsByPeriod(period);
    }

    @PutMapping ("/post/{postId}")
    public PostDto updatePost(@RequestBody UpdatePostDto updatePostDto, @PathVariable Integer postId) {
        return postService.updatePost(updatePostDto, postId);
    }
}
