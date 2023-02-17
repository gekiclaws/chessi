package view;

// Adapted from https://docs.oracle.com/javase/tutorial/uiswing/examples/layout/index.html#CardLayoutDemo

import java.awt.*;
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.*;

import javax.swing.*;

import model.Bishop;
import model.Chessi;
import model.King;
import model.Knight;
import model.Move;
import model.Pawn;
import model.Piece;
import model.Queen;
import model.Rook;
import model.Square;
//import model.board;

public class Interface {
    JPanel cards; //a panel that uses CardLayout
    private static JFrame frame;
    private Chessi app;
    private Square currentSquare;
    private Color currentColor;
    private Map<String, JTextField> squares = new HashMap<>();
    
    
    public void addComponentToPane(Container pane) {
    	app = new Chessi();
    	
        //Create the panel that contains the windows
        cards = new JPanel(new CardLayout());
        
        // Main window
        JPanel mainWindow = new JPanel();
        cards.add(mainWindow, "mainWindow");
        mainWindow.setLayout(null);
        
        JButton analysisButton = new JButton("Analysis board");
        analysisButton.setText("<html><center>"+"Analysis"+"<br>"+"board"+"</center></html>");
        analysisButton.setFont(new Font("Dialog", Font.BOLD, 40));
        analysisButton.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		CardLayout cl = (CardLayout)(cards.getLayout());
                cl.show(cards, "analysisWindow");
        	}
        });
        analysisButton.setBounds(39, 104, 300, 250);
        mainWindow.add(analysisButton);
        
        JButton gteButton = new JButton("<html><center>Guess the<br>eval</center></html>");
        gteButton.setFont(new Font("Dialog", Font.BOLD, 40));
        gteButton.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		CardLayout cl = (CardLayout)(cards.getLayout());
                cl.show(cards, "gteWindow");
        	}
        });
        
        gteButton.setBounds(398, 104, 300, 250);
        mainWindow.add(gteButton);
        
        // Analysis window
        
        JPanel analysisWindow = new JPanel();
        cards.add(analysisWindow, "analysisWindow");
        GridBagLayout gbl_analysisWindow = new GridBagLayout();
        gbl_analysisWindow.columnWidths = new int[]{450, 300, 0};
        gbl_analysisWindow.rowHeights = new int[]{470, 0};
        gbl_analysisWindow.columnWeights = new double[]{1.0, 1.0, Double.MIN_VALUE};
        gbl_analysisWindow.rowWeights = new double[]{1.0, Double.MIN_VALUE};
        analysisWindow.setLayout(gbl_analysisWindow); 
        
        // Draw the chess board
        
        JPanel chessboard = new JPanel();
        GridBagConstraints gbc_chessboard = new GridBagConstraints();
        gbc_chessboard.insets = new Insets(0, 0, 0, 0); 
        gbc_chessboard.fill = GridBagConstraints.BOTH; 
        gbc_chessboard.gridx = 0;
        gbc_chessboard.gridy = 0; 
        analysisWindow.add(chessboard, gbc_chessboard);
        
        GridBagLayout gbl_chessboard = new GridBagLayout();
        gbl_chessboard.columnWidths = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0};
        gbl_chessboard.rowHeights = new int[]{63, 63, 62, 62, 62, 62, 63, 63, 0};
        gbl_chessboard.columnWeights = new double[]{0.125, 0.125, 0.125, 0.125, 0.125, 0.125, 0.125, 0.125, Double.MIN_VALUE};
        gbl_chessboard.rowWeights = new double[]{0.125, 0.125, 0.125, 0.125, 0.125, 0.125, 0.125, 0.125, Double.MIN_VALUE};
        chessboard.setLayout(gbl_chessboard); 
        
        FocusListener myFocusListener = new FocusListener() {
        	public void focusGained(FocusEvent e) {
                Component component = e.getComponent();
                if (component instanceof JTextField) {
                    GridBagLayout layout = (GridBagLayout) component.getParent().getLayout();
                    GridBagConstraints constraints = layout.getConstraints(component);
                    update(e, new Square(constraints.gridx, constraints.gridy));
                }
                chessboard.requestFocus(); // give focus to a different component to lose focus from the square
        	}
            public void focusLost(FocusEvent e) {
            	
            }
    	};
       
        JTextField a1 = new JTextField();
        a1.setBackground(new Color(210, 180, 140));
        a1.setEditable(false);
        a1.setFont(new Font("Dialog", Font.BOLD, 40));
        a1.setBorder(javax.swing.BorderFactory.createEmptyBorder());
        a1.setText("♖");
        a1.setHorizontalAlignment(JTextField.CENTER);
        GridBagConstraints gbc_a1 = new GridBagConstraints();
        gbc_a1.fill = GridBagConstraints.BOTH;
        gbc_a1.insets = new Insets(0, 0, 0, 0);
        gbc_a1.gridx = 0;
        gbc_a1.gridy = 7;
        chessboard.add(a1, gbc_a1);
        a1.addFocusListener(myFocusListener);
        
        JTextField a3 = new JTextField();
        a3.setBackground(new Color(210, 180, 140));
        a3.setEditable(false);
        a3.setFont(new Font("Dialog", Font.BOLD, 40));
        a3.setBorder(javax.swing.BorderFactory.createEmptyBorder());
        a3.setHorizontalAlignment(JTextField.CENTER);
        GridBagConstraints gbc_a3 = new GridBagConstraints();
        gbc_a3.fill = GridBagConstraints.BOTH;
        gbc_a3.insets = new Insets(0, 0, 0, 0);
        gbc_a3.gridx = 0;
        gbc_a3.gridy = 5;
        chessboard.add(a3, gbc_a3);
        a3.addFocusListener(myFocusListener);
        
        JTextField a5 = new JTextField();
        a5.setBackground(new Color(210, 180, 140));
        a5.setEditable(false);
        a5.setFont(new Font("Dialog", Font.BOLD, 40));
        a5.setBorder(javax.swing.BorderFactory.createEmptyBorder());
        a5.setHorizontalAlignment(JTextField.CENTER);
        GridBagConstraints gbc_a5 = new GridBagConstraints();
        gbc_a5.fill = GridBagConstraints.BOTH;
        gbc_a5.insets = new Insets(0, 0, 0, 0);
        gbc_a5.gridx = 0;
        gbc_a5.gridy = 3;
        chessboard.add(a5, gbc_a5);
        a5.addFocusListener(myFocusListener);
        
        JTextField a7 = new JTextField();
        a7.setBackground(new Color(210, 180, 140));
        a7.setEditable(false);
        a7.setFont(new Font("Dialog", Font.BOLD, 40));
        a7.setBorder(javax.swing.BorderFactory.createEmptyBorder());
        a7.setText("♟");
        a7.setHorizontalAlignment(JTextField.CENTER);
        GridBagConstraints gbc_a7 = new GridBagConstraints();
        gbc_a7.fill = GridBagConstraints.BOTH;
        gbc_a7.insets = new Insets(0, 0, 0, 0);
        gbc_a7.gridx = 0;
        gbc_a7.gridy = 1;
        chessboard.add(a7, gbc_a7);
        a7.addFocusListener(myFocusListener);
        
        JTextField c1 = new JTextField();
        c1.setBackground(new Color(210, 180, 140));
        c1.setEditable(false);
        c1.setFont(new Font("Dialog", Font.BOLD, 40));
        c1.setBorder(javax.swing.BorderFactory.createEmptyBorder());
        c1.setText("♗");
        c1.setHorizontalAlignment(JTextField.CENTER);
        GridBagConstraints gbc_c1 = new GridBagConstraints();
        gbc_c1.fill = GridBagConstraints.BOTH;
        gbc_c1.insets = new Insets(0, 0, 0, 0);
        gbc_c1.gridx = 2;
        gbc_c1.gridy = 7;
        chessboard.add(c1, gbc_c1);
        c1.addFocusListener(myFocusListener);

        JTextField e1 = new JTextField();
        e1.setBackground(new Color(210, 180, 140));
        e1.setEditable(false);
        e1.setFont(new Font("Dialog", Font.BOLD, 40));
        e1.setBorder(javax.swing.BorderFactory.createEmptyBorder());
        e1.setText("♔");
        e1.setHorizontalAlignment(JTextField.CENTER);
        GridBagConstraints gbc_e1 = new GridBagConstraints();
        gbc_e1.fill = GridBagConstraints.BOTH;
        gbc_e1.insets = new Insets(0, 0, 0, 0);
        gbc_e1.gridx = 4;
        gbc_e1.gridy = 7;
        chessboard.add(e1, gbc_e1);
        e1.addFocusListener(myFocusListener);

        JTextField g1 = new JTextField();
        g1.setBackground(new Color(210, 180, 140));
        g1.setEditable(false);
        g1.setFont(new Font("Dialog", Font.BOLD, 40));
        g1.setBorder(javax.swing.BorderFactory.createEmptyBorder());
        g1.setText("♘");
        g1.setHorizontalAlignment(JTextField.CENTER);
        GridBagConstraints gbc_g1 = new GridBagConstraints();
        gbc_g1.fill = GridBagConstraints.BOTH;
        gbc_g1.insets = new Insets(0, 0, 0, 0);
        gbc_g1.gridx = 6;
        gbc_g1.gridy = 7;
        chessboard.add(g1, gbc_g1);
        g1.addFocusListener(myFocusListener);

        JTextField c3 = new JTextField();
        c3.setBackground(new Color(210, 180, 140));
        c3.setEditable(false);
        c3.setFont(new Font("Dialog", Font.BOLD, 40));
        c3.setBorder(javax.swing.BorderFactory.createEmptyBorder());
        c3.setText("");
        c3.setHorizontalAlignment(JTextField.CENTER);
        GridBagConstraints gbc_c3 = new GridBagConstraints();
        gbc_c3.fill = GridBagConstraints.BOTH;
        gbc_c3.insets = new Insets(0, 0, 0, 0);
        gbc_c3.gridx = 2;
        gbc_c3.gridy = 5;
        chessboard.add(c3, gbc_c3);
        c3.addFocusListener(myFocusListener);
        
        JTextField e3 = new JTextField();
        e3.setBackground(new Color(210, 180, 140));
        e3.setEditable(false);
        e3.setFont(new Font("Dialog", Font.BOLD, 40));
        e3.setBorder(javax.swing.BorderFactory.createEmptyBorder());
        e3.setText("");
        e3.setHorizontalAlignment(JTextField.CENTER);
        GridBagConstraints gbc_e3 = new GridBagConstraints();
        gbc_e3.fill = GridBagConstraints.BOTH;
        gbc_e3.insets = new Insets(0, 0, 0, 0);
        gbc_e3.gridx = 4;
        gbc_e3.gridy = 5;
        chessboard.add(e3, gbc_e3);
        e3.addFocusListener(myFocusListener);

        JTextField g3 = new JTextField();
        g3.setBackground(new Color(210, 180, 140));
        g3.setEditable(false);
        g3.setFont(new Font("Dialog", Font.BOLD, 40));
        g3.setBorder(javax.swing.BorderFactory.createEmptyBorder());
        g3.setText("");
        g3.setHorizontalAlignment(JTextField.CENTER);
        GridBagConstraints gbc_g3 = new GridBagConstraints();
        gbc_g3.fill = GridBagConstraints.BOTH;
        gbc_g3.insets = new Insets(0, 0, 0, 0);
        gbc_g3.gridx = 6;
        gbc_g3.gridy = 5;
        chessboard.add(g3, gbc_g3);
        g3.addFocusListener(myFocusListener);
        
        JTextField c5 = new JTextField();
        c5.setBackground(new Color(210, 180, 140));
        c5.setEditable(false);
        c5.setFont(new Font("Dialog", Font.BOLD, 40));
        c5.setBorder(javax.swing.BorderFactory.createEmptyBorder());
        c5.setText("");
        c5.setHorizontalAlignment(JTextField.CENTER);
        GridBagConstraints gbc_c5 = new GridBagConstraints();
        gbc_c5.fill = GridBagConstraints.BOTH;
        gbc_c5.insets = new Insets(0, 0, 0, 0);
        gbc_c5.gridx = 2;
        gbc_c5.gridy = 3;
        chessboard.add(c5, gbc_c5);
        c5.addFocusListener(myFocusListener);

        JTextField e5 = new JTextField();
        e5.setBackground(new Color(210, 180, 140));
        e5.setEditable(false);
        e5.setFont(new Font("Dialog", Font.BOLD, 40));
        e5.setBorder(javax.swing.BorderFactory.createEmptyBorder());
        e5.setText("");
        e5.setHorizontalAlignment(JTextField.CENTER);
        GridBagConstraints gbc_e5 = new GridBagConstraints();
        gbc_e5.fill = GridBagConstraints.BOTH;
        gbc_e5.insets = new Insets(0, 0, 0, 0);
        gbc_e5.gridx = 4;
        gbc_e5.gridy = 3;
        chessboard.add(e5, gbc_e5);
        e5.addFocusListener(myFocusListener);

        JTextField g5 = new JTextField();
        g5.setBackground(new Color(210, 180, 140));
        g5.setEditable(false);
        g5.setFont(new Font("Dialog", Font.BOLD, 40));
        g5.setBorder(javax.swing.BorderFactory.createEmptyBorder());
        g5.setText("");
        g5.setHorizontalAlignment(JTextField.CENTER);
        GridBagConstraints gbc_g5 = new GridBagConstraints();
        gbc_g5.fill = GridBagConstraints.BOTH;
        gbc_g5.insets = new Insets(0, 0, 0, 0);
        gbc_g5.gridx = 6;
        gbc_g5.gridy = 3;
        chessboard.add(g5, gbc_g5);
        g5.addFocusListener(myFocusListener);

        JTextField c7 = new JTextField();
        c7.setBackground(new Color(210, 180, 140));
        c7.setEditable(false);
        c7.setFont(new Font("Dialog", Font.BOLD, 40));
        c7.setBorder(javax.swing.BorderFactory.createEmptyBorder());
        c7.setText("♟");
        c7.setHorizontalAlignment(JTextField.CENTER);
        GridBagConstraints gbc_c7 = new GridBagConstraints();
        gbc_c7.fill = GridBagConstraints.BOTH;
        gbc_c7.insets = new Insets(0, 0, 0, 0);
        gbc_c7.gridx = 2;
        gbc_c7.gridy = 1;
        chessboard.add(c7, gbc_c7);
        c7.addFocusListener(myFocusListener);

        JTextField e7 = new JTextField();
        e7.setBackground(new Color(210, 180, 140));
        e7.setEditable(false);
        e7.setFont(new Font("Dialog", Font.BOLD, 40));
        e7.setBorder(javax.swing.BorderFactory.createEmptyBorder());
        e7.setText("♟");
        e7.setHorizontalAlignment(JTextField.CENTER);
        GridBagConstraints gbc_e7 = new GridBagConstraints();
        gbc_e7.fill = GridBagConstraints.BOTH;
        gbc_e7.insets = new Insets(0, 0, 0, 0);
        gbc_e7.gridx = 4;
        gbc_e7.gridy = 1;
        chessboard.add(e7, gbc_e7);
        e7.addFocusListener(myFocusListener);

        JTextField g7 = new JTextField();
        g7.setBackground(new Color(210, 180, 140));
        g7.setEditable(false);
        g7.setFont(new Font("Dialog", Font.BOLD, 40));
        g7.setBorder(javax.swing.BorderFactory.createEmptyBorder());
        g7.setText("♟");
        g7.setHorizontalAlignment(JTextField.CENTER);
        GridBagConstraints gbc_g7 = new GridBagConstraints();
        gbc_g7.fill = GridBagConstraints.BOTH;
        gbc_g7.insets = new Insets(0, 0, 0, 0);
        gbc_g7.gridx = 6;
        gbc_g7.gridy = 1;
        chessboard.add(g7, gbc_g7);
        g7.addFocusListener(myFocusListener);
        
        JTextField b2 = new JTextField();
        b2.setBackground(new Color(210, 180, 140));
        b2.setText("♙");
        b2.setHorizontalAlignment(JTextField.CENTER);
        b2.setFont(new Font("Dialog", Font.BOLD, 40));
        b2.setBorder(javax.swing.BorderFactory.createEmptyBorder());
        b2.setEditable(false);
        GridBagConstraints gbc_b2 = new GridBagConstraints();
        gbc_b2.insets = new Insets(0, 0, 0, 0);
        gbc_b2.fill = GridBagConstraints.BOTH;
        gbc_b2.gridx = 1;
        gbc_b2.gridy = 6;
        chessboard.add(b2, gbc_b2);
        b2.addFocusListener(myFocusListener);
        
        JTextField d2 = new JTextField();
        d2.setBackground(new Color(210, 180, 140));
        d2.setText("♙");
        d2.setHorizontalAlignment(JTextField.CENTER);
        d2.setFont(new Font("Dialog", Font.BOLD, 40));
        d2.setBorder(javax.swing.BorderFactory.createEmptyBorder());
        d2.setEditable(false);
        GridBagConstraints gbc_d2 = new GridBagConstraints();
        gbc_d2.insets = new Insets(0, 0, 0, 0);
        gbc_d2.fill = GridBagConstraints.BOTH;
        gbc_d2.gridx = 3;
        gbc_d2.gridy = 6;
        chessboard.add(d2, gbc_d2);
        d2.addFocusListener(myFocusListener);

        JTextField f2 = new JTextField();
        f2.setBackground(new Color(210, 180, 140));
        f2.setText("♙");
        f2.setHorizontalAlignment(JTextField.CENTER);
        f2.setFont(new Font("Dialog", Font.BOLD, 40));
        f2.setBorder(javax.swing.BorderFactory.createEmptyBorder());
        f2.setEditable(false);
        GridBagConstraints gbc_f2 = new GridBagConstraints();
        gbc_f2.insets = new Insets(0, 0, 0, 0);
        gbc_f2.fill = GridBagConstraints.BOTH;
        gbc_f2.gridx = 5;
        gbc_f2.gridy = 6;
        chessboard.add(f2, gbc_f2);
        f2.addFocusListener(myFocusListener);

        JTextField h2 = new JTextField();
        h2.setBackground(new Color(210, 180, 140));
        h2.setText("♙");
        h2.setHorizontalAlignment(JTextField.CENTER);
        h2.setFont(new Font("Dialog", Font.BOLD, 40));
        h2.setBorder(javax.swing.BorderFactory.createEmptyBorder());
        h2.setEditable(false);
        GridBagConstraints gbc_h2 = new GridBagConstraints();
        gbc_h2.insets = new Insets(0, 0, 0, 0);
        gbc_h2.fill = GridBagConstraints.BOTH;
        gbc_h2.gridx = 7;
        gbc_h2.gridy = 6;
        chessboard.add(h2, gbc_h2);
        h2.addFocusListener(myFocusListener);
        
        JTextField b4 = new JTextField();
        b4.setBackground(new Color(210, 180, 140));
        b4.setText("");
        b4.setHorizontalAlignment(JTextField.CENTER);
        b4.setFont(new Font("Dialog", Font.BOLD, 40));
        b4.setBorder(javax.swing.BorderFactory.createEmptyBorder());
        b4.setEditable(false);
        GridBagConstraints gbc_b4 = new GridBagConstraints();
        gbc_b4.insets = new Insets(0, 0, 0, 0);
        gbc_b4.fill = GridBagConstraints.BOTH;
        gbc_b4.gridx = 1;
        gbc_b4.gridy = 4;
        chessboard.add(b4, gbc_b4);
        b4.addFocusListener(myFocusListener);
        
        JTextField d4 = new JTextField();
        d4.setBackground(new Color(210, 180, 140));
        d4.setText("");
        d4.setHorizontalAlignment(JTextField.CENTER);
        d4.setFont(new Font("Dialog", Font.BOLD, 40));
        d4.setBorder(javax.swing.BorderFactory.createEmptyBorder());
        d4.setEditable(false);
        GridBagConstraints gbc_d4 = new GridBagConstraints();
        gbc_d4.insets = new Insets(0, 0, 0, 0);
        gbc_d4.fill = GridBagConstraints.BOTH;
        gbc_d4.gridx = 3;
        gbc_d4.gridy = 4;
        chessboard.add(d4, gbc_d4);
        d4.addFocusListener(myFocusListener);

        JTextField f4 = new JTextField();
        f4.setBackground(new Color(210, 180, 140));
        f4.setText("");
        f4.setHorizontalAlignment(JTextField.CENTER);
        f4.setFont(new Font("Dialog", Font.BOLD, 40));
        f4.setBorder(javax.swing.BorderFactory.createEmptyBorder());
        f4.setEditable(false);
        GridBagConstraints gbc_f4 = new GridBagConstraints();
        gbc_f4.insets = new Insets(0, 0, 0, 0);
        gbc_f4.fill = GridBagConstraints.BOTH;
        gbc_f4.gridx = 5;
        gbc_f4.gridy = 4;
        chessboard.add(f4, gbc_f4);
        f4.addFocusListener(myFocusListener);

        JTextField h4 = new JTextField();
        h4.setBackground(new Color(210, 180, 140));
        h4.setText("");
        h4.setHorizontalAlignment(JTextField.CENTER);
        h4.setFont(new Font("Dialog", Font.BOLD, 40));
        h4.setBorder(javax.swing.BorderFactory.createEmptyBorder());
        h4.setEditable(false);
        GridBagConstraints gbc_h4 = new GridBagConstraints();
        gbc_h4.insets = new Insets(0, 0, 0, 0);
        gbc_h4.fill = GridBagConstraints.BOTH;
        gbc_h4.gridx = 7;
        gbc_h4.gridy = 4;
        chessboard.add(h4, gbc_h4);
        h4.addFocusListener(myFocusListener);
        
        JTextField b6 = new JTextField();
        b6.setBackground(new Color(210, 180, 140));
        b6.setText("");
        b6.setHorizontalAlignment(JTextField.CENTER);
        b6.setFont(new Font("Dialog", Font.BOLD, 40));
        b6.setBorder(javax.swing.BorderFactory.createEmptyBorder());
        b6.setEditable(false);
        GridBagConstraints gbc_b6 = new GridBagConstraints();
        gbc_b6.insets = new Insets(0, 0, 0, 0);
        gbc_b6.fill = GridBagConstraints.BOTH;
        gbc_b6.gridx = 1;
        gbc_b6.gridy = 2;
        chessboard.add(b6, gbc_b6);
        b6.addFocusListener(myFocusListener);

        JTextField d6 = new JTextField();
        d6.setBackground(new Color(210, 180, 140));
        d6.setText("");
        d6.setHorizontalAlignment(JTextField.CENTER);
        d6.setFont(new Font("Dialog", Font.BOLD, 40));
        d6.setBorder(javax.swing.BorderFactory.createEmptyBorder());
        d6.setEditable(false);
        GridBagConstraints gbc_d6 = new GridBagConstraints();
        gbc_d6.insets = new Insets(0, 0, 0, 0);
        gbc_d6.fill = GridBagConstraints.BOTH;
        gbc_d6.gridx = 3;
        gbc_d6.gridy = 2;
        chessboard.add(d6, gbc_d6);
        d6.addFocusListener(myFocusListener);

        JTextField f6 = new JTextField();
        f6.setBackground(new Color(210, 180, 140));
        f6.setText("");
        f6.setHorizontalAlignment(JTextField.CENTER);
        f6.setFont(new Font("Dialog", Font.BOLD, 40));
        f6.setBorder(javax.swing.BorderFactory.createEmptyBorder());
        f6.setEditable(false);
        GridBagConstraints gbc_f6 = new GridBagConstraints();
        gbc_f6.insets = new Insets(0, 0, 0, 0);
        gbc_f6.fill = GridBagConstraints.BOTH;
        gbc_f6.gridx = 5;
        gbc_f6.gridy = 2;
        chessboard.add(f6, gbc_f6);
        f6.addFocusListener(myFocusListener);

        JTextField h6 = new JTextField();
        h6.setBackground(new Color(210, 180, 140));
        h6.setText("");
        h6.setHorizontalAlignment(JTextField.CENTER);
        h6.setFont(new Font("Dialog", Font.BOLD, 40));
        h6.setBorder(javax.swing.BorderFactory.createEmptyBorder());
        h6.setEditable(false);
        GridBagConstraints gbc_h6 = new GridBagConstraints();
        gbc_h6.insets = new Insets(0, 0, 0, 0);
        gbc_h6.fill = GridBagConstraints.BOTH;
        gbc_h6.gridx = 7;
        gbc_h6.gridy = 2;
        chessboard.add(h6, gbc_h6);
        h6.addFocusListener(myFocusListener);
        
        JTextField b8 = new JTextField();
        b8.setBackground(new Color(210, 180, 140));
        b8.setText("♞");
        b8.setHorizontalAlignment(JTextField.CENTER);
        b8.setFont(new Font("Dialog", Font.BOLD, 40));
        b8.setBorder(javax.swing.BorderFactory.createEmptyBorder());
        b8.setEditable(false);
        GridBagConstraints gbc_b8 = new GridBagConstraints();
        gbc_b8.insets = new Insets(0, 0, 0, 0);
        gbc_b8.fill = GridBagConstraints.BOTH;
        gbc_b8.gridx = 1;
        gbc_b8.gridy = 0;
        chessboard.add(b8, gbc_b8);
        b8.addFocusListener(myFocusListener);
        
        JTextField d8 = new JTextField();
        d8.setBackground(new Color(210, 180, 140));
        d8.setText("♛");
        d8.setHorizontalAlignment(JTextField.CENTER);
        d8.setFont(new Font("Dialog", Font.BOLD, 40));
        d8.setBorder(javax.swing.BorderFactory.createEmptyBorder());
        d8.setEditable(false);
        GridBagConstraints gbc_d8 = new GridBagConstraints();
        gbc_d8.insets = new Insets(0, 0, 0, 0);
        gbc_d8.fill = GridBagConstraints.BOTH;
        gbc_d8.gridx = 3;
        gbc_d8.gridy = 0;
        chessboard.add(d8, gbc_d8);
        d8.addFocusListener(myFocusListener);

        JTextField f8 = new JTextField();
        f8.setBackground(new Color(210, 180, 140));
        f8.setText("♝");
        f8.setHorizontalAlignment(JTextField.CENTER);
        f8.setFont(new Font("Dialog", Font.BOLD, 40));
        f8.setBorder(javax.swing.BorderFactory.createEmptyBorder());
        f8.setEditable(false);
        GridBagConstraints gbc_f8 = new GridBagConstraints();
        gbc_f8.insets = new Insets(0, 0, 0, 0);
        gbc_f8.fill = GridBagConstraints.BOTH;
        gbc_f8.gridx = 5;
        gbc_f8.gridy = 0;
        chessboard.add(f8, gbc_f8);
        f8.addFocusListener(myFocusListener);

        JTextField h8 = new JTextField();
        h8.setBackground(new Color(210, 180, 140));
        h8.setText("♜");
        h8.setHorizontalAlignment(JTextField.CENTER);
        h8.setFont(new Font("Dialog", Font.BOLD, 40));
        h8.setBorder(javax.swing.BorderFactory.createEmptyBorder());
        h8.setEditable(false);
        GridBagConstraints gbc_h8 = new GridBagConstraints();
        gbc_h8.insets = new Insets(0, 0, 0, 0);
        gbc_h8.fill = GridBagConstraints.BOTH;
        gbc_h8.gridx = 7;
        gbc_h8.gridy = 0;
        chessboard.add(h8, gbc_h8);
        h8.addFocusListener(myFocusListener);
        
        JTextField c4 = new JTextField();
        c4.setBackground(new Color(255, 255, 255));
        c4.setText("");
        c4.setHorizontalAlignment(JTextField.CENTER);
        c4.setFont(new Font("Dialog", Font.BOLD, 40));
        c4.setBorder(javax.swing.BorderFactory.createEmptyBorder());
        c4.setEditable(false);
        GridBagConstraints gbc_c4 = new GridBagConstraints();
        gbc_c4.insets = new Insets(0, 0, 0, 0);
        gbc_c4.fill = GridBagConstraints.BOTH;
        gbc_c4.gridx = 2;
        gbc_c4.gridy = 4;
        chessboard.add(c4, gbc_c4);
        c4.addFocusListener(myFocusListener);
        
        JTextField e4 = new JTextField();
        e4.setBackground(new Color(255, 255, 255));
        e4.setText("");
        e4.setHorizontalAlignment(JTextField.CENTER);
        e4.setFont(new Font("Dialog", Font.BOLD, 40));
        e4.setBorder(javax.swing.BorderFactory.createEmptyBorder());
        e4.setEditable(false);
        GridBagConstraints gbc_e4 = new GridBagConstraints();
        gbc_e4.insets = new Insets(0, 0, 0, 0);
        gbc_e4.fill = GridBagConstraints.BOTH;
        gbc_e4.gridx = 4;
        gbc_e4.gridy = 4;
        chessboard.add(e4, gbc_e4);
        e4.addFocusListener(myFocusListener);
        
        JTextField g4 = new JTextField();
        g4.setBackground(new Color(255, 255, 255));
        g4.setText("");
        g4.setHorizontalAlignment(JTextField.CENTER);
        g4.setFont(new Font("Dialog", Font.BOLD, 40));
        g4.setBorder(javax.swing.BorderFactory.createEmptyBorder());
        g4.setEditable(false);
        GridBagConstraints gbc_g4 = new GridBagConstraints();
        gbc_g4.insets = new Insets(0, 0, 0, 0);
        gbc_g4.fill = GridBagConstraints.BOTH;
        gbc_g4.gridx = 6;
        gbc_g4.gridy = 4;
        chessboard.add(g4, gbc_g4);
        g4.addFocusListener(myFocusListener);
        
        
        JTextField b1 = new JTextField();
        b1.setBackground(new Color(255, 255, 255));
        b1.setText("♘");
        b1.setHorizontalAlignment(JTextField.CENTER);
        b1.setFont(new Font("Dialog", Font.BOLD, 40));
        b1.setBorder(javax.swing.BorderFactory.createEmptyBorder());
        b1.setEditable(false);
        GridBagConstraints gbc_b1 = new GridBagConstraints();
        gbc_b1.insets = new Insets(0, 0, 0, 0);
        gbc_b1.fill = GridBagConstraints.BOTH;
        gbc_b1.gridx = 1;
        gbc_b1.gridy = 7;
        chessboard.add(b1, gbc_b1);
        b1.addFocusListener(myFocusListener);
        
        JTextField d1 = new JTextField();
        d1.setBackground(new Color(255, 255, 255));
        d1.setText("♕");
        d1.setHorizontalAlignment(JTextField.CENTER);
        d1.setFont(new Font("Dialog", Font.BOLD, 40));
        d1.setBorder(javax.swing.BorderFactory.createEmptyBorder());
        d1.setEditable(false);
        GridBagConstraints gbc_d1 = new GridBagConstraints();
        gbc_d1.insets = new Insets(0, 0, 0, 0);
        gbc_d1.fill = GridBagConstraints.BOTH;
        gbc_d1.gridx = 3;
        gbc_d1.gridy = 7;
        chessboard.add(d1, gbc_d1);
        d1.addFocusListener(myFocusListener);

        JTextField f1 = new JTextField();
        f1.setBackground(new Color(255, 255, 255));
        f1.setText("♗");
        f1.setHorizontalAlignment(JTextField.CENTER);
        f1.setFont(new Font("Dialog", Font.BOLD, 40));
        f1.setBorder(javax.swing.BorderFactory.createEmptyBorder());
        f1.setEditable(false);
        GridBagConstraints gbc_f1 = new GridBagConstraints();
        gbc_f1.insets = new Insets(0, 0, 0, 0);
        gbc_f1.fill = GridBagConstraints.BOTH;
        gbc_f1.gridx = 5;
        gbc_f1.gridy = 7;
        chessboard.add(f1, gbc_f1);
        f1.addFocusListener(myFocusListener);

        JTextField h1 = new JTextField();
        h1.setBackground(new Color(255, 255, 255));
        h1.setText("♖");
        h1.setHorizontalAlignment(JTextField.CENTER);
        h1.setFont(new Font("Dialog", Font.BOLD, 40));
        h1.setBorder(javax.swing.BorderFactory.createEmptyBorder());
        h1.setEditable(false);
        GridBagConstraints gbc_h1 = new GridBagConstraints();
        gbc_h1.insets = new Insets(0, 0, 0, 0);
        gbc_h1.fill = GridBagConstraints.BOTH;
        gbc_h1.gridx = 7;
        gbc_h1.gridy = 7;
        chessboard.add(h1, gbc_h1);
        h1.addFocusListener(myFocusListener);
        
        JTextField b3 = new JTextField();
        b3.setBackground(new Color(255, 255, 255));
        b3.setText("");
        b3.setHorizontalAlignment(JTextField.CENTER);
        b3.setFont(new Font("Dialog", Font.BOLD, 40));
        b3.setBorder(javax.swing.BorderFactory.createEmptyBorder());
        b3.setEditable(false);
        GridBagConstraints gbc_b3 = new GridBagConstraints();
        gbc_b3.insets = new Insets(0, 0, 0, 0);
        gbc_b3.fill = GridBagConstraints.BOTH;
        gbc_b3.gridx = 1;
        gbc_b3.gridy = 5;
        chessboard.add(b3, gbc_b3);
        b3.addFocusListener(myFocusListener);
        
        JTextField d3 = new JTextField();
        d3.setBackground(new Color(255, 255, 255));
        d3.setText("");
        d3.setHorizontalAlignment(JTextField.CENTER);
        d3.setFont(new Font("Dialog", Font.BOLD, 40));
        d3.setBorder(javax.swing.BorderFactory.createEmptyBorder());
        d3.setEditable(false);
        GridBagConstraints gbc_d3 = new GridBagConstraints();
        gbc_d3.insets = new Insets(0, 0, 0, 0);
        gbc_d3.fill = GridBagConstraints.BOTH;
        gbc_d3.gridx = 3;
        gbc_d3.gridy = 5;
        chessboard.add(d3, gbc_d3);
        d3.addFocusListener(myFocusListener);

        JTextField f3 = new JTextField();
        f3.setBackground(new Color(255, 255, 255));
        f3.setText("");
        f3.setHorizontalAlignment(JTextField.CENTER);
        f3.setFont(new Font("Dialog", Font.BOLD, 40));
        f3.setBorder(javax.swing.BorderFactory.createEmptyBorder());
        f3.setEditable(false);
        GridBagConstraints gbc_f3 = new GridBagConstraints();
        gbc_f3.insets = new Insets(0, 0, 0, 0);
        gbc_f3.fill = GridBagConstraints.BOTH;
        gbc_f3.gridx = 5;
        gbc_f3.gridy = 5;
        chessboard.add(f3, gbc_f3);
        f3.addFocusListener(myFocusListener);

        JTextField h3 = new JTextField();
        h3.setBackground(new Color(255, 255, 255));
        h3.setText("");
        h3.setHorizontalAlignment(JTextField.CENTER);
        h3.setFont(new Font("Dialog", Font.BOLD, 40));
        h3.setBorder(javax.swing.BorderFactory.createEmptyBorder());
        h3.setEditable(false);
        GridBagConstraints gbc_h3 = new GridBagConstraints();
        gbc_h3.insets = new Insets(0, 0, 0, 0);
        gbc_h3.fill = GridBagConstraints.BOTH;
        gbc_h3.gridx = 7;
        gbc_h3.gridy = 5;
        chessboard.add(h3, gbc_h3);
        h3.addFocusListener(myFocusListener);
        
        JTextField a2 = new JTextField();
        a2.setBackground(new Color(255, 255, 255));
        a2.setText("♙");
        a2.setHorizontalAlignment(JTextField.CENTER);
        a2.setFont(new Font("Dialog", Font.BOLD, 40));
        a2.setBorder(javax.swing.BorderFactory.createEmptyBorder());
        a2.setEditable(false);
        GridBagConstraints gbc_a2 = new GridBagConstraints();
        gbc_a2.insets = new Insets(0, 0, 0, 0);
        gbc_a2.fill = GridBagConstraints.BOTH;
        gbc_a2.gridx = 0;
        gbc_a2.gridy = 6;
        chessboard.add(a2, gbc_a2);
        a2.addFocusListener(myFocusListener);
        
        JTextField a4 = new JTextField();
        a4.setBackground(new Color(255, 255, 255));
        a4.setText("");
        a4.setHorizontalAlignment(JTextField.CENTER);
        a4.setFont(new Font("Dialog", Font.BOLD, 40));
        a4.setBorder(javax.swing.BorderFactory.createEmptyBorder());
        a4.setEditable(false);
        GridBagConstraints gbc_a4 = new GridBagConstraints();
        gbc_a4.insets = new Insets(0, 0, 0, 0);
        gbc_a4.fill = GridBagConstraints.BOTH;
        gbc_a4.gridx = 0;
        gbc_a4.gridy = 4;
        chessboard.add(a4, gbc_a4);
        a4.addFocusListener(myFocusListener);
        
        JTextField a6 = new JTextField();
        a6.setBackground(new Color(255, 255, 255));
        a6.setText("");
        a6.setHorizontalAlignment(JTextField.CENTER);
        a6.setFont(new Font("Dialog", Font.BOLD, 40));
        a6.setBorder(javax.swing.BorderFactory.createEmptyBorder());
        a6.setEditable(false);
        GridBagConstraints gbc_a6 = new GridBagConstraints();
        gbc_a6.insets = new Insets(0, 0, 0, 0);
        gbc_a6.fill = GridBagConstraints.BOTH;
        gbc_a6.gridx = 0;
        gbc_a6.gridy = 2;
        chessboard.add(a6, gbc_a6);
        a6.addFocusListener(myFocusListener);
        
        JTextField a8 = new JTextField();
        a8.setBackground(new Color(255, 255, 255));
        a8.setText("♜");
        a8.setHorizontalAlignment(JTextField.CENTER);
        a8.setFont(new Font("Dialog", Font.BOLD, 40));
        a8.setBorder(javax.swing.BorderFactory.createEmptyBorder());
        a8.setEditable(false);
        GridBagConstraints gbc_a8 = new GridBagConstraints();
        gbc_a8.insets = new Insets(0, 0, 0, 0);
        gbc_a8.fill = GridBagConstraints.BOTH;
        gbc_a8.gridx = 0;
        gbc_a8.gridy = 0;
        chessboard.add(a8, gbc_a8);
        a8.addFocusListener(myFocusListener);
        
        JTextField c2 = new JTextField();
        c2.setBackground(new Color(255, 255, 255));
        c2.setText("♙");
        c2.setHorizontalAlignment(JTextField.CENTER);
        c2.setFont(new Font("Dialog", Font.BOLD, 40));
        c2.setBorder(javax.swing.BorderFactory.createEmptyBorder());
        c2.setEditable(false);
        GridBagConstraints gbc_c2 = new GridBagConstraints();
        gbc_c2.insets = new Insets(0, 0, 0, 0);
        gbc_c2.fill = GridBagConstraints.BOTH;
        gbc_c2.gridx = 2;
        gbc_c2.gridy = 6;
        chessboard.add(c2, gbc_c2);
        c2.addFocusListener(myFocusListener);
        
        JTextField e2 = new JTextField();
        e2.setBackground(new Color(255, 255, 255));
        e2.setText("♙");
        e2.setHorizontalAlignment(JTextField.CENTER);
        e2.setFont(new Font("Dialog", Font.BOLD, 40));
        e2.setBorder(javax.swing.BorderFactory.createEmptyBorder());
        e2.setEditable(false);
        GridBagConstraints gbc_e2 = new GridBagConstraints();
        gbc_e2.insets = new Insets(0, 0, 0, 0);
        gbc_e2.fill = GridBagConstraints.BOTH;
        gbc_e2.gridx = 4;
        gbc_e2.gridy = 6;
        chessboard.add(e2, gbc_e2);
        e2.addFocusListener(myFocusListener);
        
        JTextField g2 = new JTextField();
        g2.setBackground(new Color(255, 255, 255));
        g2.setText("♙");
        g2.setHorizontalAlignment(JTextField.CENTER);
        g2.setFont(new Font("Dialog", Font.BOLD, 40));
        g2.setBorder(javax.swing.BorderFactory.createEmptyBorder());
        g2.setEditable(false);
        GridBagConstraints gbc_g2 = new GridBagConstraints();
        gbc_g2.insets = new Insets(0, 0, 0, 0);
        gbc_g2.fill = GridBagConstraints.BOTH;
        gbc_g2.gridx = 6;
        gbc_g2.gridy = 6;
        chessboard.add(g2, gbc_g2);
        g2.addFocusListener(myFocusListener);
        
        JTextField c6 = new JTextField();
        c6.setBackground(new Color(255, 255, 255));
        c6.setText("");
        c6.setHorizontalAlignment(JTextField.CENTER);
        c6.setFont(new Font("Dialog", Font.BOLD, 40));
        c6.setBorder(javax.swing.BorderFactory.createEmptyBorder());
        c6.setEditable(false);
        GridBagConstraints gbc_c6 = new GridBagConstraints();
        gbc_c6.insets = new Insets(0, 0, 0, 0);
        gbc_c6.fill = GridBagConstraints.BOTH;
        gbc_c6.gridx = 2;
        gbc_c6.gridy = 2;
        chessboard.add(c6, gbc_c6);
        c6.addFocusListener(myFocusListener);

        JTextField e6 = new JTextField();
        e6.setBackground(new Color(255, 255, 255));
        e6.setText("");
        e6.setHorizontalAlignment(JTextField.CENTER);
        e6.setFont(new Font("Dialog", Font.BOLD, 40));
        e6.setBorder(javax.swing.BorderFactory.createEmptyBorder());
        e6.setEditable(false);
        GridBagConstraints gbc_e6 = new GridBagConstraints();
        gbc_e6.insets = new Insets(0, 0, 0, 0);
        gbc_e6.fill = GridBagConstraints.BOTH;
        gbc_e6.gridx = 4;
        gbc_e6.gridy = 2;
        chessboard.add(e6, gbc_e6);
        e6.addFocusListener(myFocusListener);

        JTextField g6 = new JTextField();
        g6.setBackground(new Color(255, 255, 255));
        g6.setText("");
        g6.setHorizontalAlignment(JTextField.CENTER);
        g6.setFont(new Font("Dialog", Font.BOLD, 40));
        g6.setBorder(javax.swing.BorderFactory.createEmptyBorder());
        g6.setEditable(false);
        GridBagConstraints gbc_g6 = new GridBagConstraints();
        gbc_g6.insets = new Insets(0, 0, 0, 0);
        gbc_g6.fill = GridBagConstraints.BOTH;
        gbc_g6.gridx = 6;
        gbc_g6.gridy = 2;
        chessboard.add(g6, gbc_g6);
        g6.addFocusListener(myFocusListener);

        JTextField b5 = new JTextField();
        b5.setBackground(new Color(255, 255, 255));
        b5.setText("");
        b5.setHorizontalAlignment(JTextField.CENTER);
        b5.setFont(new Font("Dialog", Font.BOLD, 40));
        b5.setBorder(javax.swing.BorderFactory.createEmptyBorder());
        b5.setEditable(false);
        GridBagConstraints gbc_b5 = new GridBagConstraints();
        gbc_b5.insets = new Insets(0, 0, 0, 0);
        gbc_b5.fill = GridBagConstraints.BOTH;
        gbc_b5.gridx = 1;
        gbc_b5.gridy = 3;
        chessboard.add(b5, gbc_b5);
        b5.addFocusListener(myFocusListener);
        
        JTextField d5 = new JTextField();
        d5.setBackground(new Color(255, 255, 255));
        d5.setText("");
        d5.setHorizontalAlignment(JTextField.CENTER);
        d5.setFont(new Font("Dialog", Font.BOLD, 40));
        d5.setBorder(javax.swing.BorderFactory.createEmptyBorder());
        d5.setEditable(false);
        GridBagConstraints gbc_d5 = new GridBagConstraints();
        gbc_d5.insets = new Insets(0, 0, 0, 0);
        gbc_d5.fill = GridBagConstraints.BOTH;
        gbc_d5.gridx = 3;
        gbc_d5.gridy = 3;
        chessboard.add(d5, gbc_d5);
        d5.addFocusListener(myFocusListener);
        
        JTextField f5 = new JTextField();
        f5.setBackground(new Color(255, 255, 255));
        f5.setText("");
        f5.setHorizontalAlignment(JTextField.CENTER);
        f5.setFont(new Font("Dialog", Font.BOLD, 40));
        f5.setBorder(javax.swing.BorderFactory.createEmptyBorder());
        f5.setEditable(false);
        GridBagConstraints gbc_f5 = new GridBagConstraints();
        gbc_f5.insets = new Insets(0, 0, 0, 0);
        gbc_f5.fill = GridBagConstraints.BOTH;
        gbc_f5.gridx = 5; // change gridx value
        gbc_f5.gridy = 3;
        chessboard.add(f5, gbc_f5);
        f5.addFocusListener(myFocusListener);
        
        JTextField h5 = new JTextField();
        h5.setBackground(new Color(255, 255, 255));
        h5.setText("");
        h5.setHorizontalAlignment(JTextField.CENTER);
        h5.setFont(new Font("Dialog", Font.BOLD, 40));
        h5.setBorder(javax.swing.BorderFactory.createEmptyBorder());
        h5.setEditable(false);
        GridBagConstraints gbc_h5 = new GridBagConstraints();
        gbc_h5.insets = new Insets(0, 0, 0, 0);
        gbc_h5.fill = GridBagConstraints.BOTH;
        gbc_h5.gridx = 7; // change gridx value
        gbc_h5.gridy = 3;
        chessboard.add(h5, gbc_h5);
        h5.addFocusListener(myFocusListener);
        
        JTextField b7 = new JTextField();
        b7.setBackground(new Color(255, 255, 255));
        b7.setText("♟");
        b7.setHorizontalAlignment(JTextField.CENTER);
        b7.setFont(new Font("Dialog", Font.BOLD, 40));
        b7.setBorder(javax.swing.BorderFactory.createEmptyBorder());
        b7.setEditable(false);
        GridBagConstraints gbc_b7 = new GridBagConstraints();
        gbc_b7.insets = new Insets(0, 0, 0, 0);
        gbc_b7.fill = GridBagConstraints.BOTH;
        gbc_b7.gridx = 1;
        gbc_b7.gridy = 1;
        chessboard.add(b7, gbc_b7);
        b7.addFocusListener(myFocusListener);

        JTextField d7 = new JTextField();
        d7.setBackground(new Color(255, 255, 255));
        d7.setText("♟");
        d7.setHorizontalAlignment(JTextField.CENTER);
        d7.setFont(new Font("Dialog", Font.BOLD, 40));
        d7.setBorder(javax.swing.BorderFactory.createEmptyBorder());
        d7.setEditable(false);
        GridBagConstraints gbc_d7 = new GridBagConstraints();
        gbc_d7.insets = new Insets(0, 0, 0, 0);
        gbc_d7.fill = GridBagConstraints.BOTH;
        gbc_d7.gridx = 3;
        gbc_d7.gridy = 1;
        chessboard.add(d7, gbc_d7);
        d7.addFocusListener(myFocusListener);
        
        JTextField f7 = new JTextField();
        f7.setBackground(new Color(255, 255, 255));
        f7.setText("♟");
        f7.setHorizontalAlignment(JTextField.CENTER);
        f7.setFont(new Font("Dialog", Font.BOLD, 40));
        f7.setBorder(javax.swing.BorderFactory.createEmptyBorder());
        f7.setEditable(false);
        GridBagConstraints gbc_f7 = new GridBagConstraints();
        gbc_f7.insets = new Insets(0, 0, 0, 0);
        gbc_f7.fill = GridBagConstraints.BOTH;
        gbc_f7.gridx = 5;
        gbc_f7.gridy = 1;
        chessboard.add(f7, gbc_f7);
        f7.addFocusListener(myFocusListener);
        
        JTextField h7 = new JTextField();
        h7.setBackground(new Color(255, 255, 255));
        h7.setText("♟");
        h7.setHorizontalAlignment(JTextField.CENTER);
        h7.setFont(new Font("Dialog", Font.BOLD, 40));
        h7.setBorder(javax.swing.BorderFactory.createEmptyBorder());
        h7.setEditable(false);
        GridBagConstraints gbc_h7 = new GridBagConstraints();
        gbc_h7.insets = new Insets(0, 0, 0, 0);
        gbc_h7.fill = GridBagConstraints.BOTH;
        gbc_h7.gridx = 7;
        gbc_h7.gridy = 1;
        chessboard.add(h7, gbc_h7);
        h7.addFocusListener(myFocusListener);
        
        JTextField c8 = new JTextField();
        c8.setBackground(new Color(255, 255, 255));
        c8.setText("♝");
        c8.setHorizontalAlignment(JTextField.CENTER);
        c8.setFont(new Font("Dialog", Font.BOLD, 40));
        c8.setBorder(javax.swing.BorderFactory.createEmptyBorder());
        c8.setEditable(false);
        GridBagConstraints gbc_c8 = new GridBagConstraints();
        gbc_c8.insets = new Insets(0, 0, 0, 0);
        gbc_c8.fill = GridBagConstraints.BOTH;
        gbc_c8.gridx = 2;
        gbc_c8.gridy = 0;
        chessboard.add(c8, gbc_c8);
        c8.addFocusListener(myFocusListener);

        JTextField e8 = new JTextField();
        e8.setBackground(new Color(255, 255, 255));
        e8.setText("♚");
        e8.setHorizontalAlignment(JTextField.CENTER);
        e8.setFont(new Font("Dialog", Font.BOLD, 40));
        e8.setBorder(javax.swing.BorderFactory.createEmptyBorder());
        e8.setEditable(false);
        GridBagConstraints gbc_e8 = new GridBagConstraints();
        gbc_e8.insets = new Insets(0, 0, 0, 0);
        gbc_e8.fill = GridBagConstraints.BOTH;
        gbc_e8.gridx = 4;
        gbc_e8.gridy = 0;
        chessboard.add(e8, gbc_e8);
        e8.addFocusListener(myFocusListener);

        JTextField g8 = new JTextField();
        g8.setBackground(new Color(255, 255, 255));
        g8.setText("♞");
        g8.setHorizontalAlignment(JTextField.CENTER);
        g8.setFont(new Font("Dialog", Font.BOLD, 40));
        g8.setBorder(javax.swing.BorderFactory.createEmptyBorder());
        g8.setEditable(false);
        GridBagConstraints gbc_g8 = new GridBagConstraints();
        gbc_g8.insets = new Insets(0, 0, 0, 0);
        gbc_g8.fill = GridBagConstraints.BOTH;
        gbc_g8.gridx = 6;
        gbc_g8.gridy = 0;
        chessboard.add(g8, gbc_g8);
        g8.addFocusListener(myFocusListener);
        
        // Use string to reference squares
        
        squares.put("a1", a1);
        squares.put("a2", a2);
        squares.put("a3", a3);
        squares.put("a4", a4);
        squares.put("a5", a5);
        squares.put("a6", a6);
        squares.put("a7", a7);
        squares.put("a8", a8);

        squares.put("b1", b1);
        squares.put("b2", b2);
        squares.put("b3", b3);
        squares.put("b4", b4);
        squares.put("b5", b5);
        squares.put("b6", b6);
        squares.put("b7", b7);
        squares.put("b8", b8);

        squares.put("c1", c1);
        squares.put("c2", c2);
        squares.put("c3", c3);
        squares.put("c4", c4);
        squares.put("c5", c5);
        squares.put("c6", c6);
        squares.put("c7", c7);
        squares.put("c8", c8);

        squares.put("d1", d1);
        squares.put("d2", d2);
        squares.put("d3", d3);
        squares.put("d4", d4);
        squares.put("d5", d5);
        squares.put("d6", d6);
        squares.put("d7", d7);
        squares.put("d8", d8);

        squares.put("e1", e1);
        squares.put("e2", e2);
        squares.put("e3", e3);
        squares.put("e4", e4);
        squares.put("e5", e5);
        squares.put("e6", e6);
        squares.put("e7", e7);
        squares.put("e8", e8);

        squares.put("f1", f1);
        squares.put("f2", f2);
        squares.put("f3", f3);
        squares.put("f4", f4);
        squares.put("f5", f5);
        squares.put("f6", f6);
        squares.put("f7", f7);
        squares.put("f8", f8);

        squares.put("g1", g1);
        squares.put("g2", g2);
        squares.put("g3", g3);
        squares.put("g4", g4);
        squares.put("g5", g5);
        squares.put("g6", g6);
        squares.put("g7", g7);
        squares.put("g8", g8);

        squares.put("h1", h1);
        squares.put("h2", h2);
        squares.put("h3", h3);
        squares.put("h4", h4);
        squares.put("h5", h5);
        squares.put("h6", h6);
        squares.put("h7", h7);
        squares.put("h8", h8);
        
        JPanel sidebar = new JPanel(new CardLayout());
        GridBagConstraints gbc_sidebar = new GridBagConstraints();
        gbc_sidebar.insets = new Insets(0, 0, 0, 5);
        gbc_sidebar.fill = GridBagConstraints.BOTH;
        gbc_sidebar.gridx = 1;
        gbc_sidebar.gridy = 0;
        analysisWindow.add(sidebar, gbc_sidebar);
        
        JPanel setup = new JPanel();
        sidebar.add(setup);
        GridBagLayout gbl_setup = new GridBagLayout();
        gbl_setup.columnWidths = new int[]{300, 0}; 
        gbl_setup.rowHeights = new int[]{359, 200, -29, 0};
        gbl_setup.columnWeights = new double[]{1.0, Double.MIN_VALUE};
        gbl_setup.rowWeights = new double[]{0.0, 1.0, 1.0, Double.MIN_VALUE};
        setup.setLayout(gbl_setup); 
        
        
        Icon backIcon = new ImageIcon("/Users/gekiclaws/Documents/GitHub/guess-the-eval/Chessi/src/resources/back.png"); 
        Image img = ((ImageIcon) backIcon).getImage() ;  
        Image newimg = img.getScaledInstance(50, 50, java.awt.Image.SCALE_SMOOTH ) ;  
        Icon icon = new ImageIcon( newimg );
        
        JButton backBtn = new JButton(icon);
        GridBagConstraints gbc_backBtn = new GridBagConstraints();
        gbc_backBtn.insets = new Insets(0, 0, 5, 0);
        gbc_backBtn.gridx = 0;
        gbc_backBtn.gridy = 1;
        setup.add(backBtn, gbc_backBtn);
        backBtn.setBorderPainted(false);
        backBtn.setBackground(SystemColor.window);
        
        JPanel menu = new JPanel();
        GridBagConstraints gbc_menu = new GridBagConstraints();
        gbc_menu.fill = GridBagConstraints.BOTH;
        gbc_menu.gridx = 0;
        gbc_menu.gridy = 0;
        setup.add(menu, gbc_menu);
        
        JLabel lblNewLabel = new JLabel("analysis");
        menu.add(lblNewLabel);
        backBtn.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		CardLayout cl = (CardLayout)(cards.getLayout());
                cl.show(cards, "mainWindow");
        	}
        });
        
        JPanel analysis = new JPanel();
        sidebar.add(analysis);
        
        JPanel gteWindow = new JPanel();
        cards.add(gteWindow, "gteWindow");
        
        JLabel lblNewLabel_1 = new JLabel("gte");
        gteWindow.add(lblNewLabel_1);
        
        pane.add(cards, BorderLayout.CENTER);
        
        // Back end link
        
    	app.getGTE().loadNewPosition(4);
    	Piece[][] board = app.getGTE().getTheBoard().getBoard();
