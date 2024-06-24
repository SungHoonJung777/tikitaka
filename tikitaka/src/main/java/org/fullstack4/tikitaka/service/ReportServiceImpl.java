package org.fullstack4.tikitaka.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.fullstack4.tikitaka.domain.QuizEntity;
import org.fullstack4.tikitaka.domain.QuizMemberEntity;
import org.fullstack4.tikitaka.domain.QuizReportEntity;
import org.fullstack4.tikitaka.domain.QuizRoomEntity;
import org.fullstack4.tikitaka.dto.QuizDTO;
import org.fullstack4.tikitaka.dto.QuizMemberDTO;
import org.fullstack4.tikitaka.dto.QuizReportDTO;
import org.fullstack4.tikitaka.dto.QuizRoomDTO;
import org.fullstack4.tikitaka.repository.QuizMemberRepository;
import org.fullstack4.tikitaka.repository.QuizReportRepository;
import org.fullstack4.tikitaka.repository.QuizRepository;
import org.fullstack4.tikitaka.repository.QuizRoomRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Vector;
import java.util.stream.Collectors;

@Log4j2
@Service
@RequiredArgsConstructor
@Repository
public class ReportServiceImpl implements ReportServiceIf{

    private final QuizRoomRepository quizRoomRepository;
    private final QuizMemberRepository quizMemberRepository;
    private final QuizRepository quizRepository;
    private final QuizReportRepository quizReportRepository;
    private final ModelMapper modelMapper;

    @Override
    public List<QuizRoomDTO> list(int teacheridx, String status, String search_word) {
        List<QuizRoomEntity> list = quizRoomRepository.findByTeacherIdxAndStatusAndTitleContainsOrderByRoomIdx(teacheridx, status, search_word);
        List<QuizRoomDTO> reportlist =  list.stream().map(report -> modelMapper.map(report, QuizRoomDTO.class)).collect(Collectors.toList());
        for(int i = 0; i< reportlist.size(); i++){
            int roomIdx = reportlist.get(i).getRoomIdx();
            int enterCount = this.enterCount(roomIdx);
            reportlist.get(i).setEnterCount(enterCount);
        }
        return reportlist;
    }

    @Override
    public int enterCount(int roomIdx) {
        int enterCount = quizMemberRepository.countByRoomIdx(roomIdx);
        return enterCount;
    }

    @Override
    public int reportCount(int teacherIdx) {
        int reportCount = quizRoomRepository.countAllByTeacherIdx(teacherIdx);
        return reportCount;
    }

    @Override
    public QuizRoomDTO Roominfo(int roomIdx) {
        QuizRoomEntity entity = quizRoomRepository.findByRoomIdx(roomIdx);
        QuizRoomDTO roominfo = modelMapper.map(entity, QuizRoomDTO.class);
        return roominfo;
    }

