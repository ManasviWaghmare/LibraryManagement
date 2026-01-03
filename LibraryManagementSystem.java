import java.util.InputMismatchException;
import java.util.Scanner;

public class LibraryManagementSystem {
    
    // =======================================================
    //             MAIN EXECUTION METHOD
    // =======================================================

    public static void main(String[] args) {
        // 1. Initialize the library by attempting to LOAD saved data
        Library lib = Library.loadData();
        Scanner scanner = new Scanner(System.in);
        
        // CRITICAL FIX: Only add default data if the library is brand new (no members loaded).
        // This prevents data duplication every time the program starts.
        if (lib.getMembers().isEmpty()) {
            System.out.println("Adding initial sample data...");
            lib.addBook(new Book("The Alchemist", "Paulo Coelho", "978-0061122415"));
            lib.addBook(new Book("1984", "George Orwell", "978-0451524935"));
            lib.addMember("Alice Johnson"); // ID 1001
            lib.addMember("Bob Smith");     // ID 1002
        }

        // Main application loop
        boolean running = true;
        while (running) {
            displayMenu();
            
            try {
                System.out.print("Enter your choice (1-6): ");
                int choice = scanner.nextInt();
                scanner.nextLine(); // Consume the newline

                switch (choice) {
                    case 1: // Add Book
                        addBook(lib, scanner);
                        break;
                    case 2: // Add Member
                        addMember(lib, scanner);
                        break;
                    case 3: // Display All Books
                        lib.displayAllBooks();
                        break;
                    case 4: // Borrow Book
                        borrowBook(lib, scanner);
                        break;
                    case 5: // Return Book
                        returnBook(lib, scanner);
                        break;
                    case 6: // Exit
                        lib.saveData(); // Save all data before exiting
                        running = false;
                        System.out.println("\nThank you for using the Library Management System. Goodbye!");
                        break;
                    default:
                        System.out.println("Invalid choice. Please enter a number between 1 and 6.");
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a number for the menu choice.");
                scanner.nextLine(); // Clear the invalid input
            }
        }
        scanner.close();
    }
    
    // =======================================================
    //             HELPER MENU METHODS
    // =======================================================
    
    private static void displayMenu() {
        System.out.println("\n==================================");
        System.out.println("  📚 Library Management System 📚");
        System.out.println("==================================");
        System.out.println("1. Add New Book");
        System.out.println("2. Add New Member");
        System.out.println("3. Display All Books");
        System.out.println("4. Borrow Book");
        System.out.println("5. Return Book");
        System.out.println("6. Exit");
        System.out.println("----------------------------------");
    }
    
    private static void addBook(Library lib, Scanner scanner) {
        System.out.print("Enter Title: ");
        String title = scanner.nextLine();
        System.out.print("Enter Author: ");
        String author = scanner.nextLine();
        System.out.print("Enter ISBN: ");
        String isbn = scanner.nextLine();
        
        // Create and add the new Book object
        lib.addBook(new Book(title, author, isbn));
    }
    
    private static void addMember(Library lib, Scanner scanner) {
        System.out.print("Enter Member Name: ");
        String name = scanner.nextLine();
        lib.addMember(name);
    }
    
    private static void borrowBook(Library lib, Scanner scanner) {
        try {
            System.out.print("Enter ISBN of the book to borrow: ");
            String isbn = scanner.nextLine();
            System.out.print("Enter Member ID: ");
            int memberId = scanner.nextInt();
            scanner.nextLine(); // Consume newline
            
            lib.borrowBook(isbn, memberId);
        } catch (InputMismatchException e) {
            System.out.println("Invalid input. Member ID must be a number.");
            scanner.nextLine();
        }
    }
    
    private static void returnBook(Library lib, Scanner scanner) {
        try {
            System.out.print("Enter ISBN of the book to return: ");
            String isbn = scanner.nextLine();
            System.out.print("Enter Member ID: ");
            int memberId = scanner.nextInt();
            scanner.nextLine(); // Consume newline
            
            lib.returnBook(isbn, memberId);
        } catch (InputMismatchException e) {
            System.out.println("Invalid input. Member ID must be a number.");
            scanner.nextLine();
        }
    }
}
