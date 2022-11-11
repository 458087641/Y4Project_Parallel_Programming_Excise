package exercises;

public class Student {
        // First name of the student.
        private final String firstName;
        //Surname of the student.
        private final String lastName;
        //Age of the student.
        private final double age;
        //Grade the student has received in the class so far.
        private final int grade;
         //Whether the student is currently enrolled, or has already completed the course.
        private final boolean isCurrent;


        public Student(String firstName, String lastName, double age, int grade, boolean isCurrent) {
                this.firstName = firstName;
                this.lastName = lastName;
                this.age = age;
                this.grade = grade;
                this.isCurrent = isCurrent;
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
        public boolean checkCurrent() {return isCurrent;}

}
