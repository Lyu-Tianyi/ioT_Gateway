package Module7;

import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.eclipse.californium.core.CoapClient;
import org.eclipse.californium.core.CoapResponse;
import org.eclipse.californium.core.WebLink;
import org.eclipse.californium.core.coap.MediaTypeRegistry;

public class CoapClientConnector {
	
	//static
	
	private static final Logger _Logger = Logger.getLogger(CoapClientConnector.class.getName());
	
	// params
	
	private String _host;
	private String _protocol;
	private String _port;
	private String _serverAddr;
    
	private CoapClient _coapClient;
	private boolean _isInitialized;
	
	//constructors
	
	/**
	 * Default
	 * 
	 */
	public CoapClientConnector()
	 {
		this(null, null, null);
	 }
	
	/**
	 * Constructor.
	 *
	 * @param host
	 */
	public CoapClientConnector(String protocol, String host, String port) 
	{
		super();
		
		_protocol=protocol;
		_host=host;
		_port=port;
		 
//		if (isSecure) {
//			_protocol = ConfigConst.SECURE_COAP_PROTOCOL;
//			_port = ConfigConst.SECURE_COAP_PORT;
//		} else {
//			_protocol = ConfigConst.DEFAULT_COAP_PROTOCOL;
//			_port = ConfigConst.DEFAULT_COAP_PORT;
//		}
//		if (host != null && host.trim().length() > 0) {
//			_host = host;
//		} else {
//			_host = ConfigConst.DEFAULT_COAP_SERVER;
//		}
		 
		// NOTE: URL does not have a protocol handler for "coap",
		// so we need to construct the URL manually
		
		// _serverAddr = "coap://127.0.0.1:5683";
		_serverAddr = _protocol + "://" + _host + ":" + _port;
		_Logger.info("Using URL for server conn: " + _serverAddr);
	}
	
	// public methods
	
	public void runTests(String resourceName)
	{
		try {
			_isInitialized = false;
			initClient(resourceName);
			_Logger.info("Current URI: " + getCurrentUri());
			String payload = "Sample payload.";
			pingServer();
			discoverResources();
			sendGetRequest();
			sendGetRequest(true);
			sendPostRequest(payload, false);
			sendPostRequest(payload, true);
			sendPutRequest(payload, false);
			sendPutRequest(payload, true);
			sendDeleteRequest(true);
		} catch (Exception e) {
			_Logger.log(Level.SEVERE, "Failed to issue request to CoAP server.", e);
		}
	 }
	
	 /**
	  * Returns the CoAP client URI (if set, otherwise returns the _serverAddr, or
	  * null).
	  *
	  * @return String
	  */
	
	public String getCurrentUri()
	{
		return (_coapClient != null ? _coapClient.getURI() : _serverAddr);
	}
	
	public void discoverResources()
	{
		_Logger.info("Issuing discover...");
		initClient();
		Set<WebLink> wlSet = _coapClient.discover();
		if (wlSet != null) {
			for (WebLink wl : wlSet) {
				_Logger.info(" --> WebLink: " + wl.getURI());
			}
		}
	}
	
	public void pingServer() 
	{
		_Logger.info("Sending ping...");
		initClient();
		// TODO: you must implement this yourself (hint: it’s only one line of code)
		if(_coapClient.ping()) {
			_Logger.info("ping successfully");
		} else {
			_Logger.info("ping failed");
		}
	}
	
	public void sendDeleteRequest(boolean useCON)
	{
		initClient();
		handleDeleteRequest(useCON);
	}
	
	public void sendDeleteRequest(String resourceName, boolean useCON)
	{
		_isInitialized = false;
		initClient(resourceName);
		handleDeleteRequest(useCON);
	}
	
	public void sendGetRequest()
	{
		initClient();
		handleGetRequest(false);
	}
	
	public void sendGetRequest(String resourceName)
	{
		_isInitialized = false;
		initClient(resourceName);
		handleGetRequest(false);
	}
	
	public void sendGetRequest(boolean useNON)
	{
		initClient();
		handleGetRequest(useNON);
	}
	
