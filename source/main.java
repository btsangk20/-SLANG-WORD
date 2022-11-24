import java.awt.Color;
import java.io.*;
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

  public static void RenderMenuu() {
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

    
    frame.add(search);

    frame.setLayout(null);
    frame.setVisible(true);
  }

  public static void main(String[] args) {
    // ReadFile("");
    // if (map.isEmpty()) {
    // ReadFile("");
    // }

    RenderMenuu();
  }
}
