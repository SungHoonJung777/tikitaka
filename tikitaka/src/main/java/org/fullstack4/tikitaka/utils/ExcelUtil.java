package org.fullstack4.tikitaka.utils;

import jakarta.servlet.http.HttpServletResponse;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.fullstack4.tikitaka.dto.QuizDTO;
import org.fullstack4.tikitaka.dto.QuizMemberDTO;
import org.fullstack4.tikitaka.dto.QuizReportDTO;
import org.fullstack4.tikitaka.dto.QuizRoomDTO;
import org.fullstack4.tikitaka.service.ReportServiceIf;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class ExcelUtil {
    private final ReportServiceIf reportServiceIf;

    public ExcelUtil(ReportServiceIf reportServiceIf) {
        this.reportServiceIf = reportServiceIf;
    }

    public static void excelDownload(HttpServletResponse response, ReportServiceIf reportServiceIf, int teacher_idx, String endYn, String search_word) throws Exception{
        InputStream is;

        is = new URL("https://kdj-java-4.s3.ap-northeast-2.amazonaws.com/tikitaka/test/template.xlsx").openStream();

        Workbook wb = new XSSFWorkbook(is);

        Sheet sheet = wb.getSheetAt(0);

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmm");

        Date date = new Date(Calendar.getInstance().getTimeInMillis());
        String fileName = "report_"+ simpleDateFormat.format(date);

        int reportCount = reportServiceIf.reportCount(teacher_idx);

        CellStyle style1 = wb.createCellStyle();
        style1.setAlignment(HorizontalAlignment.CENTER);
        style1.setBorderRight(BorderStyle.THIN);
        style1.setBorderLeft(BorderStyle.THIN);
        style1.setBorderTop(BorderStyle.THIN);
        style1.setBorderBottom(BorderStyle.THIN);

        CellStyle style2 = wb.createCellStyle();
        style2.setAlignment(HorizontalAlignment.LEFT);

        Row row = null;
        Cell cell = null;
        int columnNum = 0;
        int rowNum = 6;

        List<QuizRoomDTO> reportlist = reportServiceIf.list(teacher_idx, endYn, search_word);
        System.out.println(endYn+", "+search_word);

        row = sheet.getRow(4);
        cell = row.createCell(2);
        cell.setCellStyle(style2);
        cell.setCellValue(reportlist.size());

        //리스트내용 셋팅
        if(reportlist.isEmpty()){
            for(int i = 0; i<10; i++) {
                row = sheet.getRow(6);
                cell = row.createCell(i);
                cell.setCellStyle(style1);
                cell.setCellValue("데이터가 없습니다");
            }
            sheet.addMergedRegion(new CellRangeAddress(6, 6, 0, 9));
        }
        else {
            for (int i = 0; i < reportlist.size(); i++) {
                columnNum = 0;
                QuizRoomDTO dto = reportlist.get(i);
                row = sheet.createRow(rowNum);
                for (int j = 0; j < 10; j++) {
                    cell = row.createCell(columnNum);
                    cell.setCellStyle(style1);
                    if(j==0){
                        cell.setCellValue(dto.getRoomIdx());
                    } else if (j == 1) {
                        cell.setCellValue("퀴즈");
                    } else if (j == 2) {
                        if(dto.getRegDate() == null){
                            cell.setCellValue("-");
                        }else {
                            String strRegDate = dto.getRegDate().toString().substring(0, 10);
                            cell.setCellValue(strRegDate);
                        }
                    } else if (j == 4) {
                        if(dto.getEndDate() == null){
                            cell.setCellValue("-");
                        }else {
                            String strEndDate = dto.getEndDate().toString();
                            cell.setCellValue(strEndDate);
                        }
                    } else if (j == 6) {
                        cell.setCellValue(dto.getTitle());
                    } else if (j == 9) {
                        cell.setCellValue(dto.getEnterCount());
                    }
                    columnNum++;
                }
                sheet.addMergedRegion(new CellRangeAddress(rowNum, rowNum, 2, 3));
                sheet.addMergedRegion(new CellRangeAddress(rowNum, rowNum, 4, 5));
                sheet.addMergedRegion(new CellRangeAddress(rowNum, rowNum, 6, 8));
                rowNum++;
            }
        }

        // 컨텐츠 타입과 파일명 지정
        response.setContentType("ms-vnd/excel");

        // 정규식 사용하여 파일명에 .@$^공백이 있을경우 _로 대체
        fileName = fileName.replaceAll("[.@$^\\s]", "_");
        // 헤더에 한글 셋팅
        fileName = URLEncoder.encode(fileName, "UTF-8");
        response.setHeader("Content-Disposition", "attachment;filename="+fileName+".xlsx");

        // Excel File Output
        wb.write(response.getOutputStream());
        wb.close();
    }
    public static void excelDownload2(HttpServletResponse response, ReportServiceIf reportServiceIf, int roomIdx) throws Exception{
        InputStream is;

        is = new URL("https://kdj-java-4.s3.ap-northeast-2.amazonaws.com/tikitaka/test/template2.xlsx").openStream();

        QuizRoomDTO roominfo = reportServiceIf.Roominfo(roomIdx);//방 정보
        QuizDTO quizinfo = reportServiceIf.quizinfo(roominfo.getQuizIdx());//퀴즈의 정보
        List<QuizReportDTO> questioninfo = reportServiceIf.quizReport(roomIdx);//문제별 정답 정보
        List<QuizMemberDTO> dtolist = reportServiceIf.memberscore(roomIdx);//멤버별 정보
        for(int i = 0; i<dtolist.size();i++){
            QuizMemberDTO dto = dtolist.get(i);
            dto.setEnterDate(dto.getRegDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd a HH:mm:ss")));
        }

        String fileName = "detail_"+ LocalDateTime.now();

        Workbook wb = new XSSFWorkbook(is);

        Sheet sheet = wb.getSheetAt(0);

        if(roominfo.getContinueYN().equals("Y")){
            roominfo.setContinueYN("재입장 가능");
        }else{
            roominfo.setContinueYN("재입장 불가능");
        }
        if(roominfo.getScoreYN().equals("Y")){
            roominfo.setScoreYN("점수 표시");
        }else{
            roominfo.setScoreYN("점수 미표시");
        }
        if(roominfo.getCommentYN().equals("Y")){
            roominfo.setCommentYN("해설 표시");
        }else{
            roominfo.setCommentYN("해설 미표시");
        }
        String result = reportServiceIf.quizdanwon(roominfo.getQuizIdx());

        Row row = null;
        Cell cell = null;

        CellStyle style1 = wb.createCellStyle();
        style1.setAlignment(HorizontalAlignment.CENTER);
        style1.setBorderRight(BorderStyle.THIN);
        style1.setBorderLeft(BorderStyle.THIN);
        style1.setBorderTop(BorderStyle.THIN);
        style1.setBorderBottom(BorderStyle.THIN);

        row = sheet.getRow(3);
        cell = row.getCell(0);
        CellStyle style2 = cell.getCellStyle();

        row = sheet.getRow(4);
        cell = row.getCell(0);
        CellStyle style3 = cell.getCellStyle();
        row = sheet.getRow(15);
        cell = row.getCell(0);
        CellStyle style4 = cell.getCellStyle();

        row = sheet.getRow(4);
        cell = row.createCell(2);
        cell.setCellStyle(style1);
        cell.setCellValue(roominfo.getTitle());
        row = sheet.getRow(5);
        cell = row.createCell(2);
        cell.setCellStyle(style1);
        cell.setCellValue(result);
        row = sheet.getRow(6);
        cell = row.createCell(2);
        cell.setCellStyle(style1);
        cell.setCellValue(roominfo.getRegDate().toString().substring(0, 10));
        row = sheet.getRow(9);
        cell = row.createCell(2);
        cell.setCellStyle(style1);
        cell.setCellValue(roominfo.getEndDate()==null?"-":roominfo.getEndDate().toString());
        row = sheet.getRow(10);
        cell = row.createCell(2);
        cell.setCellStyle(style1);
        cell.setCellValue(roominfo.getIntro());
        row = sheet.getRow(11);
        cell = row.createCell(2);
        cell.setCellStyle(style1);
        cell.setCellValue(roominfo.getContinueYN()+"/"+roominfo.getScoreYN()+"/"+roominfo.getCommentYN());
        row = sheet.getRow(14);
        cell = row.createCell(2);
        cell.setCellStyle(style1);
        cell.setCellValue(dtolist.size());

        int rowNum = 16;

        for(int i = 0; i<dtolist.size(); i++){
            row = sheet.createRow(rowNum);
            for(int j = 0; j < 10; j++){
                cell = row.createCell(j);
                cell.setCellStyle(style1);
                if(j==0){
                    cell.setCellValue(dtolist.get(i).getStudentName());
                }else if(j==2){
                    cell.setCellValue(dtolist.get(i).getStudentPassword());
                } else if (j==4) {
                    cell.setCellValue(dtolist.get(i).getStudentTotalScore());
                }else if(j==6){
                    cell.setCellValue(dtolist.get(i).getStudentCorrectCount());
                }else if(j==8){
                    cell.setCellValue(dtolist.get(i).getEnterDate());
                }
            }
            sheet.addMergedRegion(new CellRangeAddress(rowNum, rowNum, 0, 1));
            sheet.addMergedRegion(new CellRangeAddress(rowNum, rowNum, 2, 3));
            sheet.addMergedRegion(new CellRangeAddress(rowNum, rowNum, 4, 5));
            sheet.addMergedRegion(new CellRangeAddress(rowNum, rowNum, 6, 7));
            sheet.addMergedRegion(new CellRangeAddress(rowNum, rowNum, 8, 9));
            rowNum++;
        }

        // 컨텐츠 타입과 파일명 지정
        response.setContentType("ms-vnd/excel");

        // 정규식 사용하여 파일명에 .@$^공백이 있을경우 _로 대체
        fileName = fileName.replaceAll("[.@$^\\s]", "_");
        // 헤더에 한글 셋팅
        fileName = URLEncoder.encode(fileName, "UTF-8");
        response.setHeader("Content-Disposition", "attachment;filename="+fileName+".xlsx");

        // Excel File Output
        wb.write(response.getOutputStream());
        wb.close();
    }
}
