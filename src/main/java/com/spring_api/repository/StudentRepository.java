package com.spring_api.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.spring_api.entity.Student;

import jakarta.transaction.Transactional;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {

        Page<Student> findAll(Pageable pageable);

        List<Student> findByFirstName(String firstName);

        List<Student> findByFirstNameContaining(String name);

        List<Student> findByLastNameNotNull();

        List<Student> findByGuardianName(String guardianName);

        Student findByFirstNameAndLastName(String firstName,
                        String lastName);

        // JPQL
        @Query("select s from Student s where s.emailId = ?1")
        Student getStudentByEmailAddress(String emailId);

        // JPQL
        @Query("select s.firstName from Student s where s.emailId = ?1")
        String getStudentFirstNameByEmailAddress(String emailId);

        // Native
        @Query(value = "SELECT * FROM students s where s.email_address = ?1", nativeQuery = true)
        Student getStudentByEmailAddressNative(String emailId);

        // Native Named Param
        @Query(value = "SELECT * FROM students s where s.email_address = :emailId", nativeQuery = true)
        Student getStudentByEmailAddressNativeNamedParam(
                        @Param("emailId") String emailId);

        @Modifying
        @Transactional
        @Query(value = "update students set first_name = ?1 where email_address = ?2", nativeQuery = true)
        int updateStudentNameByEmailId(String firstName, String emailId);

}
