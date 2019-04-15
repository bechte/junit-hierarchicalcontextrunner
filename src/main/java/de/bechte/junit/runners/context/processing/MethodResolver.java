package de.bechte.junit.runners.context.processing;

import org.junit.Test;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.TestClass;

import java.util.Collections;
import java.util.List;

/**
 * A {@link MethodResolver} is responsible for resolving all tests for the given {@link TestClass}.
 *
 * A test of a class A can be defined by declaring a public method. Example:
 *
 * <pre>
 *   public class A {
 *     &#064;Before public void setUp() {...}
 *     &#064;After public void tearDown() {...}
 *
 *     &#064;Test public void test1() {...}
 *     &#064;Test public void test2() {...}
 *     &#064;Test public void test3() {...}
 *
 *     public class B {
 *       &#064;Test public void test4() {...}
 *
 *       public class C {
 *         &#064;Test public void test5() {...}
 *       }
 *     }
 *
 *     public class D {
 *       &#064;Test public void test6() {...}
 *     }
 *   }
 * </pre>
 *
 * If the {@link #getChildren(TestClass)} method is called on the {@link TestClass} object of class A, the it will
 * return a {@link List} containing methods test1, test2 and test3. A call on the {@link TestClass} object of class B,
 * C or D will return a {@link List} containing only one method each, namely test4, test5 or test6.
 */
public class MethodResolver implements ChildResolver<FrameworkMethod> {
    public List<FrameworkMethod> getChildren(final TestClass testClass) {
        if (testClass == null)
            return Collections.emptyList();

        return testClass.getAnnotatedMethods(Test.class);
    }
}