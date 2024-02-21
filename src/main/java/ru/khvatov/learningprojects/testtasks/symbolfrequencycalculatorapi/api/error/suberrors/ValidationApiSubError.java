package ru.khvatov.learningprojects.testtasks.symbolfrequencycalculatorapi.api.error.suberrors;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@EqualsAndHashCode(callSuper = false)
@Getter
@Setter
@AllArgsConstructor
public class ValidationApiSubError extends ApiSubError {
    private final String object;
    private String field;
    private Object rejectedValue;
    private final String message;

    public ValidationApiSubError(String object, String message) {
        this.object = object;
        this.message = message;
    }

    public static class Builder {
        private String object;
        private String field;
        private Object rejectedValue;
        private String message;

        public Builder(final String object, final String message) {
            this.object = object;
            this.message = message;
        }

        public Builder field(final String field) {
            this.field = field;
            return this;
        }

        public Builder rejectedValue(final Object rejectedValue) {
            this.rejectedValue = rejectedValue;
            return this;
        }

        public ValidationApiSubError build() {
            return new ValidationApiSubError(object, field, rejectedValue, message);
        }
    }

}
