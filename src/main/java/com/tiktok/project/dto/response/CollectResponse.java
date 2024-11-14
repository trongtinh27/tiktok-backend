package com.tiktok.project.dto.response;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class CollectResponse {
    private boolean collectStatus;
    private int countCollect;
}