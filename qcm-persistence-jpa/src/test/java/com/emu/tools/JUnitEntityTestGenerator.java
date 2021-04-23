package com.emu.tools;

import com.emu.tools.generators.JunitClassTestGenerator;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class JUnitEntityTestGenerator {


    public static void main(String[] args) throws IOException, ClassNotFoundException {

        List <PackageTestGenerator.PackageToScan> packages = new ArrayList <>();

        packages.add(new PackageTestGenerator.PackageToScan("qcm-persistence", "com.emu.apps.qcm.spi.persistence.adapters.jpa.entity.category"));
        packages.add(new PackageTestGenerator.PackageToScan("qcm-persistence", "com.emu.apps.qcm.spi.persistence.adapters.jpa.entity.common"));
        packages.add(new PackageTestGenerator.PackageToScan("qcm-persistence", "com.emu.apps.qcm.spi.persistence.adapters.jpa.entity.questionnaires"));
        packages.add(new PackageTestGenerator.PackageToScan("qcm-persistence", "com.emu.apps.qcm.spi.persistence.adapters.jpa.entity.questions"));
        packages.add(new PackageTestGenerator.PackageToScan("qcm-persistence", "com.emu.apps.qcm.spi.persistence.adapters.jpa.entity.tags"));
        packages.add(new PackageTestGenerator.PackageToScan("qcm-persistence", "com.emu.apps.qcm.spi.persistence.adapters.jpa.entity.upload"));
        packages.add(new PackageTestGenerator.PackageToScan("qcm-persistence", "com.emu.apps.qcm.spi.persistence.adapters.jpa.entity.events"));

        new PackageTestGenerator(new JunitClassTestGenerator(), packages).run();

    }


}
