import java.io.File;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) throws Exception {
        do {
            Scanner scanner = new Scanner(System.in);
            boolean badInput;

            String targetIn;
            do {
                System.out.println("Enter Target Path: ");
                targetIn = scanner.nextLine();
                if(!new File(targetIn).exists()){
                    badInput=true;
                    System.out.println("Path Not Exists.");
                }else{
                    badInput=false;
                }
            }while (badInput);

            boolean isRegex = false;
            do {
                System.out.println("Do you want to enter regex instead of target keyword? (y or n)");
                try{
                    isRegex = yesOrNo(scanner.nextLine());
                    badInput=false;
                }catch (Exception e){
                    badInput=true;
                }
            }while (badInput);
            System.out.println("Enter Target Keyword: ");
            String targetKeyword = scanner.nextLine();

            System.out.println("Enter Replacement Keyword: ");
            String replacementKeyword = scanner.nextLine();

            NameFindAndReplace nameFindAndReplace = new NameFindAndReplace(new File(targetIn),
                    targetKeyword, replacementKeyword, isRegex);

            do {
                System.out.println("Start? (y or n)");
                String input = scanner.nextLine();
                if (input.matches("n")){
                    return;
                }else badInput = !input.matches("y");
            }while (badInput);

            nameFindAndReplace.replaceKeyword();
            System.out.println("Name Replaced.");

            do {
                System.out.println("Rollback? (y or n)");
                String input = scanner.nextLine();
                if (input.matches("y")){
                    nameFindAndReplace.rollbackKeyword();
                }else badInput = !input.matches("n");
            }while (badInput);

            do {
                System.out.println("Exit? (y or n)");
                String input = scanner.nextLine();
                if (input.matches("y")){
                    return;
                }else badInput = !input.matches("n");
            }while (badInput);
        }while (true);
    }

    private static boolean yesOrNo(String input) throws Exception {
        if(input.matches("y")){
            return true;
        }else if(input.matches("n")){
            return false;
        }else{
            System.out.println("Wrong Input.");
            throw new Exception();
        }
    }
}
