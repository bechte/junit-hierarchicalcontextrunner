package de.bechte.junit.stubs.validation;

public class StaticClassWithInvalidConstructor {
    public static class PrivateAccessor {
        private PrivateAccessor() {
        }
    }

    public static class InvalidCount {
        private boolean aField;

        public InvalidCount(boolean aField) {
            this.aField = aField;
        }
    }
}
