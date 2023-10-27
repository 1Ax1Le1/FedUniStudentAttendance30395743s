package au.edu.federation.itech3107.studentattendance30395743;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;


public class StudentsAdapter extends RecyclerView.Adapter<StudentsAdapter.StudentViewHolder> {
    private List<Student> studentList;
    private Context context;

    public StudentsAdapter(Context context, List<Student> studentList) {
        this.context = context;
        this.studentList = studentList;
    }

    @NonNull
    @Override
    public StudentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.student_item, parent, false);
        return new StudentViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull StudentViewHolder holder, int position) {
        Student student = studentList.get(position);
        holder.tvStudentName.setText(student.getName());
        holder.btnDelete.setOnClickListener(view -> {
            deleteStudent(student.getId());
            studentList.remove(position);
            notifyItemRemoved(position);
        });
    }

    @Override
    public int getItemCount() {
        return studentList.size();
    }

    public void deleteStudent(long studentId) {

        DatabaseHelper databaseHelper = new DatabaseHelper(context);
        databaseHelper.deleteStudent(studentId);

    }

    public class StudentViewHolder extends RecyclerView.ViewHolder {
        TextView tvStudentName;
        Button btnDelete;

        public StudentViewHolder(@NonNull View itemView) {
            super(itemView);
            tvStudentName = itemView.findViewById(R.id.tvStudentName);
            btnDelete = itemView.findViewById(R.id.btnDelete);
        }
    }
}
