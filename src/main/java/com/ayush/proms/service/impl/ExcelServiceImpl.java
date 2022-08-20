package com.ayush.proms.service.impl;

import com.ayush.proms.enums.Faculty;
import com.ayush.proms.enums.Role;
import com.ayush.proms.enums.Semester;
import com.ayush.proms.model.User;
import com.ayush.proms.service.ExcelService;
import org.apache.poi.ss.usermodel.*;
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
                if (!isRowEmpty(currentRow)) {

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
                                user.setFullName(
                                        nullCheck(currentCellInRow.getStringCellValue(), "Full Name")
                                );
                                break;
                            case 1:
                                user.setEmail(
                                        nullCheck(currentCellInRow.getStringCellValue(), "Email")
                                );
                                break;
                            case 2:
                                user.setFaculty(
                                        Faculty.valueOf(
                                                nullCheck(currentCellInRow.getStringCellValue().toUpperCase(), "Faculty")
                                        ));
                                break;
                            case 3:
                                user.setSemester(
                                        Semester.valueOf(
                                                nullCheck(currentCellInRow.getStringCellValue().toUpperCase(), "Semester")
                                        ));
                                break;
                            case 4:
                                user.setAddress(
                                        nullCheck(currentCellInRow.getStringCellValue(), "Address"));
                                break;
                            case 5:
                                user.setContact(
                                        nullCheck(String.valueOf(currentCellInRow.getNumericCellValue()), "Contact")
                                );
                                break;
                            default:
                                break;
                        }
                        columnIndex++;
                    }
                    userList.add(user);
                }
            }
            workbook.close();
            return userList;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public String nullCheck(String cellValue,String cell){
        if (cellValue == null || cell.isEmpty() ){
            throw new RuntimeException("No data found for "+ cell);
        }
        else{
            return cellValue;
        }
    }

    public static boolean isRowEmpty(Row row) {
        boolean isEmpty = true;
        DataFormatter dataFormatter = new DataFormatter();
        if(row != null) {
            for(Cell cell: row) {
                if(dataFormatter.formatCellValue(cell).trim().length() > 0) {
                    isEmpty = false;
                    break;
                }
            }
        }
        return isEmpty;
    }


}
