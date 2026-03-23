/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.motorph2;

/**
 *
 * @author ninadelosreyes
 */
import java.io.FileWriter;
import java.io.IOException;

public class MotorPH2 {

    public static void main(String[] args) {

        // ===== Employee Data =====
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
            "1989-01-27","1992-02-09","1990-11-16","1990-08-07"
        };

        double[] hourlyRate = {
            535.71,357.14,357.14,357.14,313.51,
            313.51,255.80,133.93,133.93,313.51,
            302.53,229.02,142.86,142.86,318.45,
            255.80,249.11,133.93,133.93,138.39,
            138.39,142.86,133.93,133.93,142.86,
            147.32,147.32,142.86,133.93,133.93,
            133.93,313.51,313.51,313.51
        };

        // Hours (Cutoff 1 & 2)
        double[][] hoursWorked = new double[34][2];
        for (int i = 0; i < 34; i++) {
            hoursWorked[i][0] = 80;
            hoursWorked[i][1] = 85;
        }

        try {
            FileWriter writer = new FileWriter("MotorPH2.csv");

            // ===== CSV HEADER =====
            writer.append("EmpNumber,Name,Birthday,Cutoff,HoursWorked,Gross,SSS,PhilHealth,PagIBIG,Tax,TotalDeduction,NetSalary\n");

            // ===== PROCESS DATA =====
            for (int i = 0; i < empNumbers.length; i++) {

                for (int j = 0; j < 2; j++) {

                    double hours = hoursWorked[i][j];

                    if (hours >= 40)
                        hours -= 1;

                    double gross = hours * hourlyRate[i];
                    double sss = gross * 0.045;
                    double philhealth = gross * 0.02;
                    double pagibig = gross * 0.01;
                    double tax = gross * 0.10;

                    double totalDeduction = sss + philhealth + pagibig + tax;
                    double net = gross - totalDeduction;

                    String cutoff = (j == 0) ? "June 1-15" : "June 16-30";

                    writer.append(empNumbers[i] + ",")
                          .append(empNames[i] + ",")
                          .append(empBirthday[i] + ",")
                          .append(cutoff + ",")
                          .append(hours + ",")
                          .append(gross + ",")
                          .append(sss + ",")
                          .append(philhealth + ",")
                          .append(pagibig + ",")
                          .append(tax + ",")
                          .append(totalDeduction + ",")
                          .append(net + "\n");
                }
            }

            writer.close();
            System.out.println("CSV file created successfully!");

        } catch (IOException e) {
            System.out.println("Error writing file.");
        }
    }
}
