package com.fullcycle.codeflix.subscription.domain.exceptions;

import com.fullcycle.codeflix.subscription.domain.validation.handler.Notification;

public class NotificationException extends DomainException {

    public NotificationException(final String aMessage, final Notification notification) {
        super(aMessage, notification.getErrors());
    }

    public static NotificationException with(final String message, final Notification notification) {
        return new NotificationException(message, notification);
    }
}
