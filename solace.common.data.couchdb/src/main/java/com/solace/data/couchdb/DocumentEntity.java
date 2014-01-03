package com.solace.data.couchdb;

import java.util.Date;


import org.ektorp.support.CouchDbDocument;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.solace.data.*;
import java.util.UUID;
import com.solace.logging.*;

public abstract class DocumentEntity extends CouchDbDocument implements IEntity<String> {

	private static final Logger LOGGER = Logger.getLogger(DocumentEntity.class);
	
	protected String m_id;
	protected Date m_createDate;
	protected Date m_lastModifiedDate;
	protected long m_version;
	protected String m_revision;
	protected String m_docType;

	protected DocumentEntity() {
		this.m_id = this.getClass().getSimpleName().toLowerCase() + "-" + UUID.randomUUID().toString();
		this.m_docType = this.getClass().getSimpleName();

		LOGGER.info("Id set to {}", this.m_id);
		LOGGER.info("DocType set to {}", this.m_docType);
	}
	
	@Override
	@JsonProperty("_id")
	public String getId() {
		return m_id;		
	}
	
	@Override
	public void setId(String _id) {
		this.m_id = _id;
	}
	
	@JsonProperty("_rev")
	public String getRevision() {
		return m_revision;
	}
	
	public void setRevision(String _rev) {
		this.m_revision = _rev;
	}

	@Override
	public final void delete() {
		doDelete();
	}
	
	public abstract void doDelete();

	@Override
	public final void save() {
		doSave();
	}
	
	public abstract void doSave();

	@Override
	public Date getCreateDate() {
		return this.m_createDate;
	}

	@Override
	public Date getLastModifiedDate() {
		return this.m_lastModifiedDate;
	}

	@Override
	@JsonIgnore
	public long getVersion() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void setCreateDate(Date arg0) {
		this.m_createDate = arg0;
	}

	@Override
	public void setLastModifiedDate(Date arg0) {
		this.m_lastModifiedDate = arg0;
	}

	@Override
	public void setVersion(long arg0) {
		
	}

	public void setDocType(String docType) {
		this.m_docType = docType;
	}

	public String getDocType() {
		return this.m_docType;
	}
}

