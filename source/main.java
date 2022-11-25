import java.awt.Color;
import java.io.*;
import java.sql.Struct;
import java.util.*;
import javax.swing.*;

public class main {
  public static HashMap<String, List<String>> map = new HashMap<String, List<String>>();
  public static ArrayList<String> history = new ArrayList<String>();
  public static Scanner sc = new Scanner(System.in);

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

  public static void RenderMenuu(HashMap<String, List<String>> data) {
    JFrame frame = new JFrame("Slang Dictionary");
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setSize(1200, 1000);

    JLabel label = new JLabel("Slang Dictionary");
    label.setBounds(500, 10, 100, 100);

    JPanel search = new JPanel();
    search.setLayout(null);
    search.setBounds(10, 10, 1000, 200);

    JLabel keyword = new JLabel("Keyword");
    keyword.setBounds(10, 70, 100, 100);

    JTextField keywordText = new JTextField();
    keywordText.setBounds(100, 100, 200, 30);

    String[] searchBy = { "Slang", "Definition" };
    JComboBox searchByBox = new JComboBox(searchBy);
    searchByBox.setBounds(320, 100, 200, 30);

    JButton searchButton = new JButton("Search");
    searchButton.setBounds(540, 100, 200, 30);

    JButton history = new JButton("History");
    history.setBounds(760, 100, 200, 30);

    search.add(keyword);
    search.add(keywordText);
    search.add(searchByBox);
    search.add(searchButton);
    search.add(history);
    search.add(label);

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

    // button add, delete, edit
    JButton addButton = new JButton("Add");
    addButton.setBounds(120, 200, 100, 30);
    JButton deleteButton = new JButton("Delete");
    deleteButton.setBounds(240, 200, 100, 30);
    JButton editButton = new JButton("Edit");
    editButton.setBounds(360, 200, 100, 30);

    manage.add(addButton);
    manage.add(deleteButton);
    manage.add(editButton);
    manage.add(slang);
    manage.add(slangText);
    manage.add(definition);
    manage.add(definitionText);
    manage.setLayout(null);

    String[][] dataRender = {
      
    };


    for (String key : map.keySet()) {
      int j = 0;
      List<String> value = map.get(key);
      int i = 0;
      System.out.print(key + ": ");
      for (i = 0; i < value.size() - 1; i++) {
        System.out.print(value.get(i));
      }
      System.out.println(value.get(i));
    }

    // insert data

    String[] columnNames = { "ID", "Slang", "Definition" };
    JTable jTable = new JTable(dataRender, columnNames);
    JScrollPane sp = new JScrollPane(jTable);
    sp.setBounds(550, 200, 600, 700);

    // button random
    JButton randomButton = new JButton("Random");
    randomButton.setBounds(20, 400, 100, 30);
    // label
    JLabel randomSlang = new JLabel("Slang");
    randomSlang.setBounds(150, 350, 130, 130);

    JLabel randomDefinition = new JLabel("Definition");
    randomDefinition.setBounds(200, 350, 130, 130);

    // ađd frame with black border 300 x 300
    JPanel miniGame = new JPanel();
    miniGame.setBounds(10, 450, 500, 500);
    miniGame.setLayout(null);
    miniGame.setBorder(BorderFactory.createLineBorder(Color.BLACK));

    JLabel miniGameLabel = new JLabel("Mini Game");
    miniGameLabel.setBounds(150, 0, 100, 100);

    // button start
    JButton startButton = new JButton("Start");
    startButton.setBounds(300, 30, 100, 30);

    JComboBox miniGameByBox = new JComboBox(searchBy);
    miniGameByBox.setBounds(20, 100, 200, 30);

    // label question
    JLabel question = new JLabel("Question");
    question.setBounds(20, 130, 100, 100);

    // add button with solution a, b, c, d
    JButton solutionA = new JButton("A");
    solutionA.setBounds(20, 200, 200, 60);
    JButton solutionB = new JButton("B");
    solutionB.setBounds(260, 200, 200, 60);
    JButton solutionC = new JButton("C");
    solutionC.setBounds(20, 300, 200, 60);
    JButton solutionD = new JButton("D");
    solutionD.setBounds(260, 300, 200, 60);

    // button stop game and next question
    JButton stopButton = new JButton("Stop Game");
    stopButton.setBounds(70, 400, 150, 30);
    JButton nextButton = new JButton("Next Question");
    nextButton.setBounds(270, 400, 150, 30);

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
    frame.add(miniGame);

    frame.add(randomButton);
    frame.add(randomSlang);
    frame.add(randomDefinition);

    frame.add(sp);

    frame.add(search);
    frame.add(manage);

    frame.setLayout(null);
    frame.setVisible(true);
  }

  public static void main(String[] args) {
    ReadFile("mockSlang.txt");
    if (map.isEmpty()) {
      ReadFile("slang.txt");
    }

    RenderMenuu(map);
  }
}
