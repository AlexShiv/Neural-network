package experemental;

import javax.swing.*;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class RotateImage {

    public static void main(String[] args) {
        JFileChooser jFileChooser = new JFileChooser();
        File selectedFile = null;
        int result = jFileChooser.showOpenDialog(new JFrame());
        if (result == JFileChooser.APPROVE_OPTION) {
            selectedFile = jFileChooser.getSelectedFile();
        }
        try (FileReader fileReader = new FileReader(selectedFile)){
            Scanner scanner = new Scanner(fileReader);
            ArrayList<String> arr = new ArrayList<>();
            ArrayList<String> arr2 = new ArrayList<>();
            while (scanner.hasNext()) {
                String line = scanner.nextLine();
                line = line.trim();
                arr.add(line);
                arr2.add(line);
            }

            for (int i = 0; i < arr2.size(); i++) {
                String s = arr2.get(i);
                StringBuilder var = new StringBuilder();
                for (int j = i; j < arr.size(); j++) {
                    String st = arr.get(j);
                    if (s.equals(st)) {
                        continue;
                    } else {
                        String[] sArr = s.split(" ");
                        String[] stArr = st.split(" ");
                        if (sArr[0].equals(stArr[2]) && stArr[0].equals(sArr[2])) {
                            var.append(i+1).append(" ").append(j+1);
                        }
                        else continue;
                    }
                    String vars = var.toString();
                    System.out.println(vars);
                    var.delete(0, var.length());
                }

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
