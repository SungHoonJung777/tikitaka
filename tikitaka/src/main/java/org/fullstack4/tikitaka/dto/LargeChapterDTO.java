package org.fullstack4.tikitaka.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LargeChapterDTO {
    private int largeIdx;
    private int classIdx;
    private String largeChapter;
}
