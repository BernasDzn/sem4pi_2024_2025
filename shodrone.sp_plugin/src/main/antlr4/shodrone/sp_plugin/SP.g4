grammar SP;

options {
  caseInsensitive = true;
}

// Entry point
parse: proposal EOF;

proposal: header body closing annex;

header:
    ANY_TEXT NL+
    (CUSTOMER_REP_NAME NL+)*
    COMPANY_NAME NL+
    ADDRESS NL+
    VAT_NUMBER NL+
    ANY_TEXT PROPOSAL_NUMBER ANY_TEXT DATE NL+
    ('Proposta de Show'|'Show Proposal') NL+
    ;
body:
    COMPANY_NAME*
    (ANY_TEXT NL*)*
    LINK_SIMULATION_VIDEO NL*
    (ANY_TEXT NL*)*
    INSURANCE_AMOUNT NL*
    (ANY_TEXT NL*)*
    ;

closing:
    ('Melhores cumprimentos,'|'Best regards,') NL+
    CRM_MANAGER_NAME NL+
    'CRM Manager' NL+
    PAGE_BREAK NL+
    ;

annex:
    ANNEX_HEADER PROPOSAL_NUMBER NL+
    LOCATION_LABEL GPS_COORDINATES NL+
    DATE_LABEL DATE_EVENT NL+
    TIME_LABEL TIME_EVENT NL+
    DURATION_LABEL DURATION MINUTES_LABEL NL+
    drone_list figure_list
    ;

drone_list:
    DRONELIST_HEADER NL+
    (MODEL HYPHEN QUANTITY UNITS_LABEL NL+)+
    '...' NL+
    ;

figure_list:
    FIGURELIST_HEADER NL+
    (POSITION HYPHEN FIGURE_NAME NL+)+
    '...' NL*
;

CUSTOMER_REP_NAME: '[Customer Representative Name]';
COMPANY_NAME: '[Company Name]';
ADDRESS: '[Address with postal code and country]';
VAT_NUMBER: '[VAT Number]';
PROPOSAL_NUMBER: ('[proposal number]'|'[show proposal number]');
DATE: '[date]';
DATE_EVENT: '[date of the event]';
TIME_EVENT: '[time of the event]';
LINK_SIMULATION_VIDEO: ('[link to show\'s simulation video]'|'[link to show video]');
INSURANCE_AMOUNT: ('[valor do seguro]'|'[insurance amount]');
CRM_MANAGER_NAME: '[CRM Manager Name]';
GPS_COORDINATES: '[GPS coordinates of the location]';
DURATION: '[duration]';
MODEL: '[model]';
QUANTITY: '[quantity]';
POSITION: '[position in show]';
FIGURE_NAME: '[figure name]';
PAGE_BREAK: '[page break]';

ANNEX_HEADER: ('Anexo' | 'Attachment') WS* ('-'|'–') WS* ('Detalhes do Show' | 'Show Details') WS*;
LOCATION_LABEL: ('Local de realização' | 'Location') WS* ('-'|'–') WS*;
DATE_LABEL: ('Data' | 'Date') WS* ('-'|'–') WS*;
TIME_LABEL: ('Hora' | 'Time') WS* ('-'|'–') WS*;
DURATION_LABEL: ('Duração' | 'Duration') WS* ('-'|'–') WS*;
MINUTES_LABEL: WS*('minutos'|'minutes')WS*;
UNITS_LABEL: WS* ('unidades.' | 'units.') WS*;
DRONELIST_HEADER: ('#Lista de drones utilizados'|'#List of used drones');
FIGURELIST_HEADER: ('#Lista de figuras'|'#List of figures');

HYPHEN: WS*('-'|'–')WS*;

NL: ('\r'? '\n')+;
WS: [ \t]+ -> skip;
ANY_TEXT: ~('[' | ']' | '\r' | '\n')+;