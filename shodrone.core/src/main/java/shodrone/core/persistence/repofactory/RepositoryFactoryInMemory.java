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
import eapli.framework.infrastructure.authz.repositories.impl.inmemory.InMemoryUserRepository;
import shodrone.core.domain.user.ShodroneRoles;
import shodrone.core.domain.user.UserBuilderHelper;
import shodrone.core.persistence.customer.CustomerRepository;
import shodrone.core.persistence.customer.CustomerRepositoryInMemory;
import shodrone.core.persistence.customer_rep.RepresentativeRepository;
import shodrone.core.persistence.customer_rep.RepresentativeRepositoryInMemory;
import shodrone.core.persistence.drone_model.DroneModelRepository;
import shodrone.core.persistence.drone_model.DroneModelRepositoryInMemory;
import shodrone.core.persistence.figure.FigureRepository;
import shodrone.core.persistence.figure.FigureRepositoryInMemory;
import shodrone.core.persistence.figurecategory.FigureCategoryRepository;
import shodrone.core.persistence.show.ShowRepository;
import shodrone.core.persistence.show.ShowRepositoryInMemory;
import shodrone.core.persistence.figure.figurekeyword.FigureKeywordRepository;
import shodrone.core.persistence.figure.figurekeyword.FigureKeywordRepositoryInMemory;
import shodrone.core.persistence.showproposal.ShowProposalRepository;
import shodrone.core.persistence.showproposal.ShowProposalRepositoryInMemory;
import shodrone.core.persistence.showproposaltemplate.ShowProposalTemplateRepository;
import shodrone.core.persistence.showproposaltemplate.ShowProposalTemplateRepositoryInMemory;
import shodrone.core.persistence.showrequest.ShowRequestRepository;
import shodrone.core.persistence.showrequest.ShowRequestRepositoryInMemory;
import shodrone.core.persistence.signuprequest.SignupRequestRepository;
import shodrone.core.persistence.signuprequest.SignupRequestRepositoryInMemory;
/**
 *
 * Created by nuno on 20/03/16.
 */
public class RepositoryFactoryInMemory implements RepositoryFactory {

	@Override
	public UserRepository users(final TransactionalContext tx) {
		final var repo = new InMemoryUserRepository();
		// ensure we have at least a power user to be able to use the application
		final var userBuilder = UserBuilderHelper.builder();
		userBuilder.withUsername("poweruser").withPassword("Password1").withName("joe", "power")
				.withEmail("joe@email.org").withRoles(ShodroneRoles.POWER_USER);
		final var newUser = userBuilder.build();
		repo.save(newUser);
		return repo;
	}

	@Override
	public UserRepository users() {
		return users(null);
	}

	@Override
	public SignupRequestRepository signupRequests() {
		return signupRequests(null);
	}

	@Override
	public FigureCategoryRepository figureCategories(TransactionalContext autoTx) {
		return null;
	}

	@Override
	public FigureCategoryRepository figureCategories() {
		return null;
	}

	@Override
	public ShowRequestRepository showRequests(final TransactionalContext tx) {
		return new ShowRequestRepositoryInMemory();
	}

	@Override
	public ShowRequestRepository showRequests() {
		return null;
	}

	@Override
	public ShowProposalRepository showProposals(TransactionalContext autoTx) {
		return new ShowProposalRepositoryInMemory();
	}

	@Override
	public ShowProposalRepository showProposals() {
		return showProposals(null);
	}

	@Override
	public ShowRepository shows(TransactionalContext autoTx) {
		return new ShowRepositoryInMemory();
	}

	@Override
	public ShowRepository shows() {
		return shows(null);
	}

	@Override
	public CustomerRepository customers(TransactionalContext autoTx) {
		return new CustomerRepositoryInMemory();
	}

	@Override
	public CustomerRepository customers() {
		return customers(null);
	}

	@Override
	public FigureRepository figures(TransactionalContext autoTx) {
		return new FigureRepositoryInMemory();
	}

	@Override
	public FigureRepository figures() {
		return figures(null);
	}

	@Override
	public FigureKeywordRepository keywords(TransactionalContext autoTx) {
		return null;
	}

	@Override
	public FigureKeywordRepository keywords() {
		return new FigureKeywordRepositoryInMemory();
	}

	@Override
	public RepresentativeRepository representatives(TransactionalContext autoTx){
		return new RepresentativeRepositoryInMemory();
	}

	@Override
	public RepresentativeRepository representatives(){
		return representatives(null);
	}

	@Override
	public DroneModelRepository droneModels(TransactionalContext autoTx) {
		return new DroneModelRepositoryInMemory();
	}

	@Override
	public DroneModelRepository droneModels() {
		return droneModels(null);
	}

	@Override
	public ShowProposalTemplateRepository showProposalTemplates(TransactionalContext autoTx) {
		return new ShowProposalTemplateRepositoryInMemory();
	}

	@Override
	public ShowProposalTemplateRepository showProposalTemplates() {
		return showProposalTemplates(null);
	}


	@Override
	public SignupRequestRepository signupRequests(final TransactionalContext tx) {
		return new SignupRequestRepositoryInMemory();
	}

	@Override
	public TransactionalContext newTransactionalContext() {
		// in memory does not support transactions...
		return null;
	}

}
