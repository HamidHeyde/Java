package client;

import client.clientTest;

public class tests{

    public static void main(String[] args) {
        tests t = new tests();
        
        t.WrongUsername();t.AdvisorUsername();t.StudentUsername();

        t.Advisor_AvailableCourse();
        t.Advisor_AddWrongCourse();t.Advisor_AddRightCourse();
        t.Advisor_AvailableCourse();
        t.Advisor_RemoveWrongCourse();t.Advisor_RemoveRightCourse();
        t.Advisor_AvailableCourse();


        t.Student_GetClassSchedule();
        t.Student_AddWrongCourse();t.Student_AddCOMPCourse();
        t.Student_GetClassSchedule();
            t.Student_AddSOENCourse();
        t.Student_GetClassSchedule();
        t.Student_RemoveWrongCourse();t.Student_RemoveCOMPCourse();
        t.Student_GetClassSchedule();
            t.Student_RemoveSOENCourse();
        t.Student_GetClassSchedule();
    }


    //@Test: WrongUsername
    public void WrongUsername() {
        System.out.println("-----------------------------");
        System.out.println("Testing with  => Comt12345");
        clientTest test = new clientTest("Comt12345");
        if (test.evaluateAndConnect()) {
            System.out.println("Username => Right Format");
            if (test.isAdvisor) {
                System.out.println("You are => Advisor");
            } else {
                System.out.println("You are => Student");
            }
        } else {
            System.out.println("Username => Wrong Format");
        }
        System.out.println("-----------------------------");
    }
    //@Test: AdvisorUsername
    public void AdvisorUsername() {
        System.out.println("-----------------------------");
        System.out.println("Testing with  => Compa2345");
        clientTest test = new clientTest("Compa2345");
        if (test.evaluateAndConnect()) {
            System.out.println("Username => Right Format");
            if (test.isAdvisor) {
                System.out.println("You are => Advisor");
            } else {
                System.out.println("You are => Student");
            }
        } else {
            System.out.println("Username => Wrong Format");
        }
        System.out.println("-----------------------------");
    }
    //@Test: StudentUsername
    public void StudentUsername() {
        System.out.println("-----------------------------");
        System.out.println("Testing with  => Comps1245");
        clientTest test = new clientTest("Compa2345");
        if (test.evaluateAndConnect()) {
            System.out.println("Username => Right Format");
            if (test.isAdvisor) {
                System.out.println("You are => Advisor");
            } else {
                System.out.println("You are => Student");
            }
        } else {
            System.out.println("Username => Wrong Format");
        }
        System.out.println("-----------------------------");
    }
    //@Test: Advisor_AvailableCourse
    public void Advisor_AvailableCourse() {
        System.out.println("-----------------------------");
        System.out.println("Testing with  => Advisor => List Course availability => fall");
        clientTest test = new clientTest("Compa2345");
        test.listCourseAvailability("fall");
        System.out.println("-----------------------------");
    }

    //@Test: Advisor_AddWrongCourse
    public void Advisor_AddWrongCourse() {
        System.out.println("-----------------------------");
        System.out.println("Testing with  => Advisor => List addCourse => Compa2345,fall");
        clientTest test = new clientTest("Compa2345");
        test.addCourse("comp1234","fall");
        System.out.println("-----------------------------");
    }

    //@Test: Advisor_AddRightCourse
    public void Advisor_AddRightCourse() {
        System.out.println("-----------------------------");
        System.out.println("Testing with  => Advisor => addCourse => comp7676,fall");
        clientTest test = new clientTest("Compa2345");
        test.addCourse("comp7676","fall");
        System.out.println("-----------------------------");
    }

    //@Test: Advisor_RemoveWrongCourse
    public void Advisor_RemoveWrongCourse() {
        System.out.println("-----------------------------");
        System.out.println("Testing with  => Advisor => removeCourse => comp7600,fall");
        clientTest test = new clientTest("Compa2345");
        test.removeCourse("comp7600","fall");
        System.out.println("-----------------------------");
    }
    //@Test: Advisor_RemoveRightCourse
    public void Advisor_RemoveRightCourse() {
        System.out.println("-----------------------------");
        System.out.println("Testing with  => Advisor => removeCourse => comp7676,fall");
        clientTest test = new clientTest("Compa2345");
        test.removeCourse("comp7676","fall");
        System.out.println("-----------------------------");
    }


    //@Test: Student_GetClassSchedule
    public void Student_GetClassSchedule() {
        System.out.println("-----------------------------");
        System.out.println("Testing with  => Student => Get Class Schedule => comps1357");
        clientTest test = new clientTest("comps1357");
        test.getClassSchedule();
        System.out.println("-----------------------------");
    }
    //@Test:Student_AddWrongCourse
    public void Student_AddWrongCourse() {
        System.out.println("-----------------------------");
        System.out.println("Testing with  => Student => Enroll Course => comp9100,fall");
        clientTest test = new clientTest("comps1357");
        test.enrolCourse("comp9100","fall");
        System.out.println("-----------------------------");
    }
    //@Test:Student_AddCOMPCourse
    public void Student_AddCOMPCourse() {
        System.out.println("-----------------------------");
        System.out.println("Testing with  => Student => Enroll Course => comp1234,fall");
        clientTest test = new clientTest("comps1357");
        test.enrolCourse("comp1234","fall");
        System.out.println("-----------------------------");
    }
    //@Test: Student_AddSOENCourse
    public void Student_AddSOENCourse() {
        System.out.println("-----------------------------");
        System.out.println("Testing with  => Student => Enroll Course => soen1234,fall");
        clientTest test = new clientTest("comps1357");
        test.enrolCourse("soen1234","fall");
        System.out.println("-----------------------------");
    }
 

    //@Test: Student_RemoveWrongCourse
    public void Student_RemoveWrongCourse() {
        System.out.println("-----------------------------");
        System.out.println("Testing with  => Student => drop Course => comp9100");
        clientTest test = new clientTest("comps1357");
        test.dropCourse("comp9100");
        System.out.println("-----------------------------");
    }
    //@Test: Student_RemoveCOMPCourse
    public void Student_RemoveCOMPCourse() {
        System.out.println("-----------------------------");
        System.out.println("Testing with  => Student => drop Course => comp1234");
        clientTest test = new clientTest("comps1357");
        test.dropCourse("comp1234");
        System.out.println("-----------------------------");
    }
    //@Test: Student_RemoveSOENCourse
    public void Student_RemoveSOENCourse() {
        System.out.println("-----------------------------");
        System.out.println("Testing with  => Student => drop Course => soen1234");
        clientTest test = new clientTest("comps1357");
        test.dropCourse("soen1234");
        System.out.println("-----------------------------");
    }

    
}