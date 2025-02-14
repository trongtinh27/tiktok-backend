package com.tiktok.project.dto.response;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Data
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SearchResponse {
    private List<UserSearchInfo> data;

    @Override
    public String toString() {
        return "{" +
                "data=" + data +
                '}';
    }

    @Data
    @Setter
    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UserSearchInfo {
        private String nickname;
        private String fullName;
        private String avatar;
        private boolean tick;

    }
}
