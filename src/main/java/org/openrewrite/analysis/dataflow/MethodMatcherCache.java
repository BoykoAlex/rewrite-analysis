/*
 * Copyright 2022 the original author or authors.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * https://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.openrewrite.analysis.dataflow;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.openrewrite.Incubating;
import org.openrewrite.analysis.dataflow.internal.csv.GenericExternalModel;
import org.openrewrite.java.MethodMatcher;

import java.util.*;

@Incubating(since = "7.26.0")
@NoArgsConstructor(access = AccessLevel.PACKAGE, staticName = "create")
final class MethodMatcherCache {
    private final Map<org.openrewrite.analysis.dataflow.internal.csv.GenericExternalModel.MethodMatcherKey, MethodMatcher> methodMapperCache =
            new WeakHashMap<>();

    private MethodMatcher provideMethodMatcher(org.openrewrite.analysis.dataflow.internal.csv.GenericExternalModel.MethodMatcherKey key) {
        return methodMapperCache.computeIfAbsent(
                key,
                k -> new MethodMatcher(k.getSignature(), k.isMatchOverrides())
        );
    }

    Collection<MethodMatcher> provideMethodMatchers(Collection<? extends org.openrewrite.analysis.dataflow.internal.csv.GenericExternalModel> models) {
        List<MethodMatcher> mms = new ArrayList<>();
        for (org.openrewrite.analysis.dataflow.internal.csv.GenericExternalModel model : models) {
            GenericExternalModel.MethodMatcherKey asMethodMatcherKey = model.asMethodMatcherKey();
            MethodMatcher methodMatcher = provideMethodMatcher(asMethodMatcherKey);
            mms.add(methodMatcher);
        }
        return mms;
    }
}
