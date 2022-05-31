package pl.kielce.tu.sandworm.core.model;

import org.springframework.data.elasticsearch.annotations.Document;
import pl.kielce.tu.sandworm.core.model.enumeration.ThresholdType;

import java.util.Map;
import java.util.Objects;

import static pl.kielce.tu.sandworm.core.model.enumeration.ThresholdType.NONE;

public class Threshold {

    private static final String TYPE = "type";
    private static final String SECONDS = "seconds";
    private static final String KEY = "count";

    private ThresholdType type;
    private int seconds;
    private int count;

    public Threshold(ThresholdType type, int seconds, int count) {
        this.type = type;
        this.seconds = seconds;
        this.count = count;
    }

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Threshold threshold = (Threshold) o;
        return seconds == threshold.seconds && count == threshold.count && type == threshold.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, seconds, count);
    }

    public void setType(ThresholdType type) {
        this.type = type;
    }

    public void setSeconds(int seconds) {
        this.seconds = seconds;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
