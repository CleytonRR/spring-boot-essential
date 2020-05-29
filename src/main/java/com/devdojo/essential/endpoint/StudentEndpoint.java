package com.devdojo.essential.endpoint;

import com.devdojo.essential.model.Student;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static java.util.Arrays.asList;

@RestController
@RequestMapping("/students")
public class StudentEndpoint {

    @GetMapping("/list")
    public List<Student> listAll() {
        return asList(new Student("keke"), new Student("tokurico"));
    }
}
