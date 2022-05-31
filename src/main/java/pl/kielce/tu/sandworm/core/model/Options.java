package pl.kielce.tu.sandworm.core.model;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;

import static org.apache.commons.lang3.StringUtils.SPACE;

public class Options {

    private static final String COMMA = ",";
    private static final String SID = "sid";
    private static final String REV = "rev";
    private static final String MSG = "msg";
    private static final String THRESHOLD = "threshold";
    private static final String CONTENT = "content";

    private String id;
    private int revision;
    private String message;
    private Threshold threshold;
    private Set<PayloadPattern> payloadMatchers;


    public Options(Set<PayloadPattern> options) {
        this.payloadMatchers = new HashSet<>();
        options.forEach(option -> {
            switch (option.name()) {
                case SID -> id = option.value();
                case REV -> revision = Integer.parseInt(option.value());
                case MSG -> message = option.value();
                case THRESHOLD -> threshold = getThreshold(option.value());
                case CONTENT -> payloadMatchers.add(option);
                default -> throw new IllegalStateException("Unexpected value: " + option.name());
                //todo maybe throw custom one
            }
        });
    }

    private Threshold getThreshold(String thresholdOption) {
        Map<String, String> settings = transferToMap(thresholdOption);
        return new Threshold(settings);
    }

    private Map<String, String> transferToMap(String thresholdSettings) {
        Map<String, String> map = new HashMap<>();
        Stream.of(thresholdSettings.split(COMMA))
                .map(String::trim)
                .map(setting -> setting.split(SPACE))
                .forEach(array -> map.put(array[0], array[1]));
        return map;
    }

    public String getId() {
        return id;
    }

    public int getRevision() {
        return revision;
    }

    public String getMessage() {
        return message;
    }

    public Threshold getThreshold() {
        return threshold;
    }

    public Set<PayloadPattern> getPayloadMatchers() {
        return payloadMatchers;
    }
}
