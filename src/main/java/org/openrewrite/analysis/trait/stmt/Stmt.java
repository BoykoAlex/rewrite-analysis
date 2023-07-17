/*
 * Copyright 2023 the original author or authors.
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
package org.openrewrite.analysis.trait.stmt;

import fj.data.Validation;
import lombok.AllArgsConstructor;
import org.openrewrite.Cursor;
import org.openrewrite.analysis.trait.Top;
import org.openrewrite.analysis.trait.TraitFactory;
import org.openrewrite.analysis.trait.util.TraitErrors;
import org.openrewrite.java.tree.Statement;

import java.util.UUID;

/**
 * A common super-class of all statements.
 */
public interface Stmt extends StmtParent {
    enum Factory implements TraitFactory<Stmt> {
        F;

        @Override
        public Validation<TraitErrors, Stmt> viewOf(Cursor cursor) {
            return TraitFactory.findFirstViewOf(
                    cursor,
                    ReturnStmt.Factory.F,
                    c -> StmtFallback.viewOf(c).map(o -> o)
            );
        }
    }

    static Validation<TraitErrors, Stmt> viewOf(Cursor cursor) {
        return Stmt.Factory.F.viewOf(cursor);
    }
}

@AllArgsConstructor
class StmtFallback extends Top.Base implements Stmt {
    Cursor cursor;
    Statement statement;

    @Override
    public UUID getId() {
        return statement.getId();
    }

    static Validation<TraitErrors, StmtFallback> viewOf(Cursor cursor) {
        if (cursor.getValue() instanceof Statement) {
            return Validation.success(new StmtFallback(cursor, cursor.getValue()));
        }
        return TraitErrors.invalidTraitCreationType(StmtFallback.class, cursor, Statement.class);
    }
}
