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
    private static final String FILE_NAME = "students.txt";

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

    public void saveAll(List<Student> students) {
        try (PrintWriter pw = new PrintWriter(
                new OutputStreamWriter(
                        new FileOutputStream(FILE_NAME, false), StandardCharsets.UTF_8))) {
            for (Student s : students) {
                pw.println(s.toString());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

class StudentService {
    private StudentRepository repo = new StudentRepository();

    public int findMaxId(List<Student> students) {
        int maxId = 0;
        for (Student s : students) {
            if (s.getId() > maxId) {
                maxId = s.getId();
            }
        }
        return maxId + 1;
    }

    public void addStudent(String name, int score) {
        List<Student> students = repo.findAll();
        int id = findMaxId(students);
        Student newStudent = new Student(id, name, score);
        repo.save(newStudent);

    }

    public List<Student> getAllStudents() {
        return repo.findAll();
    }

    public void updateStudent(int id, String name, int score) {
        boolean found = false;
        List<Student> students = repo.findAll();
        for (int i = 0; i < students.size(); i++) {
            Student s = students.get(i);
            if (s.getId() == id) {
                students.set(i, new Student(id, name, score));
                found = true;
                break;
            }
        }
        if (!found) {
            System.out.println("該当のIDの生徒が存在しません");
        } else {
            repo.saveAll(students);
        }
    }

    public void deleteStudent(int id) {
        boolean found = false;
        List<Student> students = repo.findAll();
        for (int i = 0; i < students.size(); i++) {
            Student s = students.get(i);
            if (s.getId() == id) {
                students.remove(i);
                found = true;
                break;
            }
        }
        if (!found) {
            System.out.println("該当のIDの生徒が存在しません");
        } else {
            repo.saveAll(students);
        }
    }
}

class StudentManagementSystem {
    public static void main(String[] args) {
        StudentService service = new StudentService();
        List<Student> students = service.getAllStudents();

        for (Student s : students) {
            System.out.println(s);
        }

        Charset consoleCharset = System.console() != null ? System.console().charset() : StandardCharsets.UTF_8;
        Scanner sc = new Scanner(System.in, consoleCharset);
        while (true) {
            System.out.println("1:登録 2:更新 3:一覧 4:削除 0:終了");
            int choice = sc.nextInt();
            sc.nextLine();

            if (choice == 1) {
                // add
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

                service.addStudent(name, score);

                for (Student s : students) {
                    System.out.println(s);
                }

            } else if (choice == 2) {
                // update
                System.out.println("更新するIDを入力してください");
                int id = sc.nextInt();
                sc.nextLine();
                System.out.println("新しい名前");
                String name = sc.nextLine();

                System.out.println("新しいスコア");
                int score = sc.nextInt();
                sc.nextLine();

                service.updateStudent(id, name, score);

                for (Student s : students) {
                    System.out.println(s);
                }

            } else if (choice == 3) {
                // 一覧表示

                for (Student s : students) {
                    System.out.println(s);
                }

            } else if (choice == 4) {
                // 削除
                System.out.println("削除するIDを入力してください");
                int id = sc.nextInt();
                sc.nextLine();
                service.deleteStudent(id);

                for (Student s : students) {
                    System.out.println(s);
                }

            } else if (choice == 0) {
                break;
            }

        }
        sc.close();
    }
}