package be.kuleuven.jchr.compiler.CHRIntermediateForm.matching;

import static be.kuleuven.jchr.util.comparing.Comparison.EQUAL;

import java.util.Set;

import be.kuleuven.jchr.compiler.CHRIntermediateForm.arg.argumentable.IArgumentable;
import be.kuleuven.jchr.compiler.CHRIntermediateForm.arg.argumented.IArgumented;
import be.kuleuven.jchr.compiler.CHRIntermediateForm.arg.arguments.IArguments;
import be.kuleuven.jchr.compiler.CHRIntermediateForm.exceptions.AmbiguousArgumentsException;
import be.kuleuven.jchr.compiler.CHRIntermediateForm.exceptions.IllegalArgumentsException;
import be.kuleuven.jchr.util.comparing.Comparison;



public abstract class Matching {

    private Matching() {/* not instantiatable */}

    public static <T extends IArgumentable<?>> IArgumented<?> getBestMatch(
            Set<T> argumentables, IArguments arguments)
            throws AmbiguousArgumentsException, IllegalArgumentsException {
        
        MatchingInfos matchingInfos, bestInfos = MatchingInfos.NO_MATCH;
        T best = null;
        Comparison comparison = EQUAL;
        boolean ambiguous = false;

        for (T argumentable : argumentables) {
            matchingInfos = argumentable.canHaveAsArguments(arguments);
            comparison = matchingInfos.compareTo(bestInfos);

            switch (comparison) {
            case BETTER:
                best = argumentable;
                bestInfos = matchingInfos;
                break;

            case AMBIGUOUS:
                ambiguous = true;
                break;

            case EQUAL:
                if (bestInfos.isNonAmbiguousMatch())
                    switch (argumentable.compareTo(best)) {
                    case BETTER:
                        best = argumentable;
                        bestInfos = matchingInfos;
                        break;

                    case AMBIGUOUS:
                    case EQUAL:
                        if (bestInfos.isExactMatch())
                            throw new AmbiguousArgumentsException(
                                    argumentables, arguments);
                        else
                            ambiguous = true;
                        break;
                    }
                break;
            }
        }

        if ((ambiguous && !bestInfos.isExactMatch()) || bestInfos.isAmbiguous())
            throw new AmbiguousArgumentsException(argumentables, arguments);
        if (!bestInfos.isNonAmbiguousMatch())
            throw new IllegalArgumentsException(argumentables, arguments);

        return best.getInstance(arguments, bestInfos);
    }

}
