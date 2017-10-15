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
package org.codenergic.theskeleton.client;

import org.codenergic.theskeleton.user.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/clients")
public class Oauth2ClientRestController {
	private OAuth2ClientService oAuth2ClientService;

	public Oauth2ClientRestController(OAuth2ClientService oAuth2ClientService) {
		this.oAuth2ClientService = oAuth2ClientService;
	}

	private OAuth2ClientRestData convertEntityToRestData(OAuth2ClientEntity client) {
		return client == null ? null : OAuth2ClientRestData.builder().fromOAuth2ClientEntity(client).build();
	}

	@DeleteMapping("/{id}")
	public void deleteClient(@PathVariable("id") final String id) {
		oAuth2ClientService.deleteClient(id);
	}

	@GetMapping("/{id}")
	public OAuth2ClientRestData findClientById(@PathVariable("id") final String id) {
		OAuth2ClientEntity client = oAuth2ClientService.findClientById(id);
		return convertEntityToRestData(client);
	}

	@GetMapping
	public Page<OAuth2ClientRestData> findClients(@AuthenticationPrincipal UserEntity user, final Pageable pageable) {
		return oAuth2ClientService.findClientByOwner(user.getId(), pageable)
				.map(this::convertEntityToRestData);
	}

	@PutMapping("/{id}/generate-secret")
	public OAuth2ClientRestData generateClientSecret(@PathVariable("id") String id) {
		return convertEntityToRestData(oAuth2ClientService.generateSecret(id));
	}

	@PostMapping
	public OAuth2ClientRestData saveClient(@RequestBody @Validated(OAuth2ClientRestData.New.class) final OAuth2ClientRestData client) {
		return convertEntityToRestData(oAuth2ClientService.saveClient(client.toOAuth2ClientEntity()));
	}

	@PutMapping("/{id}")
	public OAuth2ClientRestData updateClient(@PathVariable("id") String id, 
			@RequestBody @Validated(OAuth2ClientRestData.Existing.class) final OAuth2ClientRestData client) {
		return convertEntityToRestData(oAuth2ClientService.updateClient(id, client.toOAuth2ClientEntity()));
	}
}
