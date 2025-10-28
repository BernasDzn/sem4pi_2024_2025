/*
 * Copyright (c) 2013-2024 the original author or authors.
 *
 * MIT License
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package shodrone.core.domain.user;

import eapli.framework.infrastructure.authz.domain.model.SystemUserBuilder;
import eapli.framework.util.Utility;
import shodrone.core.domain.signuprequest.SignupRequestBuilder;

/**
 * Helper class to create instances of SystemUserBuilder and
 * SignupRequestBuilder.
 */
@Utility
public class UserBuilderHelper {

    /**
     * Private constructor to prevent instantiation.
     */
    private UserBuilderHelper() {
        // ensure utility
    }

    /**
     * Creates a new SystemUserBuilder instance.
     * 
     * @return a new SystemUserBuilder instance
     */
    public static SystemUserBuilder builder() {
        return new SystemUserBuilder(new ShodronePasswordPolicy(), new ShodronePasswordEncoder());
    }

    /**
     * Creates a new SignupRequestBuilder instance.
     * 
     * @return a new SignupRequestBuilder instance
     */
    public static SignupRequestBuilder signupBuilder() {
        return new SignupRequestBuilder(new ShodronePasswordPolicy(), new ShodronePasswordEncoder());
    }
}
