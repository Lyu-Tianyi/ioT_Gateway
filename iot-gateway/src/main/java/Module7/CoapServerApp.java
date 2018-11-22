package Module7;

// import java.util.logging.Logger;

public class CoapServerApp {

	// static
	
	// private static final Logger _Logger = Logger.getLogger(CoapServerApp.class.getName());
	private static CoapServerApp _App;
	/**
	* @param args
	*/
	public static void main(String[] args)
	{
		_App = new CoapServerApp();
		try {
			_App.start();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	// private var's
	private CoapServerConnector _coapServer;
	// constructors
	/**
	*
	*/
	public CoapServerApp()
	{
		super();
	}
	// public methods
	/**
	*
	*/
	public void start()
	{
		_coapServer = new CoapServerConnector();
		_coapServer.start();
	}
}
