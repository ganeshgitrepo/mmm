/* $Id: ComponentManagerIF.java 191 2006-07-24 21:00:49Z hohwille $ */
package net.sf.mmm.framework.api;

/**
 * This is the interface of the manager of
 * {@link ComponentProviderIF components}. This is a simplistic view on the
 * {@link IocContainerIF container}.
 * 
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 */
public interface ComponentManagerIF {

    /**
     * the {@link #requestComponent(Class, String) instance-ID}
     * {@link #requestComponent(Class) used} as default.
     * 
     * Value: {@value}
     */
    String DEFAULT_INSTANCE_ID = "default";

    /**
     * This method determines if the componenet for the given
     * {@link ComponentDescriptorIF#getSpecification() specification} is
     * available. In that case, it can be retrieved via the
     * {@link #requestComponent(Class)} method without causing an "component not
     * registered" exception.
     * 
     * @param <S>
     *        is the
     *        {@link ComponentDescriptorIF#getSpecification() specification} of
     *        the requested component.
     * @param specification
     *        is the
     *        {@link ComponentDescriptorIF#getSpecification() specification} of
     *        the component.
     * @return <code>true</code> if a component for the given
     *         {@link ComponentDescriptorIF#getSpecification() specification} is
     *         registered, <code>false</code> otherwise.
     */
    <S> boolean hasComponent(Class<S> specification);

    /**
     * This method gets an instance of the component with the given
     * {@link ComponentDescriptorIF#getSpecification() specification}.
     * 
     * @see #requestComponent(Class, String)
     * @see ComponentProviderIF
     * 
     * @param <S>
     *        is the
     *        {@link ComponentDescriptorIF#getSpecification() specification} of
     *        the requested component.
     * @param specification
     *        is the
     *        {@link ComponentDescriptorIF#getSpecification() specification} of
     *        the requested component.
     * @return an instance of the requested component. It must fulfill the given
     *         {@link ComponentDescriptorIF#getSpecification() specification}
     *         and especially it must be possible to cast that instance to the
     *         type given as <code>componentSpecification</code>. It is
     *         illegal (should NOT be possible) to cast it to another type (that
     *         is no super-type).
     * @throws ComponentException
     *         if the requested component is NOT
     *         {@link #hasComponent(Class) available} or could not be provided.
     * @throws ContainerException
     *         if a fundamental problem occured (e.g. the container has NOT been
     *         started).
     */
    <S> S requestComponent(Class<S> specification) throws ComponentException, ContainerException;

    /**
     * This method gets an instance of the component with the given
     * {@link ComponentDescriptorIF#getSpecification() specification}.
     * 
     * @see ComponentProviderIF
     * 
     * @param <S>
     *        is the
     *        {@link ComponentDescriptorIF#getSpecification() specification} of
     *        the requested component.
     * @param specification
     *        is the
     *        {@link ComponentDescriptorIF#getSpecification() specification} of
     *        the requested component.
     * @param instanceId
     *        identifies a specific instance of the componnent for
     *        <code>specification</code> if there are multiple. The
     *        {@link #requestComponent(Class) default} is
     *        {@link #DEFAULT_INSTANCE_ID}.
     * @return an instance of the requested component. It must fulfill the given
     *         {@link ComponentDescriptorIF#getSpecification() specification}
     *         and especially it must be possible to cast that instance to the
     *         type given as <code>componentSpecification</code>. It is
     *         illegal (should NOT be possible) to cast it to another type (that
     *         is no super-type).
     * @throws ComponentException
     *         if the requested component is NOT
     *         {@link #hasComponent(Class) available} or could not be provided.
     * @throws ContainerException
     *         if a fundamental problem occured (e.g. the container has NOT been
     *         started).
     */
    <S> S requestComponent(Class<S> specification, String instanceId) throws ComponentException,
            ContainerException;

    /**
     * This method releases a component instance
     * {@link #requestComponent(Class) requested} via this manager that is not
     * needed anymore. This allows the {@link IocContainerIF container} to
     * complete the lifecycle of a component that is no longer needed.
     * 
     * @param <S>
     *        is the
     *        {@link ComponentDescriptorIF#getSpecification() specification} of
     *        the component to release.
     * @param component
     *        is the component instance NOT needed anymore.
     */
    <S> void releaseComponent(S component);

}
