package java51.forum.service;

import java51.forum.dao.PostRepository;
import java51.forum.dto.AddPostDto;
import java51.forum.dto.PeriodDto;
import java51.forum.dto.PostDto;
import java51.forum.dto.UpdatePostDto;
import java51.forum.model.Post;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService{

    final ModelMapper modelMapper;
    final PostRepository postRepository;

    @Override
    public Post addPost(AddPostDto addPostDto, String postId) {
        return modelMapper.map(addPostDto, Post.class);
    }

    @Override
    public PostDto findPostById(String id) {
        return null;
    }

    @Override
    public Boolean addLike(String postId) {
        return null;
    }

    @Override
    public List<PostDto> findPostsByAuthor(String user) {
        return null;
    }

    @Override
    public PostDto addComment(String message, String postId, String user) {
        return null;
    }

    @Override
    public Optional<PostDto> deletePost(String postId) {
        return Optional.empty();
    }

    @Override
    public List<PostDto> findPostsByTags(List<String> tags) {
        return null;
    }

    @Override
    public List<PostDto> findPostsByPeriod(PeriodDto period) {
        return null;
    }

    @Override
    public PostDto updatePost(UpdatePostDto updatePostDto, String postId) {
        return null;
    }
}
