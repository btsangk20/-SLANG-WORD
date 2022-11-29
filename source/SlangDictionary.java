import java.awt.Color;
import java.io.*;
import java.util.*;
import javax.swing.*;
import javax.swing.table.TableModel;
import javax.swing.table.DefaultTableModel;
import java.awt.event.*;

public class SlangDictionary {
  private static HashMap<String, List<String>> DataSource = new HashMap<String, List<String>>();
  private static ArrayList<String> history = new ArrayList<String>();
  private static String[] columnNames = { "ID", "Slang", "Definition" };
  private static JTable Table = new JTable();
  private static JScrollPane ScrollPane = new JScrollPane();
  private static String[][] dataRender;

  public SlangDictionary() {
    ReadFile("database.txt");
    if (DataSource.isEmpty()) {
      ReadFile("slang.txt");
    }
  }

  private static void ReadFile(String fileName) {
    try {
      File file = new File(fileName);
      FileReader fileReader = new FileReader(file);

      BufferedReader bufferedReader = new BufferedReader(fileReader);
      String line;
      while ((line = bufferedReader.readLine()) != null) {
        if (line.contains("`")) {
          List<String> slangWord = new ArrayList<String>();

          String[] slangArray = line.split("`");
          if (slangArray[1].contains("|")) {
            String[] definition = slangArray[1].split("\\|");
            for (int i = 0; i < definition.length; i++) {
              definition[i] = definition[i].trim();
            }
            slangWord = Arrays.asList(definition);
          } else {
            slangWord.add(slangArray[1]);
          }
          DataSource.put(slangArray[0], slangWord);
        }
      }

      fileReader.close();
      bufferedReader.close();
    } catch (Exception ex) {
      System.out.println("Error: " + ex);
    }
  }

  private static void WriteFile(String file_name) {
    try {
      File file = new File(file_name);
      FileWriter fileWriter = new FileWriter(file);
      for (String key : DataSource.keySet()) {
        fileWriter.write(key + "`");
        List<String> definitionList = DataSource.get(key);
        int i = 0;
        for (i = 0; i < definitionList.size() - 1; i++) {
          fileWriter.write(definitionList.get(i) + "| ");
        }

        fileWriter.write(definitionList.get(i) + "\n");
      }

      fileWriter.close();
    } catch (Exception ex) {
      System.out.println("Error: " + ex);
    }
  }

  private static String[][] getDataToTable() {
    String[][] data = new String[DataSource.size()][3];

    int i = 0;
    for (String slang : DataSource.keySet()) {
      data[i][0] = i + 1 + "";
      ;
      data[i][1] = slang;
      List<String> definitionList = DataSource.get(slang);
      String definitionsString = "";
      for (int j = 0; j < definitionList.size() - 1; j++) {
        definitionsString += definitionList.get(j) + ", ";
      }
      definitionsString += definitionList.get(definitionList.size() - 1);
      data[i][2] = definitionsString;
      i++;
    }

    return data;
  }

  private static void reRender(String[][] dataRender) {
    TableModel loadModel = new DefaultTableModel(dataRender, columnNames);
    Table.setModel(loadModel);
  }

