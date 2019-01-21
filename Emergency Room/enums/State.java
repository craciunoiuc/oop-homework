package enums;
/**
 * Starile de le pot avea persoanele in trecerea lor prin spital.
 *
 * @author Craciunoiu Cezar
 */
public enum State {
    TRIAGEQUEUE("in triage queue"),
    EXAMINATIONSQUEUE("in examinations queue"),
    INVESTIGATIONSQUEUE("in investigations queue"),
    HOSPITALIZED_CARDIO("hospitalized by cardiologist"),
    HOSPITALIZED_ERPHYSICIAN("hospitalized by erphysician"),
    HOSPITALIZED_GASTRO("hospitalized by gastroenterologist"),
    HOSPITALIZED_SURGEON("hospitalized by general surgeon"),
    HOSPITALIZED_INTERNIST("hospitalized by internist"),
    HOSPITALIZED_NEURO("hospitalized by neurologist"),
    OPERATED_CARDIO("operated by cardiologist"),
    OPERATED_ERPHYSICIAN("operated by erphysician"),
    OPERATED_SURGEON("operated by general surgeon"),
    OPERATED_NEURO("operated by neurologist"),
    HOME_CARDIO("sent home by cardiologist"),
    HOME_ERPHYSICIAN("sent home by erphysician"),
    HOME_GASTRO("sent home by gastroenterologist"),
    HOME_SURGEON("sent home by general surgeon"),
    HOME_INTERNIST("sent home by internist"),
    HOME_NEURO("sent home by neurologist"),
    HOME_DONE_TREATMENT("sent home after treatment"),
    OTHERHOSPITAL("transferred to other hospital"),
    NOT_DETERMINED("not determined");
    private String value;

    State(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
