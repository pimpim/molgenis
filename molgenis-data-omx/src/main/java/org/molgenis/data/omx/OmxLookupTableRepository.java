package org.molgenis.data.omx;

import java.io.IOException;
import java.util.Collections;
import java.util.Iterator;

import org.molgenis.data.DataService;
import org.molgenis.data.Entity;
import org.molgenis.data.EntityMetaData;
import org.molgenis.data.Query;
import org.molgenis.data.Queryable;
import org.molgenis.data.Writable;
import org.molgenis.data.support.AbstractRepository;
import org.molgenis.data.support.MapEntity;
import org.molgenis.data.support.QueryImpl;
import org.molgenis.omx.observ.Category;
import org.molgenis.omx.observ.ObservableFeature;
import org.molgenis.omx.protocol.OmxLookupTableEntityMetaData;

public class OmxLookupTableRepository extends AbstractRepository implements Queryable, Writable
{
	public static final String BASE_URL = "omx-lut://";

	private final DataService dataService;
	private final String categoricalFeatureIdentifier;

	public OmxLookupTableRepository(DataService dataService, String categoricalFeatureIdentifier)
	{
		super(BASE_URL);
		this.dataService = dataService;
		this.categoricalFeatureIdentifier = categoricalFeatureIdentifier;
	}

	@Override
	public long count()
	{
		return count(new QueryImpl());
	}

	@Override
	public void close() throws IOException
	{
		// noop
	}

	@Override
	public Class<? extends Entity> getEntityClass()
	{
		return MapEntity.class;
	}

	@Override
	public Iterator<Entity> iterator()
	{
		return dataService.findAll(Category.ENTITY_NAME,
				new QueryImpl().eq(Category.OBSERVABLEFEATURE, getObservableFeature())).iterator();
	}

	@Override
	public Integer add(Entity entity)
	{
		add(Collections.singleton(entity));
		return entity.getIdValue();
	}

	@Override
	public void add(Iterable<? extends Entity> entities)
	{
		throw new UnsupportedOperationException(); // FIXME implement method
	}

	@Override
	public void flush()
	{
		// noop
	}

	@Override
	public void clearCache()
	{
		// noop
	}

	@Override
	public long count(Query q)
	{
		return dataService.count(Category.ENTITY_NAME,
				new QueryImpl().eq(Category.OBSERVABLEFEATURE, getObservableFeature()));
	}

	@Override
	public Iterable<Entity> findAll(Query q)
	{
		q = new QueryImpl(q).eq(Category.OBSERVABLEFEATURE, getObservableFeature());
		return dataService.findAll(Category.ENTITY_NAME, q);
	}

	@Override
	public <E extends Entity> Iterable<E> findAll(Query q, Class<E> clazz)
	{
		q = new QueryImpl(q).eq(Category.OBSERVABLEFEATURE, getObservableFeature());
		return dataService.findAll(Category.ENTITY_NAME, q, clazz);
	}

	@Override
	public Entity findOne(Query q)
	{
		q = new QueryImpl(q).eq(Category.OBSERVABLEFEATURE, getObservableFeature());
		return dataService.findOne(Category.ENTITY_NAME, q);
	}

	@Override
	public Entity findOne(Integer id)
	{
		return dataService.findOne(Category.ENTITY_NAME, id);
	}

	@Override
	public Iterable<Entity> findAll(Iterable<Integer> ids)
	{
		return dataService.findAll(Category.ENTITY_NAME, ids);
	}

	@Override
	public <E extends Entity> Iterable<E> findAll(Iterable<Integer> ids, Class<E> clazz)
	{
		return dataService.findAll(Category.ENTITY_NAME, ids, clazz);
	}

	@Override
	public <E extends Entity> E findOne(Integer id, Class<E> clazz)
	{
		return dataService.findOne(Category.ENTITY_NAME, id, clazz);
	}

	@Override
	public <E extends Entity> E findOne(Query q, Class<E> clazz)
	{
		q = new QueryImpl(q).eq(Category.OBSERVABLEFEATURE, getObservableFeature());
		return dataService.findOne(Category.ENTITY_NAME, q, clazz);
	}

	@Override
	protected EntityMetaData getEntityMetaData()
	{
		return new OmxLookupTableEntityMetaData(getObservableFeature());
	}

	private ObservableFeature getObservableFeature()
	{
		return dataService
				.findOne(ObservableFeature.ENTITY_NAME,
						new QueryImpl().eq(ObservableFeature.IDENTIFIER, categoricalFeatureIdentifier),
						ObservableFeature.class);
	}
}
