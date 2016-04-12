package be.kuleuven.jchr.util.writers;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.OutputStream;
import java.io.PrintWriter;

/**
 * @author Peter Van Weert
 */
public class SimpleTabbedWriter extends PrintWriter {
    private int nbTabs;

    public SimpleTabbedWriter(File file) throws FileNotFoundException {
        super(file);
    }
    
    public SimpleTabbedWriter(OutputStream out) {
        super(out, true);
    }
    
    @Override
    public void print(boolean b) {
        printTabs();
        super.print(b);
    }
    @Override
    public void print(char c) {
        printTabs();
        super.print(c);
    }
    @Override
    public void print(char[] s) {
        printTabs();
        super.print(s);
    }
    @Override
    public void print(double d) {
        printTabs();
        super.print(d);
    }
    @Override
    public void print(float f) {
        printTabs();
        super.print(f);
    }
    @Override
    public void print(int i) {
        printTabs();
        super.print(i);
    }
    @Override
    public void print(long l) {
        printTabs();
        super.print(l);
    }
    @Override
    public void print(Object obj) {
        printTabs();
        super.print(obj);
    }
    @Override
    public void print(String s) {
        printTabs();
        super.print(s);
    }
    
    public void printTabs() {
        for (int i = 0; i < getNbTabs(); i++)
            write("    ");
    }
    
    public int getNbTabs() {
        return nbTabs;
    }
    public void setNbTabs(int nbTabs) {
        this.nbTabs = nbTabs;
    }
    public void incNbTabs() {
        nbTabs++;
    }
    public void decNbTabs() {
        nbTabs--;
    }
    public void incNbTabs(int nbTabs) {
        this.nbTabs += nbTabs;
    }
    public void decNbTabs(int nbTabs) {
        this.nbTabs -= nbTabs;
    }
}