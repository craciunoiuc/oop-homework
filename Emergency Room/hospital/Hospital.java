package hospital;

import entities.Doctor;
import entities.Patient;
import enums.DoctorType;
import enums.IllnessType;
import enums.InvestigationResult;
import enums.State;
import enums.Urgency;

import java.util.List;
import java.util.Observable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Comparator;

/**
 * Clasa unica unde se executa propriu-zis programul.
 *
 * @author Craciunoiu Cezar
 */
public final class Hospital extends Observable {
    private Integer simulationLength;
    private Integer nurses;
    private Integer investigators;
    private List<Doctor> doctors;
    private List<Patient> incomingPatients;
    private static Hospital hospital = null;
    private static UrgencyEstimator urgencyEstimator = UrgencyEstimator.getInstance();
    private HashMap<DoctorType, ArrayList<IllnessType>> doctorIllnessAssociation;
    private List<Pair<Urgency, Patient>> triageQueue = new ArrayList<>();
    private List<Pair<Urgency, Patient>> examinationQueue = new ArrayList<>();
    private List<Pair<Urgency, Patient>> investigationQueue = new ArrayList<>();
    private HashMap<IllnessType, List<Doctor>> availableDoctors = new HashMap<>();
    private List<Patient> treatedPatients = new ArrayList<>();
    private List<Pair<Urgency, Patient>> hospitalisedPatients = new ArrayList<>();

    private Hospital() {
    }

    /**
     * Implementare a designului Singleton, metoda intoarece o instanta unica a unui obiect
     * Hospital.
     * @return un obiect de tip Hospital
     */
    public static Hospital getInstance() {
        if (hospital == null) {
            hospital = new Hospital();
        }
        return hospital;
    }

    /**
     * Construieste cozile de doctori pentru fiecare tip de boala.
     */
    public void fillAvailableDoctors() {
        for (IllnessType illnessType : IllnessType.values()) {
            availableDoctors.put(illnessType, new LinkedList<>());
            for (Doctor doctor : doctors) {
                if (doctorIllnessAssociation.get(DoctorType.valueOf(
                                                 doctor.getType())).contains(illnessType)) {
                    availableDoctors.get(illnessType).add(doctor);
                }
            }
        }
    }

    /**
     * Metoda implementeaza prima etapa din executia programului - cea de triere. Mai intai se aleg
     * toti pacientii de la intrare daca timpul sosirii este mai mare ca runda curenta. Se sorteaza
     * noi venitii dupa severitate si apoi sunt preluati de asistente. Fiecare asistenta atribuie
     * un nivel de urgenta si adauga pacientul in coada de triere.
     *
     * @param currentRound intreg ce utilizat la alegerea pacientilor de la intrare
     */
    public void triageStage(int currentRound) {
        int nrNurses = nurses;
        ArrayList<Patient> sortedTriageQueue = new ArrayList<>();
        for (Patient patient : incomingPatients) {
            if (patient.getTime() > currentRound) {
                continue;
            }
                sortedTriageQueue.add(patient);
                patient.setStatus(State.TRIAGEQUEUE);
        }

        for (Patient patient : sortedTriageQueue) {
            incomingPatients.remove(patient);
        }
        while (!triageQueue.isEmpty()) {
            sortedTriageQueue.add(triageQueue.remove(0).getValue());
        }
        sortedTriageQueue.sort(new Comparator<Patient>() {
            @Override
            public int compare(Patient o1, Patient o2) {
                return -o1.getState().getSeverity().compareTo(o2.getState().getSeverity());
            }
        });
        for (Patient patient : sortedTriageQueue) {
            triageQueue.add(new Pair<>(Urgency.NOT_DETERMINED, patient));
        }
        while (nrNurses > 0 && !triageQueue.isEmpty()) {
            Pair<Urgency, Patient> aux = triageQueue.remove(0);
            Pair<Urgency, Patient> currentPatient = new Pair<>(urgencyEstimator.estimateUrgency(
                    aux.getValue().getState().getIllnessName(), aux.getValue().getState().
                            getSeverity()), aux.getValue());
            examinationQueue.add(currentPatient);
            currentPatient.getValue().setStatus(State.EXAMINATIONSQUEUE);
            nrNurses--;
        }
    }

