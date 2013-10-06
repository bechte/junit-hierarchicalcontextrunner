package de.bechte.junit.runners.util;

import de.bechte.junit.stubs.Class1stLevel;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.Stack;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

public class ReflectionUtilTest {

    private Stack<Class<?>> stackForClasses(Class<?>... classes) {
        Stack<Class<?>> stack = new Stack<Class<?>>();
        for (Class<?> c : classes)
            stack.push(c);
        return stack;
    }

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void whenGetEnclosingInstanceIsCalledWithNull_anIllegalArgumentExceptionIsThrown() throws Exception {
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("Target must not be null!");
        ReflectionUtil.getEnclosingInstance(null);
    }

    @Test
    public void whenGetEnclosingInstanceIsCalledWithFirstLevelInstance_nullIsReturned() throws Exception {
        Object enclosingInstance = ReflectionUtil.getEnclosingInstance(new ReflectionUtilTest());
        assertThat(enclosingInstance, is(nullValue()));
    }

    @Test
    public void whenGetEnclosingInstanceIsCalledWith2ndLevelInstance_1stLevelInstanceIsReturned() throws Exception {
        Class1stLevel class1stLevel = new Class1stLevel();
        Class1stLevel.Class2ndLevel class2ndLevel = class1stLevel.new2ndLevel();

        Object enclosingInstance = ReflectionUtil.getEnclosingInstance(class2ndLevel);
        assertThat(enclosingInstance, is(equalTo((Object) class1stLevel)));
    }

    @Test
    public void whenGetEnclosingInstanceIsCalledWith3rdLevelInstance_2ndLevelInstanceIsReturned() throws Exception {
        Class1stLevel class1stLevel = new Class1stLevel();
        Class1stLevel.Class2ndLevel class2ndLevel = class1stLevel.new2ndLevel();
        Class1stLevel.Class2ndLevel.Class3rdLevel class3rdLevel = class2ndLevel.new3rdLevel();

        Object enclosingInstance = ReflectionUtil.getEnclosingInstance(class3rdLevel);
        assertThat(enclosingInstance, is(equalTo((Object) class2ndLevel)));
    }

    @Test
    public void whenGetEnclosingInstanceIsCalledWithStatic2ndLevelInstance_nullIsReturned() throws Exception {
        Object enclosingInstance = ReflectionUtil.getEnclosingInstance(new Class1stLevel.StaticClass2ndLevel());
        assertThat(enclosingInstance, is(nullValue()));
    }

    @Test
    public void whenGetClassHierarchyIsCalledWithNull_anEmptyStackIsReturned() throws Exception {
        Stack<Class<?>> emptyStack = stackForClasses();
        Stack<Class<?>> classHierarchy = ReflectionUtil.getClassHierarchy(null);
        assertThat(classHierarchy, is(equalTo(emptyStack)));
    }

    @Test
    public void whenGetClassHierarchyIsCalledWithFirstLevelClass_returnedStackOnlyContainsFirstLevelClass() throws Exception {
        Stack<Class<?>> expectedStack = stackForClasses(ReflectionUtilTest.class);
        Stack<Class<?>> classHierarchy = ReflectionUtil.getClassHierarchy(ReflectionUtilTest.class);
        assertThat(classHierarchy, is(equalTo(expectedStack)));
    }

    @Test
    public void whenGetClassHierarchyIsCalledWithMemberClass_returnedStackContainsMemberAndEnclosingClass() throws Exception {
        Stack<Class<?>> expectedStack = stackForClasses(Class1stLevel.Class2ndLevel.class, Class1stLevel.class);
        Stack<Class<?>> classHierarchy = ReflectionUtil.getClassHierarchy(Class1stLevel.Class2ndLevel.class);
        assertThat(classHierarchy, is(equalTo(expectedStack)));
    }

