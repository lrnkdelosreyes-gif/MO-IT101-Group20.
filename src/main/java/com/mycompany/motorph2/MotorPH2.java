/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.motorph2;

/**
 *
 * @author ninadelosreyes
 */
import java.util.Scanner;
import java.io.File;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.Duration;
import java.time.LocalTime;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;

public class MotorPH2 {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        String attendanceFile = "attendance.csv";
        String employeeFile = "employee.csv";

        System.out.print("Enter username: ");
        String username = scanner.nextLine();

        System.out.print("Enter password: ");
        String password = scanner.nextLine();

        // payroll staff login
        if (username.equals("payroll_staff") && password.equals("12345")) {

            System.out.println("Payroll Staff login successful.");

            System.out.println("1 Process Payroll");
            System.out.println("2 Exit Program");

            System.out.print("Enter option: ");
            int option = scanner.nextInt();

            if (option == 1) {

                System.out.println("1 One Employee");
                System.out.println("2 All Employees");
                System.out.println("3 Exit Program");

                System.out.print("Enter option: ");
                int subOption = scanner.nextInt();

                if (subOption == 1) {

                    System.out.print("Enter employee number: ");
                    int employeeNumber = scanner.nextInt();

                    boolean found = searchEmployee(employeeNumber, true, employeeFile);

                    if (found) {
                        processPayroll(attendanceFile, employeeFile, String.valueOf(employeeNumber));
                    }

                } else if (subOption == 2) {

                    processAllEmployees(attendanceFile, employeeFile);

                } else if (subOption == 3) {
                    System.out.println("Program terminated.");
                }

            } else if (option == 2) {
                System.out.println("Program terminated.");
            }
        }

        // employee login user name: employee pw: 12345
        else if (username.equals("employee") && password.equals("12345")) {

            System.out.println("Employee login successful.");

            System.out.println("1 Enter your employee number");
            System.out.println("2 Exit program");

            System.out.print("Enter option: ");
            int option = scanner.nextInt();

            if (option == 1) {

                System.out.print("Enter employee number: ");
                int employeeNumber = scanner.nextInt();

                searchEmployee(employeeNumber, false, employeeFile);

            } else if (option == 2) {
                System.out.println("Exit Program");
            }

        } else {
            System.out.println("Incorrect username and/or password.");
        }