    @Override
    public String quizdanwon(int quizIdx) {
        QuizEntity entity = quizRepository.findByQuizIdx(quizIdx);
        QuizDTO quizinfo = modelMapper.map(entity, QuizDTO.class);
        String result = "";
        if(quizinfo.getSchool().equals("E")){
            result = "초등/"+quizinfo.getGrade()+"학년/"+quizinfo.getSemester()+"학기/"+quizinfo.getSubject();
        } else if (quizinfo.getSchool().equals("M")) {
            result = "중등/"+quizinfo.getSubject();
        }else{
            result = "고등/"+quizinfo.getSubject();
        }
        return result;
    }
    @Override
    public QuizDTO quizinfo(int quizIdx){
        QuizEntity entity = quizRepository.findByQuizIdx(quizIdx);
        QuizDTO quizinfo = modelMapper.map(entity, QuizDTO.class);
        if(quizinfo.getSchool().equals("E")){
            quizinfo.setSchool("초등");
        } else if (quizinfo.getSchool().equals("M")) {
            quizinfo.setSchool("중등");
        }else{
            quizinfo.setSchool("고등");
        }
        return quizinfo;
    }
    @Override
    public List<QuizReportDTO> quizReport(int roomIdx) {
        List<QuizReportDTO> quizquestion = new Vector<QuizReportDTO>();
        List<QuizReportEntity> entity = quizReportRepository.findStudentQuestionByRoomIdx(roomIdx);
        List<QuizReportDTO> dtoList = entity.stream().map(report -> modelMapper.map(report, QuizReportDTO.class)).collect(Collectors.toList());
        for(int i = 0; i<dtoList.size();i++){
            QuizReportDTO dto = new QuizReportDTO();
            int check = 0;
            for(int j =0; j<quizquestion.size();j++){
                if(quizquestion.get(j).getStudentQuestion().equals(dtoList.get(i).getStudentQuestion())){
                    check = 1;
                }
            }
            if(check == 0){
                dto.setStudentQuestion(dtoList.get(i).getStudentQuestion());
                quizquestion.add(dto);
            }
        }
        int enter_student = quizReportRepository.studentcount(roomIdx);
        for(int i = 0; i<quizquestion.size(); i++){
            String question = quizquestion.get(i).getStudentQuestion();
            int correct_count = quizReportRepository.countByRoomIdxAndStudentAnswerYNAndAndStudentQuestion(roomIdx, "Y", question);
            quizquestion.get(i).setCorrect_count(correct_count);
            quizquestion.get(i).setCorrect_persent(String.valueOf(((correct_count*100)/enter_student)));
        }
        return quizquestion;
    }
    @Override
    public List<QuizReportDTO> questiondetail(int roomIdx, String question){
        List<String> student_name = quizReportRepository.studentNames(roomIdx);
        List<QuizReportDTO> questioninfo = new Vector<>();
        for(int i = 0; i<student_name.size(); i++){
            Optional<QuizReportEntity> entity = quizReportRepository.findByRoomIdxAndStudentNameAndStudentQuestion(roomIdx, student_name.get(i), question);
            QuizReportDTO dto = new QuizReportDTO();
            log.info("entity : {}", entity);
            if(entity.isEmpty()) {
                dto.setStudentName(student_name.get(i));
                dto.setStudentAnswerYN("X");
            }else {
                dto = modelMapper.map(entity, QuizReportDTO.class);
                if(dto.getStudentAnswerYN().equals("Y")){
                    dto.setStudentAnswerYN("O");
                }else{
                    dto.setStudentAnswerYN("X");
                }
            }
            questioninfo.add(dto);
        }
        return questioninfo;
    }

    @Override
    public List<QuizMemberDTO> memberscore(int roomIdx) {
        List<QuizMemberEntity> entitylist = quizMemberRepository.findByRoomIdx(roomIdx);
        List<QuizMemberDTO> dtoList = entitylist.stream().map(entity -> modelMapper.map(entity, QuizMemberDTO.class)).collect(Collectors.toList());
        log.info("dtolist : {}", dtoList);
        return dtoList;
    }

    @Override
    public List<QuizReportDTO> memberdetail(int roomIdx, String studentName) {
        List<String> question = quizReportRepository.question(roomIdx);
        List<QuizReportDTO> questioninfo = new Vector<>();
        for(int i = 0; i<question.size(); i++){
            Optional<QuizReportEntity> entity = quizReportRepository.findByRoomIdxAndStudentNameAndStudentQuestion(roomIdx, studentName, question.get(i));
            if(entity.isEmpty()){
                QuizReportDTO dto = new QuizReportDTO();
                dto.setStudentQuestion(question.get(i));
                dto.setStudentAnswer("-");
                questioninfo.add(dto);
            }else{
                questioninfo.add(modelMapper.map(entity, QuizReportDTO.class));
            }
        }
        return questioninfo;
    }

    @Override
    public void endreport(int roomIdx) {
        QuizRoomEntity entity = quizRoomRepository.findByRoomIdx(roomIdx);
        QuizRoomDTO dto  = modelMapper.map(entity, QuizRoomDTO.class);
        log.info("dto : {}", dto);
        dto.setStatus("e");
        entity = modelMapper.map(dto, QuizRoomEntity.class);
        quizRoomRepository.save(entity);
    }

    @Override
    public void deletereport(String roomIdx) {
        quizRoomRepository.deleteroom(roomIdx);
    }
}