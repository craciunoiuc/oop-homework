package observers;

import entities.Doctor;
import entities.Patient;
import hospital.Hospital;

import java.util.Comparator;
import java.util.List;
import java.util.Observer;
import java.util.Observable;
import java.util.ArrayList;

/**
 * Afiseaza informatia in legatura cu fiecare pacient ce a fost tratat de asistente.
 *
 * @author Craciunoiu Cezar
 */
public class NursePrinter implements Observer {
    public final void update(Observable o, Object arg) {
        if (!arg.equals("nurse")) {
            return;
        }
        System.out.println("~~~~ Nurses treat patients ~~~~");
        Hospital hospital = (Hospital) o;
        List<Patient> patientList = new ArrayList<>();
        for (Doctor doctor : hospital.getDoctors()) {
            patientList.addAll(doctor.getHospitalised());
        }
        patientList.sort(new Comparator<Patient>() {
            @Override
            public int compare(Patient o1, Patient o2) {
                if (o1 == o2) {
                    return 0;
                }
                return o1.getName().compareTo(o2.getName());
            }
        });

        int nurseNr = 0;
        for (Patient patient : patientList) {
            System.out.print("Nurse " + nurseNr + " treated " + patient.getName()
                    + " and patient has " + patient.getTreatment().getKey() + " more round");
            if (patient.getTreatment().getKey() == 1) {
                System.out.println();
            } else {
                System.out.println("s");
            }
            nurseNr = (nurseNr + 1) % hospital.getNurses();
        }
        System.out.println();
    }
}
