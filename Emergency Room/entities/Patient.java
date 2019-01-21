package entities;

import enums.InvestigationResult;
import enums.State;
import enums.Urgency;
import hospital.Pair;

import java.util.Comparator;

/**
 * Contine informatia in legatura cu fiecare pacient cat si comaparatoare.
 *
 * @author Craciunoiu Cezar
 */
public final class Patient implements Comparable {
    private Integer id;
    private String name;
    private Integer age;
    private Integer time;
    private Disease state;
    private Pair<Integer, Integer> treatment;
    private State status = State.NOT_DETERMINED;
    private InvestigationResult investigationResult = InvestigationResult.NOT_DIAGNOSED;

    public State getStatus() {
        return status;
    }

    public void setStatus(State status) {
        this.status = status;
    }

    public Pair<Integer, Integer> getTreatment() {
        return treatment;
    }

    public void setTreatment(Pair<Integer, Integer> treatment) {
        this.treatment = treatment;
    }

    public InvestigationResult getInvestigationResult() {
        return investigationResult;
    }

    public void setInvestigationResult(InvestigationResult investigationResult) {
        this.investigationResult = investigationResult;
    }

    public Integer getAge() {
        return age;
    }

    public Disease getState() {
        return state;
    }

    public Integer getId() {
        return id;
    }

    public Integer getTime() {
        return time;
    }

    public String getName() {
        return name;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setTime(Integer time) {
        this.time = time;
    }

    public void setState(Disease state) {
        this.state = state;
    }

    /**
     * Folosit pentru compararea pacientilor.
     * @param o obiectul cu care se compara
     * @return 0 daca sunt egale, -1 daca primul e mai mare, 1 daca celalalt este
     */
    @Override
    public int compareTo(Object o) {
        if (o == this) {
            return 0;
        } else {
            return comparer((Patient) o);
        }
    }

    private int comparer(Patient patient) {
        if (this.getState().getSeverity() < patient.getState().getSeverity()) {
            return 1;
        } else {
            if (this.getState().getSeverity() > patient.getState().getSeverity()) {
                return -1;
            } else {
                return 0;
            }
        }
    }

    /**
     * Comparator ce ordoneaza mai intai dupa severitate si dupa aceea dupa nume.
     */
    private static Comparator<Patient> severityComparator = new Comparator<Patient>() {
        @Override
        public int compare(Patient o1, Patient o2) {
            if (o1 == o2) {
                return 0;
            } else {
                if (o1.getState().getSeverity() > o2.getState().getSeverity()) {
                    return 1;
                } else {
                    if (o1.getState().getSeverity() < o2.getState().getSeverity()) {
                        return -1;
                    } else {
                        return o1.getName().compareTo(o2.getName());
                    }
                }
            }
        }
    };

    /**
     * Comparator ce ordoneaza mai intai dupa urgenta, apoi dupa severitate si apoi dupa nume, in
     * ordine descrescatoare.
     */
    private static Comparator<Pair<Urgency, Patient>> urgencySeverityComparator =
                                                        new Comparator<Pair<Urgency, Patient>>() {
        @Override
        public int compare(Pair<Urgency, Patient> o1, Pair<Urgency, Patient> o2) {
            if (compareUrgency(o1, o2) == 0) {
                return -severityComparator.compare(o1.getValue(), o2.getValue());
            } else {
                return compareUrgency(o1, o2);
            }
        }

        private int compareUrgency(Pair<Urgency, Patient> o1, Pair<Urgency, Patient> o2) {
            if (o1.getKey().equals(o2.getKey())) {
                return 0;
            }
            if (o1.getKey().equals(Urgency.IMMEDIATE)) {
                return -1;
            }
            if (o1.getKey().equals((Urgency.URGENT)) && (o2.getKey().equals(Urgency.LESS_URGENT)
                    || o2.getKey().equals(Urgency.NON_URGENT))) {
                return -1;
            }
            if (o1.getKey().equals(Urgency.LESS_URGENT) && o2.getKey().equals(Urgency.NON_URGENT)) {
                return -1;
            }
            return 1;
        }
    };

    public static Comparator<Pair<Urgency, Patient>> getUrgencySeverityComparator() {
        return urgencySeverityComparator;
    }
}

