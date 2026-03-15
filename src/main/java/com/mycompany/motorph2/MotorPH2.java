/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.motorph2;

/**
 *
 * @author ninadelosreyes
 */
import java.util.Scanner;

public class MotorPH2 {

    public static void main(String[] args) {

        Scanner input = new Scanner(System.in);

        // ===== MotorPH Employee Data =====
        int[] empNumbers = {
        10001,10002,10003,10004,10005,10006,10007,10008,10009,10010,
        10011,10012,10013,10014,10015,10016,10017,10018,10019,10020,
        10021,10022,10023,10024,10025,10026,10027,10028,10029,10030,
        10031,10032,10033,10034
        };

        String[] empNames = {
        "Manuel Garcia","Antonio Lim","Bianca Aquino","Isabella Reyes","Eduard Hernandez",
        "Andrea Mae Villanueva","Brad San Jose","Alice Romualdez","Rosie Atienza","Roderick Alvaro",
        "Anthony Salcedo","Josie Lopez","Martha Farala","Leila Martinez","Fredrick Romualdez",
        "Christian Mata","Selena de Leon","Allison San Jose","Cydney Rosario","Mark Bautista",
        "Darlene Lazaro","Kolby Delos Santos","Vella Santos","Tomas del Rosario","Jacklyn Tolentino",
        "Percival Gutierrez","Garfield Manalaysay","Lizeth Villegas","Carol Ramos","Emilia Maceda",
        "Delia Aguilar","John Rafael Castro","Carlos Ian Martinez","Beatriz Santos"
        };

        String[] empBirthday = {
        "1983-10-11","1988-06-19","1989-08-04","1994-06-16","1989-09-23",
        "1988-02-14","1996-03-15","1992-05-14","1948-09-24","1988-03-30",
        "1993-09-14","1987-01-14","1942-01-11","1970-07-11","1985-03-10",
        "1987-10-21","1975-02-20","1986-06-24","1996-10-06","1991-02-12",
        "1985-11-25","1980-02-26","1983-12-31","1978-12-18","1984-05-19",
        "1970-12-18","1986-08-28","1981-12-12","1978-08-20","1973-04-14",
        "1989-01-27","1992-02-09","1990-11-16","1990-08-07"};

        double[] hourlyRate = {
        535.71,357.14,357.14,357.14,313.51,
        313.51,255.80,133.93,133.93,313.51,
        302.53,229.02,142.86,142.86,318.45,
        255.80,249.11,133.93,133.93,138.39,
        138.39,142.86,133.93,133.93,142.86,
        147.32,147.32,142.86,133.93,133.93,
        133.93,313.51,313.51,313.51
        };

        // Sample worked hours (Cutoff 1 and Cutoff 2)
        double[][] hoursWorked = new double[34][2];

        for(int i=0;i<34;i++){
            hoursWorked[i][0] = 80;
            hoursWorked[i][1] = 85;
        }

        // ===== LOGIN =====

        System.out.print("Enter Username: ");
        String username = input.nextLine();

        System.out.print("Enter Password: ");
        int password = input.nextInt();

        if (!((username.equals("employee") || username.equals("payroll_staff")) && password == 12345)) {
            System.out.println("Incorrect username and/or password");
            return;
        }

        // ===== EMPLOYEE MENU =====

        if (username.equals("employee")) {

            System.out.println("\n1 - Enter Employee Number");
            System.out.println("2 - Exit Program");
            System.out.print("Choose Option: ");

            int choice = input.nextInt();

            if (choice == 1) {

                System.out.print("Enter Employee Number: ");
                int empNumber = input.nextInt();

                boolean found = false;

                for (int i = 0; i < empNumbers.length; i++) {

                    if (empNumber == empNumbers[i]) {

                        System.out.println("\nEmployee#: " + empNumbers[i]);
                        System.out.println("Employee Name: " + empNames[i]);
                        System.out.println("Birthday: " + empBirthday[i]);

                        found = true;
                        break;
                    }
                }

                if (!found) {
                    System.out.println("Employee number does not exist");
                }

                System.out.println("\nProgram Terminated.");
                return;
            }

            System.out.println("Program Terminated.");
            return;
        }

        // ===== PAYROLL STAFF MENU =====

        if (username.equals("payroll_staff")) {

            System.out.println("\n1 - Process Payroll");
            System.out.println("2 - Exit Program");
            System.out.print("Choose Option: ");

            int staffChoice = input.nextInt();

            if (staffChoice == 1) {

                System.out.println("\n1 - One Employee");
                System.out.println("2 - All Employees");
                System.out.println("3 - Exit Program");
                System.out.print("Choose Option: ");

                int processChoice = input.nextInt();

                if (processChoice == 1) {

                    System.out.print("Enter Employee Number: ");
                    int empNumber = input.nextInt();

                    boolean found = false;

                    for (int i = 0; i < empNumbers.length; i++) {

                        if (empNumber == empNumbers[i]) {

                            displayPayroll(empNumbers[i], empNames[i], empBirthday[i],
                                           hoursWorked[i], hourlyRate[i]);

                            found = true;
                            break;
                        }
                    }

                    if (!found) {
                        System.out.println("Employee number does not exist");
                    }

                    System.out.println("\nProgram Terminated.");
                    return;
                }

                else if (processChoice == 2) {

                    for (int i = 0; i < empNumbers.length; i++) {

                        displayPayroll(empNumbers[i], empNames[i], empBirthday[i],
                                       hoursWorked[i], hourlyRate[i]);

                        System.out.println("--------------------------------");
                    }

                    System.out.println("\nProgram Terminated.");
                    return;
                }

                else {
                    System.out.println("Program Terminated.");
                    return;
                }
            }

            else {
                System.out.println("Program Terminated.");
                return;
            }
        }
    }

    // ===== PAYROLL CALCULATION FUNCTION =====

    static void displayPayroll(int empNumber, String empName, String empBirthday,
                               double[] hours, double rate) {

        double[] gross = new double[2];
        double[] sss = new double[2];
        double[] philhealth = new double[2];
        double[] pagibig = new double[2];
        double[] tax = new double[2];
        double[] totalDeduction = new double[2];
        double[] net = new double[2];

        String[] cutoffDate = {"June 1 to 15", "June 16 to 30"};

        for (int i = 0; i < 2; i++) {

            double workedHours = hours[i];

            if (workedHours >= 40)
                workedHours -= 1;

            gross[i] = workedHours * rate;

            sss[i] = gross[i] * 0.045;
            philhealth[i] = gross[i] * 0.02;
            pagibig[i] = gross[i] * 0.01;
            tax[i] = gross[i] * 0.10;

            totalDeduction[i] = sss[i] + philhealth[i] + pagibig[i] + tax[i];
            net[i] = gross[i] - totalDeduction[i];
        }

        System.out.println("\nEmployee#: " + empNumber);
        System.out.println("Employee Name: " + empName);
        System.out.println("Birthday: " + empBirthday);

        for (int i = 0; i < 2; i++) {

            System.out.println("\nCutoff Date: " + cutoffDate[i]);
            System.out.println("Total Hours Worked: " + hours[i]);
            System.out.println("Gross Salary: " + gross[i]);

            if (i == 1) {

                System.out.println("Each Deductions:");
                System.out.println("SSS: " + sss[i]);
                System.out.println("PhilHealth: " + philhealth[i]);
                System.out.println("Pag-IBIG: " + pagibig[i]);
                System.out.println("Tax: " + tax[i]);
                System.out.println("Total Deductions: " + totalDeduction[i]);
            }

            System.out.println("Net Salary: " + net[i]);
        }
    }
}