package com.tiktok.project.dto.response;

import java.util.Date;

import lombok.*;

@Data
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CommentDTO {
    private int id;
    private String displayName;
    private String username;
    private String avatarUser;
    private String content;
    private Date create_at;
}
