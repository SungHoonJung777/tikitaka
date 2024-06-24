package org.fullstack4.tikitaka.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.fullstack4.tikitaka.dto.*;
import org.fullstack4.tikitaka.service.*;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Log4j2
@RequiredArgsConstructor
@Controller
public class MainController {

    private final MainServiceIf mainServiceIf;
    private final QuizGameServiceIf quizGameServiceIf;
    private final QuizServiceIf quizServiceIf;


    @GetMapping("/")
    public String mainGET(Model model, HttpServletRequest req) {
        List<QuizDTO> likeList = mainServiceIf.mainListByLike();
        List<QuizDTO> regDateList = mainServiceIf.mainListByRegDate();
        HttpSession session = req.getSession();
        if(session.getAttribute("quizDTO") != null) {
            session.removeAttribute("quizDTO");
        }
        model.addAttribute("likeList", likeList);
        model.addAttribute("regDateList", regDateList);
        return "main";
    }

    @GetMapping("/main2")
    public String main2GET(PageRequestDTO pageRequestDTO,
                           @RequestParam(name="search_word", defaultValue = "") String search_word,
                           @RequestParam(name="order", defaultValue = "regDate") String order,
                           @RequestParam(name="school", defaultValue = "") String school,
                           @RequestParam(name="grade", defaultValue = "") String grade,
                           @RequestParam(name="semester", defaultValue = "") String semester,
                           @RequestParam(name="subject", defaultValue = "") String subject,
                           @RequestParam(name="chapter", defaultValue = "") String chapter,
                           @RequestParam(name="mediumChapter", defaultValue = "") String mediumChapter,
                           @RequestParam(name="chaxi", defaultValue = "") String chaxi,
                           @RequestParam(name="classIdx", defaultValue = "0") int classIdx,
                           @RequestParam(name="largeIdx", defaultValue = "0") int largeIdx,
                           @RequestParam(name = "mediumIdx", defaultValue = "0") int mediumIdx,
                           @RequestParam(name="smallIdx", defaultValue = "0") int smallIdx,
                           @RequestParam(name="page", defaultValue = "1") int page,
                           HttpSession session,
                           Model model) {
        log.info("----------------------------main2");

        PageResponseDTO<QuizDTO> pageResponseDTO = mainServiceIf.list(pageRequestDTO, order);
        int memberIdx = session.getAttribute( "memberIdx" ) != null ? (int) session.getAttribute( "memberIdx" ) : 0;
        List<Integer> likeList = mainServiceIf.likeList(memberIdx);
        List<ClassDTO> subjectList = mainServiceIf.subjectList(grade, semester);
        List<ClassDTO> mSubjectList = mainServiceIf.mSubjectList();
        List<ClassDTO> hSubjectList = mainServiceIf.hSubjectList();

        model.addAttribute("pageResponseDTO", pageResponseDTO);
        model.addAttribute("pageName", "quiz");
        model.addAttribute("order", order);
        model.addAttribute("search_word", search_word);
        model.addAttribute("likeList", likeList);
        model.addAttribute("school", school);
        model.addAttribute("grade", grade);
        model.addAttribute("semester", semester);
        model.addAttribute("subject", subject);
        model.addAttribute("chapter", chapter);
        model.addAttribute("mediumChapter", mediumChapter);
        model.addAttribute("chaxi", chaxi);
        model.addAttribute("subjectList", subjectList);
        model.addAttribute("mSubjectList", mSubjectList);
        model.addAttribute("hSubjectList", hSubjectList);
        model.addAttribute("page", page);
        return "main2";
    }

    @ResponseBody
    @PostMapping("/insertLike")
    public int insertLike(@RequestParam(value="quizIdx") int quizIdx,
                           HttpSession session) {
        int memberIdx = session.getAttribute( "memberIdx" ) != null ? (int) session.getAttribute( "memberIdx" ) : 0;
        List<Integer> likeList = mainServiceIf.likeList(memberIdx);
        QuizDTO quizDTO = quizServiceIf.view(quizIdx);
        int likeCnt = quizDTO.getLikeCnt();

        if (likeList.contains(quizIdx)) {
            mainServiceIf.deleteLike(quizIdx, memberIdx);
            likeCnt--;
        }
        else {
            mainServiceIf.insertLike(quizIdx, memberIdx);
            likeCnt++;
        }
        quizServiceIf.updateLikeCnt(quizIdx, likeCnt);
        return likeCnt;
    }

    @GetMapping("/fetchSubjects")
    public ResponseEntity<List<ClassDTO>> fetchSubjects(@RequestParam String grade, @RequestParam String semester) {
        try {
            List<ClassDTO> subjects = mainServiceIf.subjectList(grade, semester);
            return ResponseEntity.ok(subjects);
        } catch (Exception e) {
            log.error("Error fetching subjects for grade {} and semester {}: ", grade, semester, e);
            return ResponseEntity.status(500).body(null);
        }
    }

    @GetMapping("/fetchLargeChapters")
    public ResponseEntity<List<LargeChapterDTO>> fetchLargeChapters(@RequestParam int classIdx) {
        try {
            List<LargeChapterDTO> largeChapters = mainServiceIf.largeChapterList(classIdx);
            return ResponseEntity.ok(largeChapters);
        } catch (Exception e) {
            log.error("Error fetching large chapters for classIdx {}: ", classIdx, e);
            return ResponseEntity.status(500).body(null);
        }
    }

