package au.edu.federation.itech3107.studentattendance30395743;

import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.necer.calendar.MonthCalendar;


public class Attendance2 extends AppCompatActivity {

    private ListView attendanceListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance2);

        //  final CompactCalendarView compactCalendarView = findViewById(R.id.compactcalendar_view);


        TextView tvDate = findViewById(R.id.tv_date);
        MonthCalendar weekCalendar =  findViewById(R.id.weekCalendar);

        weekCalendar.setOnCalendarChangedListener((baseCalendar, year, month, localDate, dateChangeBehavior) -> {
            Log.e("日历选中日期：", localDate.toString());
            displayAttendance(localDate.toString());

            // 获取星期名称
            int dayOfWeekNum = Integer.parseInt(String.valueOf(localDate.getDayOfWeek()));
            String dayOfWeek = "";

            switch (dayOfWeekNum) {
                case 1:
                    dayOfWeek = "Monday";
                    break;
                case 2:
                    dayOfWeek = "Tuesday";
                    break;
                case 3:
                    dayOfWeek = "Wednesday";
                    break;
                case 4:
                    dayOfWeek = "Thursday";
                    break;
                case 5:
                    dayOfWeek = "Friday";
                    break;
                case 6:
                    dayOfWeek = "Saturday";
                    break;
                case 7:
                    dayOfWeek = "Sunday";
                    break;
            }

            tvDate.setText(year + "  year " + month + " month " + dayOfWeek);
        });

        // Add an event
     //   Event ev1 = new Event(Color.RED, 1634256000000L, "Event at this time"); // timestamp is in milliseconds
     ////   compactCalendarView.addEvent(ev1);

        // Listen for date changes
       // compactCalendarView.setListener(new CompactCalendarView.CompactCalendarViewListener() {
//            @Override
//            public void onDayClick(Date dateClicked) {
//                Calendar calendar = Calendar.getInstance();
//                calendar.setTime(dateClicked);
//                int year = calendar.get(Calendar.YEAR);
//                int month = calendar.get(Calendar.MONTH) + 1; // 0-based months
//                int day = calendar.get(Calendar.DAY_OF_MONTH);
//
//                String formattedDate = year + "-" + month + "-" + day;
//                Log.d("TAG", "Formatted Date: " + formattedDate);
//
//                displayAttendance(formattedDate);
//            }
//
//
//            @Override
//            public void onMonthScroll(Date firstDayOfNewMonth) {
//                Log.d("TAG", "Month was scrolled to: " + firstDayOfNewMonth);
//            }
//        });


       // DatePicker datePicker = findViewById(R.id.datePicker);
        attendanceListView = findViewById(R.id.attendanceListView);

//        datePicker.init(datePicker.getYear(), datePicker.getMonth(), datePicker.getDayOfMonth(),
//                new DatePicker.OnDateChangedListener() {
//                    @Override
//                    public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
//                        String selectedDate = year + "-" + (monthOfYear + 1) + "-" + dayOfMonth; // 格式化日期
//                        displayAttendance(selectedDate);
//                    }
//                });
    }

    private void displayAttendance(String date) {
        new Thread(() -> {
            DatabaseHelper databaseHelper = new DatabaseHelper(this);
            Log.e("查询日期data:",date.toString());
            Cursor cursor = databaseHelper.getAttendanceByDate(date);

            new Handler(Looper.getMainLooper()).post(() -> {


                AttendanceAdapter adapter = new AttendanceAdapter(this, cursor);

                attendanceListView.setAdapter(adapter);
            });

        }).start();



    }
}