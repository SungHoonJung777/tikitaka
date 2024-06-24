package org.fullstack4.tikitaka.service;

import lombok.extern.log4j.Log4j2;
import org.fullstack4.tikitaka.domain.QuizReportEntity;
import org.fullstack4.tikitaka.dto.QuizDTO;
import org.fullstack4.tikitaka.dto.QuizDetailDTO;
import org.fullstack4.tikitaka.dto.QuizReportDTO;
import org.fullstack4.tikitaka.dto.TogetherDetailDTO;
import org.fullstack4.tikitaka.repository.QuizReportRepository;
import org.fullstack4.tikitaka.repository.TogetherRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Log4j2
public class TogetherServiceimpl implements TogetherService{
    @Autowired
    private  ModelMapper modelMapper;
    @Autowired
    private TogetherRepository togetherRepository;
    @Autowired
    private QuizReportRepository quizReportRepository;
    @Override
    public List<TogetherDetailDTO> readTogetherDetailList(int quizIdx) {
        List<TogetherDetailDTO> list = togetherRepository.findAllByQuizIdx(quizIdx).stream().map(entity->modelMapper.map(entity,TogetherDetailDTO.class)).collect(Collectors.toList());

        return list;


    }

    @Override
    public int insertRoom(String code, String roomName, String nickname ,int roomIdx , int memberIdx) {
        int iResult = togetherRepository.insertRoom(code,roomName,nickname , roomIdx , memberIdx);
        return iResult;
    }

    @Override
    public int insertScore(QuizReportDTO quizReportDTO) {
        int iResult = togetherRepository.insertScore(quizReportDTO);
        return iResult;
    }
    @Override
    public int modifyScore(QuizReportDTO quizReportDTO) {
        int iResult = togetherRepository.modifyScore(quizReportDTO);
        return iResult;
    }

    @Override
    public int modifyStatusRoom(int roomNumber) {
        int uResult = togetherRepository.modifyStatusRoom(roomNumber);
        return uResult;
    }

    @Override
    public String getRoomStatus(int roomNumber) {
        String status = togetherRepository.getRoomStatus(roomNumber);
        return status;
    }

    @Override
    public int modifyStatusRoomEnd(int roomNumber) {
        int uResult = togetherRepository.modifyStatusRoomEnd(roomNumber);
        return uResult;
    }

    @Override
    public int getCountY(String question, int roomNumber) {
        int rCount = togetherRepository.getCountY(question,roomNumber);
        return rCount;
    }

    @Override
    public List<QuizReportDTO> resultStudent(String nickname, int roomNumber) {

        List<QuizReportDTO> list = quizReportRepository.resultStudent(nickname,roomNumber).stream().map(entity->modelMapper.map(entity,QuizReportDTO.class)).collect(Collectors.toList());

        return list;
    }

    @Override
    public int getQuizIdx(int roomIdx) {
        int quizIdx = togetherRepository.getQuizIdx(roomIdx);
        return quizIdx;
    }
}
