package au.edu.federation.itech3107.studentattendance30395743;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.necer.calendar.BaseCalendar;
import com.necer.calendar.WeekCalendar;
import com.necer.enumeration.CheckModel;
import com.necer.enumeration.DateChangeBehavior;
import com.necer.listener.OnCalendarChangedListener;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


public class AddCourseActivity extends AppCompatActivity {
    private EditText edtCourseId, edtCourseName;
    private Button btnAddCourse;
    private String UpClassDate;
    private int currentWeek = 1;
    private int currentEndWeek = 1;
    private DatabaseHelper dbHelper;
    @SuppressLint({"MissingInflatedId", "NewApi"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_course);
        EditText edtCourseId = findViewById(R.id.edtCourseId);
        EditText  edtCourseName = findViewById(R.id.edtCourseName);
        TextView tvStartDate = findViewById(R.id.tvStartDate);
        TextView  tvEndDate = findViewById(R.id.tvEndDate);


        Button  btnSave = findViewById(R.id.btnSave);


        Button buttonlast = findViewById(R.id.toLastPager);
        Button buttonnext = findViewById(R.id.toNextPager);
        TextView txNextPager = findViewById(R.id.tx_pager);

        Button buttonendlast = findViewById(R.id.toEndLastPager);
        Button buttonendnext = findViewById(R.id.toEndNextPager);
        TextView txEndNextPager = findViewById(R.id.txendpager);


        WeekCalendar weekCalendar2 = findViewById(R.id.btnPickEndDate);
        weekCalendar2.setCheckMode(CheckModel.SINGLE_DEFAULT_CHECKED);
        weekCalendar2.setOnCalendarChangedListener((baseCalendar, year, month, localDate, dateChangeBehavior) ->
                tvEndDate.setText(localDate.toString()));

        WeekCalendar weekUpclassDate = findViewById(R.id.upclassDate);
        weekUpclassDate.setOnCalendarChangedListener(new OnCalendarChangedListener() {
            @Override
            public void onCalendarChange(BaseCalendar baseCalendar, int year, int month, org.joda.time.LocalDate localDate, DateChangeBehavior dateChangeBehavior) {
                UpClassDate = localDate.toString();
            }
        });

        WeekCalendar weekCalendar = findViewById(R.id.btnPickStartDate);
        weekCalendar.setCheckMode(CheckModel.SINGLE_DEFAULT_CHECKED);
        weekCalendar.setOnCalendarChangedListener(new OnCalendarChangedListener() {
            @Override
            public void onCalendarChange(BaseCalendar baseCalendar, int year, int month, org.joda.time.LocalDate localDate, DateChangeBehavior dateChangeBehavior) {
                tvStartDate.setText(localDate.toString());

            }
        });
        buttonlast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (currentWeek>1){
                    currentWeek --;
                    txNextPager.setText(currentWeek + "week");
                    weekCalendar.toLastPager();
                }

            }
        });

        buttonnext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                weekCalendar.toNextPager();
                currentWeek ++;
                txNextPager.setText( currentWeek + "week");
            }
        });


        buttonendlast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (currentEndWeek>1){
                    currentEndWeek --;
                    txEndNextPager.setText(currentEndWeek + "week");
                    weekCalendar2.toLastPager();
                }

            }
        });

        buttonendnext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                weekCalendar2.toNextPager();
                currentEndWeek ++;
                txEndNextPager.setText( currentEndWeek + "week");
            }
        });


        dbHelper = new DatabaseHelper(this);

        btnSave.setOnClickListener(v -> {
            String courseId = edtCourseId.getText().toString().trim();
            String courseName = edtCourseName.getText().toString().trim();

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd"); // 根据您的日期格式进行调整
            LocalDate currentStartDate = LocalDate.parse(UpClassDate, formatter);

            List<String> datesList = new ArrayList<>();
            for (int i = 0; i < 12; i++) {
                datesList.add(currentStartDate.format(formatter));
                currentStartDate = currentStartDate.plusDays(7);
            }
            String datesString = String.join(",", datesList);
            Log.e("添加上课日期“：",datesList.toString());
            boolean isInserted = dbHelper.insertCourse(courseId, courseName
                    ,tvStartDate.getText().toString(),tvEndDate.getText().toString(),datesString);
            if (isInserted) {
                Toast.makeText(AddCourseActivity.this, "Course Added Successfully", Toast.LENGTH_SHORT).show();
                edtCourseId.setText("");
                edtCourseName.setText("");
                finish();
                Intent intent = new Intent(this,MainActivity.class);
                startActivity(intent);
            } else {
                Toast.makeText(AddCourseActivity.this, "Error Adding Course", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void showDatePicker(TextView targetView) {
        Calendar cal = Calendar.getInstance();
        int day = cal.get(Calendar.DAY_OF_MONTH);
        int month = cal.get(Calendar.MONTH);
        int year = cal.get(Calendar.YEAR);

        DatePickerDialog datePicker = new DatePickerDialog(this,
                (view, year1, monthOfYear, dayOfMonth) -> targetView.setText(year1 + "-" + (monthOfYear + 1) + "-" + dayOfMonth),
                year, month, day);
        datePicker.show();
    }
}

