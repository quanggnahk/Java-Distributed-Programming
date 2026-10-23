package App;

import config.EntityManagerFactoryUtil;
import config.SchoolDAO;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Persistence;

public class Connection {
	public static void main(String[] args) {
		Persistence.createEntityManagerFactory("week7-8");
		EntityManager em = EntityManagerFactoryUtil.getEntityManager();
        SchoolDAO dao = new SchoolDAO(em);

        try {
            System.out.println("--- CÂU 2 ---");
            dao.getNumberOfStudentsByDepartment().forEach((dept, count) -> 
                System.out.println(dept.getName() + " - Số SV: " + count));

            System.out.println("\n--- CÂU 3 ---");
            dao.getAverageScoreOfStudents().forEach((student, avg) -> 
                System.out.println(student.getFirstName() + " " + student.getLastName() + " - ĐTB: " + avg));

            System.out.println("\n--- CÂU 4 ---");
            dao.listDepartmentsWithoutStudents().forEach(dept -> 
                System.out.println(dept.getName()));

            System.out.println("\n--- CÂU 5 ---");
            dao.listStudentsStudyingCourseWithHighestScore("Calculus")
               .forEach(student -> System.out.println(student.getFirstName() + " " + student.getLastName()));

        } finally {
            em.close();
            EntityManagerFactoryUtil.close();
        }
	}
}