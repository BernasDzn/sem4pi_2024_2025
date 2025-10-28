/*
 * Copyright (c) 2013-2024 the original author or authors.
 *
 * MIT License
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and
 * associated documentation files (the "Software"), to deal in the Software without restriction,
 * including without limitation the rights to use, copy, modify, merge, publish, distribute,
 * sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or
 * substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT
 * NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM,
 * DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package shodrone.bootstrap.bootstrapers.demo;

import eapli.framework.actions.Action;
import eapli.framework.infrastructure.authz.domain.model.Role;
import shodrone.bootstrap.bootstrapers.AbstractUserBootstrapper;
import shodrone.core.domain.user.ShodroneRoles;

import java.util.HashSet;
import java.util.Set;

/**
 * @author Paulo Gandra Sousa
 */
public class BackofficeUsersBootstrapper extends AbstractUserBootstrapper implements Action {

    @SuppressWarnings("squid:S2068")
    private static final String PASSWORD1 = "Password1";

    @Override
    public boolean execute() {
        registerAdmin("Administrator1", PASSWORD1, "Admin", "One", "admin.one@shodrone.com");
        registerCRMManager("CRMManager1", PASSWORD1, "CRMManager", "One", "crm.manager.one@shodrone.com");
        registerCRMCollaborator("CRMCollaborator1", PASSWORD1, "CRMCollaborator", "One", "crm.collab.one@shodrone.com");
        return true;
    }

    private void registerAdmin(final String username, final String password,
            final String firstName, final String lastName, final String email) {
        final Set<Role> roles = new HashSet<>();
        roles.add(ShodroneRoles.ADMIN);

        registerUser(username, password, firstName, lastName, email, roles);
    }

    private void registerCRMManager(final String username, final String password,
            final String firstName, final String lastName, final String email) {
        final Set<Role> roles = new HashSet<>();
        roles.add(ShodroneRoles.CRM_MANAGER);

        registerUser(username, password, firstName, lastName, email, roles);
    }

    private void registerCRMCollaborator(final String username, final String password,
            final String firstName, final String lastName, final String email) {
        final Set<Role> roles = new HashSet<>();
        roles.add(ShodroneRoles.CRM_COLLABORATOR);

        registerUser(username, password, firstName, lastName, email, roles);
    }
}
