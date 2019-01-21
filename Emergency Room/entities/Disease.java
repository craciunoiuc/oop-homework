package entities;

import enums.IllnessType;

/**
 * Contine informatia in legatura cu boala unui pacient.
 *
 * @author Craciunoiu Cezar
 */
public final class Disease {
    private IllnessType illnessName;
    private Integer severity;

    public Integer getSeverity() {
        return severity;
    }

    public IllnessType getIllnessName() {
        return illnessName;
    }

    public void setIllnessName(IllnessType illnessName) {
        this.illnessName = illnessName;
    }

    public void setSeverity(Integer severity) {
        this.severity = severity;
    }
}
