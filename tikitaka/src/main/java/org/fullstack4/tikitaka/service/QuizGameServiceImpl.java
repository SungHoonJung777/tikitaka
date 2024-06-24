package org.fullstack4.tikitaka.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.fullstack4.tikitaka.domain.*;
import org.fullstack4.tikitaka.dto.*;
import org.fullstack4.tikitaka.repository.*;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Log4j2
@RequiredArgsConstructor
@Service
public class QuizGameServiceImpl implements QuizGameServiceIf {
    private final QuizRepository quizRepository;
    private final QuizDetailRepository quizDetailRepository;
    private final QuizReportRepository quizReportRepository;
    private final QuizRoomRepository quizRoomRepository;
    private final QuizMemberRepository quizMemberRepository;
    private final DetailOxRepository detailOxRepository;
    private final DetailBlankRepository detailBlankRepository;
    private final DetailDescRepository detailDescRepository;
    private final DetailMulRepository detailMulRepository;
    private final ModelMapper modelMapper;

    @Override
    public QuizRoomDTO createRoom(QuizRoomDTO quizRoomDTO) {
        QuizRoomEntity quizRoomEntity = modelMapper.map(quizRoomDTO,QuizRoomEntity.class);
        int roomIdx = quizRoomRepository.save(quizRoomEntity).getRoomIdx();
        quizRoomDTO.setRoomIdx(roomIdx);
        log.info("result quizRoomDTO : " + quizRoomDTO );
        return quizRoomDTO;
    }

    @Override
    public QuizReportDTO saveReport(QuizReportDTO quizReportDTO) {
        QuizReportEntity quizReportEntity = modelMapper.map(quizReportDTO,QuizReportEntity.class);
        quizReportRepository.save(quizReportEntity);
        return null;
    }

    @Override
    public QuizMemberDTO saveQuizMember(QuizMemberDTO quizMemberDTO) {
        QuizMemberEntity resultEntity = quizMemberRepository.findByStudentNameAndStudentPasswordAndRoomIdx(quizMemberDTO.getStudentName(),quizMemberDTO.getStudentPassword(),quizMemberDTO.getRoomIdx());
        log.info("resultEntity : " + resultEntity);
        QuizMemberDTO resultDTO;
        if(resultEntity !=null ){
            if(quizMemberDTO.getStudentAnswerCount()!=0) {
                resultEntity.modifyScore(quizMemberDTO.getStudentCorrectCount(), quizMemberDTO.getStudentAnswerCount(), quizMemberDTO.getStudentTotalScore());
            }
            log.info("resultEntity modify : " + resultEntity);
            quizMemberRepository.save(resultEntity);
            resultDTO = modelMapper.map(resultEntity,QuizMemberDTO.class);
        }
        else {
            QuizMemberEntity quizMemberEntity = modelMapper.map(quizMemberDTO, QuizMemberEntity.class);
            resultDTO=quizMemberDTO;
            resultDTO.setQuizMemberIdx(quizMemberRepository.save(quizMemberEntity).getQuizMemberIdx());
            log.info("QuizMember resultEntity X : "+resultDTO);
        }
        return resultDTO;
    }

    @Override
    public QuizDTO readQuiz(QuizDTO quizDTO) {
        Optional<QuizEntity> result = quizRepository.findById(quizDTO.getQuizIdx());
        QuizEntity quizEntity = result.orElse(null);
        if(quizEntity!=null){
            QuizDTO resultDTO = modelMapper.map(quizEntity,QuizDTO.class);
            log.info("quiz resultDTO : "+resultDTO);
            return resultDTO;
        }
        return null;
    }

    @Override
    public List<QuizDetailDTO> readQuizDetailList(QuizDTO quizDTO) {
        List<QuizDetailDTO> quizDetailDTOList = quizDetailRepository.findAllByQuizIdx(quizDTO.getQuizIdx())
                .stream().map(entity->modelMapper.map(entity,QuizDetailDTO.class)).collect(Collectors.toList());
        for(QuizDetailDTO quizDetailDTO : quizDetailDTOList){
            if(quizDetailDTO.getCategory().equals("ox")){
                DetailOxEntity detailOxEntity = detailOxRepository.findByDetailIdx(quizDetailDTO.getDetailIdx());
                quizDetailDTO.setOxIdx(detailOxEntity.getOxIdx());
                quizDetailDTO.setOcomment(detailOxEntity.getOcomment());
                quizDetailDTO.setXcomment(detailOxEntity.getXcomment());
            }
            else if(quizDetailDTO.getCategory().equals("blank")){
                DetailBlankEntity detailBlankEntity =  detailBlankRepository.findByDetailIdx(quizDetailDTO.getDetailIdx());
                quizDetailDTO.setBlankIdx(detailBlankEntity.getBlankIdx());
                quizDetailDTO.setBlank(detailBlankEntity.getBlank());
            }
            else if(quizDetailDTO.getCategory().equals("desc")){
                DetailDescEntity detailDescEntity = detailDescRepository.findByDetailIdx(quizDetailDTO.getDetailIdx());
                quizDetailDTO.setDescIdx(detailDescEntity.getDescIdx());
                quizDetailDTO.setDescMin(detailDescEntity.getDescMin());
            }
            else if(quizDetailDTO.getCategory().equals("mul")){
                DetailMulEntity detailMulEntity = detailMulRepository.findByDetailIdx(quizDetailDTO.getDetailIdx());
                quizDetailDTO.setMulIdx(detailMulEntity.getMulIdx());
                quizDetailDTO.setMulImg(detailMulEntity.getMulImg());
                quizDetailDTO.setMulTitle(detailMulEntity.getMulTitle());
            }
            else{}
        }
        return quizDetailDTOList;
    }

    @Override
    public QuizRoomDTO readRoom(QuizRoomDTO quizRoomDTO) {
        Optional<QuizRoomEntity> result = quizRoomRepository.findById(quizRoomDTO.getRoomIdx());
        QuizRoomEntity quizRoomEntity = result.orElse(null);
        if(quizRoomEntity!=null){
            QuizRoomDTO resultDTO = modelMapper.map(quizRoomEntity,QuizRoomDTO.class);
            log.info("quiz resultDTO : "+resultDTO);
            return resultDTO;
        }
        return null;
    }

    @Override
    public List<QuizMemberDTO> readRanking(QuizRoomDTO quizRoomDTO) {
        List<QuizMemberDTO> quizMemberDTOList = quizMemberRepository.findByRoomIdxOrderByStudentTotalScoreDesc(quizRoomDTO.getRoomIdx()).stream()
                .map(entity->modelMapper.map(entity,QuizMemberDTO.class)).collect(Collectors.toList());;
        return quizMemberDTOList;
    }

    @Override
    public void modifyroom(QuizRoomDTO quizRoomDTO) {
        QuizRoomEntity resultEntity = quizRoomRepository.findByRoomIdx(quizRoomDTO.getRoomIdx());
        log.info("resultQuizRoomEntity : " + resultEntity);
        if(resultEntity !=null ){
            resultEntity.modifystatus(quizRoomDTO.getStatus());
            log.info("resultQuizRoom modify : " + resultEntity);
            quizRoomRepository.save(resultEntity);
        }
    }
}
