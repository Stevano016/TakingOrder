package view;

import controller.AuthController;
import controller.CustomerController;
import java.awt.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import model.MenuItem;
import model.Order;
import model.OrderItem;

public class CustomerFrame extends JFrame {
    private AuthController authController;
    private CustomerController customerController;
    
    // Components
    private JTabbedPane tabbedPane;
    
    // Menu Tab Components
    private JTable menuTable;
    private DefaultTableModel menuTableModel;
    private JComboBox<String> categoryComboBox;
    private JButton viewAllButton, viewByCategoryButton, addToCartButton;
    private JSpinner quantitySpinner;
    
    // Cart Components (Quick Access)
    private JButton cartButton;
    private JLabel cartCountLabel;
    private JDialog cartDialog;
    private JTable cartTable;
    private DefaultTableModel cartTableModel;
    private JLabel totalLabel;
    private JButton removeFromCartButton, clearCartButton, quickCheckoutButton, cancelOrderButton;
    
    // Cart data
    private List<OrderItem> cartItems;
    private BigDecimal cartTotal;
    
    public CustomerFrame(AuthController authController) {
        this.authController = authController;
        this.customerController = new CustomerController();
        this.cartItems = new ArrayList<>();
        this.cartTotal = BigDecimal.ZERO;
        
        initializeComponents();
        setupLayout();
        setupEventHandlers();
        loadData();
    }
    
    private void initializeComponents() {
        setTitle("Dashboard Customer - " + authController.getCurrentUser().getFullName());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 700);
        setLocationRelativeTo(null);
        
        tabbedPane = new JTabbedPane();
        
