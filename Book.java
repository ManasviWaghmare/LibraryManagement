import java.io.Serializable; // <--- FIX IS HERE

public class Book implements Serializable {
    private String title;
    private String author;
    private String isbn; // Unique identifier
    private boolean isBorrowed;

    // Constructor
    public Book(String title, String author, String isbn) {
        this.title = title;
        this.author = author;
        this.isbn = isbn;
        this.isBorrowed = false; // By default, a new book is not borrowed
    }

    // --- Getters and Setters ---
    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public String getIsbn() {
        return isbn;
    }

    public boolean isBorrowed() {
        return isBorrowed;
    }

    public void setBorrowed(boolean borrowed) {
        isBorrowed = borrowed;
    }
    
    // --- Utility Method ---
    public void displayDetails() {
        System.out.println("Title: " + title + 
                           ", Author: " + author + 
                           ", ISBN: " + isbn + 
                           ", Status: " + (isBorrowed ? "Borrowed" : "Available"));
    }
}
