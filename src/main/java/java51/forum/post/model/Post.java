package java51.forum.post.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Getter
@EqualsAndHashCode(of = "id")
@Document(collection = "Forum")
public class Post {
    String id;
    @Setter
    String title;
    @Setter
    String content;
    @Setter
    String author;
    LocalDateTime dateCreated;
    Set<String> tags;
    Integer likes;
    List<Comment> comments;

    public Post(){
        dateCreated = LocalDateTime.now();
        comments = new ArrayList<>();
    }

    public Post(String title, String content, String author, Set<String> tags){
        this();
        this.title = title;
        this.content = content;
        this.author = author;
        this.tags = tags;
    }

    public void addTag(String s) {
        tags.add(s);
    }

    public Boolean addLike() {
        likes ++;
        return true;
    }

    public void addComments(Comment comment) {
        comments.add(comment);
    }
}
