package max.iv.tz_convenient_software.utill;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;


import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;

public class ExcelReader {

    public static List<Integer> readIntegersFromXlsx(String filePath) {
        List<Integer> numbers = new ArrayList<>();
        int columnIndex = 0;

        try (FileInputStream fis = new FileInputStream(filePath);
             Workbook workbook = new XSSFWorkbook(fis)) {


            for (Sheet sheet : workbook) {
                System.out.println("Чтение данных с листа: " + sheet.getSheetName());

                for (Row row : sheet) {
                    if (row == null) continue;

                    Cell cell = row.getCell(columnIndex);

                    if (cell != null && cell.getCellType() == CellType.NUMERIC) {
                        double numericValue = cell.getNumericCellValue();
                        if (numericValue == Math.floor(numericValue)) {
                            numbers.add((int) numericValue);
                        }
                    }

                }
            }

        } catch (Exception e) {
            throw new RuntimeException("Ошибка чтения файла XLSX: " + e.getMessage(), e);
        }

        return numbers;
    }
}