    /**
     * Metoda implementeaza runda de examinare a pacientilor. Fiecare pacient are doua "stari":
     * nehoatarata (NOT_DETERMINED) sau hotarata (OPERATE, TREATMENT, HOSPITALIZE). Daca pacientul
     * trebuie operat se cauta primul doctor care este si chirurg, daca nu se gaseste este trimis
     * la alt spital, schimbandu-i-se status-ul si fiind trecut la pacienti eliberati. Daca a fost
     * operat, este si internat direct.
     * Daca se doreste doar internarea acestuia se alege primul medic disponibil care se ocupa de
     * acea afectiune.
     * Daca se este necesar doar un tratament, pacientul este trimis acasa.
     * Daca starea acestuia este nehotarata, doctorul incearca sa se pronunte si poate alege daca
     * sa il trimita acasa sau sa il trimita la investigatii.
     */
    public void examinationStage() {
        Doctor doctorInCharge;
        List<Pair<Urgency, Patient>> transfered = new ArrayList<>();
        examinationQueue.sort(Patient.getUrgencySeverityComparator());
        for (Pair<Urgency, Patient> patient : examinationQueue) {
            if (patient.getValue().getInvestigationResult().equals(InvestigationResult.OPERATE)) {
                int surgeonFound = 0;
                for (Doctor doctor : availableDoctors.get(patient.getValue().getState().
                                                                                getIllnessName())) {
                    if (doctor.getIsSurgeon()) {
                        break;
                    } else {
                        surgeonFound++;
                    }
                }
                if (surgeonFound == availableDoctors.get(patient.getValue().getState().
                                                                        getIllnessName()).size()) {
                    patient.getValue().setStatus(State.OTHERHOSPITAL);
                    treatedPatients.add(patient.getValue());
                    transfered.add(patient);
                    continue;
                } else {
                    doctorInCharge = availableDoctors.get(
                            patient.getValue().getState().getIllnessName()).remove(surgeonFound);
                    availableDoctors.get(patient.getValue().getState().getIllnessName()).add(
                                                                                    doctorInCharge);
                    for (IllnessType illnessType : IllnessType.values()) {
                        if (illnessType.equals(patient.getValue().getState().getIllnessName())) {
                            continue;
                        }
                        for (int i = 0; i < availableDoctors.get(illnessType).size(); ++i) {
                            if (availableDoctors.get(illnessType).get(i).getType().equals(
                                    doctorInCharge.getType()) && availableDoctors.get(
                                            illnessType).get(i).getIsSurgeon()) {
                                availableDoctors.get(illnessType).add(availableDoctors.get(
                                                                            illnessType).remove(i));
                                break;
                            }
                        }
                    }
                }
            } else {
                doctorInCharge = availableDoctors.get(
                        patient.getValue().getState().getIllnessName()).remove(0);
                availableDoctors.get(patient.getValue().getState().getIllnessName()).add(
                                                                                    doctorInCharge);
                for (IllnessType illnessType : IllnessType.values()) {
                    if (illnessType.equals(patient.getValue().getState().getIllnessName())) {
                        continue;
                    }
                    for (int i = 0; i < availableDoctors.get(illnessType).size(); ++i) {
                        if (availableDoctors.get(illnessType).get(i).getType().equals(
                                                                        doctorInCharge.getType())) {
                            availableDoctors.get(illnessType).add(availableDoctors.get(
                                                                    illnessType).remove(i));
                            break;
                        }
                    }
                }
            }
            if (!patient.getValue().getStatus().equals(State.OTHERHOSPITAL)) {
                doctorInCharge.consultPatient(patient.getValue());
                if (patient.getValue().getInvestigationResult().equals(
                                                            InvestigationResult.HOSPITALIZE)) {
                    doctorInCharge.getHospitalised().sort(new Comparator<Patient>() {
                        @Override
                        public int compare(Patient o1, Patient o2) {
                            return o1.getName().compareTo(o2.getName());
                        }
                    });
                    hospitalisedPatients.add(patient);
                }
            }
        }
        for (Pair<Urgency, Patient> patient : transfered) {
            examinationQueue.remove(patient);
        }
        for (Pair<Urgency, Patient> patient : hospitalisedPatients) {
            examinationQueue.remove(patient);
        }
        for (Pair<Urgency, Patient> patient : examinationQueue) {
            if (patient.getValue().getInvestigationResult().equals(
                                                        InvestigationResult.NOT_DIAGNOSED)) {
                investigationQueue.add(patient);
            }
        }
        for (Pair<Urgency, Patient> patient : investigationQueue) {
            examinationQueue.remove(patient);
        }
        List<Pair<Urgency, Patient>> nonUrgentPatients = new ArrayList<>();
        for (Pair<Urgency, Patient> patient : examinationQueue) {
            if (patient.getValue().getInvestigationResult().equals(InvestigationResult.TREATMENT)) {
                treatedPatients.add(patient.getValue());
                nonUrgentPatients.add(patient);
            }
        }
        for (Pair<Urgency, Patient> patient : nonUrgentPatients) {
            examinationQueue.remove(patient);
        }
    }