    @GetMapping("/fetchMediumChapters")
    public ResponseEntity<List<MediumChapterDTO>> fetchMediumChapters(@RequestParam int largeIdx) {
        try {
            List<MediumChapterDTO> mediumChapters = mainServiceIf.mediumChapterList(largeIdx);
            return ResponseEntity.ok(mediumChapters);
        } catch (Exception e) {
            log.error("Error fetching medium chapters for largeIdx {}: ", largeIdx, e);
            return ResponseEntity.status(500).body(null);
        }
    }

    @GetMapping("/fetchSmallChapters")
    public ResponseEntity<List<SmallChapterDTO>> fetchSmallChapters(@RequestParam int mediumIdx) {
        try {
            List<SmallChapterDTO> smallChapters = mainServiceIf.smallChapterList(mediumIdx);
            return ResponseEntity.ok(smallChapters);
        } catch (Exception e) {
            log.error("Error fetching small chapters for mediumIdx {}: ", mediumIdx, e);
            return ResponseEntity.status(500).body(null);
        }
    }

    @GetMapping("/fetchMiddleSubjects")
    public ResponseEntity<List<ClassDTO>> fetchMiddleSubjects() {
        try {
            List<ClassDTO> subjects = mainServiceIf.mSubjectList();
            return ResponseEntity.ok(subjects);
        } catch (Exception e) {
            log.error("Error fetching middle school subjects: ", e);
            return ResponseEntity.status(500).body(null);
        }
    }

    @GetMapping("/fetchHighSubjects")
    public ResponseEntity<List<ClassDTO>> fetchHighSubjects() {
        try {
            List<ClassDTO> subjects = mainServiceIf.hSubjectList();
            return ResponseEntity.ok(subjects);
        } catch (Exception e) {
            log.error("Error fetching high school subjects: ", e);
            return ResponseEntity.status(500).body(null);
        }
    }

    @ResponseBody
    @PostMapping("/fetchQuizzes")
    public Map<String, Object> fetchQuizzes(PageRequestDTO pageRequestDTO,
                                            @RequestParam(name="search_word", defaultValue = "") String search_word,
                                            @RequestParam(name="order", defaultValue = "regDate") String order,
                                            @RequestParam(name="school", defaultValue = "") String school,
                                            @RequestParam(name="grade", defaultValue = "") String grade,
                                            @RequestParam(name="semester", defaultValue = "") String semester,
                                            @RequestParam(name="subject", defaultValue = "") String subject,
                                            @RequestParam(name="chapter", defaultValue = "") String chapter,
                                            @RequestParam(name="mediumChapter", defaultValue = "") String mediumChapter,
                                            @RequestParam(name="chaxi", defaultValue = "") String chaxi,
                                            @RequestParam(name="page", defaultValue = "1") int page,
                                            HttpSession session) {
        log.info("----------------------------fetchQuizzes");
        try {
            int memberIdx = session.getAttribute("memberIdx") != null ? (int) session.getAttribute("memberIdx") : 0;
            List<Integer> likeList = mainServiceIf.likeList(memberIdx);
            PageResponseDTO<QuizDTO> quizzes = mainServiceIf.list(pageRequestDTO, order);

            Map<String, Object> response = new HashMap<>();
            response.put("likeList", likeList);
            response.put("quizzes", quizzes);

            return response;
        } catch (Exception e) {
            log.error("Error fetching quizzes: ", e);
            return null;
        }
    }

    @PostMapping("/enterquiz")
    public String enterbranches(QuizRoomDTO quizRoomDTO){
        log.info("enter quizRoom : " + quizRoomDTO);
        QuizRoomDTO resultDTO = quizGameServiceIf.readRoom(quizRoomDTO);
        log.info("resultDTO quizRoom : " + resultDTO);
        if(resultDTO.getType().equals("game")){
            return "redirect:/game/joingame?roomIdx="+resultDTO.getRoomIdx();
        } else if(resultDTO.getType().equals("together")){

            return "redirect:/nameCheck?roomNumber="+resultDTO.getRoomIdx();
        }

        return "redirect:/";
    }

    @GetMapping("/quiz/detail")
    public void detailGET(
            @RequestParam(name = "quizIdx") int quizIdx,
            HttpServletRequest request,
            HttpSession session,
            Model model) {

        QuizDTO quizDTO = quizServiceIf.view(quizIdx);
        quizDTO.setGameIng(quizServiceIf.countGameStatus(quizIdx, "s"));
        quizDTO.setGameEnd(quizServiceIf.countGameStatus(quizIdx, "e"));

        List<QuizDetailDTO> dtoList = quizGameServiceIf.readQuizDetailList(quizDTO);

        String referer = request.getHeader("Referer");

        int memberIdx = session.getAttribute("memberIdx") != null ? (int) session.getAttribute("memberIdx") : 0;
        List<Integer> likeList = mainServiceIf.likeList(memberIdx);
        String name = "";
        if (session.getAttribute("name") != null) {
            name = (String) session.getAttribute("name");
        }
        model.addAttribute("referer", referer);
        model.addAttribute("likeList", likeList);
        model.addAttribute("pageName", "quiz");
        model.addAttribute("quizDTO", quizDTO);
        model.addAttribute("dtoList", dtoList);
        model.addAttribute("name", name);
    }
}
