package Module7;

import java.util.logging.Logger;

import org.eclipse.californium.core.CoapResource;
import org.eclipse.californium.core.coap.CoAP.ResponseCode;
import org.eclipse.californium.core.server.resources.CoapExchange;

public class TempResourceHandler extends CoapResource {

	//private TempResourceHandler tempResource=new TempResourceHandler();
	private static final Logger _Logger = Logger.getLogger(TempResourceHandler.class.getName());
	
	// Constructor
	
	public TempResourceHandler() {
		
		super("temp", false);
		
		System.out.println("Temp Resource Handler Working");
		
	}
	
	//public methods
	
	@Override
	public void handleGET(CoapExchange coapExchange) {
		
		_Logger.info("GET request from:" + coapExchange.getSourceAddress());
		_Logger.info("Response to --- " + super.getName());

		coapExchange.respond(ResponseCode.VALID);
		
		_Logger.info("GET Handler working:");
		_Logger.info(coapExchange.getRequestCode().toString() + ": " + coapExchange.getRequestText());
		
	}
	
	@Override
	public void handlePOST(CoapExchange coapExchange) {
		
		_Logger.info("POST request from:" + coapExchange.getSourceAddress());
		_Logger.info("Response to --- " + super.getName());

		coapExchange.respond(ResponseCode.CREATED);
		
		_Logger.info("POST Handler working:");
		_Logger.info(coapExchange.getRequestCode().toString() + ": " + coapExchange.getRequestText());

	}
	
	@Override
	public void handlePUT(CoapExchange coapExchange) {
		
		_Logger.info("PUT request from:" + coapExchange.getSourceAddress());
		_Logger.info("Response to --- " + super.getName());

		coapExchange.respond(ResponseCode.CHANGED);
		
		_Logger.info("PUT Handler working:");
		_Logger.info(coapExchange.getRequestCode().toString() + ": " + coapExchange.getRequestText());
		
	}
	
	@Override
	public void handleDELETE(CoapExchange coapExchange) {
		
		_Logger.info("DELETE request from:" + coapExchange.getSourceAddress());
		_Logger.info("Response to --- " + super.getName());

		coapExchange.respond(ResponseCode.DELETED);
		
		_Logger.info("DELETE Handler working:");
		_Logger.info(coapExchange.getRequestCode().toString() + ": " + coapExchange.getRequestText());
		
	}
}
