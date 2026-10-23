package config;

import entities.*;
import jakarta.persistence.EntityManager;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class SchoolDAO {

    private EntityManager em;

    public SchoolDAO(EntityManager em) {
        this.em = em;
    }

    public Map<Department1, Long> getNumberOfStudentsByDepartment() {
        String jpql = "SELECT d, COUNT(DISTINCT sg.student) " +
                      "FROM Department1 d " + 
                      "LEFT JOIN d.courses c " +
                      "LEFT JOIN c.studentGrades sg " +
                      "GROUP BY d " +
                      "ORDER BY COUNT(DISTINCT sg.student) DESC";
                      
        List<Object[]> results = em.createQuery(jpql, Object[].class).getResultList();

        Map<Department1, Long> map = new LinkedHashMap<>(); 
        for (Object[] row : results) {
            Department1 dept = (Department1) row[0]; 
            Long count = (Long) row[1];
            map.put(dept, count);
        }
        return map;
    }

    public Map<Student, Double> getAverageScoreOfStudents() {
        String jpql = "SELECT sg.student, AVG(sg.grade) " +
                      "FROM StudentGrade sg " +
                      "GROUP BY sg.student";
                      
        List<Object[]> results = em.createQuery(jpql, Object[].class).getResultList();

        Map<Student, Double> map = new HashMap<>();
        for (Object[] row : results) {
            Student student = (Student) row[0];
            Double avgGrade = (Double) row[1];
            map.put(student, avgGrade);
        }
        return map;
    }

    public List<Department1> listDepartmentsWithoutStudents() {
        String jpql = "SELECT d FROM Department1 d " +
                      "WHERE d NOT IN (" +
                      "   SELECT c.department FROM Course c JOIN c.studentGrades sg" +
                      ")";
                      
        return em.createQuery(jpql, Department1.class).getResultList();
    }

    public List<Student> listStudentsStudyingCourseWithHighestScore(String courseName) {
        String jpql = "SELECT sg.student FROM StudentGrade sg " +
                      "WHERE sg.course.title = :courseName " +
                      "AND sg.grade = (" +
                      "   SELECT MAX(sg2.grade) FROM StudentGrade sg2 WHERE sg2.course.title = :courseName" +
                      ")";
                      
        return em.createQuery(jpql, Student.class)
                 .setParameter("courseName", courseName)
                 .getResultList();
    }
}