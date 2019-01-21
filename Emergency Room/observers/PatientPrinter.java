package observers;

import entities.Patient;
import enums.Urgency;
import hospital.Hospital;
import hospital.Pair;

import java.util.Comparator;
import java.util.List;
import java.util.Observer;
import java.util.Observable;
import java.util.ArrayList;


/**
 * Afiseaza informatia in legatura cu fiecare pacient care a venit pana in acel moment la spital.
 *
 * @author Craciunoiu Cezar
 */
public class PatientPrinter implements Observer {
    public final void update(Observable o, Object arg) {
        if (!arg.equals("patient")) {
            return;
        }
        Hospital hospital = (Hospital) o;
        List<Patient> patientList = new ArrayList<>();
        for (Pair<Urgency, Patient> elem : hospital.getTriageQueue()) {
            patientList.add(elem.getValue());
        }
        for (Pair<Urgency, Patient> elem : hospital.getExaminationQueue()) {
            patientList.add(elem.getValue());
        }
        for (Pair<Urgency, Patient> elem : hospital.getInvestigationQueue()) {
            patientList.add(elem.getValue());
        }
        for (Pair<Urgency, Patient> elem : hospital.getHospitalisedPatients()) {
            patientList.add(elem.getValue());
        }
        patientList.addAll(hospital.getTreatedPatients());
        patientList.sort(new Comparator<Patient>() {
            @Override
            public int compare(Patient o1, Patient o2) {
                if (o1 == o2) {
                    return 0;
                }
                return o1.getName().compareTo(o2.getName());
            }
        });
        for (Patient patient : patientList) {
            System.out.print(patient.getName() + " is ");
            System.out.println(patient.getStatus().getValue());
        }
        System.out.println();
    }
}
