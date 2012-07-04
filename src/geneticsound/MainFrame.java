/*
 * MainFrame.java
 *
 * Created on 29 aprile 2007, 22.48
 * svn:Keywords
 * svn:Date
 * $Id$
 * svn:Author
 * 
 */

package geneticsound;

import geneticsound.database.DataBase;
import geneticsound.database.Melodia;
import java.awt.BorderLayout;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Vector;
import javax.swing.DefaultListModel;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileFilter;
import javax.swing.table.TableModel;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.LineAndShapeRenderer;
import org.jfree.data.category.DefaultCategoryDataset;
import tools.SynthNote;
import tools.ChartFitness;
import tools.ChartPitchs;
import soundGenetic.FitnessValue;
import soundGenetic.jgap.ConfigFitness;
import soundGenetic.jgap.ConfigJGap;
import soundGenetic.jgap.GeneticConstants;
import soundGenetic.jgap.JGapSoundGenetic;
import tools.Configs;
import tools.findWeight.FindWeightFromMidi;
import tools.findWeight.MinimiQuadrati;


/**
 * @author    abaddon
 */
public class MainFrame extends javax.swing.JFrame {
   // private final String PLUGIN_PATH="./lib/";
   // private Collection listaPlugin=null;
    private ConfigJGap configJGap;
    private ConfigFitness configFitness;
    private SynthNote synth;
    private SoundGeneticThread threadJGap;
    private UpdateInfoThread threadInfo;
    private JGapSoundGenetic genetic;
    private File fileXml=null;
    private Melodia melodia=null;
    private DefaultCategoryDataset dataset = new DefaultCategoryDataset();
    
    private DefaultListModel modelList=new DefaultListModel();
    private DefaultListModel modelListTest=new DefaultListModel();
    /** Creates new form MainFrame */
    public MainFrame() {

        
        configJGap=new ConfigJGap();
        configFitness=new ConfigFitness();
        initComponents();
        updateConfigJGap();
        updateConfigFitness();
        riempiTabella();
        
       //Parser.getIstanza().newXml(configFitness,configJGap,"./prova.xml");
//        loadPlugin();
//        inizializzaPlugin();
    }
    
    
    private DefaultListModel getListTestModel(){
        DefaultListModel model=new DefaultListModel();
        FindWeightFromMidi findWeightFromMidi=new FindWeightFromMidi();
        for(String key: findWeightFromMidi.getAllListScoreKey()){
            model.addElement(key);
        }
        
        return model;
    }
    
    private ArrayList<String> setListKeyTest(){
        ArrayList<String> list=new ArrayList<String>();
        int size=jListTestWeigh.getSelectedIndices().length;
        
        for(int i=0; i<size-1;i++){
            int indice=jListTestWeigh.getSelectedIndices()[i];
            System.out.println("size: "+jListTestWeigh.getModel().getSize()+" / "+indice);
            list.add(String.valueOf(jListTestWeigh.getModel().getElementAt(indice)));
        }
        //System.out.println("ciao "+list.size());
        return list;
    }

    private void addMidi() {
        ArrayList<File> midiList=new ArrayList<File>();
        JFileChooser fileChooser=new JFileChooser(new File("./"));
        fileChooser.setDialogTitle("Seleziona i midi");
        fileChooser.setFileFilter(new FileFilter() {

            @Override
            public boolean accept(File f) {
                if(f.getName().toLowerCase().endsWith(".mid"))
                    return true;
                else
                    return false;
            }

            @Override
            public String getDescription() {
                return "file midi";
            }
        });
        fileChooser.setFileHidingEnabled(true);
        fileChooser.setMultiSelectionEnabled(true);
        if(JFileChooser.APPROVE_OPTION==fileChooser.showOpenDialog(this)){
           //parserXml=new Parser();
            Vector<String> lista=new Vector<String>();
           for(File file: fileChooser.getSelectedFiles()){
               modelList.addElement(file.getAbsolutePath());
           }
           jLabelNumMidi.setText(String.valueOf(modelList.getSize()));
           jListMidi.setModel(modelList);
        }
        
       
    }

    private void archivioRemove() {
        boolean result=true;
        for(int i=0; i<jTableMelodie.getSelectedRows().length; i++){
            if(!DataBase.getIstanza().removeMelodia(jTableMelodie.getValueAt(jTableMelodie.getSelectedRows()[i],0).toString()))
                result=false;
        }
        riempiTabella();
        System.out.println(result);
    }

    private void creaFileConf() {
        
        FindWeightFromMidi findWeightFromMidi=new FindWeightFromMidi(setListKeyTest());

        //creo la lista di File
        ArrayList<File> list=new ArrayList<File>();
        for(int i=0; i<modelList.getSize();i++){
            list.add(new File((String)modelList.getElementAt(i)));
        }
        //ottengo la matrice di valori
        ArrayList<HashMap<String, Double>> scoreMatrix=findWeightFromMidi.extractMidiFiles(list);
        int n=scoreMatrix.get(1).size();
        int m=scoreMatrix.size();
        
        //inizializzo le matrici
        MinimiQuadrati minimiQuadrati=new MinimiQuadrati(scoreMatrix.size(),n,scoreMatrix);
        
        //imposto l'output
        jTextAreaOutput.setText(minimiQuadrati.printAll());
        
        //ottengo i pesi
        HashMap<String,Double> weightList=minimiQuadrati.getWeight();
        
        //aggiorno la jtextArea
        jTextAreaOutput.append("RESULT:\n");
        for(Double valore: weightList.values())
            jTextAreaOutput.append(valore+"\n");
        
        //creo il config nuovo
        findWeightFromMidi.createNewFileConfig(weightList, new File(jTextFieldPath.getText()));
       
    }
    
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jTabbedPane = new javax.swing.JTabbedPane();
        jPanel15 = new javax.swing.JPanel();
        jPanel16 = new javax.swing.JPanel();
        jButtonAddMidi = new javax.swing.JButton();
        jScrollPane3 = new javax.swing.JScrollPane();
        jListMidi = new javax.swing.JList();
        jButtonRemoveMidi = new javax.swing.JButton();
        jTextFieldPath = new javax.swing.JTextField();
        jButtonExplore = new javax.swing.JButton();
        jLabel71 = new javax.swing.JLabel();
        jLabelNumMidi = new javax.swing.JLabel();
        jPanel17 = new javax.swing.JPanel();
        jButton1 = new javax.swing.JButton();
        jScrollPane4 = new javax.swing.JScrollPane();
        jTextAreaOutput = new javax.swing.JTextArea();
        jPanel18 = new javax.swing.JPanel();
        jScrollPane5 = new javax.swing.JScrollPane();
        jListTestWeigh = new javax.swing.JList();
        jPanel1 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        popolazioneTextField = new javax.swing.JFormattedTextField();
        popolazioneCostCheckBox = new javax.swing.JCheckBox();
        jLabel6 = new javax.swing.JLabel();
        dimensioneCromosomaTextField = new javax.swing.JFormattedTextField();
        jLabel7 = new javax.swing.JLabel();
        notaInizioTextField = new javax.swing.JFormattedTextField();
        jLabel8 = new javax.swing.JLabel();
        ottaveComboBox = new javax.swing.JComboBox();
        preservFitTestIndividualCheckBox = new javax.swing.JCheckBox();
        jLabel14 = new javax.swing.JLabel();
        jFormattedTextFieldFitnessValueAccetable = new javax.swing.JFormattedTextField();
        jButtonOpen = new javax.swing.JButton();
        jButtonSave = new javax.swing.JButton();
        jLabel5 = new javax.swing.JLabel();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel2 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jComboBoxTonalita = new javax.swing.JComboBox();
        jLabel3 = new javax.swing.JLabel();
        jComboBoxNotaTonalita = new javax.swing.JComboBox();
        jLabel12 = new javax.swing.JLabel();
        jFormattedTextFieldFitTonalityWeight = new javax.swing.JFormattedTextField();
        jLabel41 = new javax.swing.JLabel();
        jLabel42 = new javax.swing.JLabel();
        jLabel43 = new javax.swing.JLabel();
        jFormattedTextFieldTonalityPitchWeight = new javax.swing.JFormattedTextField();
        jFormattedTextFieldTonalityScalePitchWeight = new javax.swing.JFormattedTextField();
        jFormattedTextFieldTonalityExternalPitchScale = new javax.swing.JFormattedTextField();
        jLabel49 = new javax.swing.JLabel();
        jLabel35 = new javax.swing.JLabel();
        jLabel65 = new javax.swing.JLabel();
        jFormattedTextFieldTonalityExternalScalePitchWeight = new javax.swing.JFormattedTextField();
        jPanel6 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jFormattedTextFieldMaxJump = new javax.swing.JFormattedTextField();
        jLabel13 = new javax.swing.JLabel();
        jFormattedTextFieldFitGeometricWeight = new javax.swing.JFormattedTextField();
        jLabel20 = new javax.swing.JLabel();
        jFormattedTextFieldGeometricJumpWeight = new javax.swing.JFormattedTextField();
        jLabel24 = new javax.swing.JLabel();
        jFormattedTextFieldGeometricJumpDistance = new javax.swing.JFormattedTextField();
        jLabel25 = new javax.swing.JLabel();
        jFormattedTextFieldGeometricJumpDistanceWeight = new javax.swing.JFormattedTextField();
        jLabel28 = new javax.swing.JLabel();
        jFormattedTextFieldGeometricPeakNumber = new javax.swing.JFormattedTextField();
        jLabel29 = new javax.swing.JLabel();
        jFormattedTextFieldGeometricPeakNumberWeight = new javax.swing.JFormattedTextField();
        jLabel33 = new javax.swing.JLabel();
        jLabel34 = new javax.swing.JLabel();
        jFormattedTextFieldGeometricPeakDistributionWeight = new javax.swing.JFormattedTextField();
        jLabel37 = new javax.swing.JLabel();
        jLabel39 = new javax.swing.JLabel();
        jFormattedTextFieldGeometricJumpDirectionWeight = new javax.swing.JFormattedTextField();
        jLabel46 = new javax.swing.JLabel();
        jLabel47 = new javax.swing.JLabel();
        jFormattedTextFieldGeometryJumpRecoverWeight = new javax.swing.JFormattedTextField();
        jLabel50 = new javax.swing.JLabel();
        jLabel51 = new javax.swing.JLabel();
        jFormattedTextFieldGeometricRibattutaWeight = new javax.swing.JFormattedTextField();
        jPanel9 = new javax.swing.JPanel();
        jLabel52 = new javax.swing.JLabel();
        jFormattedTextFieldFitnessFrequencyWeight = new javax.swing.JFormattedTextField();
        jLabel53 = new javax.swing.JLabel();
        jLabel54 = new javax.swing.JLabel();
        jFormattedTextFieldFrequencyPitchDistributionWeight = new javax.swing.JFormattedTextField();
        jButtonSaveAs = new javax.swing.JButton();
        jLabelNomeFileXml = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        jButtonStart = new javax.swing.JButton();
        jButtonStop = new javax.swing.JButton();
        jLabel9 = new javax.swing.JLabel();
        jLabelEvoluzione = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabelPopolazione = new javax.swing.JLabel();
        jButtonGraph = new javax.swing.JButton();
        jButtonReset = new javax.swing.JButton();
        jLabel15 = new javax.swing.JLabel();
        jLabelFitnessAttuale = new javax.swing.JLabel();
        jButtonPlayPitchs = new javax.swing.JButton();
        jButtonStopPitchs = new javax.swing.JButton();
        jPanel8 = new javax.swing.JPanel();
        jLabel40 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTextPitchs = new javax.swing.JTextField();
        jTabbedPane2 = new javax.swing.JTabbedPane();
        jPanel5 = new javax.swing.JPanel();
        jLabel19 = new javax.swing.JLabel();
        jLabelTonalitaPunteggio = new javax.swing.JLabel();
        jLabel21 = new javax.swing.JLabel();
        jLabelTonalitˆCongruenza = new javax.swing.JLabel();
        jLabel23 = new javax.swing.JLabel();
        jLabelTonalitaSize = new javax.swing.JLabel();
        jLabel45 = new javax.swing.JLabel();
        jLabeltonalityPitch = new javax.swing.JLabel();
        jLabel48 = new javax.swing.JLabel();
        jLabelTonalityScalePitch = new javax.swing.JLabel();
        jLabel44 = new javax.swing.JLabel();
        jLabel57 = new javax.swing.JLabel();
        jLabel66 = new javax.swing.JLabel();
        jLabel67 = new javax.swing.JLabel();
        jLabelTonalityExternalScalePitch = new javax.swing.JLabel();
        jPanel7 = new javax.swing.JPanel();
        jLabel17 = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        jLabel22 = new javax.swing.JLabel();
        jLabelGeometricFitness = new javax.swing.JLabel();
        jLabelGeometricFitnessJump = new javax.swing.JLabel();
        jLabelGeometricJumpCount = new javax.swing.JLabel();
        jLabelGeometricFitnessJumpDistance = new javax.swing.JLabel();
        jLabelGeometricFitnessPeakNumber = new javax.swing.JLabel();
        jLabelPeakNumber = new javax.swing.JLabel();
        jLabel31 = new javax.swing.JLabel();
        jLabelPeakDistribution = new javax.swing.JLabel();
        jLabelGeometricFitnessPeakDistribution = new javax.swing.JLabel();
        jLabelGeometricFitnessJumpDirection = new javax.swing.JLabel();
        jLabel36 = new javax.swing.JLabel();
        jLabelGeometricJumpDirectionUp = new javax.swing.JLabel();
        jLabel38 = new javax.swing.JLabel();
        jLabelGeometricJumpDirectionDown = new javax.swing.JLabel();
        jLabel55 = new javax.swing.JLabel();
        jLabel56 = new javax.swing.JLabel();
        jLabel58 = new javax.swing.JLabel();
        jLabel59 = new javax.swing.JLabel();
        jLabel60 = new javax.swing.JLabel();
        jLabel61 = new javax.swing.JLabel();
        jLabel62 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        jLabel63 = new javax.swing.JLabel();
        jLabel64 = new javax.swing.JLabel();
        jLabelGeometricFitnessJumpRecover = new javax.swing.JLabel();
        jLabelGeometricFitnessRibattuta = new javax.swing.JLabel();
        jLabel27 = new javax.swing.JLabel();
        jPanel10 = new javax.swing.JPanel();
        jLabel26 = new javax.swing.JLabel();
        jLabelFrequencyFitness = new javax.swing.JLabel();
        jLabel30 = new javax.swing.JLabel();
        jLabel32 = new javax.swing.JLabel();
        jLabelFrequencyfitnessPitchDistribution = new javax.swing.JLabel();
        jButtonSaveMelodia = new javax.swing.JButton();
        jPanel11 = new javax.swing.JPanel();
        jPanel12 = new javax.swing.JPanel();
        jButtonArchivioRemove = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTableMelodie = new javax.swing.JTable();
        jPanel13 = new javax.swing.JPanel();
        jLabel68 = new javax.swing.JLabel();
        jLabelArchivioNome = new javax.swing.JLabel();
        jLabel69 = new javax.swing.JLabel();
        jLabelArchivioData = new javax.swing.JLabel();
        jTextFieldArchivioNote = new javax.swing.JTextField();
        jLabel70 = new javax.swing.JLabel();
        jButtonArchivioPlay = new javax.swing.JButton();
        jPanel14 = chartArchivioPitch();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel16.setBorder(javax.swing.BorderFactory.createTitledBorder("Dati di ingresso"));

