import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.stage.FileChooser;
import javafx.stage.Window;
import javafx.util.Callback;

import java.io.*;
import java.time.DayOfWeek;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public class SchedulingController {

    @FXML
    private TableView<Course> classesTable;

    @FXML
    private MenuItem importMenuItem;

    @FXML
    private MenuItem exportBtn;

    @FXML
    private MenuItem closeMenuItem;

    @FXML
    private Button addBtn;

    @FXML
    private Button deleteBtn;

    @FXML
    private Button editBtn;

    public static Set<String> instructors = new HashSet<>();
    public static Set<String> rooms = new HashSet<>();

    @FXML
    public void initialize(){

        TableColumn<Course, DayOfWeek> dayColumn = new TableColumn<>("Days");
        dayColumn.setPrefWidth(140);
        dayColumn.setCellValueFactory(new PropertyValueFactory<>("days"));

        TableColumn<Course, String> timeColumn = new TableColumn<>("Time");
        timeColumn.setPrefWidth(140);
        timeColumn.setCellValueFactory(new PropertyValueFactory<>("time"));

        TableColumn<Course, String> courseColumn = new TableColumn<>("Course/CRN");
        courseColumn.setPrefWidth(140);
        courseColumn.setCellValueFactory(new PropertyValueFactory<>("course"));

        TableColumn<Course, String> instructorColumn = new TableColumn<>("Instructor");
        instructorColumn.setPrefWidth(140);
        instructorColumn.setCellValueFactory(new PropertyValueFactory<>("instructor"));

        TableColumn<Course, String> classroomColumn = new TableColumn<>("Classroom");
        classroomColumn.setPrefWidth(140);
        classroomColumn.setCellValueFactory(new PropertyValueFactory<>("classroom"));

        classesTable.getColumns().setAll(dayColumn, timeColumn, courseColumn, instructorColumn, classroomColumn);
        classesTable.getSortOrder().add(dayColumn);

        addBtn.setOnAction(event -> {
            ClassDialog classDialog = new ClassDialog();
            Window classDialogWindow = classDialog.getDialogPane().getScene().getWindow();
            classDialogWindow.setOnCloseRequest(event1 -> classDialogWindow.hide());
             Optional<Course> result = classDialog.showAndWait();
             if(result.isPresent()){
                 classesTable.getItems().add(result.get());
             }

        });

        editBtn.setOnAction(event -> {
            Course course = classesTable.getSelectionModel().getSelectedItem();
            ClassDialog classDialog = new ClassDialog(course);
            Window classDialogWindow = classDialog.getDialogPane().getScene().getWindow();
            classDialogWindow.setOnCloseRequest(event1 -> classDialogWindow.hide());
            Optional<Course> result = classDialog.showAndWait();

            if(result.isPresent()){
                if(!result.get().equals(course)) {
                    //classesTable.getItems().remove(classesTable.getSelectionModel().getSelectedIndex());
                    classesTable.getItems().set(classesTable.getSelectionModel().getSelectedIndex(), result.get());
                }
            }
        });

        deleteBtn.setOnAction(event -> {
            if(classesTable.getItems().size() > 0)
                classesTable.getItems().remove(classesTable.getSelectionModel().getSelectedIndex());
            classesTable.getSelectionModel().selectBelowCell();
        });
        deleteBtn.disableProperty().bind(Bindings.size(classesTable.getItems()).isEqualTo(0));

        importMenuItem.setOnAction(event -> {
            classesTable.getItems().clear();
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Open CSV File");
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV File (*.csv)", "*.csv"));
            File file = fileChooser.showOpenDialog(null);

            if(file != null){
                try {
                    BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
                    bufferedReader.readLine();
                    bufferedReader.lines().forEach(s -> {
                        String[] data = s.split(",");
                        if(data.length != 0) {
                            Course course = new Course(data.length == 7 ? data[3] : "", data[4] + " - " + data[5], "BCS" + data[0] + ( !data[1].isEmpty() ? "/" + data[1] : ""), data.length == 7 ? data[6] : "", data[2]);
                            classesTable.getItems().add(course);
                            if (data.length == 7) {
                                instructors.add(data[6]);
                            }
                            rooms.add(data[2]);
                        }
                    });
                    bufferedReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            classesTable.getSelectionModel().selectFirst();
        });

        exportBtn.setOnAction(event -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Save File");
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV File (*.csv)", "*.csv"));
            File file = fileChooser.showSaveDialog(null);

            if(file != null){
                try {
                    BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file));
                    for (Course course : classesTable.getItems()) {
                        String[] courseData = course.getCourse().split("/");
                        String[] timeData = course.getTime().split(" - ");
                        bufferedWriter.write(courseData[0].replace("BCS", "") + "," + courseData[1] + "," + course.getClassroom() + "," + course.getDaysAsString() + "," + timeData[0] + "," + timeData[1] + "," + course.getInstructor() + "\n");
                    }
                    bufferedWriter.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        closeMenuItem.setOnAction(event -> System.exit(0));

    }

}
