# US318 Templates for show proposals

As CRM Manager, I want to be able to configure the template that formats the document to be sent to the customer.
# Customer Specifications
The plugin used to validate the proposal template must be previously registered in the
system.

# Acceptance Criteria
- Receive and validate a Text file
- Assign new Template for Show Proposals

## Dependencies
- #101 US310 Create Show Proposal

## Input and Output Data
*Input:*
- A text file to define as Show Proposal Template

*Output:*
- none


## Definition of done
- Text files can be inserted, validated and saved as the newest Template
- An interface for the user exists
- There's tests implemented


![class diagram](docs/global-artifacts/analysis/US318/318-class-diagram.svg)

# Design
![sequence diagram](svg/sd.svg)

## Testing