
import org.nocrala.tools.texttablefmt.BorderStyle;
import org.nocrala.tools.texttablefmt.ShownBorders;
import org.nocrala.tools.texttablefmt.Table;

import java.util.Arrays;
import java.util.Scanner;

public class Main {
//   for coloring text
    enum TEXT_COLOR{
        RESET("\u001B\0m"),
        BLACK("\u001B[30m"),
        RED("\u001B[31m"),
        GREEN("\u001B[32m"),
        YELLOW("\u001B[33m"),
        BLUE("\u001B[34m"),
        PURPLE("\u001B[35m"),
        CYAN("\u001B[36m"),
        WHITE("\u001B[37m");
        private String colorCode;
        TEXT_COLOR(String colorCode){
            this.colorCode = colorCode;
        }
        public String getColorCode() {
            return colorCode;
        }
    }
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        // set the format of the input in setting up Bus
        boolean isBusSet = false;
        int busCount, seatCount;
        boolean isOptFormatted;
        // for storing boolean value of seats
        boolean[][] availableSeat = null;
        // for storing total value of seats of each bus
        int[][] availableSeatOfBus = null;
        int option;
        do {
            if (!isBusSet) {
                System.out.println("------------------ Setting up Bus ------------------");
                busCount = userInput("-> Enter number of Bus: ");
                seatCount = userInput("-> Enter number seat of bus: ");
                availableSeat = new boolean[busCount][seatCount];
                availableSeatOfBus = new int[busCount][2];
                resetSeatOfBus(availableSeat);
                calculateAvailableSeatCount(availableSeat, availableSeatOfBus);
                isBusSet = true;
            }
            displayMenu();
            do {
                option = userInput("-> Choose option (1-5): ");
                isOptFormatted = option >= 1 && option <= 5;
                if (!isOptFormatted) {
                    System.out.println("Please enter a number between 1 and 5.");
                }
            } while (!isOptFormatted);
            switch (option) {
                case 1:
                    displayBusInformation(availableSeat, availableSeatOfBus);
                    scanner.nextLine();
                    break;
                case 2:
                    bookBusSeat(scanner, availableSeat, availableSeatOfBus);
                    scanner.nextLine();
                    break;
                case 3:
                    cancelBusSeat(availableSeat,availableSeatOfBus);
                    scanner.nextLine();
                    break;
                case 4:
                    resetSeatOfBus(availableSeat);
                    calculateAvailableSeatCount(availableSeat,availableSeatOfBus);
                    scanner.nextLine();
                    break;
                case 5:
                    System.out.println("Bye Bye");
                    break;
            }
        } while (option != 5);
    }
   // for resetting the seats of all the buses to available
    private static void resetSeatOfBus(boolean[][] availableSeat) {
        for (boolean[] booleans : availableSeat) {
            Arrays.fill(booleans, true);
        }
    }
    private static void calculateAvailableSeatCount(boolean[][] availableSeat, int[][] availableSeatOfBus) {
        for (int i = 0; i < availableSeat.length; i++) {
            int sumOfSeat = 0;
            for (int j = 0; j < availableSeat[i].length; j++) {
                if (availableSeat[i][j]) sumOfSeat += 1;
            }
            availableSeatOfBus[i][1] = sumOfSeat;
        }
    }
//    for display the option menu of system
    private static void displayMenu() {
        Table table = new Table(2, BorderStyle.UNICODE_DOUBLE_BOX_WIDE, ShownBorders.ALL);
        System.out.println("------------------------- Bus Management System ------------------------- ");
        table.addCell(TEXT_COLOR.CYAN.getColorCode()+ "no");
        table.addCell(TEXT_COLOR.CYAN.getColorCode()+"Options");
        table.addCell(TEXT_COLOR.RED.getColorCode()+"1");
        table.addCell(TEXT_COLOR.RED.getColorCode()+"Check Bus");
        table.addCell(TEXT_COLOR.RED.getColorCode()+"2");
        table.addCell(TEXT_COLOR.RED.getColorCode()+"Booking Bus");
        table.addCell(TEXT_COLOR.RED.getColorCode()+"3");
        table.addCell(TEXT_COLOR.RED.getColorCode()+"Canceling Booking");
        table.addCell(TEXT_COLOR.RED.getColorCode()+"4");
        table.addCell(TEXT_COLOR.RED.getColorCode()+"Reset Bus");
        table.addCell(TEXT_COLOR.RED.getColorCode()+"5");
        table.addCell(TEXT_COLOR.RED.getColorCode()+"Exit");
        System.out.println(table.render());
    }
//    for validating the input
    private static int userInput(String prompt) {
        Scanner scanner = new Scanner(System.in);
        int input = 0;
        boolean isFormatted;
        do {
            System.out.print(prompt);
            String userInput = scanner.nextLine();
            isFormatted = userInput.matches("[1-9]+");
            if (!isFormatted) System.out.println("Only numbers are allowed.");
            else input = Integer.parseInt(userInput);
        } while (!isFormatted);
        return input;
    }
