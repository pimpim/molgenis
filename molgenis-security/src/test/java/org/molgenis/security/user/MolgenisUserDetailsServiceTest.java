package org.molgenis.security.user;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import java.util.Collection;
import java.util.Set;

import org.molgenis.data.DataService;
import org.molgenis.data.Query;
import org.molgenis.data.support.QueryImpl;
import org.molgenis.omx.auth.MolgenisUser;
import org.molgenis.security.SecurityUtils;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.mapping.GrantedAuthoritiesMapper;
import org.springframework.security.core.userdetails.UserDetails;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.testng.collections.Sets;

import com.google.common.base.Function;
import com.google.common.collect.Collections2;

public class MolgenisUserDetailsServiceTest
{
	private MolgenisUserDetailsService userDetailsService;

	@BeforeMethod
	public void setUp()
	{
		DataService dataService = mock(DataService.class);
		MolgenisUser adminUser = when(mock(MolgenisUser.class).getSuperuser()).thenReturn(Boolean.TRUE).getMock();
		when(adminUser.getUsername()).thenReturn("admin");
		when(adminUser.getPassword()).thenReturn("password");
		MolgenisUser userUser = when(mock(MolgenisUser.class).getSuperuser()).thenReturn(Boolean.FALSE).getMock();
		when(userUser.getUsername()).thenReturn("user");
		when(userUser.getPassword()).thenReturn("password");
		Query qAdmin = new QueryImpl().eq(MolgenisUser.USERNAME, "admin");
		when(dataService.findOne(MolgenisUser.ENTITY_NAME, qAdmin, MolgenisUser.class)).thenReturn(adminUser);
		Query qUser = new QueryImpl().eq(MolgenisUser.USERNAME, "user");
		when(dataService.findOne(MolgenisUser.ENTITY_NAME, qUser, MolgenisUser.class)).thenReturn(userUser);
		GrantedAuthoritiesMapper authoritiesMapper = new GrantedAuthoritiesMapper()
		{
			@Override
			public Collection<? extends GrantedAuthority> mapAuthorities(
					Collection<? extends GrantedAuthority> authorities)
			{
				return authorities;
			}
		};
		userDetailsService = new MolgenisUserDetailsService(dataService, authoritiesMapper);
	}

	@Test(expectedExceptions = IllegalArgumentException.class)
	public void MolgenisUserDetailsService()
	{
		new MolgenisUserDetailsService(null, null);
	}

	@Test
	public void loadUserByUsername_SuperUser()
	{
		UserDetails user = userDetailsService.loadUserByUsername("admin");
		Set<String> authorities = Sets.newHashSet(Collections2.transform(user.getAuthorities(),
				new Function<GrantedAuthority, String>()
				{
					@Override
					public String apply(GrantedAuthority authority)
					{
						return authority.getAuthority();
					}
				}));
		assertTrue(authorities.contains(SecurityUtils.AUTHORITY_SU));
		assertEquals(authorities.size(), 1);
	}

	@Test
	public void loadUserByUsername_NonSuperUser()
	{
		UserDetails user = userDetailsService.loadUserByUsername("user");
		Set<String> authorities = Sets.newHashSet(Collections2.transform(user.getAuthorities(),
				new Function<GrantedAuthority, String>()
				{
					@Override
					public String apply(GrantedAuthority authority)
					{
						return authority.getAuthority();
					}
				}));
		assertEquals(authorities.size(), 0);
	}
}