        jButtonAddMidi.setText("+"); // NOI18N
        jButtonAddMidi.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                eventoAddMidi(evt);
            }
        });

        jScrollPane3.setViewportView(jListMidi);

        jButtonRemoveMidi.setText("-"); // NOI18N
        jButtonRemoveMidi.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                eventoRemoveMidi(evt);
            }
        });

        jTextFieldPath.setText("path destinazione...."); // NOI18N

        jButtonExplore.setText("..."); // NOI18N
        jButtonExplore.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                eventoSelectPath(evt);
            }
        });

        jLabel71.setText("Mini inseriti:"); // NOI18N

        jLabelNumMidi.setText("0"); // NOI18N

        org.jdesktop.layout.GroupLayout jPanel16Layout = new org.jdesktop.layout.GroupLayout(jPanel16);
        jPanel16.setLayout(jPanel16Layout);
        jPanel16Layout.setHorizontalGroup(
            jPanel16Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel16Layout.createSequentialGroup()
                .addContainerGap()
                .add(jPanel16Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(org.jdesktop.layout.GroupLayout.TRAILING, jScrollPane3, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 462, Short.MAX_VALUE)
                    .add(jPanel16Layout.createSequentialGroup()
                        .add(jButtonAddMidi)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                        .add(jButtonRemoveMidi)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(jLabel71)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(jLabelNumMidi))
                    .add(jPanel16Layout.createSequentialGroup()
                        .add(jTextFieldPath, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 415, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(jButtonExplore, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 38, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPanel16Layout.setVerticalGroup(
            jPanel16Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel16Layout.createSequentialGroup()
                .add(jPanel16Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jButtonAddMidi)
                    .add(jButtonRemoveMidi)
                    .add(jLabel71)
                    .add(jLabelNumMidi))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jScrollPane3, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 192, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel16Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jTextFieldPath, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jButtonExplore, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 28, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel17.setBorder(javax.swing.BorderFactory.createTitledBorder("Info"));

        jButton1.setText("Crea nuovo file conf"); // NOI18N
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                eventoCreaFileConf(evt);
            }
        });

        jTextAreaOutput.setColumns(20);
        jTextAreaOutput.setRows(5);
        jScrollPane4.setViewportView(jTextAreaOutput);

        org.jdesktop.layout.GroupLayout jPanel17Layout = new org.jdesktop.layout.GroupLayout(jPanel17);
        jPanel17.setLayout(jPanel17Layout);
        jPanel17Layout.setHorizontalGroup(
            jPanel17Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel17Layout.createSequentialGroup()
                .addContainerGap()
                .add(jPanel17Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(org.jdesktop.layout.GroupLayout.TRAILING, jScrollPane4, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 886, Short.MAX_VALUE)
                    .add(jButton1))
                .addContainerGap())
        );
        jPanel17Layout.setVerticalGroup(
            jPanel17Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel17Layout.createSequentialGroup()
                .add(jButton1)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jScrollPane4, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 206, Short.MAX_VALUE)
                .addContainerGap())
        );

        jPanel18.setBorder(javax.swing.BorderFactory.createTitledBorder("incognite"));

        jListTestWeigh.setModel(getListTestModel());
        jListTestWeigh.setSelectionInterval(0, jListTestWeigh.getModel().getSize());
        jScrollPane5.setViewportView(jListTestWeigh);

        org.jdesktop.layout.GroupLayout jPanel18Layout = new org.jdesktop.layout.GroupLayout(jPanel18);
        jPanel18.setLayout(jPanel18Layout);
        jPanel18Layout.setHorizontalGroup(
            jPanel18Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel18Layout.createSequentialGroup()
                .addContainerGap()
                .add(jScrollPane5, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 255, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(131, Short.MAX_VALUE))
        );
        jPanel18Layout.setVerticalGroup(
            jPanel18Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel18Layout.createSequentialGroup()
                .add(jScrollPane5, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 266, Short.MAX_VALUE)
                .addContainerGap())
        );

        org.jdesktop.layout.GroupLayout jPanel15Layout = new org.jdesktop.layout.GroupLayout(jPanel15);
        jPanel15.setLayout(jPanel15Layout);
        jPanel15Layout.setHorizontalGroup(
            jPanel15Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, jPanel15Layout.createSequentialGroup()
                .addContainerGap()
                .add(jPanel15Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                    .add(org.jdesktop.layout.GroupLayout.LEADING, jPanel17, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .add(jPanel15Layout.createSequentialGroup()
                        .add(jPanel16, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 506, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                        .add(jPanel18, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel15Layout.setVerticalGroup(
            jPanel15Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel15Layout.createSequentialGroup()
                .addContainerGap()
                .add(jPanel15Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING, false)
                    .add(jPanel18, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .add(jPanel16, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .add(18, 18, 18)
                .add(jPanel17, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        jTabbedPane.addTab("crea config file", jPanel15);

        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder("configurazione jgap"));

        jLabel4.setText("popolazione iniziale:"); // NOI18N

        popolazioneTextField.setText("20"); // NOI18N
        popolazioneTextField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                evantoConfigJGap(evt);
            }
        });

        popolazioneCostCheckBox.setText("mantieni costante la popolazione"); // NOI18N
        popolazioneCostCheckBox.setMargin(new java.awt.Insets(0, 0, 0, 0));
        popolazioneCostCheckBox.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                evantoConfigJGap(evt);
            }
        });

        jLabel6.setText("note per brano:"); // NOI18N
        jLabel6.setToolTipText("dimensione cromosoma"); // NOI18N

        dimensioneCromosomaTextField.setText("20"); // NOI18N
        dimensioneCromosomaTextField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                evantoConfigJGap(evt);
            }
        });

        jLabel7.setText("nota iniziale:"); // NOI18N

        notaInizioTextField.setText("60"); // NOI18N
        notaInizioTextField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                evantoConfigJGap(evt);
            }
        });

        jLabel8.setText("numero ottava"); // NOI18N

        ottaveComboBox.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "1", "2", "3", "4", "5", "6" }));
        ottaveComboBox.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                evantoConfigJGap(evt);
            }
        });

        preservFitTestIndividualCheckBox.setText("preserva le informazioni di fitness individualmente"); // NOI18N
        preservFitTestIndividualCheckBox.setMargin(new java.awt.Insets(0, 0, 0, 0));
        preservFitTestIndividualCheckBox.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                evantoConfigJGap(evt);
            }
        });

        jLabel14.setText("Punteggio di Fitness accettabile:"); // NOI18N

        jFormattedTextFieldFitnessValueAccetable.setText("0.90"); // NOI18N
        jFormattedTextFieldFitnessValueAccetable.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                evantoConfigJGap(evt);
            }
        });

        org.jdesktop.layout.GroupLayout jPanel3Layout = new org.jdesktop.layout.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .add(jPanel3Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jPanel3Layout.createSequentialGroup()
                        .add(jLabel4)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(popolazioneTextField, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 61, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(popolazioneCostCheckBox))
                    .add(jPanel3Layout.createSequentialGroup()
                        .add(jPanel3Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING, false)
                            .add(org.jdesktop.layout.GroupLayout.LEADING, jPanel3Layout.createSequentialGroup()
                                .add(jLabel7)
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .add(notaInizioTextField, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 61, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                            .add(org.jdesktop.layout.GroupLayout.LEADING, jPanel3Layout.createSequentialGroup()
                                .add(jLabel6)
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                .add(dimensioneCromosomaTextField, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 61, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(jLabel8)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(ottaveComboBox, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 61, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                    .add(preservFitTestIndividualCheckBox)
                    .add(jPanel3Layout.createSequentialGroup()
                        .add(jLabel14)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(jFormattedTextFieldFitnessValueAccetable, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 55, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(461, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel3Layout.createSequentialGroup()
                .add(jPanel3Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabel4)
                    .add(popolazioneTextField, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(popolazioneCostCheckBox))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel3Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabel6)
                    .add(dimensioneCromosomaTextField, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel3Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabel7)
                    .add(notaInizioTextField, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jLabel8)
                    .add(ottaveComboBox, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(preservFitTestIndividualCheckBox)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, 12, Short.MAX_VALUE)
                .add(jPanel3Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabel14)
                    .add(jFormattedTextFieldFitnessValueAccetable, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        jButtonOpen.setText("load"); // NOI18N
        jButtonOpen.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                eventoLoad(evt);
            }
        });

        jButtonSave.setText("save"); // NOI18N
        jButtonSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                eventoSave(evt);
            }
        });

        jLabel5.setText("configurazione in uso:"); // NOI18N

        jTabbedPane1.setBorder(javax.swing.BorderFactory.createTitledBorder("Configurazione Fitness"));

        jLabel2.setText("tonalitˆ:"); // NOI18N

        jComboBoxTonalita.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "maggiore", "minore naturale", "minore armonico", "semi diminuita", "esatonale" }));
        jComboBoxTonalita.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                eventoConfigFitness(evt);
            }
        });

        jLabel3.setText("nota:"); // NOI18N

        jComboBoxNotaTonalita.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Do", "Do#", "Re", "Re#", "Mi", "Fa", "Fa#", "Sol", "Sol#", "La", "La#", "Si" }));
        jComboBoxNotaTonalita.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                eventoConfigFitness(evt);
            }
        });

        jLabel12.setText("peso complessivo del test:"); // NOI18N

        jFormattedTextFieldFitTonalityWeight.setText("0.5"); // NOI18N
        jFormattedTextFieldFitTonalityWeight.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                eventoConfigFitness(evt);
            }
        });

        jLabel41.setText("percentuale note fuori scala:"); // NOI18N

        jLabel42.setText("peso:"); // NOI18N

        jLabel43.setText("peso:"); // NOI18N

        jFormattedTextFieldTonalityPitchWeight.setText("0.2"); // NOI18N
        jFormattedTextFieldTonalityPitchWeight.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                eventoConfigFitness(evt);
            }
        });

        jFormattedTextFieldTonalityScalePitchWeight.setText("0.2"); // NOI18N
        jFormattedTextFieldTonalityScalePitchWeight.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                eventoConfigFitness(evt);
            }
        });

        jFormattedTextFieldTonalityExternalPitchScale.setText("5"); // NOI18N
        jFormattedTextFieldTonalityExternalPitchScale.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                eventoConfigFitness(evt);
            }
        });

        jLabel49.setText("%"); // NOI18N

        jLabel35.setText("note prese fuori scala:"); // NOI18N
        jLabel35.setEnabled(false);

        jLabel65.setText("peso:"); // NOI18N
        jLabel65.setEnabled(false);

        jFormattedTextFieldTonalityExternalScalePitchWeight.setText("0.2"); // NOI18N
        jFormattedTextFieldTonalityExternalScalePitchWeight.setEnabled(false);
        jFormattedTextFieldTonalityExternalScalePitchWeight.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                jFormattedTextFieldTonalityExternalScalePitchWeighteventoConfigFitness(evt);
            }
        });

        org.jdesktop.layout.GroupLayout jPanel2Layout = new org.jdesktop.layout.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .add(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jPanel2Layout.createSequentialGroup()
                        .add(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(jPanel2Layout.createSequentialGroup()
                                .add(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                                    .add(org.jdesktop.layout.GroupLayout.LEADING, jPanel2Layout.createSequentialGroup()
                                        .add(jLabel2)
                                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                        .add(jComboBoxTonalita, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 135, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                        .add(jLabel3))
                                    .add(org.jdesktop.layout.GroupLayout.LEADING, jPanel2Layout.createSequentialGroup()
                                        .add(jLabel12)
                                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                        .add(jFormattedTextFieldFitTonalityWeight, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 61, Short.MAX_VALUE)))
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                .add(jComboBoxNotaTonalita, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 64, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                            .add(jPanel2Layout.createSequentialGroup()
                                .add(jLabel41)
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                .add(jFormattedTextFieldTonalityExternalPitchScale, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 52, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                .add(jLabel49)
                                .add(50, 50, 50)))
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED))
                    .add(jPanel2Layout.createSequentialGroup()
                        .add(jLabel35)
                        .add(175, 175, 175)))
                .add(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jPanel2Layout.createSequentialGroup()
                        .add(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(jLabel43)
                            .add(jLabel42))
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING, false)
                            .add(jFormattedTextFieldTonalityPitchWeight)
                            .add(jFormattedTextFieldTonalityScalePitchWeight, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 49, Short.MAX_VALUE)))
                    .add(jPanel2Layout.createSequentialGroup()
                        .add(jLabel65)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(jFormattedTextFieldTonalityExternalScalePitchWeight, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 49, Short.MAX_VALUE)))
                .addContainerGap(470, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .add(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabel12)
                    .add(jFormattedTextFieldFitTonalityWeight, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .add(16, 16, 16)
                .add(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabel2)
                    .add(jComboBoxTonalita, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jLabel3)
                    .add(jComboBoxNotaTonalita, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jLabel42)
                    .add(jFormattedTextFieldTonalityPitchWeight, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabel41)
                    .add(jLabel43)
                    .add(jFormattedTextFieldTonalityScalePitchWeight, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jLabel49)
                    .add(jFormattedTextFieldTonalityExternalPitchScale, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabel35)
                    .add(jLabel65)
                    .add(jFormattedTextFieldTonalityExternalScalePitchWeight, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(147, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("tonalit\u00e0", jPanel2);

        jLabel1.setText("valore max salto:"); // NOI18N

        jFormattedTextFieldMaxJump.setText("4"); // NOI18N
        jFormattedTextFieldMaxJump.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                eventoConfigFitness(evt);
            }
        });

        jLabel13.setText("peso complessivo del test:"); // NOI18N

        jFormattedTextFieldFitGeometricWeight.setText("0.5"); // NOI18N
        jFormattedTextFieldFitGeometricWeight.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                eventoConfigFitness(evt);
            }
        });

        jLabel20.setText("peso:"); // NOI18N

        jFormattedTextFieldGeometricJumpWeight.setText("0.2"); // NOI18N
        jFormattedTextFieldGeometricJumpWeight.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                eventoConfigFitness(evt);
            }
        });

        jLabel24.setText("valore distanza salti : "); // NOI18N

        jFormattedTextFieldGeometricJumpDistance.setText("3"); // NOI18N
        jFormattedTextFieldGeometricJumpDistance.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                eventoConfigFitness(evt);
            }
        });

        jLabel25.setText("peso:"); // NOI18N

        jFormattedTextFieldGeometricJumpDistanceWeight.setText("0.2"); // NOI18N

        jLabel28.setText("numero dei picchi in 20 note:"); // NOI18N

        jFormattedTextFieldGeometricPeakNumber.setText("3"); // NOI18N
        jFormattedTextFieldGeometricPeakNumber.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                eventoConfigFitness(evt);
            }
        });

        jLabel29.setText("peso:"); // NOI18N

        jFormattedTextFieldGeometricPeakNumberWeight.setText("0.2"); // NOI18N
        jFormattedTextFieldGeometricPeakNumberWeight.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                eventoConfigFitness(evt);
            }
        });

        jLabel33.setText("distribuzione dei picchi:"); // NOI18N

        jLabel34.setText("peso:"); // NOI18N

        jFormattedTextFieldGeometricPeakDistributionWeight.setText("0.2"); // NOI18N
        jFormattedTextFieldGeometricPeakDistributionWeight.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                eventoConfigFitness(evt);
            }
        });

        jLabel37.setText("direzione salti:"); // NOI18N

        jLabel39.setText("peso:"); // NOI18N

        jFormattedTextFieldGeometricJumpDirectionWeight.setText("0.2"); // NOI18N
        jFormattedTextFieldGeometricJumpDirectionWeight.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                eventoConfigFitness(evt);
            }
        });

        jLabel46.setText("recupero dei salti:"); // NOI18N

        jLabel47.setText("peso:"); // NOI18N

        jFormattedTextFieldGeometryJumpRecoverWeight.setText("0.2"); // NOI18N
        jFormattedTextFieldGeometryJumpRecoverWeight.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                eventoConfigFitness(evt);
            }
        });

        jLabel50.setText("ribattuta:"); // NOI18N

        jLabel51.setText("peso:"); // NOI18N

        jFormattedTextFieldGeometricRibattutaWeight.setText("0.2"); // NOI18N
        jFormattedTextFieldGeometricRibattutaWeight.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                eventoConfigFitness(evt);
            }
        });

        org.jdesktop.layout.GroupLayout jPanel6Layout = new org.jdesktop.layout.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .add(jPanel6Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jPanel6Layout.createSequentialGroup()
                        .add(jPanel6Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(jPanel6Layout.createSequentialGroup()
                                .add(jLabel13)
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                .add(jFormattedTextFieldFitGeometricWeight, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 50, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                            .add(jPanel6Layout.createSequentialGroup()
                                .add(jPanel6Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                                    .add(jPanel6Layout.createSequentialGroup()
                                        .add(jLabel24)
                                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                        .add(jFormattedTextFieldGeometricJumpDistance, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 48, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                                    .add(jPanel6Layout.createSequentialGroup()
                                        .add(jLabel1)
                                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                        .add(jFormattedTextFieldMaxJump, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 55, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)))
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, 249, Short.MAX_VALUE)
                                .add(jPanel6Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING, false)
                                    .add(org.jdesktop.layout.GroupLayout.LEADING, jPanel6Layout.createSequentialGroup()
                                        .add(jLabel20)
                                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                        .add(jFormattedTextFieldGeometricJumpWeight, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 54, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                                    .add(org.jdesktop.layout.GroupLayout.LEADING, jPanel6Layout.createSequentialGroup()
                                        .add(jLabel29)
                                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                        .add(jFormattedTextFieldGeometricPeakNumberWeight))
                                    .add(org.jdesktop.layout.GroupLayout.LEADING, jPanel6Layout.createSequentialGroup()
                                        .add(jLabel34)
                                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                        .add(jFormattedTextFieldGeometricPeakDistributionWeight))
                                    .add(org.jdesktop.layout.GroupLayout.LEADING, jPanel6Layout.createSequentialGroup()
                                        .add(jLabel39)
                                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                        .add(jFormattedTextFieldGeometricJumpDirectionWeight, 0, 0, Short.MAX_VALUE))
                                    .add(org.jdesktop.layout.GroupLayout.LEADING, jPanel6Layout.createSequentialGroup()
                                        .add(jLabel25)
                                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                        .add(jFormattedTextFieldGeometricJumpDistanceWeight, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 54, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                                    .add(org.jdesktop.layout.GroupLayout.LEADING, jPanel6Layout.createSequentialGroup()
                                        .add(jLabel47)
                                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                        .add(jFormattedTextFieldGeometryJumpRecoverWeight, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 54, Short.MAX_VALUE))
                                    .add(org.jdesktop.layout.GroupLayout.LEADING, jPanel6Layout.createSequentialGroup()
                                        .add(jLabel51)
                                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                        .add(jFormattedTextFieldGeometricRibattutaWeight, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 54, Short.MAX_VALUE)))
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)))
                        .addContainerGap(335, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                    .add(jPanel6Layout.createSequentialGroup()
                        .add(jLabel37)
                        .addContainerGap(785, Short.MAX_VALUE))
                    .add(jPanel6Layout.createSequentialGroup()
                        .add(jPanel6Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(jPanel6Layout.createSequentialGroup()
                                .add(jLabel28)
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                .add(jFormattedTextFieldGeometricPeakNumber, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 39, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                            .add(jLabel33))
                        .addContainerGap(644, Short.MAX_VALUE))
                    .add(jPanel6Layout.createSequentialGroup()
                        .add(jLabel46)
                        .addContainerGap(765, Short.MAX_VALUE))
                    .add(jPanel6Layout.createSequentialGroup()
                        .add(jLabel50)
                        .addContainerGap(820, Short.MAX_VALUE))))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .add(jPanel6Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabel13)
                    .add(jFormattedTextFieldFitGeometricWeight, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel6Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabel20)
                    .add(jFormattedTextFieldGeometricJumpWeight, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jLabel1)
                    .add(jFormattedTextFieldMaxJump, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel6Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabel25)
                    .add(jFormattedTextFieldGeometricJumpDistanceWeight, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jLabel24)
                    .add(jFormattedTextFieldGeometricJumpDistance, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel6Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabel37)
                    .add(jLabel39)
                    .add(jFormattedTextFieldGeometricJumpDirectionWeight, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel6Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabel28)
                    .add(jFormattedTextFieldGeometricPeakNumber, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jLabel29)
                    .add(jFormattedTextFieldGeometricPeakNumberWeight, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel6Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabel33)
                    .add(jLabel34)
                    .add(jFormattedTextFieldGeometricPeakDistributionWeight, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel6Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabel46)
                    .add(jLabel47)
                    .add(jFormattedTextFieldGeometryJumpRecoverWeight, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel6Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabel50)
                    .add(jLabel51)
                    .add(jFormattedTextFieldGeometricRibattutaWeight, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("geometria", jPanel6);

        jLabel52.setText("peso complessivo del test:"); // NOI18N

        jFormattedTextFieldFitnessFrequencyWeight.setText("0.5"); // NOI18N
        jFormattedTextFieldFitnessFrequencyWeight.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                eventoConfigFitness(evt);
            }
        });

        jLabel53.setText("distribuzione delle note:"); // NOI18N

        jLabel54.setText("peso:"); // NOI18N

        jFormattedTextFieldFrequencyPitchDistributionWeight.setText("0.2"); // NOI18N
        jFormattedTextFieldFrequencyPitchDistributionWeight.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                eventoConfigFitness(evt);
            }
        });

        org.jdesktop.layout.GroupLayout jPanel9Layout = new org.jdesktop.layout.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel9Layout.createSequentialGroup()
                .addContainerGap()
                .add(jPanel9Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jPanel9Layout.createSequentialGroup()
                        .add(jLabel52)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(jFormattedTextFieldFitnessFrequencyWeight, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 54, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                    .add(jPanel9Layout.createSequentialGroup()
                        .add(jLabel53)
                        .add(141, 141, 141)
                        .add(jLabel54)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(jFormattedTextFieldFrequencyPitchDistributionWeight, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 63, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(477, Short.MAX_VALUE))
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel9Layout.createSequentialGroup()
                .addContainerGap()
                .add(jPanel9Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabel52)
                    .add(jFormattedTextFieldFitnessFrequencyWeight, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .add(18, 18, 18)
                .add(jPanel9Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabel53)
                    .add(jLabel54)
                    .add(jFormattedTextFieldFrequencyPitchDistributionWeight, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(217, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("frequenza", jPanel9);

        jButtonSaveAs.setText("save as"); // NOI18N
        jButtonSaveAs.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                eventoSaveAs(evt);
            }
        });

        org.jdesktop.layout.GroupLayout jPanel1Layout = new org.jdesktop.layout.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, jPanel1Layout.createSequentialGroup()
                .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                    .add(org.jdesktop.layout.GroupLayout.LEADING, jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .add(jButtonOpen)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(jButtonSave)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(jButtonSaveAs)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(jLabel5)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(jLabelNomeFileXml))
                    .add(org.jdesktop.layout.GroupLayout.LEADING, jPanel1Layout.createSequentialGroup()
                        .add(27, 27, 27)
                        .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(jTabbedPane1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 931, Short.MAX_VALUE)
                            .add(jPanel3, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel1Layout.createSequentialGroup()
                .add(20, 20, 20)
                .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jButtonOpen)
                    .add(jButtonSave)
                    .add(jButtonSaveAs)
                    .add(jLabel5)
                    .add(jLabelNomeFileXml))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel3, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jTabbedPane1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 385, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(11, Short.MAX_VALUE))
        );

        jTabbedPane.addTab("Configurazione", jPanel1);

        jButtonStart.setText("start"); // NOI18N
        jButtonStart.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                eventoStart(evt);
            }
        });

        jButtonStop.setText("stop"); // NOI18N
        jButtonStop.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                eventoStop(evt);
            }
        });

        jLabel9.setText("evoluzione numero:"); // NOI18N

        jLabelEvoluzione.setText("0.0"); // NOI18N

        jLabel10.setText("popolazione:"); // NOI18N

        jLabelPopolazione.setText("0.0"); // NOI18N

        jButtonGraph.setText("visualizza grafico fitness"); // NOI18N
        jButtonGraph.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonGraphActionPerformed(evt);
            }
        });

        jButtonReset.setText("reset"); // NOI18N
        jButtonReset.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                eventoReset(evt);
            }
        });

        jLabel15.setText("fitness attuale:"); // NOI18N

        jLabelFitnessAttuale.setText("0.0"); // NOI18N

        jButtonPlayPitchs.setText("play"); // NOI18N
        jButtonPlayPitchs.setEnabled(false);
        jButtonPlayPitchs.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                eventoPlayPitchs(evt);
            }
        });

        jButtonStopPitchs.setText("stop"); // NOI18N
        jButtonStopPitchs.setEnabled(false);
        jButtonStopPitchs.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                eventoStopPitchs(evt);
            }
        });

        jPanel8.setBorder(javax.swing.BorderFactory.createTitledBorder("sequenza migliore"));

        jLabel40.setText("note:"); // NOI18N

        double min=Double.valueOf(notaInizioTextField.getText());
        int numOttava=Integer.valueOf(ottaveComboBox.getSelectedItem().toString());
        jScrollPane1.setViewportView(ChartPitchs.getChartPanel(min,numOttava));

        jTextPitchs.setEditable(false);

        org.jdesktop.layout.GroupLayout jPanel8Layout = new org.jdesktop.layout.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel8Layout.createSequentialGroup()
                .add(jLabel40)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jTextPitchs, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(870, Short.MAX_VALUE))
            .add(jScrollPane1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 926, Short.MAX_VALUE)
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel8Layout.createSequentialGroup()
                .add(jPanel8Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabel40)
                    .add(jTextPitchs, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jScrollPane1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 220, Short.MAX_VALUE))
        );

        jLabel19.setText("punteggio complessivo:"); // NOI18N

        jLabelTonalitaPunteggio.setText("0.0"); // NOI18N

        jLabel21.setText("congruenza:"); // NOI18N

        jLabelTonalitˆCongruenza.setText("0/0"); // NOI18N

        jLabel23.setText("size:"); // NOI18N

        jLabelTonalitaSize.setText("0/0"); // NOI18N

        jLabel45.setText("punteggio:"); // NOI18N

        jLabeltonalityPitch.setText("0.0"); // NOI18N

        jLabel48.setText("note fuori scala:"); // NOI18N

        jLabelTonalityScalePitch.setText("0.0"); // NOI18N

        jLabel44.setText("tonalitˆ:"); // NOI18N

        jLabel57.setText("punteggio:"); // NOI18N

        jLabel66.setText("note prese fuori scala:"); // NOI18N

        jLabel67.setText("punteggio:"); // NOI18N

        jLabelTonalityExternalScalePitch.setText("0.0"); // NOI18N

        org.jdesktop.layout.GroupLayout jPanel5Layout = new org.jdesktop.layout.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .add(jPanel5Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jPanel5Layout.createSequentialGroup()
                        .add(jLabel19)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(jLabelTonalitaPunteggio, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 122, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .add(616, 616, 616))
                    .add(org.jdesktop.layout.GroupLayout.TRAILING, jPanel5Layout.createSequentialGroup()
                        .add(jPanel5Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(jPanel5Layout.createSequentialGroup()
                                .add(jLabel44)
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                .add(jLabel21)
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                .add(jLabelTonalitˆCongruenza, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 73, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                .add(jLabel23)
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                .add(jLabelTonalitaSize, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 67, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                            .add(jLabel48)
                            .add(jLabel66))
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, 148, Short.MAX_VALUE)
                        .add(jPanel5Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING, false)
                            .add(jPanel5Layout.createSequentialGroup()
                                .add(jLabel67)
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                .add(jLabelTonalityExternalScalePitch)
                                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .add(org.jdesktop.layout.GroupLayout.TRAILING, jPanel5Layout.createSequentialGroup()
                                .add(jPanel5Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                                    .add(jPanel5Layout.createSequentialGroup()
                                        .add(jLabel57)
                                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                        .add(jLabelTonalityScalePitch))
                                    .add(jPanel5Layout.createSequentialGroup()
                                        .add(jLabel45)
                                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                        .add(jLabeltonalityPitch)))
                                .add(323, 323, 323))))))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel5Layout.createSequentialGroup()
                .add(20, 20, 20)
                .add(jPanel5Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                    .add(jPanel5Layout.createSequentialGroup()
                        .add(jPanel5Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                            .add(jLabel45)
                            .add(jLabeltonalityPitch))
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(jPanel5Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                            .add(jLabel57)
                            .add(jLabelTonalityScalePitch)))
                    .add(jPanel5Layout.createSequentialGroup()
                        .add(jPanel5Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                            .add(jLabel19)
                            .add(jLabelTonalitaPunteggio))
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(jPanel5Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                            .add(jLabel21)
                            .add(jLabel23)
                            .add(jLabelTonalitaSize)
                            .add(jLabelTonalitˆCongruenza)
                            .add(jLabel44))
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(jLabel48)))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel5Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabel66)
                    .add(jLabel67)
                    .add(jLabelTonalityExternalScalePitch))
                .addContainerGap(119, Short.MAX_VALUE))
        );

        jTabbedPane2.addTab("tonalit\u00e0", jPanel5);

        jLabel17.setText("numero dei salti:"); // NOI18N

        jLabel18.setText("numero dei picchi:"); // NOI18N

        jLabel22.setText("punteggio complessivo:"); // NOI18N

        jLabelGeometricFitness.setText("0.0"); // NOI18N

        jLabelGeometricFitnessJump.setText("0.0"); // NOI18N

        jLabelGeometricJumpCount.setText("0"); // NOI18N

        jLabelGeometricFitnessJumpDistance.setText("0.0"); // NOI18N

        jLabelGeometricFitnessPeakNumber.setText("0.0"); // NOI18N

        jLabelPeakNumber.setText("0"); // NOI18N

        jLabel31.setText("Distribuzione dei picchi:"); // NOI18N

        jLabelPeakDistribution.setText("0"); // NOI18N

        jLabelGeometricFitnessPeakDistribution.setText("0.0"); // NOI18N

        jLabelGeometricFitnessJumpDirection.setText("0.0"); // NOI18N

        jLabel36.setText("salti direzione crescente:"); // NOI18N

        jLabelGeometricJumpDirectionUp.setText("0"); // NOI18N

        jLabel38.setText("salti direzione decrescente:"); // NOI18N

        jLabelGeometricJumpDirectionDown.setText("0"); // NOI18N

        jLabel55.setText("distanza salti:"); // NOI18N

        jLabel56.setText("salti recuperati:"); // NOI18N

        jLabel58.setText("punteggio:"); // NOI18N

        jLabel59.setText("punteggio:"); // NOI18N

        jLabel60.setText("punteggio:"); // NOI18N

        jLabel61.setText("punteggio:"); // NOI18N

        jLabel62.setText("punteggio:"); // NOI18N

        jLabel16.setText("ribattute:"); // NOI18N

        jLabel63.setText("punteggio:"); // NOI18N

        jLabel64.setText("punteggio:"); // NOI18N

        jLabelGeometricFitnessJumpRecover.setText("0.0"); // NOI18N

        jLabelGeometricFitnessRibattuta.setText("0.0"); // NOI18N

        jLabel27.setText("conteggio tipi di salto:"); // NOI18N

        org.jdesktop.layout.GroupLayout jPanel7Layout = new org.jdesktop.layout.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .add(jPanel7Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jPanel7Layout.createSequentialGroup()
                        .add(jLabel31)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(jLabelPeakDistribution))
                    .add(jPanel7Layout.createSequentialGroup()
                        .add(jLabel18)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(jLabelPeakNumber, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 43, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                    .add(jLabel55)
                    .add(jPanel7Layout.createSequentialGroup()
                        .add(jLabel22)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(jLabelGeometricFitness, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 55, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                    .add(jLabel56)
                    .add(jLabel16)
                    .add(jPanel7Layout.createSequentialGroup()
                        .add(jPanel7Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(jPanel7Layout.createSequentialGroup()
                                .add(jLabel36)
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                .add(jLabelGeometricJumpDirectionUp, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 51, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                            .add(jLabel27))
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(jPanel7Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(jPanel7Layout.createSequentialGroup()
                                .add(jLabel17)
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                .add(jLabelGeometricJumpCount, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 66, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                            .add(jPanel7Layout.createSequentialGroup()
                                .add(jLabel38)
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                .add(jLabelGeometricJumpDirectionDown, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 45, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)))))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, 29, Short.MAX_VALUE)
                .add(jPanel7Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jPanel7Layout.createSequentialGroup()
                        .add(jLabel62)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(jLabelGeometricFitnessJumpDistance, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 63, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                    .add(jPanel7Layout.createSequentialGroup()
                        .add(jLabel61)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(jLabelGeometricFitnessJump, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 61, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                    .add(jPanel7Layout.createSequentialGroup()
                        .add(jLabel60)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(jLabelGeometricFitnessPeakNumber, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 51, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                    .add(jPanel7Layout.createSequentialGroup()
                        .add(jLabel59)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(jLabelGeometricFitnessPeakDistribution, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 53, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                    .add(jPanel7Layout.createSequentialGroup()
                        .add(jLabel58)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(jLabelGeometricFitnessJumpDirection, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 49, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                    .add(jPanel7Layout.createSequentialGroup()
                        .add(jLabel63)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(jLabelGeometricFitnessJumpRecover))
                    .add(jPanel7Layout.createSequentialGroup()
                        .add(jLabel64)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(jLabelGeometricFitnessRibattuta)))
                .addContainerGap(280, Short.MAX_VALUE))
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel7Layout.createSequentialGroup()
                .add(20, 20, 20)
                .add(jPanel7Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                    .add(jPanel7Layout.createSequentialGroup()
                        .add(jPanel7Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                            .add(jLabel61)
                            .add(jLabelGeometricFitnessJump)
                            .add(jLabel17)
                            .add(jLabelGeometricJumpCount))
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(jPanel7Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                            .add(jLabel60)
                            .add(jLabelGeometricFitnessPeakNumber))
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(jPanel7Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                            .add(jLabel59)
                            .add(jLabelGeometricFitnessPeakDistribution))
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(jPanel7Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                            .add(jLabel58)
                            .add(jLabelGeometricFitnessJumpDirection))
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(jPanel7Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                            .add(jLabel62)
                            .add(jLabelGeometricFitnessJumpDistance)))
                    .add(jPanel7Layout.createSequentialGroup()
                        .add(jPanel7Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                            .add(jLabel22)
                            .add(jLabelGeometricFitness))
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(jLabel27)
                        .add(8, 8, 8)
                        .add(jPanel7Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                            .add(jLabel18)
                            .add(jLabelPeakNumber))
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(jPanel7Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                            .add(jLabel31)
                            .add(jLabelPeakDistribution))
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(jPanel7Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                            .add(jLabel36)
                            .add(jLabel38)
                            .add(jLabelGeometricJumpDirectionUp)
                            .add(jLabelGeometricJumpDirectionDown))
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(jLabel55)))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel7Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabel56)
                    .add(jLabel63)
                    .add(jLabelGeometricFitnessJumpRecover))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel7Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabel16)
                    .add(jLabel64)
                    .add(jLabelGeometricFitnessRibattuta))
                .addContainerGap(23, Short.MAX_VALUE))
        );

        jTabbedPane2.addTab("geometria", jPanel7);

        jLabel26.setText("punteggio complessivo:"); // NOI18N

        jLabelFrequencyFitness.setText("0.0"); // NOI18N

        jLabel30.setText("frequenza delle note:"); // NOI18N

        jLabel32.setText("puneggio:"); // NOI18N

        jLabelFrequencyfitnessPitchDistribution.setText("0.0"); // NOI18N

        org.jdesktop.layout.GroupLayout jPanel10Layout = new org.jdesktop.layout.GroupLayout(jPanel10);
        jPanel10.setLayout(jPanel10Layout);
        jPanel10Layout.setHorizontalGroup(
            jPanel10Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel10Layout.createSequentialGroup()
                .addContainerGap()
                .add(jPanel10Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jPanel10Layout.createSequentialGroup()
                        .add(jLabel30)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, 339, Short.MAX_VALUE)
                        .add(jLabel32)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(jLabelFrequencyfitnessPitchDistribution)
                        .add(335, 335, 335))
                    .add(jPanel10Layout.createSequentialGroup()
                        .add(jLabel26)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(jLabelFrequencyFitness)
                        .addContainerGap(718, Short.MAX_VALUE))))
        );
        jPanel10Layout.setVerticalGroup(
            jPanel10Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel10Layout.createSequentialGroup()
                .addContainerGap()
                .add(jPanel10Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabel26)
                    .add(jLabelFrequencyFitness))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel10Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabel30)
                    .add(jLabel32)
                    .add(jLabelFrequencyfitnessPitchDistribution))
                .addContainerGap(167, Short.MAX_VALUE))
        );

        jTabbedPane2.addTab("frequenza", jPanel10);

        jButtonSaveMelodia.setText("save"); // NOI18N
        jButtonSaveMelodia.setEnabled(false);
        jButtonSaveMelodia.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                eventoSaveMelodia(evt);
            }
        });

        org.jdesktop.layout.GroupLayout jPanel4Layout = new org.jdesktop.layout.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .add(jPanel4Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jTabbedPane2)
                    .add(jPanel8, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .add(jPanel4Layout.createSequentialGroup()
                        .add(jButtonStart)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(jButtonStop)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(jButtonReset)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(jButtonGraph)
                        .add(132, 132, 132)
                        .add(jButtonPlayPitchs)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(jButtonStopPitchs)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, 135, Short.MAX_VALUE)
                        .add(jButtonSaveMelodia))
                    .add(jPanel4Layout.createSequentialGroup()
                        .add(jLabel9)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(jLabelEvoluzione, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 73, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(jLabel10)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(jLabelPopolazione, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 67, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(jLabel15)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(jLabelFitnessAttuale, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 61, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel4Layout.createSequentialGroup()
                .add(25, 25, 25)
                .add(jPanel4Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jButtonStart)
                    .add(jButtonStop)
                    .add(jButtonReset)
                    .add(jButtonGraph)
                    .add(jButtonPlayPitchs)
                    .add(jButtonStopPitchs)
                    .add(jButtonSaveMelodia))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel8, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel4Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabel9)
                    .add(jLabelEvoluzione, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 16, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jLabel10)
                    .add(jLabelPopolazione, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 15, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jLabel15)
                    .add(jLabelFitnessAttuale, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 16, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jTabbedPane2, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 273, Short.MAX_VALUE)
                .add(17, 17, 17))
        );

        jTabbedPane.addTab("Esecuzione", jPanel4);

        jPanel12.setBorder(javax.swing.BorderFactory.createTitledBorder("melodie"));

        jButtonArchivioRemove.setText("-"); // NOI18N
        jButtonArchivioRemove.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                eventoArchivioRemove(evt);
            }
        });

        jTableMelodie.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane2.setViewportView(jTableMelodie);

        org.jdesktop.layout.GroupLayout jPanel12Layout = new org.jdesktop.layout.GroupLayout(jPanel12);
        jPanel12.setLayout(jPanel12Layout);
        jPanel12Layout.setHorizontalGroup(
            jPanel12Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel12Layout.createSequentialGroup()
                .addContainerGap()
                .add(jPanel12Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jScrollPane2, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 886, Short.MAX_VALUE)
                    .add(jButtonArchivioRemove, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 53, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );
        jPanel12Layout.setVerticalGroup(
            jPanel12Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel12Layout.createSequentialGroup()
                .add(jScrollPane2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 183, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                .add(jButtonArchivioRemove))
        );

        jPanel13.setBorder(javax.swing.BorderFactory.createTitledBorder("dettagli"));

        jLabel68.setText("nome:"); // NOI18N

        jLabelArchivioNome.setText("empty"); // NOI18N

        jLabel69.setText("data:"); // NOI18N

        jLabelArchivioData.setText("empty"); // NOI18N

        jLabel70.setText("note:"); // NOI18N

        jButtonArchivioPlay.setText("play"); // NOI18N
        jButtonArchivioPlay.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                eventoArchivioPlay(evt);
            }
        });

        org.jdesktop.layout.GroupLayout jPanel14Layout = new org.jdesktop.layout.GroupLayout(jPanel14);
        jPanel14.setLayout(jPanel14Layout);
        jPanel14Layout.setHorizontalGroup(
            jPanel14Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 844, Short.MAX_VALUE)
        );
        jPanel14Layout.setVerticalGroup(
            jPanel14Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 186, Short.MAX_VALUE)
        );

        org.jdesktop.layout.GroupLayout jPanel13Layout = new org.jdesktop.layout.GroupLayout(jPanel13);
        jPanel13.setLayout(jPanel13Layout);
        jPanel13Layout.setHorizontalGroup(
            jPanel13Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel13Layout.createSequentialGroup()
                .addContainerGap()
                .add(jPanel13Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jPanel13Layout.createSequentialGroup()
                        .add(jLabel68)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(jLabelArchivioNome)
                        .add(236, 236, 236)
                        .add(jLabel69)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(jLabelArchivioData)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, 411, Short.MAX_VALUE)
                        .add(jButtonArchivioPlay))
                    .add(jPanel13Layout.createSequentialGroup()
                        .add(jLabel70)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(jPanel13Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(jPanel14, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .add(jTextFieldArchivioNote, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 844, Short.MAX_VALUE))))
                .addContainerGap())
        );
        jPanel13Layout.setVerticalGroup(
            jPanel13Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel13Layout.createSequentialGroup()
                .add(jPanel13Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jPanel13Layout.createSequentialGroup()
                        .addContainerGap()
                        .add(jPanel13Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                            .add(jLabel68)
                            .add(jLabelArchivioNome)
                            .add(jLabel69)
                            .add(jLabelArchivioData)))
                    .add(jButtonArchivioPlay))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel13Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabel70)
                    .add(jTextFieldArchivioNote, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 74, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel14, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        org.jdesktop.layout.GroupLayout jPanel11Layout = new org.jdesktop.layout.GroupLayout(jPanel11);
        jPanel11.setLayout(jPanel11Layout);
        jPanel11Layout.setHorizontalGroup(
            jPanel11Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, jPanel11Layout.createSequentialGroup()
                .addContainerGap()
                .add(jPanel11Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                    .add(org.jdesktop.layout.GroupLayout.LEADING, jPanel13, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .add(org.jdesktop.layout.GroupLayout.LEADING, jPanel12, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel11Layout.setVerticalGroup(
            jPanel11Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel11Layout.createSequentialGroup()
                .addContainerGap()
                .add(jPanel12, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                .add(jPanel13, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        jTabbedPane.addTab("Archivio", jPanel11);

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(jTabbedPane, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 999, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jTabbedPane, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 710, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void eventoStopPitchs(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_eventoStopPitchs
    stopPitchs();
    }//GEN-LAST:event_eventoStopPitchs

    private void eventoPlayPitchs(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_eventoPlayPitchs
    playPitchs();
    }//GEN-LAST:event_eventoPlayPitchs

    private void eventoReset(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_eventoReset
        resetOutput();
        
        
    }//GEN-LAST:event_eventoReset

    private void eventoStop(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_eventoStop
        StopSoundGenetic();
    }//GEN-LAST:event_eventoStop

    private void jButtonGraphActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonGraphActionPerformed
            final JFrame frame = new JFrame("Memory Usage Demo");
        JPanel panel = ChartFitness.getChartPanel();
        frame.getContentPane().add(panel, BorderLayout.CENTER);
        frame.setBounds(100, 20, 600, 280);
        frame.setVisible(true);
        //panel.new DataGenerator(100).start();
        frame.addWindowListener(new WindowAdapter() {
        public void windowClosing(WindowEvent e) {
            frame.dispose();
            }
        });
    }//GEN-LAST:event_jButtonGraphActionPerformed

    private void eventoStart(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_eventoStart
    StartSoundGenetic();
    }//GEN-LAST:event_eventoStart

    private void eventoConfigFitness(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_eventoConfigFitness
        updateConfigFitness();
    }//GEN-LAST:event_eventoConfigFitness

    private void evantoConfigJGap(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_evantoConfigJGap
        updateConfigJGap();
    }//GEN-LAST:event_evantoConfigJGap

    private void eventoLoad(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_eventoLoad
        loadconfiguration();
    }//GEN-LAST:event_eventoLoad

    private void eventoSave(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_eventoSave
        saveConfiguration();
    }//GEN-LAST:event_eventoSave

    private void eventoSaveAs(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_eventoSaveAs
        saveAsConfiguration();
    }//GEN-LAST:event_eventoSaveAs

    private void jFormattedTextFieldTonalityExternalScalePitchWeighteventoConfigFitness(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jFormattedTextFieldTonalityExternalScalePitchWeighteventoConfigFitness
        // TODO add your handling code here:
}//GEN-LAST:event_jFormattedTextFieldTonalityExternalScalePitchWeighteventoConfigFitness

    private void eventoArchivioPlay(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_eventoArchivioPlay
       playArchivioPitch(); 
       
    }//GEN-LAST:event_eventoArchivioPlay

    private void eventoSaveMelodia(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_eventoSaveMelodia
       salvaMelodia();
    }//GEN-LAST:event_eventoSaveMelodia

    private void eventoArchivioRemove(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_eventoArchivioRemove
        archivioRemove();
    }//GEN-LAST:event_eventoArchivioRemove

    private void eventoAddMidi(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_eventoAddMidi
        addMidi();
    }//GEN-LAST:event_eventoAddMidi

    private void eventoRemoveMidi(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_eventoRemoveMidi
        removeMidi();
    }//GEN-LAST:event_eventoRemoveMidi

    private void eventoSelectPath(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_eventoSelectPath
        selectPath();
    }//GEN-LAST:event_eventoSelectPath

    private void eventoCreaFileConf(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_eventoCreaFileConf
        creaFileConf();
}//GEN-LAST:event_eventoCreaFileConf

    private void playArchivioPitch() {
        synth=new SynthNote(4,93,300);
        synth.play(melodia.getNoteToArrayInt());
    }

    private void removeMidi() {
        
        int index[]=jListMidi.getSelectedIndices();
//        Arrays.sort(index);
//        Collections.reverse(Arrays.asList(index));
        for(int i=index.length-1; i>=0; i--){
            
            System.out.println(index[i]);
            modelList.remove(index[i]);
        }
        jLabelNumMidi.setText(String.valueOf(modelList.getSize()));
        //System.out.println(modelList.getSize());
        jListMidi.setModel(modelList);
        
    }
    private void riempiTabella(){
        
        //DataBase db = DataBase.getIstanza();
        TableModel simpleModel = new SimpleTableModel(DataBase.getIstanza().getMelodie(DataBase.getIstanza().getAllMelodie()));
        TableSorter sorter=new TableSorter(simpleModel);
        jTableMelodie = new JTable(sorter);
        jTableMelodie.getTableHeader().setReorderingAllowed(false);
        sorter.setTableHeader(jTableMelodie.getTableHeader());
        //jTable1 = new JTable(simpleModel);
        jScrollPane2.getViewport().removeAll();
        jScrollPane2.getViewport().add(jTableMelodie, null);
        ascoltaTabella();
    }
    
    private void ascoltaTabella(){
        jTableMelodie.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                //int count=evt.getClickCount();
                //if(count==1){
                    //CATTURARE EVENTO
                    melodia=DataBase.getIstanza().getMelodia(jTableMelodie.getValueAt(jTableMelodie.getSelectedRow(),0).toString());
                    popolaArchivioInfo(melodia);
                //}
            }
        });
        
        jTableMelodie.addKeyListener(new KeyListener() {
            public void keyPressed(KeyEvent e) {
              
            }

            public void keyTyped(KeyEvent e) {
                
            }

            public void keyReleased(KeyEvent e) {
               melodia=DataBase.getIstanza().getMelodia(jTableMelodie.getValueAt(jTableMelodie.getSelectedRow(),0).toString());
               popolaArchivioInfo(melodia);
            }

        });
        
        
    }
    
    private void popolaArchivioInfo(Melodia melodia){
        jLabelArchivioNome.setText(melodia.getNome());
        jLabelArchivioData.setText(melodia.getDataFormatted("dd-MM-yyyy"));
        jTextFieldArchivioNote.setText(melodia.getNoteString());
        
        updateArchivioChart(melodia.getNotetoInt());
    }
    
    private void loadconfiguration() {
        
        JFileChooser fileChooser=new JFileChooser(new File("./"));
        fileChooser.setDialogTitle("Seleziona il file di configurazione...");
        fileChooser.setFileFilter(new FileFilter() {

            @Override
            public boolean accept(File f) {
                if(f.getName().endsWith(".gsxml"))
                    return true;
                else
                    return false;
            }

            @Override
            public String getDescription() {
                return "file condifurazione GeneticSound";
            }
        });
        fileChooser.setFileHidingEnabled(true);
        if(JFileChooser.APPROVE_OPTION==fileChooser.showOpenDialog(this)){
           Configs configs=new Configs();
          
           //parserXml=new Parser();
           fileXml=fileChooser.getSelectedFile();
           configs.ImportFromXml(configFitness, configJGap, fileXml);
           //parserXml.readFileXml(fileXml,configFitness,configJGap);
           jLabelNomeFileXml.setText(fileXml.getName());
        }else
            jLabelNomeFileXml.setText("Errore caricamento");
        
//        for(String key: configFitness.keySet()){
//            System.out.println(key+": "+configFitness.get(key));
//        }
        
            

        reloadGUI();
       
    }

    private void reloadGUI() {
      //JGAP
        if(configJGap.getPreservFitTestIndividual())
            preservFitTestIndividualCheckBox.setSelected(true);
        else
            preservFitTestIndividualCheckBox.setSelected(false);
        if(configJGap.getPopulationSizeConstant())
            popolazioneCostCheckBox.setSelected(true);
        else
            popolazioneCostCheckBox.setSelected(false);
        //System.out.println(configJGap.getSizeSoundChromosome());
        dimensioneCromosomaTextField.setText(String.valueOf(configJGap.getSizeSoundChromosome()));
        popolazioneTextField.setText(String.valueOf(configJGap.getNumPopulation()));
        notaInizioTextField.setText(String.valueOf(configJGap.getMinPitch()));
        ottaveComboBox.setSelectedItem(String.valueOf(configJGap.getNumOttave()));
        jFormattedTextFieldFitnessValueAccetable.setText(String.valueOf(configJGap.getFitnessValueAccetable()));
        //configJGap.setFitnessValueAccetable(Double.parseDouble(jFormattedTextFieldFitnessValueAccetable.getText()));
        
      //tonalitˆ
        //peso complessivo
        jFormattedTextFieldFitTonalityWeight.setText(configFitness.get("fitnessTonalityWeight").toString());
        //dati tonalitˆ tipo e nota
        jComboBoxTonalita.setSelectedIndex(configFitness.get("tonalitaScala").intValue());
        jComboBoxNotaTonalita.setSelectedIndex(configFitness.get("tonalitaNota").intValue());
        // peso tonalitˆ note
        jFormattedTextFieldTonalityPitchWeight.setText(configFitness.get("tonalityPitchWeight").toString());
         //percentuale note fuori scala
        jFormattedTextFieldTonalityExternalPitchScale.setText(String.valueOf(configFitness.get("tonalityExternalPitchScale")*100));
        //peso note fuori scala
        jFormattedTextFieldTonalityScalePitchWeight.setText(configFitness.get("tonalityScalePitchWeight").toString());
        
        
     //Geometria
        //peso complessivo
        jFormattedTextFieldFitGeometricWeight.setText(configFitness.get("fitnessGeometricWeight").toString());
        
        //peso jump
        jFormattedTextFieldGeometricJumpWeight.setText(configFitness.get("jumpWeight").toString());
        //limite accettabile estimatore jump
        jFormattedTextFieldMaxJump.setText(configFitness.get("jumpMax").toString());
        //Estimatore Jump
        //jComboBoxEstimatoreJump.setSelectedIndex(configFitness.get("estimatoreJump").intValue());
        //vicinanza dei salti
        jFormattedTextFieldGeometricJumpDistance.setText(configFitness.get("jumpDistance").toString());
        //peso vicinanza dei salti
        jFormattedTextFieldGeometricJumpDistanceWeight.setText(configFitness.get("jumpDistanceWeight").toString());
        //peso direzione dei salti
        jFormattedTextFieldGeometricJumpDirectionWeight.setText(configFitness.get("jumpDirectionWeight").toString());
        //numero dei picchi
        jFormattedTextFieldGeometricPeakNumber.setText(configFitness.get("peakNumber").toString());
        //peso numero dei picchi
        jFormattedTextFieldGeometricPeakNumberWeight.setText(configFitness.get("peakCountWeight").toString());
        //peso distribuzione dei picchi
        jFormattedTextFieldGeometricPeakDistributionWeight.setText(configFitness.get("peakDistributionWeight").toString());
        //peso ribattuta
        jFormattedTextFieldGeometricRibattutaWeight.setText(configFitness.get("geometricRibattutaWeight").toString());
        //peso recupero salti
//System.out.print("VALORE: geometricJumpRecoverWeight:"+configFitness.get("geometricJumpRecoverWeight"));
        jFormattedTextFieldGeometryJumpRecoverWeight.setText(configFitness.get("geometricJumpRecoverWeight").toString());
       
        //FREQUENZA
        //peso Frequenza
        jFormattedTextFieldFitnessFrequencyWeight.setText(configFitness.get("fitnessFrequencyWeight").toString());
        //peso distribuzione note
        jFormattedTextFieldFrequencyPitchDistributionWeight.setText(configFitness.get("frequencyPitchDistributionWeight").toString());
        
    }

    private void salvaMelodia() {
       DialogSaveAs dialog=new DialogSaveAs(this,true);
      // System.out.println(dialog.getValue());
       boolean result=false;
       if(dialog.getValue()!=null)
        result=DataBase.getIstanza().addMelodia(new Melodia(dialog.getValue(),GregorianCalendar.getInstance().getTime(),threadJGap.getPitchResult()));
      //System.out.println(result);
       riempiTabella();
    }

    private void saveAsConfiguration() {
        JFileChooser fileChooser=new JFileChooser(new File("./"));
        if(JFileChooser.APPROVE_OPTION==fileChooser.showSaveDialog(this)){
           Configs export=new Configs();
           fileXml=fileChooser.getSelectedFile();
           if(!fileXml.getName().endsWith(".gsxml"))
               fileXml=new File(fileXml.getAbsolutePath()+".gsxml");
           export.ExportToXml(configFitness, configJGap,fileXml);
           jLabelNomeFileXml.setText(fileXml.getName());
        }else
            jLabelNomeFileXml.setText("Errore salvataggio");
    }

    private void saveConfiguration() {
        //parserXml=new Parser();
        if(fileXml!=null){
            Configs export=new Configs();
            export.ExportToXml(configFitness, configJGap,fileXml);
           // parserXml.updateXml(configFitness, configJGap, fileXml);
         }else
            saveAsConfiguration();
    }

    private void selectPath() {
        JFileChooser fileChooser=new JFileChooser();
        String path="";
       // fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        if(JFileChooser.APPROVE_OPTION==fileChooser.showSaveDialog(this)){
            path=fileChooser.getSelectedFile().getAbsolutePath();
            if(!path.endsWith(".gsxml"))
               path+=".gsxml";
           jTextFieldPath.setText(path);   
        }
    }
    
    private void updateConfigFitness(){
     //tonalitˆ
        //peso complessivo
        configFitness.setFitnessTonalityWeight(Double.parseDouble(jFormattedTextFieldFitTonalityWeight.getText()));
        //dati tonalitˆ tipo e nota
        configFitness.setTonalita(jComboBoxTonalita.getSelectedIndex(),jComboBoxNotaTonalita.getSelectedIndex());
        // peso tonalitˆ note
        configFitness.setTonalityPitchWeight(Double.parseDouble(jFormattedTextFieldTonalityPitchWeight.getText()));
        //percentuale note fuori scala
        configFitness.setTonalityExternalPitchScale(Double.parseDouble(jFormattedTextFieldTonalityExternalPitchScale.getText())/100.0);
        //peso controllo scala
        configFitness.setTonalityScalePitchWeight(Double.parseDouble(jFormattedTextFieldTonalityScalePitchWeight.getText()));
        //peso note prese fuori scala
        configFitness.setTonalityExternalScalePitchWeight(Double.valueOf(jFormattedTextFieldTonalityExternalScalePitchWeight.getText()).doubleValue());
        
     //Geometria
        //peso complessivo
        configFitness.setFitnessGeometricWeight(Double.parseDouble(jFormattedTextFieldFitGeometricWeight.getText()));
        //peso jump
        configFitness.setJumpWeight(Double.parseDouble(jFormattedTextFieldGeometricJumpWeight.getText()));
        //limite accettabile estimatore jump
        configFitness.setMaxJump(Double.parseDouble(jFormattedTextFieldMaxJump.getText()));
       
        //vicinanza dei salti
        configFitness.setJumpDistance(Double.parseDouble(jFormattedTextFieldGeometricJumpDistance.getText()));
        //peso vicinanza dei salti
        configFitness.setJumpDistanceWeight(Double.parseDouble(jFormattedTextFieldGeometricJumpDistanceWeight.getText()));
        //peso direzione dei salti
        configFitness.setJumpDirectionWeight(Double.parseDouble(jFormattedTextFieldGeometricJumpDirectionWeight.getText()));
        //numero dei picchi
        configFitness.setPeakNumber(Double.parseDouble(jFormattedTextFieldGeometricPeakNumber.getText()));
        //peso numero dei picchi
        configFitness.setPeakCountWeight(Double.parseDouble(jFormattedTextFieldGeometricPeakNumberWeight.getText()));
        //peso distribuzione dei picchi
        configFitness.setPeakDistributionWeight(Double.parseDouble(jFormattedTextFieldGeometricPeakDistributionWeight.getText()));
        //peso ribattuta
        configFitness.setGeometricRibattutaWeight(Double.parseDouble(jFormattedTextFieldGeometricRibattutaWeight.getText()));
        //peso recupero salti
        configFitness.setGeometricJumpRecoverWeight(Double.parseDouble(jFormattedTextFieldGeometryJumpRecoverWeight.getText()));
     //

     //FREQUENZA
        //peso Frequenza
        configFitness.setFitnessFrequencyWeight(Double.parseDouble(jFormattedTextFieldFitnessFrequencyWeight.getText()));
        //peso distribuzione note
        configFitness.setFitnessFrequencyPitchDistributionWeight(Double.valueOf(jFormattedTextFieldFrequencyPitchDistributionWeight.getText()));
        
        
    }
    
    private void updateConfigJGap(){
        configJGap.setPreservFitTestIndividual(preservFitTestIndividualCheckBox.isSelected());
        configJGap.setPopulationSizeConstant(popolazioneCostCheckBox.isSelected());
        configJGap.setSizeSoundChromosome(Integer.parseInt(dimensioneCromosomaTextField.getText()));
        configJGap.setNumPopulation(Integer.parseInt(popolazioneTextField.getText()));
        configJGap.setMinPitch(Integer.parseInt(notaInizioTextField.getText()));
        configJGap.setNumOttave(Integer.parseInt(ottaveComboBox.getSelectedItem().toString()));
        configJGap.setFitnessValueAccetable(Double.parseDouble(jFormattedTextFieldFitnessValueAccetable.getText()));
        //DEBUG
//        System.out.println(configJGap.getPreservFitTestIndividual());
//        System.out.println(configJGap.getPopulationSizeConstant());
//        System.out.println(configJGap.getSizeSoundChromosome());
//        System.out.println(configJGap.getNumPopulation());
//        System.out.println(configJGap.getMinPitch());
//        System.out.println(configJGap.getNumOttave());
        }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new MainFrame().setVisible(true);
            }
        });
    }

    private void StartSoundGenetic() {
        jButtonSaveMelodia.setEnabled(false);
        jButtonStart.setEnabled(false);
        jButtonPlayPitchs.setEnabled(false);
        jButtonStopPitchs.setEnabled(false);
        //getContentPane().add(ChartFitness.getChart());
        //jPanelChart=ChartFitness.getChart();
        ChartFitness.newChart();
        //Debug.setJTextAreaLog(jTextAreaOutput);
        
        //Debug.setEvolutionCountLabel(jLabelEvoluzione);
        //Debug.setPopolationSizeLabel(jLabelPopolazione);
        //Debug.setActualFitnessValue(jLabelFitnessAttuale);
        genetic=new JGapSoundGenetic();
        genetic.initialization(configJGap,configFitness);
        threadJGap=new SoundGeneticThread(genetic);
        threadInfo=new UpdateInfoThread(genetic,this,threadJGap);
        
        threadJGap.start();
        threadInfo.start();
        
 
    }

    public void StopSoundGenetic() {
        if(threadJGap.isAlive()){
            genetic.kill();
           // threadInfo.isActive=false;
            //threadInfo.interrupt();
            jButtonStart.setEnabled(true);
        }else
            jButtonStart.setEnabled(true);
       // threadInfo.interrupt();
        jButtonPlayPitchs.setEnabled(true);
        jButtonSaveMelodia.setEnabled(true);
        
    }

    private void resetOutput() {
       // jTextAreaOutput.setText("");
        ChartFitness.resetChart();
        jLabelFitnessAttuale.setText("0.0");
       // threadInfo.interrupt();
    }
    
    protected void updateInfo(HashMap<String,String> list){
//        for(String key: list.keySet()){
//            System.out.println(key+": "+list.get(key));
//        }
        //CROMOSOMA MIGLIORE
        jTextPitchs.setText(list.get("pitchesNumber"));
        //System.out.println(list.get("pitchesChar"));
       //System.out.println("NOTE: "+list.get("pitches"));


        //ChartPitchs.updateChart(FitnessValue.getPiches());
        //jScrollPane1.removeAll();
        //jScrollPane1.add(ChartPitchs.getChartPanel());
        //jScrollPane1.repaint();
    
        
        
        //JGAP
       jLabelFitnessAttuale.setText(list.get("fitness"));
       jLabelEvoluzione.setText(String.valueOf(FitnessValue.getEvolution()));
       jLabelPopolazione.setText(String.valueOf(FitnessValue.getPopulation()));
        //TONALITA  
        jLabelTonalitaPunteggio.setText(list.get("tonalitaScore"));
        jLabelTonalitaSize.setText(list.get("tonalitaSize"));
        jLabelTonalitˆCongruenza.setText(list.get("tonalitaCongruenza"));
        jLabeltonalityPitch.setText(list.get("tonalityPitch"));
        jLabelTonalityScalePitch.setText(list.get("tonalityScalePitch"));
        jLabelTonalityExternalScalePitch.setText(list.get("tonalityExternalScalePitch"));
        //GEOMETRIA
        jLabelGeometricFitness.setText(list.get("geometryScore"));
        jLabelGeometricFitnessJump.setText(list.get("geometryScoreJump"));
        jLabelGeometricJumpCount.setText(list.get("geometryJumpCount"));
        jLabelGeometricFitnessJumpDistance.setText(list.get("geometryJumpDistance"));
        jLabelGeometricFitnessPeakNumber.setText(list.get("geometryPeakCount"));
        jLabelPeakNumber.setText(list.get("geometryPeakNumber"));
        jLabelPeakDistribution.setText(list.get("geometryPeakIdealDistribution"));
        jLabelGeometricFitnessPeakDistribution.setText(list.get("geometryPeakDistribution"));
        jLabelGeometricFitnessJumpDirection.setText(list.get("geometryJumpDirection"));
        jLabelGeometricJumpDirectionUp.setText(list.get("geometryJumpDirectionUp"));
        jLabelGeometricJumpDirectionDown.setText(list.get("geometryJumpDirectionDown"));
        jLabelGeometricFitnessJumpRecover.setText(list.get("geometryJumpRecover"));
        jLabelGeometricFitnessRibattuta.setText(list.get("geometryRibattuta"));
        //FREQUENZA
        jLabelFrequencyFitness.setText(list.get("frequencyScore"));
        jLabelFrequencyfitnessPitchDistribution.setText(list.get("frequencyPitchDistribution"));
        
    }

    private void playPitchs() {
        jButtonPlayPitchs.setEnabled(false);
        jButtonStopPitchs.setEnabled(true);
        for(int i=0;i<threadJGap.getPitchResult().length;i++){
            System.out.print(threadJGap.getPitchResult()[i]+" ");
        }
        synth=new SynthNote(4,93,300);
        synth.play(threadJGap.getPitchResult());
    }

    private void stopPitchs() {
         jButtonPlayPitchs.setEnabled(true);
        jButtonStopPitchs.setEnabled(false);
        synth.stop();
    }

    private void chartPitch() {
             final JFrame frame = new JFrame("che nervo...");
            double min=Double.valueOf(notaInizioTextField.getText());
            int numOttava=Integer.valueOf(ottaveComboBox.getSelectedItem().toString());
        JPanel panel = ChartPitchs.getChartPanel(min,numOttava);
        frame.getContentPane().add(panel, BorderLayout.CENTER);
        frame.setBounds(100, 20, 600, 280);
        frame.setVisible(true);
        //panel.new DataGenerator(100).start();
        frame.addWindowListener(new WindowAdapter() {
        public void windowClosing(WindowEvent e) {
            frame.dispose();
            }
        });
    }
    
    private ChartPanel chartArchivioPitch() {
        
        int count=0;
        
        JFreeChart chart = ChartFactory.createLineChart("andamento delle note", // chart title
            "tempo", // domain axis label
            "nota", // range axis label
            dataset, // data
            PlotOrientation.VERTICAL, // orientation
            false, // include legend
            true, // tooltips
            true // urls
	);
	((chart.getCategoryPlot()).getDomainAxis()).setTickLabelsVisible(false);
	((chart.getCategoryPlot()).getRangeAxis()).setStandardTickUnits(NumberAxis.createIntegerTickUnits());
	LineAndShapeRenderer render=(LineAndShapeRenderer)(chart.getCategoryPlot()).getRenderer();
	render.setShapesVisible(true);
	render.setShapesFilled(true);
	NumberAxis rangeAxis=(NumberAxis)(chart.getCategoryPlot()).getRangeAxis();
	rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
        
        rangeAxis.setUpperBound(90);
        //rangeAxis.setAutoRange(true);
        rangeAxis.setLowerBound(50);
        ChartPanel chartPanel = new ChartPanel(chart);
	chartPanel.setBorder(new EmptyBorder(0,0,0,0));
        return  chartPanel;
    }
    
    private void updateArchivioChart(ArrayList<Integer> lista){
        int count=0;
        dataset.clear();
        for(int nota: lista){
            dataset.addValue(nota, "brano", String.valueOf(count++));
            count++;
        }
    }
    
    

    
    
    
