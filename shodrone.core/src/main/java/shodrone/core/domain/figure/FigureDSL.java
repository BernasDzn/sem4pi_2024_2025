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
package shodrone.core.domain.figure;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Embeddable;
import jakarta.persistence.Embedded;
import lombok.Getter;
import shodrone.core.domain.common.DSL;

/**
 *
 * @author Jorge Santos ajs@isep.ipp.pt
 */
@Embeddable
public class FigureDSL implements Comparable<FigureDSL> {

    @Getter
    @Embedded
    private DSL dsl;

    @Getter
    private List<String> drone_types = new ArrayList<>();

    /**
     * Constructor for FigureDSL.
     *
     * @param DSLCode    the code of the DSL
     */

    public FigureDSL(String DSLCode, String DSLVersion, List<String> drones) {

        this.dsl = DSL.valueOf(DSLVersion, DSLCode);
        this.drone_types.addAll(drones);
    }
    protected FigureDSL() {
        // for ORM
    }
    
    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof FigureDSL)) {
            return false;
        }

        final FigureDSL that = (FigureDSL) o;
        return dsl.getDslSyntax() == (that.getDsl().getDslSyntax());
    }

    @Override
    public String toString() {
        return dsl.getDslSyntax();
    }

    @Override
    public int compareTo(FigureDSL o) {
        return 0;
    }

}
