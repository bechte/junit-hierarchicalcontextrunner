package de.bechte.junit.stubs;

public class Class1stLevel {
    public static class StaticClass2ndLevel {}

    private class Private2ndLevelClass {}
    class PackagePrivate2ndLevelClass {}
    protected class Protected2ndLevelClass {}

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
