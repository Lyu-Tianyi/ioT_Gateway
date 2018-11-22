package Module7;

// import java.util.logging.Logger;

public class CoapClientApp
{
	// static
	
	// private static final Logger _Logger = Logger.getLogger(CoapClientApp.class.getName());
	private static CoapClientApp _App;
	
	/**
	* @param args
	*/
	
	public static void main(String[] args)
	{
		_App = new CoapClientApp();
		try {
			_App.start();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	// private var's
	private CoapClientConnector _coapClient;
	
	// constructors
	/**
	*
	*/
	public CoapClientApp()
	{
		super();
	}
	
	// public methods
	
	/**
	* Connect to the CoAP server
	*
	*/
	
	public void start()
	{
		_coapClient = new CoapClientConnector("coap", "127.0.0.1", "5683");
		
//		coap_client.pingServer();
//		coap_client.sendPostRequest("temp", "tempdata", true);
//		coap_client.sendGetRequest("tempdata", true);
//		coap_client.sendPutRequest("temp", "tempdata", true);
//		coap_client.sendDeleteRequest("tempdata", true);

		_coapClient.runTests("temp");
	}
}

