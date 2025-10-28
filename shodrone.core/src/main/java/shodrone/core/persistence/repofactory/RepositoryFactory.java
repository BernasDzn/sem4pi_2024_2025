package shodrone.core.persistence.repofactory;

import eapli.framework.domain.repositories.TransactionalContext;
import eapli.framework.infrastructure.authz.domain.repositories.UserRepository;
import shodrone.core.persistence.customer.CustomerRepository;
import shodrone.core.persistence.customer_rep.RepresentativeRepository;
import shodrone.core.persistence.drone_model.DroneModelRepository;
import shodrone.core.persistence.figure.FigureRepository;
import shodrone.core.persistence.figurecategory.FigureCategoryRepository;
import shodrone.core.persistence.show.ShowRepository;
import shodrone.core.persistence.figure.figurekeyword.FigureKeywordRepository;
import shodrone.core.persistence.showproposal.ShowProposalRepository;
import shodrone.core.persistence.showproposaltemplate.ShowProposalTemplateRepository;
import shodrone.core.persistence.showrequest.ShowRequestRepository;
import shodrone.core.persistence.signuprequest.SignupRequestRepository;

public interface RepositoryFactory {

    /**
     * factory method to create a transactional context to use in the repositories
     *
     * @return
     */
    TransactionalContext newTransactionalContext();

    // System Users
    UserRepository users(TransactionalContext autoTx);
    UserRepository users();

    // Signup request
    SignupRequestRepository signupRequests(TransactionalContext autoTx);
    SignupRequestRepository signupRequests();

    // Figure Categories
    FigureCategoryRepository figureCategories(TransactionalContext autoTx);
    FigureCategoryRepository figureCategories();

    // Show Requests
    ShowRequestRepository showRequests(TransactionalContext autoTx);

    ShowRequestRepository showRequests();

    ShowProposalRepository showProposals(TransactionalContext autoTx);

    ShowProposalRepository showProposals();

    ShowRepository shows(TransactionalContext autoTx);
    ShowRepository shows();

    // Customers
    CustomerRepository customers(TransactionalContext autoTx);
    CustomerRepository customers();

    // Figures
    FigureRepository figures(TransactionalContext autoTx);
    FigureRepository figures();

    FigureKeywordRepository keywords(TransactionalContext autoTx);
    FigureKeywordRepository keywords();

    // Customer Representatives
    RepresentativeRepository representatives(TransactionalContext autoTx);
    RepresentativeRepository representatives();

    // Drone Models
    DroneModelRepository droneModels(TransactionalContext autoTx);
    DroneModelRepository droneModels();

    ShowProposalTemplateRepository showProposalTemplates(TransactionalContext autoTx);
    ShowProposalTemplateRepository showProposalTemplates();

}
