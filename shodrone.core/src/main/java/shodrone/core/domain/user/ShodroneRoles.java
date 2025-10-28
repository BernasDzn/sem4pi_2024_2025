/*
 * Copyright (c) 2013-2024 the original author or authors.
 *
 * MIT License
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package shodrone.core.domain.user;

import eapli.framework.infrastructure.authz.domain.model.Role;

/**
 * The roles in the system.
 * <br><br>
 * These are poised to rise so they must be modular.
 */
public final class ShodroneRoles {

	/**
	 * The Poweruser has access to all features. it must be deleted in sprint 3.
	 * <br><br>
	 * Can also be deleted at the end of sprint 2 if we ensure system coherence.
	 * <br><br>
	 * TODO: delete this role
	 */
	public static final Role POWER_USER = Role.valueOf("POWER_USER");

	/**
	 * Administrator
	 */
	public static final Role ADMIN = Role.valueOf("ADMIN");

	/**
	 * CRM Manager
	 */
	public static final Role CRM_MANAGER = Role.valueOf("CRM Manager");

	/**
	 * CRM Collaborator
	 */
	public static final Role CRM_COLLABORATOR = Role.valueOf("CRM Collaborator");

	/**
	 * Show Designer
	 */
	public static final Role SHOW_DESIGNER = Role.valueOf("Show Designer");

	/**
	 * Drone Technician
	 */
	public static final Role DRONE_TECH = Role.valueOf("Drone Technician");

	/**
	 * Customer
	 */
	public static final Role CUSTOMER = Role.valueOf("Customer");

	/**
	 * get available role types for adding new users
	 *
	 * @return
	 */
	public static Role[] nonUserValues() {
		return new Role[] { ADMIN, CRM_MANAGER, CRM_COLLABORATOR, SHOW_DESIGNER, DRONE_TECH };
	}

	// TODO: maybe implement methods that return the roles for each use case ?
}
