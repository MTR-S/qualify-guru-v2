package com.dev.mtrs.projects.qualifyguruv2;

import org.junit.jupiter.api.Test;
import org.springframework.modulith.core.ApplicationModules;
import org.springframework.modulith.docs.Documenter;

public class ModularityTests {

    @Test
    void verifiesAndCreateDocsOfTheModularStructure() {
        ApplicationModules modules = ApplicationModules.of(QualifyGuruV2Application.class);

        modules.verify();

        new Documenter(modules)
                .writeModulesAsPlantUml()
                .writeIndividualModulesAsPlantUml();
    }
}
