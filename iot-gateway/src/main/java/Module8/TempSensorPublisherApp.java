package Module8;

// import java.util.logging.Logger;

public class TempSensorPublisherApp {
	
	private String _userName = "A1E-S9V7ktuqtpKH96wmwIGVJ5dYhNMm5V";
	private String _authToken = "";
	private String _pemFileName = "D:\\git\\repository2\\iot-gateway\\ubidots_cert.pem";
	private String _host = "things.ubidots.com";
	
	// static
	// private static final Logger _Logger = Logger.getLogger(TempSensorPublisherApp.class.getName());
	private static TempSensorPublisherApp _senApp;
	/**
	 * @param args
	 */
	public static void main(String[] args) 
	{
		_senApp = new TempSensorPublisherApp();
		try {
			_senApp.start();
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
	public TempSensorPublisherApp()
	{
		super();
	}
	
	//things.ubidots.com
	public void start() 
	{
		_mqttClient = new MqttClientConnector(_host, _userName, _pemFileName, _authToken);
		_mqttClient.connect();
		String topicName = "/v1.6/devices/iotconnectdeviceu/tempsensor";
		String payload = "30";

		_mqttClient.publishMessage(topicName, 0, payload.getBytes());
		_mqttClient.disconnect();
		
	}
}
