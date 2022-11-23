import java.io.*;
import java.util.*;

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

  public static void main(String[] args) {
    ReadFile("");
    if (map.isEmpty()) {
      ReadFile("");
    }
  }
}
