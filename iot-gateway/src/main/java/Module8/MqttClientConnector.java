package Module8;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

public class MqttClientConnector implements MqttCallback {
	// static
	private static final Logger _Logger = Logger.getLogger(MqttClientConnector.class.getName());

	// private var's
	private String _protocol;
	private String _host = "things.ubidots.com";
	private int _port;
	private MqttClient _mqttClient;
	private String _clientID;
	private String _brokerAddr;
	private String _userName ;
	private String _pemFileName;
	//private String _authToken;
	private Boolean _isSecureConn;

	// constructor
	/**
	* Constructor.
	*
	* @param host The name of the broker to connect.
	* @param userName The username for authorizing access to the broker.
	* @param pemFileName The name of the certificate file to use. If null / invalid, ignored.
	*/
	public MqttClientConnector(String host, String userName, String pemFileName, String authToken)
	{
		super();
		if (host != null && host.trim().length() > 0) {
			_host = host;
		}
		if (userName != null && userName.trim().length() > 0) {
			_userName = userName;
		}
//		if (authToken != null && authToken.trim().length() > 0) {
//			_authToken = authToken;
//		}
		if (pemFileName != null) {
			File file = new File(pemFileName);
			if (file.exists()) {
				_protocol = "ssl";
				_port = 8883;
				_pemFileName = pemFileName;
				_isSecureConn = true;
				_Logger.info("PEM file valid. Using secure connection: " + _pemFileName);
			} else {
				_Logger.warning("PEM file invalid. Using insecure connection: " + pemFileName);
			}
		}
		_clientID = MqttClient.generateClientId();
		_brokerAddr = _protocol + "://" + _host + ":" + _port;
		_Logger.info("Using URL for broker conn: " + _brokerAddr);
	}
	public void connect()
	{
		if (_mqttClient == null) {
			MemoryPersistence persistence = new MemoryPersistence();
			try {
				_mqttClient = new MqttClient(_brokerAddr, _clientID, persistence);
				MqttConnectOptions connOpts = new MqttConnectOptions();
				// TODO: do we always want a clean session?
				connOpts.setCleanSession(true);
				if (_userName != null) {
					connOpts.setUserName(_userName);
				}
				// if we are using a secure connection, there's a bunch of stuff we need to do...
				if (_isSecureConn) {
					initSecureConnection(connOpts);
				}
				_mqttClient.setCallback(this);
				_mqttClient.connect(connOpts);
				_Logger.info("Connected to broker: " + _brokerAddr);
			} catch (MqttException e) {
				_Logger.log(Level.SEVERE, "Failed to connect to broker: " + _brokerAddr, e);
			}
		}
	}

	private void initSecureConnection(MqttConnectOptions connOpts)
	{
		try {
			_Logger.info("Configuring TLS...");
			SSLContext sslContext = SSLContext.getInstance("SSL");
			KeyStore keyStore = readCertificate();
			TrustManagerFactory trustManagerFactory =
			TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
			trustManagerFactory.init(keyStore);
			sslContext.init(null, trustManagerFactory.getTrustManagers(), new SecureRandom());
			connOpts.setSocketFactory(sslContext.getSocketFactory());
		} catch (Exception e) {
			_Logger.log(Level.SEVERE, "Failed to initialize secure MQTT connection.", e);
		}
	}
	
	private KeyStore readCertificate() throws KeyStoreException, NoSuchAlgorithmException, CertificateException, IOException
	{
		KeyStore ks = KeyStore.getInstance(KeyStore.getDefaultType());
		FileInputStream fis = new FileInputStream(_pemFileName);
		BufferedInputStream bis = new BufferedInputStream(fis);
		CertificateFactory cf = CertificateFactory.getInstance("X.509");
		ks.load(null);
		while (bis.available() > 0) {
			Certificate cert = cf.generateCertificate(bis);
			ks.setCertificateEntry("conndev_store" + bis.available(), cert);
		}
		return ks;
	}

	public void disconnect() 
	{
		try {
			_mqttClient.disconnect();
			_Logger.info("Disconnected from broker: " + _brokerAddr);
		} catch (Exception e) {
			_Logger.log(Level.SEVERE, "Failed to disconnect from broker: " + _brokerAddr, e);
		}
	}
	/**
	 * Publishes the given payload to broker directly to topic 'topic'.
	 *
	 * @param topic
	 * @param qosLevel
	 * @param payload
	 */
	public boolean publishMessage(String topic, int qosLevel, byte[] payload)
	{
		boolean success = false;
		try {
			_Logger.info("Publishing message to topic: " + topic);

			// create a new MqttMessage, pass 'payload' to the constructor
			MqttMessage message = new MqttMessage();
			message.setPayload(payload);
			// set the QoS on the message to qosLevel
			message.setQos(qosLevel);
			// call 'publish' on the MQTT client, passing the 'topic' and MqttMessage
			_mqttClient.publish(topic, message);
			// log the result - include the ID from the message
			_Logger.info("Publishing success. MessageID: "+ message.getId());
			success = true;
		} catch (Exception e) {
			_Logger.log(Level.SEVERE, "Failed to publish MQTT message: " + e.getMessage());
		}
		return success;
	}
	public boolean subscribeToAll()
	{
		try {
			_mqttClient.subscribe("$SYS/#");
			return true;
		} catch (MqttException e) {
			_Logger.log(Level.WARNING, "Failed to subscribe to all topics.", e);
		}
		return false;
	}
	public boolean subscribeToTopic(String topic)
	{
		try {
			_mqttClient.subscribe(topic);
			return true;
		} catch (MqttException e) {
			_Logger.log(Level.WARNING, "Failed to subscribe to topic \"" + topic + "\"", e);
		}
		return false;
	}
	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.paho.client.mqttv3.MqttCallback#connectionLost(java.lang.
	 * Throwable)
	 */
	public void connectionLost(Throwable t) 
	{
		// TODO: now what?
		_Logger.log(Level.WARNING, "Connection to broker lost. Will retry soon.", t);
	}
	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.paho.client.mqttv3.MqttCallback#deliveryComplete(org.eclipse.paho.client.mqttv3.IMqttDeliveryToken)
	 *
	 */
	public void deliveryComplete(IMqttDeliveryToken token) 
	{
		try {

			_Logger.info("Delivery complete: " + token.getMessageId() + " - " + token.getResponse() + " - " + token.getMessage());
		} catch (Exception e) {
			_Logger.log(Level.SEVERE, "Failed to retrieve message from token.", e);
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.paho.client.mqttv3.MqttCallback#messageArrived(java.lang.String, org.eclipse.paho.client.mqttv3.MqttMessage)
	 *
	 */
	public void messageArrived(String data, MqttMessage msg) throws Exception 
	{
		_Logger.info("Message arrived: " + data + ", " + msg.getId() + ", Actuator status= " + msg);
	}

}
