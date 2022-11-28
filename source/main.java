import java.awt.Color;
import java.io.*;
import java.util.*;
import javax.swing.*;
import javax.swing.table.TableModel;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionListener;

public class main {
  public static HashMap<String, List<String>> map = new HashMap<String, List<String>>();
  public static ArrayList<String> history = new ArrayList<String>();
  public static Scanner sc = new Scanner(System.in);
  public static String[] columnNames = { "ID", "Slang", "Definition" };
  public static JTable jTable = new JTable();
  public static JScrollPane sp = new JScrollPane();

  public static void searchBySlang() {

    System.out.print("Press a Slang word: ");
    String key = sc.nextLine();
    history.add(key);
    key = key.toUpperCase();
    if (!map.containsKey(key)) {
      System.out.println("Not Found !");
    } else {
      List<String> l = map.get(key);
      System.out.println("Definition:");
      for (String s : l) {
        System.out.println("- " + s);
      }
    }
  }

  public static void searchByDef() {

    ArrayList<String> slang_means = new ArrayList<String>();
    System.out.print("Press a Definition: ");
    String word = sc.nextLine();
    history.add(word);
    word = word.toLowerCase();
    for (String i : map.keySet()) {
      for (String s : map.get(i)) {
        if (s.toLowerCase().contains(word)) {
          slang_means.add(i);
        }
      }
    }

    if (!slang_means.isEmpty()) {
      System.out.println("Slang Words found: ");
      for (String i : slang_means) {
        System.out.print("- " + i + ": ");
        ShowDefinition(i);
      }
    } else {
      System.out.println("Not Found !");
    }
  }

  public static void ShowDefinition(String slang) {
    List<String> l = map.get(slang);
    for (String s : l) {
      System.out.print(s + ", ");
    }
    System.out.print("\b\b     \n");
  }

  public static void ReadFile(String fileName) {
    try {
      File f = new File(fileName);
      FileReader fr = new FileReader(f);

      BufferedReader br = new BufferedReader(fr);
      String line;
      while ((line = br.readLine()) != null) {
        if (line.contains("`")) {
          List<String> tar = new ArrayList<String>();

          String[] s = line.split("`");
          if (s[1].contains("|")) {
            String[] tmp = s[1].split("\\|");
            for (int i = 0; i < tmp.length; i++) {
              tmp[i] = tmp[i].trim();
            }
            tar = Arrays.asList(tmp);
          } else {
            tar.add(s[1]);
          }
          map.put(s[0], tar);
        }
      }

      fr.close();
      br.close();
    } catch (Exception ex) {
      System.out.println("Error: " + ex);
    }
  }

  public static void WriteFile(String file_name) {
    try {
      File f = new File(file_name);
      FileWriter fw = new FileWriter(f);
      for (String key : map.keySet()) {
        fw.write(key + "`");
        List<String> tmp = map.get(key);
        int i = 0;
        for (i = 0; i < tmp.size() - 1; i++) {
          fw.write(tmp.get(i) + "| ");
        }

        fw.write(tmp.get(i) + "\n");
      }

      fw.close();
    } catch (Exception ex) {
      System.out.println("Error: " + ex);
    }
  }

  public static void WriteHistory(String file_name) {
    try {
      File f = new File(file_name);
      FileWriter fw = new FileWriter(f);
      BufferedWriter bw = new BufferedWriter(fw);
      for (String i : history) {
        fw.write(i + "\n");
      }

      fw.close();
      bw.close();
    }

    catch (Exception ex) {
      System.out.println("Error: " + ex);
    }
  }

  public static ArrayList<String> loadHistory(String file_name) {
    ArrayList<String> his = new ArrayList<String>();
    try {
      File f = new File(file_name);
      FileReader fr = new FileReader(f);

      BufferedReader br = new BufferedReader(fr);
      String line;
      while ((line = br.readLine()) != null) {
        his.add(line);
      }

      fr.close();
      br.close();
    } catch (Exception ex) {
      System.out.println("Error: " + ex);
    }

    return his;
  }

