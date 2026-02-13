package com.library.ui;

import com.library.dao.*;
import com.library.entity.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class LibraryManagementUI extends JFrame {
    
    private BookDAO bookDAO = new BookDAO();
    private MemberDAO memberDAO = new MemberDAO();
    private BorrowDAO borrowDAO = new BorrowDAO();
    private PurchaseDAO purchaseDAO = new PurchaseDAO();
    
    private JTabbedPane tabbedPane;
    
    public LibraryManagementUI() {
        setTitle("Library Management System");
        setSize(1000, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        tabbedPane = new JTabbedPane();
        
        tabbedPane.addTab("Books", createBookPanel());
        tabbedPane.addTab("Members", createMemberPanel());
        tabbedPane.addTab("Borrow/Return", createBorrowPanel());
        tabbedPane.addTab("Purchases", createPurchasePanel());
        tabbedPane.addTab("Fines", createFinePanel());
        
        add(tabbedPane);
        setVisible(true);
    }
    
    // ==================== BOOK PANEL ====================
    private JPanel createBookPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Form Panel
        JPanel formPanel = new JPanel(new GridLayout(8, 2, 5, 5));
        JTextField titleField = new JTextField();
        JTextField authorField = new JTextField();
        JTextField isbnField = new JTextField();
        JTextField categoryField = new JTextField();
        JTextField quantityField = new JTextField();
        JTextField priceField = new JTextField();
        
        formPanel.add(new JLabel("Title:"));
        formPanel.add(titleField);
        formPanel.add(new JLabel("Author:"));
        formPanel.add(authorField);
        formPanel.add(new JLabel("ISBN:"));
        formPanel.add(isbnField);
        formPanel.add(new JLabel("Category:"));
        formPanel.add(categoryField);
        formPanel.add(new JLabel("Quantity:"));
        formPanel.add(quantityField);
        formPanel.add(new JLabel("Price:"));
        formPanel.add(priceField);
        
        // Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton addButton = new JButton("Add Book");
        JButton removeButton = new JButton("Remove Book");
        JButton searchButton = new JButton("Search");
        JTextField searchField = new JTextField(20);
        
        buttonPanel.add(searchField);
        buttonPanel.add(searchButton);
        buttonPanel.add(addButton);
        buttonPanel.add(removeButton);
        
        formPanel.add(new JLabel("Search:"));
        formPanel.add(buttonPanel);
        
        // Table
        String[] columns = {"ID", "Title", "Author", "ISBN", "Category", "Quantity", "Available", "Price"};
        DefaultTableModel tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        JTable table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);
        
        // Load books
        loadBooks(tableModel);
        
        // Add button action
        addButton.addActionListener(e -> {
            try {
                String title = titleField.getText();
                String author = authorField.getText();
                String isbn = isbnField.getText();
                String category = categoryField.getText();
                int quantity = Integer.parseInt(quantityField.getText());
                double price = Double.parseDouble(priceField.getText());
                
                Book book = new Book(title, author, isbn, category, quantity, price, new Date());
                bookDAO.save(book);
                
                JOptionPane.showMessageDialog(this, "Book added successfully!");
                loadBooks(tableModel);
                clearFields(titleField, authorField, isbnField, categoryField, quantityField, priceField);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
            }
        });
        
        // Remove button action
        removeButton.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow >= 0) {
                Long id = (Long) tableModel.getValueAt(selectedRow, 0);
                int confirm = JOptionPane.showConfirmDialog(this, 
                    "Are you sure you want to remove this book?", 
                    "Confirm", JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    bookDAO.delete(id);
                    loadBooks(tableModel);
                    JOptionPane.showMessageDialog(this, "Book removed successfully!");
                }
            } else {
                JOptionPane.showMessageDialog(this, "Please select a book to remove.");
            }
        });
        
        // Search action
        searchButton.addActionListener(e -> {
            String keyword = searchField.getText();
            if (!keyword.isEmpty()) {
                loadSearchedBooks(tableModel, keyword);
            } else {
                loadBooks(tableModel);
            }
        });
        
        panel.add(formPanel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
        
        return panel;
    }
    
    // ==================== MEMBER PANEL ====================
    private JPanel createMemberPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Form Panel
        JPanel formPanel = new JPanel(new GridLayout(5, 2, 5, 5));
        JTextField nameField = new JTextField();
        JTextField emailField = new JTextField();
        JTextField phoneField = new JTextField();
        JTextField addressField = new JTextField();
        
        formPanel.add(new JLabel("Name:"));
        formPanel.add(nameField);
        formPanel.add(new JLabel("Email:"));
        formPanel.add(emailField);
        formPanel.add(new JLabel("Phone:"));
        formPanel.add(phoneField);
        formPanel.add(new JLabel("Address:"));
        formPanel.add(addressField);
        
        // Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton addButton = new JButton("Add Member");
        buttonPanel.add(addButton);
        formPanel.add(new JLabel(""));
        formPanel.add(buttonPanel);
        
        // Table
        String[] columns = {"ID", "Name", "Email", "Phone", "Address", "Registration Date"};
        DefaultTableModel tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        JTable table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);
        
        loadMembers(tableModel);
        
        // Add button action
        addButton.addActionListener(e -> {
            try {
                String name = nameField.getText();
                String email = emailField.getText();
                String phone = phoneField.getText();
                String address = addressField.getText();
                
                Member member = new Member(name, email, phone, address);
                memberDAO.save(member);
                
                JOptionPane.showMessageDialog(this, "Member added successfully!");
                loadMembers(tableModel);
                clearFields(nameField, emailField, phoneField, addressField);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
            }
        });
        
        panel.add(formPanel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
        
        return panel;
    }
    
    // ==================== BORROW PANEL ====================
    private JPanel createBorrowPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Form Panel
        JPanel formPanel = new JPanel(new GridLayout(6, 2, 5, 5));
        JComboBox<String> bookCombo = new JComboBox<>();
        JComboBox<String> memberCombo = new JComboBox<>();
        JTextField daysField = new JTextField("14"); // Default 14 days
        
        loadBookCombo(bookCombo);
        loadMemberCombo(memberCombo);
        
        formPanel.add(new JLabel("Select Book:"));
        formPanel.add(bookCombo);
        formPanel.add(new JLabel("Select Member:"));
        formPanel.add(memberCombo);
        formPanel.add(new JLabel("Borrow Duration (days):"));
        formPanel.add(daysField);
        
        // Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton borrowButton = new JButton("Borrow Book");
        JButton returnButton = new JButton("Return Book");
        buttonPanel.add(borrowButton);
        buttonPanel.add(returnButton);
        
        formPanel.add(new JLabel(""));
        formPanel.add(buttonPanel);
        
        // Table
        String[] columns = {"ID", "Book", "Member", "Borrow Date", "Due Date", "Return Date", "Status", "Fine"};
        DefaultTableModel tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        JTable table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);
        
        loadBorrows(tableModel);
        
        // Borrow button action
        borrowButton.addActionListener(e -> {
            try {
                String bookStr = (String) bookCombo.getSelectedItem();
                String memberStr = (String) memberCombo.getSelectedItem();
                int days = Integer.parseInt(daysField.getText());
                
                if (bookStr == null || memberStr == null) {
                    JOptionPane.showMessageDialog(this, "Please select both book and member!");
                    return;
                }
                
                Long bookId = Long.parseLong(bookStr.split(" - ")[0]);
                Long memberId = Long.parseLong(memberStr.split(" - ")[0]);
                
                Book book = bookDAO.findById(bookId);
                Member member = memberDAO.findById(memberId);
                
                if (book.getAvailableQuantity() <= 0) {
                    JOptionPane.showMessageDialog(this, "Book is not available!");
                    return;
                }
                
                Date borrowDate = new Date();
                Calendar cal = Calendar.getInstance();
                cal.setTime(borrowDate);
                cal.add(Calendar.DAY_OF_MONTH, days);
                Date dueDate = cal.getTime();
                
                Borrow borrow = new Borrow(book, member, borrowDate, dueDate);
                borrowDAO.save(borrow);
                
                // Update book availability
                book.setAvailableQuantity(book.getAvailableQuantity() - 1);
                bookDAO.update(book);
                
                JOptionPane.showMessageDialog(this, "Book borrowed successfully!");
                loadBorrows(tableModel);
                loadBookCombo(bookCombo);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
            }
        });
        
        // Return button action
        returnButton.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow >= 0) {
                Long id = (Long) tableModel.getValueAt(selectedRow, 0);
                Borrow borrow = borrowDAO.findById(id);
                
                if (borrow.getIsReturned()) {
                    JOptionPane.showMessageDialog(this, "Book already returned!");
                    return;
                }
                
                borrow.setReturnDate(new Date());
                borrow.setIsReturned(true);
                borrow.calculateFine();
                borrowDAO.update(borrow);
                
                // Update book availability
                Book book = borrow.getBook();
                book.setAvailableQuantity(book.getAvailableQuantity() + 1);
                bookDAO.update(book);
                
                String message = "Book returned successfully!";
                if (borrow.getFineAmount() > 0) {
                    message += "\nFine: $" + String.format("%.2f", borrow.getFineAmount());
                }
                JOptionPane.showMessageDialog(this, message);
                
                loadBorrows(tableModel);
                loadBookCombo(bookCombo);
            } else {
                JOptionPane.showMessageDialog(this, "Please select a borrow record!");
            }
        });
        
        panel.add(formPanel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
        
        return panel;
    }
    
    // ==================== PURCHASE PANEL ====================
    private JPanel createPurchasePanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Form Panel
        JPanel formPanel = new JPanel(new GridLayout(5, 2, 5, 5));
        JComboBox<String> bookCombo = new JComboBox<>();
        JTextField quantityField = new JTextField();
        JTextField priceField = new JTextField();
        JTextField vendorField = new JTextField();
        
        loadBookCombo(bookCombo);
        
        formPanel.add(new JLabel("Select Book:"));
        formPanel.add(bookCombo);
        formPanel.add(new JLabel("Quantity:"));
        formPanel.add(quantityField);
        formPanel.add(new JLabel("Total Price:"));
        formPanel.add(priceField);
        formPanel.add(new JLabel("Vendor:"));
        formPanel.add(vendorField);
        
        // Button
        JButton purchaseButton = new JButton("Record Purchase");
        formPanel.add(new JLabel(""));
        formPanel.add(purchaseButton);
        
        // Table
        String[] columns = {"ID", "Book", "Quantity", "Total Price", "Purchase Date", "Vendor"};
        DefaultTableModel tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        JTable table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);
        
        loadPurchases(tableModel);
        
        // Purchase button action
        purchaseButton.addActionListener(e -> {
            try {
                String bookStr = (String) bookCombo.getSelectedItem();
                if (bookStr == null) {
                    JOptionPane.showMessageDialog(this, "Please select a book!");
                    return;
                }
                
                Long bookId = Long.parseLong(bookStr.split(" - ")[0]);
                int quantity = Integer.parseInt(quantityField.getText());
                double totalPrice = Double.parseDouble(priceField.getText());
                String vendor = vendorField.getText();
                
                Book book = bookDAO.findById(bookId);
                Purchase purchase = new Purchase(book, quantity, totalPrice, new Date(), vendor);
                purchaseDAO.save(purchase);
                
                // Update book quantities
                book.setQuantity(book.getQuantity() + quantity);
                book.setAvailableQuantity(book.getAvailableQuantity() + quantity);
                bookDAO.update(book);
                
                JOptionPane.showMessageDialog(this, "Purchase recorded successfully!");
                loadPurchases(tableModel);
                loadBookCombo(bookCombo);
                clearFields(quantityField, priceField, vendorField);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
            }
        });
        
        panel.add(formPanel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
        
        return panel;
    }
    
    // ==================== FINE PANEL ====================
    private JPanel createFinePanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Table
        String[] columns = {"Borrow ID", "Member", "Book", "Due Date", "Days Overdue", "Fine ($)"};
        DefaultTableModel tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        JTable table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);
        
        // Refresh button
        JButton refreshButton = new JButton("Refresh Fines");
        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.add(refreshButton);
        
        loadOverdueFines(tableModel);
        
        refreshButton.addActionListener(e -> loadOverdueFines(tableModel));
        
        panel.add(buttonPanel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
        
        return panel;
    }
    
    // ==================== HELPER METHODS ====================
    
    private void loadBooks(DefaultTableModel model) {
        model.setRowCount(0);
        List<Book> books = bookDAO.findAll();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        for (Book book : books) {
            model.addRow(new Object[]{
                book.getId(), book.getTitle(), book.getAuthor(), book.getIsbn(),
                book.getCategory(), book.getQuantity(), book.getAvailableQuantity(), 
                "$" + String.format("%.2f", book.getPrice())
            });
        }
    }
    
    private void loadSearchedBooks(DefaultTableModel model, String keyword) {
        model.setRowCount(0);
        List<Book> books = bookDAO.searchBooks(keyword);
        for (Book book : books) {
            model.addRow(new Object[]{
                book.getId(), book.getTitle(), book.getAuthor(), book.getIsbn(),
                book.getCategory(), book.getQuantity(), book.getAvailableQuantity(), 
                "$" + String.format("%.2f", book.getPrice())
            });
        }
    }
    
    private void loadMembers(DefaultTableModel model) {
        model.setRowCount(0);
        List<Member> members = memberDAO.findAll();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        for (Member member : members) {
            model.addRow(new Object[]{
                member.getId(), member.getName(), member.getEmail(), 
                member.getPhone(), member.getAddress(), 
                sdf.format(member.getRegistrationDate())
            });
        }
    }
    
    private void loadBorrows(DefaultTableModel model) {
        model.setRowCount(0);
        List<Borrow> borrows = borrowDAO.findAll();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        for (Borrow borrow : borrows) {
            borrow.calculateFine();
            model.addRow(new Object[]{
                borrow.getId(),
                borrow.getBook().getTitle(),
                borrow.getMember().getName(),
                sdf.format(borrow.getBorrowDate()),
                sdf.format(borrow.getDueDate()),
                borrow.getReturnDate() != null ? sdf.format(borrow.getReturnDate()) : "Not Returned",
                borrow.getIsReturned() ? "Returned" : "Active",
                "$" + String.format("%.2f", borrow.getFineAmount())
            });
        }
    }
    
    private void loadPurchases(DefaultTableModel model) {
        model.setRowCount(0);
        List<Purchase> purchases = purchaseDAO.findAll();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        for (Purchase purchase : purchases) {
            model.addRow(new Object[]{
                purchase.getId(),
                purchase.getBook().getTitle(),
                purchase.getQuantity(),
                "$" + String.format("%.2f", purchase.getTotalPrice()),
                sdf.format(purchase.getPurchaseDate()),
                purchase.getVendor()
            });
        }
    }
    
    private void loadOverdueFines(DefaultTableModel model) {
        model.setRowCount(0);
        List<Borrow> overdueBorrows = borrowDAO.findOverdueBorrows();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        
        for (Borrow borrow : overdueBorrows) {
            borrow.calculateFine();
            long daysOverdue = (new Date().getTime() - borrow.getDueDate().getTime()) 
                             / (1000 * 60 * 60 * 24);
            
            model.addRow(new Object[]{
                borrow.getId(),
                borrow.getMember().getName(),
                borrow.getBook().getTitle(),
                sdf.format(borrow.getDueDate()),
                daysOverdue,
                "$" + String.format("%.2f", borrow.getFineAmount())
            });
        }
    }
    
    private void loadBookCombo(JComboBox<String> combo) {
        combo.removeAllItems();
        List<Book> books = bookDAO.findAll();
        for (Book book : books) {
            combo.addItem(book.getId() + " - " + book.getTitle() + 
                         " (Available: " + book.getAvailableQuantity() + ")");
        }
    }
    
    private void loadMemberCombo(JComboBox<String> combo) {
        combo.removeAllItems();
        List<Member> members = memberDAO.findAll();
        for (Member member : members) {
            combo.addItem(member.getId() + " - " + member.getName());
        }
    }
    
    private void clearFields(JTextField... fields) {
        for (JTextField field : fields) {
            field.setText("");
        }
    }
    
    // ==================== MAIN ====================
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new LibraryManagementUI());
    }
}
