package org.fullstack4.tikitaka.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.fullstack4.tikitaka.dto.PageRequestDTO;
import org.fullstack4.tikitaka.dto.PageResponseDTO;
import org.fullstack4.tikitaka.dto.QuizDTO;
import org.fullstack4.tikitaka.dto.QuizDetailDTO;
import org.fullstack4.tikitaka.service.MainServiceIf;
import org.fullstack4.tikitaka.service.QuizDetailServiceIf;
import org.fullstack4.tikitaka.service.QuizGameServiceIf;
import org.fullstack4.tikitaka.service.QuizServiceIf;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.request.SessionScope;

import java.time.LocalDate;
import java.util.List;

@Log4j2
@RequiredArgsConstructor
@Controller
@RequestMapping("/storage")
public class StorageController {
    private final QuizServiceIf quizService;
    private final QuizDetailServiceIf quizDetailService;
    private final QuizGameServiceIf quizGameService;
    private final MainServiceIf mainServiceIf;

    @GetMapping("/list")
    public void listGET(PageRequestDTO pageRequestDTO,
                        @RequestParam(name="search_word", defaultValue = "") String search_word,
                        @RequestParam(name="order", defaultValue = "regDate") String order,
                        Model model,
                        HttpSession session
    ) {
        Integer memberIdx = 0;
        if (session.getAttribute("id") != null) {
            memberIdx = (Integer) session.getAttribute("memberIdx");
        }
        String name = "";
        if (session.getAttribute("name") != null) {
            name = (String) session.getAttribute("name");
        }
        PageResponseDTO<QuizDTO> pageResponseDTO = quizService.pagelikelist(pageRequestDTO, order, memberIdx);
        pageResponseDTO.getDtoList().forEach(dto -> {
            dto.setGameIng(quizService.countGameStatus(dto.getQuizIdx(), "s"));
            dto.setGameEnd(quizService.countGameStatus(dto.getQuizIdx(), "e"));
        });

        model.addAttribute("pageName", "storage");
        model.addAttribute("pageResponseDTO", pageResponseDTO);
        model.addAttribute("search_word", search_word);
        model.addAttribute("order", order);
        model.addAttribute("name", name);
    }

    @GetMapping("/detail")
    public void detailGET(
            @RequestParam(name = "quizIdx") int quizIdx,
                          HttpServletRequest request,
                          HttpSession session,
                          Model model) {

        QuizDTO quizDTO = quizService.view(quizIdx);
        quizDTO.setGameIng(quizService.countGameStatus(quizIdx, "s"));
        quizDTO.setGameEnd(quizService.countGameStatus(quizIdx, "e"));
        List<QuizDetailDTO> dtoList = quizGameService.readQuizDetailList(quizDTO);

        String referer = request.getHeader("Referer");

        int memberIdx = session.getAttribute("memberIdx") != null ? (int) session.getAttribute("memberIdx") : 0;
        List<Integer> likeList = mainServiceIf.likeList(memberIdx);
        String name = "";
        if (session.getAttribute("name") != null) {
            name = (String) session.getAttribute("name");
        }
        model.addAttribute("referer", referer);
        model.addAttribute("likeList", likeList);
        model.addAttribute("pageName", "storage");
        model.addAttribute("quizDTO", quizDTO);
        model.addAttribute("dtoList", dtoList);
        model.addAttribute("name", name);
    }

    @GetMapping("/mydata")
    public void mydataGET(
            PageRequestDTO pageRequestDTO,
            @RequestParam(name="search_word", defaultValue = "") String search_word,
            @RequestParam(name="order", defaultValue = "regDate") String order,
                      Model model,
                      HttpSession session
    ) {
        Integer memberIdx = 0;
        if (session.getAttribute("id") != null) {
            memberIdx = (Integer) session.getAttribute("memberIdx");
        }
        String name = "";
        if (session.getAttribute("name") != null) {
            name = (String) session.getAttribute("name");
        }
        PageResponseDTO<QuizDTO> pageResponseDTO = quizService.pagelist(pageRequestDTO, order, memberIdx);
        pageResponseDTO.getDtoList().forEach(dto -> {
            dto.setGameIng(quizService.countGameStatus(dto.getQuizIdx(), "s"));
            dto.setGameEnd(quizService.countGameStatus(dto.getQuizIdx(), "e"));
        });

        model.addAttribute("pageName", "storage");
        model.addAttribute("pageResponseDTO", pageResponseDTO);
        model.addAttribute("search_word", search_word);
        model.addAttribute("order", order);
        model.addAttribute("name", name);
    }

    @PostMapping("/delete")
    public String deletePOST(@RequestParam(name="quizIdx") List<Integer> quizIdxList) {
        log.info("delete--------------------------------------");
        quizIdxList.forEach(quizIdx -> quizService.delete(quizIdx));
        return "redirect:/storage/mydata";
    }
}
