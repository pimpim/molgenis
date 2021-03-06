package org.molgenis.data.rest;

import java.util.List;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

import org.molgenis.data.QueryRule;
import org.springframework.data.domain.Sort;

public class EntityCollectionRequest
{
	private List<QueryRule> q;

	private Sort sort;

	@Min(0)
	private int start = 0;
	@Min(0)
	@Max(10000)
	private int num = 100;

	public int getStart()
	{
		return start;
	}

	public void setStart(int start)
	{
		this.start = start;
	}

	public int getNum()
	{
		return num;
	}

	public void setNum(int num)
	{
		this.num = num;
	}

	public List<QueryRule> getQ()
	{
		return q;
	}

	public void setQ(List<QueryRule> q)
	{
		this.q = q;
	}

	public Sort getSort()
	{
		return sort;
	}

	public void setSort(Sort sort)
	{
		this.sort = sort;
	}
}