package de.bechte.junit.runners.context.processing;

import org.junit.Ignore;
import org.junit.runners.model.TestClass;

import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * A {@link ContextResolver} is responsible for resolving all sub-contexts for the given {@link TestClass}.
 *
 * A sub-context of a class A can be defined by declaring a public inner class B of A. Example:
 *
 * <code>
 *   public class A {
 *     @Before public void setUp() {...}
 *     @After public void tearDown() {...}
 *
 *     @Test public void test1() {...}
 *     @Test public void test2() {...}
 *     @Test public void test3() {...}
 *
 *     public class B {
 *       @Test public void test4() {...}
 *
 *       public class C {
 *         @Test public void test5() {...}
 *       }
 *     }
 *
 *     public class D {
 *       @Test public void test6() {...}
 *     }
 *   }
 * </code>
 *
 * If the {@link #getChildren(TestClass)} method is called on the {@link TestClass} object of class A, the it will
 * return a {@link List} containing classes B and D, which are both a sub-context. A call on the {@link TestClass}
 * object of class B will return a {@link List} containing class C, only, while a call on C or D will return an empty
 * {@link List}.
 *
 * This implementation only covers public, non-static member classes of the {@link TestClass}. Therefore, inner tests
 * classes that are static or not public will be ignored, and tests in these classes will not get executed during the
 * test run. The main purpose of static inner classes should be to create stubs and helpers to support the tests, not
 * for creating tests.
 *
 * Note: This behavior can be changed by overriding the {@link ContextResolver} and by providing a new implementation
 * of the method {@link #isAllowed(Class)}.
 */
public class ContextResolver implements ChildResolver<Class<?>> {
    public List<Class<?>> getChildren(final TestClass testClass) {
        if (testClass == null)
            return Collections.emptyList();

        final Class<?>[] memberClasses = testClass.getJavaClass().getClasses();
        final List<Class<?>> contexts = new ArrayList<Class<?>>(memberClasses.length);
        for (final Class<?> memberClass : memberClasses)
            if (isAllowed(memberClass))
                contexts.add(memberClass);
        return contexts;
    }

    /**
     * Verifies, if the given {@code candidate} is allowed. If this method return {@code false} the candidate will be
     * removed from the list of children returned by {@link #getChildren(org.junit.runners.model.TestClass)}.
     *
     * Note: Clients may override this method to provide custom filtering.
     *
     * @param candidate the candidate to verify
     * @return a boolean value
     */
    protected boolean isAllowed(final Class<?> candidate) {
        return isNotAbstract(candidate) && isNotStatic(candidate) && isPublic(candidate) && isNotIgnored(candidate);
    }

    private boolean isNotAbstract(Class<?> candidate) {
        return !Modifier.isAbstract(candidate.getModifiers());
    }

    private boolean isNotStatic(Class<?> candidate) {
        return !Modifier.isStatic(candidate.getModifiers());
    }

    private boolean isPublic(Class<?> candidate) {
        return Modifier.isPublic(candidate.getModifiers());
    }

    private boolean isNotIgnored(Class<?> candidate) {
        return candidate.getAnnotation(Ignore.class) == null;
    }
}