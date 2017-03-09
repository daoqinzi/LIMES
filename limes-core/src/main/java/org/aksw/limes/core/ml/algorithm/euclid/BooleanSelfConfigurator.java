package org.aksw.limes.core.ml.algorithm.euclid;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.aksw.limes.core.io.cache.ACache;
import org.aksw.limes.core.io.mapping.AMapping;
import org.aksw.limes.core.io.mapping.MappingFactory;
import org.aksw.limes.core.ml.algorithm.classifier.ComplexClassifier;
import org.aksw.limes.core.ml.algorithm.classifier.SimpleClassifier;

/**
 * @author Mohamed Sherif (sherif@informatik.uni-leipzig.de)
 *
 */
public class BooleanSelfConfigurator extends LinearSelfConfigurator {

    /** Constructor
     * @param source Source cache
     * @param target Target cache
     */
	public BooleanSelfConfigurator (ACache source, ACache target) {
        super(source, target);
    }

    /** Constructor
     *
     * @param source Source cache
     * @param target Target cache
     * @param beta Beta value for computing F_beta
     * @param minCoverage Minimal coverage for a property to be considered for linking
     *
     */
    public BooleanSelfConfigurator (ACache source, ACache target, double minCoverage, double beta) {
        super(source, target, minCoverage, beta);
    }
    
    /**
     * Constructor
     *
     * @param source Source cache
     * @param target Target cache
     * @param minCoverage Minimal coverage for a property to be considered for linking
     * @param beta Beta value for computing F_beta
     * @param measures Atomic measures
     */
    public BooleanSelfConfigurator(ACache source, ACache target, double minCoverage, double beta, Map<String, String> measures) {
        super(source, target, minCoverage, beta, measures);
    }



    /** Aims to improve upon a particular classifier by checking whether adding a delta
     * to its similarity worsens the total classifer
     * @return Improved classifiers and their mapping
     */
    public ComplexClassifier computeNext(ComplexClassifier classifier, int index) {
        ComplexClassifier cc = classifier.clone();
        ComplexClassifier result = new ComplexClassifier(null, 0.0);
        if (cc.getClassifiers().get(index).getThreshold() > learningRate) { //
            cc.getClassifiers().get(index).setThreshold(cc.getClassifiers().get(index).getThreshold() - learningRate);
            AMapping m = getMapping(cc.getClassifiers());
            result.setClassifiers(cc.getClassifiers());
            result.setfMeasure(computeQuality(m));
//            result.fMeasure = _measure.getPseudoFMeasure(source.getAllUris(), target.getAllUris(), m, beta);
            return result;
        } else {
            if (cc.getClassifiers().size() > index + 1) {
                List<SimpleClassifier> cp = new ArrayList<SimpleClassifier>();
                for (int i = 0; i < cc.getClassifiers().size(); i++) {
                    if (i != index) {
                        cp.add(cc.getClassifiers().get(i));
                    }
                }
                AMapping m = getMapping(cp);
                result.setClassifiers(cp);
                result.setfMeasure(computeQuality(m));
//                result.fMeasure = _measure.getPseudoFMeasure(source.getAllUris(), target.getAllUris(), m, beta);
                return result;
            } else {
                return result;
            }
        }
    }

    /** Runs classifiers and retrieves the correspoding mappings
     *
     * @param classifiers List of classifiers
     * @return AMapping generated by the list of classifiers
     */
    @Override
    public AMapping getMapping(List<SimpleClassifier> classifiers) {
        List<AMapping> mappings = new ArrayList<AMapping>();
        for (int i = 0; i < classifiers.size(); i++) {
            AMapping m = executeClassifier(classifiers.get(i), classifiers.get(i).getThreshold());
            mappings.add(m);
        }
        AMapping result = getIntersection(mappings);
        //System.out.println("***" + classifiers + "\n" + mappings + " => " + result + "***");
        return result;
    }

    /** Computes the intersection of several mappings
     *
     * @param mappings Maps classifiers to the resulting mappings
     * @return Intersection of the mappings
     */
    public static AMapping getIntersection(List<AMapping> mappings) {
        if (mappings.isEmpty()) {
            return MappingFactory.createDefaultMapping();
        }
        AMapping reference = mappings.get(0);
        AMapping result = MappingFactory.createDefaultMapping();
        for (String s : reference.getMap().keySet()) {
            for (String t : reference.getMap().get(s).keySet()) {
                boolean maps = true;
                for (int i = 1; i < mappings.size(); i++) {
                    AMapping m = mappings.get(i);
                    if (!m.contains(s, t)) {
                        maps = false;
                    }
                }
                if (maps) {
                    result.add(s, t, 1.0);
                }
            }
        }
        return result;
    }

    public List<SimpleClassifier> learnClassifer(List<SimpleClassifier> classifiers) {
        classifiers = normalizeClassifiers(classifiers);
        AMapping m = getMapping(classifiers);
        double f = computeQuality(m);
//        double f = _measure.getPseudoFMeasure(source.getAllUris(), target.getAllUris(), m, beta);
        // no need to update if the classifiers are already perfect
        if (f == 1.0) {
            return classifiers;
        }
        ComplexClassifier classifier = new ComplexClassifier(classifiers, f);
        ComplexClassifier bestClassifier = null;
        ComplexClassifier bestGlobalClassifier = classifier.clone();
        double bestF = 0;
        double globalBestF = f;
        int direction = 0;
        int iterations = 0;

        ComplexClassifier cc;
        while (iterations <= ITERATIONS_MAX) {
            iterations++;
//            double fMeasure;
//            int index = -1;
            bestF = 0;
            //evaluate neighbors of current classifier
            for (int i = 0; i < classifier.getClassifiers().size(); i++) {
                cc = computeNext(classifier, i);
                if (cc.getfMeasure() > bestF) {
                    bestF = cc.getfMeasure();
//                    index = i;
                    bestClassifier = cc;
                }
            }

            //every neighboring classifier is worse
            if (bestF < globalBestF) {
                return bestGlobalClassifier.getClassifiers();
            } //nothing better found. simply march in the space in direction
            //"direction"
            else if (bestF == globalBestF) {
//                logger.info(">>>> Walking along direction " + direction);
                if (direction >= classifier.getClassifiers().size()) {
                    direction = 0;
                }
                bestClassifier = computeNext(classifier, direction);
                direction++;
                direction = direction % (classifier.getClassifiers().size());
            } //found a better classifier
            bestGlobalClassifier = bestClassifier.clone();
            globalBestF = bestF;
            //init for next iter
            classifier = bestClassifier;
//            logger.info(">> Iteration " + iterations + ": " + classifier.getClassifiers() + " F-Measure = " + globalBestF + " AMapping = " + getMapping(classifiers));
        }
        return bestGlobalClassifier.getClassifiers();
    }
}
