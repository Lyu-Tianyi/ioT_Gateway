package Module8;

// import java.util.logging.Logger;

public class TempActurtorSubscriberApp {
	
	private String _userName = "A1E-S9V7ktuqtpKH96wmwIGVJ5dYhNMm5V";
	private String _authToken = "";
	private String _pemFileName = "D:\\git\\repository2\\iot-gateway\\ubidots_cert.pem";
	private String _host = "things.ubidots.com";

	// static
	// private static final Logger _Logger = Logger.getLogger(TempActurtorSubscriberApp.class.getName());
	private static TempActurtorSubscriberApp _actApp;
	
	public static void main(String[] args) 
	{
		// TODO Auto-generated method stub
		_actApp = new TempActurtorSubscriberApp();
		try {
			_actApp.start();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	// params
	private MqttClientConnector _mqttClient;
	
	// constructors
	/**
	 * Default.
	 */

	public TempActurtorSubscriberApp() 
	{
		super();
	}
	
	// public methods
	/**
	 * Connect to the MQTT client, then: 1) If this is the subscribe app, subscribe
	 * to the given topic 2) If this is the publish app, publish a test message to
	 * the given topic
	 */
	public void start() 
	{
		_mqttClient = new MqttClientConnector(_host, _userName, _pemFileName, _authToken);
		_mqttClient.connect();
		String topicName = "/v1.6/devices/iotconnectdeviceu/tempactuator/lv";
		_mqttClient.subscribeToTopic(topicName); 
		//_mqttClient.subscribeToAll(); 
	}
	
	
	

}
