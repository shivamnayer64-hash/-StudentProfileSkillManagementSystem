CREATE TABLE IF NOT EXISTS students (
 student_id INT PRIMARY KEY,
 student_name VARCHAR(100) NOT NULL,
 email VARCHAR(150) NOT NULL UNIQUE,
 course_name VARCHAR(100) NOT NULL,
 programming_score DECIMAL(5,2) NOT NULL,
 mathematics_score DECIMAL(5,2) NOT NULL,
 CONSTRAINT chk_programming_score CHECK (programming_score BETWEEN 0 AND 100),
 CONSTRAINT chk_mathematics_score CHECK (mathematics_score BETWEEN 0 AND 100)
);
CREATE TABLE IF NOT EXISTS student_skills (
 id BIGINT AUTO_INCREMENT PRIMARY KEY,
 student_id INT NOT NULL,
 skill VARCHAR(100) NOT NULL,
 CONSTRAINT fk_student_skills FOREIGN KEY (student_id) REFERENCES students(student_id) ON DELETE CASCADE,
 CONSTRAINT uq_student_skill UNIQUE (student_id, skill)
);
