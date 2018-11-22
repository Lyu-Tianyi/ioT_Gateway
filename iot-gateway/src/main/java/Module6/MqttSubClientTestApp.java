package Module6;

// import java.util.logging.Logger;


public class MqttSubClientTestApp {
	// static
	
	// private static final Logger _Logger = Logger.getLogger(MqttSubClientTestApp.class.getName());
	private static MqttSubClientTestApp _subApp;
	/**
	* @param args
	*/
	public static void main(String[] args)
	{
		_subApp = new MqttSubClientTestApp();
		try {
			_subApp.start();
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
	
	public MqttSubClientTestApp()
	{
		super();
	}
	
	// public methods
	
	/**
	* Connect to the MQTT client, then:
	* 1) If this is the subscribe app, subscribe to the given topic
	* 2) If this is the publish app, publish a test message to the given topic
	*/
	
	public void start()
	{
		_mqttClient = new MqttClientConnector("iot.eclipse.org", "tcp",  1883, false);
		_mqttClient.connect();
		String topicName = "SensorData";
		_mqttClient.subscribeToTopic(topicName);
		// _mqttClient.subscribeToAll();
	 }
}
