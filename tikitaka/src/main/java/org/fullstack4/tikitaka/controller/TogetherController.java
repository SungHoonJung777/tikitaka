package org.fullstack4.tikitaka.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.fullstack4.tikitaka.dto.*;
import org.fullstack4.tikitaka.service.KafkaProducer;
import org.fullstack4.tikitaka.service.QuizGameServiceIf;
import org.fullstack4.tikitaka.service.TogetherService;
import org.springframework.http.HttpRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.*;

@Log4j2
@RequiredArgsConstructor
@Controller
public class TogetherController {
    private final KafkaProducer producer;
    private final TogetherService togetherService;
    private final ObjectMapper objectMapper;
    // 채팅방 목록
    public static LinkedList<ChatingRoom> chatingRoomList = new LinkedList<>();

    //	----------------------------------------------------
    // 유틸 메서드

    // 방 번호로 방 찾기
    public ChatingRoom findRoom(String roomNumber) {
        ChatingRoom room = ChatingRoom.builder().roomNumber(roomNumber).build();
        int index = chatingRoomList.indexOf(room);

        if(chatingRoomList.contains(room)) {
            return chatingRoomList.get(index);
        }
        return null;
    }


    // 쿠키에 추가
    public void addCookie(String cookieName, String cookieValue) {
        ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        HttpServletResponse response = attr.getResponse();

        Cookie cookie = new Cookie(cookieName, cookieValue);

        int maxage = 60 * 60 * 24 * 7;
        cookie.setMaxAge(maxage);
        response.addCookie(cookie);
    }



    // 방 번호, 닉네임 쿠키 삭제
    public void deleteCookie( ) {
        ServletRequestAttributes attr = (ServletRequestAttributes)RequestContextHolder.currentRequestAttributes();
        HttpServletResponse response = attr.getResponse();

        Cookie roomCookie = new Cookie("roomNumber", null);
        Cookie nicknameCookie = new Cookie("nickname",null);

        roomCookie.setMaxAge(0);
        nicknameCookie.setMaxAge(0);

        response.addCookie(nicknameCookie);
        response.addCookie(roomCookie);
    }



    // 쿠키에서 방번호, 닉네임 찾기
    public Map<String, String> findCookie() {
        ServletRequestAttributes attr = (ServletRequestAttributes)RequestContextHolder.currentRequestAttributes();
        HttpServletRequest request = attr.getRequest();

        Cookie[] cookies = request.getCookies();
        String roomNumber = "";
        String nickname= "";

        if(cookies == null) {
            return null;
        }

        if(cookies != null) {
            for(int i=0;i<cookies.length;i++) {
                if("roomNumber".equals(cookies[i].getName())) {
                    roomNumber = cookies[i].getValue();
                }
                if("nickname".equals(cookies[i].getName())) {
                    nickname = cookies[i].getValue();
                }
            }

            if(!"".equals(roomNumber) && !"".equals(nickname)) {
                Map<String, String> map = new HashMap<>();
                map.put("nickname", nickname);
                map.put("roomNumber", roomNumber);

                return map;
            }
        }

        return null;
    }

    // 닉네임 생성
    public void createNickname(String nickname , String character) {
        addCookie("nickname", nickname);
        addCookie("character", character);
    }

    // 방 입장하기
    public boolean enterChatingRoom(ChatingRoom chatingRoom, String nickname , String character) {
        createNickname(nickname ,character);

        if(chatingRoom == null) {
            deleteCookie();
            return false;
        } else {
            LinkedList<String> users = chatingRoom.getUsers();
            users.add(nickname);
            LinkedList<String> characters = chatingRoom.getCharacters();
            characters.add(character);
            addCookie("roomNumber", chatingRoom.getRoomNumber());
            return true;
        }
    }


    // 메인화면
    @GetMapping("/urlCreate")
    public String main() {
        return "/together/urlCreate";
    }

    //선생님 팝업창 넘어가기
    @GetMapping("/start")
    public String startGET(String roomName, String nickname, String roomNumber, String code, Model model,
                           String roomIdx , HttpServletRequest request) {
        int idx = Integer.parseInt(roomIdx);
        HttpSession session = request.getSession();
        int memberIdx = (Integer) session.getAttribute("memberIdx");
        System.out.println("memberIdx: " + memberIdx);
        int iResult = togetherService.insertRoom(code, roomName,nickname , idx , memberIdx);

        model.addAttribute("roomName", roomName);
        model.addAttribute("nickname", nickname);
        model.addAttribute("code", code);
        model.addAttribute("roomIdx", roomIdx);
        if(roomNumber !=null && !roomNumber.equals("")){
            model.addAttribute("roomNumber", roomNumber);
        }
        return "together/start";
    }


