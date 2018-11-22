package Module6;

// import java.util.logging.Logger;

public class MqttPubClientTestApp {
	// static
	
	// private static final Logger _Logger = Logger.getLogger(MqttPubClientTestApp.class.getName());
	private static MqttPubClientTestApp _pubApp;
	/**
	* @param args
	*/
	public static void main(String[] args)
	{
		_pubApp = new MqttPubClientTestApp();
		try {
			_pubApp.start();
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
	public MqttPubClientTestApp()
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
		String payload = "This is a test...";
		
		_mqttClient.publishMessage(topicName, 0, payload.getBytes());
		_mqttClient.publishMessage(topicName, 1, payload.getBytes());
		_mqttClient.publishMessage(topicName, 2, payload.getBytes());
		_mqttClient.disconnect();
	}
}