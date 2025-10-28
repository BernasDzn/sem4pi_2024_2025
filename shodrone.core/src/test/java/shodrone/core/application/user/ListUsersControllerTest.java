package shodrone.core.application.user;

import eapli.framework.infrastructure.authz.application.UserManagementService;
import eapli.framework.infrastructure.authz.domain.model.SystemUser;
import org.junit.jupiter.api.Test;
import shodrone.core.domain.user.ShodronePasswordEncoder;
import shodrone.core.domain.user.ShodronePasswordPolicy;
import shodrone.core.domain.user.ShodroneRoles;
import shodrone.core.domain.user.UserBuilderHelper;
import shodrone.core.persistence.repofactory.RepositoryFactory;
import shodrone.core.persistence.repofactory.RepositoryFactoryInMemory;

import static org.junit.jupiter.api.Assertions.*;

class ListUsersControllerTest {

    @Test
    void allUsersTest() {
        RepositoryFactory repositoryFactory = new RepositoryFactoryInMemory();

        UserManagementService userManagementService = new UserManagementService(
                repositoryFactory.users(),
                new ShodronePasswordPolicy(),
                new ShodronePasswordEncoder());

        final var userBuilder = UserBuilderHelper.builder();
        userBuilder.withUsername("test").withPassword("123456A1").withName("Test", "Test")
                .withEmail("test@test.test").withRoles(ShodroneRoles.POWER_USER);
        final var newUser = userBuilder.build();

        repositoryFactory.users().save(newUser);

        int count = 0;
        for (@SuppressWarnings("unused")
        SystemUser user : userManagementService.allUsers()) {
            count++;
        }

        /**
         * RepositoryFactoryInMemory already instantiates 1 user
         * so there is 2 users now.
         */
        assertEquals(2, count);
    }

}