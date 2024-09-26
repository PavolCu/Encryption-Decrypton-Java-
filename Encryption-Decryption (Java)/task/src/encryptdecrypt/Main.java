
package encryptdecrypt;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Main {
    public static void main(String[] args) {
        String mode = "enc";
        int key = 0;
        String data = "";
        String inFile = "";
        String outFile = "";
        String alg = "shift";

        for (int i = 0; i < args.length; i++) {
            switch (args[i]) {
                case "-mode":
                    mode = args[i + 1];
                    break;
                case "-key":
                    key = Integer.parseInt(args[i + 1]);
                    break;
                case "-data":
                    data = args[i + 1];
                    break;
                case "-in":
                    inFile = args[i + 1];
                    break;
                case "-out":
                    outFile = args[i + 1];
                    break;
                case "-alg":
                    alg = args[i + 1];
                    break;
            }
        }

        if (data.isEmpty() && !inFile.isEmpty()) {
            try {
                data = new String(Files.readAllBytes(Paths.get(inFile)));
            } catch (IOException e) {
                System.out.println("Error: Unable to read input file");
                return;
            }
        }

        String result;
        switch (mode) {
            case "enc":
                result = encryptMessage(data, key, alg);
                break;
            case "dec":
                result = decryptMessage(data, key, alg);
                break;
            default:
                System.out.println("Invalid operation");
                return;
        }

        if (outFile.isEmpty()) {
            System.out.println(result);
        } else {
            try {
                Files.write(Paths.get(outFile), result.getBytes());
            } catch (IOException e) {
                System.out.println("Error: Unable to write output file");
            }
        }
    }

    private static String encryptMessage(String message, int key, String alg) {
        if ("unicode".equals(alg)) {
            return unicodeEncrypt(message, key);
        } else {
            return shiftEncrypt(message, key);
        }
    }

    private static String decryptMessage(String message, int key, String alg) {
        if ("unicode".equals(alg)) {
            return unicodeDecrypt(message, key);
        } else {
            return shiftDecrypt(message, key);
        }
    }

    private static String unicodeEncrypt(String message, int key) {
        StringBuilder encrypted = new StringBuilder();
        for (char c : message.toCharArray()) {
            encrypted.append((char) (c + key));
        }
        return encrypted.toString();
    }

    private static String unicodeDecrypt(String message, int key) {
        StringBuilder decrypted = new StringBuilder();
        for (char c : message.toCharArray()) {
            decrypted.append((char) (c - key));
        }
        return decrypted.toString();
    }

    private static String shiftEncrypt(String message, int key) {
        StringBuilder encrypted = new StringBuilder();
        for (char c : message.toCharArray()) {
            if (Character.isLetter(c)) {
                char base = Character.isLowerCase(c) ? 'a' : 'A';
                encrypted.append((char) ((c - base + key) % 26 + base));
            } else {
                encrypted.append(c);
            }
        }
        return encrypted.toString();
    }

    private static String shiftDecrypt(String message, int key) {
        StringBuilder decrypted = new StringBuilder();
        for (char c : message.toCharArray()) {
            if (Character.isLetter(c)) {
                char base = Character.isLowerCase(c) ? 'a' : 'A';
                decrypted.append((char) ((c - base - key + 26) % 26 + base));
            } else {
                decrypted.append(c);
            }
        }
        return decrypted.toString();
    }
}