package org.fullstack4.tikitaka.controller;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.fullstack4.tikitaka.dto.QuizDTO;
import org.fullstack4.tikitaka.dto.QuizMemberDTO;
import org.fullstack4.tikitaka.dto.QuizReportDTO;
import org.fullstack4.tikitaka.dto.QuizRoomDTO;
import org.fullstack4.tikitaka.service.ReportServiceIf;
import org.fullstack4.tikitaka.utils.ExcelUtil;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

@Log4j2
@RequiredArgsConstructor
@Controller
@RequestMapping("/report")
public class ReportController {
    private final ReportServiceIf reportServiceIf;
    @GetMapping("/list")
    public void reportlist(HttpSession session,
                           Model model,
                           @RequestParam(name = "endYn", defaultValue = "s") String endYn,
                           @RequestParam(name = "search_word", defaultValue = "") String search_word){
//        int teacher_idx = (Integer)session.getAttribute("member_idx");
        int teacher_idx = 1;//실제로 테스트할때는 현재줄 삭제 혹은 주석처리, 위에줄 주석 해제하고 사용할것

        List<QuizRoomDTO> reportlist = reportServiceIf.list(teacher_idx, endYn, search_word);

        model.addAttribute("reportlist", reportlist);
        model.addAttribute("endYn", endYn);
        model.addAttribute("search_word", search_word);
        model.addAttribute("pageName", "report");
    }
    @GetMapping("/detail")
    public void reportdetail(HttpSession session,
                             Model model,
                             @RequestParam(name = "roomIdx", defaultValue = "0") int roomIdx){
        //        int teacher_idx = (Integer)session.getAttribute("member_idx");
        int teacher_idx = 1;//실제로 테스트할때는 현재줄 삭제 혹은 주석처리, 위에줄 주석 해제하고 사용할것

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd a HH:mm:ss");

        QuizRoomDTO roominfo = reportServiceIf.Roominfo(roomIdx);//방 정보
        String result = reportServiceIf.quizdanwon(roominfo.getQuizIdx());//퀴즈의 단원정보
        QuizDTO quizinfo = reportServiceIf.quizinfo(roominfo.getQuizIdx());//퀴즈의 정보
        List<QuizReportDTO> questioninfo = reportServiceIf.quizReport(roomIdx);//문제별 정답 정보
        List<QuizMemberDTO> dtolist = reportServiceIf.memberscore(roomIdx);//멤버별 정보

        for(int i = 0; i<dtolist.size();i++){
            QuizMemberDTO dto = dtolist.get(i);
            dto.setEnterDate(dto.getRegDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd a HH:mm:ss")));
        }

        if(roominfo.getContinueYN().equals("Y")){
            roominfo.setContinueYN("재입장 가능");
        }else{
            roominfo.setContinueYN("재입장 불가능");
        }
        if(roominfo.getScoreYN().equals("Y")){
            roominfo.setScoreYN("점수 표시");
        }else{
            roominfo.setScoreYN("점수 미표시");
        }
        if(roominfo.getCommentYN().equals("Y")){
            roominfo.setCommentYN("해설 표시");
        }else{
            roominfo.setCommentYN("해설 미표시");
        }

        model.addAttribute("dtolist", dtolist);
        model.addAttribute("questioninfo",questioninfo);
        model.addAttribute("roominfo", roominfo);
        model.addAttribute("result",result);
        model.addAttribute("quizinfo", quizinfo);
        model.addAttribute("pageName", "report");

    }
    @GetMapping("/listdown")
    public void excelDownload(HttpSession session
                              , HttpServletResponse response,
                              @RequestParam(name = "endYn", defaultValue = "s") String endYn,
                              @RequestParam(name = "search_word", defaultValue = "") String search_word) throws Exception{
        String fileName = "report_"+ LocalDateTime.now();
//        int teacher_idx = (Integer)session.getAttribute("member_idx");
        int teacher_idx = 1;//실제로 테스트할때는 현재줄 삭제 혹은 주석처리, 위에줄 주석 해제하고 사용할것

        ExcelUtil.excelDownload(response, reportServiceIf, teacher_idx, endYn, search_word);
    }
    @GetMapping("/detaildown")
    public void detailexcelDownload(HttpSession session,
                                    HttpServletResponse response,
                                    @RequestParam(name = "roomIdx", defaultValue = "0") int roomIdx) throws Exception{
        String fileName = "detail_"+ LocalDateTime.now();;
//        int teacher_idx = (Integer)session.getAttribute("member_idx");
        int teacher_idx = 1;//실제로 테스트할때는 현재줄 삭제 혹은 주석처리, 위에줄 주석 해제하고 사용할것
        QuizRoomDTO roominfo = reportServiceIf.Roominfo(roomIdx);

        ExcelUtil.excelDownload2(response, reportServiceIf, roomIdx);
    }
    @ResponseBody
    @PostMapping("/question")
    public List<QuizReportDTO> questiondetail(@RequestParam(name = "roomIdx", defaultValue = "0") int roomIdx,
                                              @RequestParam(name = "question", defaultValue = "") String question){
        List<QuizReportDTO> result = reportServiceIf.questiondetail(roomIdx, question);
        System.out.println("roomIdx : "+roomIdx);
        log.info("test-----------------:{}",result);
        return result;
    }
    @ResponseBody
    @PostMapping("/member")
    public List<QuizReportDTO> memberdetail(@RequestParam(name = "roomIdx", defaultValue = "0") int roomIdx,
                                              @RequestParam(name = "studentName", defaultValue = "") String studentName){
        List<QuizReportDTO> result = reportServiceIf.memberdetail(roomIdx, studentName);
        System.out.println("roomIdx : "+roomIdx);
        log.info("test-----------------:{}",result);
        return result;
    }
    @ResponseBody
    @PostMapping("/endreport")
    public void endreport(@RequestParam(name = "roomIdx", defaultValue = "0") int roomIdx){
        reportServiceIf.endreport(roomIdx);
    }
    @GetMapping("/deletereport")
    public String deletereport(@RequestParam(name = "roomIdx", defaultValue = "0") String roomIdx){
        reportServiceIf.deletereport(roomIdx.substring(1,roomIdx.length()-1));
        return "redirect:/report/list";
    }
}