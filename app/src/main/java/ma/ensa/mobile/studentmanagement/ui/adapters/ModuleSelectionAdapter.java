package ma.ensa.mobile.studentmanagement.ui.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import ma.ensa.mobile.studentmanagement.R;
import ma.ensa.mobile.studentmanagement.data.local.entity.Module;

public class ModuleSelectionAdapter extends RecyclerView.Adapter<ModuleSelectionAdapter.ViewHolder> {

    private List<Module> modules = new ArrayList<>();
    private Set<Integer> selectedModuleIds = new HashSet<>();

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_module_checkbox, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Module module = modules.get(position);
        holder.bind(module);
    }

    @Override
    public int getItemCount() {
        return modules.size();
    }

    public void setModules(List<Module> modules) {
        this.modules = modules != null ? modules : new ArrayList<>();
        notifyDataSetChanged();
    }

    public void setSelectedModules(List<Integer> selectedIds) {
        selectedModuleIds.clear();
        if (selectedIds != null) {
            selectedModuleIds.addAll(selectedIds);
        }
        notifyDataSetChanged();
    }

    public List<Integer> getSelectedModuleIds() {
        return new ArrayList<>(selectedModuleIds);
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private CheckBox checkBoxModule;
        private TextView textViewModuleCode;
        private TextView textViewModuleName;
        private TextView textViewModuleInfo;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            checkBoxModule = itemView.findViewById(R.id.checkBoxModule);
            textViewModuleCode = itemView.findViewById(R.id.textViewModuleCode);
            textViewModuleName = itemView.findViewById(R.id.textViewModuleName);
            textViewModuleInfo = itemView.findViewById(R.id.textViewModuleInfo);

            // Handle checkbox clicks
            checkBoxModule.setOnCheckedChangeListener((buttonView, isChecked) -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    Module module = modules.get(position);
                    if (isChecked) {
                        selectedModuleIds.add(module.getModuleId());
                    } else {
                        selectedModuleIds.remove(module.getModuleId());
                    }
                }
            });

            // Handle item clicks
            itemView.setOnClickListener(v -> {
                checkBoxModule.setChecked(!checkBoxModule.isChecked());
            });
        }

        public void bind(Module module) {
            textViewModuleCode.setText(module.getModuleCode());
            textViewModuleName.setText(module.getModuleName());

            // Build info string
            StringBuilder info = new StringBuilder();
            info.append(module.getSemester());
            if (module.getCredits() != null) {
                info.append(" | ").append(module.getCredits()).append(" crédits");
            }
            textViewModuleInfo.setText(info.toString());

            // Set checkbox state
            checkBoxModule.setChecked(selectedModuleIds.contains(module.getModuleId()));
        }
    }
}
