package main.java;

import java.io.IOException;

public class Startup {

    public static void main(String[] args) {

        /*//todo stub
        try {
            TlvJsonParser parser = new TlvJsonParser("data-1.bin", "test.json");
            parser.parse();
        } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
        } catch (IOException e) {
            e.printStackTrace();
        }*/


        if(args.length == 2) {
            try {
                TlvJsonParser parser = new TlvJsonParser(args[0], args[1]);
                parser.parse();
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("Arguments must contains 2 files names");
        }
    }

}
