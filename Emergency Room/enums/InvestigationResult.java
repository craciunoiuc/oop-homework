package enums;

/**
 * Decizia luata de un doctor sau investigator in legatura cu starea unui pacient.
 *
 * @author Craciunoiu Cezar
 */
public enum InvestigationResult {

    OPERATE("operate"),
    HOSPITALIZE("hospitalize"),
    TREATMENT("treatment"),
    NOT_DIAGNOSED("not diagnosed");
    private String value;

    InvestigationResult(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
