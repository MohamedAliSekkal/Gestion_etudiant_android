package ma.ensa.mobile.studentmanagement.ui.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import ma.ensa.mobile.studentmanagement.R;
import ma.ensa.mobile.studentmanagement.data.local.entity.Student;

public class StudentAdapter extends ListAdapter<Student, StudentAdapter.StudentViewHolder> {

    private OnItemClickListener listener;

    public StudentAdapter(OnItemClickListener listener) {
        super(DIFF_CALLBACK);
        this.listener = listener;
    }

    private static final DiffUtil.ItemCallback<Student> DIFF_CALLBACK =
            new DiffUtil.ItemCallback<Student>() {
                @Override
                public boolean areItemsTheSame(@NonNull Student oldItem, @NonNull Student newItem) {
                    return oldItem.getStudentId() == newItem.getStudentId();
                }

                @Override
                public boolean areContentsTheSame(@NonNull Student oldItem, @NonNull Student newItem) {
                    return oldItem.getApogeeNumber().equals(newItem.getApogeeNumber()) &&
                           oldItem.getFullName().equals(newItem.getFullName()) &&
                           oldItem.getEmail().equals(newItem.getEmail());
                }
            };

    @NonNull
    @Override
    public StudentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_student, parent, false);
        return new StudentViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull StudentViewHolder holder, int position) {
        Student student = getItem(position);
        holder.bind(student);
    }

    class StudentViewHolder extends RecyclerView.ViewHolder {
        private TextView textViewAvatar;
        private TextView textViewName;
        private TextView textViewApogee;
        private TextView textViewEmail;

        public StudentViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewAvatar = itemView.findViewById(R.id.textViewAvatar);
            textViewName = itemView.findViewById(R.id.textViewName);
            textViewApogee = itemView.findViewById(R.id.textViewApogee);
            textViewEmail = itemView.findViewById(R.id.textViewEmail);

            itemView.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (listener != null && position != RecyclerView.NO_POSITION) {
                    listener.onItemClick(getItem(position));
                }
            });
        }

        public void bind(Student student) {
            // Set avatar initials
            String initials = getInitials(student.getFirstName(), student.getLastName());
            textViewAvatar.setText(initials);

            // Set name
            textViewName.setText(student.getFullName());

            // Set apogee
            textViewApogee.setText("Apogée: " + student.getApogeeNumber());

            // Set email
            textViewEmail.setText(student.getEmail());
        }

        private String getInitials(String firstName, String lastName) {
            String initials = "";
            if (firstName != null && !firstName.isEmpty()) {
                initials += firstName.charAt(0);
            }
            if (lastName != null && !lastName.isEmpty()) {
                initials += lastName.charAt(0);
            }
            return initials.toUpperCase();
        }
    }

    public interface OnItemClickListener {
        void onItemClick(Student student);
    }
}
