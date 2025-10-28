# US316 Send show proposal to the customer

As CRM Collaborator, I want to send the show proposal to the customer.

# Customer Specifications
The proposal to be sent is a properly formated document with the show details and a link to the video
preview. The format of the proposal must be one supported by the system and has to be generated
using the correct plugin.
No proposal can be sent to the customer without prior successful tes ng of its show.

## Acceptance Criteria
- Verifying the show Proposal is ready to be sent (has testing validated)
- Changing show Proposal state to Waiting Approval
- Assuring a document with the show Proposal characteristics is created

## Dependencies
- #101 US310 Create Show Proposal

## Input and Output Data
*Input:*
- A Show Proposal to send

*Output:*
- A Document according to a template choosen filled with the Show Proposal information


## Definition of done
- Updated Show Proposal in the system
- An interface for the user exists
- There's tests implemented


![class diagram](docs/global-artifacts/analysis/US316/316-class-diagram.svg)

# Design
![sequence diagram](svg/sd.svg)

## Testing