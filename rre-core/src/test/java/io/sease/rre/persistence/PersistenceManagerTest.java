package io.sease.rre.persistence;

import io.sease.rre.core.domain.Query;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ConcurrentModificationException;
import java.util.Map;

import static org.junit.Assert.fail;

/**
 * Unit tests for the PersistenceManager class.
 *
 * @author Matt Pearce (matt@flax.co.uk)
 */
public class PersistenceManagerTest {

    private PersistenceManager persistenceManager;

    @Before
    public void setupManager() {
        this.persistenceManager = new PersistenceManager();
    }

    @After
    public void tearDownManager() {
        this.persistenceManager = null;
    }

    @Test
    public void managerThrowsRuntimeException_whenAllHandlersFailBeforeStart() {
        try {
            persistenceManager.registerHandler(failingHandler);
            persistenceManager.beforeStart();
        } catch (ConcurrentModificationException e) {
            fail("Unexpected ConcurrentModificationException: " + e.getMessage());
        } catch (RuntimeException e) {
            // Expected
        }
    }

    @Test
    public void managerThrowsRuntimeException_whenAllHandlersFailStart() {
        try {
            persistenceManager.registerHandler(failingHandler);
            persistenceManager.start();
        } catch (ConcurrentModificationException e) {
            fail("Unexpected ConcurrentModificationException: " + e.getMessage());
        } catch (RuntimeException e) {
            // Expected
        }
    }

    private PersistenceHandler failingHandler = new PersistenceHandler() {
        @Override
        public void configure(String name, Map<String, Object> configuration) { }

        @Override
        public String getName() {
            return "failingHandler";
        }

        @Override
        public void beforeStart() throws PersistenceException {
            throw new PersistenceException();
        }

        @Override
        public void start() throws PersistenceException {
            throw new PersistenceException();
        }

        @Override
        public void recordQuery(Query q) { }

        @Override
        public void beforeStop() { }

        @Override
        public void stop() { }
    };



}
