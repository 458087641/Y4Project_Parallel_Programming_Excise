package exercises;

public class StudentInfo {
        // First name of the student.
        private final String firstName;
        //Surname of the student.
        private final String lastName;
        //Age of the student.
        private final double age;
        //Grade the student has received in the class so far.
        private final int grade;



        public StudentInfo(String firstName, String lastName, double age, int grade) {
                this.firstName = firstName;
                this.lastName = lastName;
                this.age = age;
                this.grade = grade;
        }
        public String getFirstName() {
                return firstName;
        }
        public String getLastName() {
                return lastName;
        }
        public double getAge() {
                return age;
        }
        public int getGrade() {
                return grade;
        }


}
