package ex00;

import ex01.A;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("=== Archiwizer ===");
        System.out.println("1. Archiwizacja katalogu");
        System.out.println("2. Weryfikacja archiwum");
        System.out.print("Wybierz opcję: ");
        int choice = scanner.nextInt();
        scanner.nextLine();

        if (choice == 1) {
            System.out.print("Podaj katalog do archiwizacji: ");
            String source = scanner.nextLine();
            System.out.print("Podaj nazwę archiwum (z .zip): ");
            String archive = scanner.nextLine();

            try {
                A.createArchive(source, archive);
                String md5File = archive + ".md5";
                A.saveMD5(archive, md5File);
                System.out.println("Archiwum zapisane: " + archive);
                System.out.println("MD5 zapisane: " + md5File);
            } catch (Exception e) {
                System.err.println("Błąd: " + e.getMessage());
            }
        } else if (choice == 2) {
            System.out.print("Podaj archiwum do weryfikacji: ");
            String archive = scanner.nextLine();
            String md5File = archive + ".md5";

            try {
                if (A.verifyMD5(archive, md5File)) {
                    System.out.println("Weryfikacja zakończona sukcesem!");
                } else {
                    System.out.println("Nieprawidłowa suma kontrolna!");
                }
            } catch (Exception e) {
                System.err.println("Błąd: " + e.getMessage());
            }
        } else {
            System.out.println("Nieprawidłowy wybór.");
        }
    }
}