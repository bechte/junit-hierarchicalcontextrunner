package de.bechte.junit.stubs.validation;

public class ClassWithInvalidConstructorArguments {
    private boolean aField;

    public ClassWithInvalidConstructorArguments(boolean aField) {
        this.aField = aField;
    }

    public class MemberWithInvalidArguments {
        private boolean aField;

        public MemberWithInvalidArguments(boolean aField) {
            this.aField = aField;
        }
    }
}
