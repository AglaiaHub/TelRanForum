package java51.forum.post.model;

import lombok.EqualsAndHashCode;
import lombok.Setter;

import java.time.LocalDateTime;

@EqualsAndHashCode(of = { "author", "dateCreated"})
public class Comment {
    @Setter
    String author;
    @Setter
    String message;
    LocalDateTime dateCreated;
    Integer likes;

    public Comment(String author, String message) {
        this.author = author;
        this.message = message;
        dateCreated = LocalDateTime.now();
    }
}