    @Test
    public void whenGetClassHierarchyIsCalledWithMember2Class_returnedStackContainsMember2AndAllEnclosingClasses() throws Exception {
        Stack<Class<?>> expectedStack = stackForClasses(Class1stLevel.Class2ndLevel.Class3rdLevel.class, Class1stLevel.Class2ndLevel.class, Class1stLevel.class);
        Stack<Class<?>> classHierarchy = ReflectionUtil.getClassHierarchy(Class1stLevel.Class2ndLevel.Class3rdLevel.class);
        assertThat(classHierarchy, is(equalTo(expectedStack)));
    }

    @Test
    public void whenGetClassHierarchyIsCalledWithStaticInnerClass_returnedStackOnlyContainsStaticInnerClass() throws Exception {
        Stack<Class<?>> expectedStack = stackForClasses(Class1stLevel.StaticClass2ndLevel.class);
        Stack<Class<?>> classHierarchy = ReflectionUtil.getClassHierarchy(Class1stLevel.StaticClass2ndLevel.class);
        assertThat(classHierarchy, is(equalTo(expectedStack)));
    }

    @Test
    public void whenCreateDeepInstanceIsCalledWithNull_anIllegalArgumentExceptionIsThrown() throws Throwable {
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("Stack must not be null or empty!");
        ReflectionUtil.createDeepInstance(null);
    }

    @Test
    public void whenCreateDeepInstanceIsCalledWithEmptyStack_anIllegalArgumentExceptionIsThrown() throws Throwable {
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("Stack must not be null or empty!");

        Stack<Class<?>> emptyStack = new Stack<Class<?>>();
        ReflectionUtil.createDeepInstance(emptyStack);
    }

    @Test
    public void whenCreateDeepInstanceIsCalledWithStackOfSize1_anInstanceOfThatObjectIsReturned() throws Throwable {
        Stack<Class<?>> classHierarchy = stackForClasses(Class1stLevel.class);

        Object instance = ReflectionUtil.createDeepInstance(classHierarchy);

        assertThat(instance, is(notNullValue()));
        assertThat(instance, is(instanceOf(Class1stLevel.class)));
    }

    @Test
    public void whenCreateDeepInstanceIsCalledWithStackOfSize2_anInstanceOfThatObjectIsReturned() throws Throwable {
        Stack<Class<?>> classHierarchy = stackForClasses(Class1stLevel.Class2ndLevel.class, Class1stLevel.class);

        Object instance = ReflectionUtil.createDeepInstance(classHierarchy);

        assertThat(instance, is(notNullValue()));
        assertThat(instance, is(instanceOf(Class1stLevel.Class2ndLevel.class)));
    }

    @Test
    public void whenCreateDeepInstanceIsCalledWithStackOfSize3_anInstanceOfThatObjectIsReturned() throws Throwable {
        Stack<Class<?>> classHierarchy = stackForClasses(Class1stLevel.Class2ndLevel.Class3rdLevel.class, Class1stLevel.Class2ndLevel.class, Class1stLevel.class);

        Object instance = ReflectionUtil.createDeepInstance(classHierarchy);

        assertThat(instance, is(notNullValue()));
        assertThat(instance, is(instanceOf(Class1stLevel.Class2ndLevel.Class3rdLevel.class)));
    }

    @Test
    public void whenCreateDeepInstanceIsCalledWithMemberClassOnly_anExceptionWillBeRaised() throws Throwable {
        thrown.expect(InstantiationException.class);

        Stack<Class<?>> classHierarchy = stackForClasses(Class1stLevel.Class2ndLevel.Class3rdLevel.class);
        Object instance = ReflectionUtil.createDeepInstance(classHierarchy);
    }

    @Test
    public void whenCreateDeepInstanceIsCalledWithInconsistentClassHierarchy_anExceptionWillBeRaised() throws Throwable {
        thrown.expect(NoSuchMethodException.class);

        Stack<Class<?>> classHierarchy = stackForClasses(Class1stLevel.Class2ndLevel.Class3rdLevel.class, Class1stLevel.class);
        Object instance = ReflectionUtil.createDeepInstance(classHierarchy);
    }
}