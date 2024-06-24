package org.fullstack4.tikitaka.dto;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

@Log4j2
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PageRequestDTO {
    @Builder.Default

    private int total_count=0;
    @Builder.Default
    @Min(1)
    private int page=1;
    @Builder.Default
    private int page_size=12;
    @Builder.Default
    @Min(1)
    private int total_page=1;
    @Builder.Default
    private int page_skip_count=0;
    @Builder.Default
    private int page_block_size=10;
    @Builder.Default
    private int page_block_start=1;
    @Builder.Default
    private int page_block_end=10;
    private boolean prev_page_flag;
    private boolean next_page_flag;

    private String search_type; //검색타입 (title:제목, content:내용, memberId:사용자ID...)
    private String[] search_types;
    private String search_word;
    private String linkParams;  //리턴 url

    private String school;
    private String grade;
    private String semester;
    private String subject;
    private String chapter;
    private String mediumChapter;
    private String chaxi;
    private String order;

    private String type;

    public int getTotal_page() {
        this.total_page = (int)Math.ceil(this.total_count/(double)this.page_size);
        return (this.total_page < 1 ? 1 : this.total_page);
    }

    public int getPage_skip_count() {
        this.page = (this.page < 1 ? 1 : this.page);
        return (this.page-1)*this.page_size;
    }

    public int getPage_block_start() {
        this.page_block_start = (int)(Math.ceil(page / (double)page_block_size) -1 ) * page_block_size + 1;
        return this.page_block_start;
    }

    public int getPage_block_end() {
        if (this.total_page < 1) {
            this.page_block_end = 1;
        } else {
            this.page_block_end = (int)Math.ceil(this.page/(double)this.page_block_size)*this.page_block_size;
            this.page_block_end = (this.page_block_end < 1 ? 1 : this.page_block_end);
            this.page_block_end = (this.total_page > this.page_block_end ? this.page_block_end : this.total_page);
        }
        return this.page_block_end;
    }

    public boolean getPrev_page_flag() {
        return (this.page_block_start > 1);
    }

    public boolean getNext_page_flag() {
        return (this.total_page > this.page_block_end);
    }

    public String[] getSearch_types() {
        if (search_types == null || search_type.isEmpty()) {
            return null;
        }
        return search_type.split("");
    }

    public PageRequest getPageable(String...props) {
        if(props[0].toString().equals("title")) {
            return PageRequest.of(this.page-1, this.page_size, Sort.by(props).ascending());
        } else {
            return PageRequest.of(this.page-1, this.page_size, Sort.by(props).descending());
        }
    }
    public String getLinkParams() {
        if (this.linkParams == null || this.linkParams.isEmpty()) {
            StringBuilder sb = new StringBuilder();
//            sb.append("page=" + this.page);
            sb.append("&page_size=" + this.page_size);

            if (search_type != null && !search_type.isEmpty()) {
                sb.append("&search_type=" + this.search_type);
            }
            if (search_word != null && !search_word.isEmpty() && !search_word.equals("")) {
                try {
                    sb.append("&search_word=" + URLEncoder.encode(search_word, "UTF-8"));
                } catch(UnsupportedEncodingException e) {

                }
            }
            if (school != null && !school.isEmpty()) {
                sb.append("&classIdx=" + school);
            }
            if (grade != null && !grade.isEmpty()) {
                sb.append("$grade=" + grade);
            }
            if (semester != null && !semester.isEmpty()) {
                sb.append("&semester=" + semester);
            }
            if (subject != null && !subject.isEmpty()) {
                sb.append("&subject=" + subject);
            }
            if (chapter != null && !chapter.isEmpty()) {
                sb.append("&chapter=" + chapter);
            }
            if (mediumChapter != null && !mediumChapter.isEmpty()) {
                sb.append("&mediumChapter=" + mediumChapter);
            }
            if (chaxi != null && !chaxi.isEmpty()) {
                sb.append("&chaxi=" + chaxi);
            }
            if (order != null && !order.isEmpty()) {
                sb.append("&order=" + order);
            }
            linkParams = sb.toString();
        }
        return linkParams;
    }



}
