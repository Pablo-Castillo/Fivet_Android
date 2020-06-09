package cl.ucn.disc.pdis.fivet;

import cl.ucn.disc.pdis.fivet.zeroice.model.Contratos;
import cl.ucn.disc.pdis.fivet.zeroice.model.ContratosPrx;
import com.zeroc.Ice.Communicator;
import com.zeroc.Ice.InitializationData;
import com.zeroc.Ice.ObjectPrx;
import com.zeroc.Ice.Properties;
import com.zeroc.Ice.Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SuppressWarnings("Singleton")
public final class ZeroIce {
    /**
     * The Logger
     */
    private static final Logger log = LoggerFactory.getLogger(ZeroIce.class);
    /**
     * The Singleton
     */
    private static final ZeroIce ZERO_ICE = new ZeroIce();
    /**
     * The communicator
     */
    private Communicator theCommunicator;
    /**
     * Contracts Implementation
     */
    private ContratosPrx theContratos;

    /**
     *  The Constructor
     */
    private ZeroIce() {
        // Nothing here
    }

    /**
     * @return the ZeroIce
     */
    public static ZeroIce getInstance() {
        return ZERO_ICE;
    }

    private static InitializationData getInitializationData(String[] args) {

        final Properties properties = Util.createProperties(args);
        properties.setProperty("Ice.Package.model", "cl.ucn.disc.pdis.fivet.zeroice");

        InitializationData initializationData = new InitializationData();
        initializationData.properties = properties;

        return initializationData;
    }

    /**
     * @return the Contratos
     */
    public ContratosPrx getContratos() {
        return this.theContratos;
    }

    /**
     *  Start the communications
     */
    public void start() {

        if (this.theCommunicator != null) {
            log.warn("The Communicator was already initialized?");
            return;
        }

        this.theCommunicator = Util.initialize(getInitializationData(new String[1]));

        String name = Contratos.class.getSimpleName();
        log.debug("Proxying <{}> ..", name);

        ObjectPrx theProxy = this.theCommunicator.stringToProxy(name + ":tcp -z -t 15000 -p 8080");

        this.theContratos = ContratosPrx.checkedCast(theProxy);
    }

    /**
     * Stop the communications
     */
    public void stop() {
        if (this.theCommunicator == null) {
            log.warn("The Communicator was already stopped?");
            return;
        }

        this.theContratos = null;
        this.theCommunicator.destroy();
    }
}
