import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        //Todo : Make a GUI of this stuff later

        /*
            @Author : Gideon Kane M.
            7th june 2021, This code is going to be the first
            version of a database maker for the nova project which wll
            soon be ported to all platforms. I will try to make this as generic as possible so i can port to other
            implementations and frameworks
         */
        //for now main will serve as the GUI

        setUpInterface();
        Scanner userInput = new Scanner(System.in);
        System.out.println("Please input the path of the text file of questions   >");
        String filePath = userInput.nextLine();

    }

    private static void setUpInterface() {

    }
}
