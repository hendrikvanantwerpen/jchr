package be.kuleuven.jchr.compiler.analysis.scheduling;

import be.kuleuven.jchr.compiler.CHRIntermediateForm.arg.argument.IArgument;
import be.kuleuven.jchr.compiler.CHRIntermediateForm.arg.arguments.Arguments;
import be.kuleuven.jchr.compiler.CHRIntermediateForm.arg.arguments.IArguments;
import be.kuleuven.jchr.compiler.CHRIntermediateForm.constraints.ud.Occurrence;
import be.kuleuven.jchr.compiler.CHRIntermediateForm.constraints.ud.lookup.type.ILookupType;

public class Lookup extends be.kuleuven.jchr.compiler.CHRIntermediateForm.constraints.ud.lookup.Lookup {
    
    private Occurrence occurrence;
    
    private ILookupType lookupType;
    
    private IArguments arguments;
    
    protected Lookup() {
        // NOP (just declaring the fact that it's to be protected)
    }
    
    public Lookup(Occurrence occurrence, ILookupType lookupType, int nbArguments) {
        setOccurrence(occurrence);
        setLookupType(lookupType);
        setNbArguments(nbArguments);
    }
    
    public Lookup(Occurrence occurrence, ILookupType lookupType, IArguments arguments) {
        setOccurrence(occurrence);
        setLookupType(lookupType);
        setArguments(arguments);
    }

    @Override
    public Occurrence getOccurrence() {
        return occurrence;
    }
    protected void setOccurrence(Occurrence occurrence) {
        this.occurrence = occurrence;
    }
    
    @Override
    public ILookupType getLookupType() {
        return lookupType;
    }
    protected void setLookupType(ILookupType lookupType) {
        this.lookupType = lookupType;
    }
    
    public IArguments getArguments() {
        return arguments;
    }
    protected void setArguments(IArguments arguments) {
        this.arguments = arguments;
    }
    protected void setNbArguments(int nbArguments) {
        setArguments(new Arguments(nbArguments));
    }
    public void addArgument(IArgument argument) {
        getArguments().addArgument(argument);
    }
    
    @Override
    public String toString() {
        return getLookupType().toString() + " lookup of " + getOccurrence();
    }    
}