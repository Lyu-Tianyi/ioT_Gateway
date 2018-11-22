package Module7;

import java.util.logging.Logger;
import org.eclipse.californium.core.CoapResource;
import org.eclipse.californium.core.CoapServer;

public class CoapServerConnector {
	
	// static
	
	private static final Logger _Logger = Logger.getLogger(CoapServerConnector.class.getName());
	
	// private var's
	
	private CoapServer _coapServer;
	
	// constructors
	/**
	* Default.
	*
	*/
	
	public CoapServerConnector()
	{
		super();
	}
	
	// public methods
	
	public void addResource(CoapResource resource)
	{
		if (resource != null) {
			_coapServer.add(resource);
		}
	}
	public void start()
	{
		if (_coapServer == null) {
			_Logger.info("Creating CoAP server instance and 'temp' handler...");
			_coapServer = new CoapServer();
			// NOTE: you must implement TestResourceHandler yourself
			TempResourceHandler tempHandler = new TempResourceHandler();
			_coapServer.add(tempHandler);
		}
		_Logger.info("Starting CoAP server...");
		_coapServer.start();
	}
	public void stop()
	{
		_Logger.info("Stopping CoAP server...");
		_coapServer.stop();
	}
}
