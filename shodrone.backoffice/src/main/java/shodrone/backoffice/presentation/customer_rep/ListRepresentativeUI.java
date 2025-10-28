package shodrone.backoffice.presentation.customer_rep;
import eapli.framework.presentation.console.AbstractListUI;
import eapli.framework.presentation.console.SelectWidget;
import eapli.framework.visitor.Visitor;
import shodrone.backoffice.presentation.customer.CustomerPrinterShort;
import shodrone.core.application.customer_rep.ListRepresentativeController;
import shodrone.core.domain.customer.Customer;
import shodrone.core.domain.customer_rep.Representative;
import shodrone.core.presentation.console.ConsoleEvent;

public class ListRepresentativeUI extends AbstractListUI<Representative> {

	private final ListRepresentativeController theController = new ListRepresentativeController();

	@Override
	public boolean doShow() {
		Iterable<Customer> customers = theController.allCustomers();
		try{
			SelectWidget<Customer> selector = new SelectWidget<>("Select a customer", customers, new CustomerPrinterShort());
			selector.show();
			Customer selectedCustomer = selector.selectedElement();
			theController.setSelectedCustomer(selectedCustomer);	
		}catch(Exception e){
			ConsoleEvent.error(e.getMessage());
			return false;
		}
        System.out.println();
        ConsoleEvent.blue("Customer Representatives of " + theController.getSelectedCustomer().getCustomerName() + ":");
		super.doShow();
		return true;
	}

    @Override
    public String headline() {
        return "List Customer Representatives of a Customer";
    }

    @Override
    protected String emptyMessage() {
        return "No data.";
    }

    @Override
    protected Iterable<Representative> elements() {
        return theController.representativesOfSelectedCustomer();
    }

    @Override
    protected Visitor<Representative> elementPrinter() {
        return new RepresentativePrinter();
    }

    @Override
    protected String elementName() {
        return "Customer Representative";
    }

    @Override
    protected String listHeader() {
        return String.format("#  %-30s%-40s%-20s%-10s", "Name", "Email", "Position", "Status");
    }
	
}
