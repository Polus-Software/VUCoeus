/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.mit.coeus.utils.birt;

import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.utils.CoeusProperties;
import edu.mit.coeus.utils.UtilFactory;
import java.net.URL;
import java.util.logging.Level;
import org.eclipse.birt.core.framework.Platform;
import org.eclipse.birt.report.engine.api.EngineConfig;
import org.eclipse.birt.report.engine.api.IReportEngine;
import org.eclipse.birt.report.engine.api.IReportEngineFactory;

/**
 *
 * @author sharathk
 */
class BirtInstance {

    private static BirtInstance birtInstance = null;
    private static IReportEngine engine = null; //JM 12-20-2011 added static
    private static EngineConfig config = null; // JM 12-20-2011 added static
    public static String BIRT_ROOT;

    private BirtInstance()throws CoeusException {
        init();
    }

    private static void init()throws CoeusException {
        try {
        	//System.out.println("Creating a new instance of the BIRT report engine ...");
            config = new EngineConfig();
            //URL url = getClass().getResource("/");
            URL url = Thread.currentThread().getContextClassLoader().getResource("/"); //JM static
            String coeusRoot = url.getPath().substring(0, url.getPath().indexOf("WEB-INF"));
            BIRT_ROOT = coeusRoot + CoeusProperties.getProperty("BIRT_HOME");
            config.setEngineHome(BIRT_ROOT);
            config.setLogConfig(null, Level.FINE);
            Platform.startup(config);
            IReportEngineFactory factory = (IReportEngineFactory) Platform.createFactoryObject(IReportEngineFactory.EXTENSION_REPORT_ENGINE_FACTORY);
            engine = factory.createReportEngine(config);
            engine.changeLogLevel(Level.WARNING);
        } catch (Exception exception) {
            UtilFactory.log(exception.getMessage(), exception, "BirtInstance", "init");
            CoeusException coeusException = new CoeusException(exception.getMessage());
            throw coeusException;
        }
    }

    private void shutdown() {
        //Destroy the engine and shutdown the Platform
        //Note - If the program stays resident do not shutdown the Platform or the Engine
        engine.destroy();
        Platform.shutdown();
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        shutdown();
    }

    public static synchronized BirtInstance getInstance()throws CoeusException {
        if (birtInstance == null) {
            birtInstance = new BirtInstance();
        }
        return birtInstance;
    }

    public IReportEngine getIReportEngine() {
        return engine;
    }

    public EngineConfig getEngineConfig() {
        return config;
    }
}
