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
import ma.ensa.mobile.studentmanagement.data.local.entity.Absence;

public class AbsenceAdapter extends RecyclerView.Adapter<AbsenceAdapter.AbsenceViewHolder> {

    private List<Absence> absences = new ArrayList<>();
    private SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.FRANCE);

    public void setAbsences(List<Absence> absences) {
        this.absences = absences != null ? absences : new ArrayList<>();
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public AbsenceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_absence, parent, false);
        return new AbsenceViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AbsenceViewHolder holder, int position) {
        Absence absence = absences.get(position);
        holder.bind(absence);
    }

    @Override
    public int getItemCount() {
        return absences.size();
    }

    class AbsenceViewHolder extends RecyclerView.ViewHolder {
        private MaterialCardView cardView;
        private TextView textViewDate;
        private TextView textViewType;
        private TextView textViewHours;
        private TextView textViewJustification;
        private View statusIndicator;

        public AbsenceViewHolder(@NonNull View itemView) {
            super(itemView);
            cardView = (MaterialCardView) itemView;
            textViewDate = itemView.findViewById(R.id.textViewAbsenceDate);
            textViewType = itemView.findViewById(R.id.textViewAbsenceType);
            textViewHours = itemView.findViewById(R.id.textViewAbsenceHours);
            textViewJustification = itemView.findViewById(R.id.textViewJustification);
            statusIndicator = itemView.findViewById(R.id.statusIndicator);
        }

        public void bind(Absence absence) {
            // Format date
            String dateStr = dateFormat.format(new Date(absence.getAbsenceDate() * 1000));
            textViewDate.setText(dateStr);

            // Display type
            boolean isJustified = absence.isJustified();
            textViewType.setText(isJustified ? "Justifiée" : "Non justifiée");

            // Display hours
            textViewHours.setText(absence.getHours() + "h");

            // Display justification if exists
            if (absence.getJustification() != null && !absence.getJustification().isEmpty()) {
                textViewJustification.setText(absence.getJustification());
                textViewJustification.setVisibility(View.VISIBLE);
            } else {
                textViewJustification.setVisibility(View.GONE);
            }

            // Set status indicator color
            int color = isJustified ?
                    itemView.getContext().getColor(R.color.success) :
                    itemView.getContext().getColor(R.color.error);
            statusIndicator.setBackgroundColor(color);
        }
    }
}
