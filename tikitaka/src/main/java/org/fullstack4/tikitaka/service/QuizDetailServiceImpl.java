package org.fullstack4.tikitaka.service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.fullstack4.tikitaka.domain.*;
import org.fullstack4.tikitaka.dto.QuizDetailDTO;
import org.fullstack4.tikitaka.repository.*;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Log4j2
@Service
//@RequiredArgsConstructor
public class QuizDetailServiceImpl implements QuizDetailServiceIf{
    private final ModelMapper modelMapper;
    private final QuizDetailRepository quizDetailRepository;
    private final DetailBlankRepository detailBlankRepository;
    private final DetailDescRepository detailDescRepository;
    private final DetailMulRepository detailMulRepository;
    private final DetailOxRepository detailOxRepository;

    @PersistenceContext
    private final EntityManager entityManager;

    @Autowired
     public QuizDetailServiceImpl(ModelMapper modelMapper, QuizDetailRepository quizDetailRepository,
                                  DetailBlankRepository detailBlankRepository,DetailDescRepository detailDescRepository,
                                  DetailMulRepository detailMulRepository,DetailOxRepository detailOxRepository,
                                  EntityManager entityManager) {
        this.modelMapper = modelMapper;
        this.quizDetailRepository = quizDetailRepository;
        this.detailBlankRepository = detailBlankRepository;
        this.detailDescRepository = detailDescRepository;
        this.detailMulRepository = detailMulRepository;
        this.detailOxRepository = detailOxRepository;
        this.entityManager = entityManager;
    }


    @Override
    public List<QuizDetailDTO> findAllByQuizIdx(int quizIdx) {
        List<QuizDetailEntity> list = quizDetailRepository.findAllByQuizIdx(quizIdx);

        List<QuizDetailDTO> dtoList = list.stream().map(vo->modelMapper.map(vo, QuizDetailDTO.class))
                        .collect(Collectors.toList());
        return dtoList;
    }

    @Override
    public int registDetail(QuizDetailDTO quizDetailDTO) {
        switch (quizDetailDTO.getCategory()) {
            case "Multiple":
                quizDetailDTO.setCategory("mul");
                if (quizDetailDTO.getMulImg() == null) {
                    quizDetailDTO.setMulImg("|");
                } else {
                    quizDetailDTO.setMulImg(quizDetailDTO.getMulImg());
                }
                break;
            case "Description":
                quizDetailDTO.setCategory("desc");
                break;
            case "Short":
                quizDetailDTO.setCategory("short");
                break;
            case "Blank":
                quizDetailDTO.setCategory("blank");
                break;
            default:
                quizDetailDTO.setCategory("ox");
                break;
        }
        log.info("quizDTO : {}", quizDetailDTO);
        QuizDetailEntity quizDetailEntity = modelMapper.map(quizDetailDTO, QuizDetailEntity.class);
        quizDetailRepository.save(quizDetailEntity);
        quizDetailDTO.setDetailIdx(quizDetailEntity.getDetailIdx());
        log.info("Saved detailIdx: {}", quizDetailDTO.getDetailIdx());
        switch (quizDetailDTO.getCategory()) {
            case "mul":
                DetailMulEntity detailMulEntity = modelMapper.map(quizDetailDTO, DetailMulEntity.class);
                detailMulRepository.save(detailMulEntity);
                break;
            case "desc":
                DetailDescEntity detailDescEntity = modelMapper.map(quizDetailDTO, DetailDescEntity.class);
                detailDescRepository.save(detailDescEntity);
                break;
            case "blank":
                DetailBlankEntity detailBlankEntity = modelMapper.map(quizDetailDTO, DetailBlankEntity.class);
                detailBlankRepository.save(detailBlankEntity);
                break;
            default:
                DetailOxEntity detailOxEntity = modelMapper.map(quizDetailDTO, DetailOxEntity.class);
                detailOxRepository.save(detailOxEntity);
                break;
        }
        return quizDetailEntity.getDetailIdx();
    }


    @Override
    public void saveDetailData(QuizDetailDTO quizDetailDTO) {
        if(quizDetailDTO.getCategory().equals("OX")) {

        }
    }

    @Override
    public QuizDetailDTO detailQuizNum(int quizDetailIdx) {
        QuizDetailEntity list = quizDetailRepository.findByDetailIdx(quizDetailIdx);
        QuizDetailDTO quizDetailDTO = modelMapper.map(list, QuizDetailDTO.class);
//        return qDto;

//        QuizDetailEntity quizDetailEntity = quizDetailRepository.findByQuizIdx(quizIdx);
//        QuizDetailDTO quizDetailDTO = modelMapper.map(quizDetailEntity, QuizDetailDTO.class);

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
        return quizDetailDTO;
    }

    @Override
    public void deleteDetailQuiz(int detailIdx) {
        quizDetailRepository.deleteByDetailIdx(detailIdx);
        detailBlankRepository.deleteByDetailIdx(detailIdx);
        detailOxRepository.deleteByDetailIdx(detailIdx);
        detailMulRepository.deleteByDetailIdx(detailIdx);
        detailDescRepository.deleteByDetailIdx(detailIdx);
    }
}
