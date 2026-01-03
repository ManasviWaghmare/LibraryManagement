import java.io.Serializable; // Required for saving/loading data
import java.util.ArrayList;

public class Member implements Serializable {
    private int memberId; // Unique identifier
    private String name;
    // We store the ISBNs of the books they currently have checked out
    private ArrayList<String> borrowedBookIsbns; 

    // Constructor
    public Member(int memberId, String name) {
        this.memberId = memberId;
        this.name = name;
        this.borrowedBookIsbns = new ArrayList<>();
    }

    // --- Getters ---
    public int getMemberId() {
        return memberId;
    }

    public String getName() {
        return name;
    }

    public ArrayList<String> getBorrowedBookIsbns() {
        return borrowedBookIsbns;
    }
    
    // --- Core Member Methods ---
    public void borrowBook(String isbn) {
        borrowedBookIsbns.add(isbn);
    }
    
    public void returnBook(String isbn) {
        borrowedBookIsbns.remove(isbn);
    }

    public void displayDetails() {
        System.out.println("Member ID: " + memberId + ", Name: " + name + ", Books Borrowed: " + borrowedBookIsbns.size());
    }
}
