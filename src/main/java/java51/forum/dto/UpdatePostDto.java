package java51.forum.dto;

import lombok.Getter;

import java.util.List;

@Getter
public class UpdatePostDto {
    String title;
    List<String> tags;
    String content;
}
