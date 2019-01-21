package hospital;

import entities.Doctor;
import enums.DoctorType;
import enums.IllnessType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Contine metode statice pentru initializarea constatelor.
 *
 * @author Craciunoiu Cezar
 */
public final class HospitalBuilder {

    private HospitalBuilder() {

    }

    /**
     * Metoda face asocierea din enunt, coreland fiecare doctor cu afectiunile pe care le trateaza.
     *
     * @param doctorIllnessAssociation tabela de asociere Doctor-Boala
     * @return tabela ce a fost instantiata
     */
    public static HashMap<DoctorType, ArrayList<IllnessType>> instantiateDoctorIllnessAssociation(
                  HashMap<DoctorType, ArrayList<IllnessType>> doctorIllnessAssociation) {
        if (doctorIllnessAssociation == null) {
            doctorIllnessAssociation = new HashMap<>();
            ArrayList<IllnessType> aux = new ArrayList<>();

            //CARDIOLOGIST
            aux.add(IllnessType.HEART_ATTACK);
            aux.add(IllnessType.HEART_DISEASE);
            doctorIllnessAssociation.put(DoctorType.CARDIOLOGIST, aux);

            //ER_PHYSICIAN
            aux = new ArrayList<>();
            aux.add(IllnessType.ALLERGIC_REACTION);
            aux.add(IllnessType.BROKEN_BONES);
            aux.add(IllnessType.BURNS);
            aux.add(IllnessType.CAR_ACCIDENT);
            aux.add(IllnessType.CUTS);
            aux.add(IllnessType.HIGH_FEVER);
            aux.add(IllnessType.SPORT_INJURIES);
            doctorIllnessAssociation.put(DoctorType.ER_PHYSICIAN, aux);

            //GASTROENTEROLOGIST
            aux = new ArrayList<>();
            aux.add(IllnessType.ABDOMINAL_PAIN);
            aux.add(IllnessType.ALLERGIC_REACTION);
            aux.add(IllnessType.FOOD_POISONING);
            doctorIllnessAssociation.put(DoctorType.GASTROENTEROLOGIST, aux);

            //GENERAL SURGEON
            aux = new ArrayList<>();
            aux.add(IllnessType.ABDOMINAL_PAIN);
            aux.add(IllnessType.BURNS);
            aux.add(IllnessType.CAR_ACCIDENT);
            aux.add(IllnessType.CUTS);
            aux.add(IllnessType.SPORT_INJURIES);
            doctorIllnessAssociation.put(DoctorType.GENERAL_SURGEON, aux);

            //INTERNIST
            aux = new ArrayList<>();
            aux.add(IllnessType.ABDOMINAL_PAIN);
            aux.add(IllnessType.ALLERGIC_REACTION);
            aux.add(IllnessType.FOOD_POISONING);
            aux.add(IllnessType.HEART_DISEASE);
            aux.add(IllnessType.HIGH_FEVER);
            aux.add(IllnessType.PNEUMONIA);
            doctorIllnessAssociation.put(DoctorType.INTERNIST, aux);

            //NEUROLOGIST
            aux = new ArrayList<>();
            aux.add(IllnessType.STROKE);
            doctorIllnessAssociation.put(DoctorType.NEUROLOGIST, aux);
        }
        return doctorIllnessAssociation;
    }

    /**
     * Metoda initializeaza constantele cu datele din enunt si marcheaza toti General Surgeon ca
     * isSurgeon = true pentru a corecta eroarea din input.
     *
     * @param doctors lista de doctori ce are nevoie de constante
     */
    public static void setConstants(List<Doctor> doctors) {
        for (Doctor doctor : doctors) {
            switch (DoctorType.valueOf(doctor.getType())) {
                case CARDIOLOGIST:
                    doctor.setC1(Double.parseDouble("0.4"));
                    doctor.setC2(Double.parseDouble("0.1"));
                    continue;
                case ER_PHYSICIAN:
                    doctor.setC1(Double.parseDouble("0.1"));
                    doctor.setC2(Double.parseDouble("0.3"));
                    continue;
                case GASTROENTEROLOGIST:
                    doctor.setC1(Double.parseDouble("0.5"));
                    doctor.setC2(Double.parseDouble("0"));
                    continue;
                case GENERAL_SURGEON:
                    doctor.setC1(Double.parseDouble("0.2"));
                    doctor.setC2(Double.parseDouble("0.2"));
                    doctor.setIsSurgeon(true);
                    continue;
                case INTERNIST:
                    doctor.setC1(Double.parseDouble("0.01"));
                    doctor.setC2(Double.parseDouble("0"));
                    continue;
                case NEUROLOGIST:
                    doctor.setC1(Double.parseDouble("0.5"));
                    doctor.setC2(Double.parseDouble("0.1"));
                    continue;
                default:
                    break;
            }
        }
    }
}