  public static String[][] getDataToTable() {
    String[][] dataArr = new String[map.size()][3];

    int i = 0;
    for (String key : map.keySet()) {
      dataArr[i][0] = i + 1 + "";
      ;
      dataArr[i][1] = key;
      List<String> tmp = map.get(key);
      String tmpStr = "";
      for (int j = 0; j < tmp.size() - 1; j++) {
        tmpStr += tmp.get(j) + ", ";
      }
      tmpStr += tmp.get(tmp.size() - 1);
      dataArr[i][2] = tmpStr;
      i++;
    }

    return dataArr;
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

    searchButton.addActionListener(new java.awt.event.ActionListener() {

      public void actionPerformed(java.awt.event.ActionEvent evt) {
        String key = keywordText.getText();
        main.history.add(key);
        key = key.toUpperCase();
        String[][] arrData = getDataToTable();
        TableModel loadModel = new DefaultTableModel(arrData, columnNames);
        jTable.setModel(loadModel);
        if (searchByBox.getSelectedItem().equals("Slang")) {
          if (!map.containsKey(key)) {
            JOptionPane.showMessageDialog(null, "Not Found !");
          } else {
            String[][] dataArr = new String[1][3];
            dataArr[0][0] = "1";
            dataArr[0][1] = key;
            List<String> tmp = map.get(key);
            String tmpStr = "";
            for (int j = 0; j < tmp.size() - 1; j++) {
              tmpStr += tmp.get(j) + ", ";
            }
            tmpStr += tmp.get(tmp.size() - 1);
            dataArr[0][2] = tmpStr;
            TableModel model = new DefaultTableModel(dataArr, columnNames);
            jTable.setModel(model);
          }
        } else {
          ArrayList<String> slang_means = new ArrayList<String>();
          String word = keywordText.getText();
          word = word.toLowerCase();
          for (String i : map.keySet()) {
            for (String s : map.get(i)) {
              if (s.toLowerCase().contains(word)) {
                slang_means.add(i);
              }
            }
          }

          if (!slang_means.isEmpty()) {
            String[][] dataArr = new String[slang_means.size()][3];
            int i = 0;
            for (String s : slang_means) {
              dataArr[i][0] = i + 1 + "";
              dataArr[i][1] = s;
              List<String> tmp = map.get(s);
              String tmpStr = "";
              for (int j = 0; j < tmp.size() - 1; j++) {
                tmpStr += tmp.get(j) + ", ";
              }
              tmpStr += tmp.get(tmp.size() - 1);
              dataArr[i][2] = tmpStr;
              i++;
            }
            TableModel model = new DefaultTableModel(dataArr, columnNames);
            jTable.setModel(model);
          }
        }
      }
    });

    historyButton.addActionListener(new java.awt.event.ActionListener() {

      public void actionPerformed(java.awt.event.ActionEvent evt) {
        String[] dataArr = new String[main.history.size()];

        for (int i = 0; i < main.history.size(); i++) {
          dataArr[i] = main.history.get(i);
        }

        JList list = new JList(dataArr);
        JScrollPane scrollPane = new JScrollPane(list);
        JOptionPane.showMessageDialog(null, scrollPane, "History", JOptionPane.PLAIN_MESSAGE);

      }
    });

    resetButton.addActionListener(e -> {
      map = new HashMap<String, List<String>>();
      ReadFile("slang.txt");
      String[][] dataArr = getDataToTable();
      TableModel model = new DefaultTableModel(dataArr, columnNames);
      jTable.setModel(model);
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

    addButton.addActionListener(new java.awt.event.ActionListener() {

      public void actionPerformed(java.awt.event.ActionEvent evt) {
        String slang = slangText.getText();
        String definition = definitionText.getText();
        slang = slang.toUpperCase();
        if (map.containsKey(slang)) {
          JOptionPane.showMessageDialog(null, "Slang already exists !");
        } else {
          String[] tmp = definition.split(",");
          List<String> tmpList = new ArrayList<String>();
          for (String s : tmp) {
            tmpList.add(s);
          }
          map.put(slang, tmpList);
          String[][] arrData = getDataToTable();
          TableModel model = new DefaultTableModel(arrData, columnNames);
          jTable.setModel(model);
          JOptionPane.showMessageDialog(null, "Add successfully !");
        }
      }
    });

    editButton.addActionListener(new java.awt.event.ActionListener() {

      public void actionPerformed(java.awt.event.ActionEvent evt) {
        String slang = slangText.getText();
        String definition = definitionText.getText();
        slang = slang.toUpperCase();
        if (!map.containsKey(slang)) {
          JOptionPane.showMessageDialog(null, "Slang does not exist !");
        } else {
          String[] tmp = definition.split(",");
          List<String> tmpList = new ArrayList<String>();
          for (String s : tmp) {
            tmpList.add(s);
          }
          map.put(slang, tmpList);
          String[][] arrData = getDataToTable();
          TableModel model = new DefaultTableModel(arrData, columnNames);
          jTable.setModel(model);
          JOptionPane.showMessageDialog(null, "Edit successfully !");
        }
      }
    });

    deleteButton.addActionListener(new java.awt.event.ActionListener() {

      public void actionPerformed(java.awt.event.ActionEvent evt) {
        String slang = slangText.getText();
        slang = slang.toUpperCase();
        if (!map.containsKey(slang)) {
          JOptionPane.showMessageDialog(null, "Slang does not exist !");
        } else {
          map.remove(slang);
          String[][] arrData = getDataToTable();
          TableModel model = new DefaultTableModel(arrData, columnNames);
          jTable.setModel(model);
          JOptionPane.showMessageDialog(null, "Delete successfully !");
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

    main.jTable = new JTable(dataRender, columnNames);
    main.sp = new JScrollPane(jTable);
    main.sp.setBounds(550, 180, 600, 770);

    JButton randomButton = new JButton("Random");
    randomButton.setBounds(20, 400, 100, 30);
    JLabel randomSlang = new JLabel("Slang");
    randomSlang.setBounds(150, 350, 400, 130);
    JLabel randomDefinition = new JLabel("Definition");
    randomDefinition.setBounds(150, 400, 400, 130);
    Random rand = new Random();

    randomButton.addActionListener(new java.awt.event.ActionListener() {

      public void actionPerformed(java.awt.event.ActionEvent evt) {
        Random rand = new Random();
        int n = rand.nextInt(map.size());
        int i = 0;
        for (String s : map.keySet()) {
          if (i == n) {
            randomSlang.setText("Slang: " + s);
            List<String> tmp = map.get(s);
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

    stopButton.addActionListener(new java.awt.event.ActionListener() {

      public void actionPerformed(java.awt.event.ActionEvent evt) {
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
        int n = rand.nextInt(map.size());
        int i = 0;
        if (isSlang) {
          for (String s : map.keySet()) {
            if (i == n) {
              question.setText("Question: " + s);
              List<String> tmp = map.get(s);
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
          for (String key : map.keySet()) {
            if (i == n) {
              question.setText("Question: " + map.get(key).get(0));
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
          solutionA.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
              JOptionPane.showMessageDialog(null, "Correct !");
              randomAnswer(isSlang);
            }
          });
        } else {
          if (isSlang) {
            solutionA.setText(map.get((String) map.keySet().toArray()[rand.nextInt(map.size())]).get(0));
          } else {
            solutionA.setText(map.keySet().toArray()[rand.nextInt(map.size())].toString());
          }
          solutionA.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
              JOptionPane.showMessageDialog(null, "Wrong !" + " The correct answer is " + answer.toUpperCase());
              randomAnswer(isSlang);
            }
          });
        }

        if (result == "B") {
          solutionB.setText(answer);
          solutionB.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
              JOptionPane.showMessageDialog(null, "Correct !");
              randomAnswer(isSlang);
            }
          });
        } else {
          if (isSlang) {
            solutionB.setText(map.get((String) map.keySet().toArray()[rand.nextInt(map.size())]).get(0));
          } else {
            solutionB.setText(map.keySet().toArray()[rand.nextInt(map.size())].toString());
          }
          solutionB.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
              JOptionPane.showMessageDialog(null, "Wrong !" + " The correct answer is " + answer.toUpperCase());
              randomAnswer(isSlang);
            }
          });
        }

        if (result == "C") {
          solutionC.setText(answer);
          solutionC.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
              JOptionPane.showMessageDialog(null, "Correct !");
              randomAnswer(isSlang);
            }
          });
        } else {
          if (isSlang) {
            solutionC.setText(map.get((String) map.keySet().toArray()[rand.nextInt(map.size())]).get(0));
          } else {
            solutionC.setText(map.keySet().toArray()[rand.nextInt(map.size())].toString());
          }
          solutionC.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
              JOptionPane.showMessageDialog(null, "Wrong !" + " The correct answer is " + answer.toUpperCase());
              randomAnswer(isSlang);
            }
          });
        }

        if (result == "D") {
          solutionD.setText(answer);
          solutionD.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
              JOptionPane.showMessageDialog(null, "Correct !");
              randomAnswer(isSlang);
            }
          });
        } else {
          if (isSlang) {
            solutionD.setText(map.get((String) map.keySet().toArray()[rand.nextInt(map.size())]).get(0));
          } else {
            solutionD.setText(map.keySet().toArray()[rand.nextInt(map.size())].toString());
          }
          solutionD.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
              JOptionPane.showMessageDialog(null, "Wrong !" + " The correct answer is " + answer.toUpperCase());
              randomAnswer(isSlang);
            }
          });
        }
      }

      public void actionPerformed(java.awt.event.ActionEvent evt) {
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

    startButton.addActionListener(actionListener);
    nextButton.addActionListener(actionListener);

    frame.add(miniGame);
    frame.add(randomButton);
    frame.add(randomSlang);
    frame.add(randomDefinition);

    frame.add(sp);

    frame.add(search);
    frame.add(manage);

    main.history = main.loadHistory("history.txt");

    frame.setLayout(null);
    frame.setVisible(true);
  }

  public static void main(String[] args) {
    ReadFile("mockSlang.txt");
    if (map.isEmpty()) {
      ReadFile("slang.txt");
    }
    RenderMenuu();
  }
}
