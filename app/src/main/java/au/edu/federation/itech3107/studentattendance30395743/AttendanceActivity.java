package au.edu.federation.itech3107.studentattendance30395743;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class AttendanceActivity extends AppCompatActivity {

    private Spinner dateSpinner;
    private RecyclerView studentsRecyclerView;
    private StudentAttendanceAdapter adapter;
    private DatabaseHelper dbHelper;
    private Cursor studentsByCourseId;
    private int courseId;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance);
        courseId = getIntent().getIntExtra("COURSE_ID", -1);
        Log.d("AttendanceActivity", "Received courseId: " + courseId);

        dbHelper = new DatabaseHelper(this);

        dateSpinner = findViewById(R.id.dateSpinner);
        studentsRecyclerView = findViewById(R.id.studentsRecyclerView);
        Button saveAttendanceButton = findViewById(R.id.saveAttendanceButton);

        List<Student> students = getStudentsFromDatabase(courseId);

        adapter = new StudentAttendanceAdapter(this,students);
        studentsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        studentsRecyclerView.setAdapter(adapter);

        saveAttendanceButton.setOnClickListener(v -> saveAttendance());


        DatabaseHelper databaseHelper = new DatabaseHelper(this);

        new Thread(new Runnable() {
            @Override
            public void run() {
                Log.e("获取课程信息courseId “", courseId +"");
                Course courseByBean = databaseHelper.getCourseById(courseId);
                Log.e("获取课程信息“",courseByBean.toString());
                List<String> listDate = courseByBean.getListDate();
                studentsByCourseId = databaseHelper.getStudentsByCourseId(courseId);

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ArrayAdapter<String> dateAdapter = new ArrayAdapter<>(AttendanceActivity.this,
                                android.R.layout.simple_spinner_item, listDate);
                        dateAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        dateSpinner.setAdapter(dateAdapter);
                    }
                });


            }
        }).start();


    }
    @SuppressLint("Range")
    private List<Student> getStudentsFromDatabase(int courseId) {
        List<Student> students = new ArrayList<>();
        Cursor cursor =   dbHelper.getStudentsByCourseId(courseId);
       // Cursor cursor = dbHelper.getAllStudents();
        while (cursor.moveToNext()) {
            int id = cursor.getInt(cursor.getColumnIndex(DatabaseHelper.COLUMN_STUDENT_IDS));
            String name = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_STUDENT_NAME));
            students.add(new Student(id, name));
        }
        cursor.close();

        return students;
    }

    private void saveAttendance() {
        String selectedDate = dateSpinner.getSelectedItem().toString();
        SparseBooleanArray attendance = adapter.getAttendanceState();

        for (int i = 0; i < attendance.size(); i++) {
            int studentId = attendance.keyAt(i);
            boolean isPresent = attendance.valueAt(i);
            dbHelper.insertAttendance(selectedDate, studentId, isPresent,courseId);
        }
        Toast.makeText(this, "Attendance Saved", Toast.LENGTH_SHORT).show();
    }
}