    // 채팅방 목록
    @GetMapping("/chatingRoomList")
    public ResponseEntity<?> chatingRoomList() {
        return new ResponseEntity<LinkedList<ChatingRoom>>(chatingRoomList, HttpStatus.OK);
    }

    @PostMapping("/chatingRoom")
    public ResponseEntity<?> chatingRoomPOST(String roomName, String nickname , String roomNumber , String character){
        log.info("roomNumber: " + roomNumber);
        ChatingRoom chatingRoom = ChatingRoom.builder()
                .roomNumber(roomNumber)
                .users(new LinkedList<>())
                .roomName(roomName)
                .characters(new LinkedList<>())
                .build();
        chatingRoomList.add(chatingRoom);

        enterChatingRoom(chatingRoom, nickname , character);
        log.info("chatingRoom List 입니다:" + chatingRoomList.toString());
        return new ResponseEntity<>(chatingRoom, HttpStatus.OK);
    }

    // 방 들어가기
    @GetMapping("/chatingRoom-enter")
    public ResponseEntity<?> EnterChatingRoom(String roomNumber, String nickname , String character){
        log.info("EnterChatingRoom");
        // 방 번호로 방 찾기
        ChatingRoom chatingRoom = findRoom(roomNumber);

        if(chatingRoom == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            // 방 들어가기
            enterChatingRoom(chatingRoom, nickname ,character );
            //enterChatingRoom(chatingRoom, nickname , character);

            return new ResponseEntity<>(chatingRoom, HttpStatus.OK);
        }
    }

    // 방 나가기
    @PatchMapping("/chatingRoom-exit")
    public ResponseEntity<?> ExitChatingRoom(){

        Map<String, String> map = findCookie();

        if(map == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }


        String roomNumber = map.get("roomNumber");
        String nickname = map.get("nickname");

        // 방목록에서 방번호에 맞는 유저목록 가져오기
        ChatingRoom chatingRoom = findRoom(roomNumber);
        List<String> users = chatingRoom.getUsers();

        // 닉네임 삭제
        users.remove(nickname);

        // 쿠키에서 닉네임과 방번호 삭제
        deleteCookie();

        // 유저가 한명도 없다면 방 삭제
        if(users.size() == 0) {
            chatingRoomList.remove(chatingRoom);
        }

        return new ResponseEntity<>(chatingRoom, HttpStatus.OK);
    }
    // 방 강퇴하기
    @PatchMapping("/chatingRoom-resign")
    public ResponseEntity<?> resignChatingRoom(@RequestBody ResignRequest request){
        String nickname = request.getNickname();
        String roomNumber = request.getRoomNumber();
        log.info("resign nickname" + nickname);
        log.info("resign roomNumber" + roomNumber);

        // 방목록에서 방번호에 맞는 유저목록 가져오기
        ChatingRoom chatingRoom = findRoom(roomNumber);
        List<String> users = chatingRoom.getUsers();
        for(int i=0; i<users.size(); i++){
            log.info("users:" + users.get(i));
        }
        // 닉네임 삭제
        users.remove(nickname);

        // 유저가 한명도 없다면 방 삭제
        if(users.size() == 0) {
            chatingRoomList.remove(chatingRoom);
        }

        return new ResponseEntity<>(chatingRoom, HttpStatus.OK);
    }
    @PatchMapping("/chatingRoom-exitTest")
    public ResponseEntity<?> exitTestChatingRoom(@RequestBody ResignRequest request){
        String nickname = request.getNickname();
        String roomNumber = request.getRoomNumber();
        log.info("resign nickname" + nickname);
        log.info("resign roomNumber" + roomNumber);

        // 방목록에서 방번호에 맞는 유저목록 가져오기
        ChatingRoom chatingRoom = findRoom(roomNumber);
        List<String> users = chatingRoom.getUsers();
        for(int i=0; i<users.size(); i++){
            log.info("users:" + users.get(i));
        }
        // 닉네임 삭제
        users.remove(nickname);

        // 유저가 한명도 없다면 방 삭제
        if(users.size() == 0) {
            chatingRoomList.remove(chatingRoom);
        }

        return new ResponseEntity<>(chatingRoom, HttpStatus.OK);
    }

