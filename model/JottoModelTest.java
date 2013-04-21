package model;

import static org.junit.Assert.*;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.junit.Test;

public class JottoModelTest{
    
    @Test
    public void test1() throws IOException {
        //Tests when invalid guess whose length is too long.
        String[] e = new String[2];
        e[0] =  "Invalid guess.";
        e[1] = "";
        JottoModel jottomod = new JottoModel("16952");
        String[] observed = jottomod.makeGuess("sedikit");
        assertArrayEquals(e, observed);   
    }
    
    @Test
    public void test2() throws IOException {
        //Test when inputted puzzle number is bad. Such as a word instead of number.
        String[] e = new String[2];
        e[0] =  "Non-number puzzle ID.";
        e[1] = "";
        JottoModel jmod = new JottoModel("BillGates");
        String[] observed = jmod.makeGuess("stack");
        assertArrayEquals(e, observed);   
    }
    
    @Test
    public void test3() throws IOException {
        //Tests when we have no constructor in jottomod and bad input
        String[] e = new String[2];
        e[0] =  "Invalid guess.";
        e[1] = "";
        JottoModel jmod = new JottoModel();
        String[] observed = jmod.makeGuess("Wayne Brady");
        assertArrayEquals(e, observed);   
    }
    
    @Test
    public void test4() throws IOException {
        //tests when we have empty string
        String[] e = new String[2];
        e[0] =  "Ill-formatted request.";
        e[1] = "";
        JottoModel jmod = new JottoModel();
        String[] observed = jmod.makeGuess("");
        assertArrayEquals(e, observed);   
    }
    
    @Test
    public void test5() throws IOException {
        //tests a valid guess.
        String[] e = new String[2];
        e[0] =  "3";
        e[1] = "3";
        JottoModel jmod = new JottoModel("16952");
        String[] observed = jmod.makeGuess("carts");
        assertArrayEquals(e, observed);   
    }
    
    @Test
    public void test6() throws IOException {
        //tests good input with winning word: "cargo"
        String[] e = new String[2];
        e[0] =  "You win! The secret word was cargo. Nice job!";
        e[1] = "";
        JottoModel jmod = new JottoModel("16952");
        String[] observed = jmod.makeGuess("cargo");
        assertArrayEquals(e, observed);   
    }
    
    @Test
    public void test7() throws IOException {
        //tests good input with asterisks
        String[] e = new String[2];
        e[0] = "0";
        e[1] = "0";
        JottoModel jmod = new JottoModel("16952");
        String[] observed = jmod.makeGuess("*****");
        assertArrayEquals(e, observed); 
    }
    
    @Test
    public void test8() throws IOException {
        //tests a negative puzzleNumber
        //We expect it to instead, generate a random PuzzleNumber
        String[] e = new String[2];
        e[0] = "0";
        e[1] = "0"; //not necessarily true, just want numbers
        JottoModel jmod = new JottoModel("-45");
        String[] observed = jmod.makeGuess("zebra");
        System.out.println(observed);
        assertArrayEquals(e,observed);
        
    }
    
}
