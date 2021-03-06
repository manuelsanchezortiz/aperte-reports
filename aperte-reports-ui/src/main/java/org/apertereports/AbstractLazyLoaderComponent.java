package org.apertereports;

import org.apertereports.util.NotificationUtil;
import org.apertereports.util.VaadinUtil;

import com.vaadin.ui.Component;
import com.vaadin.ui.CustomComponent;
import eu.livotov.tpt.gui.widgets.TPTLazyLoadingLayout;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A Vaadin component wrapper with lazy loading. Extending class should provide
 * its heavy logic (i.e. fetching data from db) in {@link #lazyLoad()} method.
 */
public abstract class AbstractLazyLoaderComponent extends CustomComponent implements TPTLazyLoadingLayout.LazyLoader {

    private static Logger logger = LoggerFactory.getLogger(AbstractLazyLoaderComponent.class);

    public abstract void lazyLoad() throws Exception;

    /**
     * Executes the lazy loading of the component.
     *
     * @param tptLazyLoadingLayout The TPT wrapper for lazy loading.
     * @return Returns the proper lazy loaded component.
     */
    @Override
    public Component lazyLoad(TPTLazyLoadingLayout tptLazyLoadingLayout) {
        try {
            lazyLoad();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            NotificationUtil.showExceptionNotification(getWindow(), "exception.gui.error", e.getMessage());
        }
        return this;
    }

    /**
     * Returns the message shown on lazy load (i.e. "The data is still
     * loading...").
     *
     * @return The message
     */
    @Override
    public String getLazyLoadingMessage() {
        return VaadinUtil.getValue("loading.data");
    }
}
