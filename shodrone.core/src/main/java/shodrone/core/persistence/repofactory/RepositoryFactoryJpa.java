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
package shodrone.core.persistence.repofactory;

import eapli.framework.domain.repositories.TransactionalContext;
import eapli.framework.infrastructure.authz.domain.repositories.UserRepository;
import eapli.framework.infrastructure.authz.repositories.impl.jpa.JpaAutoTxUserRepository;
import eapli.framework.infrastructure.repositories.impl.jpa.JpaAutoTxRepository;
import shodrone.core.infrastructure.Application;
import shodrone.core.persistence.customer.CustomerRepository;
import shodrone.core.persistence.customer.CustomerRepositoryJpa;
import shodrone.core.persistence.customer_rep.RepresentativeRepository;
import shodrone.core.persistence.customer_rep.RepresentativeRepositoryJpa;
import shodrone.core.persistence.drone_model.DroneModelRepository;
import shodrone.core.persistence.drone_model.DroneModelRepositoryJpa;
import shodrone.core.persistence.figure.FigureRepository;
import shodrone.core.persistence.figure.FigureRepositoryJpa;
import shodrone.core.persistence.figurecategory.FigureCategoryRepository;
import shodrone.core.persistence.figurecategory.FigureCategoryRepositoryJpa;
import shodrone.core.persistence.show.ShowRepository;
import shodrone.core.persistence.show.ShowRepositoryJpa;
import shodrone.core.persistence.figure.figurekeyword.FigureKeywordRepository;
import shodrone.core.persistence.figure.figurekeyword.FigureKeywordRepositoryJpa;
import shodrone.core.persistence.showproposal.ShowProposalRepository;
import shodrone.core.persistence.showproposal.ShowProposalRepositoryJpa;
import shodrone.core.persistence.showproposaltemplate.ShowProposalTemplateRepository;
import shodrone.core.persistence.showproposaltemplate.ShowProposalTemplateRepositoryJpa;
import shodrone.core.persistence.showrequest.ShowRequestRepository;
import shodrone.core.persistence.showrequest.ShowRequestRepositoryJpa;
import shodrone.core.persistence.signuprequest.SignupRequestRepositoryJpa;
import shodrone.core.persistence.signuprequest.SignupRequestRepository;

/**
 *
 * Created by nuno on 21/03/16.
 */
public class RepositoryFactoryJpa implements RepositoryFactory {

    @Override
    public UserRepository users(final TransactionalContext autoTx) {
        return new JpaAutoTxUserRepository(autoTx);
    }

    @Override
    public UserRepository users() {
        return new JpaAutoTxUserRepository(Application.settings().getPersistenceUnitName(),
                Application.settings().getExtendedPersistenceProperties());
    }

    @Override
    public SignupRequestRepository signupRequests(final TransactionalContext autoTx) {
        return new SignupRequestRepositoryJpa(autoTx);
    }

    @Override
    public SignupRequestRepository signupRequests() {
        return new SignupRequestRepositoryJpa(Application.settings().getPersistenceUnitName());
    }

    @Override
    public FigureCategoryRepository figureCategories(TransactionalContext autoTx) {
        return new FigureCategoryRepositoryJpa(autoTx);
    }

    @Override
    public FigureCategoryRepository figureCategories(){
        return new FigureCategoryRepositoryJpa(Application.settings().getPersistenceUnitName());
    }

    @Override
    public ShowRequestRepository showRequests(TransactionalContext autoTx) {
        return new ShowRequestRepositoryJpa(autoTx);
    }

    @Override
    public ShowRequestRepository showRequests() {
        return new ShowRequestRepositoryJpa(Application.settings().getPersistenceUnitName());
    }

    @Override
    public ShowProposalRepository showProposals(TransactionalContext autoTx) {
        return new ShowProposalRepositoryJpa(autoTx);
    }

    @Override
    public ShowProposalRepository showProposals() {
        return new ShowProposalRepositoryJpa(Application.settings().getPersistenceUnitName());
    }

    @Override
    public ShowRepository shows(TransactionalContext autoTx) {
        return new ShowRepositoryJpa(autoTx);
    }

    @Override
    public ShowRepository shows() {
        return new ShowRepositoryJpa(Application.settings().getPersistenceUnitName());
    }

    @Override
    public CustomerRepository customers(TransactionalContext autoTx) {
        return new CustomerRepositoryJpa(autoTx);
    }

    @Override
    public CustomerRepository customers() {
        return new CustomerRepositoryJpa(Application.settings().getPersistenceUnitName());
    }

    @Override
    public FigureRepository figures(TransactionalContext autoTx) {
        return new FigureRepositoryJpa(autoTx);
    }

    @Override
    public FigureRepository figures() {
        return new FigureRepositoryJpa(Application.settings().getPersistenceUnitName());
    }

    @Override
    public FigureKeywordRepository keywords(TransactionalContext autoTx) {
        return new FigureKeywordRepositoryJpa(autoTx);
    }

    @Override
    public FigureKeywordRepository keywords() {
        return new FigureKeywordRepositoryJpa(Application.settings().getPersistenceUnitName());
    }


    @Override
    public RepresentativeRepository representatives(TransactionalContext autoTx){
        return new RepresentativeRepositoryJpa(autoTx);
    }

    @Override
    public RepresentativeRepository representatives(){
        return new RepresentativeRepositoryJpa(Application.settings().getPersistenceUnitName());
    }

    @Override
    public DroneModelRepository droneModels(TransactionalContext autoTx) {
        return new DroneModelRepositoryJpa(autoTx);
    }

    @Override
    public DroneModelRepository droneModels() {
        return new DroneModelRepositoryJpa(Application.settings().getPersistenceUnitName());
    }

    @Override
    public ShowProposalTemplateRepository showProposalTemplates(TransactionalContext autoTx) {
        return new ShowProposalTemplateRepositoryJpa(autoTx);
    }

    @Override
    public ShowProposalTemplateRepository showProposalTemplates() {
        return new ShowProposalTemplateRepositoryJpa(Application.settings().getPersistenceUnitName());
    }

    

    @Override
    public TransactionalContext newTransactionalContext() {
        return JpaAutoTxRepository.buildTransactionalContext(Application.settings().getPersistenceUnitName(),
                Application.settings().getExtendedPersistenceProperties());
    }

}
