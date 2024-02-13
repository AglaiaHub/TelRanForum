package java51.forum.dto;

import lombok.Getter;

import java.util.Date;
import java.util.List;

@Getter
public class PostDto {
    String id;
    String title;
    String content;
    String author;
    String dateCreated;
    List<String> tags;
    Integer likes;
    List<String> comments;
}
