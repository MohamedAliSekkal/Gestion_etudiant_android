package ma.ensa.mobile.studentmanagement.ui.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import ma.ensa.mobile.studentmanagement.R;
import ma.ensa.mobile.studentmanagement.data.local.entity.Module;

/**
 * Adapter for displaying professor's modules
 */
public class ModuleAdapter extends RecyclerView.Adapter<ModuleAdapter.ModuleViewHolder> {

    private List<Module> modules;
    private OnModuleClickListener listener;

    public interface OnModuleClickListener {
        void onModuleClick(Module module);
    }

    public ModuleAdapter(OnModuleClickListener listener) {
        this.modules = new ArrayList<>();
        this.listener = listener;
    }

    public void setModules(List<Module> modules) {
        this.modules = modules != null ? modules : new ArrayList<>();
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ModuleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_module, parent, false);
        return new ModuleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ModuleViewHolder holder, int position) {
        Module module = modules.get(position);
        holder.bind(module, listener);
    }

    @Override
    public int getItemCount() {
        return modules.size();
    }

    static class ModuleViewHolder extends RecyclerView.ViewHolder {
        private TextView textViewModuleCode;
        private TextView textViewModuleName;
        private TextView textViewSemester;
        private TextView textViewCredits;
        private TextView textViewDescription;
        private TextView textViewHours;

        public ModuleViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewModuleCode = itemView.findViewById(R.id.textViewModuleCode);
            textViewModuleName = itemView.findViewById(R.id.textViewModuleName);
            textViewSemester = itemView.findViewById(R.id.textViewSemester);
            textViewCredits = itemView.findViewById(R.id.textViewCredits);
            textViewDescription = itemView.findViewById(R.id.textViewDescription);
            textViewHours = itemView.findViewById(R.id.textViewHours);
        }

        public void bind(Module module, OnModuleClickListener listener) {
            textViewModuleCode.setText(module.getModuleCode());
            textViewModuleName.setText(module.getModuleName());
            textViewSemester.setText(module.getSemester());
            textViewCredits.setText(module.getCredits() + " crédits");

            // Description
            if (module.getDescription() != null && !module.getDescription().isEmpty()) {
                textViewDescription.setText(module.getDescription());
                textViewDescription.setVisibility(View.VISIBLE);
            } else {
                textViewDescription.setVisibility(View.GONE);
            }

            // Hours breakdown
            StringBuilder hours = new StringBuilder();
            if (module.getHoursCourse() != null && module.getHoursCourse() > 0) {
                hours.append("Cours: ").append(module.getHoursCourse()).append("h");
            }
            if (module.getHoursTd() != null && module.getHoursTd() > 0) {
                if (hours.length() > 0) hours.append(" | ");
                hours.append("TD: ").append(module.getHoursTd()).append("h");
            }
            if (module.getHoursTp() != null && module.getHoursTp() > 0) {
                if (hours.length() > 0) hours.append(" | ");
                hours.append("TP: ").append(module.getHoursTp()).append("h");
            }
            textViewHours.setText(hours.toString());

            // Click listener
            itemView.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onModuleClick(module);
                }
            });
        }
    }
}
