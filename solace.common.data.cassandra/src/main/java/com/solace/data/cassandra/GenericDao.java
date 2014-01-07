package com.solace.data.cassandra;

import java.util.List;

import com.datastax.driver.core.BoundStatement;
import com.datastax.driver.core.PreparedStatement;
import com.datastax.driver.core.ResultSet;
import com.solace.data.IGenericDao;
import com.solace.logging.Logger;
import com.solace.support.StringUtils;

public abstract class GenericDao<ID extends Class<?>, T extends CassandraEntity<ID>> 
		implements IGenericDao<ID, T> {
	
	private static final Logger LOGGER = Logger.getLogger(GenericDao.class);
	
	private Client client;
	
	private String schema;
	
	private Class<T> m_persistenceClass;
	
	private PreparedStatement findById, deleteById;
	
	
	
	protected GenericDao(Class<T> type, String... nodes) {
		this(type, null, nodes);
	}

	
	protected GenericDao(Class<T> type, String schema, String... nodes) {
		try {
			client = Client.getInstance(nodes);
			this.schema = schema;
			
			LOGGER.debug("persistent class identified as {}", type);
			this.m_persistenceClass = type;
			
		} catch (Exception e) {
			LOGGER.error(e.getMessage(),e);
		}
		
		String query = "select * from " + (!StringUtils.isNullOrEmpty(schema) ? schema + "." : "") +
				m_persistenceClass.getSimpleName().toLowerCase() + " where id = ?";

		findById = client.getSession().prepare(query);
		
		query = "delete from " + (!StringUtils.isNullOrEmpty(schema) ? schema + "." : "") +
				m_persistenceClass.getSimpleName().toLowerCase() + " where id = ?";
		
		deleteById = client.getSession().prepare(query);
	}
	

	@Override
	public T findById(ID id) throws Exception {
		BoundStatement statement = new BoundStatement(findById);
		
		ResultSet results = client.getSession().execute(statement.bind(id));
		
		if ( results != null && results.isFullyFetched() )
			return results.get
		else
			return null;				
	}

	@Override
	public void save(T entity) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void delete(T entity) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<T> findAll() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<T> queryObjects(String query, Object... args) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public T queryObject(String query, Object... args) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int executeUpdate(String query, Object... args) throws Exception {
		// TODO Auto-generated method stub
		return 0;
	}


}
