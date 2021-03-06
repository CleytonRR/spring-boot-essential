package com.devdojo.essential.endpoint;

import com.devdojo.essential.error.ResourceNotFoundException;
import com.devdojo.essential.model.Student;
import com.devdojo.essential.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import javax.validation.Valid;
import java.util.Optional;

import static org.springframework.beans.support.PagedListHolder.DEFAULT_PAGE_SIZE;

@RestController
@RequestMapping("v1")
public class StudentEndpoint {

    private final StudentRepository studentDAO;

    @Autowired
    public StudentEndpoint(StudentRepository studentDAO) {
        this.studentDAO = studentDAO;
    }

    @GetMapping("protected/students")
    public ResponseEntity<?> listAll(@PageableDefault(page = 0, size = 5)
            Pageable pageable) {
        return new ResponseEntity<>(studentDAO.findAll(pageable), HttpStatus.OK);
    }

    @GetMapping("protected/students/{id}")
    public ResponseEntity<?> getStudentById(@PathVariable("id") Long id) {
        Optional<Student> student = studentDAO.findById(id);
        verifyStudentExists(id);
        return new ResponseEntity<>(student, HttpStatus.OK);
    }

    @GetMapping("protected/students/findByName/{name}")
    public ResponseEntity<?> findStudentsByName(@PathVariable String name) {
        return new ResponseEntity<>(studentDAO.findByNameIgnoreCaseContaining(name), HttpStatus.OK);
    }

    @PostMapping("/admin/students")
    @Transactional(rollbackOn = Exception.class)
    public ResponseEntity<?> save(@RequestBody @Valid Student student) {
        return new ResponseEntity<>(studentDAO.save(student), HttpStatus.CREATED);
    }

    @DeleteMapping("/admin/students/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        verifyStudentExists(id);
        studentDAO.deleteById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping("/admin/students")
    public ResponseEntity<?> update(@RequestBody Student student) {
        verifyStudentExists(student.getId());
        studentDAO.save(student);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    private void verifyStudentExists(Long id) {
        Student student = studentDAO
                .findById(id).
                orElseThrow(() -> new ResourceNotFoundException("Student not found for ID: " + id));
    }
}
