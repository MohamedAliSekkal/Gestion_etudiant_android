package ma.ensa.mobile.studentmanagement.ui.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;
import java.util.List;

import ma.ensa.mobile.studentmanagement.R;
import ma.ensa.mobile.studentmanagement.data.local.entity.Student;

/**
 * Adapter for displaying students enrolled in a module
 */
public class StudentInModuleAdapter extends RecyclerView.Adapter<StudentInModuleAdapter.StudentViewHolder> {

    private List<Student> students;
    private OnStudentActionListener listener;

    public interface OnStudentActionListener {
        void onAddGradeClick(Student student);
        void onAddAbsenceClick(Student student);
    }

    public StudentInModuleAdapter(OnStudentActionListener listener) {
        this.students = new ArrayList<>();
        this.listener = listener;
    }

    public void setStudents(List<Student> students) {
        this.students = students != null ? students : new ArrayList<>();
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public StudentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_student_in_module, parent, false);
        return new StudentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StudentViewHolder holder, int position) {
        Student student = students.get(position);
        holder.bind(student, listener);
    }

    @Override
    public int getItemCount() {
        return students.size();
    }

    static class StudentViewHolder extends RecyclerView.ViewHolder {
        private TextView textViewStudentName;
        private TextView textViewApogee;
        private TextView textViewAverageGrade;
        private MaterialButton buttonAddGrade;
        private MaterialButton buttonAddAbsence;

        public StudentViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewStudentName = itemView.findViewById(R.id.textViewStudentName);
            textViewApogee = itemView.findViewById(R.id.textViewApogee);
            textViewAverageGrade = itemView.findViewById(R.id.textViewAverageGrade);
            buttonAddGrade = itemView.findViewById(R.id.buttonAddGrade);
            buttonAddAbsence = itemView.findViewById(R.id.buttonAddAbsence);
        }

        public void bind(Student student, OnStudentActionListener listener) {
            textViewStudentName.setText(student.getFullName());
            textViewApogee.setText("Apogée: " + student.getApogeeNumber());

            // TODO: Load and display average grade for this module
            textViewAverageGrade.setVisibility(View.GONE);

            // Button listeners
            buttonAddGrade.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onAddGradeClick(student);
                }
            });

            buttonAddAbsence.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onAddAbsenceClick(student);
                }
            });
        }
    }
}
