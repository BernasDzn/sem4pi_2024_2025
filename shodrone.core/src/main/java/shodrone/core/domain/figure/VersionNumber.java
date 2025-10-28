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

import eapli.framework.domain.model.ValueObject;
import eapli.framework.strings.util.StringPredicates;
import jakarta.persistence.Embeddable;

/**
 *
 * @author Jorge Santos ajs@isep.ipp.pt
 */
@Embeddable
public class VersionNumber implements ValueObject, Comparable<VersionNumber> {


    private String version;

    public VersionNumber(final String VersionNumber) {
        if (StringPredicates.isNullOrEmpty(VersionNumber)) {
            throw new IllegalArgumentException(
                    "Version Number should not be empty");
        }
        // TODO validate invariants,
        // expression
        this.version = VersionNumber;
    }

    protected VersionNumber() {
        // for ORM
    }

    public static VersionNumber valueOf(final String versionNumber) {
        return new VersionNumber(versionNumber);
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof VersionNumber)) {
            return false;
        }

        final VersionNumber that = (VersionNumber) o;
        return this.version.equals(that.version);
    }

    @Override
    public String toString() {
        return this.version;
    }

    @Override
    public int compareTo(VersionNumber o) {
        return version.compareTo(o.version);
    }
}
