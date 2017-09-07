/*
 * Copyright 2017 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.codenergic.theskeleton.privilege.role;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import org.codenergic.theskeleton.privilege.PrivilegeEntity;
import org.codenergic.theskeleton.privilege.PrivilegeRestData;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.web.config.EnableSpringDataWebSupport;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import com.fasterxml.jackson.databind.ObjectMapper;

@RunWith(SpringRunner.class)
@EnableSpringDataWebSupport
@WebMvcTest(controllers = { RoleRestService.class }, secure = false)
public class RoleRestServiceTest {
	@Autowired
	private MockMvc mockMvc;
	@Autowired
	private ObjectMapper objectMapper;
	@MockBean
	private RoleService roleService;

	@Test
	public void testFindRoleByCode() throws Exception {
		RoleEntity dbResult = new RoleEntity()
				.setId("123")
				.setCode("12345")
				.setDescription("Description 12345");
		when(roleService.findRoleByIdOrCode("123")).thenReturn(dbResult);
		MockHttpServletResponse response = mockMvc.perform(get("/api/roles/123"))
				.andReturn()
				.getResponse();
		verify(roleService).findRoleByIdOrCode("123");
		assertThat(response.getStatus()).isEqualTo(200);
		assertThat(response.getContentAsByteArray())
				.isEqualTo(objectMapper.writeValueAsBytes(RoleRestData.builder(dbResult).build()));
	}

	@Test
	public void testFindRoleByCodeNotFound() throws Exception {
		when(roleService.findRoleByIdOrCode("123")).thenReturn(null);
		MockHttpServletResponse response = mockMvc.perform(get("/api/roles/123"))
				.andReturn()
				.getResponse();
		verify(roleService).findRoleByIdOrCode("123");
		assertThat(response.getStatus()).isEqualTo(200);
		assertThat(response.getContentAsByteArray()).isEqualTo(new byte[0]);
	}

	@Test
	public void testFindRoles() throws Exception {
		RoleEntity dbResult = new RoleEntity()
				.setId("123")
				.setCode("12345")
				.setDescription("Description 12345");
		Page<RoleEntity> pageResponseBody = new PageImpl<>(Arrays.asList(dbResult));
		Page<RoleRestData> expectedResponseBody = new PageImpl<>(Arrays.asList(RoleRestData.builder(dbResult).build()));
		when(roleService.findRoles(anyString(), any())).thenReturn(pageResponseBody);
		MockHttpServletResponse response = mockMvc.perform(get("/api/roles"))
				.andReturn()
				.getResponse();
		verify(roleService).findRoles(anyString(), any());
		assertThat(response.getStatus()).isEqualTo(200);
		assertThat(response.getContentAsByteArray()).isEqualTo(objectMapper.writeValueAsBytes(expectedResponseBody));
	}

	@Test
	public void testSaveRole() throws Exception {
		RoleEntity input = new RoleEntity()
				.setId("123")
				.setCode("12345")
				.setDescription("Description 12345");
		RoleEntity dbResult = new RoleEntity()
				.setId(UUID.randomUUID().toString())
				.setCode("12345")
				.setDescription("Description 12345");
		byte[] jsonInput = objectMapper.writeValueAsBytes(RoleRestData.builder(input).build());
		when(roleService.saveRole(any())).thenReturn(dbResult);
		MockHttpServletResponse response = mockMvc.perform(post("/api/roles")
					.contentType(MediaType.APPLICATION_JSON)
					.content(jsonInput))
				.andReturn()
				.getResponse();
		verify(roleService).saveRole(any());
		assertThat(response.getStatus()).isEqualTo(200);
		assertThat(response.getContentAsByteArray())
				.isEqualTo(objectMapper.writeValueAsBytes(RoleRestData.builder(dbResult).build()));
	}

	@Test
	public void testUpdateRole() throws Exception {
		RoleEntity input = new RoleEntity()
				.setId("123")
				.setCode("12345")
				.setDescription("Description 12345");
		RoleEntity dbResult = new RoleEntity()
				.setId(UUID.randomUUID().toString())
				.setCode("12345")
				.setDescription("Description 12345");
		byte[] jsonInput = objectMapper.writeValueAsBytes(RoleRestData.builder(input).build());
		when(roleService.updateRole(eq("123"), any())).thenReturn(dbResult);
		MockHttpServletResponse response = mockMvc.perform(put("/api/roles/123")
					.contentType(MediaType.APPLICATION_JSON)
					.content(jsonInput))
				.andReturn()
				.getResponse();
		verify(roleService).updateRole(eq("123"), any());
		assertThat(response.getStatus()).isEqualTo(200);
		assertThat(response.getContentAsByteArray())
				.isEqualTo(objectMapper.writeValueAsBytes(RoleRestData.builder(dbResult).build()));
	}

	@Test
	public void testDeleteRole() throws Exception {
		RoleEntity input = new RoleEntity()
				.setId("123")
				.setCode("12345")
				.setDescription("Description 12345");
		when(roleService.findRoleByIdOrCode(input.getId())).thenReturn(input);
		doNothing().when(roleService).deleteRole(input.getId());
		MockHttpServletRequestBuilder request = delete("/api/roles/{id}", input.getId())
				.contentType(MediaType.APPLICATION_JSON);
		MockHttpServletResponse response = mockMvc.perform(request)
				.andReturn()
				.getResponse();
		assertThat(response.getStatus()).isEqualTo(200);
	}
	
	@Test
	public void testAddRoleToUser() throws Exception {
		final RoleEntity role = new RoleEntity()
				.setId("role123")
				.setCode("role123");
		when(roleService.addPrivilegeToRole("role123", "privilege123")).thenReturn(role);
		MockHttpServletRequestBuilder request = put("/api/roles/role123/privileges")
				.content("{\"privilege\": \"privilege123\"}")
				.contentType(MediaType.APPLICATION_JSON);
		MockHttpServletResponse response = mockMvc.perform(request)
				.andReturn()
				.getResponse();
		verify(roleService).addPrivilegeToRole("role123", "privilege123");
		assertThat(response.getStatus()).isEqualTo(200);
		assertThat(response.getContentAsByteArray())
				.isEqualTo(objectMapper.writeValueAsBytes(RoleRestData.builder(role).build()));
	}

	@Test
	public void testFindPrivilegesByRoleCode() throws Exception {
		final PrivilegeEntity privilege = new PrivilegeEntity()
				.setId("privilege123")
				.setName("user_list_read");
		final Set<PrivilegeEntity> privileges = new HashSet<>(Arrays.asList(privilege));
		final Set<PrivilegeRestData> expected = privileges.stream()
				.map(p -> PrivilegeRestData.builder(p).build())
				.collect(Collectors.toSet());
		when(roleService.findPrivilegesByRoleCode("role123")).thenReturn(privileges);
		MockHttpServletRequestBuilder request = get("/api/roles/role123/privileges")
				.contentType(MediaType.APPLICATION_JSON);
		MockHttpServletResponse response = mockMvc.perform(request)
				.andReturn()
				.getResponse();
		verify(roleService).findPrivilegesByRoleCode("role123");
		assertThat(response.getStatus()).isEqualTo(200);
		assertThat(response.getContentAsByteArray()).isEqualTo(objectMapper.writeValueAsBytes(expected));
	}

	@Test
	public void testRemoveRoleFromUser() throws Exception {
		final RoleEntity role = new RoleEntity()
				.setId("role123")
				.setCode("role123");
		when(roleService.removePrivilegeFromRole("role123", "privilege123")).thenReturn(role);
		MockHttpServletRequestBuilder request = delete("/api/roles/role123/privileges")
				.content("{\"privilege\": \"privilege123\"}")
				.contentType(MediaType.APPLICATION_JSON);
		MockHttpServletResponse response = mockMvc.perform(request)
				.andReturn()
				.getResponse();
		verify(roleService).removePrivilegeFromRole("role123", "privilege123");
		assertThat(response.getStatus()).isEqualTo(200);
		assertThat(response.getContentAsByteArray())
				.isEqualTo(objectMapper.writeValueAsBytes(RoleRestData.builder(role).build()));
	}
}