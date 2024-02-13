package java51.forum.model;

import java.util.List;

public class Post {
    String id;
    String title;
    String content;
    String author;
    String dateCreated;
    List<String> tags;
    Integer likes;
    List<String> comments;
}
