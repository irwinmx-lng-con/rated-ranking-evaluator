package io.sease.rre.core.domain.metrics.impl;

/**
 * The most popular F-Measure, which balances between precisin and recall using 1 as beta factor.
 *
 * @author worleydl 
 * @since 1.1
 */
public class F1AtOne extends FMeasureAtK {
    /**
     * Builds a new F1 metric instance.
     */
    public F1AtOne() {
        super("F1@1", 1, 1);
    }
}