package com.crimps.ui;

import com.crimps.service.CopyUtils;
import com.sun.deploy.panel.JavaPanel;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.FileOutputStream;
import java.io.RandomAccessFile;
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

    public MainUI getMainUI() {
        return this;
    }

    public MainUI() {
        this.setLayout(new BorderLayout());
        this.setSize(800, 500);
        //北面板：复制按钮、class文件夹设置
        JPanel northPanel = new JPanel();
        copyButton = getCopyButton();
        copyButton.setText("复制");
        classFilePathTextField = new JTextField();
        setClassFileButton = getSetClassFileButton();
        setClassFileButton.setText("选择");
        northPanel.add(classFilePathTextField);
        northPanel.add(setClassFileButton);
        northPanel.add(copyButton);
        this.add(northPanel, BorderLayout.NORTH);
        //中心面板
        JPanel centerPanel = new JPanel();
        centerPanel.setBackground(Color.RED);
        centerPanel.setLayout(new BorderLayout());
        textArea = new JTextArea();
        centerPanel.add(textArea, BorderLayout.CENTER);
        this.add(centerPanel, BorderLayout.CENTER);
        this.setVisible(true);
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
//                    ArrayList<String> oldFileList = (ArrayList<String>)resultMap.get(CopyUtils.OLD_KEY);
                    ArrayList<String> newFileList = (ArrayList<String>)resultMap.get(CopyUtils.NEW_KEY);
                    //提示展示

                    //生成提取文件清单
                    String dir = (String) resultMap.get(CopyUtils.DIR);
                    File fileListFile = new File(dir + File.separator + "文件清单.txt");
                    try {
                        if (!fileListFile.exists()) {
                            fileListFile.createNewFile();
                            StringBuffer content = new StringBuffer("");
                            for (String str : newFileList) {
                                content.append(str).append("\n");
                            }
                            writeTxtFile(content.toString(), fileListFile);
                        }
                    } catch (Exception e1) {
                        e1.printStackTrace();
                    }
                    JOptionPane.showMessageDialog(getMainUI(), "复制成功！", "提示", 0);
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
                }
            }
        });
        return setClassFileButton;
    }

    public static boolean writeTxtFile(String content,File  fileName)throws Exception{
        RandomAccessFile mm=null;
        boolean flag=false;
        FileOutputStream o=null;
        try {
            o = new FileOutputStream(fileName);
            o.write(content.getBytes("GBK"));
            o.close();
//   mm=new RandomAccessFile(fileName,"rw");
//   mm.writeBytes(content);
            flag=true;
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }finally{
            if(mm!=null){
                mm.close();
            }
        }
        return flag;
    }
}
