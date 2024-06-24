package org.fullstack4.tikitaka.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MediumChapterDTO {
    private int mediumIdx;
    private int largeIdx;
    private String mediumChapter;
}