  public static void RenderMenuu() {
    JFrame frame = new JFrame("Slang Dictionary");
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setSize(1200, 1000);

    JLabel label = new JLabel("Slang Dictionary");
    label.setBounds(500, 10, 100, 100);

    JPanel search = new JPanel();
    search.setLayout(null);
    search.setBounds(10, 10, 1200, 200);

    JLabel keyword = new JLabel("Keyword");
    keyword.setBounds(10, 70, 100, 100);

    JTextField keywordText = new JTextField();
    keywordText.setBounds(100, 100, 200, 30);

    String[] searchBy = { "Slang", "Definition" };
    JComboBox searchByBox = new JComboBox(searchBy);
    searchByBox.setBounds(320, 100, 200, 30);

    JButton searchButton = new JButton("Search");
    searchButton.setBounds(540, 100, 150, 30);

    JButton historyButton = new JButton("History");
    historyButton.setBounds(710, 100, 150, 30);

    JButton resetButton = new JButton("Reset");
    resetButton.setBounds(880, 100, 150, 30);

    search.add(keyword);
    search.add(keywordText);
    search.add(searchByBox);
    search.add(searchButton);
    search.add(historyButton);
    search.add(resetButton);
    search.add(label);

    searchButton.addActionListener(new ActionListener() {

      public void actionPerformed(ActionEvent event) {
        String key = keywordText.getText();
        history.add(key);
        key = key.toUpperCase();
        if (searchByBox.getSelectedItem().equals("Slang")) {
          if (!DataSource.containsKey(key)) {
            JOptionPane.showMessageDialog(null, "Not Found !");
            dataRender = getDataToTable();
            reRender(dataRender);
          } else {
            dataRender = new String[1][3];
            dataRender[0][0] = "1";
            dataRender[0][1] = key;
            List<String> definitionList = DataSource.get(key);
            String definitionsString = "";
            for (int j = 0; j < definitionList.size() - 1; j++) {
              definitionsString += definitionList.get(j) + ", ";
            }
            definitionsString += definitionList.get(definitionList.size() - 1);
            dataRender[0][2] = definitionsString;
            reRender(dataRender);
          }
        } else {
          ArrayList<String> slangWord = new ArrayList<String>();
          String word = keywordText.getText();
          word = word.toLowerCase();
          for (String slang : DataSource.keySet()) {
            for (String definition : DataSource.get(slang)) {
              if (definition.toLowerCase().contains(word)) {
                slangWord.add(slang);
              }
            }
          }

          if (!slangWord.isEmpty()) {
            dataRender = new String[slangWord.size()][3];
            int i = 0;
            for (String slang : slangWord) {
              dataRender[i][0] = i + 1 + "";
              dataRender[i][1] = slang;
              List<String> definitionList = DataSource.get(slang);
              String definitionsString = "";
              for (int j = 0; j < definitionList.size() - 1; j++) {
                definitionsString += definitionList.get(j) + ", ";
              }
              definitionsString += definitionList.get(definitionList.size() - 1);
              dataRender[i][2] = definitionsString;
              i++;
            }
            reRender(dataRender);
          }
        }
      }
    });

    historyButton.addActionListener(new ActionListener() {

      public void actionPerformed(ActionEvent event) {
        String[] dataArr = new String[history.size()];

        for (int i = 0; i < history.size(); i++) {
          dataArr[i] = history.get(i);
        }

        JList list = new JList(dataArr);
        JScrollPane scrollPane = new JScrollPane(list);
        JOptionPane.showMessageDialog(null, scrollPane, "History", JOptionPane.PLAIN_MESSAGE);

      }
    });

    resetButton.addActionListener(e -> {
      DataSource = new HashMap<String, List<String>>();
      ReadFile("slang.txt");
      dataRender = getDataToTable();
      reRender(dataRender);
    });

    JPanel manage = new JPanel();
    manage.setBounds(10, 120, 500, 300);

    JLabel slang = new JLabel("Slang");
    slang.setBounds(10, 70, 100, 100);
    JTextField slangText = new JTextField();
    slangText.setBounds(100, 100, 400, 30);

    JLabel definition = new JLabel("Definition");
    definition.setBounds(10, 120, 100, 100);

    JTextField definitionText = new JTextField();
    definitionText.setBounds(100, 150, 400, 30);

    JButton addButton = new JButton("Add");
    addButton.setBounds(120, 200, 100, 30);
    JButton deleteButton = new JButton("Delete");
    deleteButton.setBounds(240, 200, 100, 30);
    JButton editButton = new JButton("Edit");
    editButton.setBounds(360, 200, 100, 30);

    addButton.addActionListener(new ActionListener() {

      public void actionPerformed(ActionEvent event) {
        String slangInput = slangText.getText();
        String definitionInput = definitionText.getText();
        slangInput = slangInput.toUpperCase();
        if (!DataSource.containsKey(slangInput)) {
          List<String> definitionList = new ArrayList<String>();
          definitionList.add(definitionInput);
          DataSource.put(slangInput, definitionList);
        } else {
          String[] options = { "Overview", "Duplicate" };
          int x = JOptionPane.showOptionDialog(null, "Slang is already exist !", "Warning",
              JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);
          if (x == 0) {
            List<String> definitionList = new ArrayList<String>();
            definitionList.add(definitionInput);
            DataSource.put(slangInput, definitionList);
          } else {
            List<String> definitionList = DataSource.get(slangInput);
            definitionList.add(definitionInput);
            DataSource.put(slangInput, definitionList);
          }
        }
        dataRender = getDataToTable();
        reRender(dataRender);
        JOptionPane.showMessageDialog(null, "Add successfully !");
      }
    });

    editButton.addActionListener(new ActionListener() {

      public void actionPerformed(ActionEvent event) {
        String slangInput = slangText.getText();
        String definitionInput = definitionText.getText();
        slangInput = slangInput.toUpperCase();
        if (!DataSource.containsKey(slangInput)) {
          JOptionPane.showMessageDialog(null, "Slang does not exist !");
        } else {
          String[] definitions = definitionInput.split(",");
          List<String> definitionList = new ArrayList<String>();
          for (String definition : definitions) {
            definitionList.add(definition);
          }
          DataSource.put(slangInput, definitionList);
          dataRender = getDataToTable();
          reRender(dataRender);
          JOptionPane.showMessageDialog(null, "Edit successfully !");
        }
      }
    });

    deleteButton.addActionListener(new ActionListener() {

      public void actionPerformed(ActionEvent event) {
        String slangInput = slangText.getText();
        slangInput = slangInput.toUpperCase();
        if (!DataSource.containsKey(slangInput)) {
          JOptionPane.showMessageDialog(null, "Slang does not exist !");
        } else {
          int x = JOptionPane.showConfirmDialog(null, "Do you want to delete ?", "Warning",
              JOptionPane.YES_NO_OPTION);
          if (x == JOptionPane.YES_OPTION) {
            DataSource.remove(slangInput);
            dataRender = getDataToTable();
            reRender(dataRender);
            JOptionPane.showMessageDialog(null, "Delete successfully !");
          } else {
            JOptionPane.showMessageDialog(null, "Delete failed !");
          }
        }
      }
    });

    manage.add(addButton);
    manage.add(deleteButton);
    manage.add(editButton);
    manage.add(slang);
    manage.add(slangText);
    manage.add(definition);
    manage.add(definitionText);
    manage.setLayout(null);

    String[][] dataRender = getDataToTable();

    Table = new JTable(dataRender, columnNames);
    ScrollPane = new JScrollPane(Table);
    ScrollPane.setBounds(550, 180, 600, 770);

    JButton randomButton = new JButton("Random");
    randomButton.setBounds(20, 400, 100, 30);
    JLabel randomSlang = new JLabel("Slang");
    randomSlang.setBounds(150, 350, 400, 130);
    JLabel randomDefinition = new JLabel("Definition");
    randomDefinition.setBounds(150, 400, 400, 130);
    Random rand = new Random();

    randomButton.addActionListener(new ActionListener() {

      public void actionPerformed(ActionEvent event) {
        Random rand = new Random();
        int n = rand.nextInt(DataSource.size());
        int i = 0;
        for (String s : DataSource.keySet()) {
          if (i == n) {
            randomSlang.setText("Slang: " + s);
            List<String> tmp = DataSource.get(s);
            String tmpStr = "";
            for (int j = 0; j < tmp.size() - 1; j++) {
              tmpStr += tmp.get(j) + ", ";
            }
            tmpStr += tmp.get(tmp.size() - 1);
            randomDefinition.setText("Definition: " + tmpStr);
            break;
          }
          i++;
        }
      }
    });

    JPanel miniGame = new JPanel();
    miniGame.setBounds(10, 500, 500, 450);
    miniGame.setLayout(null);
    miniGame.setBorder(BorderFactory.createLineBorder(Color.BLACK));

    JLabel miniGameLabel = new JLabel("Mini Game");
    miniGameLabel.setBounds(150, 0, 100, 100);

    JButton startButton = new JButton("Start");
    startButton.setBounds(300, 30, 100, 30);

    JComboBox miniGameByBox = new JComboBox(searchBy);
    miniGameByBox.setBounds(20, 100, 200, 30);

    JLabel question = new JLabel("Question");
    question.setBounds(20, 130, 450, 100);

    JButton solutionA = new JButton("A");
    JButton solutionB = new JButton("B");
    JButton solutionC = new JButton("C");
    JButton solutionD = new JButton("D");

    solutionA.setBounds(20, 200, 200, 60);
    solutionB.setBounds(260, 200, 200, 60);
    solutionC.setBounds(20, 300, 200, 60);
    solutionD.setBounds(260, 300, 200, 60);

    JButton stopButton = new JButton("Stop Game");
    stopButton.setBounds(70, 400, 150, 30);
    JButton nextButton = new JButton("Next Question");
    nextButton.setBounds(270, 400, 150, 30);

    solutionA.setEnabled(false);
    solutionB.setEnabled(false);
    solutionC.setEnabled(false);
    solutionD.setEnabled(false);
    stopButton.setEnabled(false);
    nextButton.setEnabled(false);

    miniGame.add(miniGameLabel);
    miniGame.add(miniGameByBox);
    miniGame.add(startButton);
    miniGame.add(solutionA);
    miniGame.add(solutionB);
    miniGame.add(solutionC);
    miniGame.add(solutionD);
    miniGame.add(question);
    miniGame.add(stopButton);
    miniGame.add(nextButton);

    stopButton.addActionListener(new ActionListener() {

      public void actionPerformed(ActionEvent event) {
        solutionA.setEnabled(false);
        solutionB.setEnabled(false);
        solutionC.setEnabled(false);
        solutionD.setEnabled(false);
        stopButton.setEnabled(false);
        nextButton.setEnabled(false);
        question.setText("Question?");
        solutionA.setText("A");
        solutionB.setText("B");
        solutionC.setText("C");
        solutionD.setText("D");
      }
    });

    ActionListener actionListener = new ActionListener() {

      public void randomAnswer(Boolean isSlang) {
        int n = rand.nextInt(DataSource.size());
        int i = 0;
        if (isSlang) {
          for (String s : DataSource.keySet()) {
            if (i == n) {
              question.setText("Question: " + s);
              List<String> tmp = DataSource.get(s);
              String tmpStr = "";
              for (int j = 0; j < tmp.size() - 1; j++) {
                tmpStr += tmp.get(j) + ", ";
              }
              tmpStr += tmp.get(tmp.size() - 1);
              int n1 = rand.nextInt(4);
              if (n1 == 0) {
                handleEvent("A", tmpStr, true);
              } else if (n1 == 1) {
                handleEvent("B", tmpStr, true);
              } else if (n1 == 2) {
                handleEvent("C", tmpStr, true);
              } else if (n1 == 3) {
                handleEvent("D", tmpStr, true);
              }
              break;
            }
            i++;
          }
        } else if (!isSlang) {
          for (String key : DataSource.keySet()) {
            if (i == n) {
              question.setText("Question: " + DataSource.get(key).get(0));
              int n1 = rand.nextInt(4);
              String tmpStr = key;
              if (n1 == 0) {
                handleEvent("A", tmpStr, false);
              } else if (n1 == 1) {
                handleEvent("B", tmpStr, false);
              } else if (n1 == 2) {
                handleEvent("C", tmpStr, false);
              } else if (n1 == 3) {
                handleEvent("D", tmpStr, false);
              }
              break;
            }
            i++;
          }
        }
      }

      public void handleEvent(String result, String answer, Boolean isSlang) {

        if (solutionA.getActionListeners().length > 0) {
          solutionA.removeActionListener(solutionA.getActionListeners()[0]);
        }

        if (solutionB.getActionListeners().length > 0) {
          solutionB.removeActionListener(solutionB.getActionListeners()[0]);
        }

        if (solutionC.getActionListeners().length > 0) {
          solutionC.removeActionListener(solutionC.getActionListeners()[0]);
        }

        if (solutionD.getActionListeners().length > 0) {
          solutionD.removeActionListener(solutionD.getActionListeners()[0]);
        }

        if (result == "A") {
          solutionA.setText(answer);
          solutionA.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent event) {
              JOptionPane.showMessageDialog(null, "Correct !");
              randomAnswer(isSlang);
            }
          });
        } else {
          if (isSlang) {
            solutionA.setText(
                DataSource.get((String) DataSource.keySet().toArray()[rand.nextInt(DataSource.size())]).get(0));
          } else {
            solutionA.setText(DataSource.keySet().toArray()[rand.nextInt(DataSource.size())].toString());
          }
          solutionA.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent event) {
              JOptionPane.showMessageDialog(null, "Wrong !" + " The correct answer is " + answer.toUpperCase());
              randomAnswer(isSlang);
            }
          });
        }

        if (result == "B") {
          solutionB.setText(answer);
          solutionB.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent event) {
              JOptionPane.showMessageDialog(null, "Correct !");
              randomAnswer(isSlang);
            }
          });
        } else {
          if (isSlang) {
            solutionB.setText(
                DataSource.get((String) DataSource.keySet().toArray()[rand.nextInt(DataSource.size())]).get(0));
          } else {
            solutionB.setText(DataSource.keySet().toArray()[rand.nextInt(DataSource.size())].toString());
          }
          solutionB.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent event) {
              JOptionPane.showMessageDialog(null, "Wrong !" + " The correct answer is " + answer.toUpperCase());
              randomAnswer(isSlang);
            }
          });
        }

        if (result == "C") {
          solutionC.setText(answer);
          solutionC.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent event) {
              JOptionPane.showMessageDialog(null, "Correct !");
              randomAnswer(isSlang);
            }
          });
        } else {
          if (isSlang) {
            solutionC.setText(
                DataSource.get((String) DataSource.keySet().toArray()[rand.nextInt(DataSource.size())]).get(0));
          } else {
            solutionC.setText(DataSource.keySet().toArray()[rand.nextInt(DataSource.size())].toString());
          }
          solutionC.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent event) {
              JOptionPane.showMessageDialog(null, "Wrong !" + " The correct answer is " + answer.toUpperCase());
              randomAnswer(isSlang);
            }
          });
        }

        if (result == "D") {
          solutionD.setText(answer);
          solutionD.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent event) {
              JOptionPane.showMessageDialog(null, "Correct !");
              randomAnswer(isSlang);
            }
          });
        } else {
          if (isSlang) {
            solutionD.setText(
                DataSource.get((String) DataSource.keySet().toArray()[rand.nextInt(DataSource.size())]).get(0));
          } else {
            solutionD.setText(DataSource.keySet().toArray()[rand.nextInt(DataSource.size())].toString());
          }
          solutionD.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent event) {
              JOptionPane.showMessageDialog(null, "Wrong !" + " The correct answer is " + answer.toUpperCase());
              randomAnswer(isSlang);
            }
          });
        }
      }

      public void actionPerformed(ActionEvent event) {
        stopButton.setEnabled(true);
        nextButton.setEnabled(true);
        solutionA.setEnabled(true);
        solutionB.setEnabled(true);
        solutionC.setEnabled(true);
        solutionD.setEnabled(true);

        String searchBy = (String) miniGameByBox.getSelectedItem();
        if (searchBy.equals("Slang")) {
          randomAnswer(true);
        } else if (searchBy.equals("Definition")) {
          randomAnswer(false);
        }
      }
    };
    
    frame.addWindowListener(new WindowAdapter() {
      public void windowClosing(WindowEvent e) {
        WriteFile("database.txt");
      }
    });

    startButton.addActionListener(actionListener);
    nextButton.addActionListener(actionListener);

    frame.add(miniGame);
    frame.add(randomButton);
    frame.add(randomSlang);
    frame.add(randomDefinition);

    frame.add(ScrollPane);

    frame.add(search);
    frame.add(manage);

    frame.setLayout(null);
    frame.setVisible(true);
  }
}
