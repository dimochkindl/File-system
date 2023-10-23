package client.frames;

import client.file_operations.ClientFilesManager;
import client.file_operations.FilesManager;

import javax.swing.*;
import java.awt.*;
import java.net.Socket;
import java.util.List;

public class MainFrame extends JFrame {

    private final Socket socket;
    private final FilesManager manager;

    private final ClientFilesManager clientManager;
    private final JButton uploadButton;
    private final JButton downloadButton, readButton, writeButton, deleteButton, updateButton, clearButton;
    private DefaultListModel serverListModel, clientListModel;
    private JTextArea updateArea;
    private JList<String> serverList, clientList;


    public MainFrame(Socket socket) {
        this.socket = socket;
        manager = new FilesManager(socket);
        clientManager = new ClientFilesManager();

        uploadButton = new JButton("Upload");
        downloadButton = new JButton("Download");
        readButton = new JButton("Read");
        writeButton = new JButton("Write");
        deleteButton = new JButton("Delete");
        updateButton = new JButton("Update");
        clearButton = new JButton("Clear");


        uploadButton.setFont(new Font("Arial", Font.PLAIN, 12));
        downloadButton.setFont(new Font("Arial", Font.PLAIN, 12));
        readButton.setFont(new Font("Arial", Font.PLAIN, 12));
        writeButton.setFont(new Font("Arial", Font.PLAIN, 12));
        deleteButton.setFont(new Font("Arial", Font.PLAIN, 12));
        updateButton.setFont(new Font("Arial", Font.PLAIN, 12));
        clearButton.setFont(new Font("Arial", Font.PLAIN, 12));

        setFunctions();
        setUI();
    }

    private void setUI() {
        setResizable(false);
        setTitle("Cloud Storage");
        setSize(500, 330);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new BorderLayout());
        getContentPane().add(mainPanel);

        JPanel inputPanel = new JPanel(new BorderLayout());
        mainPanel.add(inputPanel, BorderLayout.NORTH);

