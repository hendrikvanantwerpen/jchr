package jchr.example;

import java.util.Collection;

import jchr.example.gcd.GcdConstraint;
import jchr.example.gcd.GcdHandler;

public class Gcd {

    public static void main(String[] args) throws Exception {
        if (args.length != 2) printUsage();
        else try {
            final long i0 = Long.parseLong(args[0]),
                       i1 = Long.parseLong(args[1]);
            
            if (i0 < 0 || i1 < 0) {                
                printUsage();
                return;
            }
            
            // First we create a new JCHR constraint handler:
            GcdHandler handler = new GcdHandler();
            
            // Next we tell the JCHR handler the following two constraints:             
            handler.tellGcd(i0);
            handler.tellGcd(i1);
            
            // Afterwards we can lookup the constraints in the 
            // resulting constraint store:
            final Collection<GcdConstraint> gcds = handler.getGcdConstraints();
            final long gcd;
            
            // This should be exactly one constraint, containing
            // the greatest common divider:
            //              (the exceptions should never occur)
            if (gcds.size() != 1)
                throw new RuntimeException(gcds.size() + " GcdConstraints in the store?!");
            else
                gcd = gcds.iterator().next().get$X0();
            
            // Simply print out the result:
            System.out.println(" ==>  gcd(" + i0 + ", " + i1 + ") == " + gcd);
            
        } catch (NumberFormatException e) {
            System.err.println(e.getMessage());
            printUsage();
        }
    }
    
    public final static void printUsage() {
        System.out.println("Usage: java examples.gcd.Main <positive int> <positive int>");
    }
}
