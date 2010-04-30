/* $Id$
 * Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.util.io.base;

import java.io.Closeable;
import java.io.Flushable;
import java.io.IOException;
import java.io.Writer;

/**
 * This class is a {@link Writer} that adapts an {@link Appendable}.
 * 
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @since 2.0.0
 */
public class AppendableWriter extends Writer {

  /** The delegate. */
  private final Appendable appendable;

  /**
   * The constructor.
   * 
   * @param appendable is the {@link Appendable} to adapt.
   */
  public AppendableWriter(Appendable appendable) {

    super();
    this.appendable = appendable;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void close() throws IOException {

    if (this.appendable instanceof Closeable) {
      ((Closeable) this.appendable).close();
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void flush() throws IOException {

    if (this.appendable instanceof Flushable) {
      ((Flushable) this.appendable).flush();
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Writer append(char c) throws IOException {

    this.appendable.append(c);
    return this;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Writer append(CharSequence csq) throws IOException {

    this.appendable.append(csq);
    return this;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Writer append(CharSequence csq, int start, int end) throws IOException {

    this.appendable.append(csq, start, end);
    return this;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void write(char[] buffer) throws IOException {

    append(new String(buffer));
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void write(String string) throws IOException {

    append(string);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void write(String string, int offset, int length) throws IOException {

    append(string, offset, offset + length);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void write(char[] buffer, int offset, int length) throws IOException {

    append(new String(buffer, offset, length));
  }

  /**
   * This method gets the {@link Appendable} to delegate to.
   * 
   * @return the appendable
   */
  public Appendable getAppendable() {

    return this.appendable;
  }
}
