package com.studentmanagement.controller;
import com.studentmanagement.model.Student;
import com.studentmanagement.service.StudentService;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@RestController
@RequestMapping("/api/students")
public class StudentController {
 private final StudentService service;
 public StudentController(StudentService service){this.service=service;}
 @PostMapping
 public ResponseEntity<?> add(@RequestBody Student s){
  try{return ResponseEntity.status(HttpStatus.CREATED).body(service.add(s));}
  catch(IllegalArgumentException e){return ResponseEntity.badRequest().body(Map.of("error",e.getMessage()));}
  catch(DuplicateKeyException e){return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("error","Student ID or email already exists."));}
 }
 @GetMapping public List<Student> all(){return service.getAll();}
 @GetMapping("/{id}") public ResponseEntity<?> byId(@PathVariable int id){
  Student s=service.getById(id);return s==null?ResponseEntity.status(404).body(Map.of("error","Student not found.")):ResponseEntity.ok(s);
 }
 @GetMapping("/search") public List<Student> search(@RequestParam String term){return service.search(term);}
 @PutMapping("/{id}") public ResponseEntity<?> update(@PathVariable int id,@RequestBody Student s){
  try{Student result=service.update(id,s);return result==null?ResponseEntity.status(404).body(Map.of("error","Student not found.")):ResponseEntity.ok(result);}
  catch(IllegalArgumentException e){return ResponseEntity.badRequest().body(Map.of("error",e.getMessage()));}
 }
 @DeleteMapping("/{id}") public ResponseEntity<?> delete(@PathVariable int id){
  return service.delete(id)?ResponseEntity.ok(Map.of("message","Student deleted successfully.")):ResponseEntity.status(404).body(Map.of("error","Student not found."));
 }
 @GetMapping("/merit-list") public List<Map<String,Object>> merit(){return service.meritList();}
 @GetMapping("/same-skills") public Map<String,List<Integer>> sameSkills(){return service.sameSkills();}
 @GetMapping("/programming-improvement") public List<Student> improvement(){return service.programmingImprovement();}
}
