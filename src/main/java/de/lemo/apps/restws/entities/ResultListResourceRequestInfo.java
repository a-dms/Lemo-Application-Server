package de.lemo.apps.restws.entities;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Result list with resourceRequestInfo objects
 */
@XmlRootElement
public class ResultListResourceRequestInfo {

	private List<ResourceRequestInfo> rri;

	public ResultListResourceRequestInfo() {
		this.rri = new ArrayList<ResourceRequestInfo>();
	}

	public ResultListResourceRequestInfo(final List<ResourceRequestInfo> resourceRequestInfos) {
		this.rri = resourceRequestInfos;
	}

	@XmlElement
	public List<ResourceRequestInfo> getResourceRequestInfos() {
		return this.rri;
	}

	public void setRoles(final List<ResourceRequestInfo> resourceRequestInfos) {
		this.rri = resourceRequestInfos;
	}

	public void add(final ResourceRequestInfo rri) {
		this.rri.add(rri);
	}

	public void addAll(final Collection<ResourceRequestInfo> rri) {
		this.rri.addAll(rri);
	}
}
