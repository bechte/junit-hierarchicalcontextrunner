package de.bechte.junit.runners.util.testclasses;

public class Class1stLevel {
    public static class StaticClass2ndLevel {}

    public class Class2ndLevel {
        public class Class3rdLevel {}

        public Class3rdLevel new3rdLevel() {
            return new Class3rdLevel();
        }
    }

    public Class2ndLevel new2ndLevel() {
        return new Class2ndLevel();
    }
}
