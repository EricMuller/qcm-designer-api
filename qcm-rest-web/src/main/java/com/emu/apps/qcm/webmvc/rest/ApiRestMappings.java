/*
 *
 * The MIT License (MIT)
 *
 * Copyright (c)  2019 qcm-rest-api
 * Author  Eric Muller
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 *
 *
 */

package com.emu.apps.qcm.webmvc.rest;

public final class ApiRestMappings {

    public static final String PUBLIC_API = "/qcm/api/v1";

    public static final String GUEST_API = "/guest/qcm/api/v1";

    public static final String PUBLIC_QUESTIONNAIRES = PUBLIC_API + "/questionnaires";

    public static final String PUBLIC_CATEGORIES = PUBLIC_API + "/categories";

    public static final String PUBLIC_QUESTIONS = PUBLIC_API + "/questions";

    public static final String PUBLIC_SUGGEST = PUBLIC_API + "/suggest";

    public static final String PUBLIC_TAGS = PUBLIC_API + "/tags";

    public static final String PUBLIC_UPLOADS = PUBLIC_API + "/upload";

    public static final String PUBLIC_USERS = PUBLIC_API + "/users";

    public static final String GUEST_EXPORTS = GUEST_API + "/exports";

    public static final String GUEST_QUESTIONNAIRES = GUEST_API + "/questionnaires";

    public static final String GUEST_CATEGORIES = GUEST_API + "/categories";

    private ApiRestMappings() {
        //nop
    }
}
