package com.spring_api.entity;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class StudentTest {

    @Test
    void testGetterAndSetterMethods() {
        Student student = new Student();
        student.setStudentId(1L);
        student.setFirstName("John");
        student.setLastName("Doe");
        student.setEmailId("john.doe@example.com");
        Guardian guardian = new Guardian();
        guardian.setName("Jane Doe");
        guardian.setEmail("jane.doe@example.com");
        student.setGuardian(guardian);

        assertEquals(1L, student.getStudentId());
        assertEquals("John", student.getFirstName());
        assertEquals("Doe", student.getLastName());
        assertEquals("john.doe@example.com", student.getEmailId());
        assertEquals(guardian, student.getGuardian());
    }

    @Test
    void testEqualsAndHashCodeMethods() {
        Student student1 = new Student(1L, "John", "Doe", "john.doe@example.com", new Guardian());
        Student student2 = new Student(1L, "John", "Doe", "john.doe@example.com", new Guardian());
        Student student3 = new Student(2L, "Jane", "Doe", "jane.doe@example.com", new Guardian());

        assertTrue(student1.equals(student2) && student2.equals(student1));
        assertEquals(student1.hashCode(), student2.hashCode());

        assertFalse(student1.equals(student3) || student3.equals(student1));
    }

    @Test
    void testToStringMethod() {
        Student student = new Student(1L, "John", "Doe", "john.doe@example.com", new Guardian());

        String expected = "Student(studentId=1, firstName=John, lastName=Doe, emailId=john.doe@example.com, guardian=Guardian(name=null, email=null, mobile=null))";
        assertEquals(expected, student.toString());
    }
}