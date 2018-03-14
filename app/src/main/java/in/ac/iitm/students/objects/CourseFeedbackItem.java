package in.ac.iitm.students.objects;

/**
 * Created by dell on 03-03-2018.
 */

public class CourseFeedbackItem {
    private String courseName, courseNo, courseProfs;

    public CourseFeedbackItem() {
    }

    public CourseFeedbackItem(String courseName, String courseNo, String courseProfs) {
        this.courseName = courseName;
        this.courseNo = courseNo;
        this.courseProfs = courseProfs;
    }

    public String getCourseFeedbackItemName() {
        return courseName;
    }

    public void setCourseFeedbackItemName(String courseName) {
        this.courseName = courseName;
    }

    public String getCourseFeedbackItemNo() {
        return courseNo;
    }

    public void setCourseFeedbackItemNo(String courseNo) {
        this.courseNo = courseNo;
    }

    public String getCourseFeedbackItemProfs() {
        return courseProfs;
    }

    public void setCourseFeedbackItemProfs(String courseProfs) {
        this.courseProfs = courseProfs;
    }
}
