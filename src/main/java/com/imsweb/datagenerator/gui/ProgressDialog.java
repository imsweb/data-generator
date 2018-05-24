/*
 * Copyright (C) 2016 Information Management Services, Inc.
 */
package com.imsweb.datagenerator.gui;

import java.awt.BorderLayout;
import java.awt.Desktop;
import java.awt.FlowLayout;
import java.awt.Window;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.zip.GZIPOutputStream;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.SwingWorker;
import javax.swing.border.EmptyBorder;

import com.imsweb.datagenerator.naaccr.NaaccrDataGenerator;
import com.imsweb.datagenerator.naaccr.NaaccrDataGeneratorOptions;
import com.imsweb.datagenerator.utils.Distribution;
import com.imsweb.layout.LayoutFactory;
import com.imsweb.layout.record.fixed.naaccr.NaaccrLayout;

public class ProgressDialog extends JDialog {

    private File _file;
    private String _layoutId;
    private int _numRecs;
    private NaaccrDataGeneratorOptions _options;

    private JLabel _label;
    private JProgressBar _progressBar;
    private JButton _cancelBtn;

    private ProgressDialogWorker _worker;

    public ProgressDialog(Window owner, File file, int numRecs, String layoutId, NaaccrDataGeneratorOptions options) {
        super(owner);

        _file = file;
        _numRecs = numRecs;
        _layoutId = layoutId;
        _options = options;

        this.setTitle("Progress Dialog");
        this.setModal(true);
        this.setResizable(false);
        this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent we) {
                performCancel();
            }
        });

        this.setLayout(new BorderLayout());

        JPanel centerPnl = new JPanel();
        centerPnl.setLayout(new BoxLayout(centerPnl, BoxLayout.Y_AXIS));
        centerPnl.setBorder(new EmptyBorder(10, 10, 10, 10));
        this.add(centerPnl, BorderLayout.CENTER);

        // force a minimum width for the dialog...
        centerPnl.add(Box.createHorizontalStrut(300));

        JPanel labelPnl = new JPanel();
        labelPnl.setLayout(new FlowLayout(FlowLayout.LEADING, 0, 0));
        labelPnl.setBorder(null);
        labelPnl.setOpaque(false);
        _label = new JLabel("Retrieving layout information...");
        labelPnl.add(_label);
        centerPnl.add(labelPnl);
        centerPnl.add(Box.createVerticalStrut(5));

        JPanel progressPnl = new JPanel();
        progressPnl.setLayout(new BorderLayout());
        progressPnl.setBorder(null);
        progressPnl.setOpaque(false);
        _progressBar = new JProgressBar(0, numRecs);
        _progressBar.setValue(0);
        _progressBar.setStringPainted(false);
        progressPnl.add(_progressBar, BorderLayout.CENTER);
        centerPnl.add(progressPnl);
        centerPnl.add(Box.createVerticalStrut(5));

        JPanel controlsPnl = new JPanel();
        controlsPnl.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 0));
        controlsPnl.setBorder(null);
        controlsPnl.setOpaque(false);
        _cancelBtn = new JButton("Cancel");
        _cancelBtn.addActionListener(e -> performCancel());
        controlsPnl.add(_cancelBtn);
        centerPnl.add(controlsPnl);

        _worker = new ProgressDialogWorker();

        this.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentShown(ComponentEvent e) {
                _worker.execute();
                ProgressDialog.this.removeComponentListener(this);
            }
        });
    }

    public void performCancel() {
        int result = JOptionPane.showConfirmDialog(ProgressDialog.this, "Are you sure you want to cancel?", "Confirmation", JOptionPane.YES_NO_OPTION);
        if (result != JOptionPane.YES_OPTION)
            return;

        if (_worker != null)
            _worker.stopNow();
        _worker = null;

        setVisible(false);
        this.dispose();
    }

    /**
     * File creation can be a long process, we need to do it in a background thread so the GUI stays responsive...
     */
    private class ProgressDialogWorker extends SwingWorker<Void, Integer> {

        // if set to true, we need to stop the processing ASAP
        private boolean _needsToStop = false;

        @Override
        protected Void doInBackground() {
            try {
                // create the layout
                _label.setText("Retrieving layout information...");
                NaaccrLayout layout = (NaaccrLayout)LayoutFactory.getLayout(_layoutId);
                if (_needsToStop)
                    return null;

                // instanciate the generator
                _label.setText("Preparing rules...");
                NaaccrDataGenerator generator = new NaaccrDataGenerator(layout);
                if (_needsToStop)
                    return null;

                // create a random distribution for the number of tumors, if we have to
                Distribution<Integer> numTumGen = _options.getNumTumorsPerPatient() == null ? generator.getNumTumorsPerPatientDistribution() : null;

                // deal with compression
                OutputStream os = new FileOutputStream(_file);
                if (_file.getName().toLowerCase().endsWith(".gz"))
                    os = new GZIPOutputStream(os);

                // and finally write the file!
                _label.setText("Writing file...");
                try (Writer writer = new BufferedWriter(new OutputStreamWriter(os, StandardCharsets.UTF_8))) {
                    int numCreatedTumors = 0;
                    while (numCreatedTumors < _numRecs) {
                        int numTumorForThisPatient = numTumGen == null ? _options.getNumTumorsPerPatient() : numTumGen.getValue();
                        // never create more tumors than requested, so we use a min() call
                        List<Map<String, String>> patient = generator.generatePatient(Math.min(numTumorForThisPatient, _numRecs - numCreatedTumors), _options);
                        for (Map<String, String> tumor : patient)
                            layout.writeRecord(writer, tumor);
                        numCreatedTumors += patient.size();
                        publish(patient.size());
                        if (_needsToStop)
                            break;
                    }
                }

                _worker = null;
                setVisible(false);
                ProgressDialog.this.dispose();

                if (_needsToStop) {
                    if (!_file.delete())
                        JOptionPane.showMessageDialog(getOwner(), "Unable to delete partial file, please delete it manually", "Error", JOptionPane.ERROR_MESSAGE);
                }
                else {
                    String msg = "File successfully created!\n\nWould you like to open the folder containing the file?\n ";
                    int result = JOptionPane.showConfirmDialog(getOwner(), msg, "Confirmation", JOptionPane.YES_NO_OPTION);
                    if (result == JOptionPane.YES_OPTION) {
                        if (System.getProperty("os.name").startsWith("Windows"))
                            Runtime.getRuntime().exec("Explorer /select," + _file.getParentFile().getAbsolutePath() + "\\" + _file.getName());
                        else
                            Desktop.getDesktop().open(_file.getParentFile());
                    }
                }
            }
            catch (IOException | RuntimeException e) {
                _worker = null;
                setVisible(false);
                ProgressDialog.this.dispose();
                JOptionPane.showMessageDialog(getOwner(), "Unable to create file.\n\n" + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
            finally {
                _worker = null;
                setVisible(false);
                ProgressDialog.this.dispose();
            }

            return null;
        }

        @Override
        protected void process(List<Integer> chunks) {
            int total = 0;
            for (Integer i : chunks)
                total += i;
            _progressBar.setValue(_progressBar.getValue() + total);
        }

        public void stopNow() {
            _needsToStop = true;
        }
    }

}
