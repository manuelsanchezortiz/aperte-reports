package org.apertereports;

import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.EventRequest;
import javax.portlet.EventResponse;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import javax.portlet.ResourceRequest;
import javax.portlet.ResourceResponse;

import org.apertereports.common.exception.ARRuntimeException;
import org.apertereports.common.users.User;
import org.apertereports.common.users.UserRole;
import org.apertereports.util.NotificationUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.liferay.portal.kernel.util.WebKeys;
import com.liferay.portal.model.Role;
import com.liferay.portal.theme.ThemeDisplay;
import com.liferay.portal.util.PortalUtil;
import com.vaadin.event.ListenerMethod;
import com.vaadin.terminal.gwt.server.PortletApplicationContext2;
import com.vaadin.ui.Panel;
import com.vaadin.ui.Window;

import eu.livotov.tpt.TPTApplication;
import eu.livotov.tpt.i18n.TM;

/**
 * This is a stub abstract class for all application portlets. Extending classes
 * should initialize themselves by overriding {@link #portletInit()}.
 *
 * @param <T> Type of main panel
 */
public abstract class AbstractReportingApplication<T extends Panel> extends TPTApplication implements
        PortletApplicationContext2.PortletListener {

    private static final Logger logger = LoggerFactory.getLogger(AbstractReportingApplication.class);
    /**
     * Main window object
     */
    protected Window mainWindow;
    /**
     * Main panel
     */
    protected T mainPanel;
    /**
     * Liferay user.
     */
    private User user;
    /**
     * User locale.
     */
    private Locale locale;
    
    /**
     * Current group
     */
    private Long groupId;
    
    /**
     * Initializes the application context.
     */
    @Override
    public void applicationInit() {
        if (getContext() instanceof PortletApplicationContext2) {
            ((PortletApplicationContext2) getContext()).removePortletListener(this, this);
            ((PortletApplicationContext2) getContext()).addPortletListener(this, this);
        }

        TM.getDictionary().setDefaultLanguage(getLocale().getLanguage());
        reloadDictionary();
        portletInit();

        setMainWindow(mainWindow);
    }

    @Override
    public void close() {
        logger.info(" ----------------- CLOSING APPLICATION ------------");
        super.close();
    }

    /**
     * Initializes the portlet GUI.
     */
    protected abstract void portletInit();

    /**
     * Reinitializes user data when the user is logged on or logged off.
     *
     * @param user User, can be null when user is not logged
     */
    protected abstract void reinitUserData(User user);

    /**
     * This method should be overriden to implement a custom behavior on a first
     * application startup.
     */
    @Override
    public void firstApplicationStartup() {
        // override to implement a custom behavior
    }

    /**
     * Reloads the dictionary manually.
     */
    private void reloadDictionary() {
        // File themeFolder = new File(getContext().getBaseDirectory(),
        // String.format("VAADIN/themes/%s", VRIES_THEME));
        // if (themeFolder.exists() && themeFolder.isDirectory()) {
        // try {
        // TM.getDictionary().loadTranslationFilesFromThemeFolder(themeFolder);
        // }
        // catch (IOException e) {
        // ExceptionUtils.logSevereException(e);
        // }
        // }
    }

    /**
     * Logs uncaught exceptions to logger and presents them to the user.
     *
     * @param event Error event
     */
    @Override
    public void terminalError(com.vaadin.terminal.Terminal.ErrorEvent event) {
        Throwable throwable = event.getThrowable();
        if (throwable instanceof Exception) {
            Exception e = (Exception) throwable;
            logger.error(e.getMessage(), throwable);
        }
        /**
         * Exceptions thrown inside Vaadin listeners methods are wrapped by
         * MethodException
         */
        if (throwable instanceof ListenerMethod.MethodException) {
            throwable = ((ListenerMethod.MethodException) throwable).getCause();
        }

        if (throwable instanceof ARRuntimeException) {
            ARRuntimeException vre = (ARRuntimeException) throwable;
            NotificationUtil.showExceptionNotification(getMainWindow(), vre);
        } else {
            super.terminalError(event);
        }
    }

    /**
     * Provides information about user and the locale for the application.
     *
     * @param request Render request
     * @param response Render response
     * @param window Vaadin window
     */
    @Override
    public void handleRenderRequest(RenderRequest request, RenderResponse response, Window window) {
        logger.debug("RENDER REQUEST, " + getClass().getSimpleName());
        if (getContext() instanceof PortletApplicationContext2) {
            try {
                com.liferay.portal.model.User liferayUser = PortalUtil.getUser(request);

                //liferay user can be null because he can be not logged in 
                if (liferayUser != null && (user == null || user.getLogin().equals(liferayUser.getLogin()))) {
                	long userId= liferayUser.getUserId();
                    String login = liferayUser.getLogin();
                    String email = liferayUser.getEmailAddress();
                    Set<UserRole> roles = new HashSet<UserRole>();
                    boolean admin = false;

                    for (Role r : liferayUser.getRoles()) {
                        boolean adminRole = "administrator".equalsIgnoreCase(r.getName());
                        UserRole ur = new UserRole(r.getName(), r.getRoleId(), adminRole);
                        roles.add(ur);
                        admin |= adminRole;
                    }

                    user = new User(userId, login, roles, admin, email);
                    reinitUserData(user);
                }

                if (liferayUser == null && user != null) {  //check if user logged off
                    user = null;
                    reinitUserData(user);
                }

                locale = PortalUtil.getLocale(request);
                
                groupId= this.getThemeDisplay(request).getLayout().getGroupId();
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
                throw new RuntimeException(e);
            }
        }
        logger.debug("RENDER REQUEST END, " + getClass().getSimpleName());
    }

    private ThemeDisplay getThemeDisplay(RenderRequest request){
    	ThemeDisplay themeDisplay = 
    			(ThemeDisplay)request.getAttribute(WebKeys.THEME_DISPLAY);
    	
    	return themeDisplay;
    }
    
    /**
     * Handles portlet action request however not used in the application.
     *
     * @param request Render request
     * @param response Render response
     * @param window Vaadin window
     */
    @Override
    public void handleActionRequest(ActionRequest request, ActionResponse response, Window window) {
        // do nothing
    }

    /**
     * Handles portlet event request however not used in the application.
     *
     * @param request Render request
     * @param response Render response
     * @param window Vaadin window
     */
    @Override
    public void handleEventRequest(EventRequest request, EventResponse response, Window window) {
        // do nothing
    }

    /**
     * Handles portlet resource request however not used in the application.
     *
     * @param request Render request
     * @param response Render response
     * @param window Vaadin window
     */
    @Override
    public void handleResourceRequest(ResourceRequest request, ResourceResponse response, Window window) {
        // do nothing
    }

    /**
     * Returns the user of the last request
     *
     * @return The user
     */
    public User getArUser() {
        return user;
    }

    /**
     * Returns application locale
     * @return Locale
     */
    public Locale getArLocale() {
        return locale;
    }
    
    public Long getGroupId(){
    	return this.groupId;
    }
}
