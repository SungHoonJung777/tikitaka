package org.fullstack4.tikitaka.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.fullstack4.tikitaka.dto.*;
import org.fullstack4.tikitaka.service.KafkaProducer;
import org.fullstack4.tikitaka.service.QuizGameServiceIf;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@Log4j2
@AllArgsConstructor
@RequestMapping(value="/game")
public class QuizGameController {
    private final KafkaProducer producer;
    private final QuizGameServiceIf quizGameServiceIf;
    private final ObjectMapper objectMapper;

    @MessageMapping("userCount")
    public void sendSocketMessage(QuizMemberDTO quizMemberDTO) {
        log.info("quizMemberDTO controller : "+ quizMemberDTO);
        producer.sendMessage(quizMemberDTO);
    }

    @GetMapping("/gamesetting")
    public void createsetting(QuizRoomDTO quizRoomDTO, Model model){
        //http://localhost:8080/game/gamesetting?quizIdx=1
        log.info("get createsetting quizRoomDTO : " + quizRoomDTO);
        model.addAttribute("quizRoomDTO",quizRoomDTO);
    }
    @GetMapping("/create")
    public void createroom(QuizRoomDTO quizRoomDTO, Model model){
        log.info("get quizRoomDTO : " + quizRoomDTO);
        quizGameServiceIf.readRoom(quizRoomDTO);
        model.addAttribute("quizRoomDTO",quizGameServiceIf.readRoom(quizRoomDTO));
    }
    @PostMapping("/create")
    public String createroomPost(QuizRoomDTO quizRoomDTO){
        log.info("quizRoomDTO : " + quizRoomDTO);
        String roomIdx = String.valueOf(quizGameServiceIf.createRoom(quizRoomDTO).getRoomIdx());
        return "redirect:/game/create?roomIdx="+roomIdx;
    }
    @GetMapping("/joingame")
    public void joinroom(QuizRoomDTO quizRoomDTO,Model model){
        QuizRoomDTO resultRoomDTO = quizGameServiceIf.readRoom(quizRoomDTO);
        QuizDTO quizDTO = QuizDTO.builder().quizIdx(resultRoomDTO.getQuizIdx()).build();
        QuizDTO resultQuizDTO = quizGameServiceIf.readQuiz(quizDTO);
        List<QuizDetailDTO> resultQuizDetailDTO = quizGameServiceIf.readQuizDetailList(resultQuizDTO);
        try {
            model.addAttribute("quizRoomDTO",objectMapper.writeValueAsString(resultRoomDTO));
            model.addAttribute("quizDTO",objectMapper.writeValueAsString(resultQuizDTO));
            model.addAttribute("QuizDetailDTOList",objectMapper.writeValueAsString(resultQuizDetailDTO));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @ResponseBody
    @PostMapping("/joinmember")
    public String joinmember(QuizMemberDTO quizMemberDTO) throws JsonProcessingException {
        log.info("joinmember : " + quizMemberDTO);
        QuizMemberDTO resultDTO = quizGameServiceIf.saveQuizMember(quizMemberDTO);
        producer.sendMessage(quizMemberDTO);
        log.info("joinmember resultDTO : " +resultDTO);
        return objectMapper.writeValueAsString(resultDTO);
    }

    @ResponseBody
    @PostMapping("/scoreReport")
    public String scoreReport(QuizReportDTO quizReportDTO, QuizMemberDTO quizMemberDTO) throws JsonProcessingException {
        log.info("scoreReport  QuizReportDTO : " + quizReportDTO);
        log.info("scoreReport  quizMemberDTO : " + quizMemberDTO);
        quizGameServiceIf.saveReport(quizReportDTO);
        QuizMemberDTO resultDTO = quizGameServiceIf.saveQuizMember(quizMemberDTO);
        producer.sendMessage(quizMemberDTO);
        log.info("scoreReport resultDTO : " +resultDTO);
        return objectMapper.writeValueAsString(resultDTO);
    }

    @ResponseBody
    @PostMapping("/start")
    public QuizMemberDTO start(QuizMemberDTO quizMemberDTO, QuizRoomDTO quizRoomDTO){
        log.info("start : " + quizMemberDTO);
        quizGameServiceIf.modifyroom(quizRoomDTO);
        producer.sendMessage(quizMemberDTO);
        return quizMemberDTO;
    }

    @ResponseBody
    @PostMapping("/end")
    public QuizMemberDTO end(QuizMemberDTO quizMemberDTO,QuizRoomDTO quizRoomDTO){
        log.info("end : " + quizMemberDTO);
        quizGameServiceIf.modifyroom(quizRoomDTO);
        producer.sendMessage(quizMemberDTO);
        return quizMemberDTO;
    }

    @ResponseBody
    @PostMapping("/showranking")
    public List<QuizMemberDTO> showranking(QuizRoomDTO quizRoomDTO){
        List<QuizMemberDTO> quizMemberDTOList= quizGameServiceIf.readRanking(quizRoomDTO);
        log.info("quizMemberDTOList : " + quizMemberDTOList);

        return quizMemberDTOList;
    }
}
