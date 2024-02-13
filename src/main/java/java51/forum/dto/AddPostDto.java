package java51.forum.dto;

import lombok.Getter;

@Getter
public class AddPostDto {
    String title;
    String content;
    String[] tags;
}
