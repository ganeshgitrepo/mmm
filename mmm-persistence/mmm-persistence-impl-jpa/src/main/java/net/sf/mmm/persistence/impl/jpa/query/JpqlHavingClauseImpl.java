/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.persistence.impl.jpa.query;

import net.sf.mmm.persistence.api.jpa.query.JpqlHavingClause;

/**
 * This is the implementation of {@link JpqlHavingClause}.
 * 
 * @param <E> is the generic type of the {@link #getEntityType() entity type}.
 * 
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @since 1.0.0
 */
public class JpqlHavingClauseImpl<E> extends AbstractJpqlConditionalExpression<E, JpqlHavingClause<E>> implements
    JpqlHavingClause<E> {

  /**
   * The constructor.
   * 
   * @param context is the {@link JpqlQueryContext}.
   * @param propertyBasePath - see {@link #getPropertyBasePath()}.
   */
  public JpqlHavingClauseImpl(JpqlQueryContext<E> context, String propertyBasePath) {

    super(context, propertyBasePath);
    context.getQueryBuffer().append(JPQL_HAVING);
  }

}
