package ma.ensa.mobile.studentmanagement.ui.fragments.profile;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import ma.ensa.mobile.studentmanagement.R;
import ma.ensa.mobile.studentmanagement.data.local.entity.Student;
import ma.ensa.mobile.studentmanagement.utils.PreferencesManager;
import ma.ensa.mobile.studentmanagement.viewmodel.ProfileViewModel;

/**
 * Fragment B3: Student Profile
 * Displays student's personal and academic information
 */
public class ProfileFragment extends Fragment {

    private ProfileViewModel viewModel;
    private PreferencesManager preferencesManager;

    // Views
    private TextView textViewFullName;
    private TextView textViewApogee;
    private TextView textViewCNE;
    private TextView textViewEmail;
    private TextView textViewPhone;
    private TextView textViewDateOfBirth;
    private TextView textViewGender;
    private TextView textViewAddress;
    private TextView textViewStatus;
    private TextView textViewEnrollmentDate;
    private ImageView imageViewProfile;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        preferencesManager = new PreferencesManager(requireContext());

        // Initialize views
        initializeViews(view);

        // Initialize ViewModel
        viewModel = new ViewModelProvider(this).get(ProfileViewModel.class);

        // Load student profile
        loadProfile();
    }

    private void initializeViews(View view) {
        textViewFullName = view.findViewById(R.id.textViewFullName);
        textViewApogee = view.findViewById(R.id.textViewApogee);
        textViewCNE = view.findViewById(R.id.textViewCNE);
        textViewEmail = view.findViewById(R.id.textViewEmail);
        textViewPhone = view.findViewById(R.id.textViewPhone);
        textViewDateOfBirth = view.findViewById(R.id.textViewDateOfBirth);
        textViewGender = view.findViewById(R.id.textViewGender);
        textViewAddress = view.findViewById(R.id.textViewAddress);
        textViewStatus = view.findViewById(R.id.textViewStatus);
        textViewEnrollmentDate = view.findViewById(R.id.textViewEnrollmentDate);
        imageViewProfile = view.findViewById(R.id.imageViewProfile);
    }

    private void loadProfile() {
        // Get current user's username
        String username = preferencesManager.getUsername();

        if (username == null || username.isEmpty()) {
            Toast.makeText(requireContext(), "Erreur: Utilisateur non connecté", Toast.LENGTH_SHORT).show();
            return;
        }

        // Load student data by username
        viewModel.getStudentByUsername(username).observe(getViewLifecycleOwner(), student -> {
            if (student != null) {
                displayStudentInfo(student);
            } else {
                showNoProfileMessage();
            }
        });
    }

    private void displayStudentInfo(Student student) {
        // Display full name
        String fullName = student.getFirstName() + " " + student.getLastName();
        textViewFullName.setText(fullName);

        // Display Apogee number
        textViewApogee.setText("Apogée: " + student.getApogeeNumber());

        // Display CNE
        textViewCNE.setText(student.getCne());

        // Display email
        textViewEmail.setText(student.getEmail());

        // Display phone
        textViewPhone.setText(student.getPhone() != null ? student.getPhone() : "Non renseigné");

        // Display date of birth
        if (student.getDateOfBirth() != null) {
            String dateOfBirth = formatDate(student.getDateOfBirth());
            textViewDateOfBirth.setText(dateOfBirth);
        } else {
            textViewDateOfBirth.setText("Non renseigné");
        }

        // Display gender
        String gender = student.getGender();
        if (gender != null) {
            textViewGender.setText(gender.equals("M") ? "Masculin" : "Féminin");
        } else {
            textViewGender.setText("Non renseigné");
        }

        // Display address
        String address = student.getAddress();
        String city = student.getCity();
        String fullAddress = "";
        if (address != null && !address.isEmpty()) {
            fullAddress = address;
            if (city != null && !city.isEmpty()) {
                fullAddress += ", " + city;
            }
        } else if (city != null && !city.isEmpty()) {
            fullAddress = city;
        } else {
            fullAddress = "Non renseigné";
        }
        textViewAddress.setText(fullAddress);

        // Display status
        textViewStatus.setText(student.getStatus());

        // Display enrollment date
        if (student.getEnrollmentDate() != null) {
            String enrollmentDate = formatDate(student.getEnrollmentDate());
            textViewEnrollmentDate.setText(enrollmentDate);
        } else {
            textViewEnrollmentDate.setText("Non renseigné");
        }
    }

    private void showNoProfileMessage() {
        Toast.makeText(requireContext(),
            "Aucun profil étudiant trouvé pour cet utilisateur",
            Toast.LENGTH_LONG).show();

        textViewFullName.setText("Profil non disponible");
        textViewApogee.setText("Veuillez contacter l'administration");
    }

    private String formatDate(Long timestamp) {
        try {
            Date date = new Date(timestamp * 1000); // Convert seconds to milliseconds
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.FRANCE);
            return sdf.format(date);
        } catch (Exception e) {
            return "Non disponible";
        }
    }
}
