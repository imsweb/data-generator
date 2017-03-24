/*
 * Copyright (C) 2016 Information Management Services, Inc.
 */
package com.imsweb.datagenerator.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.WindowConstants;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;

import org.apache.commons.io.IOUtils;

import com.imsweb.datagenerator.naaccr.NaaccrDataGeneratorOptions;
import com.imsweb.layout.LayoutFactory;

public class StandaloneNaaccrDataGenerator extends JFrame implements ActionListener {

    // application version
    public static final String VERSION = getVersion();

    // possible values for the compression
    protected static final String _COMPRESSION_NONE = "None";
    protected static final String _COMPRESSION_GZIP = "GZip";

    // possible values for the NAACCR formats
    protected static final String _FORMAT_16_ABS = "NAACCR 16 Abstract (22,824 characters)";
    protected static final String _FORMAT_16_INC = "NAACCR 16 Incidence (3,339 characters)";
    protected static final String _FORMAT_15_ABS = "NAACCR 15 Abstract (22,824 characters)";
    protected static final String _FORMAT_15_INC = "NAACCR 15 Incidence (3,339 characters)";

    protected JFileChooser _fileChooser;
    protected JTextField _targetFld, _numRecFld, _dxYearFld, _registryIdFld;
    protected JRadioButton _numTumPerPatRandom, _numTumPerPatFixed;
    protected JComboBox<String> _compressionBox, _formatBox, _numTumPerPatBox, _stateBox, _vsBox;
    protected JButton _processBtn;