//    for displaying bus information
    private static void displayBusInformation(boolean[][] availableSeat, int[][] availableSeatOfBus) {
        Scanner scanner = new Scanner(System.in);
        Table table = new Table(4, BorderStyle.UNICODE_ROUND_BOX_WIDE, ShownBorders.ALL);
        table.setColumnWidth(0,6,6);
        table.setColumnWidth(1,20,20);
        table.setColumnWidth(2,20,20);
        table.setColumnWidth(3,20,20);

        table.addCell(TEXT_COLOR.CYAN.getColorCode()+"Id");
        table.addCell(TEXT_COLOR.CYAN.getColorCode()+"Seat");
        table.addCell(TEXT_COLOR.CYAN.getColorCode()+"Available");
        table.addCell(TEXT_COLOR.CYAN.getColorCode()+"Unavailable");
        for (int i = 0; i < availableSeat.length; i++) {
            table.addCell(TEXT_COLOR.RED.getColorCode()+ (i + 1));
            table.addCell(TEXT_COLOR.RED.getColorCode()+ availableSeat[i].length);
            table.addCell(TEXT_COLOR.RED.getColorCode()+ availableSeatOfBus[i][1]);
            table.addCell(TEXT_COLOR.RED.getColorCode()+ (availableSeat[i].length - availableSeatOfBus[i][1]));
        }
        System.out.println(table.render());
        int subOpt;
        System.out.print("=> Enter 0 to back or Bus ID to see detail: ");
        subOpt = scanner.nextInt();
        if (subOpt == 0){
            System.out.println();
        } else if (subOpt != 0) {
            displayEachBusInfo(availableSeat, availableSeatOfBus, subOpt - 1);
        }
    }
//    for booking seats
    private static void bookBusSeat(Scanner scanner, boolean[][] availableSeat, int[][] availableSeatOfBus) {
        int selectedBusId = userInput("Enter bus's ID: ");
        displayEachBusInfo(availableSeat, availableSeatOfBus, selectedBusId - 1);
        int selectedSeatId = userInput("-> Enter Chair number to booking: ");
        String seatConfirm;
        boolean isConfirmed;
        do {
            System.out.print("Do you want to book chair number " + selectedSeatId + " ? (y/n): ");
            seatConfirm = scanner.nextLine();
            isConfirmed = seatConfirm.matches("[yY]");
        } while (!isConfirmed && !seatConfirm.matches("[nN]"));
        if (isConfirmed) {
            System.out.println("Chair number " + selectedSeatId + " was booked successfully.");
            availableSeat[selectedBusId - 1][selectedSeatId - 1] = false;
            calculateAvailableSeatCount(availableSeat, availableSeatOfBus);
        }
    }
//    for displaying an information of individual bus
private static void displayEachBusInfo(boolean[][] availableSeat, int[][] availableSeatOfBus, int busId) {

    System.out.println("------------------------------ Display Bus Information ------------------------------");
    int pageSize = 20;
    int totalPages = (int) Math.ceil((double) availableSeat[busId].length / pageSize);

    Scanner scanner = new Scanner(System.in);

    int currentPage = 0;
    do {
        int startIndex = currentPage * pageSize;
        int endIndex = Math.min((currentPage + 1) * pageSize, availableSeat[busId].length);

        Table table = new Table(5, BorderStyle.UNICODE_ROUND_BOX_WIDE, ShownBorders.ALL);
        for (int i = startIndex; i < endIndex; i++) {
            table.addCell(TEXT_COLOR.GREEN.getColorCode()+"(" + (availableSeat[busId][i] ? "+" : "-") + ") " + (i + 1));
        }
        System.out.println(table.render());

        System.out.printf("\n%-15s( - ) : Unavailable ( %d )%-20s( + ) : Available ( %d )\n\n",
                "", availableSeat[busId].length - availableSeatOfBus[busId][1],
                "", availableSeatOfBus[busId][1]);

        System.out.printf("Page %d/%d.Enter option:\n 0 to go back,\n 1 for next page,\n 2 for previous page,\n 3 for first page,\n 4 for last page:\n ",
                currentPage + 1, totalPages);
        int userChoice = scanner.nextInt();

        switch (userChoice) {
            case 0:
                return;
            case 1:
                currentPage = Math.min(currentPage + 1, totalPages - 1);
                break;
            case 2:
                currentPage = Math.max(currentPage - 1, 0);
                break;
            case 3:
                currentPage = 0;
                break;
            case 4:
                currentPage = totalPages - 1;
                break;
            default:
                System.out.println("Invalid choice. Please enter 0, 1, 2, 3, or 4.");
        }

    } while (true);
}
    //    for canceling seats
    private static void cancelBusSeat(boolean[][] availableSeat, int[][] availableSeatOfBus){
        int busId;
        busId = userInput("-> Enter bus's ID: ");
        displayEachBusInfo(availableSeat, availableSeatOfBus, busId - 1);
        int seatId = userInput("-> Enter Seat number to cancel booking: ");
        availableSeat[busId - 1][seatId - 1] = true;
        calculateAvailableSeatCount(availableSeat, availableSeatOfBus);
    }
}