package com.spring_api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.spring_api.Main;
import com.spring_api.entity.Student;
import com.spring_api.repository.StudentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MockMvcBuilder;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.StandaloneMockMvcBuilder;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
@SpringBootTest(classes = Main.class)
@AutoConfigureTestDatabase
@EnableWebSecurity
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class StudentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Mock
    private StudentRepository studentRepository;

    @InjectMocks
    private StudentController studentController;

    private List<Student> studentList;

    Student student1 = Student.builder().studentId(1L).firstName("John").lastName("Doe").emailId("johndoe@example.com").build();
    Student student2 = Student.builder().studentId(2L).firstName("Jane").lastName("Doe").emailId("janedoe@example.com").build();

    @BeforeEach
    @WithMockUser(username = "testuser", authorities = {"ROLE_ADMIN"})
    public void setup() {
        MockitoAnnotations.openMocks(this);
        //this.mockMvc = StandaloneMockMvcBuilder.standaloneSetup(studentController).build();
        studentList = new ArrayList<>();  
        studentList.add(student1);
        studentList.add(student2);
        studentList = new ArrayList<>();  
        studentList.add(student1);
        studentList.add(student2);
      
    }

    @Test
    @Order(2)
    @WithMockUser(username = "testuser", authorities = {"ROLE_ADMIN"})
    public void testGetAllStudents() throws Exception {
        studentList = new ArrayList<>();  
        studentList.add(student1);
        studentList.add(student2);
        when(studentRepository.findAll()).thenReturn(studentList);
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/students"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))

                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.[0].firstName").value("John"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.[0].lastName").value("Doe"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.[0].emailId").value("johndoe@gmail.com"))

                .andExpect(jsonPath("$[0].lastName", is("Doe")));

                //.andExpect(MockMvcResultMatchers.jsonPath("$.[1].firstName").value("Jane"));
    }

    @Test
    @Order(3)
    @WithMockUser(username = "testuser", authorities = {"ROLE_ADMIN"})
    public void testGetExistingStudentById() throws Exception {
        Long id = 1L;
        Student student = Student.builder().studentId(id).firstName("John").lastName("Doe").emailId("johndoe@gmail.com").build();
        when(studentRepository.findById(id)).thenReturn(Optional.of(student));
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/students/" + id))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.firstName").value("John"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.lastName").value("Doe"));
    }

    @Test
    @Order(4)
    @WithMockUser(username = "testuser", authorities = {"ROLE_ADMIN"})
    public void testGetNonExistingStudentById() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/students/3"))
                .andExpect(status().isNotFound())
                .andReturn();
                
    }


    @Test
    @Order(1)
    @WithMockUser(username = "testuser", authorities = {"ROLE_ADMIN"})
    public void testAddStudent() throws Exception {
        Student student = Student.builder().firstName("John").lastName("Doe").emailId("johndoe@gmail.com").build();
        when(studentRepository.save(student)).thenReturn(student);
        String json = objectMapper.writeValueAsString(student);
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/students")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isCreated())
                .andReturn();
        String content = mvcResult.getResponse().getContentAsString();
        Student savedStudent = objectMapper.readValue(content, Student.class);
        assert savedStudent.getFirstName().equals("John");
        assert savedStudent.getLastName().equals("Doe");
    }
    @Test
    @Order(5)
    @WithMockUser(username = "testuser", authorities = {"ROLE_ADMIN"})
    public void testEditExistingStudent() throws Exception {
        Student student = Student.builder().firstName("John").lastName("Doe2").emailId("johndoe@gmail.com").build();
        when(studentRepository.save(student)).thenReturn(student);
        String json = objectMapper.writeValueAsString(student);
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/students/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isOk())
                .andReturn();
        String content = mvcResult.getResponse().getContentAsString();
        Student savedStudent = objectMapper.readValue(content, Student.class);
        assert savedStudent.getFirstName().equals("John");
        assert savedStudent.getLastName().equals("Doe2");
    }

    @Test
    @Order(6)
    @WithMockUser(username = "testuser", authorities = {"ROLE_ADMIN"})
    public void testEditNonExistingStudent() throws Exception {
        Student student = Student.builder().firstName("John").lastName("Doe2").emailId("johndoe@gmail.com").build();
        when(studentRepository.save(student)).thenReturn(student);
        String json = objectMapper.writeValueAsString(student);
        mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/students/2")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isNotFound())
                .andReturn();
    }

    @Test
    @Order(7)
    @WithMockUser(username = "testuser", authorities = {"ROLE_ADMIN"})
    public void testDeleteNonExistingStudent() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/students/2"))
                .andExpect(status().isNotFound())
                .andReturn();
    }

    @Test
    @Order(8)
    @WithMockUser(username = "testuser", authorities = {"ROLE_ADMIN"})
    public void testDeleteExistingStudent() throws Exception {
        Student studentToDelete = Student.builder().firstName("John").lastName("Doe").emailId("johndoe2@gmail.com").build();
        when(studentRepository.save(studentToDelete)).thenReturn(studentToDelete);
    
        // Save the student and get its ID
        MvcResult saveResult = mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/students")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(studentToDelete)))
                .andExpect(status().isCreated())
                .andReturn();
        String saveContent = saveResult.getResponse().getContentAsString();
        Student savedStudent = objectMapper.readValue(saveContent, Student.class);
        Long studentId = savedStudent.getStudentId();
    
        // Delete the student
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/students/2"))
                .andExpect(status().isNoContent())
                .andReturn();
        
        assertNull(studentRepository.findById(studentId).orElse(null));

    }

    @Test
    @Order(9)
    public void postAboutWithoutCsrfThenReturns403() throws Exception {
        this.mockMvc.perform(post("/students"))
                .andExpect(status().isForbidden());
    }

    @Test
    @Order(10)
    public void getAboutWithoutCsrfThenReturns403() throws Exception {
        this.mockMvc.perform(get("/students"))
                .andExpect(status().isForbidden());
    }
}
