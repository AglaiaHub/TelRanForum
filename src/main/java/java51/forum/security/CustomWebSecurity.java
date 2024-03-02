package java51.forum.security;

import java51.forum.post.dao.PostRepository;
import java51.forum.post.model.Post;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service ("customSecurity")     //- если надо указать имя
@RequiredArgsConstructor
public class CustomWebSecurity {
    final PostRepository postRepository;

    public boolean checkPostAuthor(String postId, String userName){
        Post post = postRepository.findById(postId).orElse(null);
        return post != null && userName.equals(post.getAuthor());
    }
}