        updateArea = new JTextArea(3, 30);
        inputPanel.add(updateArea, BorderLayout.CENTER);

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, createClientJListPanel(), createServerJListPanel());
        splitPane.setEnabled(false);
        splitPane.setDividerLocation(145);

        JSplitPane mainSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, updateArea, splitPane);
        mainSplitPane.setEnabled(false);
        mainSplitPane.setDividerLocation(190);

        mainPanel.add(mainSplitPane, BorderLayout.CENTER);

        JPanel buttonPanelUp = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 3));
        JPanel buttonPanelDown = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 3));
        JSplitPane buttonSplitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, buttonPanelUp, buttonPanelDown);
        buttonSplitPane.setEnabled(false);
        mainPanel.add(buttonSplitPane, BorderLayout.SOUTH);

        clearButton.setPreferredSize(new Dimension(90, 25));
        uploadButton.setPreferredSize(new Dimension(90, 25));
        downloadButton.setPreferredSize(new Dimension(90, 25));
        readButton.setPreferredSize(new Dimension(90, 25));

        writeButton.setPreferredSize(new Dimension(95, 25));
        deleteButton.setPreferredSize(new Dimension(95, 25));
        updateButton.setPreferredSize(new Dimension(95, 25));


        buttonPanelUp.add(clearButton);

        buttonPanelUp.add(uploadButton);
        buttonPanelDown.add(updateButton);

        buttonPanelUp.add(downloadButton);
        buttonPanelDown.add(deleteButton);

        buttonPanelUp.add(readButton);
        buttonPanelDown.add(writeButton);
    }

    private JPanel createServerJListPanel() {
        serverListModel = new DefaultListModel<>();
        serverList = new JList<>(serverListModel);
        JScrollPane serverListScrollPane = new JScrollPane(serverList);

        JPanel panel = new JPanel(new BorderLayout());
        JLabel label = new JLabel("Available Files:");
        panel.add(label, BorderLayout.NORTH);
        panel.add(serverListScrollPane, BorderLayout.CENTER);

        List<String> files = manager.showFiles();
        for (String file : files) {
            serverListModel.addElement(file);
        }

        serverList.addListSelectionListener(e -> clientList.clearSelection());

        return panel;
    }

    private JPanel createClientJListPanel() {
        clientListModel = new DefaultListModel<>();
        clientList = new JList<>(clientListModel);
        JScrollPane clientListScrollPane = new JScrollPane(clientList);

        JPanel panel = new JPanel(new BorderLayout());
        JLabel label = new JLabel("Your Files:");
        panel.add(label, BorderLayout.NORTH);
        panel.add(clientListScrollPane, BorderLayout.CENTER);

        List<String> files = clientManager.showUserFiles();
        for (String file : files) {
            clientListModel.addElement(file);
        }

        clientList.addListSelectionListener(e -> serverList.clearSelection());

        return panel;
    }


    private void setFunctions() {

        setClearButtonListener();
        setWriteButtonListener();
        setUploadButtonListener();
        setDownloadButtonListener();
        setDeleteButtonListener();
        setReadButtonListener();

        int delay = 8000;
        Timer timer = new Timer(delay, e -> {
            String curr = clientList.getSelectedValue();
            if(updateClientFileList(curr)) {
                clientList.setSelectedValue(curr, false);
            }
        });

        Timer oneMoreTimer = new Timer(delay, e -> {
            String curr = serverList.getSelectedValue();
            if(updateServerList(curr)) {
                serverList.setSelectedValue(curr, false);
            }
        });

        oneMoreTimer.start();
        timer.start();
    }

    private void setClearButtonListener(){
        clearButton.addActionListener(e->{
            updateArea.setText("");
        });
    }

    private void setWriteButtonListener(){
        writeButton.addActionListener(e->{
            if(clientList.getSelectedValue() != null){
                clientManager.writeFile(clientList.getSelectedValue(), updateArea.getText());
            } else if (serverList.getSelectedValue() != null) {
                manager.writeFile(serverList.getSelectedValue(), updateArea.getText());
            }else{
                showError("No file was chosen to write into");
            }
        });
    }

    private void setUploadButtonListener(){
        uploadButton.addActionListener(e -> {
            String status = manager.uploadFile(clientList.getSelectedValue());
            if (status == null) {
                showError("The file is already exists on server");
            } else if (status.equals("File doesn't exist")) {
                showError("File doesn't exist");
            }
        });
    }

    private void setDownloadButtonListener(){
        downloadButton.addActionListener(e -> {
            if (serverList.getSelectedValue() == null) {
                System.out.println("download value is null");
            } else {
                String status = manager.downloadFile(serverList.getSelectedValue());
                if (status.equals("alreadyExists")) {
                    showError("file is already exists");
                } else if (status.equals("doesn'tExistsOnServer")) {
                    showError("File doesn't exists on server");
                }
            }
        });
    }

    private void setDeleteButtonListener(){
        deleteButton.addActionListener(e ->{
            if(serverList.getSelectedValue() != null){
                if(!manager.deleteFile(serverList.getSelectedValue())){
                    showError("error deleting file from server");
                }
            } else if (clientList.getSelectedValue() != null) {
                if(!clientManager.deleteFile(clientList.getSelectedValue())){
                    showError("error deleting file from client");
                }
            }else{
                showError("No file was chosen to delete");
            }
        });
    }

    private void setReadButtonListener(){
        readButton.addActionListener(e->{
            if(clientList.getSelectedValue() != null){
                List<String> readFile = clientManager.readFile(clientList.getSelectedValue());
                if(readFile == null){
                    showError("Can't read from empty file");
                }
                readFile.forEach(t->updateArea.append(t+"\n"));
            } else if (serverList.getSelectedValue() != null) {
                updateArea.setText("");
                List<String> readFile = manager.readFile(serverList.getSelectedValue());
                if(readFile == null){
                    showError("Can't read from empty file");
                }
                readFile.forEach(t->updateArea.append(t+"\n"));
            }else{
                showError("No file was chosen to read from");
            }
        });
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }

    private boolean updateServerList(String curr) {
        serverListModel.clear();
        List<String> files = manager.showFiles();

        for (String file : files) {
            serverListModel.addElement(file);
        }

        serverList.revalidate();
        return files.contains(curr);
    }



    public boolean updateClientFileList(String curr) {
        clientListModel.clear();
        List<String> files = clientManager.showUserFiles();

        for (String file : files) {
            clientListModel.addElement(file);
        }

        clientList.revalidate();
        clientList.repaint();

        return files.contains(curr);
    }
}
