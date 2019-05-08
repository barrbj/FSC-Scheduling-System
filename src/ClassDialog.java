import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.text.Text;

import java.io.*;
import java.time.DayOfWeek;

public class ClassDialog extends Dialog<Course> {

    private File INTSTRUCTORS = new File("data/instructors.csv");
    private File CLASSROOMS = new File("data/classrooms.csv");
    private File COURSES_CRN = new File("data/CoursesCRN.csv");

    public ClassDialog() {

        Text dayText = new Text(15, 16, "Days: ");
        Group days = new Group();
        DayOfWeek[] dayOfWeeks = DayOfWeek.values();
        for (int i = 0; i < dayOfWeeks.length - 1; i++) {
            CheckBox checkBox = new CheckBox(dayOfWeeks[i].toString());
            checkBox.setLayoutX(50);
            checkBox.setLayoutY(0 + i * 20);
            days.getChildren().add(checkBox);
        }


        Text timeText = new Text(15, 166, "Time: ");
        ComboBox<String> timeCB = new ComboBox<>();
        timeCB.getItems().addAll("800 - 915", "830 - 1035", "900 - 1015", "925 - 1040", "1015 - 1140", "1030 - 1225", "1040 - 1230", "1050 - 1205", "1215 - 1330", "1230 - 1650", "1340 - 1455", "1505 - 1620", "1545 - 1745", "1630 - 1745", "1755 - 1910", "1920 - 2200", "1945 - 2125");
        timeCB.setPrefWidth(110);
        timeCB.setLayoutX(45);
        timeCB.setLayoutY(150);

        Text courseText = new Text(15, 196, "Course/CRN: ");
        ComboBox<String> courseCB = new ComboBox<>();
        if (COURSES_CRN != null) {
            BufferedReader bufferedReader = null;
            try {
                bufferedReader = new BufferedReader(new FileReader(COURSES_CRN));
                bufferedReader.readLine();
            } catch (IOException e) {
                e.printStackTrace();
            }

            bufferedReader.lines().forEach(course_crn -> {
                String[] crs_crn = course_crn.split(",");
                courseCB.getItems().add("BCS" + crs_crn[0] + (crs_crn.length == 2 ? "/" + crs_crn[1] : ""));
            });
        }
        courseCB.setLayoutX(84);
        courseCB.setLayoutY(180);

        Text instructorText = new Text(15, 226, "Instructor: ");
        ComboBox<String> instructorCB = new ComboBox<>();
        if (INTSTRUCTORS != null) {
            BufferedReader bufferedReader = null;
            try {
                bufferedReader = new BufferedReader(new FileReader(INTSTRUCTORS));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            bufferedReader.lines().forEach(instructor -> {
                instructorCB.getItems().add(instructor);
            });
        }
        instructorCB.getSelectionModel().selectFirst();
        instructorCB.setLayoutX(70);
        instructorCB.setLayoutY(210);

        Text classroomText = new Text(15, 256, "Classroom: ");
        ComboBox<String> classroomCB = new ComboBox<>();
        if (CLASSROOMS != null) {
            BufferedReader bufferedReader = null;
            try {
                bufferedReader = new BufferedReader(new FileReader(CLASSROOMS));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            bufferedReader.lines().forEach(classroom -> {
                classroomCB.getItems().add(classroom);
            });
        }
        classroomCB.getSelectionModel().selectFirst();
        classroomCB.setLayoutX(75);
        classroomCB.setLayoutY(240);


        Group group = new Group(dayText, days, timeText, timeCB, courseText, courseCB, instructorText, instructorCB, classroomText, classroomCB);

        getDialogPane().setPrefWidth(250);
        getDialogPane().setPrefHeight(300);
        setTitle("Add Course");
        getDialogPane().setContent(group);


        getDialogPane().getButtonTypes().addAll(ButtonType.FINISH, ButtonType.CANCEL);
        setResultConverter(buttonType -> {
            if (buttonType.equals(ButtonType.FINISH)) {
                if (timeCB.getValue() != null && courseCB.getValue() != null && instructorCB.getValue() != null && classroomCB.getValue() != null) {
                    String daysOfWeek = "";
                    for (Node node : days.getChildren()) {
                        CheckBox checkBox = (CheckBox) node;
                        if (checkBox.isSelected()) {
                            switch (checkBox.getText()) {
                                case "MONDAY":
                                    daysOfWeek += "M";
                                    break;
                                case "TUESDAY":
                                    daysOfWeek += "T";
                                    break;
                                case "WEDNESDAY":
                                    daysOfWeek += "W";
                                    break;
                                case "THURSDAY":
                                    daysOfWeek += "R";
                                    break;
                                case "FRIDAY":
                                    daysOfWeek += "F";
                                    break;
                                case "SATURDAY":
                                    daysOfWeek += "S";
                                    break;
                            }
                        }
                    }
                    return new Course(daysOfWeek, timeCB.getValue(), courseCB.getValue(), instructorCB.getValue(), classroomCB.getValue());
                }
            } else if (buttonType.equals(ButtonType.CANCEL)) {
                close();
            }
            return null;
        });
    }

    public ClassDialog(Course course) {

        Text dayText = new Text(15, 16, "Days: ");
        Group days = new Group();
        DayOfWeek[] dayOfWeeks = DayOfWeek.values();
        for (int i = 0; i < dayOfWeeks.length - 1; i++) {
            CheckBox checkBox = new CheckBox(dayOfWeeks[i].toString());
            checkBox.setLayoutX(50);
            checkBox.setLayoutY(0 + i * 20);
            if (course.getDays().contains(dayOfWeeks[i]))
                checkBox.setSelected(true);
            days.getChildren().add(checkBox);
        }

        Text timeText = new Text(15, 166, "Time: ");
        ComboBox<String> timeCB = new ComboBox<>();
        timeCB.getItems().addAll("800 - 915", "830 - 1035", "900 - 1015", "925 - 1040", "1015 - 1140", "1030 - 1225", "1040 - 1230", "1050 - 1205", "1215 - 1330", "1230 - 1650", "1340 - 1455", "1505 - 1620", "1545 - 1745", "1630 - 1745", "1755 - 1910", "1920 - 2200", "1945 - 2125");
        timeCB.getSelectionModel().select(course.getTime());
        timeCB.setPrefWidth(110);
        timeCB.setLayoutX(45);
        timeCB.setLayoutY(150);

        Text courseText = new Text(15, 196, "Course/CRN: ");
        ComboBox<String> courseCB = new ComboBox<>();
        if (COURSES_CRN != null) {
            BufferedReader bufferedReader = null;
            try {
                bufferedReader = new BufferedReader(new FileReader(COURSES_CRN));
                bufferedReader.readLine();
            } catch (IOException e) {
                e.printStackTrace();
            }

            bufferedReader.lines().forEach(course_crn -> {
                String[] crs_crn = course_crn.split(",");
                courseCB.getItems().add("BCS" + crs_crn[0] + (crs_crn.length == 2 ? "/" + crs_crn[1] : ""));
            });
        }
        courseCB.getSelectionModel().select(course.getCourse());
        courseCB.setLayoutX(84);
        courseCB.setLayoutY(180);

        Text instructorText = new Text(15, 226, "Instructor: ");
        ComboBox<String> instructorCB = new ComboBox<>();
        if (INTSTRUCTORS != null) {
            BufferedReader bufferedReader = null;
            try {
                bufferedReader = new BufferedReader(new FileReader(INTSTRUCTORS));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            bufferedReader.lines().forEach(instructor -> {
                instructorCB.getItems().add(instructor);
            });
        }
        instructorCB.getSelectionModel().select(course.getInstructor());
        instructorCB.setLayoutX(70);
        instructorCB.setLayoutY(210);

        Text classroomText = new Text(15, 256, "Classroom: ");
        ComboBox<String> classroomCB = new ComboBox<>();
        if (CLASSROOMS != null) {
            BufferedReader bufferedReader = null;
            try {
                bufferedReader = new BufferedReader(new FileReader(CLASSROOMS));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            bufferedReader.lines().forEach(classroom -> {
                classroomCB.getItems().add(classroom);
            });
        }
        classroomCB.getSelectionModel().select(course.getClassroom());
        classroomCB.setLayoutX(75);
        classroomCB.setLayoutY(240);


        Group group = new Group(dayText, days, timeText, timeCB, courseText, courseCB, instructorText, instructorCB, classroomText, classroomCB);

        getDialogPane().setPrefWidth(250);
        getDialogPane().setPrefHeight(300);
        setTitle("Add Course");
        getDialogPane().setContent(group);


        getDialogPane().getButtonTypes().addAll(ButtonType.FINISH, ButtonType.CANCEL);
        setResultConverter(buttonType -> {
            if (buttonType.equals(ButtonType.FINISH)) {
                if (timeCB.getValue() != null && courseCB.getValue() != null && instructorCB.getValue() != null && classroomCB.getValue() != null) {
                    String daysOfWeek = "";
                    for (Node node : days.getChildren()) {
                        CheckBox checkBox = (CheckBox) node;
                        if (checkBox.isSelected()) {
                            switch (checkBox.getText()) {
                                case "MONDAY":
                                    daysOfWeek += "M";
                                    break;
                                case "TUESDAY":
                                    daysOfWeek += "T";
                                    break;
                                case "WEDNESDAY":
                                    daysOfWeek += "W";
                                    break;
                                case "THURSDAY":
                                    daysOfWeek += "R";
                                    break;
                                case "FRIDAY":
                                    daysOfWeek += "F";
                                    break;
                                case "SATURDAY":
                                    daysOfWeek += "S";
                                    break;
                            }
                        }
                    }
                    return new Course(daysOfWeek, timeCB.getValue(), courseCB.getValue(), instructorCB.getValue(), classroomCB.getValue());
                }
            } else if (buttonType.equals(ButtonType.CANCEL)) {
                close();
            }
            return null;
        });

    }

}