    // 참가 중이었던 대화방
    @GetMapping("/chatingRoom")
    public ResponseEntity<?> chatingRoom() {
        // 쿠키에 닉네임과 방번호가 있다면 대화중이던 방이 있던것
        Map<String, String> map = findCookie();

        if(map == null) {
            return new ResponseEntity<>(HttpStatus.OK);
        }

        String roomNumber = map.get("roomNumber");
        String nickname = map.get("nickname");

        ChatingRoom chatingRoom = findRoom(roomNumber);

        if(chatingRoom == null) {
            deleteCookie();
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            Map<String, Object> map2 = new HashMap<>();
            map2.put("chatingRoom", chatingRoom);
            map2.put("myNickname", nickname);

            return new ResponseEntity<>(map2, HttpStatus.OK);
        }
    }



    //	----------------------------------------------------
    // 메세지 컨트롤러

    // 여기서 메세지가 오면 방목록 업데이트
    @MessageMapping("/socket/roomList")
    @SendTo("/topic/roomList")
    public String roomList() {
        return "";
    }


    @MessageMapping("/socket/sendMessage/{roomNumber}")
    @SendTo("/topic/message/{roomNumber}")
    public MessageLine sendMessage(@DestinationVariable String roomNumber, MessageLine message) {
        return message;
    }

    // 채팅방에 입장 퇴장 메세지 보내기
    @MessageMapping("/socket/notification/{roomNumber}")
    @SendTo("/topic/notification/{roomNumber}")
    public Map<String, Object> notification(@DestinationVariable String roomNumber, Map<String, Object> chatingRoom) {
        return chatingRoom;
    }

    @GetMapping("/nameCheck")
    public String nameCheck(String roomNumber , Model model){
        model.addAttribute("roomNumber",roomNumber);

        return "together/nameCheck";
    }

