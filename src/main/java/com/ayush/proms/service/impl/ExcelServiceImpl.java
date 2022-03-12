package com.ayush.proms.service.impl;

import com.ayush.proms.enums.Faculty;
import com.ayush.proms.enums.Semester;
import com.ayush.proms.model.User;
import com.ayush.proms.service.ExcelService;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

@Service
public class ExcelServiceImpl implements ExcelService {

    @Override
    public List<User> convertToEntity(InputStream inputStream) {
        List<User> userList = new ArrayList<User>();

        try {
            Workbook workbook = new XSSFWorkbook(inputStream);

            Sheet sheet = workbook.getSheetAt(0);

            Iterator rows = sheet.iterator();
            int rowNumber = 0;
            while (rows.hasNext()) {
                Row currentRow = (Row) rows.next();

                /*Skips Heading*/
                if (rowNumber == 0) {
                    rowNumber++;
                    continue;
                }

                Iterator cellsInRow = currentRow.iterator();
                User user = new User();
                int columnIndex = 0;
                while (cellsInRow.hasNext()) {
                    Cell currentCellInRow = (Cell) cellsInRow.next();
                    switch (columnIndex) {
                        case 0:
                            user.setFullName(currentCellInRow.getStringCellValue());
                            break;
                        case 1:
                            user.setEmail(currentCellInRow.getStringCellValue());
                            break;
                        case 2:
                            user.setFaculty(Faculty.valueOf(currentCellInRow.getStringCellValue().toUpperCase()));
                            break;
                        case 3:
                            user.setSemester(Semester.valueOf(currentCellInRow.getStringCellValue().toUpperCase()));
                            break;
                        case 4:
                            user.setPassword(currentCellInRow.getStringCellValue());
                        default:
                            break;
                    }
                    columnIndex++;
                }
                userList.add(user);
            }
            workbook.close();
            return userList;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
