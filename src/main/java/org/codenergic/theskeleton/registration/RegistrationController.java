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
package org.codenergic.theskeleton.registration;

import javax.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/registration")
public class RegistrationController {
	private static final String REGISTRATION = "registration";
	private static final String REGISTRATION_CONFIRMATION = "registration_confirmation";

	private RegistrationService registrationService;

	public RegistrationController(RegistrationService registrationService) {
		this.registrationService = registrationService;
	}

	@GetMapping
	public String registrationView(RegistrationForm registrationForm) {
		return REGISTRATION;
	}

	@PostMapping
	public String register(@Valid RegistrationForm registrationForm, BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			return registrationView(registrationForm);
		}
		registrationService.registerUser(registrationForm);
		return REGISTRATION_CONFIRMATION;
	}
}
