package au.edu.federation.itech3107.studentattendance30395743;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class MainActivity extends AppCompatActivity {
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button btnStudent =  findViewById(R.id.btnStudent);
        Button btnAdd =  findViewById(R.id.btnAdd);
        Button btnAttendance =  findViewById(R.id.btn_attendance);
        btnStudent.setOnClickListener(view -> {
            Intent intent = new Intent( this,AddStudentActivity.class);
            startActivity(intent);
        });
        btnAdd.setOnClickListener(view -> {
            Intent intent = new Intent(this,AddCourseActivity.class);
            startActivity(intent);
        });

        btnAttendance.setOnClickListener(view -> {
            Intent intent = new Intent(this,Attendance2.class);
            startActivity(intent);
        });




        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        DatabaseHelper databaseHelper = new DatabaseHelper(this);
        List<Course> courses = fetchCoursesFromDB(databaseHelper);
        CourseAdapter adapter = new CourseAdapter(courses, this, course -> {

            Intent intent = new Intent(MainActivity.this, AttendanceActivity.class);
            Log.e("获取课程信息COURSE_ID“",course.getCourseId()+"");
            intent.putExtra("COURSE_ID", course.getCourseId());
            startActivity(intent);
        });

        recyclerView.setAdapter(adapter);
    }
    @SuppressLint("Range")
    private List<Course> fetchCoursesFromDB(DatabaseHelper dbHelper) {
        List<Course> courses = new ArrayList<>();
        Cursor cursor = dbHelper.getAllCourses();
        while (cursor.moveToNext()) {
            int courseId = cursor.getInt(cursor.getColumnIndex(DatabaseHelper.COLUMN_COURSE_ID));
            String courseName = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_COURSE_NAME));

            // 获取 listDate 字符串，并将其转换为 List<String>
            String listDateString = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_COURSE_LIST_DATE));

            List<String> listDate = new ArrayList<>(Arrays.asList(listDateString.split(",")));

            courses.add(new Course(courseId, courseName, listDate));
        }
        cursor.close();
        return courses;
    }

}