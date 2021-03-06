package io.sease.rre.core.domain.metrics.impl;

import io.sease.rre.core.domain.metrics.Metric;
import io.sease.rre.core.domain.metrics.ValueFactory;

import java.math.BigDecimal;
import java.util.Map;

import static io.sease.rre.Calculator.*;

/**
 * Precision and recall are single-value metrics based on the whole list of documents returned by the system.
 * For systems that return a ranked sequence of documents, it is desirable to also consider the order in which the
 * returned documents are presented. By computing a precision and recall at every position in the ranked sequence of
 * documents, one can plot a precision-recall curve, plotting precision.
 *
 * @author agazzarini
 * @since 1.0
 */
public class AveragePrecision extends Metric {
    /**
     * Builds a new {@link AveragePrecision} metric.
     */
    public AveragePrecision() {
        super("AP");
    }

    @Override
    public ValueFactory createValueFactory(final String version) {
        return new ValueFactory(this, version) {
            private BigDecimal relevantItemsFound = BigDecimal.ZERO;

            private BigDecimal howManyRelevantDocuments;

            private BigDecimal value = BigDecimal.ZERO;
            private BigDecimal lastCollectedRecallLevel = BigDecimal.ZERO;

            @Override
            public void collect(final Map<String, Object> hit, final int rank, String version) {
                if (howManyRelevantDocuments == null)
                    howManyRelevantDocuments = new BigDecimal(relevantDocuments.size());

                relevantItemsFound = sum(relevantItemsFound, judgment(id(hit)).isPresent() ? BigDecimal.ONE : BigDecimal.ZERO);

                final BigDecimal currentPrecision = divide(relevantItemsFound, new BigDecimal(rank));
                final BigDecimal currentRecall =
                        howManyRelevantDocuments.equals(BigDecimal.ZERO)
                                ? BigDecimal.ZERO
                                : divide(relevantItemsFound, howManyRelevantDocuments);
                value = sum(
                        value,
                        multiply(
                                currentPrecision,
                                subtract(currentRecall, lastCollectedRecallLevel)));

                lastCollectedRecallLevel = currentRecall;
            }

            @Override
            public BigDecimal value() {
                if (relevantDocuments.size() == 0) {
                    return totalHits == 0 ? BigDecimal.ONE : BigDecimal.ZERO;
                }
                return value;
            }
        };
    }
}
