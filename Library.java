import java.io.*; // For serialization (saving/loading data)
import java.util.ArrayList;
import java.util.Optional; // Used for safer searching

public class Library implements Serializable { // Implementing Serializable is CRITICAL for saving
    
    // --- Attributes ---
    private ArrayList<Book> books;
    private ArrayList<Member> members;
    private int nextMemberId;
    
    // File name for persistence
    private static final String FILE_NAME = "library_data.ser";

    // Constructor
    public Library() {
        this.books = new ArrayList<>();
        this.members = new ArrayList<>();
        this.nextMemberId = 1001; // Starting ID for new members
    }

    // =======================================================
    //             CORE MANAGEMENT METHODS
    // =======================================================

    // 1. Add Book
    public void addBook(Book book) {
        // Check for duplicate ISBN before adding
        if (findBook(book.getIsbn()).isPresent()) {
            System.out.println("Error: Book with ISBN " + book.getIsbn() + " already exists.");
            return;
        }
        books.add(book);
        System.out.println("Success: Book added to the library.");
    }
    
    // 2. Add Member (Member name is passed as parameter)
    public void addMember(String name) {
        Member newMember = new Member(nextMemberId++, name); // ID is used, then incremented
        members.add(newMember);
        System.out.println("Success: Member added. ID: " + newMember.getMemberId()); 
    }

    // 3. Display All Books
    public void displayAllBooks() {
        if (books.isEmpty()) {
            System.out.println("The library currently has no books.");
            return;
        }
        System.out.println("\n--- All Books in Library ---");
        for (Book book : books) {
            book.displayDetails();
        }
    }
    
    // 4. Borrow Book
    public void borrowBook(String isbn, int memberId) {
        Optional<Book> bookOpt = findBook(isbn);
        Optional<Member> memberOpt = findMember(memberId);

        if (bookOpt.isEmpty()) {
            System.out.println("Error: Book with ISBN " + isbn + " not found.");
            return;
        }
        if (memberOpt.isEmpty()) {
            System.out.println("Error: Member with ID " + memberId + " not found.");
            return;
        }

        Book book = bookOpt.get();
        Member member = memberOpt.get();

        if (book.isBorrowed()) {
            System.out.println("Error: Book '" + book.getTitle() + "' is already borrowed.");
            return;
        }
        
        // Perform the transaction
        book.setBorrowed(true);
        member.borrowBook(isbn);
        System.out.println("Success: Member " + member.getName() + " borrowed '" + book.getTitle() + "'.");
    }
    
    // 5. Return Book
    public void returnBook(String isbn, int memberId) {
        Optional<Book> bookOpt = findBook(isbn);
        Optional<Member> memberOpt = findMember(memberId);

        if (bookOpt.isEmpty() || memberOpt.isEmpty()) {
            System.out.println("Error: Book or Member not found.");
            return;
        }

        Book book = bookOpt.get();
        Member member = memberOpt.get();

        if (!book.isBorrowed() || !member.getBorrowedBookIsbns().contains(isbn)) {
            System.out.println("Error: Book was not checked out by this member or is already available.");
            return;
        }
        
        // Perform the transaction
        book.setBorrowed(false);
        member.returnBook(isbn);
        System.out.println("Success: Member " + member.getName() + " returned '" + book.getTitle() + "'.");
    }

    // =======================================================
    //             HELPER SEARCH METHODS
    // =======================================================
    
    // Finds a book object by ISBN
    private Optional<Book> findBook(String isbn) {
        return books.stream()
                    .filter(b -> b.getIsbn().equals(isbn))
                    .findFirst();
    }

    // Finds a member object by ID
    private Optional<Member> findMember(int memberId) {
        return members.stream()
                      .filter(m -> m.getMemberId() == memberId)
                      .findFirst();
    }


    // =======================================================
    //             INTEGRATION GETTER
    // =======================================================
    
    // Necessary for LibraryManagementSystem to check if data was loaded
    public ArrayList<Member> getMembers() {
        return members;
    }


    // =======================================================
    //             PERSISTENCE METHODS
    // =======================================================

    // 1. Method to Save Data (Serialization)
    public void saveData() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_NAME))) {
            // Write all lists and the next ID to the file
            oos.writeObject(books);
            oos.writeObject(members);
            oos.writeInt(nextMemberId);
            System.out.println("Data saved successfully to " + FILE_NAME);
        } catch (IOException e) {
            System.err.println("Error saving data: " + e.getMessage());
        }
    }

    // 2. Static Method to Load Data (Deserialization)
    public static Library loadData() {
        File file = new File(FILE_NAME);
        if (file.exists()) {
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(FILE_NAME))) {
                Library loadedLib = new Library();
                
                // Read objects in the exact order they were saved
                loadedLib.books = (ArrayList<Book>) ois.readObject();
                loadedLib.members = (ArrayList<Member>) ois.readObject();
                loadedLib.nextMemberId = ois.readInt();
                
                System.out.println("Data loaded successfully from " + FILE_NAME);
                return loadedLib;

            } catch (IOException | ClassNotFoundException e) {
                System.err.println("Error loading data. Starting new library: " + e.getMessage());
                return new Library(); // Return an empty library if loading fails
            }
        } else {
            System.out.println("No saved data found. Starting a new library.");
            return new Library();
        }
    }
}