    /**
     * Metoda simuleaza etapa de investigatii din enunt. Se actualizeaza status-ul pacientilor din
     * investigationQueue si se sorteaza dupa urgenta, severitate si nume. Se compara severitatea
     * fiecarui pacient cu constantele din enunt si se ofera o "parere" in legatura cu ce ar trebui
     * facut. Sunt investigati doar primii nrInvestigators pacienti.
     *
     */
    public void investigationStage() {
        int nrInvestigators = investigators;
        final int c1 = Integer.parseInt("75");
        final int c2 = Integer.parseInt("40");
        Pair<Urgency, Patient> currentPatient;
        for (Pair<Urgency, Patient> patient : investigationQueue) {
            patient.getValue().setStatus(State.INVESTIGATIONSQUEUE);
        }
        investigationQueue.sort(Patient.getUrgencySeverityComparator());
        while (!investigationQueue.isEmpty() && nrInvestigators > 0) {
            currentPatient = investigationQueue.remove(0);
            if (currentPatient.getValue().getState().getSeverity() > c1) {
                currentPatient.getValue().setInvestigationResult(InvestigationResult.OPERATE);
            } else {
                if (currentPatient.getValue().getState().getSeverity() <= c2) {
                    currentPatient.getValue().setInvestigationResult(InvestigationResult.TREATMENT);
                } else {
                    currentPatient.getValue().setInvestigationResult(
                            InvestigationResult.HOSPITALIZE);
                }
            }
            examinationQueue.add(currentPatient);
            currentPatient.getValue().setStatus(State.EXAMINATIONSQUEUE);
            nrInvestigators--;
        }
    }

    /**
     * Fiecarui pacient internat ii este adminisrat tratamentul stabilit.
     */
    public void treatPatients() {
        for (Doctor doctor : doctors) {
            for (Patient patient : doctor.getHospitalised()) {
                patient.setTreatment(new Pair<>(
                        patient.getTreatment().getKey() - 1, patient.getTreatment().getValue()));
                patient.getState().setSeverity(
                        patient.getState().getSeverity() - patient.getTreatment().getValue());
            }
        }
    }

    /**
     * Metoda care implementeaza etapa de inspectare a persoanelor internate. Se trece prin fiecare
     * persoana internata si se verifica daca s-a sfarsit timpul tratamentului sau starea
     * pacientului, adica severitatea, a sczut sub 0, adica pacientul este sanatos.
     */
    public void inspectHospitalised() {
        for (Doctor doctor : doctors) {
            List<Patient> patientsToBeReleased = new ArrayList<>();
            for (Patient patient : doctor.getHospitalised()) {
                if (patient.getTreatment().getKey() <= 0 || patient.getState().getSeverity() <= 0) {
                    patientsToBeReleased.add(patient);
                }
            }
            for (Patient patient : patientsToBeReleased) {
                doctor.getHospitalised().remove(patient);
                for (int i = 0; i < hospitalisedPatients.size(); ++i) {
                    if (hospitalisedPatients.get(i).getValue().equals(patient)) {
                        hospitalisedPatients.remove(i);
                        break;
                    }
                }
                patient.setStatus(State.HOME_DONE_TREATMENT);
                treatedPatients.add(patient);
            }
        }
    }

    /**
     * Cand este apelata metoda se afiseaza informatie la output folosind observatorii.
     *
     * @param s in functie de acest argument se folosesc diferiti observatori
     */
    public void update(String s) {

        this.setChanged();
        this.notifyObservers(s);
    }

    public HashMap<DoctorType, ArrayList<IllnessType>> getDoctorIllnessAssociation() {
        return doctorIllnessAssociation;
    }

    public void setDoctorIllnessAssociation(HashMap<DoctorType, ArrayList<IllnessType>>
                                                    doctorIllnessAssociation) {
        this.doctorIllnessAssociation = doctorIllnessAssociation;
    }

    public List<Pair<Urgency, Patient>> getHospitalisedPatients() {
        return hospitalisedPatients;
    }

    public List<Patient> getTreatedPatients() {
        return treatedPatients;
    }

    public List<Pair<Urgency, Patient>> getInvestigationQueue() {
        return investigationQueue;
    }

    public List<Pair<Urgency, Patient>> getExaminationQueue() {
        return examinationQueue;
    }

    public List<Pair<Urgency, Patient>> getTriageQueue() {
        return triageQueue;
    }

    public List<Doctor> getDoctors() {
        return doctors;
    }

    public void setDoctors(List<Doctor> doctors) {
        this.doctors = doctors;
    }

    public void setIncomingPatients(List<Patient> incomingPatients) {
        this.incomingPatients = incomingPatients;
    }

    public Integer getSimulationLength() {
        return simulationLength;
    }

    public List<Patient> getIncomingPatients() {
        return incomingPatients;
    }

    public void setSimulationLength(Integer simulationLength) {
        this.simulationLength = simulationLength;
    }

    public Integer getNurses() {
        return nurses;
    }

    public Integer getInvestigators() {
        return investigators;
    }

    public void setInvestigators(Integer investigators) {
        this.investigators = investigators;
    }

    public void setNurses(Integer nurses) {
        this.nurses = nurses;
    }
}
