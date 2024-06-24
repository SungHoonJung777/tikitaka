package org.fullstack4.tikitaka.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.fullstack4.tikitaka.dto.QuizDTO;
import org.fullstack4.tikitaka.dto.QuizDetailDTO;
import org.fullstack4.tikitaka.service.QuizDetailServiceIf;
import org.fullstack4.tikitaka.service.QuizServiceIf;
import org.fullstack4.tikitaka.utils.FileUploadUtil;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.Optional;

@Log4j2
@Controller
@RequiredArgsConstructor
@RequestMapping("/quiz")
public class QuizController {
    private final QuizDetailServiceIf quizDetailServiceif;

    private final QuizServiceIf quizService;
    private final FileUploadUtil fileUploadUtil;
    @GetMapping("/maker")
    public void quizRegiGet(Model model) {


    }
    @PostMapping("/maker")
    public String quizRegiPost(QuizDTO quizDTO,
                               @RequestParam(name="thumbnail-img", defaultValue = "") MultipartFile thumbnailFile,
                               @RequestParam(name="readFile", defaultValue = "") MultipartFile file,
                               HttpServletResponse resp, HttpServletRequest req, Model model,
                               RedirectAttributes redirectAttributes
                               ) throws IOException {
        HttpSession session = req.getSession();
        session.setAttribute("quizDTO", quizDTO);
        log.info(quizDTO);

        // 첨부파일 저장
        String orgfile = file.getOriginalFilename();
        String savefile = fileUploadUtil.upload(file, "test");
        quizDTO.setOrgfile(orgfile);
        quizDTO.setSavefile(savefile);

        //썸네일 저장
        String thumbnail = fileUploadUtil.upload(thumbnailFile, "test");
        String thumbnailUrl = fileUploadUtil.getFileUrl(thumbnail, "test");
        quizDTO.setThumbnail(thumbnailUrl);

        int idx = quizService.regist(quizDTO);
        log.info("quizIdx :" + idx);
        redirectAttributes.addAttribute("quizIdx", idx);
        model.addAttribute("quizinfo", quizDTO);
        return "redirect:/quiz/makerdetail";
    }
    @GetMapping("/makerdetail")
    public void makerdetailGet(Model model, HttpServletRequest req, @RequestParam("quizIdx") int quizIdx) {
        HttpSession session = req.getSession();
        QuizDTO quizDTO = (QuizDTO) session.getAttribute("quizDTO");
        log.info("quizDTO : " + quizDTO);
        log.info("quizIdx" + quizIdx);
        model.addAttribute("quizDTO", quizDTO);
        model.addAttribute("pageName", "storage");
        model.addAttribute("pageName", "report");
    }
    @PostMapping("/makerdetail")
    public void makerDetailPost() {

    }
    @ResponseBody
    @PostMapping ("/save")
    public int saveDetail (QuizDetailDTO quizDDTO, @RequestParam("quizIdx") int quizIdx,
                           @RequestParam(name = "questionMedia", required = false) MultipartFile file
    ) throws IOException {
        quizDDTO.setQuizIdx(quizIdx);
        if (file!= null){
        String media = fileUploadUtil.upload(file, "test");
        String mediaUrl = fileUploadUtil.getFileUrl(media, "test");
        quizDDTO.setMedia(mediaUrl);
        }
        int result = quizDetailServiceif.registDetail(quizDDTO);
        log.info("result : " + result);
        return result;
    }

    @ResponseBody
    @PostMapping ("/viewq")
    public QuizDetailDTO viewquiz (@RequestParam(name = "detailIdx") int detailIdx) {
        QuizDetailDTO quizDetailDTO = quizDetailServiceif.detailQuizNum(detailIdx);
        log.info("detail보기 :" + quizDetailDTO);
        return quizDetailDTO;
    }
    @PostMapping("/savetemp")
    public String  saveTemp (@RequestParam(name = "quizIdx") int quizIdx){
         int result = quizService.tmpSaveQuizDetail(quizIdx);
        log.info("result : " + result);
        return "redirect:/quiz/makerdetail";
    }
    @GetMapping("/saveQuiz")
    public String  saveQuiz (@RequestParam(name = "quizIdx") int quizIdx){
        int result = quizService.SaveQuiz(quizIdx);
        log.info("result : " + result);
        return "redirect:/main2";
    }






}
