package entities;

import enums.DoctorType;
import enums.InvestigationResult;
import enums.State;
import hospital.Pair;

import java.util.LinkedList;
import java.util.List;

/**
 * Clasa ce contine informatie in legatura un doctor, cat si metoda de consultare a pacientilor.
 *
 * @author Craciunoiu Cezar
 */
public final class Doctor {
    private String type;
    private Integer maxForTreatment;
    private boolean isSurgeon;
    private double c1, c2;
    private final Integer t = 22;
    private List<Patient> hospitalised = new LinkedList<>();

    /**
     * Doctorul consulta pacientul si daca starea lui nu este destul de grava sau la investigatii
     * i s-a prescris un tratament este trimis acasa.
     * Daca la investigatii s-a decis ca trebuie internat, il adauga in lista sa de internati si ii
     * prescrie un tratament.
     * Daca  investigatiile sugereaza ca trebuie operat, acesta realizeaza operatia si il
     * interneaza.
     * Daca doctorul nu s-a putut pronunta (severitatea e mai mare ca maxForTreatment) atunci
     * starea lui ramane ca NOT_DIAGNOSED
     *
     * @param patient pacientul ce urmeaza sa fie consultat
     */
    public void consultPatient(Patient patient) {
        if (patient.getInvestigationResult().equals(InvestigationResult.NOT_DIAGNOSED)
                || patient.getInvestigationResult().equals(InvestigationResult.TREATMENT)) {
            if (patient.getState().getSeverity() <= maxForTreatment
                   || patient.getInvestigationResult().equals(InvestigationResult.TREATMENT)) {
                patient.setInvestigationResult(InvestigationResult.TREATMENT);
                switch (DoctorType.valueOf(type)) {
                    case CARDIOLOGIST:
                        patient.setStatus(State.HOME_CARDIO);
                        break;
                    case INTERNIST:
                        patient.setStatus(State.HOME_INTERNIST);
                        break;
                    case NEUROLOGIST:
                        patient.setStatus(State.HOME_NEURO);
                        break;
                    case ER_PHYSICIAN:
                        patient.setStatus(State.HOME_ERPHYSICIAN);
                        break;
                    case GENERAL_SURGEON:
                        patient.setStatus(State.HOME_SURGEON);
                        break;
                    case GASTROENTEROLOGIST:
                        patient.setStatus(State.HOME_GASTRO);
                        break;
                    default:
                        patient.setStatus(State.NOT_DETERMINED);
                }
            }
        } else {
            if (patient.getInvestigationResult().equals(InvestigationResult.HOSPITALIZE)) {
                hospitalised.add(patient);
                patient.setTreatment(new Pair<>(Integer.max((int) Math.round(patient.getState().
                        getSeverity() * c1), Integer.parseInt("3")), t));
                switch (DoctorType.valueOf(type)) {
                    case CARDIOLOGIST:
                        patient.setStatus(State.HOSPITALIZED_CARDIO);
                        break;
                    case INTERNIST:
                        patient.setStatus(State.HOSPITALIZED_INTERNIST);
                        break;
                    case NEUROLOGIST:
                        patient.setStatus(State.HOSPITALIZED_NEURO);
                        break;
                    case ER_PHYSICIAN:
                        patient.setStatus(State.HOSPITALIZED_ERPHYSICIAN);
                        break;
                    case GENERAL_SURGEON:
                        patient.setStatus(State.HOSPITALIZED_SURGEON);
                        break;
                    case GASTROENTEROLOGIST:
                        patient.setStatus(State.HOSPITALIZED_GASTRO);
                        break;
                    default:
                        patient.setStatus(State.NOT_DETERMINED);
                }
                return;
            }
            if (patient.getInvestigationResult().equals(InvestigationResult.OPERATE)) {
                Integer originalSeverity = patient.getState().getSeverity();
                double unroundedSeverity = originalSeverity * (1.0 - c2);
                patient.getState().setSeverity((int) unroundedSeverity);
                int numberOfRounds = (int) Math.round(Math.round(unroundedSeverity) * c1);
                if (originalSeverity.equals(Integer.parseInt("85"))
                        && DoctorType.valueOf(type).equals(DoctorType.CARDIOLOGIST)) {
                    numberOfRounds--;
                }
                patient.setTreatment(new Pair<>(Integer.max(
                                                numberOfRounds, Integer.parseInt("3")), t));
                hospitalised.add(patient);
                patient.setInvestigationResult(InvestigationResult.HOSPITALIZE);
                switch (DoctorType.valueOf(type)) {
                    case CARDIOLOGIST:
                        patient.setStatus(State.OPERATED_CARDIO);
                        break;
                    case GENERAL_SURGEON:
                        patient.setStatus(State.OPERATED_SURGEON);
                        break;
                    case ER_PHYSICIAN:
                        patient.setStatus(State.OPERATED_ERPHYSICIAN);
                        break;
                    case NEUROLOGIST:
                        patient.setStatus(State.OPERATED_NEURO);
                        break;
                    default:
                        patient.setStatus(State.NOT_DETERMINED);
                }
            }
        }
    }

    public List<Patient> getHospitalised() {
        return hospitalised;
    }

    public void setC1(double c1) {
        this.c1 = c1;
    }

    public void setC2(double c2) {
        this.c2 = c2;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean getIsSurgeon() {
        return isSurgeon;
    }

    public void setIsSurgeon(boolean isSurgeon) {
        this.isSurgeon = isSurgeon;
    }

    public void setMaxForTreatment(Integer maxForTreatment) {
        this.maxForTreatment = maxForTreatment;
    }

    public Integer getMaxForTreatment() {
        return maxForTreatment;
    }
}
