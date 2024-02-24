package java51.forum.post.dto;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PostDto {
    String id;
    String title;
    String content;
    String author;
    LocalDateTime dateCreated;
    @Singular
    List<String> tags;
    Integer likes;
    List<CommentDto> comments;
}
