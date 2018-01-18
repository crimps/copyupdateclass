package com.crimps.ui;

import com.crimps.service.CopyUtils;
import com.crimps.service.FileUtils;
import org.apache.commons.lang.StringUtils;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.util.*;
import java.util.List;

/**
 * @author crimps
 * @create 2018-01-09 16:31
 **/
public class MainUI extends JFrame{

    private JButton copyButton;
    private JButton setClassFileButton;
    private JTextField classFilePathTextField;
    private JTextArea textArea;

    private String classFilePath;
    private final String CONFIG_FILE = "config.txt";

    public MainUI getMainUI() {
        return this;
    }

    public MainUI() {
        this.setLayout(new BorderLayout());
        this.setSize(1200, 500);
        //北面板：复制按钮、class文件夹设置
        JPanel northPanel = new JPanel();
        copyButton = getCopyButton();
        copyButton.setText("复制");
        classFilePathTextField = new JTextField();
        classFilePathTextField.setPreferredSize(new Dimension(400, 30));
        setClassFileButton = getSetClassFileButton();
        setClassFileButton.setText("选择class文件夹");
        setClassFilePathTextField();
        northPanel.add(classFilePathTextField);
        northPanel.add(setClassFileButton);
        northPanel.add(copyButton);
        northPanel.setBorder(new TitledBorder("信息"));
        this.add(northPanel, BorderLayout.NORTH);
        //中心面板
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BorderLayout());
        textArea = new JTextArea();
        centerPanel.add(textArea, BorderLayout.CENTER);
        centerPanel.setBorder(new TitledBorder("svn记录列表"));
        this.add(centerPanel, BorderLayout.CENTER);
        this.setVisible(true);
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }

    private JButton getCopyButton() {
        copyButton = new JButton();
        copyButton.setAction(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (StringUtils.isBlank(classFilePathTextField.getText())) {
                    JOptionPane.showMessageDialog(getMainUI(), "请选择class文件夹", "警告", 0);
                } else {
                    System.out.println(textArea.getText());
                    Map<String, Object> resultMap = CopyUtils.copy(textArea.getText(), classFilePathTextField.getText());
                    List<String> oldFileList = (List<String>)resultMap.get(CopyUtils.OLD_KEY);
                    ArrayList<String> newFileList = (ArrayList<String>)resultMap.get(CopyUtils.NEW_KEY);
                    //生成提取文件清单
                    String dir = (String) resultMap.get(CopyUtils.DIR);
                    File fileListFile = new File(dir + File.separator + "文件清单.txt");
                    try {
                        if (!fileListFile.exists()) {
                            fileListFile.createNewFile();
                            String content = buildFileList(newFileList);
                            FileUtils.writeTxtFile(content, fileListFile);
                        }
                    } catch (Exception e1) {
                        e1.printStackTrace();
                    }
                    StringBuffer tip = new StringBuffer("");
                    for (String str : oldFileList) {
                        if (str.contains("失败")) {
                            tip.append(str).append("\n");
                        }
                    }
                    if (StringUtils.isNotBlank(tip.toString())) {
                        JOptionPane.showMessageDialog(getMainUI(), "复制异常", "警告", 0);
                    } else {
                        JOptionPane.showMessageDialog(getMainUI(), "复制成功！", "提示", 0);
                        FileUtils.open_directory(dir);
                    }
                }
            }
        });
        return copyButton;
    }

    private JButton getSetClassFileButton() {
        setClassFileButton = new JButton();
        setClassFileButton.setAction(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                int result = fileChooser.showOpenDialog(getMainUI());
                if (JFileChooser.APPROVE_OPTION == result) {
                    classFilePathTextField.setText(fileChooser.getSelectedFile().getPath());
                    //写入配置文件
                    try {
                        FileUtils.writeTxtFile(fileChooser.getSelectedFile().getPath(), new File(CONFIG_FILE));
                    } catch (Exception e1) {
                        e1.printStackTrace();
                    }

                }
            }
        });
        return setClassFileButton;
    }

    private String buildFileList(List<String> fileList) {
        StringBuffer fileNames = new StringBuffer("##################### fileName list #####################").append("\n");
        StringBuffer filePaths = new StringBuffer("##################### filePath list #####################").append("\n");
        for (String str : fileList) {
            String temp = StringUtils.isBlank(StringUtils.substringAfterLast(str, "/")) ?
                    StringUtils.substringAfterLast(str, "\\") : StringUtils.substringAfterLast(str, "/");
            fileNames.append(temp).append("\n");
            filePaths.append(str).append("\n");
        }
        return fileNames.toString() + "\n \n" + filePaths.toString();
    }

    /**
     * 从配置文件中给路径文本初始值
     */
    private void setClassFilePathTextField() {
        File configFile = new File(CONFIG_FILE);
        if (configFile.exists()) {
            try {
                String classPath = FileUtils.readTxtFileFirstLine(configFile);
                if (StringUtils.isNotBlank(classPath)) {
                    classFilePathTextField.setText(classPath);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            try {
                configFile.createNewFile();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
