package au.edu.federation.itech3107.studentattendance30395743;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;
import java.util.function.Consumer;

public class SetDateActivity extends AppCompatActivity {
    private Button btnSetStartDate, btnSetEndDate;
    private TextView tvStartDate, tvEndDate;
    private String startDate, endDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_date);

        btnSetStartDate = findViewById(R.id.btnSetStartDate);
        btnSetEndDate = findViewById(R.id.btnSetEndDate);
        tvStartDate = findViewById(R.id.tvStartDate);
        tvEndDate = findViewById(R.id.tvEndDate);

        btnSetStartDate.setOnClickListener(v -> {
            selectDate((date) -> {
                startDate = date;
                tvStartDate.setText("Start Date: " + date);
            });
        });

        btnSetEndDate.setOnClickListener(v -> {
            selectDate((date) -> {
                endDate = date;
                tvEndDate.setText("End Date: " + date);
            });
        });
    }

    @SuppressLint("NewApi")
    private void selectDate(Consumer<String> onDateSelected) {
        Calendar calendar = Calendar.getInstance();
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                SetDateActivity.this,
                (view, year, month, dayOfMonth) -> onDateSelected.accept(year + "-" + (month + 1) + "-" + dayOfMonth),
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
        );
        datePickerDialog.show();
    }
}
