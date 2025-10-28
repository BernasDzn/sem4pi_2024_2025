#!/bin/bash
# filepath: /home/rui/code/sem4pi-2024-2025-sem4pi_2024_2025_g46/run_bootstrap.sh

case "$1" in
    -bs)
        cd shodrone.bootstrap
        mvn clean compile exec:java -Dexec.mainClass="shodrone.bootstrap.ShodroneBootstrap"
        ;;
    -cl)
        cd shodrone.customerapp
        mvn clean compile exec:java -Dexec.mainClass="shodrone.customerapp.ShodroneCustomerApp"
        ;;
    -bo)
        cd shodrone.backoffice
        mvn clean compile exec:java -Dexec.mainClass="shodrone.backoffice.ShodroneBackoffice"
        ;;
    *)
        echo "Usage: $0 {-bootstrap|-client|-backoffice}"
        exit 1
        ;;
esac