//    	app.getGTE().getTheBoard().showBoard();
        
    	for (int i = 0; i < board.length; i++) {
        	for (int k = 0; k<board[0].length; k++) {
        		String type = "";
        		try {
        			type = board[i][k].getClass().getSimpleName();
        		} catch (NullPointerException ne) {}
        		
        		Square square = new Square(k, i);
				
        		switch (type) {
        			case "Pawn":
        				if (board[i][k].getColor().equals("white")) {
        					squares.get(square.getName()).setText("♙");
        				} else {
        					squares.get(square.getName()).setText("♟︎");
        				}
        				break;
        			case "Rook":
        				if (board[i][k].getColor().equals("white")) {
        					squares.get(square.getName()).setText("♖");
        				} else {
        					squares.get(square.getName()).setText("♜︎");
        				}
        				break;
        			case "Knight":
        				if (board[i][k].getColor().equals("white")) {
        					squares.get(square.getName()).setText("♘");
        				} else {
        					squares.get(square.getName()).setText("♞︎");
        				}
        				break;
        			case "Bishop":
        				if (board[i][k].getColor().equals("white")) {
        					squares.get(square.getName()).setText("♗");
        				} else {
        					squares.get(square.getName()).setText("♝︎");
        				}
        				break;
        			case "Queen":
        				if (board[i][k].getColor().equals("white")) {
        					squares.get(square.getName()).setText("♕");
        				} else {
        					squares.get(square.getName()).setText("♛︎");
        				}
        				break;
        			case "King":
        				if (board[i][k].getColor().equals("white")) {
        					squares.get(square.getName()).setText("♔");
        				} else {
        					squares.get(square.getName()).setText("♚");
        				}
        				break;
        			default:
        				squares.get(square.getName()).setText("");
        		}
        	}
        }
    	
    	
    		
    }
  
    
    public void update(FocusEvent e, Square s) {
    	Piece[][] board = app.getGTE().getTheBoard().copyBoard();
    	System.out.println(s.getName());
    	
    	if (currentSquare == null && board[s.getY()][s.getX()] != null) {
//    		JTextField textField = (JTextField) e.getSource();
//    		currentColor = textField.getBackground();
//        	textField.setBackground(new Color(0, 255, 128));
        	currentSquare = s;
    	} else if (currentSquare != null) {
    		
//    		squares.get(currentSquare.getName()).setBackground(currentColor);
    		boolean pass = app.getGTE().getTheBoard().makeMove(currentSquare, s);
    		System.out.println(pass);
    		if (pass) {
    			squares.get(s.getName()).setText(squares.get(currentSquare.getName()).getText());
    			squares.get(currentSquare.getName()).setText("");
    			
    			
    			if (board[currentSquare.getY()][currentSquare.getX()] instanceof Pawn) {				
    				Pawn pawn = (Pawn) board[currentSquare.getY()][currentSquare.getX()];
    				
    				// remove extra pawn if en passant
    				if (pawn.canBeCapturedEnPassant(board, pawn.getColor(), currentSquare, s)) {
    					squares.get(new Square(s.getX(), currentSquare.getY()).getName()).setText("");
    				}
    				
    				// promote pawn if it reaches the end
    				if (pawn.checkForPromotion(s) && !pawn.isPromoted()) {
    					board = app.getGTE().getTheBoard().copyBoard();
    					pawn = (Pawn) board[s.getY()][s.getX()];
    					String type = pawn.getPromotedTo().getClass().getSimpleName();
    					switch (type) {
	    				    case "Rook":
	    				        squares.get(s.getName()).setText(pawn.getColor().equals("white") ? "♖" : "♜");
	    				        break;
	    				    case "Knight":
	    				        squares.get(s.getName()).setText(pawn.getColor().equals("white") ? "♘" : "♞");
	    				        break;
	    				    case "Bishop":
	    				        squares.get(s.getName()).setText(pawn.getColor().equals("white") ? "♗" : "♝");
	    				        break;
	    				    case "Queen":
	    				        squares.get(s.getName()).setText(pawn.getColor().equals("white") ? "♕" : "♛");
	    				        break;
	    				}
		          				
            	      }
    					
    	            }
    			
    			
    			// move rook if castling
    			if (board[currentSquare.getY()][currentSquare.getX()] instanceof King) {
    			    if(Math.abs(s.getX() - currentSquare.getX()) == 2) {
	    				if (s.getX() > currentSquare.getX()) {
	    					System.out.println("kingside");
				            // Kingside castle
				            squares.get(new Square(5, s.getY()).getName()).setText(squares.get(new Square(7, s.getY()).getName()).getText());
				            squares.get(new Square(7, s.getY()).getName()).setText("");
				        } else {
				        	System.out.println("queenside");
				            // Queenside castle
				            squares.get(new Square(3, s.getY()).getName()).setText(squares.get(new Square(0, s.getY()).getName()).getText());
				            squares.get(new Square(0, s.getY()).getName()).setText("");
				        }
    			    }
    			}
    			
    			app.getGTE().setMoveGuess(new Move(currentSquare, s));		
   
    			currentSquare = null;
    		} else {
    			if (board[s.getY()][s.getX()] != null) {
//    				currentColor = squares.get(s.getName()).getBackground();
//    				squares.get(currentSquare.getName()).setBackground(new Color(0, 255, 128));
    				currentSquare = s;
            	} else {
            		currentSquare = null;
            	}
    		}
    	}
    }
    
    
    private static void createAndShowGUI() {
        //Create and set up the window.
        frame = new JFrame("Chessi");
        frame.setSize(750, 500);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        //Create and set up the content pane.
        Interface demo = new Interface();
        demo.addComponentToPane(frame.getContentPane());
        
        //Display the window.
//        frame.pack();
        frame.setVisible(true);
    } 
    
    public static void main(String[] args) {
        /* Use an appropriate Look and Feel */
        try {
        	//UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
            UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");
        } catch (UnsupportedLookAndFeelException ex) {
            ex.printStackTrace();
        } catch (IllegalAccessException ex) { 
        	ex.printStackTrace();
        } catch (InstantiationException ex) {
            ex.printStackTrace();
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
        }
        /* Turn off metal's use of bold fonts */
        UIManager.put("swing.boldMetal", Boolean.FALSE);
        
        //Schedule a job for the event dispatch thread:
        //creating and showing this application's GUI.
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGUI();
            }
        });
        
    }
}
