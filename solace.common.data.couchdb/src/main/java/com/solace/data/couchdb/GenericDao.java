package com.solace.data.couchdb;

import java.lang.reflect.ParameterizedType;
import java.util.List;

import org.ektorp.CouchDbConnector;
import org.ektorp.ViewQuery;
import org.ektorp.support.CouchDbRepositorySupport;

import com.solace.data.IEntity;
import com.solace.logging.*;
import com.solace.data.IGenericDao;

public abstract class GenericDao<T extends IEntity<String>> extends
		CouchDbRepositorySupport<T> implements IGenericDao<String, T> {


	private static final Logger LOGGER = Logger.getLogger(GenericDao.class);

	protected Class<T> m_persistenceClass;

	protected GenericDao(Class<T> type, CouchDbConnector db) {
		super(type, db, true);

		try {
			/*ParameterizedType genericSuperclass = (ParameterizedType) getClass()
					.getGenericSuperclass();
			this.m_persistenceClass = (Class<T>) genericSuperclass
					.getActualTypeArguments()[0];

			LOGGER.debugFormat("persistent class identified as {}", m_persistenceClass);*/
			LOGGER.debug("persistent class identified as {}", type);
			this.m_persistenceClass = type;
			
		} catch (Exception e) {
			LOGGER.error(e.getMessage(),e);
		}
	}

	@Override
	public void delete(T arg0) {
		db.delete(arg0);
	}
	
	@Override
	public int executeUpdate(String arg0, Object... arg1) throws Exception {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public List<T> findAll() throws Exception {
		return getAll();
		//ViewQuery q = createQuery("all").descending(true);
		//return db.queryView(q, m_persistenceClass);
	}

	@Override
	public T findById(String arg0) throws Exception {
		return get(arg0);
	}

	@Override
	public T queryObject(String arg0, Object... arg1) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<T> queryObjects(String arg0, Object... arg1) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void save(T arg0) {
		DocumentEntity entity = (DocumentEntity)arg0;
		if ( entity.getRevision() == null )
			db.create(entity);
		else
			db.update(entity);
	}
}
