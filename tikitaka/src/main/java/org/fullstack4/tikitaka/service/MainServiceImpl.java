package org.fullstack4.tikitaka.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.fullstack4.tikitaka.domain.*;
import org.fullstack4.tikitaka.dto.*;
import org.fullstack4.tikitaka.repository.*;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Log4j2
@Service
@RequiredArgsConstructor
public class MainServiceImpl implements MainServiceIf {

    private final MainRepository mainRepository;
    private final LikeRepository likeRepository;
    private final ClassRepository classRepository;
    private final LargeChapterRepository largeChapterRepository;
    private final MediumChapterRepository mediumChapterRepository;
    private final ModelMapper modelMapper;
    private final SmallChapterRepository smallChapterRepository;

    @Override
    public List<QuizDTO> mainListByLike() {
        List<QuizEntity> list = mainRepository.findTop4ByShareAndStatusOrderByLikeCntDesc("Y", "S");

        List<QuizDTO> dtoList = list.stream()
                .map(vo -> modelMapper.map(vo, QuizDTO.class))
                .collect(Collectors.toList());

        return dtoList;
    }

    @Override
    public List<QuizDTO> mainListByRegDate() {
        List<QuizEntity> list = mainRepository.findTop4ByShareAndStatusOrderByRegDateDesc("Y", "S");

        List<QuizDTO> dtoList = list.stream()
                .map(vo -> modelMapper.map(vo, QuizDTO.class))
                .collect(Collectors.toList());

        return dtoList;
    }

    @Override
    public PageResponseDTO<QuizDTO> list(PageRequestDTO pageRequestDTO, String order) {
        PageRequest pageable = pageRequestDTO.getPageable(order);
        String search_word = pageRequestDTO.getSearch_word();
        String school = pageRequestDTO.getSchool();
        String grade = pageRequestDTO.getGrade();
        String semester = pageRequestDTO.getSemester();
        String subject = pageRequestDTO.getSubject();
        String chapter = pageRequestDTO.getChapter();
        String mediumChapter = pageRequestDTO.getMediumChapter();
        String chaxi = pageRequestDTO.getChaxi();

//        Page<QuizEntity> list = mainRepository.findAll(pageable);

        Page<QuizEntity> list = mainRepository.search(pageable, search_word, school, grade, semester, subject, chapter, mediumChapter, chaxi, "Y", "S");

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
    public void insertLike(int quizIdx, int memberIdx) {
        likeRepository.insertLike(quizIdx, memberIdx);
    }

    @Override
    public void deleteLike(int quizIdx, int memberIdx) {
        likeRepository.deleteLike(quizIdx, memberIdx);
    }

    @Override
    public List<Integer> likeList(int memberIdx) {
        return likeRepository.getLikeByMemberId(memberIdx);
    }

    //초등
    @Override
    public List<ClassDTO> subjectList(String grade, String semester) {
        List<ClassDTO> list =  classRepository.findBySchoolAndGradeAndSemester("E", grade, semester).stream()
                .map(entity -> modelMapper.map(entity, ClassDTO.class))
                .collect(Collectors.toList());
        return list;
    }

    @Override
    public List<LargeChapterDTO> largeChapterList(int classIdx) {
        List<LargeChapterDTO> list = largeChapterRepository.findByClassIdx(classIdx).stream()
                .map(entity -> modelMapper.map(entity, LargeChapterDTO.class))
                .collect(Collectors.toList());
        return list;
    }

    @Override
    public List<MediumChapterDTO> mediumChapterList(int largeIdx) {
        List<MediumChapterDTO> list = mediumChapterRepository.findByLargeIdx(largeIdx).stream()
                .map(entity -> modelMapper.map(entity, MediumChapterDTO.class))
                .collect(Collectors.toList());
        return list;
    }

    @Override
    public List<SmallChapterDTO> smallChapterList(int mediumIdx) {
        List<SmallChapterDTO> list = smallChapterRepository.findByMediumIdx(mediumIdx).stream()
                .map(entity -> modelMapper.map(entity, SmallChapterDTO.class))
                .collect(Collectors.toList());
        return list;
    }

    //중등
    @Override
    public List<ClassDTO> mSubjectList() {
        List<ClassDTO> list = classRepository.findSubjectBySchool("M").stream()
                .map(entity->modelMapper.map(entity, ClassDTO.class))
                .collect(Collectors.toList());
        return list;
    }

    //고등
    @Override
    public List<ClassDTO> hSubjectList() {
        List<ClassDTO> list = classRepository.findSubjectBySchool("H").stream()
                .map(entity->modelMapper.map(entity, ClassDTO.class))
                .collect(Collectors.toList());
        return list;
    }


}
