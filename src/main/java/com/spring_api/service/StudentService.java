package com.spring_api.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.spring_api.entity.Student;
import com.spring_api.repository.StudentRepository;

@Service
public class StudentService {

  private final StudentRepository studentRepository;

  @Autowired
  public StudentService(StudentRepository studentRepository) {
    this.studentRepository = studentRepository;
  }

  public Page<Student> getAllStudents(Pageable pageable) {
    return studentRepository.findAll(pageable);
  }

  public Optional<Student> getStudentById(Long id) {
    return studentRepository.findById(id);
  }

  public Student addStudent(Student student) {
    return studentRepository.save(student);
  }

  public Optional<Student> updateStudent(Long id, Student student) {
    Optional<Student> optionalStudent = studentRepository.findById(id);
    if (optionalStudent.isPresent()) {
      student.setStudentId(id);
      Student savedStudent = studentRepository.save(student);
      return Optional.of(savedStudent);
    } else {
      return Optional.empty();
    }
  }

  public void deleteStudent(Long id) {
    studentRepository.deleteById(id);
  }
}