        scanner.close();
    }


    // search and display employee info
    public static boolean searchEmployee(int employeeNumber, boolean isPayroll, String employeeFile) {

        try {
            File file = new File(employeeFile);
            BufferedReader br = new BufferedReader(new FileReader(file));

            br.readLine(); // skip header
            String line;
            boolean found = false;

            while ((line = br.readLine()) != null) {

                String[] employeeData = line.split(",");

                if (employeeData[0].trim().equals(String.valueOf(employeeNumber))) {

                    System.out.println("Employee #: " + employeeData[0].trim());
                    System.out.println("Employee Name: " + employeeData[2].trim() + " " + employeeData[1].trim());
                    System.out.println("Birthday: " + employeeData[3].trim());

                    if (isPayroll) {
                        System.out.println("Hourly Rate: " + employeeData[18].trim());
                    }

                    found = true;
                    break;
                }
            }

            if (!found) {
                System.out.println("Employee number does not exist.");
            }

            br.close();
            return found;

        } catch (IOException e) {
            System.out.println("Error reading employee file.");
            return false;
        }
    }


    // get hourly rate for an employee
    public static double getHourlyRate(String employeeFile, String employeeNum) {

        try {
            BufferedReader br = new BufferedReader(new FileReader(new File(employeeFile)));
            br.readLine(); // skip header
            String line;

            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                if (data[0].trim().equals(employeeNum)) {
                    br.close();
                    // remove commas from formatted numbers e.g. "1,000.00"
                    String rateStr = data[18].trim().replace(",", "");
                    return Double.parseDouble(rateStr);
                }
            }
            br.close();

        } catch (Exception e) {
            System.out.println("Error reading hourly rate.");
        }
        return 0.0;
    }


    // process payroll for one employee
    public static void processPayroll(String attendanceFile, String employeeFile, String employeeNum) {

        DateTimeFormatter timeFormat = DateTimeFormatter.ofPattern("H:mm");
        double hourlyRate = getHourlyRate(employeeFile, employeeNum);

        for (int month = 6; month <= 12; month++) {

            double firstCutOff = 0.0;
            double secondCutOff = 0.0;

            try (BufferedReader br = new BufferedReader(new FileReader(attendanceFile))) {

                br.readLine(); // skip header
                String line;

                while ((line = br.readLine()) != null) {

                    if (line.trim().isEmpty()) continue;

                    String[] attendanceData = line.split(",");

                    String recordEmpNo = attendanceData[0].trim();
                    if (!recordEmpNo.equals(employeeNum)) continue;

                    String date = attendanceData[3].trim();
                    String[] dateParts = date.split("/");

                    int recordMonth = Integer.parseInt(dateParts[0]);
                    int day = Integer.parseInt(dateParts[1]);
                    int year = Integer.parseInt(dateParts[2]);

                    if (year != 2024 || recordMonth != month) continue;

                    LocalTime timeIn = LocalTime.parse(attendanceData[4].trim(), timeFormat);
                    LocalTime timeOut = LocalTime.parse(attendanceData[5].trim(), timeFormat);

                    double hoursWorked = computeDailyHoursWorked(timeIn, timeOut);

                    if (day <= 15) {
                        firstCutOff += hoursWorked;
                    } else {
                        secondCutOff += hoursWorked;
                    }
                }

            } catch (Exception e) {
                System.out.println("Error reading attendance file.");
                return;
            }

            String monthName = getMonthName(month);
            int lastDay = YearMonth.of(2024, month).lengthOfMonth();

            double firstGross = firstCutOff * hourlyRate;
            double secondGross = secondCutOff * hourlyRate;
            double combinedGross = firstGross + secondGross;

            // deductions based on combined monthly gross salary
            double sss = computeSSS(combinedGross);
            double philHealth = computePhilHealth(combinedGross);
            double pagIbig = computePagIbig(combinedGross);
            double totalDeductions = sss + philHealth + pagIbig;
            double taxableIncome = combinedGross - totalDeductions;
            double tax = computeTax(taxableIncome);
            double totalDeductionsWithTax = totalDeductions + tax;

            double firstNetSalary = firstGross;
            double secondNetSalary = secondGross - totalDeductionsWithTax;

            // first cutoff display
            System.out.println("\nCutoff Date: " + monthName + " 1 to 15");
            System.out.println("Total Hours Worked: " + firstCutOff);
            System.out.println("Gross Salary: " + firstGross);
            System.out.println("Net Salary: " + firstNetSalary);

            // second cutoff display (includes deductions)
            System.out.println("\nCutoff Date: " + monthName + " 16 to " + lastDay + " (Second payout includes all deductions)");
            System.out.println("Total Hours Worked: " + secondCutOff);
            System.out.println("Gross Salary: " + secondGross);
            System.out.println("Each Deduction:");
            System.out.println("  SSS: " + sss);
            System.out.println("  PhilHealth: " + philHealth);
            System.out.println("  Pag-IBIG: " + pagIbig);
            System.out.println("  Tax: " + tax);
            System.out.println("Total Deductions: " + totalDeductionsWithTax);
            System.out.println("Net Salary: " + secondNetSalary);
            System.out.println("--------------------------------------------------");
        }
    }


    // process payroll for ALL employees
    public static void processAllEmployees(String attendanceFile, String employeeFile) {

        try {
            BufferedReader br = new BufferedReader(new FileReader(new File(employeeFile)));
            br.readLine(); // skip header
            String line;

            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue;

                String[] data = line.split(",");
                String empNum = data[0].trim();

                System.out.println("\n==================================================");
                System.out.println("Employee #: " + data[0].trim());
                System.out.println("Employee Name: " + data[2].trim() + " " + data[1].trim());
                System.out.println("Birthday: " + data[3].trim());
                System.out.println("Hourly Rate: " + data[18].trim());
                System.out.println("==================================================");

                processPayroll(attendanceFile, employeeFile, empNum);
            }

            br.close();

        } catch (IOException e) {
            System.out.println("Error reading employee file.");
        }
    }


    // compute daily hours worked
   public static double computeDailyHoursWorked(LocalTime timeIn, LocalTime timeOut) {

    LocalTime startWork = LocalTime.of(8, 0);
    LocalTime gracePeriod = LocalTime.of(8, 10);
    LocalTime endWork = LocalTime.of(17, 0);

    // do not count before 8:00 AM
    if (timeIn.isBefore(startWork)) {
        timeIn = startWork;
    }
    // apply grace period (first 10 minutes is treated as 8:00)
    else if (!timeIn.isAfter(gracePeriod)) {
        timeIn = startWork;
    }

    // else, 8:11 onwards will deduct

    // do not count after 5:00 PM
    if (timeOut.isAfter(endWork)) {
        timeOut = endWork;
    }

    // condition for invalid records
    if (timeOut.isBefore(timeIn)) {
        return 0.0;
    }

    long totalMinutes = Duration.between(timeIn, timeOut).toMinutes();

    // subtract 1 hour lunch break
    if (totalMinutes > 60) {
        totalMinutes -= 60;
    }

    return totalMinutes / 60.0;
}


    // convert month number to name
    public static String getMonthName(int month) {
        switch (month) {
            case 6:  return "June";
            case 7:  return "July";
            case 8:  return "August";
            case 9:  return "September";
            case 10: return "October";
            case 11: return "November";
            case 12: return "December";
            default: return "";
        }
    }


    // SSS computation
    public static double computeSSS(double salary) {
        if (salary < 3250) {
            return 135.00;
        }
        if (salary >= 24750) {
            return 1125.00;
        }
        int step = (int) ((salary - 3250) / 500);
        return 157.50 + (step * 22.50);
    }


    // PhilHealth computation
    public static double computePhilHealth(double salary) {
        if (salary < 10000) {
            salary = 10000;
        }
        if (salary > 60000) {
            salary = 60000;
        }
        double premium = salary * 0.03;
        return premium / 2;
    }


    // Pag-IBIG computation
    public static double computePagIbig(double salary) {
        if (salary <= 1500) {
            return salary * 0.01;
        } else {
            return salary * 0.02;
        }
    }


    // Withholding tax computation
    public static double computeTax(double taxableIncome) {
        if (taxableIncome <= 20832) {
            return 0;
        } else if (taxableIncome < 33333) {
            return (taxableIncome - 20833) * 0.20;
        } else if (taxableIncome < 66667) {
            return 2500 + (taxableIncome - 33333) * 0.25;
        } else if (taxableIncome < 166667) {
            return 10833.33 + (taxableIncome - 66667) * 0.30;
        } else if (taxableIncome < 666667) {
            return 40833.33 + (taxableIncome - 166667) * 0.32;
        } else {
            return 200833.33 + (taxableIncome - 666667) * 0.35;
        }
    }
}
