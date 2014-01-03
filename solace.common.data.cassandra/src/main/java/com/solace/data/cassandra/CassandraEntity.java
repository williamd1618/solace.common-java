package com.solace.data.cassandra;

import java.util.Date;
import com.solace.data.IEntity;
import com.solace.logging.Logger;

public abstract class CassandraEntity<ID> implements IEntity<ID> {

	private static final Logger LOGGER = Logger.getLogger(CassandraEntity.class);
	
	protected ID m_id;
	protected Date m_createDate;
	protected Date m_lastModifiedDate;
	protected long m_version;	
	protected String m_docType;

	protected CassandraEntity() {
		this(null);
		
		this.m_docType = this.getClass().getSimpleName();
		
		LOGGER.info("DocType set to {}", this.m_docType);
	}
	
	protected CassandraEntity(final ID _id) {
		this.m_id = _id;
		LOGGER.info("Id set to {}", this.m_id);
	}
	
	@Override
	public ID getId() {
		return m_id;		
	}
	
	@Override
	public void setId(ID _id) {
		this.m_id = _id;
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