        // Menu Tab Components
        String[] menuColumns = {"ID", "Nama", "Kategori", "Harga", "Deskripsi"};
        menuTableModel = new DefaultTableModel(menuColumns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        menuTable = new JTable(menuTableModel);
        menuTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        categoryComboBox = new JComboBox<>();
        viewAllButton = new JButton("Lihat Semua Menu");
        viewAllButton.setBackground(new Color(46, 204, 113));
        viewAllButton.setForeground(Color.WHITE);
        
        viewByCategoryButton = new JButton("Filter Kategori");
        viewByCategoryButton.setBackground(new Color(52, 152, 219));
        viewByCategoryButton.setForeground(Color.WHITE);
        
        quantitySpinner = new JSpinner(new SpinnerNumberModel(1, 1, 100, 1));
        addToCartButton = new JButton("Tambah ke Keranjang");
        addToCartButton.setBackground(new Color(230, 126, 34));
        addToCartButton.setForeground(Color.WHITE);
        
        // Cart Button (Quick Access)
        cartButton = new JButton("KERANJANG");
        cartButton.setFont(new Font("Arial", Font.BOLD, 16));
        cartButton.setBackground(new Color(46, 204, 113));
        cartButton.setForeground(Color.WHITE);
        cartButton.setPreferredSize(new Dimension(150, 50));
        cartButton.setFocusPainted(false);
        
        cartCountLabel = new JLabel("0");
        cartCountLabel.setFont(new Font("Arial", Font.BOLD, 12));
        cartCountLabel.setForeground(Color.WHITE);
        cartCountLabel.setOpaque(true);
        cartCountLabel.setBackground(new Color(231, 76, 60));
        cartCountLabel.setHorizontalAlignment(SwingConstants.CENTER);
        cartCountLabel.setPreferredSize(new Dimension(25, 25));
        cartCountLabel.setBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2));
        
        // Cart Dialog Components
        String[] cartColumns = {"Nama Menu", "Harga", "Qty", "Subtotal"};
        cartTableModel = new DefaultTableModel(cartColumns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        cartTable = new JTable(cartTableModel);
        cartTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        totalLabel = new JLabel("Total: Rp0");
        totalLabel.setFont(new Font("Arial", Font.BOLD, 18));
        totalLabel.setForeground(new Color(46, 204, 113));
        
        removeFromCartButton = new JButton("Hapus Item");
        removeFromCartButton.setBackground(new Color(231, 76, 60));
        removeFromCartButton.setForeground(Color.WHITE);
        
        clearCartButton = new JButton("Kosongkan Keranjang");
        clearCartButton.setBackground(new Color(149, 165, 166));
        clearCartButton.setForeground(Color.WHITE);
        
        quickCheckoutButton = new JButton("CHECKOUT SEKARANG");
        quickCheckoutButton.setBackground(new Color(46, 204, 113));
        quickCheckoutButton.setForeground(Color.WHITE);
        quickCheckoutButton.setFont(new Font("Arial", Font.BOLD, 14));
        quickCheckoutButton.setPreferredSize(new Dimension(200, 40));
        
        cancelOrderButton = new JButton("BATAL");
        cancelOrderButton.setBackground(new Color(231, 76, 60));
        cancelOrderButton.setForeground(Color.WHITE);
        cancelOrderButton.setFont(new Font("Arial", Font.BOLD, 14));
    }
    
    private void setupLayout() {
        setLayout(new BorderLayout());
        getContentPane().setBackground(new Color(248, 249, 250));
        
        // Modern Header with gradient and cart
        JPanel headerPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                GradientPaint gp = new GradientPaint(0, 0, new Color(231, 76, 60), getWidth(), 0, new Color(192, 57, 43));
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        headerPanel.setPreferredSize(new Dimension(0, 90));
        headerPanel.setLayout(new BorderLayout());
        headerPanel.setBorder(BorderFactory.createEmptyBorder(15, 25, 15, 25));
        
        // Header Left - Title
        JPanel headerLeft = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        headerLeft.setOpaque(false);
        
        JLabel iconLabel = new JLabel("🛒");
        iconLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 32));
        
        JPanel titlePanel = new JPanel(new GridLayout(2, 1, 0, -5));
        titlePanel.setOpaque(false);
        titlePanel.setBorder(BorderFactory.createEmptyBorder(0, 15, 0, 0));
        
        JLabel titleLabel = new JLabel("CUSTOMER PORTAL");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(Color.WHITE);
        
        JLabel userLabel = new JLabel("Welcome, " + authController.getCurrentUser().getFullName() + " - Order Online!");
        userLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        userLabel.setForeground(new Color(255, 255, 255, 200));
        
        titlePanel.add(titleLabel);
        titlePanel.add(userLabel);
        
        headerLeft.add(iconLabel);
        headerLeft.add(titlePanel);
        
        // Header Right - Cart and Logout
        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 5));
        rightPanel.setOpaque(false);
        
        // Modern Cart Button with Badge
        JPanel cartPanel = new JPanel();
        cartPanel.setOpaque(false);
        cartPanel.setLayout(new OverlayLayout(cartPanel));
        
        // Cart button styling
        cartButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        cartButton.setPreferredSize(new Dimension(140, 45));
        cartButton.setBorderPainted(false);
        cartButton.setFocusPainted(false);
        cartButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // Cart badge styling
        cartCountLabel.setFont(new Font("Segoe UI", Font.BOLD, 11));
        cartCountLabel.setPreferredSize(new Dimension(22, 22));
        cartCountLabel.setBorder(BorderFactory.createEmptyBorder());
        
        // Position badge
        JPanel badgePanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, -5, -5));
        badgePanel.setOpaque(false);
        badgePanel.add(cartCountLabel);
        
        cartPanel.add(badgePanel);
        cartPanel.add(cartButton);
        
        // Logout button
        JButton logoutButton = new JButton("🚪 Logout");
        logoutButton.setFont(new Font("Segoe UI", Font.BOLD, 12));
        logoutButton.setBackground(new Color(52, 73, 94));
        logoutButton.setForeground(Color.WHITE);
        logoutButton.setBorderPainted(false);
        logoutButton.setFocusPainted(false);
        logoutButton.setPreferredSize(new Dimension(100, 35));
        logoutButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        logoutButton.addActionListener(e -> logout());
        
        rightPanel.add(cartPanel);
        rightPanel.add(logoutButton);
        
        headerPanel.add(headerLeft, BorderLayout.WEST);
        headerPanel.add(rightPanel, BorderLayout.EAST);
        
        // Setup modern menu tab
        setupMenuTab();
        
        add(headerPanel, BorderLayout.NORTH);
        add(tabbedPane, BorderLayout.CENTER);
        
        // Setup modern cart dialog
        setupCartDialog();
    }
    
    private void setupMenuTab() {
        JPanel menuPanel = new JPanel(new BorderLayout(15, 15));
        menuPanel.setBackground(new Color(248, 249, 250));
        menuPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Modern Control Panel
        JPanel controlPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 10));
        controlPanel.setBackground(Color.WHITE);
        controlPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(233, 236, 239), 1),
            BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(3, 0, 0, 0, new Color(52, 152, 219)),
                BorderFactory.createEmptyBorder(15, 20, 15, 20)
            )
        ));
        
        JLabel filterIcon = new JLabel("🔍");
        filterIcon.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 16));
        
        JLabel filterLabel = new JLabel("Filter Menu:");
        filterLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        filterLabel.setForeground(new Color(52, 73, 94));
        
        categoryComboBox.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        categoryComboBox.setPreferredSize(new Dimension(150, 30));
        
        // Modern buttons
        viewByCategoryButton.setFont(new Font("Segoe UI", Font.BOLD, 11));
        viewByCategoryButton.setBorderPainted(false);
        viewByCategoryButton.setFocusPainted(false);
        viewByCategoryButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        viewByCategoryButton.setPreferredSize(new Dimension(120, 30));
        
        viewAllButton.setFont(new Font("Segoe UI", Font.BOLD, 11));
        viewAllButton.setBorderPainted(false);
        viewAllButton.setFocusPainted(false);
        viewAllButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        viewAllButton.setPreferredSize(new Dimension(120, 30));
        
        controlPanel.add(filterIcon);
        controlPanel.add(filterLabel);
        controlPanel.add(categoryComboBox);
        controlPanel.add(viewByCategoryButton);
        controlPanel.add(viewAllButton);
        
        // Modern Table Panel
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBackground(Color.WHITE);
        tablePanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(233, 236, 239), 1),
            BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(3, 0, 0, 0, new Color(46, 204, 113)),
                BorderFactory.createEmptyBorder(15, 15, 15, 15)
            )
        ));
        
        JLabel menuIcon = new JLabel("🍽️ Daftar Menu");
        menuIcon.setFont(new Font("Segoe UI", Font.BOLD, 14));
        menuIcon.setForeground(new Color(46, 204, 113));
        menuIcon.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
        
        // Modern table styling
        menuTable.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        menuTable.setRowHeight(45);
        menuTable.setGridColor(new Color(233, 236, 239));
        menuTable.setSelectionBackground(new Color(46, 204, 113, 50));
        menuTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        menuTable.getTableHeader().setBackground(new Color(46, 204, 113));
        menuTable.getTableHeader().setForeground(Color.WHITE);
        menuTable.getTableHeader().setPreferredSize(new Dimension(0, 40));
        
        JScrollPane menuScrollPane = new JScrollPane(menuTable);
        menuScrollPane.setBorder(BorderFactory.createEmptyBorder());
        menuScrollPane.getViewport().setBackground(Color.WHITE);
        
        tablePanel.add(menuIcon, BorderLayout.NORTH);
        tablePanel.add(menuScrollPane, BorderLayout.CENTER);
        
        // Modern Add to Cart Panel
        JPanel addToCartPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 15));
        addToCartPanel.setBackground(Color.WHITE);
        addToCartPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(233, 236, 239), 1),
            BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(3, 0, 0, 0, new Color(230, 126, 34)),
                BorderFactory.createEmptyBorder(15, 20, 15, 20)
            )
        ));
        
        JLabel cartIcon = new JLabel("🛒");
        cartIcon.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 18));
        
        JLabel qtyLabel = new JLabel("Jumlah:");
        qtyLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        qtyLabel.setForeground(new Color(52, 73, 94));
        
        quantitySpinner.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        quantitySpinner.setPreferredSize(new Dimension(60, 30));
        
        addToCartButton.setFont(new Font("Segoe UI", Font.BOLD, 12));
        addToCartButton.setBorderPainted(false);
        addToCartButton.setFocusPainted(false);
        addToCartButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        addToCartButton.setPreferredSize(new Dimension(180, 35));
        
        addToCartPanel.add(cartIcon);
        addToCartPanel.add(qtyLabel);
        addToCartPanel.add(quantitySpinner);
        addToCartPanel.add(addToCartButton);
        
        // Modern Info Panel
        JPanel infoPanel = new JPanel();
        infoPanel.setBackground(new Color(241, 196, 15, 30));
        infoPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(241, 196, 15), 1),
            BorderFactory.createEmptyBorder(15, 20, 15, 20)
        ));
        
        JLabel infoLabel = new JLabel("<html><center>💡 <b>Tips:</b> Klik tombol <b>KERANJANG</b> di header untuk checkout langsung!<br/>Double-click menu untuk melihat detail lengkap.</center></html>");
        infoLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        infoLabel.setForeground(new Color(127, 140, 141));
        infoPanel.add(infoLabel);
        
        menuPanel.add(controlPanel, BorderLayout.NORTH);
        menuPanel.add(tablePanel, BorderLayout.CENTER);
        
        JPanel bottomPanel = new JPanel(new BorderLayout(0, 10));
        bottomPanel.setOpaque(false);
        bottomPanel.add(addToCartPanel, BorderLayout.NORTH);
        bottomPanel.add(infoPanel, BorderLayout.SOUTH);
        menuPanel.add(bottomPanel, BorderLayout.SOUTH);
        
        tabbedPane.addTab("🍽️ Menu", menuPanel);
    }
    
    private void setupCartDialog() {
        cartDialog = new JDialog(this, "Keranjang Belanja", true);
        cartDialog.setSize(600, 500);
        cartDialog.setLocationRelativeTo(this);
        cartDialog.setLayout(new BorderLayout());
        
        // Header
        JPanel dialogHeader = new JPanel();
        dialogHeader.setBackground(new Color(46, 204, 113));
        dialogHeader.setPreferredSize(new Dimension(0, 50));
        
        JLabel dialogTitle = new JLabel("KERANJANG BELANJA");
        dialogTitle.setFont(new Font("Arial", Font.BOLD, 18));
        dialogTitle.setForeground(Color.WHITE);
        dialogTitle.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 0));
        
        dialogHeader.add(dialogTitle);
        
        // Cart Table
        JScrollPane cartScrollPane = new JScrollPane(cartTable);
        cartScrollPane.setBorder(BorderFactory.createTitledBorder("Items dalam Keranjang"));
        
        // Controls Panel
        JPanel controlsPanel = new JPanel(new GridBagLayout());
        controlsPanel.setBorder(BorderFactory.createTitledBorder("Kontrol Pesanan"));
        controlsPanel.setPreferredSize(new Dimension(0, 150));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        
        // Item controls
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        controlsPanel.add(removeFromCartButton, gbc);
        
        gbc.gridx = 1; gbc.gridy = 0;
        controlsPanel.add(clearCartButton, gbc);
        
        // Total
        gbc.gridx = 0; gbc.gridy = 1; gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        controlsPanel.add(totalLabel, gbc);
        
        // Action buttons
        JPanel actionPanel = new JPanel(new FlowLayout());
        actionPanel.add(quickCheckoutButton);
        actionPanel.add(cancelOrderButton);
        
        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        controlsPanel.add(actionPanel, gbc);
        
        cartDialog.add(dialogHeader, BorderLayout.NORTH);
        cartDialog.add(cartScrollPane, BorderLayout.CENTER);
        cartDialog.add(controlsPanel, BorderLayout.SOUTH);
    }
    
    private void setupEventHandlers() {
        viewAllButton.addActionListener(e -> loadAllMenu());
        viewByCategoryButton.addActionListener(e -> loadMenuByCategory());
        addToCartButton.addActionListener(e -> addToCart());
        cartButton.addActionListener(e -> showCartDialog());
        removeFromCartButton.addActionListener(e -> removeFromCart());
        clearCartButton.addActionListener(e -> clearCart());
        quickCheckoutButton.addActionListener(e -> quickCheckout());
        cancelOrderButton.addActionListener(e -> cartDialog.setVisible(false));
        
        // Double click to show menu details
        menuTable.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                if (e.getClickCount() == 2) {
                    showMenuDetails();
                }
            }
        });
    }
    
    private void showCartDialog() {
        if (cartItems.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "Keranjang masih kosong!\nSilakan tambahkan menu terlebih dahulu.",
                "Keranjang Kosong",
                JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        
        updateCartDisplay();
        cartDialog.setVisible(true);
    }
    
    private void quickCheckout() {
        if (cartItems.isEmpty()) {
            JOptionPane.showMessageDialog(cartDialog, "Keranjang kosong!");
            return;
        }
        
        // Show payment method selection dialog
        String[] paymentOptions = {"CASH (Bayar saat ambil)", "QRIS (Bayar sekarang)"};
        
        int paymentChoice = JOptionPane.showOptionDialog(
            cartDialog,
            "Pilih metode pembayaran:\n\nTotal: Rp" + cartTotal,
            "Pilih Pembayaran",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE,
            null,
            paymentOptions,
            paymentOptions[0]
        );
        
        if (paymentChoice == -1) return; // User cancelled
        
        Order.PaymentMethod paymentMethod = (paymentChoice == 0) ? 
            Order.PaymentMethod.CASH : Order.PaymentMethod.QRIS;
        
        // Show order confirmation
        StringBuilder orderSummary = new StringBuilder();
        orderSummary.append("KONFIRMASI PESANAN\n\n");
        
        for (OrderItem item : cartItems) {
            orderSummary.append(String.format("• %-20s x%-2d = Rp%,.0f\n",
                item.getMenuItem().getName(),
                item.getQuantity(),
                item.getSubtotal()));
        }
        
        orderSummary.append("\n" + "=".repeat(35) + "\n");
        orderSummary.append(String.format("TOTAL: Rp%,.0f\n", cartTotal));
        orderSummary.append("Pembayaran: ").append(paymentMethod == Order.PaymentMethod.CASH ? "CASH" : "QRIS").append("\n\n");
        
        if (paymentMethod == Order.PaymentMethod.CASH) {
            orderSummary.append("Bayar saat pengambilan pesanan\n");
        } else {
            orderSummary.append("Pembayaran akan diproses sekarang\n");
        }
        
        orderSummary.append("\nLanjutkan pesanan?");
        
        int confirm = JOptionPane.showConfirmDialog(
            cartDialog,
            orderSummary.toString(),
            "Konfirmasi Pesanan",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE
        );
        
        if (confirm == JOptionPane.YES_OPTION) {
            processPayment(paymentMethod);
        }
    }
    
    private void processPayment(Order.PaymentMethod paymentMethod) {
        boolean paymentSuccess = false;
        
        if (paymentMethod == Order.PaymentMethod.CASH) {
            // CASH payment - just confirm
            JOptionPane.showMessageDialog(cartDialog,
                "PEMBAYARAN CASH\n\n" +
                "Pesanan akan diproses.\n" +
                "Silakan bayar saat pengambilan pesanan.\n\n" +
                "Total yang harus dibayar: Rp" + cartTotal,
                "Pembayaran CASH",
                JOptionPane.INFORMATION_MESSAGE);
            paymentSuccess = true;
            
        } else if (paymentMethod == Order.PaymentMethod.QRIS) {
            // QRIS payment simulation
            JDialog qrisDialog = new JDialog(cartDialog, "Pembayaran QRIS", true);
            qrisDialog.setSize(400, 300);
            qrisDialog.setLocationRelativeTo(cartDialog);
            qrisDialog.setLayout(new BorderLayout());
            
            // QRIS Header
            JPanel qrisHeader = new JPanel();
            qrisHeader.setBackground(new Color(52, 152, 219));
            qrisHeader.setPreferredSize(new Dimension(0, 50));
            
            JLabel qrisTitle = new JLabel("PEMBAYARAN QRIS");
            qrisTitle.setFont(new Font("Arial", Font.BOLD, 16));
            qrisTitle.setForeground(Color.WHITE);
            qrisTitle.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 0));
            qrisHeader.add(qrisTitle);
            
            // QRIS Content
            JPanel qrisContent = new JPanel(new GridBagLayout());
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.insets = new Insets(10, 10, 10, 10);
            
            // QR Code simulation
            JLabel qrLabel = new JLabel("QR");
            qrLabel.setFont(new Font("Arial", Font.PLAIN, 80));
            qrLabel.setHorizontalAlignment(SwingConstants.CENTER);
            qrLabel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
            qrLabel.setPreferredSize(new Dimension(120, 120));
            
            gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
            qrisContent.add(qrLabel, gbc);
            
            gbc.gridx = 0; gbc.gridy = 1; gbc.gridwidth = 2;
            JLabel instructionLabel = new JLabel("<html><center>Scan QR Code dengan aplikasi pembayaran<br/>Total: <b>Rp" + cartTotal + "</b></center></html>");
            instructionLabel.setHorizontalAlignment(SwingConstants.CENTER);
            qrisContent.add(instructionLabel, gbc);
            
            // QRIS Buttons
            JPanel qrisButtonPanel = new JPanel(new FlowLayout());
            
            JButton payButton = new JButton("Sudah Bayar");
            payButton.setBackground(new Color(46, 204, 113));
            payButton.setForeground(Color.WHITE);
            
            JButton cancelButton = new JButton("Batal");
            cancelButton.setBackground(new Color(231, 76, 60));
            cancelButton.setForeground(Color.WHITE);
            
            final boolean[] qrisResult = {false};
            
            payButton.addActionListener(e -> {
                qrisResult[0] = true;
                qrisDialog.dispose();
            });
            
            cancelButton.addActionListener(e -> {
                qrisResult[0] = false;
                qrisDialog.dispose();
            });
            
            qrisButtonPanel.add(payButton);
            qrisButtonPanel.add(cancelButton);
            
            gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 2;
            qrisContent.add(qrisButtonPanel, gbc);
            
            qrisDialog.add(qrisHeader, BorderLayout.NORTH);
            qrisDialog.add(qrisContent, BorderLayout.CENTER);
            
            qrisDialog.setVisible(true);
            
            paymentSuccess = qrisResult[0];
            
            if (!paymentSuccess) {
                JOptionPane.showMessageDialog(cartDialog, "Pembayaran dibatalkan.");
                return;
            }
        }
        
        if (paymentSuccess) {
            // Process the order
            boolean orderSuccess = customerController.createOrder(
                authController.getCurrentUser().getId(),
                cartItems,
                paymentMethod
            );
            
            if (orderSuccess) {
                // Show success message
                String successMessage = String.format(
                    "PESANAN BERHASIL!\n\n" +
                    "Terima kasih, %s!\n\n" +
                    "Detail Pesanan:\n" +
                    "Total: Rp%,.0f\n" +
                    "Pembayaran: %s\n" +
                    "Status: SELESAI\n\n" +
                    "Pesanan sedang diproses.\n" +
                    "Silakan tunggu konfirmasi dari restoran.",
                    authController.getCurrentUser().getFullName(),
                    cartTotal,
                    paymentMethod == Order.PaymentMethod.CASH ? "CASH" : "QRIS"
                );
                
                JOptionPane.showMessageDialog(cartDialog,
                    successMessage,
                    "Pesanan Berhasil",
                    JOptionPane.INFORMATION_MESSAGE);
                
                // Clear cart and close dialog
                cartItems.clear();
                updateCartDisplay();
                updateCartButton();
                cartDialog.setVisible(false);
                
            } else {
                JOptionPane.showMessageDialog(cartDialog,
                    "Gagal memproses pesanan.\nSilakan coba lagi.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void loadData() {
        loadAllMenu();
        loadCategories();
        updateCartDisplay();
        updateCartButton();
    }
    
    private void loadAllMenu() {
        menuTableModel.setRowCount(0);
        List<MenuItem> menuItems = customerController.viewMenu();
        
        for (MenuItem item : menuItems) {
            Object[] row = {
                item.getId(),
                item.getName(),
                item.getCategory(),
                "Rp" + item.getPrice(),
                item.getDescription()
            };
            menuTableModel.addRow(row);
        }
    }
    
    private void loadMenuByCategory() {
        String selectedCategory = (String) categoryComboBox.getSelectedItem();
        if (selectedCategory == null || selectedCategory.equals("Semua Kategori")) {
            loadAllMenu();
            return;
        }
        
        menuTableModel.setRowCount(0);
        List<MenuItem> menuItems = customerController.getMenuByCategory(selectedCategory);
        
        for (MenuItem item : menuItems) {
            Object[] row = {
                item.getId(),
                item.getName(),
                item.getCategory(),
                "Rp" + item.getPrice(),
                item.getDescription()
            };
            menuTableModel.addRow(row);
        }
    }
    
    private void loadCategories() {
        categoryComboBox.removeAllItems();
        categoryComboBox.addItem("Semua Kategori");
        
        List<String> categories = customerController.getAvailableCategories();
        for (String category : categories) {
            categoryComboBox.addItem(category);
        }
    }
    
    private void addToCart() {
        int selectedRow = menuTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Pilih menu yang akan ditambahkan ke keranjang!");
            return;
        }
        
        int menuId = (Integer) menuTableModel.getValueAt(selectedRow, 0);
        String menuName = (String) menuTableModel.getValueAt(selectedRow, 1);
        String priceText = (String) menuTableModel.getValueAt(selectedRow, 3);
        BigDecimal price = new BigDecimal(priceText.replace("Rp", ""));
        
        int quantity = (Integer) quantitySpinner.getValue();
        
        // Check if item already exists in cart
        boolean found = false;
        for (OrderItem item : cartItems) {
            if (item.getMenuItemId() == menuId) {
                item.setQuantity(item.getQuantity() + quantity);
                found = true;
                break;
            }
        }
        
        if (!found) {
            OrderItem orderItem = new OrderItem(0, menuId, quantity, price);
            MenuItem menuItem = new MenuItem();
            menuItem.setId(menuId);
            menuItem.setName(menuName);
            menuItem.setPrice(price);
            orderItem.setMenuItem(menuItem);
            cartItems.add(orderItem);
        }
        
        updateCartDisplay();
        updateCartButton();
        quantitySpinner.setValue(1);
        
        // Show quick confirmation with cart button highlight
        JOptionPane.showMessageDialog(this, 
            String.format("%s (x%d) ditambahkan!\n\nKlik tombol KERANJANG untuk checkout.", menuName, quantity),
            "Ditambahkan ke Keranjang", 
            JOptionPane.INFORMATION_MESSAGE);
        JOptionPane.showMessageDialog(this, 
            String.format("%s (x%d) ditambahkan!\n\nKlik tombol KERANJANG untuk checkout.", menuName, quantity),
            "Ditambahkan ke Keranjang", 
            JOptionPane.INFORMATION_MESSAGE);
    }
    
    private void removeFromCart() {
        int selectedRow = cartTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(cartDialog, "Pilih item yang akan dihapus!");
            return;
        }
        
        String itemName = cartItems.get(selectedRow).getMenuItem().getName();
        cartItems.remove(selectedRow);
        updateCartDisplay();
        updateCartButton();
        
        JOptionPane.showMessageDialog(cartDialog, itemName + " dihapus dari keranjang.");
        
        if (cartItems.isEmpty()) {
            cartDialog.setVisible(false);
        }
    }
    
    private void clearCart() {
        if (cartItems.isEmpty()) {
            JOptionPane.showMessageDialog(cartDialog, "Keranjang sudah kosong!");
            return;
        }
        
        int confirm = JOptionPane.showConfirmDialog(cartDialog,
            "Kosongkan semua item dari keranjang?",
            "Konfirmasi",
            JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION) {
            cartItems.clear();
            updateCartDisplay();
            updateCartButton();
            cartDialog.setVisible(false);
            JOptionPane.showMessageDialog(this, "Keranjang berhasil dikosongkan.");
        }
    }
    
    private void updateCartDisplay() {
        cartTableModel.setRowCount(0);
        cartTotal = BigDecimal.ZERO;
        
        for (OrderItem item : cartItems) {
            Object[] row = {
                item.getMenuItem().getName(),
                "Rp" + item.getUnitPrice(),
                item.getQuantity(),
                "Rp" + item.getSubtotal()
            };
            cartTableModel.addRow(row);
            cartTotal = cartTotal.add(item.getSubtotal());
        }
        
        totalLabel.setText("Total: Rp" + cartTotal);
        
        // Enable/disable buttons
        boolean hasItems = !cartItems.isEmpty();
        quickCheckoutButton.setEnabled(hasItems);
        removeFromCartButton.setEnabled(hasItems);
        clearCartButton.setEnabled(hasItems);
    }
    
    private void updateCartButton() {
        int itemCount = cartItems.size();
        
        if (itemCount > 0) {
            cartCountLabel.setText(String.valueOf(itemCount));
            cartCountLabel.setVisible(true);
            cartButton.setBackground(new Color(230, 126, 34)); // Orange when has items
        } else {
            cartCountLabel.setVisible(false);
            cartButton.setBackground(new Color(46, 204, 113)); // Green when empty
        }
        
        repaint();
    }
    
    private void showMenuDetails() {
        int selectedRow = menuTable.getSelectedRow();
        if (selectedRow != -1) {
            String menuInfo = String.format(
                "DETAIL MENU\n\n" +
                "ID: %s\n" +
                "Nama: %s\n" +
                "Kategori: %s\n" +
                "Harga: %s\n" +
                "Deskripsi: %s\n\n" +
                "Klik 'Tambah ke Keranjang' untuk memesan.",
                menuTableModel.getValueAt(selectedRow, 0),
                menuTableModel.getValueAt(selectedRow, 1),
                menuTableModel.getValueAt(selectedRow, 2),
                menuTableModel.getValueAt(selectedRow, 3),
                menuTableModel.getValueAt(selectedRow, 4)
            );
            
            JOptionPane.showMessageDialog(this, menuInfo, "Detail Menu", JOptionPane.INFORMATION_MESSAGE);
        }
    }
    
    private void logout() {
        if (!cartItems.isEmpty()) {
            int confirm = JOptionPane.showConfirmDialog(this,
                "Anda memiliki " + cartItems.size() + " item di keranjang.\n" +
                "Item akan hilang jika logout. Yakin ingin logout?",
                "Konfirmasi Logout",
                JOptionPane.YES_NO_OPTION);
            
            if (confirm != JOptionPane.YES_OPTION) {
                return;
            }
        }
        
        int confirm = JOptionPane.showConfirmDialog(this, 
            "Yakin ingin logout?", 
            "Konfirmasi", 
            JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION) {
            authController.logout();
            dispose();
            new LoginFrame().setVisible(true);
        }
    }
}
