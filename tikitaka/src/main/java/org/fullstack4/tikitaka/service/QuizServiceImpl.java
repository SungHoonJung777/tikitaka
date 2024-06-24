package org.fullstack4.tikitaka.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.fullstack4.tikitaka.domain.DetailOxEntity;
import org.fullstack4.tikitaka.domain.QuizDetailEntity;
import org.fullstack4.tikitaka.domain.QuizEntity;
import org.fullstack4.tikitaka.dto.PageRequestDTO;
import org.fullstack4.tikitaka.dto.PageResponseDTO;
import org.fullstack4.tikitaka.dto.QuizDTO;
import org.fullstack4.tikitaka.dto.QuizDetailDTO;
import org.fullstack4.tikitaka.repository.*;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Log4j2
@Service
@RequiredArgsConstructor
public class QuizServiceImpl implements QuizServiceIf{
    private final ModelMapper modelMapper;
    private final QuizRepository quizRepository;
    private final QuizRoomRepository quizRoomRepository;
    private final QuizDetailRepository quizDetailRepository;
    private final DetailBlankRepository detailBlankRepository;
    private final DetailOxRepository detailOxRepository;
    private final DetailMulRepository detailMulRepository;
    private final DetailDescRepository detailDescRepository;
    private final LikeRepository likeRepository;

    @Override
    public int regist(QuizDTO quizDTO) {
        QuizEntity quizEntity = modelMapper.map(quizDTO, QuizEntity.class);
        int idx = quizRepository.save(quizEntity).getQuizIdx();
        return idx;
    }

    @Override
    public int modify(QuizDTO quizDTO) {
        return 0;
    }

    @Transactional
    @Override
    public void delete(int quizIdx) {
        List<QuizDetailEntity> list = quizDetailRepository.findAllByQuizIdx(quizIdx);
        list.forEach(quizDetail -> {
            int detailIdx = quizDetail.getDetailIdx();
            detailBlankRepository.deleteByDetailIdx(detailIdx);
            detailOxRepository.deleteByDetailIdx(detailIdx);
            detailMulRepository.deleteByDetailIdx(detailIdx);
            detailDescRepository.deleteByDetailIdx(detailIdx);
        });
        quizDetailRepository.deleteByQuizIdx(quizIdx);
        likeRepository.deleteByQuizIdx(quizIdx);
        quizRepository.deleteById(quizIdx);
    }

    @Override
    public QuizDTO view(Integer quizIdx) {
        QuizEntity quizEntity = quizRepository.findByQuizIdx(quizIdx)
                .orElseThrow(() -> new EntityNotFoundException("Quiz not found for id: " + quizIdx));
        return modelMapper.map(quizEntity, QuizDTO.class);
    }

    @Override
    public PageResponseDTO<QuizDTO> pagelist(PageRequestDTO pageRequestDTO, String order, Integer member_idx) {
        pageRequestDTO.setPage_size(10);
        PageRequest pageable = pageRequestDTO.getPageable(order);
        String search_word = pageRequestDTO.getSearch_word();

        Page<QuizEntity> list = quizRepository.search(pageable, search_word, member_idx);

        List<QuizDTO> dtoList = list.getContent().stream()
                .map(vo->modelMapper.map(vo, QuizDTO.class))
                .collect(Collectors.toList());

        return PageResponseDTO.<QuizDTO>withAll()
                .pageRequestDTO(pageRequestDTO)
                .dtoList(dtoList)
                .total_count((int)list.getTotalElements())
                .build();
    }

    @Override
    public PageResponseDTO<QuizDTO> pagelikelist(PageRequestDTO pageRequestDTO, String order, Integer member_idx) {
        pageRequestDTO.setPage_size(10);
        PageRequest pageable = pageRequestDTO.getPageable(order);
        String search_word = pageRequestDTO.getSearch_word();

        Page<QuizEntity> list = quizRepository.searchlike(pageable, search_word, member_idx);

        List<QuizDTO> dtoList = list.getContent().stream()
                .map(vo->modelMapper.map(vo, QuizDTO.class))
                .collect(Collectors.toList());

        return PageResponseDTO.<QuizDTO>withAll()
                .pageRequestDTO(pageRequestDTO)
                .dtoList(dtoList)
                .total_count((int)list.getTotalElements())
                .build();
    }

    @Override
    public int countGameStatus(Integer quizIdx, String status) {
        int result = quizRoomRepository.countByQuizIdxAndTypeAndStatus(quizIdx, "game", status);
        return result;
    }
    @Override
    public void updateLikeCnt(int quizIdx, int likeCnt) {
        quizRepository.updateLikeCntByQuizIdx(quizIdx, likeCnt);
    }

    @Transactional
    @Override
    public int tmpSaveQuizDetail(int quizIdx) {
        int result = quizRepository.updateTmpStatus(quizIdx);
        return result;
    }

    @Override
    public int SaveQuiz(int quizIdx) {
        int result = quizRepository.saveTmpStatus(quizIdx);
        return result;
    }


}
