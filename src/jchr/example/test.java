package jchr.example;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

import javax.swing.JFrame;

import bsh.Interpreter;
import bsh.util.JConsole;


public class test {

    public static void main(String[] args) throws Exception {
    	JFrame f = new JFrame();
    	JConsole console = new JConsole();
    	f.getContentPane().add( console );
    	f.setSize( 500, 300 );
    	Interpreter shell = new Interpreter(console);
    	f.setTitle( "JCHR -- Java Contraint Handling Rules" );
    	
    	shell.eval( "importCommands( \"jchr/example/cmds\");" );
    	InputStream is = test.class.getResource("test.bsh").openStream();
    	Reader r = new BufferedReader(new InputStreamReader(is));
    	shell.eval(r);
    	
    	f.setVisible(true);
    	f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    	new Thread(shell).run();
    }
    
}
