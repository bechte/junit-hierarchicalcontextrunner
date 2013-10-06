package de.bechte.junit.stubs.validation;

public class ClassWithInvalidConstructorCount {
    private boolean aField;

    public ClassWithInvalidConstructorCount() {
    }

    public ClassWithInvalidConstructorCount(boolean aField) {
        this.aField = aField;
    }

    public class MemberWithInvalidConstructorCount {
        private boolean aField;

        public MemberWithInvalidConstructorCount() {
        }

        public MemberWithInvalidConstructorCount(boolean aField) {
            this.aField = aField;
        }
    }
}
