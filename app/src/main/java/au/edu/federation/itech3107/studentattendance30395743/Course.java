package au.edu.federation.itech3107.studentattendance30395743;

import java.util.List;

public class Course {
    private int courseId;
    private String courseName;
    private List<String> listDate;  // 添加这一行

    public Course(int courseId, String courseName, List<String> listDate) {  // 更新构造函数
        this.courseId = courseId;
        this.courseName = courseName;
        this.listDate = listDate;  // 添加这一行
    }

    public int getCourseId() {
        return courseId;
    }

    public String getCourseName() {
        return courseName;
    }

    public List<String> getListDate() {  // 新增 getter 方法
        return listDate;
    }

    @Override
    public String toString() {
        return "Course{" +
                "courseId='" + courseId + '\'' +
                ", courseName='" + courseName + '\'' +
                ", listDate=" + listDate +
                '}';
    }
}
