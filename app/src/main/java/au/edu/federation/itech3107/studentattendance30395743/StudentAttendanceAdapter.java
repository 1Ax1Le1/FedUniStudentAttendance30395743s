package au.edu.federation.itech3107.studentattendance30395743;


import static au.edu.federation.itech3107.studentattendance30395743.DatabaseHelper.COLUMN_STUDENT_IDS;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;


public class StudentAttendanceAdapter extends RecyclerView.Adapter<StudentAttendanceAdapter.ViewHolder> {
    private Context context;
    private List<Student> students;
    private SparseBooleanArray attendanceState;

    public StudentAttendanceAdapter(Context context,List<Student> students) {
        this.context = context;
        this.students = students;
        this.attendanceState = new SparseBooleanArray();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_student_attendance, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Student student = students.get(position);
        holder.studentNameTextView.setText(student.getName());

        DatabaseHelper databaseHelper = new DatabaseHelper(context);
        SQLiteDatabase db = databaseHelper.getReadableDatabase();

        Cursor cursor = databaseHelper.getStudentByName(db, student.getName());

        if (cursor != null) {
            while (cursor.moveToNext()) {
                @SuppressLint("Range")
                String studentId = cursor.getString(cursor.getColumnIndex(COLUMN_STUDENT_IDS));

                holder.attendanceCheckbox.setChecked(attendanceState.get(Integer.parseInt(studentId), false));
                Log.e("获取学生列表所有数据:",Integer.parseInt(studentId)+"");
                holder.attendanceCheckbox.setOnCheckedChangeListener((buttonView, isChecked) -> attendanceState.put(Integer.parseInt(studentId), isChecked));
            }
            cursor.close();
        }
        db.close();




    }

    @Override
    public int getItemCount() {
        return students.size();
    }

    public SparseBooleanArray getAttendanceState() {
        return attendanceState;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView studentNameTextView;
        CheckBox attendanceCheckbox;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            studentNameTextView = itemView.findViewById(R.id.studentNameTextView);
            attendanceCheckbox = itemView.findViewById(R.id.attendanceCheckbox);
        }
    }
}