    public StandaloneNaaccrDataGenerator() {
        this.setTitle("Synthetic Data File Generator v" + VERSION);
        this.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        this.getContentPane().setLayout(new BorderLayout());
        this.setResizable(false);

        _fileChooser = new JFileChooser();
        _fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        _fileChooser.setDialogTitle("Select Target Folder");
        _fileChooser.setApproveButtonToolTipText("Select Folder");
        _fileChooser.setMultiSelectionEnabled(false);

        JMenuBar bar = new JMenuBar();
        JMenu fileMenu = new JMenu(" File ");
        fileMenu.setMnemonic(KeyEvent.VK_F);
        bar.add(fileMenu);
        JMenuItem exitItem = new JMenuItem("Exit       ");
        exitItem.setActionCommand("menu-exit");
        exitItem.addActionListener(this);
        fileMenu.add(exitItem);
        JMenu helpMenu = new JMenu(" Help ");
        helpMenu.setMnemonic(KeyEvent.VK_H);
        JMenuItem helpItem = new JMenuItem("View Help       ");
        helpItem.setActionCommand("menu-help");
        helpItem.addActionListener(this);
        helpMenu.add(helpItem);
        //TODO uncomment
        //JMenuItem rulesItem = new JMenuItem("View Rules       ");
        //rulesItem.setActionCommand("menu-rules");
        //rulesItem.addActionListener(this);
        //helpMenu.add(rulesItem);
        helpMenu.addSeparator();
        JMenuItem aboutItem = new JMenuItem("About");
        aboutItem.setActionCommand("menu-about");
        aboutItem.addActionListener(this);
        helpMenu.add(aboutItem);
        bar.add(helpMenu);
        this.setJMenuBar(bar);

        JPanel centerPnl = new JPanel();
        centerPnl.setLayout(new BoxLayout(centerPnl, BoxLayout.Y_AXIS));
        centerPnl.setBorder(new EmptyBorder(10, 10, 10, 10));
        centerPnl.add(createOptionsPanel());
        centerPnl.add(Box.createVerticalStrut(20));
        centerPnl.add(createControlsPanel());
        this.getContentPane().add(centerPnl, BorderLayout.CENTER);

        Thread.setDefaultUncaughtExceptionHandler((t, e) -> SwingUtilities.invokeLater(() -> {
            String msg = "An unexpected error happened, it is recommended to close the application.\n\n   Error: " + (e.getMessage() == null ? "null access" : e.getMessage());
            JOptionPane.showMessageDialog(StandaloneNaaccrDataGenerator.this, msg, "Error", JOptionPane.ERROR_MESSAGE);
        }));

        this.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentShown(ComponentEvent e) {
                _processBtn.requestFocusInWindow();
                StandaloneNaaccrDataGenerator.this.removeComponentListener(this);
            }
        });
    }

    private static String getVersion() {
        String version = null;

        // this will make it work when running from the JAR file
        try (InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream("DATA-GENERATOR-VERSION")) {
            if (is != null)
                version = IOUtils.readLines(is, StandardCharsets.UTF_8).get(0);
        }
        catch (IOException e) {
            version = null;
        }

        // this will make it work when running from an IDE
        if (version == null) {
            try (FileInputStream is = new FileInputStream(System.getProperty("user.dir") + File.separator + "VERSION")) {
                version = IOUtils.readLines(is, StandardCharsets.UTF_8).get(0);
            }
            catch (IOException e) {
                version = null;
            }
        }

        if (version == null)
            version = "??";

        return version;
    }

    protected JPanel createOptionsPanel() {
        JPanel pnl = new JPanel();
        pnl.setLayout(new BoxLayout(pnl, BoxLayout.Y_AXIS));
        pnl.setBorder(new EmptyBorder(0, 0, 0, 0));

        JPanel filePnl = new JPanel();
        filePnl.setLayout(new BoxLayout(filePnl, BoxLayout.Y_AXIS));
        TitledBorder border = new TitledBorder(" File to Create ");
        border.setTitleColor(new Color(0, 50, 100));
        border.setTitleFont(new JLabel().getFont().deriveFont(Font.BOLD));
        filePnl.setBorder(new CompoundBorder(border, new EmptyBorder(5, 15, 5, 15)));
        pnl.add(filePnl);

        // file location
        JPanel pathPnl = new JPanel();
        pathPnl.setLayout(new FlowLayout(FlowLayout.LEADING, 0, 0));
        pathPnl.setBorder(null);
        pathPnl.add(new JLabel("Location: "));
        pathPnl.add(Box.createHorizontalStrut(5));
        _targetFld = new JTextField(60);
        _targetFld.setText(new File(_fileChooser.getCurrentDirectory(), "synthetic-data_naaccr-16-abstract_1000-recs.txt").getPath());
        pathPnl.add(_targetFld);
        pathPnl.add(Box.createHorizontalStrut(10));
        JButton browseBtn = new JButton("Browse...");
        browseBtn.addActionListener(e -> {
            if (_fileChooser.showDialog(StandaloneNaaccrDataGenerator.this, "Select") == JFileChooser.APPROVE_OPTION) {
                File file = new File(_targetFld.getText());
                _targetFld.setText(new File(_fileChooser.getSelectedFile(), file.getName()).getPath());
            }
        });
        pathPnl.add(browseBtn);
        filePnl.add(pathPnl);
        filePnl.add(Box.createVerticalStrut(15));

        // file format & compression
        JPanel formatPnl = new JPanel();
        formatPnl.setLayout(new FlowLayout(FlowLayout.LEADING, 0, 0));
        formatPnl.setBorder(null);
        formatPnl.add(new JLabel("Format:"));
        formatPnl.add(Box.createHorizontalStrut(5));
        _formatBox = new JComboBox<>(new String[] {_FORMAT_16_ABS, _FORMAT_16_INC, _FORMAT_15_ABS, _FORMAT_15_INC});
        _formatBox.addActionListener(e -> {
            if (!_targetFld.getText().isEmpty())
                _targetFld.setText(fixTargetFile());
        });
        formatPnl.add(_formatBox);
        formatPnl.add(Box.createHorizontalStrut(15));
        formatPnl.setLayout(new FlowLayout(FlowLayout.LEADING, 0, 0));
        formatPnl.setBorder(null);
        formatPnl.add(new JLabel("Compression:"));
        formatPnl.add(Box.createHorizontalStrut(5));
        _compressionBox = new JComboBox<>(new String[] {_COMPRESSION_NONE, _COMPRESSION_GZIP});
        _compressionBox.addActionListener(e -> {
            if (!_targetFld.getText().isEmpty())
                _targetFld.setText(fixTargetFile());
        });
        formatPnl.add(_compressionBox);
        filePnl.add(formatPnl);
        filePnl.add(Box.createVerticalStrut(15));

        // total number of records
        JPanel totalNumRecPnl = new JPanel();
        totalNumRecPnl.setLayout(new FlowLayout(FlowLayout.LEADING, 0, 0));
        totalNumRecPnl.setBorder(null);
        totalNumRecPnl.add(new JLabel("Number of records:"));
        totalNumRecPnl.add(Box.createHorizontalStrut(5));
        _numRecFld = new JTextField(10);
        _numRecFld.setText("1000");
        _numRecFld.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                if (!_targetFld.getText().isEmpty() && !_numRecFld.getText().isEmpty())
                    _targetFld.setText(fixTargetFile());
            }
        });
        totalNumRecPnl.add(_numRecFld);
        filePnl.add(totalNumRecPnl);

        pnl.add(Box.createVerticalStrut(15));

        JPanel optionsPnl = new JPanel();
        optionsPnl.setLayout(new BoxLayout(optionsPnl, BoxLayout.Y_AXIS));
        border = new TitledBorder(" Options ");
        border.setTitleColor(new Color(0, 50, 100));
        border.setTitleFont(new JLabel().getFont().deriveFont(Font.BOLD));
        optionsPnl.setBorder(new CompoundBorder(border, new EmptyBorder(10, 15, 5, 15)));
        pnl.add(optionsPnl);

        // registry id
        JPanel registryId = new JPanel();
        registryId.setLayout(new FlowLayout(FlowLayout.LEADING, 0, 0));
        registryId.setBorder(null);
        registryId.add(new JLabel("Registry ID: "));
        registryId.add(Box.createHorizontalStrut(5));
        _registryIdFld = new JTextField(10);
        registryId.add(_registryIdFld);
        registryId.add(Box.createHorizontalStrut(5));
        registryId.add(new JLabel("- optional, corresponds to NAACCR Item #40."));
        optionsPnl.add(registryId);
        optionsPnl.add(Box.createVerticalStrut(15));

        // number of tumor per patient
        JPanel numTumPerPat = new JPanel();
        numTumPerPat.setLayout(new FlowLayout(FlowLayout.LEADING, 0, 0));
        numTumPerPat.setBorder(null);
        numTumPerPat.add(new JLabel("Number of records per patient (records for the same patient will have the same Patient ID Number):"));
        optionsPnl.add(numTumPerPat);
        optionsPnl.add(Box.createVerticalStrut(5));

        JPanel numTumPerPat1 = new JPanel();
        numTumPerPat1.setLayout(new FlowLayout(FlowLayout.LEADING, 0, 0));
        numTumPerPat1.setBorder(null);
        numTumPerPat1.add(Box.createHorizontalStrut(35));
        _numTumPerPatRandom = new JRadioButton("Use a randomized number of records per patient (mostly 1, a few with 2 or 3 tumors)");
        numTumPerPat1.add(_numTumPerPatRandom);
        optionsPnl.add(numTumPerPat1);

        JPanel numTumPerPat2 = new JPanel();
        numTumPerPat2.setLayout(new FlowLayout(FlowLayout.LEADING, 0, 0));
        numTumPerPat2.setBorder(null);
        numTumPerPat2.add(Box.createHorizontalStrut(35));
        _numTumPerPatFixed = new JRadioButton("Use a fixed number of records per patient: ");
        numTumPerPat2.add(_numTumPerPatFixed);
        numTumPerPat2.add(Box.createHorizontalStrut(5));
        _numTumPerPatBox = new JComboBox<>(new String[] {" 1", " 2", " 3", " 4", " 5", " 6", " 7", " 8", " 9", "10"});
        numTumPerPat2.add(_numTumPerPatBox);
        optionsPnl.add(numTumPerPat2);
        optionsPnl.add(Box.createVerticalStrut(15));

        ButtonGroup group = new ButtonGroup();
        group.add(_numTumPerPatRandom);
        group.add(_numTumPerPatFixed);
        _numTumPerPatRandom.setSelected(true);

        // DX year
        JPanel dxYearPnl = new JPanel();
        dxYearPnl.setLayout(new FlowLayout(FlowLayout.LEADING, 0, 0));
        dxYearPnl.setBorder(null);
        dxYearPnl.add(new JLabel("DX year range:"));
        dxYearPnl.add(Box.createHorizontalStrut(5));
        _dxYearFld = new JTextField(10);
        _dxYearFld.setText(String.valueOf(LocalDate.now().getYear() - 10) + "-" + String.valueOf(LocalDate.now().getYear()));
        dxYearPnl.add(_dxYearFld);
        dxYearPnl.add(Box.createHorizontalStrut(5));
        dxYearPnl.add(new JLabel("- use nnnn-nnnn for a range, or nnnn for a single year"));
        optionsPnl.add(dxYearPnl);
        optionsPnl.add(Box.createVerticalStrut(15));

        // state
        JPanel statePnl = new JPanel();
        statePnl.setLayout(new FlowLayout(FlowLayout.LEADING, 0, 0));
        statePnl.setBorder(null);
        statePnl.add(new JLabel("State:"));
        statePnl.add(Box.createHorizontalStrut(5));
        _stateBox = new JComboBox<>(
                new String[] {" < no state > ", "AK", "AL", "AR", "AZ", "CA", "CO", "CT", "DE", "FL", "GA", "HI", "IA", "ID", "IL", "IN", "KS", "KY", "LA", "MA", "MD", "ME", "MI", "MN", "MO", "MS",
                        "MT", "NC", "ND", "NE", "NH", "NJ", "NM", "NV", "NY", "OH", "OK", "OR", "PA", "RI", "SC", "SD", "TN", "TX", "UT", "VA", "VT", "WA", "WI", "WV", "WY"});
        statePnl.add(_stateBox);
        statePnl.add(Box.createHorizontalStrut(5));
        statePnl.add(new JLabel("- if not provided, no address information will be generated"));
        optionsPnl.add(statePnl);
        optionsPnl.add(Box.createVerticalStrut(15));

        // vital status
        JPanel vsPnl = new JPanel();
        vsPnl.setLayout(new FlowLayout(FlowLayout.LEADING, 0, 0));
        vsPnl.setBorder(null);
        vsPnl.add(new JLabel("When creating a record with a Vital Status of \"dead\", use the following value:"));
        vsPnl.add(Box.createHorizontalStrut(5));
        _vsBox = new JComboBox<>(new String[] {"4 (SEER flavor)", "0 (CoC flavor)"});
        vsPnl.add(_vsBox);
        vsPnl.add(Box.createHorizontalStrut(10));
        optionsPnl.add(vsPnl);

        pnl.add(optionsPnl);

        pnl.add(Box.createVerticalStrut(5));

        return pnl;
    }

    protected JPanel createControlsPanel() {
        JPanel pnl = new JPanel();
        pnl.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 0));
        _processBtn = new JButton("Create File");
        _processBtn.addActionListener(e -> {

            // get the target file
            File targetFile = new File(_targetFld.getText());
            if (targetFile.exists()) {
                String msg = "Target file already exists, are you sure you want to replace it?";
                int result = JOptionPane.showConfirmDialog(StandaloneNaaccrDataGenerator.this, msg, "Confirmation", JOptionPane.YES_NO_OPTION);
                if (result != JOptionPane.YES_OPTION)
                    return;
            }

            // get the number of records
            String numRecsRaw = _numRecFld.getText().trim();
            if (!numRecsRaw.matches("\\d+") || numRecsRaw.length() > 8) {
                String message = "Wrong format for number of records, needs to be 1 to 8 digits.";
                JOptionPane.showMessageDialog(StandaloneNaaccrDataGenerator.this, message, "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            int numRecords = Integer.parseInt(numRecsRaw);

            // get the DX year range
            String dxYearRaw = _dxYearFld.getText();
            if (!dxYearRaw.matches("\\d{4}-\\d{4}") && !dxYearRaw.matches("\\d{4}")) {
                String message = "Wrong format for DX year, needs to be nnnn or nnnn-nnnn.";
                JOptionPane.showMessageDialog(StandaloneNaaccrDataGenerator.this, message, "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            int dxStart, dxEnd;
            if (dxYearRaw.contains("-")) {
                dxStart = Integer.parseInt(dxYearRaw.split("-")[0]);
                dxEnd = Integer.parseInt(dxYearRaw.split("-")[1]);
            }
            else
                dxStart = dxEnd = Integer.parseInt(dxYearRaw);

            String registryIdRaw = _registryIdFld.getText();
            // if the user has entered 1 or more only whitespace characters, give an error
            if(registryIdRaw.length() > 0 && "".equals(registryIdRaw.trim())) {
                String message = "Registry ID cannot be whitespace. Leave this field empty to create data with no Registry ID.";
                JOptionPane.showMessageDialog(StandaloneNaaccrDataGenerator.this, message, "Error", JOptionPane.ERROR_MESSAGE);
                _registryIdFld.setText("");
                return;
            }

            // get the layout ID
            String layoutId = getFormatIdFromLabel((String)_formatBox.getSelectedItem());

            // create the options
            NaaccrDataGeneratorOptions options = new NaaccrDataGeneratorOptions();
            try {
                if (_numTumPerPatFixed.isSelected())
                    options.setNumTumorsPerPatient(Integer.valueOf(((String)_numTumPerPatBox.getSelectedItem()).trim()));
                options.setMinDxYear(dxStart);
                options.setMaxDxYear(dxEnd);
                options.setRegistryId(registryIdRaw);
                String state1 = (String)_stateBox.getSelectedItem();
                if (state1.matches("[A-Z][A-Z]"))
                    options.setState(state1);
                if (_vsBox.getSelectedItem().toString().toLowerCase().contains("coc"))
                    options.setVitalStatusDeadValue("0");
            }
            catch (IllegalArgumentException exception) {
                JOptionPane.showMessageDialog(StandaloneNaaccrDataGenerator.this, exception.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // and finally, create and show a progress dialog
            final ProgressDialog progressDlg = new ProgressDialog(StandaloneNaaccrDataGenerator.this, targetFile, numRecords, layoutId, options);
            SwingUtilities.invokeLater(() -> {
                // show the dialog in the center of the parent window
                Component parent1 = StandaloneNaaccrDataGenerator.this;
                Point center = new Point();
                center.setLocation(parent1.getLocationOnScreen().x + parent1.getWidth() / 2, parent1.getLocationOnScreen().y + parent1.getHeight() / 2);
                progressDlg.pack();
                progressDlg.setLocation(center.x - progressDlg.getWidth() / 2, center.y - progressDlg.getHeight() / 2);
                progressDlg.setVisible(true);
            });
        });
        pnl.add(_processBtn);
        return pnl;
    }

    private String fixTargetFile() {
        File file = new File(_targetFld.getText());
        String compression = (String)_compressionBox.getSelectedItem();
        String numRec = _numRecFld.getText();
        String format = getFormatIdFromLabel((String)_formatBox.getSelectedItem());

        String filename = file.getName();
        if (_COMPRESSION_GZIP.equals(compression)) {
            if (!filename.endsWith(".gz"))
                filename = filename + ".gz";
        }
        else if (_COMPRESSION_NONE.equals(compression)) {
            if (filename.endsWith(".gz"))
                filename = filename.replace(".gz", "");
        }

        Matcher matcher = Pattern.compile("_\\d+-recs").matcher(filename);
        if (matcher.find())
            filename = matcher.replaceFirst("_" + numRec + "-recs");

        matcher = Pattern.compile("_naaccr-\\d+-(abstract|incidence)_").matcher(filename);
        if (matcher.find())
            filename = matcher.replaceFirst("_" + format + "_");

        return new File(file.getParentFile(), filename).getPath();
    }

    private String getFormatIdFromLabel(String label) {
        if (label.startsWith("NAACCR 16 Abstract"))
            return LayoutFactory.LAYOUT_ID_NAACCR_16_ABSTRACT;
        else if (label.startsWith("NAACCR 16 Incidence"))
            return LayoutFactory.LAYOUT_ID_NAACCR_16_INCIDENCE;
        else if (label.startsWith("NAACCR 15 Abstract"))
            return LayoutFactory.LAYOUT_ID_NAACCR_15_ABSTRACT;
        else if (label.startsWith("NAACCR 15 Incidence"))
            return LayoutFactory.LAYOUT_ID_NAACCR_15_INCIDENCE;
        else
            throw new RuntimeException("Unknown format label: " + label);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String cmd = e.getActionCommand();
        if ("menu-exit".equals(cmd))
            System.exit(0);
        else if ("menu-help".equals(cmd)) {
            try {
                File targetFile = File.createTempFile("synthetic-data-generator-help", ".html");
                targetFile.deleteOnExit();
                InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream("gui/help.html");
                OutputStream os = new FileOutputStream(targetFile);
                IOUtils.copy(is, os);
                is.close();
                os.close();
                Desktop.getDesktop().open(targetFile);
            }
            catch (IOException ex) {
                JOptionPane.showMessageDialog(this, "Unable to display help.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
        else if ("menu-rules".equals(cmd)) {
            //TODO
        }
        else if ("menu-about".equals(cmd)) {
            final JDialog dlg = new AboutDialog(this);
            dlg.pack();
            Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
            Point center = new Point(screenSize.width / 2, screenSize.height / 2);
            dlg.setLocation(center.x - dlg.getWidth() / 2, center.y - dlg.getHeight() / 2);
            SwingUtilities.invokeLater(() -> dlg.setVisible(true));
        }
    }

    public static void main(String[] args) {

        if (System.getProperty("os.name").toLowerCase().contains("windows")) {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            }
            catch (ClassNotFoundException | InstantiationException | UnsupportedLookAndFeelException | IllegalAccessException e) {
                // ignored, the look and feel will be the default Java one...
            }
            UIManager.put("TabbedPane.contentBorderInsets", new Insets(0, 0, 0, 0));
            Insets insets = UIManager.getInsets("TabbedPane.tabAreaInsets");
            insets.bottom = 0;
            UIManager.put("TabbedPane.tabAreaInsets", insets);
        }

        final JFrame frame = new StandaloneNaaccrDataGenerator();
        frame.pack();

        // start in the middle of the screen
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Point center = new Point(screenSize.width / 2, screenSize.height / 2);
        frame.setLocation(center.x - frame.getWidth() / 2, center.y - frame.getHeight() / 2);

        SwingUtilities.invokeLater(() -> frame.setVisible(true));
    }
}
