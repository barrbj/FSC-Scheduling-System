import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class Course{

    private List<DayOfWeek> days;
    private String time;
    private String course;
    private String instructor;
    private String classroom;

    public Course(String days, String time, String course, String instructor, String classroom) {
        this.days = getDaysFromString(days);
        this.time = time;
        this.course = course;
        this.instructor = instructor;
        this.classroom = classroom;
    }



    private List<DayOfWeek> getDaysFromString(String days) {
        List<DayOfWeek> dayOfWeeks= new ArrayList<DayOfWeek>(){
            @Override
            public String toString() {
                return getDaysAsString();
            }
        };
        for (String s : days.split("")) {
            switch (s.toUpperCase()){
                case "M":
                    dayOfWeeks.add(DayOfWeek.MONDAY);
                    break;
                case "T":
                    dayOfWeeks.add(DayOfWeek.TUESDAY);
                    break;
                case "W":
                    dayOfWeeks.add(DayOfWeek.WEDNESDAY);
                    break;
                case "R":
                    dayOfWeeks.add(DayOfWeek.THURSDAY);
                    break;
                case "F":
                    dayOfWeeks.add(DayOfWeek.FRIDAY);
                    break;
                case "S":
                    dayOfWeeks.add(DayOfWeek.SATURDAY);
                    break;
            }
        }
        return dayOfWeeks;
    }

    public List<DayOfWeek> getDays() {
        return days;
    }

    public String getDaysAsString(){
        String daysString = "";
        for (DayOfWeek day : days) {
            switch (day){
                case MONDAY:
                    daysString += "M";
                    break;
                case TUESDAY:
                    daysString += "T";
                    break;
                case WEDNESDAY:
                    daysString += "W";
                    break;
                case THURSDAY:
                    daysString += "R";;
                    break;
                case FRIDAY:
                    daysString += "F";
                    break;
                case SATURDAY:
                    daysString += "S";
                    break;
            }
        }
        return daysString;
    }

    public String getTime() {
        return time;
    }

    public String getCourse(){
        return course;
    }

    public String getInstructor() {
        return instructor;
    }

    public String getClassroom() {
        return classroom;
    }

    @Override
    public String toString() {
        return "Course{" +
                "days=" + days +
                ", time='" + time + '\'' +
                ", course='" + course + '\'' +
                ", instructor='" + instructor + '\'' +
                ", classroom='" + classroom + '\'' +
                '}';
    }
}
