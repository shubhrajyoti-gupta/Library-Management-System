package com.library.entity;

import javax.persistence.*;
import java.util.Date;
import java.util.concurrent.TimeUnit;

@Entity
@Table(name = "borrows")
public class Borrow {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "book_id", nullable = false)
    private Book book;
    
    @ManyToOne
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;
    
    @Temporal(TemporalType.DATE)
    @Column(nullable = false)
    private Date borrowDate;
    
    @Temporal(TemporalType.DATE)
    @Column(nullable = false)
    private Date dueDate;
    
    @Temporal(TemporalType.DATE)
    private Date returnDate;
    
    @Column(nullable = false)
    private Boolean isReturned = false;
    
    private Double fineAmount = 0.0;
    
    public Borrow() {}
    
    public Borrow(Book book, Member member, Date borrowDate, Date dueDate) {
        this.book = book;
        this.member = member;
        this.borrowDate = borrowDate;
        this.dueDate = dueDate;
        this.isReturned = false;
        this.fineAmount = 0.0;
    }
    
    // Calculate fine (1 dollar per day overdue)
    public void calculateFine() {
        if (isReturned && returnDate != null && returnDate.after(dueDate)) {
            long diffInMillies = Math.abs(returnDate.getTime() - dueDate.getTime());
            long diffInDays = TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS);
            this.fineAmount = diffInDays * 1.0; // $1 per day
        } else if (!isReturned && new Date().after(dueDate)) {
            long diffInMillies = Math.abs(new Date().getTime() - dueDate.getTime());
            long diffInDays = TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS);
            this.fineAmount = diffInDays * 1.0;
        }
    }
    
    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public Book getBook() { return book; }
    public void setBook(Book book) { this.book = book; }
    
    public Member getMember() { return member; }
    public void setMember(Member member) { this.member = member; }
    
    public Date getBorrowDate() { return borrowDate; }
    public void setBorrowDate(Date borrowDate) { this.borrowDate = borrowDate; }
    
    public Date getDueDate() { return dueDate; }
    public void setDueDate(Date dueDate) { this.dueDate = dueDate; }
    
    public Date getReturnDate() { return returnDate; }
    public void setReturnDate(Date returnDate) { this.returnDate = returnDate; }
    
    public Boolean getIsReturned() { return isReturned; }
    public void setIsReturned(Boolean returned) { isReturned = returned; }
    
    public Double getFineAmount() { return fineAmount; }
    public void setFineAmount(Double fineAmount) { this.fineAmount = fineAmount; }
}
