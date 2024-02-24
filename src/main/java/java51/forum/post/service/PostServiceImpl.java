package java51.forum.post.service;

import java51.forum.post.dao.PostRepository;
import java51.forum.post.dto.*;
import java51.forum.post.dto.exeption.PostNotFoundException;
import java51.forum.post.model.Comment;
import java51.forum.post.model.Post;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService{

    final ModelMapper modelMapper;
    final PostRepository postRepository;

    @Override
    public Post addNewPost(NewPostDto newPostDto, String author) {
        Post post = modelMapper.map(newPostDto, Post.class);
        post.setAuthor(author);
        postRepository.save(post);
        return post;
    }

    @Override
    public PostDto findPostById(String id) {
        return null;
    }

    @Override
    public Boolean addLike(String id) {
        Post post = postRepository.findById(id).orElseThrow(PostNotFoundException::new);
        post.addLike();
        postRepository.save(post);
        return true;
    }

    @Override
    public List<PostDto> findPostsByAuthor(String author) {

        return postRepository.findPostByAuthorIgnoreCase(author)
                .map(p -> modelMapper.map(p, PostDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public PostDto addComment(NewCommentDto newCommentDto, String author, String id) {
        Comment comment = new Comment(author, newCommentDto.getMessage());
        Post post = postRepository.findById(id).orElseThrow(PostNotFoundException::new);
        post.addComments(comment);

        return null;
    }

    @Override
    public PostDto addComment(CommentDto newCommentDto, String id, String author) {
        return null;
    }

    @Override
    public PostDto removePost(String id) {
        Post post = postRepository.findById(id).orElseThrow(PostNotFoundException::new);
        postRepository.delete(post);
        return modelMapper.map(post, PostDto.class);
    }

    @Override
    public List<PostDto> findPostsByTags(List<String> tags) {
        return postRepository.findPostsByTagsIgnoreCase(tags)
                .map(p ->modelMapper.map(p, PostDto.class))
                .collect(Collectors.toList());

    }

    @Override
    public List<PostDto> findPostsByPeriod(DataPeriodDto period) {
//        return postRepository.findByCreatedAtBetween(period.getDateFrom(), period.getDateTo())
//                .map(p ->modelMapper.map(p, PostDto.class))
//                .collect(Collectors.toList());
        return null;
    }

    @Override
    public PostDto updatePost(NewPostDto newPostDto, String id) {
        Post post = postRepository.findById(id).orElseThrow(PostNotFoundException::new);
        String content = newPostDto.getContent();
        if (content != null ) post.setContent(content);

        String title = newPostDto.getTitle();
        if ( title != null ) post.setTitle(title);

        Set<String> tags = newPostDto.getTags();
        if (tags != null ) tags.forEach(post::addTag);

        postRepository.save(post);
        return modelMapper.map(post, PostDto.class);
    }
}
