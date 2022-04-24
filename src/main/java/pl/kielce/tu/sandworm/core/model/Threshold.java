package pl.kielce.tu.sandworm.core.model;

import pl.kielce.tu.sandworm.core.model.enumeration.ThresholdType;

import java.util.Map;

import static pl.kielce.tu.sandworm.core.model.enumeration.ThresholdType.NONE;

public class Threshold {

    private static final String TYPE = "type";
    private static final String SECONDS = "seconds";
    private static final String KEY = "count";

    private final ThresholdType type;
    private int seconds;
    private int count;

    public Threshold() {
        type = NONE;
    }

    public Threshold(Map<String, String> settings) {
        type = ThresholdType.get(settings.get(TYPE));
        seconds = Integer.parseInt(settings.get(SECONDS));
        count = Integer.parseInt(settings.get(KEY));
    }

    public ThresholdType getType() {
        return type;
    }

    public int getSeconds() {
        return seconds;
    }

    public int getCount() {
        return count;
    }
}
