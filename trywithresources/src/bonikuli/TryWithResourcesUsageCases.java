package bonikuli;

import java.io.Closeable;
import java.io.IOException;

public class TryWithResourcesUsageCases {
    
    public static void main(String[] args) {
        System.out.println("#1");
        execute(ResourceMock.ScenarioType.COMPLETE, ResourceMock.ScenarioType.COMPLETE);
        System.out.println("#2");
        execute(ResourceMock.ScenarioType.THROW_ON_ACTION, ResourceMock.ScenarioType.COMPLETE);
        System.out.println("#3");
        execute(ResourceMock.ScenarioType.THROW_ON_ACTION, ResourceMock.ScenarioType.THROW_ON_CLOSE);
        System.out.println("#4");
        execute(ResourceMock.ScenarioType.THROW_ON_CLOSE, ResourceMock.ScenarioType.THROW_ON_CLOSE);
    }
    
    private static void execute(ResourceMock.ScenarioType firstScenario, ResourceMock.ScenarioType secondScenario) {
        try (ResourceMock resource1 = new ResourceMock(firstScenario, 1); ResourceMock resource2 = new ResourceMock(secondScenario, 2)) {
            resource2.doAction();
            resource1.doAction();
        } catch (IOException|ExceptionMock e) {
            System.out.println(e.getMessage());
            if (e.getSuppressed() != null && e.getSuppressed().length > 0) {
                for (Throwable suppressedException: e.getSuppressed()) {
                    System.out.println("Suppressed: " + suppressedException.getMessage());
                }
            }
        }
    }
    
    private static class ResourceMock implements Closeable {
        
        private boolean closed;
        
        private enum ScenarioType {
            COMPLETE, THROW_ON_CLOSE, THROW_ON_ACTION
        }
        
        private final ScenarioType scenario;
        private final int resourceId;
        
        public ResourceMock(ScenarioType scenario, int resourceId) {
            this.scenario = scenario;
            this.resourceId = resourceId;
        }
        
        public void doAction() throws ExceptionMock {
            switch (scenario) {
                case THROW_ON_ACTION:
                    throw new ExceptionMock("Exception happened at a time of action " + resourceId);
                default:
                    System.out.println("Do action on resource " + resourceId);
            }
        }

        @Override
        public void close() throws IOException {
            switch (scenario) {
                case THROW_ON_CLOSE:
                    throw new IOException("Cannot close resource " + resourceId);
                default:
                    System.out.println("Close resource " + resourceId);
            }
            closed = true;
        }
        
        public boolean isClosed() {
            return closed;
        }
    }
    
    private static class ExceptionMock extends Exception {
        public ExceptionMock(String message) {
            super(message);
        }
    }
    
}
