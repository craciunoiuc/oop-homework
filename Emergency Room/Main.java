import com.fasterxml.jackson.databind.ObjectMapper;
import hospital.Hospital;
import hospital.HospitalBuilder;
import observers.DoctorPrinter;
import observers.NursePrinter;
import observers.PatientPrinter;

import java.io.File;

/**
 * @author Craciunoiu Cezar
 */
public final class Main {

    private Main() {

    }

    public static void main(String[] args) {
        ObjectMapper objectMapper = new ObjectMapper();
        Hospital hospital = Hospital.getInstance();
        try {
            hospital = objectMapper.readValue(new File(args[0]), Hospital.class);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return;
        }
        int currentRound = 0;
        hospital.setDoctorIllnessAssociation(HospitalBuilder.instantiateDoctorIllnessAssociation(
                                             hospital.getDoctorIllnessAssociation()));
        HospitalBuilder.setConstants(hospital.getDoctors());
        hospital.fillAvailableDoctors();
        hospital.addObserver(new PatientPrinter());
        hospital.addObserver(new NursePrinter());
        hospital.addObserver(new DoctorPrinter());
        while (currentRound < hospital.getSimulationLength()) {
            System.out.println("~~~~ Patients in round " + (currentRound + 1) + " ~~~~");
            hospital.triageStage(currentRound);
            hospital.examinationStage();
            hospital.investigationStage();
            hospital.update("patient");
            hospital.treatPatients();
            hospital.update("nurse");
            hospital.update("doctor");
            hospital.inspectHospitalised();
            currentRound++;
        }
    }
}
