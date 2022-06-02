package pl.kielce.tu.sandworm.core.model.enumeration;

public enum ThresholdType {

    PASS("pass"),
    THRESHOLD("threshold"),
    LIMIT("limit");

    private final String label;

    ThresholdType(String label) {
        this.label = label;
    }

    public static ThresholdType get(String label) {
        for (ThresholdType type : ThresholdType.values())
            if (type.label.equalsIgnoreCase(label)) {
                return type;
            }
        throw new IllegalArgumentException();
    }
}
