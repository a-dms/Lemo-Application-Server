package de.lemo.apps.restws.entities;

import java.util.List;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * container for the REST/JSON communication to transfer the list of connectors
 * 
 * @author Boris Wenzlaff
 */
@XmlRootElement
public class SCConnectors {

	private List<SCConnector> connectors;

	public List<SCConnector> getConnectors() {
		return this.connectors;
	}

	public void setConnectors(final List<SCConnector> connectors) {
		this.connectors = connectors;
	}
}