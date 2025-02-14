package com.tiktok.project.dto.request;

import com.tiktok.project.util.DifferentUsers;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@DifferentUsers // Áp dụng custom validation
public class ChatRoomRequest {
    @NotNull
    @Min(1)
    private Integer userOneId;

    @NotNull
    @Min(1)
    private Integer userTwoId;
}
