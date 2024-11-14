package com.tiktok.project.dto.request;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class CollectRequest {
    private int userId;
    private int videoId;
}
