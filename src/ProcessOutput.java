import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.DayOfWeek;
import java.util.List;

/**
 * This class writes the Schedule, the Data and some statistics to a excel file
 */
public class ProcessOutput {

    //the file name of the generated file
    public static final String FILE_NAME = "Schedule.xlsx";

    /**
     * this method generates the ouput
     *
     * @param schedule
     * @param data
     */
    public static void generateScheduleOutput(Schedule schedule, Data data) {
        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet scheduleSheet = workbook.createSheet("Schedule");
        XSSFSheet dataSheet = workbook.createSheet("Data");
        XSSFSheet statisticsSheet = workbook.createSheet("Statistics");
        int rowNum = 0;
        createScheduleSheet(scheduleSheet, schedule.getClasses(), data);
        createStatisticsSheet(statisticsSheet, schedule, data);
        createDataSheet(dataSheet, data);
        try {
            FileOutputStream outputStream = new FileOutputStream(FILE_NAME);
            workbook.write(outputStream);
            workbook.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("Done");
    }

    /**
     * this method writes to the statistic sheet
     *
     * @param statisticsSheet
     * @param schedule
     */
    private static void createStatisticsSheet(XSSFSheet statisticsSheet,
                                              Schedule schedule,
                                              Data data) {
        int numRow = 0;
        Row row = statisticsSheet.createRow(numRow++);
        Cell cell = row.createCell(0);
        cell.setCellValue("Number Of Conflicts");
        cell = row.createCell(1);
        cell.setCellValue(schedule.getNumberOfConflicts());
        row = statisticsSheet.createRow(numRow++);
        cell = row.createCell(0);
        cell.setCellValue("Best Schedule Fitness");
        cell = row.createCell(1);
        cell.setCellValue(schedule.getFitness());
        row = statisticsSheet.createRow(numRow++);
        cell = row.createCell(0);
        cell.setCellValue("Generations");
        cell = row.createCell(1);
        cell.setCellValue(data.getFitnessPerGeneration().keySet().size());
        row = statisticsSheet.createRow(numRow++);
        cell = row.createCell(0);
        cell.setCellValue("Fitness per Generation");
        row = statisticsSheet.createRow(numRow++);
        cell = row.createCell(0);
        cell.setCellValue("Number Of Generation");
        cell = row.createCell(1);
        cell.setCellValue("Fitness");
        cell = row.createCell(2);
        cell.setCellValue("Conflicts");
        for (Integer generation : data.getFitnessPerGeneration().keySet()) {
            row = statisticsSheet.createRow(numRow++);
            cell = row.createCell(0);
            cell.setCellValue(generation);
            cell = row.createCell(1);
            cell.setCellValue(data.getFitnessPerGeneration().get(generation));
            cell = row.createCell(2);
            cell.setCellValue(data.getConflictsPerGeneration().get(generation));
        }

    }

    /**
     * this method writes to the data sheet
     *
     * @param dataSheet
     * @param data
     */
    private static void createDataSheet(XSSFSheet dataSheet, Data data) {
        int rowNum = 0;
        Row row = dataSheet.createRow(rowNum++);
        Cell cell = row.createCell(0);
        cell.setCellValue("Rooms");
        for (Room room : data.getRooms()) {
            row = dataSheet.createRow(rowNum++);
            cell = row.createCell(0);
            cell.setCellValue(room.getName());
        }
        row = dataSheet.createRow(++rowNum);
        cell = row.createCell(0);
        cell.setCellValue("Teachers");
        int col = 0;
        row = dataSheet.createRow(rowNum++);
        cell = row.createCell(col++);
        cell.setCellValue("ID");
        cell = row.createCell(col++);
        cell.setCellValue("Name");
        cell = row.createCell(col++);
        cell.setCellValue("Lessons");
        cell = row.createCell(col++);
        cell.setCellValue("Available Hours Per Day");
        cell = row.createCell(col++);
        cell.setCellValue("Available Hours Per Week");
        for (Teacher teacher : data.getTeachers()) {
            col = 0;
            row = dataSheet.createRow(++rowNum);
            cell = row.createCell(col++);
            cell.setCellValue(teacher.getId());
            cell = row.createCell(col++);
            cell.setCellValue(teacher.getName());
            cell = row.createCell(col++);
            for (int i = 0; i < teacher.getLessons().size(); i++) {
                cell.setCellValue(cell.getStringCellValue() + teacher.getLessons().get(i) + " ");
            }
            cell = row.createCell(col++);
            cell.setCellValue(teacher.getAvailableHoursPerDay().get(DayOfWeek.of(1).name()));
            cell = row.createCell(col++);
            cell.setCellValue(teacher.getAvailableHoursPerWeek());
        }
        row = dataSheet.createRow(++rowNum);
        cell = row.createCell(0);
        cell.setCellValue("Lessons");
        col = 0;
        row = dataSheet.createRow(rowNum++);
        cell = row.createCell(col++);
        cell.setCellValue("ID");
        cell = row.createCell(col++);
        cell.setCellValue("Name");
        cell = row.createCell(col++);
        cell.setCellValue("Available Hours Per Room");
        cell = row.createCell(col++);
        cell.setCellValue("Teachers");
        for (Lesson lesson : data.getLessons()) {
            col = 0;
            row = dataSheet.createRow(++rowNum);
            cell = row.createCell(col++);
            cell.setCellValue(lesson.getId());
            cell = row.createCell(col++);
            cell.setCellValue(lesson.getName());
            cell = row.createCell(col++);
            for (Room room : lesson.getAvailableHoursPerRoom().keySet()) {
                cell.setCellValue(cell.getStringCellValue() + room.getName() + ":" + lesson.getAvailableHoursPerRoom().get(room) + " ");
            }
            cell = row.createCell(col++);
            for (int i = 0; i < lesson.getTeachers().size(); i++) {
                if (i == lesson.getTeachers().size() - 1) {
                    cell.setCellValue(cell.getStringCellValue() + lesson.getTeachers().get(i).getName());
                } else {
                    cell.setCellValue(cell.getStringCellValue() + lesson.getTeachers().get(i).getName() + ", ");
                }
            }
        }
    }

    /**
     * this method writes to the schedule sheet
     *
     * @param scheduleSheet
     * @param classes
     * @param data
     */
    private static void createScheduleSheet(XSSFSheet scheduleSheet,
                                            List<Class> classes,
                                            Data data) {
        int rowNum = 0;
        for (Room room : data.getRooms()) {
            Row row = scheduleSheet.createRow(rowNum++);
            Cell cell = row.createCell(0);
            cell.setCellValue("Room: " + room.getName());
            row = scheduleSheet.createRow(rowNum++);
            for (int hour = 0; hour <= Schedule.MAX_HOURS; hour++) {
                row = scheduleSheet.createRow(rowNum++);
                for (int day = 1; day <= Schedule.DAYS; day++) {
                    if (hour == 0) {
                        cell = row.createCell(day);
                        cell.setCellValue(DayOfWeek.of(day).name());
                    } else if (day == 1 && hour != 0) {
                        cell = row.createCell(day - 1);
                        cell.setCellValue(hour);
                        cell = row.createCell(day);
                        cell.setCellValue(findClass(classes, room, DayOfWeek.of(day).name(), hour));
                    } else {
                        cell = row.createCell(day);
                        cell.setCellValue(findClass(classes, room, DayOfWeek.of(day).name(), hour));
                    }
                }
            }
            row = scheduleSheet.createRow(rowNum++);
            row = scheduleSheet.createRow(rowNum++);
        }
    }

    /**
     * this method is used for finding the class for a room, day and hour
     */
    private static String findClass(List<Class> classes, Room room, String day, int hour) {
        for (Class clas : classes) {
            if (clas.getRoom().equals(room) &&
                    clas.getDay().equals(day) &&
                    clas.getHour() == hour) {
                return clas.getLesson().getName();
            }
        }
        return "";
    }
}
