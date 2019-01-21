package observers;

import entities.Doctor;
import entities.Patient;
import enums.DoctorType;
import hospital.Hospital;

import java.util.Observable;
import java.util.Observer;

/**
 * Afiseaza informatiile rezultate in urma investigarii internatilor.
 *
 * @author Craciunoiu Cezar
 */
public class DoctorPrinter implements Observer {
    public final void update(Observable o, Object arg) {
        if (!arg.equals("doctor")) {
            return;
        }
        System.out.println("~~~~ Doctors check their hospitalized patients and give verdicts ~~~~");
        Hospital hospital = (Hospital) o;
        for (Doctor doctor : hospital.getDoctors()) {
            for (Patient patient : doctor.getHospitalised()) {
                if (patient.getTreatment().getKey() <= 0 || patient.getState().getSeverity() <= 0) {
                    System.out.println(DoctorType.valueOf(doctor.getType()).getValue() + " sent "
                            + patient.getName() + " home");
                } else {
                    System.out.println(DoctorType.valueOf(doctor.getType()).getValue()
                           + " says that " + patient.getName() + " should remain in hospital");
                }
            }
        }
        System.out.println();
    }
}
