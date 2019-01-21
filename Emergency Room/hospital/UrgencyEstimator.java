package hospital;

import java.util.HashMap;
import java.util.Map;
import enums.IllnessType;
import enums.Urgency;

/**
 * Estimates the urgency based on the patient's illness and how severe the illness is manifested.
 */
final class UrgencyEstimator {

    private static UrgencyEstimator instance;
    private final Map<Urgency, HashMap<IllnessType, Integer>> algorithm;

    private UrgencyEstimator() {
        algorithm = new HashMap<Urgency, HashMap<IllnessType, Integer>>() {
            {
                put(Urgency.IMMEDIATE,
                        new HashMap<IllnessType, Integer>() {
                            {
                                put(IllnessType.ABDOMINAL_PAIN, Integer.parseInt("60"));
                                put(IllnessType.ALLERGIC_REACTION, Integer.parseInt("50"));
                                put(IllnessType.BROKEN_BONES, Integer.parseInt("80"));
                                put(IllnessType.BURNS, Integer.parseInt("40"));
                                put(IllnessType.CAR_ACCIDENT, Integer.parseInt("30"));
                                put(IllnessType.CUTS, Integer.parseInt("50"));
                                put(IllnessType.FOOD_POISONING, Integer.parseInt("50"));
                                put(IllnessType.HEART_ATTACK, Integer.parseInt("0"));
                                put(IllnessType.HEART_DISEASE, Integer.parseInt("40"));
                                put(IllnessType.HIGH_FEVER, Integer.parseInt("70"));
                                put(IllnessType.PNEUMONIA, Integer.parseInt("80"));
                                put(IllnessType.SPORT_INJURIES, Integer.parseInt("70"));
                                put(IllnessType.STROKE, Integer.parseInt("0"));

                            }
                        });

                put(Urgency.URGENT,
                        new HashMap<IllnessType, Integer>() {
                            {
                                put(IllnessType.ABDOMINAL_PAIN, Integer.parseInt("40"));
                                put(IllnessType.ALLERGIC_REACTION, Integer.parseInt("30"));
                                put(IllnessType.BROKEN_BONES, Integer.parseInt("50"));
                                put(IllnessType.BURNS, Integer.parseInt("20"));
                                put(IllnessType.CAR_ACCIDENT, Integer.parseInt("20"));
                                put(IllnessType.CUTS, Integer.parseInt("30"));
                                put(IllnessType.HEART_ATTACK, Integer.parseInt("0"));
                                put(IllnessType.FOOD_POISONING, Integer.parseInt("20"));
                                put(IllnessType.HEART_DISEASE, Integer.parseInt("20"));
                                put(IllnessType.HIGH_FEVER, Integer.parseInt("40"));
                                put(IllnessType.PNEUMONIA, Integer.parseInt("50"));
                                put(IllnessType.SPORT_INJURIES, Integer.parseInt("50"));
                                put(IllnessType.STROKE, Integer.parseInt("0"));
                            }
                        });

                put(Urgency.LESS_URGENT,
                        new HashMap<IllnessType, Integer>() {
                            {
                                put(IllnessType.ABDOMINAL_PAIN, Integer.parseInt("10"));
                                put(IllnessType.ALLERGIC_REACTION, Integer.parseInt("10"));
                                put(IllnessType.BROKEN_BONES, Integer.parseInt("20"));
                                put(IllnessType.BURNS, Integer.parseInt("10"));
                                put(IllnessType.CAR_ACCIDENT, Integer.parseInt("10"));
                                put(IllnessType.CUTS, Integer.parseInt("10"));
                                put(IllnessType.FOOD_POISONING, Integer.parseInt("0"));
                                put(IllnessType.HEART_ATTACK, Integer.parseInt("0"));
                                put(IllnessType.HEART_DISEASE, Integer.parseInt("10"));
                                put(IllnessType.HIGH_FEVER, Integer.parseInt("0"));
                                put(IllnessType.PNEUMONIA, Integer.parseInt("10"));
                                put(IllnessType.SPORT_INJURIES, Integer.parseInt("20"));
                                put(IllnessType.STROKE, Integer.parseInt("0"));
                            }
                        });

            }
        };
    }

    static UrgencyEstimator getInstance() {
        if (instance == null) {
            instance = new UrgencyEstimator();
        }
        return instance;
    }

    /**
     * Estimeaza urgenta unui pacient in functie de boala si severitate.
     *
     * @param illnessType un tip de boala
     * @param severity severitatea unui pacient
     * @return o urgenta
     */
    Urgency estimateUrgency(IllnessType illnessType, int severity) {

        if (severity >= algorithm.get(Urgency.IMMEDIATE).get(illnessType)) {
            return Urgency.IMMEDIATE;
        }
        if (severity >= algorithm.get(Urgency.URGENT).get(illnessType)) {
            return Urgency.URGENT;
        }
        if (severity >= algorithm.get(Urgency.LESS_URGENT).get(illnessType)) {
            return Urgency.LESS_URGENT;
        }
        return Urgency.NON_URGENT;
    }
}
