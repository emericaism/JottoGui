package model;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Random;


public class JottoModel {
    
    //puzzleNumber does have a limit. server takes modulo of whatever 
    //number you enter by 
    //amount of available puzzles (after multiplying it with a constant)
    //we will arbitrarily apply 2,000,000
    private static final int MAX_PUZZ_NUM = 2000000;
    private String puzzNum;
    
    public JottoModel(){
        Random numba = new Random();
        this.puzzNum = numba.nextInt(MAX_PUZZ_NUM)+ "";
        //The case for a random puzzleNumber, 
        //which needs to ultimately be a string.
    }
    
    /*
     * Instantiates a JottoModel that has puzzle Number puzzNum.
     * @param puzzNum
     */
    public JottoModel(String puzzNum){
        this.puzzNum = puzzNum;
    }
    
    /*
     * Get JottoModel puzzNum.
     * @return puzzNum of puzzle that is being played.
     */
    public String getPuzzNum(){
        return this.puzzNum;
    }
    
    
    /*
     * Set JottoModel puzzNum.
     * @param puzzNum of puzzle that is to be played.
     */
    public void setPuzzNum(String puzzNum){
        this.puzzNum = puzzNum;
    }
    

	/**
	 * This method guesses the string: guess. Then it calls to the server
	 * with the puzzleNumber of the game, and the guess. It will
	 * return the server response.
	 * 
	 * @param String guess
	 * @return String[] server response.
	 * @throws IOException
	 */
	public String[] makeGuess(String guess) throws IOException {
		String msg = "jotto.py?puzzle=" + puzzNum + "&guess=" + guess.toLowerCase();
		URL myURL = new URL ("http://courses.csail.mit.edu/6.005/" + msg);
		
		//We need to parse the information received by the server, so we
		//send it to the parse method.
		String response = parse(myURL, guess);
		String[] response_t = new String[2];
		
		if (response.startsWith("guess")){
		    response_t[0] = response.substring(6,7);
		    //Gives us the number of letters that the guess and the secret
		    //word have in common.
		    
		    //guess 3 1
		    
		    response_t[1] = response.substring(8);
		    //Gives us the number of letters that the guess has in the
		    //exact right position
		}
		
		else if (response.startsWith("error")){
		  //error 2: Invalid guess.
		    response_t[0] = response.substring(9);
		    response_t[1] = "";
		}
		
		else{
		    response_t[0] = response;
		    response_t[1] = "";
		}
		
		return response_t;
	    
	}
	
	/**
	 * Parser will take the message from the server
	 * and parse it into readable information. It will return wither
	 * the number of correct letters to the user
	 * or it will return a message indicating what kind of error
	 * occurred.
	 * 
	 * @param url, the URL of the server for this puzzle
	 * @return happen, a string that tells you what happened
	 * @throws IOException
	 */
	public String parse(URL myURL, String guess) throws IOException {
	    BufferedReader in = new BufferedReader(new InputStreamReader(myURL.openStream()));
	    String curr_line;
	    //inputs
	    String error0 = "error 0: Ill-formatted request.";
	    String error1 = "error 1: Non-number puzzle ID.";
	    String error2 = "error 2: Invalid guess. Length of guess != 5 or guess is not a dictionary word.";
	    String winString = "guess 5 5";
	    
	    
	    //results
	    String happen = "this shouldn't appear";
	    String r_error0 = "Ill-formatted request.";
	    String r_error1 = "Non-number puzzle ID.";
	    String r_error2 = "Invalid guess.";
	    String r_winString = "You win! The secret word was " + guess.toLowerCase() + ". Nice job!";
	    
	    
	    while ((curr_line = in.readLine()) != null){
	        if (curr_line.equals(error0)){
	            happen = r_error0;
	        }
	        else if (curr_line.equals(error1)){
	            happen = r_error1;
	            
	        }
	        else if (curr_line.equals(error2)){
	            happen = r_error2;
	        }
	        else if (curr_line.equals(winString)){
	            happen = r_winString;
	            
	        }
	        else{
	            happen = curr_line;
	        }
	    }
	    return happen;
	}
	
}