	public void sendGetRequest(String resourceName, boolean useNON)
	{
		_isInitialized = false;
		initClient(resourceName);
		sendGetRequest(useNON);
	}
	
	public void sendPostRequest(String payload, boolean useCON)
	{
		initClient();
		handlePostRequest(payload, useCON);
	}
	
	public void sendPostRequest(String resourceName, String payload, boolean useCON)
	{
		_isInitialized = false;
		initClient(resourceName);
		handlePostRequest(payload, useCON);
	}
	
	public void sendPutRequest(String payload, boolean useCON)
	{
		initClient();
		handlePutRequest(payload, useCON);
	}
	
	public void sendPutRequest(String resourceName, String payload, boolean useCON)
	{
		_isInitialized = false;
		initClient(resourceName);
		handlePutRequest(payload, useCON);
	}
	
//	public void registerObserver(boolean enableWait)
//	{
//		_Logger.info("Registering observer...");
//		CoapClientObserverHandler handler = null;
//		if (enableWait) {
//			_clientConn.observeAndWait(handler);
//		} else {
//			_clientConn.observe(handler);
//		}
//	}
	
	// private methods
	
	private void handleDeleteRequest(boolean useCON)
	{
		// NOTE: you must implement this yourself
		_Logger.info("Sending DELETE...");
		CoapResponse response = null;
		if (useCON) {
			_coapClient.useCONs().useEarlyNegotiation(32).get();
		}
		response = _coapClient.delete();
		if (response != null) {
			 _Logger.info(
			 "Response: " + response.isSuccess() + " - " + response.getOptions() + " - " + response.getCode());
		} else {
			 _Logger.warning("No response received.");
		}
	}
	
	private void handleGetRequest(boolean useNON)
	{
		if (useNON) {
			_coapClient.useNONs();
		} else {
			_Logger.info("Sending GET...");
			CoapResponse response = null;
			_coapClient.useCONs().useEarlyNegotiation(32).get();
			response = _coapClient.get();
			if (response != null) {
				 _Logger.info(
				 "Response: " + response.isSuccess() + " - " + response.getOptions() + " - " + response.getCode());
			} else {
				 _Logger.warning("No response received.");
			}
		}
	// NOTE: you must implement the rest of this yourself
	}
	
	private void handlePutRequest(String payload, boolean useCON)
	{
		_Logger.info("Sending PUT...");
		CoapResponse response = null;
		if (useCON) {
			_coapClient.useCONs().useEarlyNegotiation(32).get();
		}
		response = _coapClient.put(payload, MediaTypeRegistry.TEXT_PLAIN);
		if (response != null) {
			_Logger.info(
					"Response: " + response.isSuccess() + " - " + response.getOptions() + " - " + response.getCode());
		} else {
			_Logger.warning("No response received.");
		}
	}
	
	private void handlePostRequest(String payload, boolean useCON)
	{
		_Logger.info("Sending POST...");
		CoapResponse response = null;
		if (useCON) {
			_coapClient.useCONs().useEarlyNegotiation(32).get();
		}
		response = _coapClient.post(payload, MediaTypeRegistry.TEXT_PLAIN);
		if (response != null) {
			 _Logger.info(
			 "Response: " + response.isSuccess() + " - " + response.getOptions() + " - " + response.getCode());
		} else {
			 _Logger.warning("No response received.");
		}
	}
	
	private void initClient()
	{
		initClient(null);
	}
	private void initClient(String resourceName)
	{
		if (_isInitialized) {
			return;
		}
		if (_coapClient != null) {
			_coapClient.shutdown();
			_coapClient = null;
		}
		try {
			if (resourceName != null) {
				_serverAddr += "/" + resourceName;
			}
			_coapClient = new CoapClient(_serverAddr);
			_Logger.info("Created client connection to server / resource: " + _serverAddr);
		} catch (Exception e) {
			_Logger.log(Level.SEVERE, "Failed to connect to broker: " + getCurrentUri(), e);
		}
	}

}
