package com.studentmanagement.repository;
import com.studentmanagement.model.Student;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import java.util.ArrayList;
import java.util.List;

@Repository
public class StudentRepository {
 private final JdbcTemplate jdbc;
 public StudentRepository(JdbcTemplate jdbc){this.jdbc=jdbc;}
 private final RowMapper<Student> mapper=(rs,n)->new Student(
  rs.getInt("student_id"),rs.getString("student_name"),rs.getString("email"),rs.getString("course_name"),
  rs.getDouble("programming_score"),rs.getDouble("mathematics_score"),new ArrayList<>());

 public List<Student> findAll(){
  List<Student> list=jdbc.query("SELECT * FROM students ORDER BY student_id",mapper);
  list.forEach(this::loadSkills); return list;
 }
 public Student findById(int id){
  List<Student> list=jdbc.query("SELECT * FROM students WHERE student_id=?",mapper,id);
  if(list.isEmpty())return null; Student s=list.get(0); loadSkills(s); return s;
 }
 public List<Student> search(String term){
  String p="%"+term+"%";
  List<Student> list=jdbc.query("SELECT * FROM students WHERE CAST(student_id AS CHAR) LIKE ? OR email LIKE ? OR course_name LIKE ? ORDER BY student_id",mapper,p,p,p);
  list.forEach(this::loadSkills); return list;
 }
 @Transactional
 public void save(Student s){
  jdbc.update("INSERT INTO students(student_id,student_name,email,course_name,programming_score,mathematics_score) VALUES(?,?,?,?,?,?)",
   s.getStudentId(),s.getStudentName(),s.getEmail(),s.getCourseName(),s.getProgrammingScore(),s.getMathematicsScore());
  replaceSkills(s.getStudentId(),s.getTechnicalSkills());
 }
 @Transactional
 public boolean update(int id,double p,double m,List<String> skills){
  int count=jdbc.update("UPDATE students SET programming_score=?,mathematics_score=? WHERE student_id=?",p,m,id);
  if(count==0)return false; replaceSkills(id,skills); return true;
 }
 @Transactional
 public boolean delete(int id){return jdbc.update("DELETE FROM students WHERE student_id=?",id)>0;}
 private void loadSkills(Student s){
  List<String> skills=jdbc.query("SELECT skill FROM student_skills WHERE student_id=? ORDER BY skill",
   (rs,n)->rs.getString("skill"),s.getStudentId());
  s.setTechnicalSkills(skills);
 }
 private void replaceSkills(int id,List<String> skills){
  jdbc.update("DELETE FROM student_skills WHERE student_id=?",id);
  if(skills!=null)for(String skill:skills)if(skill!=null&&!skill.isBlank())
   jdbc.update("INSERT IGNORE INTO student_skills(student_id,skill) VALUES(?,?)",id,skill.trim());
 }
}
