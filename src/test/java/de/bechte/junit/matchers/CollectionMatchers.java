package de.bechte.junit.matchers;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

import java.util.*;

/**
 * This utility class contains several matchers that are not present in hamcrest-1.1.
 * The implementation is not exactly the same but a minimal one to perform the required tests.
 */
public class CollectionMatchers {
    private CollectionMatchers() { }

    public static <T> Matcher<Collection<? extends T>> empty() {
        return new TypeSafeMatcher<Collection<? extends T>>() {
            @Override
            public boolean matchesSafely(Collection<? extends T> items) {
                return items.isEmpty();
            }

            public void describeTo(Description description) {
                description.appendText("should be empty");
            }
        };
    }

    public static <T> Matcher<List<? extends T>> hasSize(final int expectedSize) {
        return new TypeSafeMatcher<List<? extends T>>() {
            @Override
            public boolean matchesSafely(List<? extends T> items) {
                return items.size() == expectedSize;
            }

            public void describeTo(Description description) {
                description //
                        .appendText("should have size of: ") //
                        .appendValue(expectedSize);
            }
        };
    }

    public static <E> Matcher<Collection<E>> emptyCollectionOf(
            @SuppressWarnings("unused") Class<E> unusedToForceReturnType) {
        //noinspection unchecked
        return (Matcher)empty();
    }

    public static <T> Matcher<Iterable<? extends T>> containsInAnyOrder(final T... expectedTs) {
        return new TypeSafeMatcher<Iterable<? extends T>>() {
            @Override
            public boolean matchesSafely(Iterable<? extends T> ts) {
                Set<T> tsSet = new HashSet<T>();

                for (T t : ts) {
                    tsSet.add(t);
                }

                return tsSet.containsAll(Arrays.asList(expectedTs));
            }

            public void describeTo(Description description) {
                description //
                        .appendText("should contains all of: ") //
                        .appendValue(expectedTs) //
                        .appendText(" without specific order");
            }
        };
    }
}
