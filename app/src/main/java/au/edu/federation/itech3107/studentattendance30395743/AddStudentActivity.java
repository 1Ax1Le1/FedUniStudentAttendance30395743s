package au.edu.federation.itech3107.studentattendance30395743;


import static au.edu.federation.itech3107.studentattendance30395743.DatabaseHelper.COLUMN_COURSE_ID;
import static au.edu.federation.itech3107.studentattendance30395743.DatabaseHelper.COLUMN_COURSE_NAME;
import static au.edu.federation.itech3107.studentattendance30395743.DatabaseHelper.COLUMN_STUDENT_ID;
import static au.edu.federation.itech3107.studentattendance30395743.DatabaseHelper.COLUMN_STUDENT_NAME;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class AddStudentActivity extends AppCompatActivity {
    private EditText edtStudentName,edtStudentId;
    private Spinner spinnerCourses;
    private Button btnAddStudentToCourse;
    private DatabaseHelper dbHelper;
    private List<String> courseNames = new ArrayList<>();
    private List<String> courseIds = new ArrayList<>();

    @SuppressLint("Range")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_student);
        edtStudentId = findViewById(R.id.edtStudentId);
        edtStudentName = findViewById(R.id.edtStudentName);
        spinnerCourses = findViewById(R.id.spinnerCourses);
        btnAddStudentToCourse = findViewById(R.id.btnAddStudentToCourse);
        dbHelper = new DatabaseHelper(this);


        Cursor cursor = dbHelper.getAllCourses();
        while (cursor.moveToNext()) {
            courseIds.add(cursor.getString(cursor.getColumnIndex(COLUMN_COURSE_ID)));
            courseNames.add(cursor.getString(cursor.getColumnIndex(COLUMN_COURSE_NAME)));
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, courseNames);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCourses.setAdapter(adapter);

        btnAddStudentToCourse.setOnClickListener(v -> {
            String student = edtStudentId.getText().toString().trim();
            String studentName = edtStudentName.getText().toString().trim();
            int selectedCoursePosition = spinnerCourses.getSelectedItemPosition();

            if (selectedCoursePosition >= 0 && selectedCoursePosition < courseIds.size()) {
                String courseId = courseIds.get(selectedCoursePosition);
                Log.e("get courseId:",courseId);
                long studentId = dbHelper.insertStudent(student, studentName, Long.parseLong(courseId));
                if (studentId != -1) {
                    Toast.makeText(AddStudentActivity.this, "Student Added Successfully", Toast.LENGTH_SHORT).show();
                    edtStudentName.setText("");
                    finish();
                } else {
                    Toast.makeText(AddStudentActivity.this, "Error Adding Student", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(AddStudentActivity.this, "Please select a valid course.", Toast.LENGTH_SHORT).show();
            }
        });



        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        List<Student> studentList = new ArrayList<>();
        DatabaseHelper databaseHelper = new DatabaseHelper(this);
        Cursor cursors = databaseHelper.getAllStudents();
        while (cursors.moveToNext()) {
            studentList.add(new Student(
                    cursors.getInt(cursors.getColumnIndex(COLUMN_STUDENT_ID)),
                    cursors.getString(cursors.getColumnIndex(COLUMN_STUDENT_NAME))
            ));
        }

        StudentsAdapter adapters = new StudentsAdapter(this, studentList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapters);

    }
}

