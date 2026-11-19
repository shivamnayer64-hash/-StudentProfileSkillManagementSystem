package com.studentmanagement.service;
import com.studentmanagement.model.Student;
import com.studentmanagement.repository.StudentRepository;
import org.springframework.stereotype.Service;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class StudentService {
 private final StudentRepository repo;
 public StudentService(StudentRepository repo){this.repo=repo;}
 public Student add(Student s){validate(s);repo.save(s);return repo.findById(s.getStudentId());}
 public List<Student> getAll(){return repo.findAll();}
 public Student getById(int id){return repo.findById(id);}
 public List<Student> search(String term){return repo.search(term);}
 public Student update(int id,Student s){
  validateScores(s.getProgrammingScore(),s.getMathematicsScore());
  return repo.update(id,s.getProgrammingScore(),s.getMathematicsScore(),s.getTechnicalSkills())?repo.findById(id):null;
 }
 public boolean delete(int id){return repo.delete(id);}
 public List<Map<String,Object>> meritList(){
  return getAll().stream().sorted(Comparator.comparingDouble(Student::getAverageScore).reversed())
   .map(this::summary).collect(Collectors.toList());
 }
 public Map<String,List<Integer>> sameSkills(){
  Map<String,List<Integer>> map=new HashMap<>();
  for(Student s:getAll())for(String skill:new HashSet<>(s.getTechnicalSkills()))
   map.computeIfAbsent(skill,k->new ArrayList<>()).add(s.getStudentId());
  return map.entrySet().stream().filter(e->e.getValue().size()>1)
   .collect(Collectors.toMap(Map.Entry::getKey,Map.Entry::getValue));
 }
 public List<Student> programmingImprovement(){
  return getAll().stream().filter(s->s.getProgrammingScore()<50&&s.getMathematicsScore()>=75).collect(Collectors.toList());
 }
 private Map<String,Object> summary(Student s){
  Map<String,Object> m=new LinkedHashMap<>();m.put("studentId",s.getStudentId());m.put("studentName",s.getStudentName());
  m.put("averageScore",s.getAverageScore());m.put("performanceCategory",s.getPerformanceCategory());
  m.put("strongerSubject",s.getStrongerSubject());return m;
 }
 private void validate(Student s){
  if(s.getStudentId()<=0)throw new IllegalArgumentException("Student ID must be positive.");
  if(s.getStudentName()==null||s.getStudentName().isBlank())throw new IllegalArgumentException("Student name is required.");
  if(s.getEmail()==null||!s.getEmail().matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$"))throw new IllegalArgumentException("A valid email address is required.");
  if(s.getCourseName()==null||s.getCourseName().isBlank())throw new IllegalArgumentException("Course name is required.");
  validateScores(s.getProgrammingScore(),s.getMathematicsScore());
 }
 private void validateScores(double p,double m){
  if(p<0||p>100||m<0||m>100)throw new IllegalArgumentException("Scores must be between 0 and 100.");
 }
}
