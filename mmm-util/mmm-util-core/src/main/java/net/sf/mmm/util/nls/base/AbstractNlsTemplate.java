/* $Id$
 * Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.util.nls.base;

import java.io.IOException;
import java.util.Locale;
import java.util.Map;

import net.sf.mmm.util.nls.api.NlsMessageFormatter;
import net.sf.mmm.util.nls.api.NlsTemplate;
import net.sf.mmm.util.nls.api.NlsTemplateResolver;

/**
 * This is the abstract base implementation of the {@link NlsTemplate}
 * interface.
 * 
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @since 1.0.0
 */
public abstract class AbstractNlsTemplate implements NlsTemplate {

  /**
   * The constructor.
   */
  public AbstractNlsTemplate() {

    super();
  }

  /**
   * This method creates an {@link NlsMessageFormatter} for the given
   * <code>messageTemplate</code> and <code>locale</code>.
   * 
   * @param messageTemplate is the template of the message for the given
   *        <code>locale</code>.
   * @param locale is the locale to use. The implementation may ignore it here
   *        because it is also supplied at
   *        {@link NlsMessageFormatter#format(Void, Locale, Map, NlsTemplateResolver)}
   *        . Anyhow it allows the implementation to do smart caching of the
   *        parsed formatter in association with the locale.
   * @return the formatter instance.
   */
  protected abstract NlsMessageFormatter createFormatter(String messageTemplate, Locale locale);

  /**
   * {@inheritDoc}
   */
  public boolean translate(Locale locale, Map<String, Object> arguments, Appendable buffer,
      NlsTemplateResolver resolver) throws IOException {

    String translation = translate(locale);
    if (translation == null) {
      return false;
    } else {
      NlsMessageFormatter formatter = createFormatter(translation, locale);
      formatter.format(null, locale, arguments, buffer, resolver);
      return true;
    }
  }

}
