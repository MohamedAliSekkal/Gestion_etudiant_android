package ma.ensa.mobile.studentmanagement.ui.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.card.MaterialCardView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import ma.ensa.mobile.studentmanagement.R;
import ma.ensa.mobile.studentmanagement.data.local.entity.Grade;

public class GradeAdapter extends RecyclerView.Adapter<GradeAdapter.GradeViewHolder> {

    private List<Grade> grades = new ArrayList<>();
    private SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.FRANCE);

    public void setGrades(List<Grade> grades) {
        this.grades = grades != null ? grades : new ArrayList<>();
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public GradeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_grade, parent, false);
        return new GradeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GradeViewHolder holder, int position) {
        Grade grade = grades.get(position);
        holder.bind(grade);
    }

    @Override
    public int getItemCount() {
        return grades.size();
    }

    class GradeViewHolder extends RecyclerView.ViewHolder {
        private MaterialCardView cardView;
        private TextView textViewExamType;
        private TextView textViewGrade;
        private TextView textViewCoefficient;
        private TextView textViewDate;
        private View gradeIndicator;

        public GradeViewHolder(@NonNull View itemView) {
            super(itemView);
            cardView = (MaterialCardView) itemView;
            textViewExamType = itemView.findViewById(R.id.textViewExamType);
            textViewGrade = itemView.findViewById(R.id.textViewGrade);
            textViewCoefficient = itemView.findViewById(R.id.textViewCoefficient);
            textViewDate = itemView.findViewById(R.id.textViewDate);
            gradeIndicator = itemView.findViewById(R.id.gradeIndicator);
        }

        public void bind(Grade grade) {
            // Display exam type
            textViewExamType.setText(grade.getExamType());

            // Display grade
            textViewGrade.setText(String.format(Locale.FRANCE, "%.2f/20", grade.getGradeValue()));

            // Display coefficient
            textViewCoefficient.setText(String.format(Locale.FRANCE, "Coef: %.1f", grade.getCoefficient()));

            // Format and display date
            if (grade.getExamDate() != null) {
                String dateStr = dateFormat.format(new Date(grade.getExamDate() * 1000));
                textViewDate.setText(dateStr);
            }

            // Set grade indicator color based on performance
            double gradeValue = grade.getGradeValue();
            int color;
            if (gradeValue >= 16) {
                color = itemView.getContext().getColor(R.color.success);
            } else if (gradeValue >= 12) {
                color = itemView.getContext().getColor(R.color.warning);
            } else if (gradeValue >= 10) {
                color = itemView.getContext().getColor(R.color.info_light);
            } else {
                color = itemView.getContext().getColor(R.color.error);
            }
            gradeIndicator.setBackgroundColor(color);
        }
    }
}
