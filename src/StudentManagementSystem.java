import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

class Student {
    private final int id;
    private final String name;
    private final int score;

    public Student(int id, String name, int score) {
        this.id = id;
        this.name = name;
        this.score = score;

    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;

    }

    public int getScore() {
        return score;
    }

    @Override
    public String toString() {
        return id + "," + name + "," + score;
    }
}

class StudentRepository {
    private static final String FILE_NAME = "C:\\Java_practice\\MakeStudentManagementSystem\\students.txt";

    public List<Student> findAll() {
        List<Student> students = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(
                new FileInputStream(FILE_NAME), StandardCharsets.UTF_8))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length != 3)
                    continue;
                try {
                    int id = Integer.parseInt(parts[0]);
                    String name = parts[1];
                    int score = Integer.parseInt(parts[2]);
                    students.add(new Student(id, name, score));
                } catch (NumberFormatException e) {

                }
            }
        }

        catch (IOException e) {
            e.printStackTrace();
        }
        return students;

    }

    public void save(Student student) {
        try (PrintWriter pw = new PrintWriter(
                new OutputStreamWriter(
                new FileOutputStream(FILE_NAME, true), StandardCharsets.UTF_8))) {
            pw.println(student.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

class StudentManagementSystem {
    public static void main(String[] args) {
        StudentRepository repo = new StudentRepository();
        List<Student> students = repo.findAll();
        for (Student s : students) {
            System.out.println(s);
        }
        Charset consoleCharset = System.console() != null ? System.console().charset() : StandardCharsets.UTF_8;
        Scanner sc = new Scanner(System.in, consoleCharset);
        while (true) {

            int maxId = 0;
            for (Student s : students) {
                if (s.getId() > maxId) {
                    maxId = s.getId();
                }
            }
            int id = maxId + 1;

            System.out.println("\n--- 生徒登録 ---");
            String name;
            name = sc.nextLine();

            System.out.println("スコアを入力してください");
            int score;
            try {
                score = sc.nextInt();
            } catch (Exception e) {
                System.out.println("半角数字を入力してください");
                sc.nextLine();
                continue;
            }
            sc.nextLine();

            Student newStudent = new Student(id, name, score);

            repo.save(newStudent);// セーブ

            students = repo.findAll();

            for (Student s : students) {
                System.out.println(s);
            }
            System.out.println("続けますか？(y/n)");
            String answer = sc.nextLine();

            if (answer.equalsIgnoreCase("n"))
                break;

        }
        sc.close();
    }
}