package au.edu.federation.itech3107.studentattendance30395743;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;


public class AttendanceAdapter extends BaseAdapter {
    private Context context;
    private Cursor cursor;

    public AttendanceAdapter(Context context, Cursor cursor) {
        this.context = context;
        this.cursor = cursor;
    }

    @Override
    public int getCount() {
        return cursor.getCount();
    }

    @Override
    public Object getItem(int position) {
        cursor.moveToPosition(position);
        return cursor;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @SuppressLint("Range")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view;

        if (convertView == null) {
            view = inflater.inflate(R.layout.attendance_item, parent, false);
        } else {
            view = convertView;
        }

        TextView studentNameTextView = view.findViewById(R.id.studentNameTextView);
        TextView attendanceStatusTextView = view.findViewById(R.id.attendanceStatusTextView);
        TextView attendanceClassName = view.findViewById(R.id.attendanceClassName);

        cursor.moveToPosition(position);


        int studentId = cursor.getInt(cursor.getColumnIndex(DatabaseHelper.COLUMN_ATTENDANCE_STUDENT_ID));
        Log.e("获取出勤数据库中的：", String.valueOf(studentId));
        DatabaseHelper databaseHelper = new DatabaseHelper(context);
        Cursor studentCursor = databaseHelper.getStudent(String.valueOf(studentId));
        if (studentCursor.moveToFirst()) {
            String studentName = studentCursor.getString(studentCursor.getColumnIndex(DatabaseHelper.COLUMN_STUDENT_NAME));
            studentNameTextView.setText(studentName);
            String studentCouresId = studentCursor.getString(studentCursor.getColumnIndex(DatabaseHelper.COLUMN_STUDENT_COURSE_ID));
            String courseName = databaseHelper.getCourseNameById(studentCouresId);
            Log.e("获取出勤数据库中的studentCouresId：", String.valueOf(studentCouresId));
            Log.e("获取出勤数据库中的courseName：", String.valueOf(courseName));
            attendanceClassName.setText(courseName);
        } else {
            studentNameTextView.setText("Unknown Student"); // 或其它默认文本
        }
        studentCursor.close();
        int isPresent = cursor.getInt(cursor.getColumnIndex(DatabaseHelper.COLUMN_ATTENDANCE_IS_PRESENT));
        attendanceStatusTextView.setText(isPresent == 1 ? "Attendance" : "NO Attendance");

        return view;
    }

}