//    private JPanel getPitchGraph(ArrayList<Integer> pitchs) {
//       return  ChartPitchs.getChartPanel();
//    }
    
//    private void loadPlugin(){
//          pf = new PluginFactory(PLUGIN_PATH);
//          this.listaPlugin = pf.getAllEntryDescriptor();
//    }
//    
//    private void inizializzaPlugin(){
//        Iterator pluginIter=listaPlugin.iterator();
//        while(pluginIter.hasNext()){
//            EntryDescriptor pd=(EntryDescriptor)pluginIter.next();
//            System.out.println("trovato plugin: "+pd.getName()+" id: "+pd.getId());
//            System.out.println("CLASS: "+System.getProperty("java.class.path"));
//            System.out.println("LIBRARY: "+System.getProperty("java.library.path"));
//            PluginEntry plugin=(PluginEntry)pf.getPluginEntry(pd.getId());
//            plugin.initPluginEntry(jTabbedPane);
//            plugin.startPluginEntry();
//        }
//    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JFormattedTextField dimensioneCromosomaTextField;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButtonAddMidi;
    private javax.swing.JButton jButtonArchivioPlay;
    private javax.swing.JButton jButtonArchivioRemove;
    private javax.swing.JButton jButtonExplore;
    private javax.swing.JButton jButtonGraph;
    private javax.swing.JButton jButtonOpen;
    private javax.swing.JButton jButtonPlayPitchs;
    private javax.swing.JButton jButtonRemoveMidi;
    private javax.swing.JButton jButtonReset;
    private javax.swing.JButton jButtonSave;
    private javax.swing.JButton jButtonSaveAs;
    private javax.swing.JButton jButtonSaveMelodia;
    private javax.swing.JButton jButtonStart;
    private javax.swing.JButton jButtonStop;
    private javax.swing.JButton jButtonStopPitchs;
    private javax.swing.JComboBox jComboBoxNotaTonalita;
    private javax.swing.JComboBox jComboBoxTonalita;
    private javax.swing.JFormattedTextField jFormattedTextFieldFitGeometricWeight;
    private javax.swing.JFormattedTextField jFormattedTextFieldFitTonalityWeight;
    private javax.swing.JFormattedTextField jFormattedTextFieldFitnessFrequencyWeight;
    private javax.swing.JFormattedTextField jFormattedTextFieldFitnessValueAccetable;
    private javax.swing.JFormattedTextField jFormattedTextFieldFrequencyPitchDistributionWeight;
    private javax.swing.JFormattedTextField jFormattedTextFieldGeometricJumpDirectionWeight;
    private javax.swing.JFormattedTextField jFormattedTextFieldGeometricJumpDistance;
    private javax.swing.JFormattedTextField jFormattedTextFieldGeometricJumpDistanceWeight;
    private javax.swing.JFormattedTextField jFormattedTextFieldGeometricJumpWeight;
    private javax.swing.JFormattedTextField jFormattedTextFieldGeometricPeakDistributionWeight;
    private javax.swing.JFormattedTextField jFormattedTextFieldGeometricPeakNumber;
    private javax.swing.JFormattedTextField jFormattedTextFieldGeometricPeakNumberWeight;
    private javax.swing.JFormattedTextField jFormattedTextFieldGeometricRibattutaWeight;
    private javax.swing.JFormattedTextField jFormattedTextFieldGeometryJumpRecoverWeight;
    private javax.swing.JFormattedTextField jFormattedTextFieldMaxJump;
    private javax.swing.JFormattedTextField jFormattedTextFieldTonalityExternalPitchScale;
    private javax.swing.JFormattedTextField jFormattedTextFieldTonalityExternalScalePitchWeight;
    private javax.swing.JFormattedTextField jFormattedTextFieldTonalityPitchWeight;
    private javax.swing.JFormattedTextField jFormattedTextFieldTonalityScalePitchWeight;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel30;
    private javax.swing.JLabel jLabel31;
    private javax.swing.JLabel jLabel32;
    private javax.swing.JLabel jLabel33;
    private javax.swing.JLabel jLabel34;
    private javax.swing.JLabel jLabel35;
    private javax.swing.JLabel jLabel36;
    private javax.swing.JLabel jLabel37;
    private javax.swing.JLabel jLabel38;
    private javax.swing.JLabel jLabel39;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel40;
    private javax.swing.JLabel jLabel41;
    private javax.swing.JLabel jLabel42;
    private javax.swing.JLabel jLabel43;
    private javax.swing.JLabel jLabel44;
    private javax.swing.JLabel jLabel45;
    private javax.swing.JLabel jLabel46;
    private javax.swing.JLabel jLabel47;
    private javax.swing.JLabel jLabel48;
    private javax.swing.JLabel jLabel49;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel50;
    private javax.swing.JLabel jLabel51;
    private javax.swing.JLabel jLabel52;
    private javax.swing.JLabel jLabel53;
    private javax.swing.JLabel jLabel54;
    private javax.swing.JLabel jLabel55;
    private javax.swing.JLabel jLabel56;
    private javax.swing.JLabel jLabel57;
    private javax.swing.JLabel jLabel58;
    private javax.swing.JLabel jLabel59;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel60;
    private javax.swing.JLabel jLabel61;
    private javax.swing.JLabel jLabel62;
    private javax.swing.JLabel jLabel63;
    private javax.swing.JLabel jLabel64;
    private javax.swing.JLabel jLabel65;
    private javax.swing.JLabel jLabel66;
    private javax.swing.JLabel jLabel67;
    private javax.swing.JLabel jLabel68;
    private javax.swing.JLabel jLabel69;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel70;
    private javax.swing.JLabel jLabel71;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JLabel jLabelArchivioData;
    private javax.swing.JLabel jLabelArchivioNome;
    private javax.swing.JLabel jLabelEvoluzione;
    private javax.swing.JLabel jLabelFitnessAttuale;
    private javax.swing.JLabel jLabelFrequencyFitness;
    private javax.swing.JLabel jLabelFrequencyfitnessPitchDistribution;
    private javax.swing.JLabel jLabelGeometricFitness;
    private javax.swing.JLabel jLabelGeometricFitnessJump;
    private javax.swing.JLabel jLabelGeometricFitnessJumpDirection;
    private javax.swing.JLabel jLabelGeometricFitnessJumpDistance;
    private javax.swing.JLabel jLabelGeometricFitnessJumpRecover;
    private javax.swing.JLabel jLabelGeometricFitnessPeakDistribution;
    private javax.swing.JLabel jLabelGeometricFitnessPeakNumber;
    private javax.swing.JLabel jLabelGeometricFitnessRibattuta;
    private javax.swing.JLabel jLabelGeometricJumpCount;
    private javax.swing.JLabel jLabelGeometricJumpDirectionDown;
    private javax.swing.JLabel jLabelGeometricJumpDirectionUp;
    private javax.swing.JLabel jLabelNomeFileXml;
    private javax.swing.JLabel jLabelNumMidi;
    private javax.swing.JLabel jLabelPeakDistribution;
    private javax.swing.JLabel jLabelPeakNumber;
    private javax.swing.JLabel jLabelPopolazione;
    private javax.swing.JLabel jLabelTonalitaPunteggio;
    private javax.swing.JLabel jLabelTonalitaSize;
    private javax.swing.JLabel jLabelTonalityExternalScalePitch;
    private javax.swing.JLabel jLabelTonalityScalePitch;
    private javax.swing.JLabel jLabelTonalitˆCongruenza;
    private javax.swing.JLabel jLabeltonalityPitch;
    private javax.swing.JList jListMidi;
    private javax.swing.JList jListTestWeigh;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel13;
    private javax.swing.JPanel jPanel14;
    private javax.swing.JPanel jPanel15;
    private javax.swing.JPanel jPanel16;
    private javax.swing.JPanel jPanel17;
    private javax.swing.JPanel jPanel18;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JTabbedPane jTabbedPane;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTabbedPane jTabbedPane2;
    private javax.swing.JTable jTableMelodie;
    private javax.swing.JTextArea jTextAreaOutput;
    private javax.swing.JTextField jTextFieldArchivioNote;
    private javax.swing.JTextField jTextFieldPath;
    private javax.swing.JTextField jTextPitchs;
    private javax.swing.JFormattedTextField notaInizioTextField;
    private javax.swing.JComboBox ottaveComboBox;
    private javax.swing.JCheckBox popolazioneCostCheckBox;
    private javax.swing.JFormattedTextField popolazioneTextField;
    private javax.swing.JCheckBox preservFitTestIndividualCheckBox;
    // End of variables declaration//GEN-END:variables
    
}