    @GetMapping("/ready")
    public String ready(Model model, String roomNumber , String nickname , String character){

        model.addAttribute("roomNumber",roomNumber);
        model.addAttribute("nickname",nickname);
        model.addAttribute("character",character);

        return "together/ready";
    }
    @GetMapping("/regist")
    public String registGET() {

        return "together/regist";
    }
    @MessageMapping("/socket/kick-user/{roomNumber}/{member}")
    @SendTo("/topic/kick/{roomNumber}/{member}")
    public String kickUser(@DestinationVariable String roomNumber,@DestinationVariable String member){
        log.info("username Kick roomNumber" + roomNumber);
        log.info("username Kick member" + member);
        /*log.info("username Kick" + username);*/
        return "{\"message\": \"" + member + "\"}";

    }
    @GetMapping("/userlist")
    public String userListGET(String roomNumber, Model model , String quizIdx) {
        log.info("quizIdx:" + quizIdx);
        int idx = Integer.parseInt(quizIdx);
        List<TogetherDetailDTO> readTogetherDetailList = togetherService.readTogetherDetailList(idx);

        log.info("readTogetherDetailList : " + readTogetherDetailList);

        try {
            int readCount = readTogetherDetailList.size();
            model.addAttribute("readCount", readCount);
            model.addAttribute("readTogetherDetailList",objectMapper.writeValueAsString(readTogetherDetailList));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        // 방목록에서 방번호에 맞는 유저목록 가져오기
        ChatingRoom chatingRoom = findRoom(roomNumber);
        List<String> users = chatingRoom.getUsers();

        int leng = users.size() - 1;

        int rNumber  = Integer.parseInt(roomNumber);
        int uResult = togetherService.modifyStatusRoom(rNumber);
        model.addAttribute("quizIdx" , idx);
        model.addAttribute("userLength" , leng);
        model.addAttribute("users",users);
        model.addAttribute("roomNumber",roomNumber);
        return "together/gameStartTeacher";
    }

    @MessageMapping("/socket/start/{roomNumber}")
    @SendTo("/topic/start/{roomNumber}")
    public List<String> startQuiz(@DestinationVariable String roomNumber){
        ChatingRoom chatingRoom = findRoom(roomNumber);
        List<String> users = chatingRoom.getUsers();
        log.info("roomNumber" + roomNumber);
        return users;
    }

    @GetMapping("/startStudent")
    public String startStudent(String roomNumber , String nickname, Model model , String character) {
        int idx = Integer.parseInt(roomNumber);
        int quizIdx = togetherService.getQuizIdx(idx);
        List<TogetherDetailDTO> readTogetherDetailList = togetherService.readTogetherDetailList(quizIdx);

        try {
            int readCount = readTogetherDetailList.size();
            model.addAttribute("readCount", readCount);
            model.addAttribute("readTogetherDetailList",objectMapper.writeValueAsString(readTogetherDetailList));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        model.addAttribute("nickname",nickname);
        model.addAttribute("roomNumber",roomNumber);
        model.addAttribute("character",character);
        return "together/gameStartStudent";
    }
    @MessageMapping("/socket/first/{roomNumber}")
    @SendTo("/topic/first/{roomNumber}")
    public String firstQuiz(@DestinationVariable String roomNumber){
        log.info("firstQuiz : roomNumber" + roomNumber);
        return "";
    }

    @MessageMapping("/socket/nextQuiz/{roomNumber}")
    @SendTo("/topic/nextQuiz/{roomNumber}")
    public String nextQuiz(@DestinationVariable String roomNumber){
        log.info("nextQuiz : roomNumber" + roomNumber);
        return "";
    }

    @MessageMapping("/socket/next/{roomNumber}")
    @SendTo("/topic/next/{roomNumber}")
    public String nextQuizAnswer(@DestinationVariable String roomNumber){
        log.info("nextQuizAnswer : roomNumber" + roomNumber);
        return "";
    }

    @GetMapping("/resultTeacher")
    public String resultTeacher(String roomNumber , String readCount , String quizIdx , Model model){
        int count = Integer.parseInt(readCount);
        int rNumber = Integer.parseInt(roomNumber);
        int qIdx = Integer.parseInt(quizIdx);

        togetherService.modifyStatusRoomEnd(rNumber);
        List<TogetherDetailDTO> readTogetherDetailList = togetherService.readTogetherDetailList(qIdx);
        log.info("readTogetherDetailListRESTLUT" + readTogetherDetailList);
        int num[] = new int[readTogetherDetailList.size()];
        int percent[] = new int[readTogetherDetailList.size()];
        for(int i=0; i<readTogetherDetailList.size(); i++){
            System.out.println("ss : " + readTogetherDetailList.get(i).getQuestion() );
            int rCount = togetherService.getCountY(readTogetherDetailList.get(i).getQuestion() , rNumber);
            num[i] = rCount;
            percent[i] = (int) ((double) num[i] / count * 100);
        }
        model.addAttribute("percent", percent);
        model.addAttribute("numCount", num);
        model.addAttribute("readCount", count);
        model.addAttribute("readTogetherDetailList",readTogetherDetailList);

        return "together/resultTeacher";
    }
    @GetMapping("/resultStudent")
    public String resultStudent(String roomNumber , String nickname, String img , Model model){
        model.addAttribute("roomNumber" , roomNumber);
        model.addAttribute("nickname" , nickname);
        model.addAttribute("character" , img);
        int idx = Integer.parseInt(roomNumber);
        List<QuizReportDTO> report = togetherService.resultStudent(nickname,idx);
        int total = 0;
        for(int i=0; i<report.size(); i++){
            total += report.get(i).getStudentScore();
        }
        log.info("report : " + report);
        model.addAttribute("total" , total);
        model.addAttribute("report" , report);
        return "together/resultStudent";
    }
    @GetMapping("/togetherSetting")
    public String togetherSetting(String name, String num , Model model){

        model.addAttribute("name" , name);
        model.addAttribute("num" , num);
        return "together/togetherSetting";
    }

    @MessageMapping("/socket/submit/{roomNumber}/{member}")
    @SendTo("/topic/submit/{roomNumber}")
    public String submitQuizAnswer(@DestinationVariable String roomNumber,@DestinationVariable String member){
        log.info("nextQuizAnswer" +roomNumber  + " : " +  member);
        return member;
    }
    @PostMapping("/insertScore")
    @ResponseBody
    public String insertScore(@RequestBody QuizReportDTO scoreRequest) {
        int iResult = togetherService.insertScore(scoreRequest);
        return "Score inserted successfully!";
    }
    @PostMapping("/modifyScore")
    @ResponseBody
    public String modifyScore(@RequestBody QuizReportDTO scoreRequest) {
        int iResult = togetherService.modifyScore(scoreRequest);
        return "Score modifyScore successfully!";
    }

    @GetMapping("/roomStatusCheck")
    public ResponseEntity<String> checkRoomStatus(@RequestParam int roomNumber) {
        String status = togetherService.getRoomStatus(roomNumber);
        log.info("roomStatusCheck : stauts : " + status);
        return ResponseEntity.ok(status);
    }
}
